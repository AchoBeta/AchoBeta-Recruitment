package com.achobeta;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

/**
 * @author BanTanger 半糖
 * @date 2024/1/11 15:25
 */
@SpringBootApplication
@MapperScan({"com.achobeta.domain.*.model.dao.mapper"})
public class AchobetaRecruitmentApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(AchobetaRecruitmentApplication.class, args);
    }

}