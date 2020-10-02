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

### 使用Netflix Hystrix的客户端弹性模式

客户端弹性软件模式的重点是：在远程服务发生错误或表现不佳时保护远程资源（另一个微服务调用或者数据库资源）的客户端免于崩溃。这些模式的目标是让客户端“快速失败”，而不消耗诸如数据库连接和线程池之类的宝贵资源。共有4种客户端弹性模式，分别是：客户端负载均衡（`client load balance`）模式、断路器（`circuit breaker`）模式、后备（`fallback`）模式、舱壁（`bulkhead`）模式。

客户端负载均衡主要是通过`netflix eureka`查找服务的所有实例，然后缓存服务实例的物理位置。每当有服务消费者需要调用服务实例时，客户端负载均衡器将从它维护的服务位置池返回一个位置。默认使用`Netflix Ribbon`进行客户端服务调用，当客户端均衡器检测到问题，它可以从可用服务位置池中移除该服务实例，并防止将来的服务调用访问该服务实例。

`spring cloud`使用`hystrix`实现断路器模式，通过在方法上标注`@HystrixCommand`注解来将`Java`类方法标记为由`Hystrix`断路器进行管理。其将动态生成一个代理，该代理将包装该方法，并通过专门用于处理远程调用的线程池来管理对该方法的所有调用。

```java
@HystrixCommand(commandProperties = {
  @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),     // 设置熔断是否开启
  @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),   // 表示在滚动期断路器的最小请求数
  @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),  // 设置熔断器时间窗的时间
  @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),    // 表示打开熔断器的百分比(当调用失败达到60%以上时候)
  @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
}, fallbackMethod = "fallback")
@GetMapping(value = RequestConstInfo.HYSTRIX_GET_PRODUCT_LIST)
public String getProductInfoList(@RequestParam("number") Integer number) {
  if(number % 2 == 0) {
    return "success";
  }
  RestTemplate restTemplate = new RestTemplate();
  return restTemplate.postForObject("http://localhost:8083/product/listForOrder",
                                    Arrays.asList("157875196366160022"),
                                    String.class);
}

private String fallback(Integer number) {
        return "太拥挤了，请稍后重试~~";
}
```

`hystix`默认的断路器时间为`1000ms`，熔断超时时间可通过`@HystixCommand`注解的`execution.isolation.thread.timeoutInMilliseconds`进行配置，当服务调用超过配置的时间时，会自动进行熔断。在后备处理模式中，通过给`@HystrixCommand`的`fallbackMethod`属性指定默认方法用于在发生熔断时进行调用。

舱壁模式是建立在造船的概念基础上的，采用舱壁设计，一艘船被划分为完全隔离和防水的隔间，这称为舱壁。即使船的船体被击穿，由于船被划分为水密舱（舱壁），舱壁会将水限制被击穿的船的区域内，防止整艘船灌满水并沉没。`hystrix`使用线程池进行资源的隔离，每个远程资源都是隔离的，并分配给线程池。如果一个服务响应缓慢，那么这种服务调用的线程池就会饱和并停止处理请求，而对其它服务的调用则不会变得饱和，因为它们被分配给了其他线程池。

```java
@HystrixCommand(threadPoolKey = "product-service-threadpool",
                threadPoolProperties = {
                  @HystrixProperty(name = "coreSize", value = "30"),
                  @HystrixProperty(name = "maxQueueSize", value = "10")
                })
@GetMapping(value = RequestConstInfo.GET_PRODUCT_LIST)
public String getProductList() {
  List<ProductInfoOutput> response = productClient.listForOrder(Arrays.asList("157875227953464068"));
  log.info("response => {}", response);
  return "ok";
}
```

在`@HystrixCommand`注解中引入了一个新属性`threadPoolKey`，其表明我们想要建立一个新的线程池。要定制线程池配置，应使用其`threadPoolProperties`属性，此属性使用`HystrixProperty`对象的数组控制线程池的行为，使用`coreSize`属性可以设置线程池的大小、用`maxQueueSize`设置请求队列的大小，一旦请求数超过队列大小，对线程池的任何其他请求都将失败，直到队列中有空间。

### 使用Spring Cloud Sleuth和Zipkin进行分布式跟踪

微服务架构是一种强大的设计范型，可以将复杂的单体软件系统分解为更小、更易于管理的部分。这些可管理的部分可以独立构建和部署，其提供的灵活性提升了服务管理上复杂度。服务的分布式特性意味着必须在多个服务、物理机器和不同的数据存储之间跟踪一个或多个事务。使用关联`ID`将跨多个服务的事务链接在一起、将来自多个服务的日志聚合为一个可搜索的源、可视化跨多个服务的用户事务流并理解事务每个部分的性能特征。

<img src="document/images/springcloud-zipkin.png" alt="springcloud-zipkin" style="zoom:50%;" />

可使用`sleuth`和`zipkin`进行分布式链路跟踪，`zipkin`是一种凯源数据可视化工具，可以显示跨多个服务的事务流。`zipkin`允许开发人员将事务分解到它的组件块中，并可视化地识别可能存在性能热点的位置。