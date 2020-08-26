package com.estate.sdzy.asstes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.exception.BillException;
import com.estate.sdzy.asstes.entity.*;
import com.estate.sdzy.asstes.mapper.*;
import com.estate.sdzy.asstes.service.RRoomService;
import com.estate.sdzy.system.entity.SCompany;
import com.estate.sdzy.system.entity.SDictItem;
import com.estate.sdzy.system.entity.SUser;
import com.estate.sdzy.system.mapper.SCompanyMapper;
import com.estate.sdzy.system.mapper.SDictItemMapper;
import com.estate.util.BillExceptionEnum;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 房间 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
@Service
@Slf4j
public class RRoomServiceImpl extends ServiceImpl<RRoomMapper, RRoom> implements RRoomService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RRoomMapper rRoomMapper;

    @Autowired
    private SCompanyMapper sCompanyMapper;

    @Autowired
    private RCommunityMapper rCommunityMapper;

    @Autowired
    private RCommAreaMapper commAreaMapper;

    @Autowired
    private RBuildingMapper rBuildingMapper;

    @Autowired
    private RUnitMapper rUnitMapper;

    @Autowired
    private SDictItemMapper sDictItemMapper;

    @Autowired
    private ROwnerPropertyMapper rOwnerPropertyMapper;

    @Override
    public boolean save(RRoom rRoom, String token) {
        SUser user = getUserByToken(token);
        if (null == rRoom) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        rRoom.setCreatedBy(user.getId());
        rRoom.setCreatedName(user.getUserName());
        rRoom.setModifiedBy(user.getId());
        rRoom.setModifiedName(user.getUserName());
        int insert = rRoomMapper.insert(rRoom);
        if (insert > 0) {
            log.info("房间添加成功，添加人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
        }
        return insert > 0;
    }

    @Override
    public boolean update(RRoom rRoom, String token) {
        SUser user = getUserByToken(token);
        if (null == rRoom) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        rRoom.setModifiedBy(user.getId());
        rRoom.setModifiedName(user.getUserName());
        int update = rRoomMapper.updateById(rRoom);
        if (update > 0) {
            log.info("房间修改成功，修改人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
        }
        return update > 0;
    }

    private boolean isNum(String str) {
        try {
            new BigDecimal(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean delete(String id, String token) {
        SUser user = getUserByToken(token);
        //if(id.indexOf(",")!=-1){//多选删除
        String[] ids = id.split(",");
        int delete = rRoomMapper.updateBatch(user.getId(),user.getUserName(),ids);
        List<ROwnerProperty> rOwners = getROwnerProperty(id);
        int delOwnerProperty=0;
        if(rOwners.size()>0){
            delOwnerProperty = rRoomMapper.updateOwnerProperty(user.getId(), user.getUserName(), ids);
            if(delete>0&&delOwnerProperty>0){
                log.info("房间删除成功，删除人={}",user.getUserName());
            }else{
                throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
            }
        }else{
            if(delete>0){
                log.info("房间删除成功，删除人={}",user.getUserName());
            }else{
                throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
            }
        }

        return delete>0;
        /*}else{
            if(StringUtils.isEmpty(id)){
                throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
            }
            RRoom rRoom = rRoomMapper.selectById(id);
            rRoom.setModifiedBy(user.getId());
            rRoom.setModifiedName(user.getUserName());
            rRoom.setIsDelete(1);
            QueryWrapper<RRoom> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",id);
            int delete = rRoomMapper.update(rRoom,queryWrapper);
            //如果用户确认删除，进行逻辑删除，房间与业主的关联关系也进行逻辑删除

            if(delete>0){
                log.info("房间删除成功，删除人={}",user.getUserName());
            }else{
                throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
            }
            return delete>0;
        }*/

    }

    @Override
    public List<RRoom> list(Map<String, String> map, Integer pageNo, Integer size, String token) {
        SUser user = getUserByToken(token);
        if(user.getCompId()==0) {
            List<RRoom> rRooms = rRoomMapper.getListRoom(map.get("compName"), map.get("commName"), map.get("commAreaName"),
                    map.get("buildingName"), map.get("unitName"), map.get("roomNo"),
                    map.get("roomModel"), map.get("usable"),map.get("name"),
                    (pageNo - 1) * size, size, null);
            return rRooms;
        }else{
            List<RRoom> rRooms = rRoomMapper.getListRoom(map.get("compName"), map.get("commName"), map.get("commAreaName"),
                    map.get("buildingName"), map.get("unitName"), map.get("roomNo"),
                    map.get("roomModel"), map.get("usable"),map.get("name"),
                    (pageNo - 1) * size, size, user.getId());
            return rRooms;
        }
    }

    @Override
    public Integer listNum(Map<String, String> map, String token) {
        SUser user = getUserByToken(token);
        if(user.getCompId()==0) {
            Integer rRooms = rRoomMapper.getListRoomNum(map.get("compName"), map.get("commName"), map.get("commAreaName"),
                    map.get("buildingName"), map.get("unitName"), map.get("roomNo"),
                    map.get("roomModel"), map.get("usable"),map.get("name"),
                    null, null, null);
            return rRooms;
        }else{
            Integer rRooms = rRoomMapper.getListRoomNum(map.get("compName"), map.get("commName"), map.get("commAreaName"),
                    map.get("buildingName"), map.get("unitName"), map.get("roomNo"),
                    map.get("roomModel"), map.get("usable"),map.get("name"),
                    null, null, user.getId());
            return rRooms;
        }
    }

    @Override
    public String checkRoomOwer(String roomId) {
        //查询该房间下的用户需要验证是否有业主，如果有业主需要提示给用户，如果用户确认删除，进行逻辑删除，房间与业主的关联关系也进行逻辑删除
        //批量查询
        List<ROwnerProperty> rOwners = getROwnerProperty(roomId);
        if(rOwners.size()>0){
            return "您选择的房间下有业主信息，点击删除将同时删除该房间下的业主信息，是否继续删除？";
        }else{
            return "";
        }
    }

    @Override
    public List<RRoom> list(Map<String, String> map, String token) {
        SUser user = getUserByToken(token);
        if(user.getCompId()==0) {
            List<RRoom> rRooms = rRoomMapper.getListRoom(map.get("compName"), map.get("commName"), map.get("commAreaName"),
                    map.get("buildingName"), map.get("unitName"), map.get("roomNo"),
                    map.get("roomModel"), map.get("usable"),map.get("name"),
                    null, null, null);
            return rRooms;
        }else{
            List<RRoom> rRooms = rRoomMapper.getListRoom(map.get("compName"), map.get("commName"), map.get("commAreaName"),
                    map.get("buildingName"), map.get("unitName"), map.get("roomNo"),
                    map.get("roomModel"), map.get("usable"),map.get("name"),
                    null, null, user.getId());
            return rRooms;
        }
    }

    private List<ROwnerProperty> getROwnerProperty(String roomId){
        QueryWrapper<ROwnerProperty> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete",0);
        queryWrapper.eq("property_type",113);//字典项房产id
        queryWrapper.in("property_id",roomId.split(","));
        return rOwnerPropertyMapper.selectList(queryWrapper);
    }

    public SUser getUserByToken(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            throw new BillException(BillExceptionEnum.LOGIN_TIME_OUT);
        }
        return (SUser) o;
    }

    public void saveOrUpdateRoom(RRoom room,String token){
        //保存前进行判断是否已经存在数据
        QueryWrapper<RRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comp_id",room.getCompId());
        queryWrapper.eq("comm_id",room.getCommId());
        queryWrapper.eq("comm_area_id",room.getCommAreaId());
        queryWrapper.eq("unit_id",room.getUnitId());
        queryWrapper.eq("building_id",room.getBuildingId());
        queryWrapper.eq("name",room.getName());
        queryWrapper.eq("room_no",room.getRoomNo());
        queryWrapper.eq("is_delete",0);
        List<RRoom> rRooms = rRoomMapper.selectList(queryWrapper);
        if(rRooms.size()>0){//执行update
            room.setId(rRooms.get(0).getId());
            update(room,token);
        }else{//执行新增
            save(room,token);
        }
    }

    @Override
    public Result importExcel(HttpServletRequest request,String token){
        SUser user = getUserByToken(token);
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        String message="";
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            try {
                InputStream is = file.getInputStream();
                if (is != null) {
                    XSSFWorkbook workbook = new XSSFWorkbook(is);
                    CellStyle style = workbook.createCellStyle();
                    style.setFillForegroundColor(IndexedColors.RED.getIndex());
                    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    int rowCount = 0;
                    boolean temp = true;
                    try {
                        Sheet st = workbook.getSheetAt(0);
                        int rowNum = st.getLastRowNum(); //获取Excel最后一行索引，从零开始，所以获取到的是表中最后一行行数减一
                        int colNum = st.getRow(0).getLastCellNum();//获取Excel列数

                        outterLoop :for (int r = 0; r <= rowNum; r++) {//读取每一行，第一行为标题，从第二行开始
                            rowCount = r;
                            Row row = st.getRow(r);
                            String  compId= "", commId= "", commAreaId = "", buildingId = "",unitId= "", name= "",
                                    roomNo= "", floor = "",floorNum = "",elevatorNum="",roomNum="",roomModel= "",
                                    roomType = "",propertyRightNature = "",direction = "",renovationLevel = "",
                                    usable = "",titleDeedNo = "",landDeedNo= "", contractNo = "",
                                    state= "", remark= "",buildingArea = "",usableArea = "",gardenArea = "";
                            List<String> titles = new ArrayList<>();
                            RRoom room = new RRoom();
                            for (int l = 0; l < colNum; l++) {//读取每一行的每一列
                                Cell cell = row.getCell(l);
                                if (cell != null) {
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
                                }
                                if (temp) {
                                    switch (l) {
                                        case 0:
                                            if(r==0){
                                                if(!"物业公司".equals(cell.getStringCellValue())){
                                                    //提示第一列的标题错误
                                                    return ResultUtil.success("第"+(l+1)+"列标题错误，应为物业公司");
                                                }
                                            }else{
                                                compId = cell.getStringCellValue();
                                                if(StringUtils.isEmpty(compId)){
                                                    message += "第"+r+"行物业公司为空，无法导入";
                                                    break outterLoop;
                                                }else{
                                                    //查找名称是否正确
                                                    QueryWrapper<SCompany> sCompanyQueryWrapper = new QueryWrapper<>();
                                                    sCompanyQueryWrapper.eq("is_delete","0");
                                                    sCompanyQueryWrapper.eq("name",compId);
                                                    List<SCompany> sCompanies = sCompanyMapper.selectList(sCompanyQueryWrapper);
                                                    if(sCompanies.size()==1){
                                                        room.setCompId(sCompanies.get(0).getId());
                                                    }else{
                                                        message += "第"+r+"行物业公司名称有误，无法导入";
                                                        break outterLoop;
                                                    }
                                                }
                                            }
                                            break;
                                        case 1://CODE_PROV
                                            if(r==0){
                                                if(!"社区".equals(cell.getStringCellValue())){
                                                    //提示第一列的标题错误
                                                    return ResultUtil.success("第"+(l+1)+"列标题错误，应为社区");
                                                }
                                            }else{
                                                commId = cell.getStringCellValue();
                                                //查找名称是否正确
                                                if(StringUtils.isEmpty(commId)){
                                                    message += "第"+r+"行社区名称为空，无法导入";
                                                    break outterLoop;
                                                }else {
                                                    QueryWrapper<RCommunity> QueryWrapper = new QueryWrapper<>();
                                                    QueryWrapper.eq("is_delete", "0");
                                                    QueryWrapper.eq("comp_id",room.getCompId());
                                                    QueryWrapper.eq("name", commId);
                                                    List<RCommunity> sRCommunities = rCommunityMapper.selectList(QueryWrapper);
                                                    if (sRCommunities.size() == 1) {
                                                        room.setCommId(sRCommunities.get(0).getId());
                                                    } else {
                                                        message += "第" + r + "行社区名称有误，无法导入";
                                                        break outterLoop;
                                                    }
                                                }
                                            }
                                            break;
                                        case 2://NAME_PROV
                                            if(r==0){
                                                if(!"分区".equals(cell.getStringCellValue())){
                                                    //提示第一列的标题错误
                                                    return ResultUtil.success("第"+(l+1)+"列标题错误，应为分区");
                                                }
                                            }else{
                                                commAreaId = cell.getStringCellValue();
                                                //查找名称是否正确
                                                if(StringUtils.isEmpty(commAreaId)){
                                                    message += "第"+r+"行分区名称为空，无法导入";
                                                    break outterLoop;
                                                }else {
                                                    QueryWrapper<RCommArea> QueryWrapper = new QueryWrapper<>();
                                                    QueryWrapper.eq("is_delete", "0");
                                                    QueryWrapper.eq("comm_id",room.getCommId());
                                                    QueryWrapper.eq("name", commAreaId);
                                                    List<RCommArea> rCommAreas = commAreaMapper.selectList(QueryWrapper);
                                                    if (rCommAreas.size() == 1) {
                                                        room.setCommAreaId(rCommAreas.get(0).getId());
                                                    } else {
                                                        message += "第" + r + "行分区名称有误，无法导入";
                                                        break outterLoop;
                                                    }
                                                }
                                            }
                                            break;
                                        case 3://NAME_CITY
                                            if(r==0){
                                                if(!"建筑".equals(cell.getStringCellValue())){
                                                    //提示第一列的标题错误
                                                    return ResultUtil.success("第"+(l+1)+"列标题错误，应为建筑");
                                                }
                                            }else{
                                                buildingId = cell.getStringCellValue();
                                                //查找名称是否正确
                                                if(StringUtils.isEmpty(buildingId)){
                                                    message += "第"+r+"行建筑名称为空，无法导入";
                                                    break outterLoop;
                                                }else {
                                                    QueryWrapper<RBuilding> QueryWrapper = new QueryWrapper<>();
                                                    QueryWrapper.eq("is_delete","0");
                                                    QueryWrapper.eq("comm_area_id",room.getCommAreaId());
                                                    QueryWrapper.eq("name",buildingId);
                                                    List<RBuilding> rBuildings = rBuildingMapper.selectList(QueryWrapper);
                                                    if(rBuildings.size()==1){
                                                        room.setBuildingId(rBuildings.get(0).getId());
                                                    }else{
                                                        message += "第"+r+"行建筑名称有误，无法导入";
                                                        break outterLoop;
                                                    }
                                                }
                                            }
                                            break;
                                        case 4://CODE_COUN
                                            if(r==0){
                                                if(!"单元".equals(cell.getStringCellValue())){
                                                    //提示第一列的标题错误
                                                    return ResultUtil.success("第"+(l+1)+"列标题错误，应为单元");
                                                }
                                            }else{
                                                unitId = cell.getStringCellValue();
                                                //查找名称是否正确
                                                if(StringUtils.isEmpty(unitId)){
                                                    message += "第"+r+"行单元名称为空，无法导入";
                                                    break outterLoop;
                                                }else {
                                                    QueryWrapper<RUnit> QueryWrapper = new QueryWrapper<>();
                                                    QueryWrapper.eq("is_delete","0");
                                                    QueryWrapper.eq("building_id",room.getBuildingId());
                                                    QueryWrapper.eq("name",unitId);
                                                    List<RUnit> rUnits = rUnitMapper.selectList(QueryWrapper);
                                                    if(rUnits.size()==1){
                                                        room.setUnitId(rUnits.get(0).getId());
                                                    }else{
                                                        message += "第"+r+"行单元名称有误，无法导入";
                                                        break outterLoop;
                                                    }
                                                }
                                            }
                                            break;
                                        case 5://CODE_CITY
                                            if(!check(r, "房间号", roomNo, cell)){
                                                return ResultUtil.success("第"+((l+1))+"列标题错误，应为房间号");
                                            }else{
                                                if(r!=0){
                                                    if(StringUtils.isEmpty(cell.getStringCellValue())){
                                                        message += "第"+r+"行房间号为空，无法导入";
                                                        break outterLoop;
                                                    }else{
                                                        room.setRoomNo(cell.getStringCellValue());
                                                    }
                                                }
                                            }
                                            break;
                                        case 6://NAME_COUN
                                            if(!check(r, "房间名称", name, cell)){
                                                return ResultUtil.success("第"+(l+1)+"列标题错误，应为房间名称");
                                            }else{
                                                if(r!=0){
                                                    if(StringUtils.isEmpty(cell.getStringCellValue())){
                                                        message += "第"+r+"行房间名称为空，无法导入";
                                                        break outterLoop;
                                                    }else{
                                                        room.setName(cell.getStringCellValue());
                                                    }
                                                }
                                            }
                                            break;
                                        case 7://CODE_TOWN
                                            if(!check(r, "楼层", floor, cell)){
                                                return ResultUtil.success("第"+(l+1)+"列标题错误，应为楼层");
                                            }else{
                                                if(r!=0){
                                                    if(StringUtils.isEmpty(cell.getStringCellValue())){
                                                        message += "第"+r+"行楼层为空，无法导入";
                                                        break outterLoop;
                                                    }else{
                                                        room.setFloor(Integer.parseInt(cell.getStringCellValue()));
                                                    }
                                                }
                                            }
                                            break;
                                        case 8://NAME_TOWN
                                            if(!check(r, "楼层数", floorNum, cell)){
                                                return ResultUtil.success("第"+(l+1)+"列标题错误，应为楼层数");
                                            }
                                            break;
                                        case 9://NAME_TOWN
                                            if(!check(r, "电梯数", elevatorNum, cell)){
                                                return ResultUtil.success("第"+(l+1)+"列标题错误，应为电梯数");
                                            }
                                            break;
                                        case 10://NAME_TOWN
                                            if(!check(r, "每层房间数", roomNum, cell)){
                                                return ResultUtil.success("第"+(l+1)+"列标题错误，应为每层房间数");
                                            }
                                            break;
                                        case 11://NAME_TOWN
                                            if(r==0){
                                                if(!"房型".equals(cell.getStringCellValue())){
                                                    //提示第一列的标题错误
                                                    return ResultUtil.success("第"+(l+1)+"列标题错误，应为房型");
                                                }
                                            }else{
                                                roomModel = cell.getStringCellValue();
                                                //判断房型字典是否正确
                                                QueryWrapper<SDictItem> QueryWrapper = new QueryWrapper<>();
                                                QueryWrapper.eq("is_delete","0");
                                                QueryWrapper.eq("name",roomModel);
                                                QueryWrapper.eq("dict_id",44);//44是房型的id
                                                List<SDictItem> sDictItems = sDictItemMapper.selectList(QueryWrapper);
                                                if(sDictItems.size()==1){
                                                    room.setRoomModel(String.valueOf(sDictItems.get(0).getId()));
                                                }else{
                                                    message += "第"+r+"行房型名称有误，无法导入";
                                                    break outterLoop;
                                                }
                                            }
                                            break;
                                        case 12://NAME_TOWN
                                            if(r==0){
                                                if(!"房屋类型".equals(cell.getStringCellValue())){
                                                    //提示第一列的标题错误
                                                    return ResultUtil.success("第"+(l+1)+"列标题错误，应为房屋类型");
                                                }
                                            }else{
                                                roomType = cell.getStringCellValue();
                                                //判断房屋类型字典是否正确
                                                QueryWrapper<SDictItem> QueryWrapper = new QueryWrapper<>();
                                                QueryWrapper.eq("is_delete","0");
                                                QueryWrapper.eq("name",roomType);
                                                QueryWrapper.eq("dict_id",1);//1是建筑类型的id
                                                List<SDictItem> sDictItems = sDictItemMapper.selectList(QueryWrapper);
                                                if(sDictItems.size()==1){
                                                    room.setRoomType(String.valueOf(sDictItems.get(0).getId()));
                                                }else{
                                                    message += "第"+r+"行房屋类型名称有误，无法导入";
                                                    break outterLoop;
                                                }
                                            }
                                            break;
                                        case 13://NAME_TOWN
                                            if(r==0){
                                                if(!"产权性质".equals(cell.getStringCellValue())){
                                                    //提示第一列的标题错误
                                                    return ResultUtil.success("第"+(l+1)+"列标题错误，应为产权性质");
                                                }
                                            }else{
                                                propertyRightNature = cell.getStringCellValue();
                                                //判断产权性质字典是否正确
                                                QueryWrapper<SDictItem> QueryWrapper = new QueryWrapper<>();
                                                QueryWrapper.eq("is_delete","0");
                                                QueryWrapper.eq("name",propertyRightNature);
                                                QueryWrapper.eq("dict_id",27);///27是产权性质的id
                                                List<SDictItem> sDictItems = sDictItemMapper.selectList(QueryWrapper);
                                                if(sDictItems.size()==1){
                                                    room.setPropertyRightNature(String.valueOf(sDictItems.get(0).getId()));
                                                }else{
                                                    message += "第"+r+"行产权性质名称有误，无法导入";
                                                    break outterLoop;
                                                }
                                            }
                                            break;
                                        case 14://NAME_TOWN
                                            if(r==0){
                                                if(!"朝向".equals(cell.getStringCellValue())){
                                                    //提示第一列的标题错误
                                                    return ResultUtil.success("第"+(l+1)+"列标题错误，应为朝向");
                                                }
                                            }else{
                                                direction = cell.getStringCellValue();
                                                //判断朝向字典是否正确
                                                QueryWrapper<SDictItem> QueryWrapper = new QueryWrapper<>();
                                                QueryWrapper.eq("is_delete","0");
                                                QueryWrapper.eq("name",direction);
                                                QueryWrapper.eq("dict_id",30);///30是朝向的id
                                                List<SDictItem> sDictItems = sDictItemMapper.selectList(QueryWrapper);
                                                if(sDictItems.size()==1){
                                                    room.setDirection(String.valueOf(sDictItems.get(0).getId()));
                                                }else{
                                                    message += "第"+r+"行朝向名称有误，无法导入";
                                                    break outterLoop;
                                                }
                                            }
                                            break;
                                        case 15://NAME_TOWN
                                            if(r==0){
                                                if(!"装修程度".equals(cell.getStringCellValue())){
                                                    //提示第一列的标题错误
                                                    return ResultUtil.success("第"+(l+1)+"列标题错误，应为装修程度");
                                                }
                                            }else{
                                                renovationLevel = cell.getStringCellValue();
                                                //判断装修程度字典是否正确
                                                QueryWrapper<SDictItem> QueryWrapper = new QueryWrapper<>();
                                                QueryWrapper.eq("is_delete","0");
                                                QueryWrapper.eq("name",renovationLevel);
                                                QueryWrapper.eq("dict_id",31);//31是装修程度的id
                                                List<SDictItem> sDictItems = sDictItemMapper.selectList(QueryWrapper);
                                                if(sDictItems.size()==1){
                                                    room.setRenovationLevel(String.valueOf(sDictItems.get(0).getId()));
                                                }else{
                                                    message += "第"+r+"行装修程度名称有误，无法导入";
                                                    break outterLoop;
                                                }
                                            }
                                            break;
                                        case 16://NAME_TOWN
                                            if(r==0){
                                                if(!"用途".equals(cell.getStringCellValue())){
                                                    //提示第一列的标题错误
                                                    return ResultUtil.success("第"+(l+1)+"列标题错误，应为用途");
                                                }
                                            }else{
                                                usable = cell.getStringCellValue();
                                                //判断用途字典是否正确
                                                QueryWrapper<SDictItem> QueryWrapper = new QueryWrapper<>();
                                                QueryWrapper.eq("is_delete","0");
                                                QueryWrapper.eq("name",usable);
                                                QueryWrapper.eq("dict_id",7);//7是用途的id
                                                List<SDictItem> sDictItems = sDictItemMapper.selectList(QueryWrapper);
                                                if(sDictItems.size()==1){
                                                    room.setUsable(String.valueOf(sDictItems.get(0).getId()));
                                                }else{
                                                    message += "第"+r+"行用途名称有误，无法导入";
                                                    break outterLoop;
                                                }
                                            }
                                            break;
                                        case 17://NAME_TOWN
                                            if(!check(r, "产权证号", titleDeedNo, cell)){
                                                return ResultUtil.success("第"+(l+1)+"列标题错误，应为产权证号");
                                            }else{
                                                if(r!=0){
                                                    room.setTitleDeedNo(cell.getStringCellValue());
                                                }
                                            }
                                            break;
                                        case 18://NAME_TOWN
                                            if(!check(r, "土地证号", landDeedNo, cell)){
                                                return ResultUtil.success("第"+(l+1)+"列标题错误，应为土地证号");
                                            }else{
                                                if(r!=0)room.setLandDeedNo(cell.getStringCellValue());
                                            }
                                            break;
                                        case 19://NAME_TOWN
                                            if(!check(r, "购房合同号", contractNo, cell)){
                                                return ResultUtil.success("第"+(l+1)+"列标题错误，应为购房合同号");
                                            }else{
                                                if(r!=0)room.setContractNo(cell.getStringCellValue());
                                            }
                                            break;
                                        case 20://NAME_TOWN
                                            if(!check(r, "建筑面积", buildingArea, cell)){
                                                return ResultUtil.success("第"+(l+1)+"列标题错误，应为建筑面积");
                                            }else{
                                                if(r!=0){
                                                    if(StringUtils.isEmpty(cell.getStringCellValue())){
                                                        message += "第"+r+"行建筑面积为空，无法导入";
                                                        break outterLoop;
                                                    }else{
                                                        try {
                                                            room.setBuildingArea(new BigDecimal(cell.getStringCellValue()));
                                                        }catch (Exception e){
                                                            e.printStackTrace();
                                                            message += "第"+r+"行建筑面积格式有误，无法导入";
                                                            break outterLoop;
                                                        }
                                                    }
                                                }
                                            }
                                            break;
                                        case 21://NAME_TOWN 建筑面积 使用面积 花园面积
                                            if(!check(r, "使用面积", usableArea, cell)){
                                                return ResultUtil.success("第"+(l+1)+"列标题错误，应为使用面积");
                                            }else{
                                                if(r!=0){
                                                    if(StringUtils.isEmpty(cell.getStringCellValue())){
                                                        message += "第"+r+"行使用面积为空，无法导入";
                                                        break outterLoop;
                                                    }else{
                                                        try {
                                                            room.setUsableArea(new BigDecimal(cell.getStringCellValue()));
                                                        }catch (Exception e){
                                                            e.printStackTrace();
                                                            message += "第"+r+"行使用面积格式有误，无法导入";
                                                            break outterLoop;
                                                        }
                                                    }
                                                }
                                            }
                                            break;
                                        case 22://NAME_TOWN
                                            if(!check(r, "花园面积", gardenArea, cell)){
                                                return ResultUtil.success("第"+(l+1)+"列标题错误，应为花园面积");
                                            }else{
                                                if(r!=0){
                                                    try {
                                                        room.setGardenArea(new BigDecimal(cell.getStringCellValue()));
                                                    }catch (Exception e){
                                                        e.printStackTrace();
                                                        message += "第"+r+"行花园面积格式有误，无法导入";
                                                        break outterLoop;
                                                    }
                                                }
                                            }
                                            break;
                                        case 23://NAME_TOWN
                                            if(!check(r, "状态", state, cell)){
                                                return ResultUtil.success("第"+(l+1)+"列标题错误，应为状态");
                                            }else{
                                                if(r!=0){
                                                    if(StringUtils.isEmpty(cell.getStringCellValue())){
                                                        message += "第"+r+"行状态为空，无法导入";
                                                        break outterLoop;
                                                    }else{
                                                        if("在用".equals(cell.getStringCellValue())||"不在用".equals(cell.getStringCellValue())){
                                                            room.setState(cell.getStringCellValue());
                                                        }else{
                                                            message += "第"+r+"行状态有误，无法导入";
                                                            break outterLoop;
                                                        }
                                                    }
                                                }
                                            }
                                            break;
                                        case 24://NAME_TOWN
                                            if(!check(r, "备注", remark, cell)){
                                                return ResultUtil.success("第"+(l+1)+"列标题错误，应为备注");
                                            }else{
                                                if(r!=0)room.setRemark(cell.getStringCellValue());
                                            }
                                            break;
                                    }
                                }
                            }
                            if(r!=0){
                                //保存前进行判断是否已经存在数据
                                QueryWrapper<RRoom> queryWrapper = new QueryWrapper<>();
                                queryWrapper.eq("comp_id",room.getCompId());
                                queryWrapper.eq("comm_id",room.getCommId());
                                queryWrapper.eq("comm_area_id",room.getCommAreaId());
                                queryWrapper.eq("unit_id",room.getUnitId());
                                queryWrapper.eq("building_id",room.getBuildingId());
                                queryWrapper.eq("name",room.getName());
                                queryWrapper.eq("room_no",room.getRoomNo());
                                queryWrapper.eq("is_delete",0);
                                List<RRoom> rRooms = rRoomMapper.selectList(queryWrapper);
                                if(rRooms.size()>0){//执行update
                                    room.setId(rRooms.get(0).getId());
                                    update(room,token);
                                }else{//执行新增
                                    save(room,token);
                                }
                            }
                        }

                    } catch (Exception e) {
                        System.out.println("第" + rowCount + "行出错");
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ResultUtil.success(message);
    }

    public boolean check(int r, String title, String name, Cell cell){
        if(r==0){
            if(!title.equals(cell.getStringCellValue())){
                //提示第一列的标题错误
                return false;
            }else{
                return true;
            }
        }else{
            //name = cell.getStringCellValue();
            //查找名称是否正确
            return true;
        }
    }
}
