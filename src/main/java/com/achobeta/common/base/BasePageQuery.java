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
import java.util.Optional;

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

    private Integer current;

    private Integer pageSize;

    private String sortBy;

    private Boolean isAsc;

    private void init() {
        current = Optional.ofNullable(current).orElse(DEFAULT_CURRENT);
        pageSize = Optional.ofNullable(pageSize).orElse(DEFAULT_PAGE_SIZE);
        sortBy = Optional.ofNullable(sortBy).orElse(DEFAULT_SORT_BY);
        isAsc = Optional.ofNullable(isAsc).orElse(DEFAULT_IS_ASC);
    }

    public <T> IPage<T> toMpPage(OrderItem... orders){
        // 初始化
        init();
        // 1.分页条件
        Page<T> page = Page.of(current, pageSize);
        page.addOrder(new OrderItem(sortBy, isAsc));
        // 2.排序条件
        List<OrderItem> orderItemList = Arrays.stream(orders)
                .filter(Objects::nonNull)
                .filter(order -> Objects.nonNull(order.getColumn()))
                .toList();
        if(!CollectionUtils.isEmpty(orderItemList)) {
            page.addOrder(orderItemList);
        }
        return page;
    }

    public <T> IPage<T> toMpPage(String sortBy, boolean isAsc){
        return toMpPage(new OrderItem(sortBy, isAsc));
    }

}
