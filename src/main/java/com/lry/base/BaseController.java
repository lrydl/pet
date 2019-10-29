package com.lry.base;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:刘仁有
 * @desc:
 * @email:953506233@qq.com
 * @data:2019/10/19
 */
public class BaseController {

    protected Map<String,Object> toResponseOK(Object data){
        Map<String,Object> res = new HashMap<>();
        res.put("code",0);
        res.put("data",data);
        return res;
    }

    protected Map<String,Object> toResponseNOT(String msg){
        Map<String,Object> res = new HashMap<>();
        res.put("code",400);
        res.put("msg",msg);
        return res;
    }
}
