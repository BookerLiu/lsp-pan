package com.booker.lsp.vo.common;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @Author BookerLiu
 * @Date 2022/11/12 11:53
 * @Description 分页返回对象
 **/

@Data
public class PageRes<T> {

    private long pageNumber;
    private long pageSize;
    private long total;
    private List<T> list = Collections.emptyList();

    public static <T> PageRes<T> fromPage(Page<?> page, Class<T> tClass) {
        return new PageRes<>(page, tClass);
    }

    private PageRes() {}

    private PageRes(Page<?> page, Class<T> tClass) {
        this.list = BeanUtil.copyToList(page.getRecords(), tClass);
        this.pageSize = page.getSize();
        this.pageNumber = page.getCurrent();
        this.total = page.getTotal();
    }


}
