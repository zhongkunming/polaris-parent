package com.github.polaris.interceptor.support;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.github.polaris.common.UserContext;
import com.github.polaris.context.UserContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author zhongkunming
 */
@Slf4j
@Component
@ConditionalOnMissingBean(MetaObjectHandler.class)
public class GlobalMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createUser", String.class, getUserId());
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictUpdateFill(metaObject, "updateUser", String.class, getUserId());
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateUser", String.class, getUserId());
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    private String getUserId() {
        String userId = "anno";
        try {
            UserContext userContext = UserContextHelper.currentUser();
            if (Objects.nonNull(userContext)) {
                userId = userContext.getUserId();
            }
        } catch (Exception e) {
            log.error("无法获取当前UserId，原因：{}", e.getMessage());
        }
        return userId;
    }
}
