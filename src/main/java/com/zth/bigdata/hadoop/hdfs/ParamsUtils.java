package com.zth.bigdata.hadoop.hdfs;

import java.io.IOException;
import java.util.Properties;

/**
 * Author: 3zZ.
 * Date: 2019-07-14 00:11
 */
public class ParamsUtils {

    private static Properties properties = new Properties();

    static {
        try {
            properties.load(ParamsUtils.class.getClassLoader().getResourceAsStream("wc.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Properties getProperties() throws Exception {
        return properties;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getProperties().getProperty("INPUT_PATH"));
    }
}
