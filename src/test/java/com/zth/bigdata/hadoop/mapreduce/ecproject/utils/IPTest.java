package com.zth.bigdata.hadoop.mapreduce.ecproject.utils;

import org.junit.Test;

/**
 * Author: 3zZ.
 * Date: 2019-07-21 21:19
 */
public class IPTest {
    @Test
    public void testIP(){
        IPParser.RegionInfo regionInfo = IPParser.getInstance().analyseIp("223.72.93.192");
        System.out.println(regionInfo.getCountry());
        System.out.println(regionInfo.getCity());
        System.out.println(regionInfo.getProvince());
    }
}
