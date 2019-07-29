package com.zth.bigdata.hadoop.mapreduce.ecproject.utils;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: 3zZ.
 * Date: 2019-07-28 23:50
 */
public class ContentUtils {
    public static String getPageId(String url){
        String pageId = "-";
        if(StringUtils.isBlank(url)){
            return pageId;
        }
        Pattern pattern = Pattern.compile("topicId=[0-9]+");
        Matcher matcher = pattern.matcher(url);

        if(matcher.find()){
            pageId = matcher.group().split("topicId=")[1];
        }
        return pageId;
    }
}
