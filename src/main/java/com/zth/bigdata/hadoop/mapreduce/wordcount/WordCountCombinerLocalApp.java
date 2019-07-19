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
 * Date: 2019-07-16 00:00
 *
 * 使用本地文件进行统计，然后统计结果输出到本地路径
 */
public class WordCountCombinerLocalApp {
    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();
        // 创建一个Job
        Job job = Job.getInstance(configuration);

        // 设置Job对应的参数: 主类
        job.setJarByClass(WordCountApp.class);
        // 设置Job对应的参数: 设置自定义的Mapper和Reducer处理类
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        // 添加Combiner的设置
        job.setCombinerClass(WordCountReducer.class);

        // 设置Job对应的参数: Mapper输出key和value的类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // 设置Job对应的参数: Mapper输出key和value的类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 设置Job对应的参数: 作业输入和输出的路径
        FileInputFormat.setInputPaths(job, new Path("input"));
        FileOutputFormat.setOutputPath(job, new Path("output"));

        // 提交job
        boolean result = job.waitForCompletion(true);

        System.exit(result ? 0 : -1);
    }
}
