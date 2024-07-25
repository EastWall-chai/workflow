# app 安装

目录包含使用helm安装当前app所需文件以及本安装文档



### 说明

默认已配置好基础配置，values.yaml已参数化各安装参数

`#`命名空间     
namespace:      
  tiger     
`#`服务应用名称     
appname:     
  oauth    
imagename:   
  oauth-center:1.0.0-SNAPSHOT   
`#`阿里云相关配置   
`#`       imagePullSecrets   用于拉取阿里云镜像口令   
aliyun:   
  containerSrv:   
​    region:   
​      registry.cn-zhangjiakou.aliyuncs.com   
​    namespace:   
​      sinocarbon   
​    imagePullSecrets:   
​      tiger   

###推荐操作说明

- 进入helm目录

- 执行如下命令

`helm template app --values app/values.yaml > app.yaml`

当前目录下生成app.yaml部署文件

- 执行如下命令安装服务

（1）`kubectl apply -f xxxx-pvc.yaml`

（2）`kubectl apply -f app.yaml`