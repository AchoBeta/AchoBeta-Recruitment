package com.achobeta.domain.users.context;

import com.achobeta.domain.users.model.po.StudentEntity;
import com.achobeta.domain.users.model.po.UserHelper;

/**
 * @author cattleYuan
 * @date 2024/1/18
 */
public class BaseContext {

    public static ThreadLocal<UserHelper> threadLocal = new ThreadLocal<>();

    public static void setCurrentUser(UserHelper userHelper ) {
        threadLocal.set(userHelper);
    }

    public static UserHelper getCurrentUser() {
        return threadLocal.get();
    }

    public static void removeCurrentUser() {
        threadLocal.remove();
    }

}
