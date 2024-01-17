package com.achobeta.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
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

    /**
     * 打印表格
     * @param title 表格标题
     * @param sheetName 图纸名称
     * @param clazz 每一行的类型（字节码）
     * @param data 数据集合
     * @param filePath 下载路径
     * @param fileName xlsx文件名
     * @param <T> 表格行类型
     */
    public static <T> void exportXlsxFile(String  title, String sheetName,
                                     Class<T> clazz, List<? extends T> data,
                                     String filePath, String fileName) {
        data = data.stream().map(x -> (T)x).collect(Collectors.toList());
        // 导出
        ExportParams params = new ExportParams();
        params.setTitle(title);//表格 标题
        params.setSheetName(sheetName);// 表格左下角sheet名称
        Workbook workbook = ExcelExportUtil.exportExcel(params, clazz, data);
        try{
            String path = tryCreateFile(filePath, fileName);
            // 输出流写入(覆盖)
            FileOutputStream outputStream = new FileOutputStream(path);
            workbook.write(outputStream);
            // 关闭写，不然用户点击生成的文件会显示只读
            outputStream.close();
            workbook.close();
            log.info("导出成功：{}", path);
        }catch (IOException e){
            log.warn(e.getMessage());
        }
    }

    private static String tryCreateFile(String filePath, String fileName) throws IOException {
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
        return path;
    }

}
