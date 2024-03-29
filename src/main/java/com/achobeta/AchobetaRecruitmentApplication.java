package com.achobeta;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author BanTanger 半糖
 * @date 2024/1/11 15:25
 */
@SpringBootApplication
@MapperScan({"com.achobeta.domain.*.model.dao.mapper"})
public class AchobetaRecruitmentApplication {
    public static void main(String[] args) {
        SpringApplication.run(AchobetaRecruitmentApplication.class, args);
    }

}