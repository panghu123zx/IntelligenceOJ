package com.ph.phojmodel.model.codesandbox;

import com.ph.phojmodel.model.dto.questionsubmit.judgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCode {
    /**
     * 输出示例
     */
    private List<String> output;
    /**
     * 执行信息
     */
    private String message;
    /**
     * 执行状态
     */
    private String status;
    /**
     * 执行的详细信息
     */
    private judgeInfo judgeInfo;

}
