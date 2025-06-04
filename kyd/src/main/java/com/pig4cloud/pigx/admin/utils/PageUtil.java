package com.pig4cloud.pigx.admin.utils;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.PageRequest;

import java.util.List;

public class PageUtil {
    /**
     * PageRequest 转 Page
     */
    public static <T> Page<T> toPage(PageRequest pageRequest) {
        Page<T> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        List<OrderItem> orders = pageRequest.getOrders();
        if (orders != null && !orders.isEmpty()) {
            page.setOrders(orders);
        }
        return page;
    }
}
