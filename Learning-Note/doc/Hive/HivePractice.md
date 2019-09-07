# Hive 外部表、内部表

## 内部表

可以通过 formatted 查看表的属性

```sql
desc formattede mp2

可以看到一系列属性 其中有属性如下

Table Type: MANAGED_TABLE

MANAGED_TABLE就代emp2是一个内部表

删除emp2
drop table emp2;
```

删除表: HDFS 上的数据被删除 & Meta 也被删除

## 外部表

创建外部表

```sql
CREATE EXTERNNAL TABLE emp_external(
empno int,
ename string,
job string,
mgr int,
hiredate string,
sal double,
comm double,
deptno int
) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
location '/external/emp/';

此时表是空表 还需要加载数据
LOAD DATA LOCAL INPATH '/home/hadoop/data/emp.txt' ONVERWRITE INTO TABLE emp_external

此时通过desc查看表的属性
Table Type: EXTERNAL_TABLE
是一个外部表

drop table emp_external
```

删除表: HDFS 上的数据不被删除 & Meta 上被删除  
安全性更好

## 分区表

使用分区表创建

```sql
CREATE EXTERNAL TABLE track_info(
ip string,
country string,
province string,
city string,
url string,
time string,
page string
) partitioned by (day string)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
location '/project/trackinfo/';
```

此时通过执行 jar 包生成 ETL 文件

将这个文件存储到 hive 中

```sql
LOAD DATA INPATH 'hdfs://localhost:8020/project/input/etl'
OVERWRITE INTO TABLE track_info partition(day='2013-07-21');

统计总数
select count(*) from track_info where day='2013-07-21';

统计省份的个数
select province,count(*) from track_info where day='2013-07-21' group by province;

为了方便展示 创建一张表用来存储省份的信息
create table track_info_province_stat(
province string,
cnt bigint
) partitioned by (day string)
row format delimited fields terminated by '\t';

通过sql语句直接写入数据
insert overwrite table track_info_province_stat partition(day='2013-07-21') select province, count(*) as cnt from track_info where day='2013-07-21' group by province;

统计页面访问情况
select page,count(*) from track_info where day = '2013-07-21' group by page;

创建一张表用来存储页面访问信息
create table track_info_page_stat(
province string,
cnt bigint
) partitioned by (day string)
row format delimited fields terminated by '\t';

写入数据
insert overwrite table track_info_page_stat partition(day='2013-07-21') select page, count(*) as cnt from track_info where day='2013-07-21' group by page;
```

到现在为止，我们统计的数据已经在 Hive 表 track_info_province_stat
而且这个表是一个分区表，后续统计报表的数据可以直接从这个表中查询
也可以将 hive 表的数据导出到 RDBMS（sqoop
总结一下所有的操作

1. ETL
2. 把 ETL 输出的数据加载到 track_info 分区表里
3. 各个维度统计结果的数据输出到各自维度的表里 （如 track_info_province_stat）
4. 将数据导出 (optional)
