package com.ph.phojmodel.model.dto.questionsubmit;

import lombok.Data;

@Data
public class judgeInfo {
    /**
     * 执行信息
     */
    private String message;
    /**
     * 消耗内存
     */
    private Long memory;
    /**
     * 消耗时间
     */
    private Long time;
}
