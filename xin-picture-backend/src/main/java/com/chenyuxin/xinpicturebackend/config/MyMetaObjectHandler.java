package com.chenyuxin.xinpicturebackend.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自动填充处理器
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Date now = new Date();
        this.strictInsertFill(metaObject, "editTime", Date.class, now);
        this.strictInsertFill(metaObject, "createTime", Date.class, now);
        this.strictInsertFill(metaObject, "updateTime", Date.class, now);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
    }
}
