package com.lry.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * @author:刘仁有
 * @desc:
 * @email:953506233@qq.com
 * @data:2019/6/27
 */
public class RandomUtil {

    public static void main(String[] args) throws Exception {
    }
    public static String getRandomStr(int len){
        char[] words = new char[len];
        for (int i = 0; i<words.length; i++) {
            words[i] = getRandomChar();
        }
        return String.valueOf(words);
    }
    public static char getRandomChar() {
        String str = "";
        int hightPos;
        int lowPos;

        Random random = new Random();

        hightPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = (161 + Math.abs(random.nextInt(93)));

        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(hightPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();

        try {
            str = new String(b, "GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return str.charAt(0);
    }
}
