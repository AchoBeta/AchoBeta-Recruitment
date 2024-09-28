package com.achobeta.domain.resource.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.achobeta.domain.resource.constants.ResourceConstants;
import com.achobeta.exception.GlobalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class ExcelUtil {

    /**
     * 打印表格
     * @param title 表格标题
     * @param sheetName 图纸名称
     * @param clazz 每一行的类型（字节码）
     * @param data 数据集合
     * @param <E> 数据类型
     */
    public static <E> byte[] exportXlsxFile(String  title, String sheetName, Class<E> clazz, List<E> data) {
        // 导出
        ExportParams params = new ExportParams(title, sheetName);
        try (Workbook workbook = ExcelExportUtil.exportExcel(params, clazz, data);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            // 输出流写入(覆盖)
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }catch (IOException e){
            throw new GlobalServiceException(e.getMessage());
        }
    }

}
