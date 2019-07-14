package com.zth.bigdata.hadoop.hdfs;

/**
 * Author: 3zZ.
 * Date: 2019-07-13 23:02
 * 自定义一个wordCount实现类
 */
public class CaseIgnoreWordCountMapper implements ZthMapper {

    public void map(String line, ZthContext context) {
        String[] words = line.toLowerCase().split(" ");
        for(String word: words){
            Object value = context.get(word);
            if(value == null){// 表示没有出现过该单词
                context.write(word, 1);
            } else {
                int v = Integer.parseInt(value.toString());
                context.write(word, v+1); // 取出单词对应的次数并+1
            }
        }
    }
}
