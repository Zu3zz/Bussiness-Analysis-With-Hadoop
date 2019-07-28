package com.zth.bigdata.hadoop.mapreduce.ecproject.utils;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: 3zZ.
 * Date: 2019-07-21 21:26
 * 日志解析类
 */
public class LogParser {

    Map<String, String> info = new HashMap<>();
    IPParser ipParser = IPParser.getInstance();

    public Map<String, String> parse(String log) {
        if (StringUtils.isNotBlank(log)) {
            String[] splits = log.split("\u0001");

            String ip = splits[13];
            String country = "-";
            String province = "-";
            String city = "-";
            IPParser.RegionInfo regionInfo = ipParser.analyseIp(ip);
            if(regionInfo != null){
                country = regionInfo.getCountry();
                province = regionInfo.getProvince();
                city = regionInfo.getCity();
            }
            info.put("ip",ip);
            info.put("country",country);
            info.put("province",province);
            info.put("city",city);
        }
        return info;
    }
}
