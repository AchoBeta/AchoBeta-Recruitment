package com.achobeta.domain.resource.service;

import com.achobeta.domain.users.model.po.UserHelper;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-21
 * Time: 12:16
 */
public interface ResourceService {

    boolean isValid(Long code);

    boolean isPermit(UserHelper currentUser, Long code);

    String analyzeCode(UserHelper currentUser, Long code);

}
