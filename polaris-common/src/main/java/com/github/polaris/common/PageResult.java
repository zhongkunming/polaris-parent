package com.github.polaris.common;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页查询响应结果,
 * 如果需要返回除分页结果外其他的数据，可以用继承的形式实现
 *
 * @author zhongkunming
 */
@Data
@NoArgsConstructor
@Schema(description = "分页查询响应结果")
public class PageResult<R> implements Serializable {

    @Schema(description = "总条数")
    private Long total;

    @Schema(description = "总页数")
    private Long pages;

    @Schema(description = "每页数量")
    private Long pageSize;

    @Schema(description = "页码")
    private Long pageNum;

    @Schema(description = "页数据")
    private List<R> list;

    public static <R> PageResult<R> to(PageRequest page) {
        return to(page, 0L, null);
    }

    public static <R> PageResult<R> to(PageRequest page, Long total, List<R> list) {
        Integer pageNum = page.getPageNum();
        Integer pageSize = page.getPageSize();
        PageResult<R> pageResult = new PageResult<>();
        pageResult.setPageNum(Long.valueOf(pageNum));
        pageResult.setPageSize(Long.valueOf(pageSize));
        pageResult.setTotal(total);
        pageResult.setPages((total + pageSize - 1) / pageSize);
        pageResult.setList(list);
        return pageResult;
    }

    public static <T, R> PageResult<R> toEmpty(IPage<T> page) {
        PageResult<R> result = new PageResult<>();
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setPageSize(page.getSize());
        result.setPageNum(page.getCurrent());
        result.setList(null);
        return result;
    }

    public static <R> PageResult<R> to(IPage<R> page) {
        PageResult<R> result = new PageResult<>();
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setPageSize(page.getSize());
        result.setPageNum(page.getCurrent());
        result.setList(page.getRecords());
        return result;
    }

    public static <T, R> PageResult<R> to(IPage<T> page, List<R> list) {
        PageResult<R> result = new PageResult<>();
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setPageSize(page.getSize());
        result.setPageNum(page.getCurrent());
        result.setList(list);
        return result;
    }

    public static <T, R> PageResult<R> to(IPage<T> page, Class<R> clazz) {
        return to(page, val -> BeanUtil.copyProperties(val, clazz));
    }

    public static <T, R> PageResult<R> to(IPage<T> page, Function<T, R> func) {
        PageResult<R> result = new PageResult<>();
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setPageSize(page.getSize());
        result.setPageNum(page.getCurrent());
        result.setList(page.getRecords().stream().map(func).collect(Collectors.toList()));
        return result;
    }
}

