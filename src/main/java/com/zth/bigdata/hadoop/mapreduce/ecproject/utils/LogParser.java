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

    public Map<String, String> parse(String log) {
        Map<String, String> info = new HashMap<>();
        IPParser ipParser = IPParser.getInstance();
        if (StringUtils.isNotBlank(log)) {
            String[] splits = log.split("\u0001");

            String ip = splits[13];
            String url = splits[1];
            String time = splits[17];
            String country = "-";
            String province = "-";
            String city = "-";
            IPParser.RegionInfo regionInfo = ipParser.analyseIp(ip);
            if (regionInfo != null) {
                country = regionInfo.getCountry();
                province = regionInfo.getProvince();
                city = regionInfo.getCity();
            }
            info.put("ip", ip);
            info.put("country", country);
            info.put("province", province);
            info.put("city", city);
            info.put("url", url);
            info.put("time", time);
        }
        return info;
    }

    public Map<String, String> parseV2(String log) {
        Map<String, String> info = new HashMap<>();

        if(StringUtils.isNotBlank(log)){
            String[] splits = log.split("\t");

            String ip = splits[0];
            String country = splits[1];
            String province = splits[2];
            String city = splits[3];
            info.put("ip", ip);
            info.put("country", country);
            info.put("province", province);
            info.put("city", city);

            String url = splits[4];
            info.put("url", url);

            String time = splits[5];
            info.put("time", time);

            String pageId = splits[6];
            info.put("pageId", pageId);
        }
        return info;
    }
}
