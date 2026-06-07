package com.github.polaris.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * 分页请求基础类
 *
 * @author zhongkunming
 */
@Getter
@Setter
@Schema(description = "分页请求基础类")
public abstract class PageRequest implements Serializable {

    @Range(min = 1L, max = Integer.MAX_VALUE, message = "页码最小为1，最大为" + Integer.MAX_VALUE)
    @NotNull(message = "页码不能为空")
    @Schema(description = "页码")
    private Integer pageNum = 1;

    @Range(min = 1L, max = 1000L, message = "每页数量最小为1，最大为1000")
    @NotNull(message = "每页数量不能为空")
    @Schema(description = "每页数量")
    private Integer pageSize = 10;

    public <T> Page<T> toPage() {
        Page<T> page = new Page<>();
        page.setMaxLimit(1000L);
        page.setSize(this.getPageSize());
        page.setCurrent(this.getPageNum());
        return page;
    }

    public Integer toOffset() {
        return (this.getPageNum() - 1) * this.getPageSize();
    }
}
