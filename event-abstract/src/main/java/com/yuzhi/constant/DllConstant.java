package com.yuzhi.constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class DllConstant {

    private static String hownetPath;


    @Value("${hownetPathName}")
    public void setHownetPath(String hownetPathName){
        DllConstant.hownetPath = hownetPathName;
    }

    public static String getHownetPath(){
        return System.getProperty("user.dir").substring(0, System.getProperty("user.dir").lastIndexOf("\\") + 1) + hownetPath+"\\";
    }


    //hownet路径
    //public static String INITIAL_PATH = System.getProperty("user.dir").substring(0, System.getProperty("user.dir").lastIndexOf("\\") + 1) + "hownet\\";
    /**
     * 解析结果中Key标签前缀
     */
    public static final String KEY_MENTION = "mention";//触发词
    public static final String KEY_SUB = "sub";//主体
    public static final String KEY_OBJ = "obj";//客体
    public static final String KEY_DATE = "date";//时间
    public static final String KEY_AMOUNT = "amount";//程度


}
