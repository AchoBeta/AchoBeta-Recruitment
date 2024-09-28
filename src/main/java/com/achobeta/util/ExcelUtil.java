package com.achobeta.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.achobeta.exception.GlobalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

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
        ExportParams params = new ExportParams();
        params.setTitle(title);//表格 标题
        params.setSheetName(sheetName);// 表格左下角sheet名称

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
