package com.estate.sdzy.common.excel;

import com.alibaba.fastjson.JSON;
import com.estate.exception.BillException;
import com.estate.sdzy.asstes.mapper.RParkingSpaceMapper;
import com.estate.sdzy.common.annotation.ExcelAnnotation;
import com.estate.sdzy.system.mapper.SDictMapper;
import com.estate.util.BillExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 导入excel
 */
@Slf4j
@Component
public class ImportExcel extends ExcelUtil {

    private static SDictMapper dictMapper;

    private static RParkingSpaceMapper parkingSpaceMapper;

    /**
     * 解析表格中的数据
     *
     * @param file
     * @param className
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<Object> getFileData(MultipartFile file, String className) throws IOException, ClassNotFoundException {
        checkFile(file);
        Class<?> c = Class.forName(className);
        Field[] fields1 = c.getDeclaredFields();
        Annotation[] annotations = c.getAnnotations();
        List<String> fields = new ArrayList<>();
        List<String> masterField = new ArrayList<>();
        // 存在字典属性的
        Map<String,String> distMap = new HashMap<>();

        for (Field field : fields1) {
            if (field.isAnnotationPresent(ExcelAnnotation.class)){
                fields.add(field.getName());
                if(field.getAnnotation(ExcelAnnotation.class).master()){
                    masterField.add(field.getName());
                }
                String dist = field.getAnnotation(ExcelAnnotation.class).dist();
                if(!StringUtils.isEmpty(dist)){
                    distMap.put(field.getName(),dist);
                }
            }
        }
        Integer compId = 0;
        Integer commId = 0;
        Integer areaId = 0;
        if (fields.contains("compName")) {
            compId = fields.indexOf("compName");
        }
        if (fields.contains("commName")) {
            commId = fields.indexOf("commName");
        }
        if (fields.contains("areaName")) {
            areaId = fields.indexOf("areaName");
        }

        List<Object> list = new ArrayList<>();
        // excel文件,
        /*
         * 1.先循环获取到每一行的信息
         * 2.然后将每一行的信息在循环，获取每列的信息即每个单元格的内容
         * */
        Workbook workBook = getWorkBook(file);
        if (null != workBook) {
            for (int i = 0; i < workBook.getNumberOfSheets(); i++) {
                Sheet sheetAt = workBook.getSheetAt(i);
                if (null == sheetAt) {
                    continue;
                }
                int firstRowNum = sheetAt.getFirstRowNum();
                int lastRowNum = sheetAt.getLastRowNum();
                for (int rowNum = firstRowNum + 3; rowNum < lastRowNum + 1; rowNum++) {
                    // 获取到具体的行
                    Row row = sheetAt.getRow(rowNum);
                    if (null == row) {
                        continue;
                    }
                    short firstCellNum = row.getFirstCellNum();
                    short lastCellNum = row.getLastCellNum();
                    if (lastCellNum > 0) {
                        Map<String, Object> map = new HashMap<>(16);
                        for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                            Cell cell = row.getCell(cellNum);
                            // 判断该属性是否是字典属性,存在字典属性，必须与字典一致，否则就置空。
                            String s = fields.get(cellNum);
                            if(distMap.containsKey(s)){
                                String dictName = dictMapper.getDictName(cell.toString(), distMap.get(s));
                                cell.setCellValue(dictName);
                            }
                            if(masterField.contains(s) && StringUtils.isEmpty(getCellValue(cell))){
                                throw new BillException(BillExceptionEnum.FILE_MASTER_FIELDNO_ERROR);
                            }
                            map.put(s, getCellValue(cell));
                            if(cellNum == compId){
                                log.info("公司名称是:{}",getCellValue(cell));
                                Long compIdByName = parkingSpaceMapper.getCompIdByName(getCellValue(cell));
                                map.put("compId",compIdByName);
                            }
                            if(cellNum == commId){
                                log.info("社区称是:{}",getCellValue(cell));
                                Long commIdByName = parkingSpaceMapper.getCommIdByName(getCellValue(cell));
                                map.put("commId",commIdByName);
                            }
                            if(cellNum == areaId){
                                log.info("分区名称是:{}",getCellValue(cell));
                                Long commIdByName = parkingSpaceMapper.getAreaIdByName(getCellValue(cell));
                                map.put("commAreaId",commIdByName);
                            }
                        }
                        Object aClass = JSON.parseObject(JSON.toJSONString(map), c);
                        list.add(aClass);
                    }

                }

            }
        }

        return list;
    }


    public static Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith("xls")) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith("xlsx")) {
                //2007 及2007以上
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            e.getMessage();
        }
        return workbook;
    }

    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        CellType cellTypeEnum = cell.getCellTypeEnum();
        switch (cellTypeEnum) {
            case STRING:
                cellValue = String.valueOf(cell);
                break;
            case NUMERIC:
                cellValue = String.valueOf(cell);
                break;
            case BLANK:
                cellValue = "";
                break;
        }
        return cellValue;

    }

    @Autowired
    public void setDictMapper(SDictMapper dictMapper) {
        ImportExcel.dictMapper = dictMapper;
    }

    @Autowired
    public void setParkingSpaceMapper(RParkingSpaceMapper parkingSpaceMapper) {
        ImportExcel.parkingSpaceMapper = parkingSpaceMapper;
    }

}
