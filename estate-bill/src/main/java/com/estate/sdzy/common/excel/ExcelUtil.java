package com.estate.sdzy.common.excel;


import com.estate.exception.BillException;
import com.estate.sdzy.asstes.mapper.RParkingSpaceMapper;
import com.estate.sdzy.common.annotation.ExcelAnnotation;
import com.estate.sdzy.system.mapper.SDictMapper;
import com.estate.util.BillExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
        Field[] fields = ExcelUtil.getClassObject(className);
        List<String> list = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                ExcelAnnotation annotation = field.getAnnotation(ExcelAnnotation.class);
                String value = annotation.value();
//                boolean export = annotation.export();
//                boolean master = annotation.master();
//                String dist = annotation.dist();

                list.add(value);
            }
        }
        return list;
    }

    public static Map<String,String> getDistfield(String className) throws ClassNotFoundException {
        Field[] fields = ExcelUtil.getClassObject(className);
        Map<String,String> map = new HashMap<>(16);
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                ExcelAnnotation annotation = field.getAnnotation(ExcelAnnotation.class);
                String dist = annotation.dist();
                String name = annotation.value();
                map.put(name,dist);
            }
        }
        return  map;
    }

    public static Field[] getClassObject(String className) throws ClassNotFoundException {
        Class<?> aClass = Class.forName(className);
        Field[] fields = aClass.getDeclaredFields();
        return fields;
    }

    /**
     * 根据不同类型设置表格内的值
     * @param cell 表格对象
     * @param value 需要填写的值
     * @param type 实体中的属性类型.
     * @param fmt 如果存在时间格式，就按照时间格式转，否则就是 yyyy-MM-dd的格式
     */
    public static void setCellValue(HSSFCell cell, Object value, String type, String fmt){
        if(null == value){
            cell.setCellValue("");
        }else{
            if(INTEGER.equals(type)){
                cell.setCellValue((Integer)value);
            }
            if(DOUBLE.equals(type)){
                cell.setCellValue((Double)value);
            }
            if(BOOLEAN.equals(type)){
                cell.setCellValue((Boolean)value);
            }
            if(STRING.equals(type)){
                cell.setCellValue(String.valueOf(value));
            }
            if(DATE.equals(type)){
                String date = new SimpleDateFormat(fmt != null ? fmt:"yyyy-MM-dd").format((Date)value);
                cell.setCellValue(date.toString());
            }
        }
    }



}
