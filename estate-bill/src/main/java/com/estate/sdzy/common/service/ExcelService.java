package com.estate.sdzy.common.service;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

public interface ExcelService {

    void writeOut(HttpServletResponse response,String token,String className) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
