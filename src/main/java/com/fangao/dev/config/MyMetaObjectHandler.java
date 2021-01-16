package com.fangao.dev.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.fangao.dev.common.web.Account;
import com.fangao.dev.common.web.LoginHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * <p>
 * 填充器
 * </p>
 *
 * @author jobob
 * @since 2018-11-01
 */
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Account account = LoginHelper.getAccount(false);
        if (null != account) {
            setFieldValByName("operator", account.getName(), metaObject);
        }
        setFieldValByName("deleted", false, metaObject);
        setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        setFieldValByName("modifiedTime", LocalDateTime.now(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("modifiedTime", LocalDateTime.now(), metaObject);
    }
}