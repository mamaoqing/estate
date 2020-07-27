package com.estate.util;

public class ResultUtil {

    public static Result success(Object o){
        Result result = new Result();

        result.setCode(0);
        result.setMsg("成功");
        result.setData(o);
        return result;
    }
    public static Result success(){
        return success(null);
    }

    public static Result error(String msg,Integer code){
        Result result = new Result();

        result.setCode(code);
        result.setMsg(msg);

        return result;
    }
}
