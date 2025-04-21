package com.ph.phojquestioncomment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ph.phojclient.service.UserClientService;
import com.ph.phojcommon.common.ErrorCode;
import com.ph.phojcommon.exception.BusinessException;
import com.ph.phojmodel.model.dto.questionComment.QuestionCommentAddRequest;
import com.ph.phojmodel.model.dto.questionComment.QuestionCommentDeleteRequest;
import com.ph.phojmodel.model.entity.QuestionComment;
import com.ph.phojmodel.model.entity.User;
import com.ph.phojmodel.model.vo.QuestionCommentVO;
import com.ph.phojquestioncomment.service.QuestionCommentService;
import com.ph.phojquestioncomment.mapper.QuestionCommentMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 杨志亮
 * @description 针对表【question_comment(评论)】的数据库操作Service实现
 * @createDate 2024-11-24 17:49:43
 */
@Service
public class QuestionCommentServiceImpl extends ServiceImpl<QuestionCommentMapper, QuestionComment>
        implements QuestionCommentService {


    @Resource
    private UserClientService userClientService;

    /**
     * 根据题目id获取到所有的评论
     *
     * @param questionId
     * @return
     */
    @Override
    public List<QuestionCommentVO> getAllQuestionComment(long questionId) {
        //查找所有的父级评论
        QueryWrapper<QuestionComment> qwFather = new QueryWrapper<>();
        qwFather.eq("questionId", questionId);
        qwFather.eq("parentId", -1);
        List<QuestionComment> questionCommentList = this.list(qwFather);
        List<QuestionCommentVO> questionCommentVoList = new ArrayList<>();
        //转化为vo类型
        for (QuestionComment questionComment : questionCommentList) {
            User user = userClientService.getById(questionComment.getUserId());
            QuestionCommentVO questionCommentVO = QuestionCommentVO.objToVo(questionComment);
            if(user.getUserName()!=null){
                questionCommentVO.setUserName(user.getUserName());
            }
            if(user.getUserAvatar()!=null){
                questionCommentVO.setUserAvatar(user.getUserAvatar());
            }
            questionCommentVoList.add(questionCommentVO);
        }
        //根据父级评论查询子级评论
        for (QuestionCommentVO questionCommentVO : questionCommentVoList) {
            QueryWrapper<QuestionComment> qwChild = new QueryWrapper<>();
            qwChild.eq("parentId", questionCommentVO.getId());
            List<QuestionComment> questionCommentListChild = this.list(qwChild);
            List<QuestionCommentVO> questionCommentVOChildList = new ArrayList<>();
            for (QuestionComment questionComment : questionCommentListChild) {
                User user = userClientService.getById(questionComment.getUserId());
                QuestionCommentVO questionCommentVOChild = QuestionCommentVO.objToVo(questionComment);
                if(user.getUserName()!=null){
                    questionCommentVOChild.setUserName(user.getUserName());
                }
                if(user.getUserAvatar()!=null){
                    questionCommentVOChild.setUserAvatar(user.getUserAvatar());
                }
                questionCommentVOChildList.add(questionCommentVOChild);
            }
            //将子级的评论放进去
            questionCommentVO.setCommentVOChildList(questionCommentVOChildList);
        }
        return questionCommentVoList;
    }

    /**
     * 删除用户评论的一条内容，
     *
     * @param questionCommentDeleteRequest
     * @param loginUser
     * @return
     */
    @Override
    public boolean deleteCommentById(QuestionCommentDeleteRequest questionCommentDeleteRequest, User loginUser) {
        QuestionComment questionComment = new QuestionComment();
        BeanUtils.copyProperties(questionCommentDeleteRequest,questionComment);
        //如果没有子级评论就直接删除
        QueryWrapper<QuestionComment> qw = new QueryWrapper<>();
        Long id = questionComment.getId();
        qw.eq("parentId", id);
        long childCount = this.count(qw);
        if (childCount == 0) {
            boolean remove = this.removeById(id);
            if(!remove){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"删除失败");
            }
            // todo  子级评论删除之后查看父级的如果父级的也被删除了，也将父级的删除了
        }else {
            //有子级评论就将内容更改为 “评论已被删除”
            questionComment.setContent("评论已被删除");
            boolean update = this.updateById(questionComment);
            if(!update){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"删除失败");
            }
        }

        return true;
    }


    /**
     * 添加评论
     * @param questionCommentAddRequest
     * @param request
     * @return
     */
    @Override
    public boolean addComment(QuestionCommentAddRequest questionCommentAddRequest, HttpServletRequest request) {
        if (questionCommentAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "评论内容不能为空");
        }
        QuestionComment questionComment = new QuestionComment();
        BeanUtils.copyProperties(questionCommentAddRequest,questionComment);
        //直接进行添加操作
        boolean save = this.save(questionComment);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "添加评论失败");
        }
        return true;
    }


    /**
     * 修改评论
     *
     * @param questionComment
     * @return
     */
    @Override
    public boolean updateComment(QuestionComment questionComment) {
        if (questionComment == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        return this.updateById(questionComment);
    }
}




