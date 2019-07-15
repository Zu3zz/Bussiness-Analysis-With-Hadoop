package com.zth.bigdata.hadoop.mapreduce.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Author: 3zZ.
 * Date: 2019-07-15 21:41
 * 使用MapReduce统计HDFS上的文件对应的词频
 *
 * Driver: 配置Mapper，Reducer的相关属性
 *
 * 提交到本地运行: 开发过程中使用
 */
public class WordCountApp {
    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS","hdfs://localhost:8020");
        // 创建一个Job
        Job job = Job.getInstance(configuration);

        // 设置Job对应的参数: 主类
        job.setJarByClass(WordCountApp.class);
        // 设置Job对应的参数: 设置自定义的Mapper和Reducer处理类
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        // 设置Job对应的参数: Mapper输出key和value的类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // 设置Job对应的参数: Mapper输出key和value的类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 设置Job对应的参数: 作业输入和输出的路径
        FileInputFormat.setInputPaths(job, new Path("/wordcount/input"));
        FileOutputFormat.setOutputPath(job, new Path("/wordcount/output"));

        // 提交job
        boolean result = job.waitForCompletion(true);

        System.exit(result ? 0 : -1);
    }
}
