package com.estate.sdzy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.system.entity.SAuditerCnf;

import java.util.List;
import java.util.Map;

public interface SAuditerCnfService extends IService<SAuditerCnf> {

    boolean save(SAuditerCnf sAuditerCnf, String token);
    boolean update(SAuditerCnf sAuditerCnf, String token);
    boolean delete(String id,String token);
    List<SAuditerCnf> list(Map<String,String> map, Integer pageNo, Integer size, String token);
    Integer listNum(Map<String,String> map,String token);
    String checkSAuditerCnf(SAuditerCnf sAuditerCnf);
}
