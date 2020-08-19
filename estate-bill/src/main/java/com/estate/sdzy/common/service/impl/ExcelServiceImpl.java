package com.estate.sdzy.common.service.impl;

import com.estate.exception.BillException;
import com.estate.sdzy.common.excel.ExportExcel;
import com.estate.sdzy.common.service.ExcelService;
import com.estate.sdzy.system.entity.SUser;
import com.estate.util.BillExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class ExcelServiceImpl implements ExcelService {
    private final RedisTemplate<String, Object> redisTemplate;
    @Override
    public void writeOut(HttpServletResponse response, String token, String className) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        SUser user = getUserByToken(token);
        ExportExcel.writeOut(response,"停车位信息列表",className,null,"导出人：");
    }

    private SUser getUserByToken(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            throw new BillException(BillExceptionEnum.LOGIN_TIME_OUT);
        }
        return (SUser) o;
    }
}
