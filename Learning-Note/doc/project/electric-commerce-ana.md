# 电商用户行为日志

- 每一次访问的行为(访问、搜索)产生的日志
- 历史行为数据 <== 历史订单
- ==> 推荐
- ==> 订单的转化量/转化率

## 原始日志字段说明

- 第二个字段：url
- 第十四个字段：ip
- 第十八字段：time

- ==> 字段的解析

  - ip => 城市：国家、省份、城市
  - url => 页面 ID

- referer  
  用户的来历 用户的需求