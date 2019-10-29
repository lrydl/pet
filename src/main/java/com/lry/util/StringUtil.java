package com.lry.util;

/**
 * @author:刘仁有
 * @desc:
 * @email:953506233@qq.com
 * @data:2019/10/19
 */
public class StringUtil {

    public static boolean isEmpty(String str){
        if(null==str||"".equals(str)){
            return true;
        }
        return false;
    }
}
