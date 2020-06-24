package com.djangson.common.mybatis.interceptor;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.djangson.common.util.SqlUtil;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.Properties;

@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class KeywordEscapeInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        // 通过MetaObject优雅访问对象的属性，这里是访问statementHandler的属性
        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());

        // 配置文件中SQL语句的ID 原始的SQL语句
        String sql = statementHandler.getBoundSql().getSql();

        // 格式化转义SQL语句
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        metaObject.setValue("delegate.boundSql.sql", SqlUtil.sqlFormatWithEscape(sql));
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties prop) {
    }
}
