package com.achobeta.domain.recruitment.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.recruitment.model.dao.mapper.CustomEntryMapper;
import com.achobeta.domain.recruitment.model.entity.CustomEntry;
import com.achobeta.domain.recruitment.service.CustomEntryService;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【custom_entry(自定义项表)】的数据库操作Service实现
* @createDate 2024-05-11 02:30:58
*/
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomEntryServiceImpl extends ServiceImpl<CustomEntryMapper, CustomEntry>
    implements CustomEntryService{

    @Override
    public Long addCustomEntry(Long recId, String title) {
        CustomEntry customEntry = new CustomEntry();
        customEntry.setRecId(recId);
        customEntry.setTitle(title);
        this.save(customEntry);
        return customEntry.getId();
    }

    @Override
    public List<CustomEntry> selectCustomEntry(Long recId) {
        return this.lambdaQuery().eq(CustomEntry::getRecId, recId).list();
    }

    @Override
    public void removeCustomEntry(Long id) {
        this.lambdaUpdate().eq(CustomEntry::getId, id).remove();
    }

    @Override
    public Long getRecIdById(Long id) {
        return this.lambdaQuery()
                .eq(CustomEntry::getId, id)
                .oneOpt()
                .orElseThrow(() -> new GlobalServiceException(GlobalServiceStatusCode.ENTRY_NOT_EXISTS))
                .getRecId();
    }
}




