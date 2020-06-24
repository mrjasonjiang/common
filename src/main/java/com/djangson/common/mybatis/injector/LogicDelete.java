package com.djangson.common.mybatis.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2018/11/01 17:18
 */
public class LogicDelete extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        BaseSqlMethodEnum sqlMethod = BaseSqlMethodEnum.LOGIC_DELETE;
        String sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), sqlLogicSet(), this.sqlWhereEntityWrapper(true, tableInfo));
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return addUpdateMappedStatement(mapperClass, modelClass, sqlMethod.getMethod(), sqlSource);
    }

    public String sqlLogicSet() {
        return "<trim prefix=\"SET\" suffixOverrides=\",\">\n" + "<if test=\"et.updateBy != null\">update_by=#{et.updateBy},</if>\n" + "<if test=\"et.updateTime != null\">update_time=#{et.updateTime},</if>\n" + "is_deleted = 1\n" + "</trim> ";
    }
}
