package com.ph.phojmodel.model.codesandbox;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestCode {
    /**
     * 输入示例
     */
    private List<String> input;
    /**
     * 代码语言
     */
    private String language;
    /**
     * 代码
     */
    private String code;
}
