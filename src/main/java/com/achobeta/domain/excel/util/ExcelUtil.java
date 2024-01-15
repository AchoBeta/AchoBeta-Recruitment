package com.achobeta.domain.excel.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ExcelUtil {

    private static final String DEFAULT_SUFFIX = ".xlsx";

    public static <T> void printXlsx(String  title, String sheetName,
                                     Class<T> clazz, List<? extends T> data,
                                     String filePath, String fileName) {
        data = data.stream().map(x -> (T)x).collect(Collectors.toList());
        // 导出
        ExportParams params = new ExportParams();
        params.setTitle(title);//表格 标题
        params.setSheetName(sheetName);// 表格左下角sheet名称
        Workbook workbook = ExcelExportUtil.exportExcel(params, clazz, data);
        try{
            // 文件夹是否存在，若没有对应文件夹直接根据路径生成文件会报错
            File directory = new File(filePath);
            if (!directory.exists() && !directory.isDirectory()) {
                directory.mkdirs();
            }
            // 文件是否存在
            String path = filePath + fileName + DEFAULT_SUFFIX;
            File file = new File(path);
            if (!file.exists()){
                file.createNewFile();
            }
            // 输出流写入(覆盖)
            FileOutputStream outputStream = new FileOutputStream(path);
            workbook.write(outputStream);
            // 关闭写，不然用户点击生成的文件会显示只读
            outputStream.close();
            workbook.close();
            log.info("导出成功：" + path);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
