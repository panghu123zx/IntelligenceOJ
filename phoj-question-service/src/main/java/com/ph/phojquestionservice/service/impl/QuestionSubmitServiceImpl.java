package com.ph.phojquestionservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ph.phojclient.service.UserClientService;
import com.ph.phojcommon.common.ErrorCode;
import com.ph.phojcommon.constant.CommonConstant;
import com.ph.phojcommon.exception.BusinessException;
import com.ph.phojcommon.utils.SqlUtils;
import com.ph.phojmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.ph.phojmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.ph.phojmodel.model.entity.Question;
import com.ph.phojmodel.model.entity.QuestionSubmit;
import com.ph.phojmodel.model.entity.User;
import com.ph.phojmodel.model.enums.LanguageEnum;
import com.ph.phojmodel.model.enums.SubmitStatusEnum;
import com.ph.phojmodel.model.vo.QuestionSubmitVO;
import com.ph.phojmodel.model.vo.QuestionVO;
import com.ph.phojmodel.model.vo.UserVO;
import com.ph.phojquestionservice.manager.RedissonLock;
import com.ph.phojquestionservice.mapper.QuestionSubmitMapper;
import com.ph.phojquestionservice.rabbitMQ.MessageProducer;
import com.ph.phojquestionservice.service.QuestionService;
import com.ph.phojquestionservice.service.QuestionSubmitService;
import org.apache.commons.lang3.ObjectUtils;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 杨志亮
 * @description 针对表【question_submit(题目提交)】的数据库操作Service实现
 * @createDate 2024-11-04 19:31:03
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;
    @Resource
    private UserClientService userClientService;

    @Resource
    private RedissonLock redissonLock;

    @Resource
    private MessageProducer messageProducer;


    @Resource
    private RedissonClient redissonClient;


    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        long userId = loginUser.getId();
        String language = questionSubmitAddRequest.getLanguage();
        LanguageEnum enumByValue = LanguageEnum.getEnumByValue(language);
        if (enumByValue == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        // 判断实体是否存在，根据类别获取实体
        Long questionId = questionSubmitAddRequest.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setUserId(userId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setJudgeInfo("{}");
        questionSubmit.setLanguage(language);
        questionSubmit.setStatus(SubmitStatusEnum.WAITING.getValue());


        String lockKey = "doQuestionSubmit" + userId + ":" + questionId;
        Boolean save = redissonLock.lockExecute(lockKey, () -> this.save(questionSubmit));
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        Long questionSubmitId = questionSubmit.getId();
        // 异步执行 执行判题服务
        messageProducer.sendMessage(String.valueOf(questionSubmitId));


        return questionSubmitId;
    }

    /**
     * 获取查询包装类
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }

        Long id = questionSubmitQueryRequest.getId();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String language = questionSubmitQueryRequest.getLanguage();
        String code = questionSubmitQueryRequest.getCode();
        Integer status = questionSubmitQueryRequest.getStatus();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(code), "code", code);
        queryWrapper.eq(SubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


    /**
     * 获取题目提交封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        // 1. 关联查询用户信息
        Long userId = questionSubmit.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userClientService.getById(userId);
        }
        UserVO userVO = userClientService.getUserVO(user);
        questionSubmitVO.setUserVO(userVO);
        //todo 将题目的VO信息封装到submitVO中
        //脱敏 只有管理员和自己可以看到自己的代码
        if (!Objects.equals(loginUser.getId(), userId) && !userClientService.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    /**
     * 分页获取题目提交封装
     *
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollUtil.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }

        //填充信息，调用获取一条信息的接口
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser))
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }

    /**
     * 将题目信息封装到QuestionSubmitVo中
     *
     * @param request
     * @param questionSubmit
     * @param questionSubmitVO
     */
    @Override
    public void obtainQuestionVO(HttpServletRequest request, QuestionSubmit questionSubmit, QuestionSubmitVO questionSubmitVO) {
        // 2. 关联查询题目信息
        Long questionId = questionSubmit.getQuestionId();
        Question question = null;
        if (questionId != null && questionId > 0) {
            question = questionService.getById(questionId);
        }
        QuestionVO questionVO = questionService.getQuestionVO(question, request);
        questionSubmitVO.setQuestionVO(questionVO);
    }


}




