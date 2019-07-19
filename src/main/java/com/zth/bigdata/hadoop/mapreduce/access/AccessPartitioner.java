package com.zth.bigdata.hadoop.mapreduce.access;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * Author: 3zZ.
 * Date: 2019-07-18 12:37
 * MapReduce自定义分区规则
 */
public class AccessPartitioner extends Partitioner<Text, Access> {
    /**
     * @param text 手机号
     */
    @Override
    public int getPartition(Text phone, Access access, int numPartitions) {
        if (phone.toString().startsWith("13")) {
            return 0;
        } else if (phone.toString().startsWith("15")) {
            return 1;
        } else {
            return 2;
        }
    }
}
