package com.github.polaris.support;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.polaris.constants.CommonConstants;

import java.util.Collection;
import java.util.List;

/**
 * 在 MyBatis Plus 的 BaseMapper 的基础上拓展，提供更多的能力
 */
public interface BaseMapperX<T> extends BaseMapper<T> {

    default T selectOneByCondition(QueryWrapper<T> wrapper) {
        wrapper.last(CommonConstants.SQL_LAST_LIMIT_1);
        return selectOne(wrapper);
    }

    default T selectOneByCondition(LambdaQueryWrapper<T> wrapper) {
        wrapper.last(CommonConstants.SQL_LAST_LIMIT_1);
        return selectOne(wrapper);
    }

    default T selectOne(SFunction<T, ?> field, Object value) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(field, value);
        return selectOneByCondition(wrapper);
    }

    default T selectOne(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(field1, value1);
        wrapper.eq(field2, value2);
        return selectOneByCondition(wrapper);
    }

    default List<T> selectList() {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        return selectList(wrapper);
    }

    default List<T> selectList(SFunction<T, ?> field, Object value) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(field, value);
        return selectList(wrapper);
    }

    default List<T> selectListIn(SFunction<T, ?> field, Collection<?> values) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(field, values);
        return selectList(wrapper);
    }

    default Boolean exists(SFunction<T, ?> field, Object value) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(field, value);
        wrapper.last(CommonConstants.SQL_LAST_LIMIT_1);
        return exists(wrapper);
    }

    default Boolean existsIn(SFunction<T, ?> field, Collection<?> values) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(field, values);
        wrapper.last(CommonConstants.SQL_LAST_LIMIT_1);
        return exists(wrapper);
    }
}
