package com.achobeta.domain.recruitment.cotroller;

import cn.hutool.core.bean.BeanUtil;
import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.recruitment.model.entity.Recruitment;
import com.achobeta.domain.recruitment.model.vo.RecruitmentVO;
import com.achobeta.domain.recruitment.service.RecruitmentService;
import com.achobeta.domain.users.context.BaseContext;
import com.achobeta.exception.GlobalServiceException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-05-11
 * Time: 11:42
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruit")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @PostMapping("/create")
    public SystemJsonResponse createRecruitment(@RequestParam("batch") @NonNull Integer batch) {
        if(batch.compareTo(0) <= 0) {
            throw new GlobalServiceException("ab版本非法", GlobalServiceStatusCode.PARAM_FAILED_VALIDATE);
        }
        // 调用服务常见一次招新
        Long recruitmentId = recruitmentService.createRecruitment(batch);
        log.info("管理员({}) 创建了一次招新，版本：{}，id：{}",
                BaseContext.getCurrentUser().getUserId(), batch, recruitmentId);
        // 返回招新id
        return SystemJsonResponse.SYSTEM_SUCCESS(recruitmentId);
    }

    @GetMapping("/list")
    public SystemJsonResponse getRecruitments(@RequestParam(name = "isRun", required = false) Boolean isRun) {
        List<Recruitment> list = null;
        if(Objects.isNull(isRun)) {
            list = recruitmentService.list();
        } else {
            list = recruitmentService.lambdaQuery().eq(Recruitment::getIsRun, isRun).list();
        }
        List<RecruitmentVO> recruitmentVOS = BeanUtil.copyToList(list, RecruitmentVO.class);
        return SystemJsonResponse.SYSTEM_SUCCESS(recruitmentVOS);
    }
}
