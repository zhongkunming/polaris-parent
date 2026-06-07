package com.github.polaris.context;

import com.github.polaris.common.UserContext;

/**
 * 用户上下文
 *
 * @author zhongkunming
 * @implNote 使用此类，需要引用并启用 polaris-interceptor 模块，或者手动设置 UserContext
 */
public class UserContextHelper {

    private static final ThreadLocal<UserContext> tenantThreadLocal = new ThreadLocal<>();

    public static UserContext currentUser() {
        return tenantThreadLocal.get();
    }

    public static void setUser(UserContext userContext) {
        tenantThreadLocal.set(userContext);
    }

    public static void clearUser() {
        tenantThreadLocal.remove();
    }
}
