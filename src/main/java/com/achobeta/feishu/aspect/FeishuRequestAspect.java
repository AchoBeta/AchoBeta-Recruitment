package com.achobeta.feishu.aspect;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.feishu.token.FeishuTenantSession;
import com.lark.oapi.core.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-10-02
 * Time: 18:50
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class FeishuRequestAspect {

    private final FeishuTenantSession feishuTenantSession;

    // 虽然不排除没有返回值的情况，idea 插件识别切点目标方法里没有无返回值的目标方法，但实际上无返回值的目标方法也会执行 AfterReturning，result 为 null 罢了
    // 所以要专门排除！
    @Pointcut("execution(* com.achobeta.feishu.request.FeishuRequestEngine.*(..)) && !execution(void com.achobeta.feishu.request.FeishuRequestEngine.*(..))")
    public void check() {}

    // 符合 check() 切点的方法执行以下逻辑（自调用不会触发）
    @AfterReturning(value = "check()", returning = "result")
    public void doAfterReturning(Object result) {
        if(result instanceof BaseResponse response) {
            int code = response.getCode();
            log.info("飞书响应码 {}", code);
            if(response.success()) {
                log.info("飞书请求成功");
            } else {
                log.info("飞书 token 刷新");
                feishuTenantSession.refreshToken();
                throw new GlobalServiceException(response.getMsg(), GlobalServiceStatusCode.REQUEST_NOT_VALID);
            }
        }else {
            throw new GlobalServiceException("飞书响应内容为 null 或类型不匹配", GlobalServiceStatusCode.REQUEST_NOT_VALID);
        }
    }

}
