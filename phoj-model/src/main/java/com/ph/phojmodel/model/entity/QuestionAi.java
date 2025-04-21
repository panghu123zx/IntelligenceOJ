package com.ph.phojmodel.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ai表
 * @TableName question_ai
 */
@TableName(value ="question_ai")
@Data
public class QuestionAi implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * ai提问的内容
     */
    private String genContent;

    /**
     * ai输出结果
     */
    private String genRes;

    /**
     * 使用次数
     */
    private Integer useNum;

    /**
     * 剩余次数
     */
    private Integer remainNum;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 提交 id
     */
    private Long questionSubmitId;
    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}