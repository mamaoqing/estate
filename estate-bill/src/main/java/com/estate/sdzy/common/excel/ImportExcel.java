package com.estate.sdzy.common.excel;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.sdzy.asstes.entity.RBuilding;
import com.estate.sdzy.asstes.entity.ROwner;
import com.estate.sdzy.asstes.entity.RUnit;
import com.estate.sdzy.asstes.mapper.RParkingSpaceMapper;
import com.estate.sdzy.asstes.service.ROwnerService;
import com.estate.sdzy.common.annotation.ExcelAnnotation;
import com.estate.sdzy.system.mapper.SDictMapper;
import com.estate.sdzy.tariff.mapper.FMeterMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导入excel
 */
@Slf4j
@Component
public class ImportExcel extends ExcelUtil {

    private static SDictMapper dictMapper;

    private static RParkingSpaceMapper parkingSpaceMapper;
    private static FMeterMapper fMeterMapper;

    /**
     * 解析表格中的数据
     *
     * @param file 导入的问价
     * @param className 类的完全限定名
     * @return 返回一个object的集合，object是参数className的类型
     * @throws IOException io异常
     * @throws ClassNotFoundException 找不到类异常
     */
    public static List<Object> getFileData(MultipartFile file, String className) throws IOException, ClassNotFoundException {
        checkFile(file);
        // 反射得到实体类中的所有的属性
        Class<?> c = Class.forName(className);
        Field[] fields1 = c.getDeclaredFields();
        // 需要导出的字段
        List<String> fields = (List<String>) ImportExcel.getFieldMap(fields1).get("fields");
        // 必填的字段
        List<String> masterField = (List<String>) ImportExcel.getFieldMap(fields1).get("masterField");
        // 存在字典属性的
        Map<String,String> distMap = (Map<String, String>) ImportExcel.getFieldMap(fields1).get("distMap");


        Map<String, Integer> index = ImportExcel.getFieldIndex(fields);

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
                            // 将结果放入map中
                            map.put(s, getCellValue(cell));
                            if(masterField.contains(s)){
                                ImportExcel.setMapValue(index,cellNum,map,cell,rowNum);
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
        if(StringUtils.isEmpty(fileName)){
            throw new BillException(BillExceptionEnum.FILE_NOTFOUND_ERROR);
        }
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

    /**
     * 根据所有需要转编码的字段的位置，将
     * @param map 所有下标位置的键值对，key:名称 value:名称出现在数组的下标
     * @param value 当前下标
     * @param resultMap 结果map
     * @param cell 单元格
     */
    public static void setMapValue(Map<String,Integer> map,Integer value,Map<String, Object> resultMap,Cell cell,int rowNum){
        if(map.get("compName") == value){
            log.info("公司名称是:{}",getCellValue(cell));
            Long compIdByName = parkingSpaceMapper.getCompIdByName(getCellValue(cell));
            resultMap.put("compId",compIdByName);
        }
        if(map.get("commName") == value){
            log.info("社区称是:{}",getCellValue(cell));
            Long commIdByName = parkingSpaceMapper.getCommIdByName(getCellValue(cell));
            resultMap.put("commId",commIdByName);
        }
        if(map.get("areaName") == value){
            log.info("分区名称是:{}",getCellValue(cell));
            Long commIdByName = parkingSpaceMapper.getAreaIdByName(getCellValue(cell));

            resultMap.put("commAreaId",commIdByName);
        }
        if(map.get("buildingName") == value){
            log.info("建筑名称是:{}",getCellValue(cell));
            List<RBuilding> buildings= parkingSpaceMapper.getBuildingByName(getCellValue(cell),(Long) resultMap.get("commAreaId"));
            if(buildings.size()==1){
                resultMap.put("buildingId",buildings.get(0).getId());
            }else{
                throw new BillException(415,"第"+(rowNum+1)+"行建筑名称错误,找不到该建筑，导入失败");
            }

        }
        if(map.get("unitName") == value){
            log.info("单元名称是:{}",getCellValue(cell));
            List<RUnit> units= parkingSpaceMapper.getUnitByName(getCellValue(cell),(Long) resultMap.get("buildingId"));
            if(units.size()==1){
                resultMap.put("unitId",units.get(0).getId());
            }else{
                throw new BillException(415,"第"+(rowNum+1)+"行单元名称错误，找不到该单元，导入失败");
            }
        }
        if(map.get("commAreaName") == value){
            log.info("分区名称是:{}",getCellValue(cell));
            Long commIdByName = parkingSpaceMapper.getAreaIdByName(getCellValue(cell));
            resultMap.put("commAreaId",commIdByName);
        }
        if(map.get("propertyName") == value){//仪表中物业编号：房间（建筑编号-单元编号-房间编号）、停车位（停车位编号）
            log.info("物业编号是:{}",getCellValue(cell));
            //根据分区id和物业类型查询编号是否重复
            setMeterPropertyId(resultMap,cell,rowNum);
        }
        //导入仪表抄表时增加判断仪表编号是否存在（社区+仪表编号）
        if(map.get("no") == value){
            log.info("仪表编号是:{}",getCellValue(cell));
            List<Long> meterIds = fMeterMapper.getMeterByNo(getCellValue(cell), (Long) resultMap.get("commId"));
            if(meterIds.size()==1){
                resultMap.put("no",getCellValue(cell));
            }else{
                throw new BillException(415,"第"+(rowNum+1)+"行仪表编号错误，找不到该仪表，导入失败");
            }
        }
    }

    public static void setMeterPropertyId(Map<String, Object> resultMap,Cell cell,int rowNum){
        if("房产".equals(resultMap.get("propertyType"))){
            String[] split = getCellValue(cell).split("-");
            if(split.length==3){//判断格式是否正确（建筑编号-单元编号-房间编号）
                //截取后根据分区编号、建筑编号、单元编号验证物业编号的合理性，查看是否有该建筑和单元
                List<Long> buildings= parkingSpaceMapper.getBuildingByBuildingNo(split[0],(Long) resultMap.get("commAreaId"));
                if(buildings.size()==1){//判断建筑编号是否存在，根据分区id和建筑编号进行判断
                    List<Long> units= parkingSpaceMapper.getUnitByUnitNo(split[1],buildings.get(0));
                    if(units.size()==1){//判断单元编号是否存在，根据建筑id和单元编号进行判断
                        //将物业编号转为物业id，即房产id
                        //根据单元id和房间编号查询是否存在房间，若存在则转为房间id，不存在则抛出异常
                        List<Long> rooms= parkingSpaceMapper.getRoomByRoomNo(split[2],units.get(0));
                        if(rooms.size()==1){
                            resultMap.put("propertyId",rooms.get(0));
                        }else{
                            throw new BillException(415,"第"+(rowNum+1)+"行物业编号错误，导入失败，物业编号中房间编号有误，找不到该房间");
                        }
                    }else{
                        throw new BillException(415,"第"+(rowNum+1)+"行物业编号错误，导入失败，物业编号中单元编号有误，找不到该单元");
                    }
                }else{
                    throw new BillException(415,"第"+(rowNum+1)+"行物业编号错误，导入失败，物业编号中建筑编号有误，找不到该建筑");
                }
            }else{
                throw new BillException(415,"第"+(rowNum+1)+"行物业编号错误，导入失败，物业编号为‘建筑编号-单元编号-房间编号’");
            }
        }else{
            //将物业编号转为物业id，即停车位id
            //根据分区id和停车位编号查询是否存在停车位，若存在则转为停车位id，不存在则抛出异常
            List<Long> parkings = parkingSpaceMapper.getParkingByParkingNo(getCellValue(cell),(Long) resultMap.get("commAreaId"));
            if(parkings.size()==1){
                resultMap.put("propertyId",parkings.get(0));
            }else{
                throw new BillException(415,"第"+(rowNum+1)+"行物业编号错误，导入失败，物业编号有误");
            }
        }
    }


    /**
     * 将文字转编码。比如公司名称-->公司id，如果以后还有需要转编码的，需要在这里面进行配置。
     * 返回一个map集合。
     * @param fields 实体类中所有带注解的属性的集合
     * @return 返回一个map，所有下标位置的键值对，key:名称 value:名称出现在数组的下标
     */
    public static Map<String,Integer> getFieldIndex(List<String> fields){
        Map<String,Integer> map = new HashMap<>(16);
        Integer index = 0;
        if (fields.contains("compName")) {
            index = fields.indexOf("compName");
            map.put("compName",index);
        }
        if (fields.contains("commName")) {
            index = fields.indexOf("commName");
            map.put("commName",index);
        }
        if (fields.contains("areaName")) {
            index = fields.indexOf("areaName");
            map.put("areaName",index);
        }
        if(fields.contains("commAreaName")){
            index = fields.indexOf("commAreaName");
            map.put("commAreaName",index);
        }
        if (fields.contains("buildingName")) {
            index = fields.indexOf("buildingName");
            map.put("buildingName",index);
        }
        if (fields.contains("unitName")) {
            index = fields.indexOf("unitName");
            map.put("unitName",index);
        }
        if (fields.contains("propertyName")) {
            index = fields.indexOf("propertyName");
            map.put("propertyName",index);
        }
        //导入仪表抄表时需要，仪表编号
        if (fields.contains("no")) {
            index = fields.indexOf("no");
            map.put("no",index);
        }
        return map;
    }

    /**
     * 将实体类中的属性整合，方便以后使用
     * @param fields1 实体类中的所有的属性
     * @return 返回一个自己需要的map，里面包括所有导出的集合，所有必填的集合，所有有字典的map
     */
    public static Map<String,Object> getFieldMap(Field [] fields1){
        Map<String,Object> map = new HashMap<>(16);
        List<String> fields = new ArrayList<>();
        List<String> masterField = new ArrayList<>();
        // 存在字典属性的
        Map<String,String> distMap = new HashMap<>();
        for (Field field : fields1) {
            if (field.isAnnotationPresent(ExcelAnnotation.class)){
                if(field.getAnnotation(ExcelAnnotation.class).export()){//导入模板和导入时列要对应
                    fields.add(field.getName());
                }
                if(field.getAnnotation(ExcelAnnotation.class).master()){
                    masterField.add(field.getName());
                }
                String dist = field.getAnnotation(ExcelAnnotation.class).dist();
                if(!StringUtils.isEmpty(dist)){
                    distMap.put(field.getName(),dist);
                }
            }
        }
        map.put("fields",fields);
        map.put("masterField",masterField);
        map.put("distMap",distMap);
        return map;
    }

    @Autowired
    public void setDictMapper(SDictMapper dictMapper) {
        ImportExcel.dictMapper = dictMapper;
    }

    @Autowired
    public void setParkingSpaceMapper(RParkingSpaceMapper parkingSpaceMapper) {
        ImportExcel.parkingSpaceMapper = parkingSpaceMapper;
    }

    @Autowired
    public void setMeterMapper(FMeterMapper fMeterMapper) {
        ImportExcel.fMeterMapper = fMeterMapper;
    }

}
