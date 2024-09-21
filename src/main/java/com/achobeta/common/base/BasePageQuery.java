package com.achobeta.common.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.achobeta.common.constants.MyBatisPageConstants.*;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-21
 * Time: 13:31
 */
@Getter
@Setter
public class BasePageQuery {

    private Integer pageNo;

    private Integer pageSize;

    private String sortBy;

    private Boolean isAsc;

    private void init() {
        pageNo = Objects.isNull(pageNo) ? DEFAULT_PAGE_NO : pageNo;
        pageSize = Objects.isNull(pageSize) ? DEFAULT_PAGE_SIZE : pageSize;
        sortBy = Objects.isNull(sortBy) ? DEFAULT_SORT_BY : sortBy;
        isAsc = Objects.isNull(isAsc) ? DEFAULT_IS_ASC : isAsc;
    }

    public <T> IPage<T> toMpPage(OrderItem... orders){
        // 初始化
        init();
        // 1.分页条件
        Page<T> page = Page.of(pageNo, pageSize);
        page.addOrder(new OrderItem(sortBy, isAsc));
        // 2.排序条件
        List<OrderItem> orderItemList = Arrays.stream(orders)
                .filter(order -> Objects.nonNull(order) && Objects.nonNull(order.getColumn()))
                .toList();
        if(!CollectionUtils.isEmpty(orderItemList)) {
            page.addOrder(orderItemList);
        }
        return page;
    }
    public <T> IPage<T> toMpPage(OrderItem order){
        return toMpPage(order);
    }

    public <T> IPage<T> toMpPage(String sortBy, boolean isAsc){
        return toMpPage(new OrderItem(sortBy, isAsc));
    }

}
