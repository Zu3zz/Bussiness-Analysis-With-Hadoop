# 需求
1. 统计页面浏览量
  - count
  - select count(1) from xxx;
  - 一行的记录做成一个固定的KEY, value赋值为1
2. 统计各个省份的浏览量
  - 按照省份进行分组
  - select province count(1) from xxx group by province;
  - 地市信息可以通过Ip解析得到 <== ip如何转换成城市信息
  - ip解析: 收费
  