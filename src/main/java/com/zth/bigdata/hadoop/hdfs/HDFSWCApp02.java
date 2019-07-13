package com.zth.bigdata.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Map;
import java.util.Set;

/**
 * Author: 3zZ.
 * Date: 2019-07-12 19:40
 * 使用HDFS API完成wordcount统计
 * 需求： 统计HDFS上的文件的wordCount， 然后将统计结果输出到HDFS
 * 功能拆解
 * 1. 读取HDFS上的文件 ==> HDFS API
 * 2. 业务处理(词频统计)： 对文件中的每一行数据都要进行业务处理(按照分隔符分割) ==> Mapper
 * 3. 将处理结果缓存起来 ==> Context
 * 4. 将结果输出到HDFS ==> HDFS API
 */
public class HDFSWCApp02 {
    public static void main(String[] args) throws Exception{
        // 1. 读取HDFS上的文件 ==> HDFS API
        Path input = new Path("/hdfsapi/test/c.txt");

        // 获取到要操作的HDFS文件系统
        FileSystem fs = FileSystem.get(new URI("hdfs://localhost:8020"), new Configuration(), "3zz");

        RemoteIterator<LocatedFileStatus> iterator = fs.listFiles(input, false);

        ZthMapper mapper = new WordCountMapper();
        ZthContext context = new ZthContext();

        while(iterator.hasNext()){
            LocatedFileStatus file = iterator.next();
            FSDataInputStream in = fs.open(file.getPath());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line = "";
            while((line = reader.readLine()) != null){
                // 2. 业务处理(词频统计)
                // TODO... 在业务逻辑完成之后将结果写到Cache中去
                mapper.map(line, context);
            }

            reader.close();
            in.close();
        }
        // TODO... 3. 将处理结果缓存起来 Map

        Map<Object, Object> contextMap = context.getCacheMap();

        // 4. 将结果输出到HDFS ==> HDFS API
        Path output = new Path("/hdfsapi/output/");
        FSDataOutputStream out = fs.create(new Path(output, new Path("wc.out")));

        // TODO... 将第三布缓存中的内容输出到out中去
        Set<Map.Entry<Object, Object>> entries = contextMap.entrySet();
        for(Map.Entry<Object, Object> entry: entries) {
            out.write((entry.getKey().toString() + "\t" + entry.getValue() + "\n").getBytes());
        }
        out.close();
        fs.close();
        System.out.println("3zz HDFS 词频统计 finished");

    }
}
