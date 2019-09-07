# HDFS概述

  1. 分布式
  2. commodity hardware
  3. fault-tolerant 容错
  4. high throughput
  5. large data sets

## HDFS是一个分布式的文件系统

* 文件系统：Linux、Windows、Mac....
  * 目录结构: C    /
  * 存放的是文件或者文件夹
  * 对外提供服务：创建、修改、删除、查看、移动等等
  * 普通文件系统 vs  分布式文件系统
    * 单机
    * 分布式文件系统能够横跨N个机器

## HDFS前提和设计目标

* Hardware Failure  硬件错误
  * 每个机器只存储文件的部分数据，blocksize=128M
  * block存放在不同的机器上的，由于容错，HDFS默认采用3副本机制
* Streaming Data Access  流式数据访问
  * The emphasis is on high throughput of data access
  * rather than low latency of data access.
* Large Data Sets  大规模数据集
* Moving Computation is Cheaper than Moving Data	移动计算比移动数据更划算

## HDFS的架构

1. NameNode(master) and DataNodes(slave)
2. master/slave的架构
3. NN:
  * the file system namespace
	  * /home/hadoop/software
	  * /home/hadoop/app
	* regulates access to files by clients

4. DN：storage

5. HDFS exposes a file system namespace and allows user data to be stored in files.
6. a file is split into one or more blocks
	* blocksize: 128M
	* 150M拆成2个block
7. blocks are stored in a set of DataNodes
	* 为什么？ 容错！！！
8. NameNode executes file system namespace operations：CRUD
9. determines the mapping of blocks to DataNodes
	* a.txt  150M   blocksize=128M
	* a.txt 拆分成2个block   一个是block1：128M  另一个是block2：22M
		block1存放在哪个DN？block2存放在哪个DN？

	* a.txt
		* block1：128M, 192.168.199.1
		* block2：22M,  192.168.199.2
		* get a.txt
		* 这个过程对于用户来说是不感知的
10. 通常情况下：1个Node部署一个组件

## 课程环境介绍：

### 本课程录制的系统是Mac，所以我采用的linux客户端是mac自带的shell

* 如果你们是win：xshell、crt
* 服务器/linux地址：192.168.199.233
* 连接到linux环境
	* 登陆：ssh hadoop@192.168.199.233
	* 登陆成功以后：[hadoop@hadoop000 ~]$ 
	* linux机器：用户名hadoop、密码123456、hostname是* * hadoop000
	* 创建课程中所需要的目录（合适的文件存放在合适的目录）
		1. [hadoop@hadoop000 ~]$ mkdir software  存放课程所使用的软件安装包
		2. [hadoop@hadoop000 ~]$ mkdir app       存放课程所有软件的安装目录
		3. [hadoop@hadoop000 ~]$ mkdir data      存放课程中使用的数据
		4. [hadoop@hadoop000 ~]$ mkdir lib       存放课程中开发过的作业jar存放的目录
		5. [hadoop@hadoop000 ~]$ mkdir shell     存放课程中相关的脚本
		6. [hadoop@hadoop000 ~]$ mkdir maven_resp 存放课程中使用到的maven依赖包存放的目录
* 学员问：root密码
	1. 切换hadoop到root用户：[hadoop@hadoop000 ~]$ sudo -i
	2. 切换root到hadoop用户：[root@hadoop000 ~]# su hadoop
	3. 我OOTB环境中创建的hadoop用户是有sudo权限：sudo vi /etc/hosts
* Linux版本：
	* 以前的课程是centos6.4，本次课程升级成centos7

### Hadoop环境搭建

* 使用的Hadoop相关版本：CDH
* CDH相关软件包下载地址：http://archive.cloudera.com/cdh5/cdh/5/
* Hadoop使用版本：hadoop-2.6.0-cdh5.15.1
* Hadoop下载：wget http://archive.cloudera.com/cdh5/cdh/5/hadoop-2.6.0-cdh5.15.1.tar.gz
* Hive使用版本：hive-1.1.0-cdh5.15.1

### Hadoop/Hive/Spark相关框架的学习：

* 使用单机版足够
* 如果使用集群学习会导致：从入门到放弃

* 使用Linux/Mac学习
* 一定不要使用Windows搭建Hadoop环境
* 所以Linux基础是要会的

### Hadoop安装前置要求

* Java  1.8+
* ssh

### 安装Java

* 拷贝本地软件包到服务器：scp jdk-8u91-linux-x64.tar.gz hadoop@192.168.199.233:~/software/
* 解压jdk到~/app/：tar -zvxf jdk-8u91-linux-x64.tar.gz -C ~/app/
* 把jdk配置系统环境变量中： ~/.bash_profile
	- export JAVA_HOME=/home/hadoop/app/jdk1.8.0_91
	- export PATH=$JAVA_HOME/bin:$PATH
* 使得配置修改生效：source .bash_profile
* 验证：java -version

### 安装ssh无密码登陆

* ls
* ls -a
* ls -la  并没有发现一个.ssh的文件夹

