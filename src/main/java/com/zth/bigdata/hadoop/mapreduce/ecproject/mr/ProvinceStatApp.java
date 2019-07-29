package com.zth.bigdata.hadoop.mapreduce.ecproject.mr;

import com.zth.bigdata.hadoop.mapreduce.ecproject.utils.IPParser;
import com.zth.bigdata.hadoop.mapreduce.ecproject.utils.LogParser;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Map;

/**
 * Author: 3zZ.
 * Date: 2019-07-23 23:36
 * 省份浏览量统计
 */
public class ProvinceStatApp {
    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();

        FileSystem fileSystem = FileSystem.get(configuration);
        Path outputPath = new Path("access/output/v1/provincestat");
        if (fileSystem.exists(outputPath)) {
            fileSystem.delete(outputPath, true);
        }

        Job job = Job.getInstance(configuration);
        job.setJarByClass(ProvinceStatApp.class);

        job.setMapperClass(MyMapper.class);

        job.setReducerClass(MyReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        FileInputFormat.setInputPaths(job, new Path("access/input/raw/trackinfo_20130721.txt"));
        FileOutputFormat.setOutputPath(job, new Path("access/output/v1/provincestat"));

        job.waitForCompletion(true);

    }

    static class MyMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

        private LongWritable ONE = new LongWritable(1);

        private LogParser parser;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            parser = new LogParser();
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String log = value.toString();
            Map<String, String> info = parser.parse(log);
            String ip = info.get("ip");


            if (StringUtils.isNotBlank(ip)) {
                IPParser.RegionInfo regionInfo = IPParser.getInstance().analyseIp(ip);
                if (regionInfo != null) {
                    String province = regionInfo.getProvince();
                    if (StringUtils.isNotBlank(province)) {
                        context.write(new Text(province), ONE);
                    } else {
                        context.write(new Text("-"), ONE);
                    }
                } else {
                    context.write(new Text("-"), ONE);
                }
            } else {
                context.write(new Text("-"), ONE);
            }
        }
    }

    static class MyReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            long count = 0;
            for (LongWritable access : values) {
                count++;
            }
            context.write(key, new LongWritable(count));
        }
    }
}
