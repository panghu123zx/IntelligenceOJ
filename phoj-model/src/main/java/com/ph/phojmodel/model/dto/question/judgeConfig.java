package com.ph.phojmodel.model.dto.question;

import lombok.Data;

@Data
public class judgeConfig {
    /**
     * 时间限制
     */
    private Long timeLimit;
    /**
     * 空间限制
     */
    private Long memoryLimit;
    /**
     * 堆栈限制
     */
    private Long stackLimit;
}
