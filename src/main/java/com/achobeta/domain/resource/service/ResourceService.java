package com.achobeta.domain.resource.service;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-21
 * Time: 12:16
 */
public interface ResourceService {

    boolean isValid(Long code);

    boolean isPermit(Long userId, Long code);

    String analyzeCode(Long userId, Long code);

}
