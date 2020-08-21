package com.estate.sdzy.common.excel;

import com.alibaba.druid.util.StringUtils;
import com.estate.exception.BillException;
import com.estate.sdzy.asstes.mapper.RParkingSpaceMapper;
import com.estate.sdzy.common.annotation.ExcelAnnotation;
import com.estate.sdzy.system.mapper.SDictMapper;
import com.estate.util.BillExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 导出excel
 */
@Slf4j
@Component
public class ExportExcel extends ExcelUtil {

    private static SDictMapper dictMapper;

    private static RParkingSpaceMapper parkingSpaceMapper;
    /**
     *
     * @param response 返回
     * @param fileName 文件名称
     * @param className 类名，完全限定名
     * @param dataList 数据集合
     * @param auth 导出人
     * @throws ClassNotFoundException 1
     * @throws NoSuchMethodException 1
     * @throws IllegalAccessException 1
     * @throws InvocationTargetException 1
     */
    public static void writeOut(HttpServletResponse response, String fileName, String className,List<?> dataList,String auth) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<String> list = ExcelUtil.getClassFirld(className);
        Class<?> c = Class.forName(className);
        Map<String, String> map = getDistfield(className);
        HSSFWorkbook workbook = createHSSFWorkbook(fileName,list,auth,dataList,c,map);
        //将excel的数据写入文件
        ByteArrayOutputStream fos = null;
        byte[] retArr = null;
        fos = new ByteArrayOutputStream();
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            workbook.write(fos);
            retArr = fos.toByteArray();
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            //解决中文名称乱码问题,要保存的文件名
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(), "iso-8859-1") + ".xls");
            log.info("文件导出成功，导出人："+auth);
            os.write(retArr);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件导出异常。");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param fileName 文件名
     * @param list 表头集合
     * @param auth 导出人
     * @param dataList 数据集合
     * @param c 类
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static HSSFWorkbook createHSSFWorkbook(String fileName, List<String> list, String auth, List<?> dataList,Class c,Map<String, String> map) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // 创建表格
        HSSFWorkbook wb = new HSSFWorkbook();
        //默认宽，默认高
        HSSFSheet sheet = setSheetBaseInfoExcel(fileName, 15, 20, wb);
        // 设置样式
        HSSFCellStyle style = getColumnTopStyle(wb);
        // 冻结三行,即excel表的前三行不随鼠标滚动而滚动
        /**
         * colsplit : 冻结的行
         * rowsplit : 冻结的列
         * leftmostcolum : 未冻结的第一列
         * toprow : 未冻结的第一行
         *
         */
        sheet.createFreezePane(0, 3, 0, 3);

        // 创建第一行
        HSSFRow rowm = sheet.createRow(0);
        // 第一行的内容,开始设置第一行的第一个单元格
        HSSFCell cell = rowm.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue(fileName);
        // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, list.size() - 1));
        // 创建第二行
        HSSFRow rowm2 = sheet.createRow(1);
        // 第二行的内容,开始设置第二行的第一个单元格
        HSSFCell cell2 = rowm2.createCell(0);
        cell2.setCellStyle(style);
        cell2.setCellValue(auth);
        // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, list.size() - 1));
        // 循环创建第三行内容,表头
        Integer rows = 999;
        if(!CollectionUtils.isEmpty(dataList)){
            rows = dataList.size();
        }
        HSSFRow rowm3 = sheet.createRow(2);
        for (int i = 0, count = list.size(); i < count; i++) {
            String value = list.get(i);
            String dist = map.get(value);
            HSSFCell cellTitle = rowm3.createCell(i);
            cellTitle.setCellStyle(style);
            cellTitle.setCellValue(value);
            // 如果dist不是空的，表示该字段是下拉列表
            if(!StringUtils.isEmpty(dist)){
                // 创建新的下拉框从第三行开始如果数据集合长度为空,就到999行.否则就到集合大小的长度
                CellRangeAddressList regions = new CellRangeAddressList(3,3+rows,i,i);
                List<String> dictNames = dictMapper.listDictName(dist);
                String [] arr = new String[dictNames.size()];
                for (int j = 0; j < dictNames.size(); j++) {
                    arr[j] = dictNames.get(j);
                }
                DVConstraint constraint = DVConstraint.createExplicitListConstraint(arr);
                HSSFDataValidation dataValidation = new HSSFDataValidation(regions,constraint);

                sheet.addValidationData(dataValidation);
            }
        }
        if (dataList != null) {
            createData(dataList,sheet,style,c,list);
        }

        return wb;
    }

    /**
     *  创建数据内容
     * @param dataList 数据集合
     * @param sheet
     * @param style 样式
     * @param c 类
     * @param list 表头集合
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static void createData(List<?> dataList, HSSFSheet sheet, HSSFCellStyle style, Class c,List<String> list) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 数据集是从表格的第四行开始的
        Field[] fields = c.getDeclaredFields();
        int row = 3;
        for (int i = 0, count = dataList.size(); i < count; i++) {
            HSSFRow rowm = sheet.createRow(row + i);
            for (Field field : fields) {
                // 如果字段存在ExcelAnnotation注解，表示是需要导出的内容，就获取里面的值
                if(field.isAnnotationPresent(ExcelAnnotation.class)){
                    // 获取字段属性
                    String type = field.getGenericType().toString();
                    // 字段名
                    String name = field.getName();
                    Method m = c.getMethod("get" +upperCase1th(name));
                    Object value = m.invoke(dataList.get(i));
                    
                    // 获取注解属性
                    String aname =  field.getAnnotation(ExcelAnnotation.class).value();
                    for (int j=0;j<list.size();j++){
                        if(aname.equals(list.get(j))){
                            HSSFCell cell = rowm.createCell(j);
                            cell.setCellStyle(style);
                            setCellValue(cell,value,type,null);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 初始化表格
     *
     * @param excelName 表名称
     * @param columWith 列宽
     * @param rowHight  行高
     * @param wb        表格对象.
     * @return wb
     */
    private static HSSFSheet setSheetBaseInfoExcel(String excelName, int columWith, int rowHight, HSSFWorkbook wb) {
        // 创建文件名称为excelName的excel文件
        HSSFSheet sheet = wb.createSheet(excelName);
        // 设置默认宽高
        sheet.setDefaultColumnWidth(columWith);
        sheet.setDefaultRowHeightInPoints(rowHight);

        return sheet;
    }

    /**
     * 设置样式
     *
     * @param workbook
     * @return
     */
    private static HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 10);
        // 设置字体名字
        font.setFontName("宋体");

        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.DASH_DOT); //下边框
        style.setBorderLeft(BorderStyle.DASH_DOT);//左边框
        style.setBorderTop(BorderStyle.DASH_DOT);//上边框
        style.setBorderRight(BorderStyle.DASH_DOT);//右边框
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(true);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    public static String upperCase1th(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Autowired
    public void setDictMapper(SDictMapper dictMapper) {
        ExportExcel.dictMapper = dictMapper;
    }

    @Autowired
    public void setParkingSpaceMapper(RParkingSpaceMapper parkingSpaceMapper) {
        ExportExcel.parkingSpaceMapper = parkingSpaceMapper;
    }

}