* ssh-keygen -t rsa  一路回车
* cd ~/.ssh
* [hadoop@hadoop000 .ssh]$ ll
* 总用量 12

  ```shell
  -rw------- 1 hadoop hadoop 1679 10月 15 02:54 id_rsa  私钥
  -rw-r--r-- 1 hadoop hadoop  398 10月 15 02:54 id_rsa.pub 公钥
  -rw-r--r-- 1 hadoop hadoop  358 10月 15 02:54 known_hosts
  cat id_rsa.pub >> authorized_keys
  chmod 600 authorized_keys
  ```

### Hadoop(HDFS)安装

* 下载
	* 解压：~/app
	* 添加HADOOP_HOME/bin到系统环境变量
	* 修改Hadoop配置文件
		* hadoop-env.sh
			* export JAVA_HOME=/home/hadoop/app/jdk1.8.0_91
		* core-site.xml

    ```xml
      <property>
          <name>fs.defaultFS</name>
          <value>hdfs://hadoop000:8020</value>
      </property>

      hdfs-site.xml
      <property>
          <name>dfs.replication</name>
          <value>1</value>
      </property>

      <property>
          <name>hadoop.tmp.dir</name>
          <value>/home/hadoop/app/tmp</value>
      </property>
    ```

    * slaves
			* hadoop000
	* 启动HDFS：
		* 第一次执行的时候一定要格式化文件系统，不要重复执行: hdfs namenode -format
		* 启动集群：$HADOOP_HOME/sbin/start-dfs.sh
		* 验证:
			1. [hadoop@hadoop000 sbin]$ jps
			2. 60002 DataNode
			3. 60171 SecondaryNameNode
			4. 59870 NameNode
			5. http://192.168.199.233:50070
			6. 如果发现jps ok，但是浏览器不OK？ 十有八九是防火墙问题
			7. 查看防火墙状态：sudo firewall-cmd --state
			8. 关闭防火墙: sudo systemctl stop firewalld.service
			9. 进制防火墙开机启动：

### hadoop软件包常见目录说明

* bin：hadoop客户端名单
* etc/hadoop：hadoop相关的配置文件存放目录
* sbin：启动hadoop相关进程的脚本
* share：常用例子

### 注意：

* start/stop-dfs.sh与hadoop-daemons.sh的关系
* start-dfs.sh

  ```shell
  hadoop-daemons.sh start namenode
  hadoop-daemons.sh start datanode
  hadoop-daemons.sh start secondarynamenode
  ```

* stop-dfs.sh =

* hadoop常用命令：
  1. hadoop fs -ls /
  2. hadoop fs -put
  3. hadoop fs -copyFromLocal
  4. hadoop fs -moveFromLocal
  5. hadoop fs -cat
  6. hadoop fs -text
  7. hadoop fs -get
  8. hadoop fs -mkdir
  9. hadoop fs -mv  移动/改名
  10. hadoop fs -getmerge
  11. hadoop fs -rm
  12. hadoop fs -rmdir
  13. hadoop fs -rm -r

### HDFS存储扩展：

* put: 1file ==> 1...n block ==> 存放在不同的节点上的
* get: 去nn上查找这个file对应的元数据信息
* 了解底层的存储机制这才是我们真正要学习的东西，掌握API那是毛毛雨

### 使用HDFS API的方式来操作HDFS文件系统

* IDEA/Eclipse
* Java
  * 使用Maven来管理项目
  * 拷贝jar包
  * 我的所有课程都是使用maven来进行管理的

```shell
Caused by: org.apache.hadoop.ipc.RemoteException
(org.apache.hadoop.security.AccessControlException):
Permission denied: user=rocky, access=WRITE,
inode="/":hadoop:supergroup:drwxr-xr-x
```

### HDFS操作：shell + Java API

* 综合性的HDFS实战：使用HDFS Java API才完成HDFS文件系统上的文件的词频统计
* 词频统计：wordcount
  * /path/1.txt
  * hello world hello
  * /path/2.txt
  * hello world hello
    * ==> (hello,4) (world,2)
* 将统计完的结果输出到HDFS上去。

### 假设：有的小伙伴了解过mr、spark等等，觉得这个操作很简单

### 本实战的要求：只允许使用HDFS API进行操作

### 目的

1. 掌握HDFS API的操作
2. 通过这个案例，让你们对于后续要学习的mr有一个比较好的认识

### 硬编码 ： 非常忌讳的

### ==> 可配置

### 可插拔的开发/管理方式  plugin

### 副本摆放策略

* 1-本rack的一个节点上
* 2-另外一个rack的节点上
* 3-与2相同的rack的另外一个节点上

* 1-本rack的一个节点上
* 2-本rack的另外一个节点上
* 3-不同rack的一个节点上

### HDFS的元数据管理

* 元数据：HDFS的目录结构以及每个文件的BLOCK信息(id，副本系数、block存放在哪个DN上)
* 存在什么地方：对应配置 ${hadoop.tmp.dir}/name/......
* 元数据存放在文件中：

```shell
/test1
/test1/a.txt
/test2
/test2/1.txt
/test2/2.txt
```
