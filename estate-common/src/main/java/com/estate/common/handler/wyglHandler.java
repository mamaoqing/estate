package com.estate.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class wyglHandler implements MetaObjectHandler {

    // 在数据库信息创建的时候自动添加创建时间和更新时间
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createdAt",new Date(),metaObject);
        if(metaObject.hasSetter("modifiedAt")) {
            this.setFieldValByName("modifiedAt", new Date(), metaObject);
        }
    }

    // 在数据库信息更新的时候，自动更改更新时间
    public void updateFill(MetaObject metaObject) {
        if(metaObject.hasSetter("modifiedAt")){
            this.setFieldValByName("modifiedAt",new Date(),metaObject);
        }
    }
}
