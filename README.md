# oa-wx-api
oa系统的小程序开发后端接口

### 架构

```
springboot+ssm+mp+shiro+rabbitMQ
```

### 重点模块(更新中)

```
1.token机制+shiro鉴权 Done.
2.人脸识别建模+签到 Done.
```

### 需要整理的问题

#### 1.项目的路径

现上传的文件保存在windows文件系统下的**绝对路径**,如何保存在**项目的相对路径**需要进一步解决

### 2.时间类

Java类中的`DateTime`/`LocalDateTime`/`Date` 

sql数据库中的时间类

java与sql时间映射问题需要理清和解决

