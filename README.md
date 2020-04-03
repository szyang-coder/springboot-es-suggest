# springboot-elasticsearch

#### 一、项目介绍
技术框架使用 Springboot2.1.1 + elasticsearch6.5.3, MySQL数据持久化, Redis缓存检索命中的结果
实现功能：
1. 给定一个关键词搜索API接口;
2. 实现一个Web界面可操作;
3. 能通过https及域名进行访问查询功能;

#### 二、软件架构
1. Springboot2.1.1
2. elasticsearch6.5.3
3. spring-boot-starter-data-elasticsearch
4. mysql5.6
5. redis2.6

#### 三、配置教程
1. elasticsearch6.5.3  <br/>
    - 下载 <br/>
    https://www.elastic.co/cn/downloads/elasticsearch <br/>
    - 配置<br/>
          解压后，打开 ```config/elasticsearch.yml```，对其中两项配置进行修改 <br/>
        - ```cluster.name```集群名称，随便填写，或者使用默认的“my-application”，注意，后面Java链接elasticsearch时，需要该配置。
        - ```network.host```如果此不配置此项，其他机器无法链接当前elasticsearch。配置为：（0.0.0.0代表任何IP都可访问）
        - 启动 <br/>
        Mac/Linux：运行 ```bin/elasticsearch```<br/>
        Windows：运行 ```bin\elasticsearch.bat```

2. essuggest <br/>
       标准springboot项目，导入IDE运行即可。
    
#### 四、essuggest配置说明
1. 修改```application.properties->spring.data.elasticsearch.cluster-nodes```  elasticsearch地址
2. 修改```application.properties->spring.data.elasticsearch.cluster-name``` 集群名称，和上面配置的相对应
3. 各种操作方式，请参考：``` com/demo/essuggest/EsSuggestApplicationTests.java ```测试类
4. 端口，文根等其他配置请可自行配置

#### 五、补充
1. 数据流：mysql-->索引-->检索（也可从redis缓存中获取），查询时是直接从索引里通过前缀匹配查询。
2. 数据可通过db.sql导入数据库；
3. 运行com/demo/essuggest/EsSuggestApplicationTests.java的save()方法创建索引。