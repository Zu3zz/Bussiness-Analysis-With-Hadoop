package com.zth.bigdata.hadoop.hdfs;

/**
 * Author: 3zZ.
 * Date: 2019-07-13 22:44
 */
public interface ZthMapper {
    /**
     * @param line 读取到每一行数据
     * @param context 上下文/缓存
     */
    public void map(String line, ZthContext context);
}
