package com.zth.bigdata.hadoop.mapreduce.ecproject.mrv2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Author: 3zZ.
 * Date: 2019-07-21 19:30
 * 第二版 统计浏览量
 */
public class PVStatV2App {
    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();

        FileSystem fileSystem = FileSystem.get(configuration);
        Path outputPath = new Path("access/output/v2/pvstat");
        if(fileSystem.exists(outputPath)){
            fileSystem.delete(outputPath,true);
        }

        Job job = Job.getInstance(configuration);
        job.setJarByClass(PVStatV2App.class);

        job.setMapperClass(MyMapper.class);

        job.setReducerClass(MyReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(LongWritable.class);

        FileInputFormat.setInputPaths(job, new Path("access/input/etl"));
        FileOutputFormat.setOutputPath(job, new Path("access/output/v2/pvstat"));

        job.waitForCompletion(true);

    }

    static class MyMapper extends Mapper<LongWritable, Text, Text, LongWritable>{
        private Text KEY = new Text("key");
        private LongWritable ONE = new LongWritable(1);
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            context.write(KEY, ONE);
        }
    }

    static class MyReducer extends Reducer<Text, LongWritable, NullWritable, LongWritable>{
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            long count = 0;
            for(LongWritable value: values){
                count++;
            }
            context.write(NullWritable.get(), new LongWritable(count));
        }

    }
}
