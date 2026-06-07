package com.github.polaris.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContext {

    /**
     * 租户ID
     */
    private String tenantId = "T0";

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 当前系统编码
     */
    private String sysCode;

    /**
     * Token
     */
    private String token;
}
