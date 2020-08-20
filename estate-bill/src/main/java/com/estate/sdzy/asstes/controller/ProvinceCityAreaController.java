package com.estate.sdzy.asstes.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/sdzy/pca")
public class ProvinceCityAreaController {

    public static void main(String[] args) {

            try {
                ClassPathResource classPathResource = new ClassPathResource("static/行政区划乡镇清单202003.xlsx");
                InputStream is =classPathResource.getInputStream();
                //Resource resource = new ClassPathResource("static/行政区划乡镇清单202003.xlsx");
                //InputStream is = resource.getInputStream();
                //InputStream is = new FileInputStream(new File("D:\\tool\\行政区划乡镇清单202003.xlsx"));
                if (is != null) {
                    XSSFWorkbook workbook = new XSSFWorkbook(is);
                    //Workbook wb = WorkbookFactory.create(is);
                    CellStyle style = workbook.createCellStyle();
                    style.setFillForegroundColor(IndexedColors.RED.getIndex());
//                    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
                    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    //List<UserInfo> userInfoList = new ArrayList<UserInfo>();
                    int rowCount = 0;
                    boolean temp = true;
                    try {
                        Sheet st = workbook.getSheetAt(0);
                        int rowNum = st.getLastRowNum(); //获取Excel最后一行索引，从零开始，所以获取到的是表中最后一行行数减一
                        int colNum = st.getRow(0).getLastCellNum();//获取Excel列数
                        Map<String, String> procMap = new HashMap<>();
                        Map<String, String> cityMap = new HashMap<>();
                        Map<String, String> areaMap = new HashMap<>();
                        Map<String, String> townMap = new HashMap<>();
                        for (int r = 1; r <= rowNum; r++) {//读取每一行，第一行为标题，从第二行开始
                            rowCount = r;
                            Row row = st.getRow(r);
                            //UserInfo userInfo = new UserInfo();
                            String CODE_PROV = "", NAME_PROV = "", CODE_CITY = "", NAME_CITY = "";
                            String CODE_COUN = "", NAME_COUN = "", CODE_TOWN = "", NAME_TOWN = "";
                            for (int l = 0; l < colNum; l++) {//读取每一行的每一列
                                Cell cell = row.getCell(l);
                                if (cell != null) {
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
                                }
                                if (temp) {
                                    switch (l) {
                                        //MAPINFO_ID
                                        case 0://MAPINFO_ID
                                            //userInfo.setEmail(cell.getStringCellValue());
                                            System.out.println(cell.getStringCellValue());
                                            break;
                                        case 1://CODE_PROV
                                            CODE_PROV = cell.getStringCellValue();
                                            break;
                                        case 2://NAME_PROV
                                            NAME_PROV = cell.getStringCellValue();
                                            break;
                                        case 3://CODE_CITY
                                            CODE_CITY = cell.getStringCellValue();
                                            break;
                                        case 4://NAME_CITY
                                            NAME_CITY = cell.getStringCellValue();
                                            break;
                                        case 5://CODE_COUN
                                            CODE_COUN = cell.getStringCellValue();
                                            break;
                                        case 6://NAME_COUN
                                            NAME_COUN = cell.getStringCellValue();
                                            break;
                                        case 7://CODE_TOWN
                                            CODE_TOWN = cell.getStringCellValue();
                                            break;
                                        case 8://NAME_TOWN
                                            NAME_TOWN = cell.getStringCellValue();
                                            break;
                                    }
                                    //userInfo.setConferencesId(conId);
                                    //userInfo.setFromWhere(code);
                                    //userInfo.setCreateTime(new Date());
                                }
                            }
                            procMap.put(CODE_PROV, NAME_PROV);
                            cityMap.put(CODE_CITY, NAME_CITY);
                            areaMap.put(CODE_COUN, NAME_COUN);
                            townMap.put(CODE_TOWN, NAME_TOWN);

                        }
                        if (temp) {//Excel完全没有问题
                            for (Map.Entry<String, String> entry : procMap.entrySet()) {
                                System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                            }
                        } else {//Excel存在必填项为空的情况

                        }
                    } catch (Exception e) {
                        System.out.println("第" + rowCount + "行出错");
                        //msg = "第" + rowCount + "行出错";
                        e.printStackTrace();
                    }
                }
                is.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

    }

}
