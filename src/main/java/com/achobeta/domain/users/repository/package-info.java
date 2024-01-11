/**
 * 仓储层，不单只是数据库，也可以是 es、redis 这种与底层数据打交道的底层
 * 它的上层是 service 层，下层是 mysql、nosql 等等
 * 也就是 service 调用 repository， repository 以接口方式调用具体实现类与底层数据交互
 * @author BanTanger 半糖
 * @date 2024/1/11 15:49
 */
package com.achobeta.domain.users.repository;