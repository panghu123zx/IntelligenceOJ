package com.ph.phojquestionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ph.phojcommon.constant.CommonConstant;
import com.ph.phojcommon.utils.SqlUtils;
import com.ph.phojmodel.model.dto.QuestionAi.QuestionAiQueryRequest;
import com.ph.phojmodel.model.entity.QuestionAi;
import com.ph.phojmodel.model.enums.SubmitStatusEnum;
import com.ph.phojquestionservice.service.QuestionAiService;
import com.ph.phojquestionservice.mapper.QuestionAiMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

/**
* @author 杨志亮
* @description 针对表【question_ai(题目表)】的数据库操作Service实现
* @createDate 2024-11-24 23:01:21
*/
@Service
public class QuestionAiServiceImpl extends ServiceImpl<QuestionAiMapper, QuestionAi>
    implements QuestionAiService{

    @Override
    public Wrapper<QuestionAi> getQueryWrapper(QuestionAiQueryRequest questionAiQueryRequest) {
        QueryWrapper<QuestionAi> queryWrapper = new QueryWrapper<>();
        if(questionAiQueryRequest==null){
            return queryWrapper;
        }
        Long id = questionAiQueryRequest.getId();
        String genContent = questionAiQueryRequest.getGenContent();
        Long questionId = questionAiQueryRequest.getQuestionId();
        Long questionSubmitId = questionAiQueryRequest.getQuestionSubmitId();
        Long userId = questionAiQueryRequest.getUserId();
        Integer status = questionAiQueryRequest.getStatus();
        String sortField = questionAiQueryRequest.getSortField();
        String sortOrder = questionAiQueryRequest.getSortOrder();



        // 拼接查询条件
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionSubmitId), "questionSubmitId", questionSubmitId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(genContent), "genContent", genContent);
        queryWrapper.eq(SubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq("isDelete", 0);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;


    }
}




