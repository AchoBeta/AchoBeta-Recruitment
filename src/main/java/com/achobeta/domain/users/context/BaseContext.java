package com.achobeta.domain.users.context;

import com.achobeta.domain.users.model.po.StudentEntity;

/**
 * @author cattleYuan
 * @date 2024/1/18
 */
public class BaseContext {

    public static ThreadLocal<StudentEntity> threadLocal = new ThreadLocal<>();

    public static void setCurrentUser(StudentEntity studentEntity ) {
        threadLocal.set(studentEntity);
    }

    public static StudentEntity getCurrentUser() {
        return threadLocal.get();
    }

    public static void removeCurrentUser() {
        threadLocal.remove();
    }

}
