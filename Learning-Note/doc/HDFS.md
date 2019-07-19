# HDFS

## The Hadoop Distributed File System (HDFS)

* 分布式
* commodity hardware
* fault-tolerant 容错
* high throughput
* low-cast hardware
* large data sets

## HDFS的架构

1. NameNode(master) 和 DataNodes(slave)
2. master/slave的架构
3. NN(NameNode):
    * the file system namespace
      * /home/hadoop/software
      * /home/hadoop/app
    * regulate access to files by clients
4. DN(DataNodes): storeage
5. HDFS exposes a file system namespace and allows user data
6. a file is split into one or more blocks
7. blocks are stored in a set of DataNodes 容错！
8. NameNode executes file system namespace operations: CRUD
9. determines the mapping of blocks to DataNodes
10. DataNode & block 通常情况下：1个node部署一个结点
