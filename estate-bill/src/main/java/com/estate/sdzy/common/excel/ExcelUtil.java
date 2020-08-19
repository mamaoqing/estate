package com.estate.sdzy.common.excel;


import com.estate.exception.BillException;
import com.estate.sdzy.common.annotation.ExcelAnnotation;
import com.estate.util.BillExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public abstract class ExcelUtil {

    static final String STRING = "class java.lang.String";
    static final String DOUBLE = "class java.lang.Double";
    static final String INTEGER = "class java.lang.Integer";
    static final String BOOLEAN = "class java.lang.Boolean";
    static final String DATE = "class java.util.Date";

    /**
     * 检查文件
     *
     * @param file
     * @throws IOException
     */
    public static void checkFile(MultipartFile file) throws IOException {
        //判断文件是否存在
        if (null == file) {
            log.error("文件不存在");
            throw new BillException(BillExceptionEnum.FILE_NOTFOUND_ERROR);
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if (!fileName.endsWith("xls") && !fileName.endsWith("xlsx")) {
            log.error("文件错误");
            throw new BillException(BillExceptionEnum.FILE_TYPE_ERROR);
        }
    }

    /**
     * 获取实体类中的所有需要导出的字段名称
     *
     * @param className 完全限定名
     * @return
     * @throws ClassNotFoundException
     */
    public static List<String> getClassFirld(String className) throws ClassNotFoundException {
        Class<?> aClass = Class.forName(className);
        Field[] fields = aClass.getDeclaredFields();
        List<String> list = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                ExcelAnnotation annotation = field.getAnnotation(ExcelAnnotation.class);
                String value = annotation.value();
                boolean export = annotation.export();
                boolean master = annotation.master();
                String dist = annotation.dist();
                System.out.println(field.getName());
                System.out.println(value);
                System.out.println(dist);
                System.out.println(master);
                System.out.println(export);
                System.out.println("----------");
                list.add(value);
            }
        }
        return list;
    }

}
