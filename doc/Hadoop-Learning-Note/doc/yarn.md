# YARN产生背景

+ MapReduce1.x ==> MapReduce2.x
  + master/slave : JobTracker/TaskTracker
  + JobTracker：单点、压力大
  + 仅仅只能够支持mapreduce作业

+ 资源利用率
  + 所有的计算框架运行在一个集群中，共享一个集群的资源，按需分配！

```txt
master: resource management：ResourceManager (RM)
job scheduling/monitoring：per-application ApplicationMaster (AM)
slave: NodeManager (NM)
```

## YARN架构

+ Client、ResourceManager、NodeManager、ApplicationMaster
+ master/slave: RM/NM

## Client: 向RM提交任务、杀死任务等

+ ApplicationMaster：
  + 每个应用程序对应一个AM
  + AM向RM申请资源用于在NM上启动对应的Task
  + 数据切分
  + 为每个task向RM申请资源（container）
  + NodeManager通信
  + 任务的监控

NodeManager： 多个
	干活
	向RM发送心跳信息、任务的执行情况
	接收来自RM的请求来启动任务
	处理来自AM的命令

ResourceManager:集群中同一时刻对外提供服务的只有1个，负责资源相关
	处理来自客户端的请求：提交、杀死
	启动/监控AM
	监控NM
	资源相关

container：任务的运行抽象
	memory、cpu....
	task是运行在container里面的
	可以运行am、也可以运行map/reduce task




提交自己开发的MR作业到YARN上运行的步骤：
1）mvn clean package -DskipTests  
	windows/Mac/Linux ==> Maven 
2）把编译出来的jar包(项目根目录/target/...jar)以及测试数据上传到服务器
	scp xxxx hadoop@hostname:directory
3) 把数据上传到HDFS
	hadoop fs -put xxx hdfspath
4) 执行作业
	hadoop jar xxx.jar 完整的类名(包名+类名) args.....	
5) 到YARN UI(8088) 上去观察作业的运行情况
6）到输出目录去查看对应的输出结果











