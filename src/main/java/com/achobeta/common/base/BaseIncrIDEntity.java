package com.achobeta.common.base;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <span>
 * classes that inherit this entity all have auto-incrementing ID.
 * </span>
 *
 * @author jettcc in 2023/12/07
 * @version 1.0
 */
@Getter
@Setter
public class BaseIncrIDEntity extends BaseEntity implements Serializable {
    /**
     * id, incr
     */
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

}
