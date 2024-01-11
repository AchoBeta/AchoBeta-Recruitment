/**
 * 参考 DDD 的设计理念(但该项目依然是 MVC 架构)，对每一个领域进行分包处理
 * 一来避免业务职责脏污，二来方便分模块开发
 * 每一个领域模块就在 domain 包下新开一个包
 * 比如招新系统有注册登录模块 users，有通知模块 notice
 * 每个领域模块里面的内容应当包含四个（controller、model、repository、service）
 * 暂时以 users 包为例，大家可以点击浏览一下，每个包的具体含义和职责请查看 package-info
 * @author BanTanger 半糖
 * @date 2024/1/11 15:40
 */
package com.achobeta.domain;