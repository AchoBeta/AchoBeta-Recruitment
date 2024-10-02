package com.achobeta.feishu.aspect;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.feishu.token.FeishuTenantAccessToken;
import com.lark.oapi.core.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import static com.achobeta.feishu.constants.FeishuConstants.SHOULD_REFRESH_CODE;
import static com.achobeta.feishu.constants.FeishuConstants.SUCCESS_CODE;

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

    private final FeishuTenantAccessToken feishuTenantAccessToken;

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
            if(code == SUCCESS_CODE) {
                log.info("飞书请求成功");
            } else {
                if(code == SHOULD_REFRESH_CODE) {
                    log.info("token 刷新");
                    feishuTenantAccessToken.refreshToken();
                }
                throw new GlobalServiceException(response.getMsg(), GlobalServiceStatusCode.REQUEST_NOT_VALID);
            }
        }else {
            throw new GlobalServiceException("返回值类型不匹配", GlobalServiceStatusCode.REQUEST_NOT_VALID);
        }
    }

}
