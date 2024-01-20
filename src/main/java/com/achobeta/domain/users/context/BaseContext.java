package com.achobeta.domain.users.context;

import com.achobeta.domain.users.model.po.StudentEntity;
import com.achobeta.domain.users.model.vo.LoginVO;

/**
 * @author cattleYuan
 * @date 2024/1/18
 */
public class BaseContext {

    public static ThreadLocal<StudentEntity> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(StudentEntity studentEntity ) {
        threadLocal.set(studentEntity);
    }

    public static StudentEntity getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
