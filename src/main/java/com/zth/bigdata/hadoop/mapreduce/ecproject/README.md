# 需求

## v1

1. 统计页面浏览量
  - count
  - select count(1) from xxx;
  - 一行的记录做成一个固定的KEY, value赋值为1
2. 统计各个省份的浏览量
  - 按照省份进行分组
  - select province count(1) from xxx group by province;
  - 地市信息可以通过Ip解析得到 <== ip如何转换成城市信息
  - ip解析: 收费
3. 统计页面的访问量：把符合规则的pageId获取到，然后进行统计即可。

## v2

1. 使用ETL对数据进行一步处理  
ip/time/url/page_id/country/province/city
