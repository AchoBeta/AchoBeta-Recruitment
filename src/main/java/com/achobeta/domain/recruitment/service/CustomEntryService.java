package com.achobeta.domain.recruitment.service;

import com.achobeta.domain.recruitment.model.entity.CustomEntry;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【custom_entry(自定义项表)】的数据库操作Service
* @createDate 2024-05-11 02:30:58
*/
public interface CustomEntryService extends IService<CustomEntry> {

    Long addCustomEntry(Long recId, String title);


    List<CustomEntry> selectCustomEntry(Long recId);

    void removeCustomEntry(Long id);

}
