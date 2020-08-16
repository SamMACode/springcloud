[TOC]

![Spring-Logos-CLOUD-HOR-1200x350](https://raw.githubusercontent.com/SamMACode/springcloud/master/document/images/microservice-logo.png)

#  微服务系统设计

> “微服务就是一些协同工作的小而自治的服务，微服务很小，只专注做好一件事。一个微服务就是一个独立的实体，它可以独立的部署在`PAAS(Platform As A Service)`上，也可以作为一个操作系统进程存在。”

微服务具有很多不同的好处，其中很多好处也适用于任何一个分布式系统：**系统技术的异构性**，在一个由多个服务相互协作的系统中，可以在不同的服务中使用最适合该服务的技术。**提高系统的弹性**，弹性工程学的一个关键概念就是舱壁，如果系统中一个组件不可用了，但是并没有导致级联故障，那么系统的其它部分还可以正常运行。**系统易于扩展**，使用较小的多个服务，则可以只对需要扩展的服务进行扩展，这样就可以把那些不需要扩展的服务运行在更小的、性能稍差的硬件上。

## 服务整体设计图

服务之间的调用关系如下所示，服务结构图详细描述了从`Reactive Gateway`到具体服务及后端数据源`Datasource`之间的调用流程：`eureka-server`作为服务注册中心用于服务发现、`Reactive-gateway`作为微服务网关其对外提供统一访问入口，在网关层统一做request鉴权、请求分发、限流策略等。`config-server`为应用配置中心提供统一的配置管理功能，其从`github`指定仓库获取配置文件。`oauth-service`基于`spring cloud security`实现，其主要提供鉴权校验功能。

![image-20200809152824756](document/images/cloud-native-architecture.png)



服务列表及所依赖`spring cloud`、`spring boot`版本信息如下：

| ServiceName     | Spring Boot    | Spring Cloud  | ORM   | Messaing     | Service Type         |
| --------------- | -------------- | ------------- | ----- | ------------ | -------------------- |
| eureka-server   | 2.1.12.RELEASE | Greenwich.SR5 | R2DBC | N/A          | netflix eureka       |
| api-gateway     | 2.1.12.RELEASE | Greenwich.SR5 | R2DBC | N/A          | spring cloud gateway |
| config-server   | 2.1.12.RELEASE | Greenwich.SR5 | N/A   | N/A          | config center        |
| oauth-service   | 2.1.12.RELEASE | Greenwich.SR5 | R2DBC | N/A          | spring cloud oauth   |
| order-service   | 2.1.12.RELEASE | Greenwich.SR5 | R2DBC | apache kafka | domain               |
| product-service | 2.1.12.RELEASE | Greenwich.SR5 | R2DBC | apache kafka | domain               |
| user-service    | 2.1.12.RELEASE | Greenwich.SR5 | R2DBC | N/A          | domain               |

### Api Gateway对外提供的接口列表
-   **用户登陆接口**
    -   GET <http://{gateway-ip}:8086/user/login/buyer?openid={0}>
-   **商家登陆接口**
    -   GET <http://{gateway-ip}:8086/user/login/seller?openid={0}>
-   **获取产品列表接口**
    -   POST <http://{gateway-ip}:8086/product/product/list>
-   **根据订单获取产品接口**
    -   POST <http://{gateway-ip}:8086/product/product/list>
-   **根据产品id减库存接口**
    -   POST <http://{gateway-ip}:8086/product/product/decreaseStock>
-   **创建订单接口**
    -   POST <http://{gateway-ip}:8086/order/order/create>
-   **完成订单接口**
    -   GET <http://{gateway-ip}:8086/order/order/finish?orderId={0}>
-   **异步生成订单接口**
    -   POST <http://{gateway-ip}:8086/order/decreaseStockAsync>

### 通过kubernetes在本地部署应用

服务运行依赖与`MySQL`、`Kafka`和`Redis`、`RabbitMQ`组件，已在`docker`中启动相应实例，通过`docker ps`命令查看：

```bash
CONTAINER ID        IMAGE                    COMMAND                  CREATED             STATUS              PORTS                                                                             NAMES
d8c58b66f42c        wurstmeister/kafka       "start-kafka.sh"         4 weeks ago         Up 24 hours         0.0.0.0:9092->9092/tcp                                                            kafka
e1cd62ea621a        wurstmeister/zookeeper   "/bin/sh -c '/usr/sb…"   4 weeks ago         Up 24 hours         22/tcp, 2888/tcp, 3888/tcp, 0.0.0.0:2181->2181/tcp                                zookeeper
742e2bdf8ceb        rabbitmq                 "docker-entrypoint.s…"   4 weeks ago         Up 24 hours         4369/tcp, 0.0.0.0:5672->5672/tcp, 5671/tcp, 25672/tcp, 0.0.0.0:15672->15672/tcp   rabbitmq
e7e23c2459a5        redis                    "docker-entrypoint.s…"   4 weeks ago         Up 24 hours         0.0.0.0:6379->6379/tcp                                                            redis-server
e2602c9d96b4        mysql:5.6                "docker-entrypoint.s…"   7 weeks ago         Up 24 hours         0.0.0.0:3306->3306/tcp                                                            mysql
```

由于`kubectl`使用阿里云镜像中心作为镜像仓库，因此需要将要部署的镜像上传到阿里云镜像中心（公开镜像），kubectl根据`deployment`中镜像名称去阿里云仓库下载并在`kubernetes`部署：

```bash
% sudo docker images | grep aliyun
registry.cn-shanghai.aliyuncs.com/spotify-music/config-server     latest                a86a7d0bdb0c        21 hours ago        694MB
registry.cn-shanghai.aliyuncs.com/spotify-music/user-service      latest                ffb510167232        21 hours ago        711MB
registry.cn-shanghai.aliyuncs.com/spotify-music/product-service   latest                1584a7a9e04f        21 hours ago        715MB
registry.cn-shanghai.aliyuncs.com/spotify-music/api-gateway       latest                5c4fd84f7307        21 hours ago        695MB
registry.cn-shanghai.aliyuncs.com/spotify-music/order-service     latest                a22a0a1a54c3        21 hours ago        726MB
registry.cn-hangzhou.aliyuncs.com/spotify-music/eureka-server     latest                28a756e96fd5        22 hours ago        689MB
registry.cn-shanghai.aliyuncs.com/spotify-music/eureka-server     latest                28a756e96fd5        22 hours ago        689MB
```

编写应用部署的`yml`文件（以`eureka-server`为例），如下`eureka-server-k8s-deploy.yml`为`eureka`配置文件：应用部署的`namespace`为`svc`，应用类型为`Deployment`类型控制应用副本以及应用升级，同时`liveness`存活探针指向服务的`8762`端口（检测应用是否可用，否则`k8s`会启动新的实例）。

```yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: eureka-server
  namespace: svc
spec:
  selector:
    matchLabels:
      app: eureka-server
  replicas: 2
  template:
    metadata:
      labels:
        app: eureka-server
    spec:
      containers:
        - name: eureka-server
          livenessProbe:
            httpGet:
              path: /actuator/health
              port: 8762
            initialDelaySeconds: 10
            timeoutSeconds: 15
            periodSeconds: 60
            failureThreshold: 6
          image: registry.cn-shanghai.aliyuncs.com/spotify-music/eureka-server:latest
          ports:
            - containerPort: 8762
```

通过`deployment`中`spec.replicas`启动了多个应用实例，由于`Pod`可能随时启动或关闭、同时`Pod`也存在水平扩展多个实例，对其中某个固定`pod`发起请求并不合理。此时需要一个单一不变的接入点请求应用（引入`Service`资源），其使用`round`轮询或者随机方式转发请求，可确保服务一直处理可用状态。 `eureka-server-service.yml`服务配置如下：

```yml
apiVersion: v1
kind: Service
metadata:
  name: eureka-server
  namespace: svc
spec:
  ports:
    - port: 8762
      targetPort: 8762
  selector:
    app: eureka-server
```

与`spring cloud`服务发现进行整合的思考：各个微服务最终都会注册到`eureka`注册中心上，控制面板上显示微服务实例`host`:`port`地址。当存在使用`feign`调用其它服务时，此时在当前实例`pod`上会将要调用服务的`host`转换为对应`ip`地址（`pod`地址），然后进行服务请求。在`kube-system`命名空间下存在一个`pod`其为`kube-dns`，集群中其它的`pod`都被配置成使用其作为`dns`。`feign`服务调用也是一样，`kube-dns`会按`service.namespace`格式从`service`列表中进行匹配找相应`pod`实例（不存在`namespace`默认从`default`命名空间找相应`service`），然后将具体`pod`地址进行返回。

微服务`host`是通过`application.yml`文件中`spring.instance.hostname`参数进行配置的，配置内容应写为`k8s`的`service`.`namespace`格式，否则就会出现`host`无法解析的问题。

```yml
spring:
  application:
    name: eureka-server
  instance:
    hostname: eureka-server.svc
    metadata-map:
      instanceId: ${spring.application.name}:${spring.application.instance_id:${random.value}}    
```

在`minikube`上对`eureka-server`进行部署：`kubectl create -f eureka-server-k8s-deploy.yml`

创建`service`对外统一提供服务：`kubectl create -f eureka-server-service.yml`，当对配置文件存在修改时，使用`kubectl replace -f eureka-server-service.yml`对服务进行更新。

```shell
% kubectl get pods -n svc
NAME                             READY   STATUS    RESTARTS   AGE
api-gateway-6d7d9cc4d7-czjvd     1/1     Running   11         10h
config-server-8d7866488-6vmft    1/1     Running   9         11h
eureka-server-7fcfcb6c59-l2lsr   1/1     Running   5         11h
eureka-server-6sdf9cb081-23ndp   1/1     Running   4         11h
```

要打算从宿主机的`port`访问`eureka dashboard`可使用`kubectl port-forward eureka-server-6sdf9cb081-23ndp 8762:8762 --namespace svc `命令，此时即可通过宿主机的`8762`端口进行服务访问。

