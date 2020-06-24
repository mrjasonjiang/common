package com.djangson.common.mybatis.injector;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2018/11/01 17:36
 */
public enum BaseSqlMethodEnum {

    /**
     * 根据参数逻辑删除
     */
    LOGIC_DELETE("logicDelete", "根据ID 修改数据", "<script>\nUPDATE %s %s %s\n</script>"),

    /**
     * 根据id逻辑删除
     */
    LOGIC_DELETE_BY_ID("logicDeleteById", "根据ID 修改数据", "<script>\nUPDATE %s %s WHERE %s=#{%s}\n</script>");

    private final String method;
    private final String desc;
    private final String sql;

    BaseSqlMethodEnum(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

    public String getMethod() {
        return method;
    }

    public String getDesc() {
        return desc;
    }

    public String getSql() {
        return sql;
    }
}
