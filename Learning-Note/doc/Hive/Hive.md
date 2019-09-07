# Hive 笔记

## Hive 概念

- 统一元数据管理:

  - Hive 数据是存放在 HDFS
  - 元数据信息(记录数据的数据)是存放在 MySQL 中
  - SQL on Hadoop： Hive、Spark SQL、impala....

- Hive 体系架构

  - client: shell、thrift/jdbc(server/jdbc)、WebUI(HUE/Zeppelin)
  - metastore：==> MySQL  
    database：name、location、owner....  
    table：name、location、owner、column name/type ....

- Hive 部署

  1. 下载（官网）
  2. 解压到~/app
  3. 添加 HIVE_HOME 到系统环境变量
  4. 修改配置  
     hive-env.sh  
     hive-site.xml
  5. 拷贝 MySQL 驱动包 mysql-java-connector.jar 包到\$HIVE_HOME/lib 下
  6. 前提是要准备安装一个 MySQL 数据库，利用 yum install 安装一个 MySQL 数据库 https://www.cnblogs.com/julyme/p/5969626.html

  ```xml
  <!--hive-site.xml配置-->
  <?xml version="1.0"?>
  <?xml-stylesheet type="text/xsl" href="configuration.xsl"?>

  <configuration>
  <property>
    <name>javax.jdo.option.ConnectionURL</name>
    <value>jdbc:mysql://hadoop000:3306/hadoop_hive?createDatabaseIfNotExist=true</value>
  </property>

  <property>
    <name>javax.jdo.option.ConnectionDriverName</name>
    <value>com.mysql.jdbc.Driver</value>
  </property>

  <property>
    <name>javax.jdo.option.ConnectionUserName</name>
    <value>我是用户名</value>
  </property>

  <property>
    <name>javax.jdo.option.ConnectionPassword</name>
    <value>我是密码</value>
  </property>
  </configuration>
  ```

## Hive 的 sql 语言

### DDL

Hive Data Definition Language

```shell
create、delete、alter...
```

Hive 数据抽象/结构:

```ASCII
+-- database  HDFS一个目录
|   +-- table HDFS一个目录
    |   +-- data  文件
    |   +-- partition 分区表  HDFS一个文件
        |   +-- data  文件
        |   +-- bucket  分桶  HDFS一个文件
```

Hive 具体 ddl 操作

### 创建数据库

```sql
CREATE (DATABASE|SCHEMA) [IF NOT EXISTS] database_name
  [COMMENT database_comment]
  [LOCATION hdfs_path]
  [WITH DBPROPERTIES (property_name=property_value, ...)];

CREATE DATABASE IF NOT EXISTS hive;

CREATE DATABASE IF NOT EXISTS hive2 LOCATION '/test/location';

CREATE DATABASE IF NOT EXISTS hive3
WITH DBPROPERTIES('creator'='pk');
```

/user/hive/warehouse 是 Hive 默认的存储在 HDFS 上的路径

### 创建表

```sql
CREATE TABLE emp(
empno int,
ename string,
job string,
mgr int,
hiredate string,
sal double,
comm double,
deptno int
) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';
```

### 读取本地数据

```sql
LOAD DATA [LOCAL] INPATH 'filepath' [OVERWRITE] INTO TABLE tablename [PARTITION (partcol1=val1, partcol2=val2 ...)]

LOAD DATA LOCAL INPATH '/home/hadoop/data/emp.txt' OVERWRITE INTO TABLE emp;
```

LOCAL：本地系统，如果没有 local 那么就是指的 HDFS 的路径  
OVERWRITE：是否数据覆盖，如果没有那么就是数据追加

```sql
LOAD DATA LOCAL INPATH '/home/hadoop/data/emp.txt' OVERWRITE INTO TABLE emp;

LOAD DATA INPATH 'hdfs://hadoop000:8020/data/emp.txt' INTO TABLE emp;

INSERT OVERWRITE LOCAL DIRECTORY '/tmp/hive/'
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
select empno,ename,sal,deptno from emp;
```

### 基本统计

```sql
select * from emp where sal between 800 and 1500 (limit(5));

select * from emp where ename in ('SMITH','KING');

select * from emp where sal is (not) null;
```

### 聚合操作

max/min/sum/avg

```sql
select count(1) from emp where deptno = 10;

select max(sal),min(sal),sum(sal),avg(sal) from emp;
```

### 分组函数

group by

出现在 select 中的字段，如果没有出现在聚合函数里，那么一定要实现在 group by 里

```sql
求每个部门的平均工资
select deptno, avg(sal) from emp group by deptno;

求每个部门、工作岗位的平均工资
select deptno,job,avg(sal) from emp group by deptno, job;

求每个部门的平均工资大于2000的部门
select deptno, avg(sal) avg_sal from emp group by deptno where avg_sal > 2000; 错误！！！

group by 不能同时和 where 使用
应该使用having替代where

select deptno, avg(sal) abg_sal from emp group by deptno having avg_sal > 2000;
```

### 多表操作

join

两个表:

1. emp
2. dept

```sql
创建表
CREATE TABLE dept(
deptno int,
dname string,
loc string
) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';
加载数据
LOAD DATA LOCAL INPATH '/home/hadoop/data/dept.txt' OVERWRITE INTO TABLE dept;

select
e.empno,e.ename,e.sal,e.deptno,d.name
from emp e join dept d
on e.deptno=d.deptno
```

#### 关于 stage-0、stage-3、stage-4

由于join是一个复杂操作，所以需要分步骤进行查询

```sql
explain EXTENDED
select
e.empno,e.ename,e.sal,e.deptno,d.dname
from emp e join dept d
on e.deptno=d.deptno;
```

- stage-4 is root stage
- stage-3 depends on stage-4
- stage-0 depends on stage-3
