package com.djangson.common.base.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface BaseDao<T> extends Mapper<T> {

    int insert(T entity);

    int deleteById(Serializable id);

    int delete(@Param(Constants.WRAPPER) Wrapper<T> wrapper);

    int logicDelete(@Param(Constants.ENTITY) T entity, @Param(Constants.WRAPPER) Wrapper<T> updateWrapper);

    int deleteBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);

    int updateById(@Param(Constants.ENTITY) T entity);

    int update(@Param(Constants.ENTITY) T entity, @Param(Constants.WRAPPER) Wrapper<T> updateWrapper);

    T selectById(Serializable id);

    List<T> selectBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);

    List<T> selectByMap(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);

    T selectOne(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    Integer selectCount(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    List<T> selectList(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    List<Map<String, Object>> selectMaps(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    List<Object> selectObjs(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    IPage<T> selectPage(IPage<T> page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    IPage<Map<String, Object>> selectMapsPage(IPage<T> page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
}
