![kubernetes](https://raw.githubusercontent.com/SamMACode/springcloud/master/kubernetes/images/kubernetes_logo.png)

##  Build distributed services with kubernetes

>  **Kubernetes** (commonly  stylized as k8s) is an open-source container-orchestration system for  automating application deployment, scaling, and management.  It aims to provide a "platform for automating deployment, scaling, and operations of application  containers across clusters of hosts". 

####  一、在`elementory OS`服务器搭建kubernetes环境

`elementary OS`是基于`ubuntu`精心打磨美化的桌面 `linux` 发行版的一款软件，号称 “最美的 `linux`”， 最早是 `ubuntu` 的一个美化主题项目，现在成了独立的发行版。"快速、开源、注重隐私的 `windows` /` macOS` 替代品"。

1）在`elementary OS`系统上安装`docker`环境，具体可以参考` https://docs.docker.com/engine/installation/linux/docker-ce/ubuntu/`：

```shell
# 1.更新ubuntu的apt源索引
sam@elementoryos:~$ sudo apt-get update
# 2.安装以下包以使apt可以通过HTTPS使用存储库repository
sam@elementoryos:~$ sudo apt-get install apt-transport-https ca-certificates curl software-properties-common
# 3.添加Docker官方GPG key
sam@elementoryos:~$ curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
# 4.设置Docker稳定版仓库
sam@elementoryos:~$ sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
# 5.再更新下apt源索引，然后通过docker version显示器版本信息
sam@elementoryos:~$ apt-get update
sam@elementoryos:~$ sudo docker version
Client:
 Version:           18.09.7
Server:
 Engine:
  Version:          18.09.7   
# 6.从镜像中心拉取hello-world镜像并进行运行
sam@elementoryos:~$ sudo docker run hello-world
Hello from Docker!
This message shows that your installation appears to be working correctly.
```

管理`docker`服务常用应用脚本：

` sudo service docker start `  启动`docker`服务、` sudo service docker stop ` 停止`docker`服务、` sudo service docker restart `重启docker服务.



2）使用`minikube`在本机搭建`kubernetes`集群，简单体验`k8s`： 

为了方便开发者开发和体验`kubernetes`，社区提供了可以在本地部署的`minikube`。由于国内网络的限制导致，导致在本地安装`minikube`时相关的依赖是无法下载。从`minikube`最新的`1.5`版本之后，已经提供了配置化的方式，可以直接从阿里云的镜像地址来获取所需要的`docker`镜像和配置。

在`elementary OS`上安装`kubectl`的稳定版本：

```shell
sam@elementoryos:~$ sudo curl -LO https://storage.googleapis.com/kubernetes-release/release/v1.16.0/bin/linux/amd64/kubectl && chmod +x ./kubectl && sudo mv ./kubectl /usr/local/bin/kubectl
```

在安装完成后使用`kubectl version`进行验证，由于`minikube`服务未启动最后的报错可以忽略:

```shell
sam@elementoryos:~$ sudo kubectl version
Client Version: version.Info{Major:"1", Minor:"16", GitVersion:"v1.16.0", GitCommit:"2bd9643cee5b3b3a5ecbd3af49d09018f0773c77", GitTreeState:"clean", BuildDate:"2019-09-18T14:36:53Z", GoVersion:"go1.12.9", Compiler:"gc", Platform:"linux/amd64"}
The connection to the server 192.168.170.130:8443 was refused - did you specify the right host or port?
```

通过`curl`命令从`github`上下载`minikube`的`1.5.0`版本：

```shell
sam@elementoryos:~$ curl -Lo minikube https://github.com/kubernetes/minikube/releases/download/v1.5.0/minikube-linux-amd64 && chmod +x minikube && sudo mv minikube /usr/local/bin/
```

启动`minikube`服务，为了访问海外资源阿里云提供了一系列基础措施可以通过参数进行配置，`--image-mirror-country cn`默认会从`registry.cn-hangzhou.aliyuncs.com/google_containers`下载`kubernetes`依赖的相关资源。首次启动会在本地下载` localkube `、`kubeadm`等工具。

```shell
sam@elementoryos:~$ sudo minikube start --vm-driver=none --image-mirror-country cn --memory=1024mb --disk-size=8192mb --registry-mirror=https://registry.docker-cn.com --image-repository='registry.cn-hangzhou.aliyuncs.com/google_containers' --bootstrapper=kubeadm --extra-config=apiserver.authorization-mode=RBAC
😄  minikube v1.5.0 on Debian buster/sid
✅  Using image repository registry.cn-hangzhou.aliyuncs.com/google_containers
🤹  Running on localhost (CPUs=2, Memory=3653MB, Disk=40059MB) ...
ℹ️   OS release is elementary OS 5.0 Juno
🐳  Preparing Kubernetes v1.16.2 on Docker 18.09.7 ...
🏄  Done! kubectl is now configured to use "minikube"
```

在`minikube`安装完成后，在本地`minikube dashboard --url`控制页面无法展示，目前暂时未解决。

```shell
sam@elementoryos:~$ sudo kubectl create clusterrolebinding add-on-cluster-admin --clusterrole=cluster-admin --serviceaccount=kube-system:default
```

使用`sudo minikube dashboard --url`自动生成`minikube`的管理页面：

```
sam@elementoryos:~$ sudo minikube dashboard -url
```

`minikube`本地环境搭建可参考这几篇文章：

使用`minikube`在本地搭建集群：http://qii404.me/2018/01/06/minukube.html

阿里云的`minikube`本地实验环境：https://yq.aliyun.com/articles/221687

关于`kubernetes`解决`dashboard`：https://blog.8hfq.com/2019/03/01/kubernetes-dashboard.html

#### 二、运行于kubernetes中的容器

`kubernetes`中的`pod`组件：`pod`是一组并置的容器，代表了`kubernetes`中基本构建模块。在实际应用中我们并不会单独部署容器，更多的是针对一组`pod`容器进行部署和操作。当一个`pod`包含多个容器时，这些容器总是会运行于同一个工作节点上——一个`pod`绝不会跨越多个工作节点。

对于`docker`和`kubernetes`期望的工作方式是将每个进程运行于自己的容器内，由于不能将多个进程聚集在一个单独的容器中，我们需要另一种更高级的结构来将容器绑定在一起，并将它们作为一个单元进行管理，这就是`pod`背后的根本原理。对于容器彼此之间是完全隔离的，但此时我们期望的是隔离容器组，而不是单个容器，并让容器组内的容器共享一些资源。`kubernetes`通过配置`docker`来让一个`pod`内的所有容器共享相同的`linux`命名空间，而不是每个容器都有自己的一组命名空间。

由于一个`pod`中的容器运行于相同的`network`命名空间中，因此它们共享相同的`IP`地址和端口空间。这意味着在同一`pod`中的容器运行的多个进程需要注意不能绑定想同的端口号，否则会导致端口冲突。

1）在`kubernetes`上运行第一个应用`swagger-editor`并对外暴露`8081`端口：

```shell
sam@elementoryos:~$ sudo kubectl run swagger-editor --image=swaggerapi/swagger-editor:latest --port=8081 --generator=run/v1

sam@elementoryos:~$ sudo kubectl get pods
NAME                   READY   STATUS    RESTARTS   AGE
swagger-editor-xgqzm   1/1     Running   0          57s
```

在`kubectl run`命令中使用`--generator=run/v1`参数表示它让`kubernetes`创建一个`ReplicationController`而不是`Deployment`。通过`kubectl get pods`可以查看所有`pod`中运行的容器实例信息。每个`pod`都有自己的`ip`地址，但是这个地址是集群内部的，只有通过`LoadBalancer`类型服务公开它，才可以被外部访问，可以通过运行`kubectl get services`命令查看新创建的服务对象。

```shell
sam@elementoryos:~$ sudo kubectl expose rc swagger-editor --type=LoadBalancer --name swagger-editor-http
service/swagger-editor-http exposed

sam@elementoryos:~$ sudo kubectl get services
NAME                  TYPE           CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
kubernetes            ClusterIP      10.96.0.1        <none>        443/TCP          46m
swagger-editor-http   LoadBalancer   10.108.118.211   <pending>     8081:30507/TCP   3m24s
```

2）为了增加期望的副本数，需要改变`ReplicationController`期望的副本数，现已告诉`kubernetes`需要采取行动，对`pod`的数量采取操作来实现期望的状态。

```shell
sam@elementoryos:~$ sudo kubectl scale rc swagger-editor --replicas=3
replicationcontroller/swagger-editor scaled
sam@elementoryos:~$ sudo kubectl get pods
NAME                   READY   STATUS              RESTARTS   AGE
swagger-editor-fzppq   0/1     ContainerCreating   0          12s
swagger-editor-wqpg5   0/1     ContainerCreating   0          12s
swagger-editor-xgqzm   1/1     Running             0          16m
```

为了观察列出`pod`时显示`pod ip`和`pod`的节点，可以通过使用`-o wide`选项请求显示其他列。在列出`pod`时，该选项显示`pod`的`ip`和所运行的节点。由于`minikube`不支持`rc`，因而并不会展示外部`ip`地址。若想在不通过`service`的情况下与某个特定的`pod`进行通信（处于调试或其它原因）,`kubernetes`将允许我们配置端口转发到该`pod`，可以通过`kubectl port-forward`命令完成上述操作：

```shell
sam@elementoryos:~$ sudo kubectl get pods -o wide
NAME                   READY   STATUS    RESTARTS   AGE     IP           NODE       NOMINATED NODE   READINESS GATES
swagger-editor-fzppq   1/1     Running   0          5m28s   172.17.0.7   minikube   <none>           <none>
swagger-editor-wqpg5   1/1     Running   0          5m28s   172.17.0.5   minikube   <none>           <none>
swagger-editor-xgqzm   1/1     Running   0          21m     172.17.0.6   minikube   <none>           <none>

sam@elementoryos:~$ sudo kubectl port-forward swagger-editor-fzppq 8088:8081
Forwarding from 127.0.0.1:8088 -> 8081
Forwarding from [::1]:8088 -> 8081
```

标签是一种简单却功能强大的`kubernetes`特性，不仅可以组织`pod`也可以组织所有其他的`kubernetes`资源。详细来讲，可以通过标签选择器来筛选`pod`资源。在使用多个`namespace`的前提下，我们可以将包括大量组件的复杂系统拆分为更小的不同组，这些不同组也可以在多租户环境中分配资源。



#### 三、副本机制和其它控制器：部署托管的`pod`

`kubernetes`可以通过存活探针`(liveness probe)`检查容器是否还在运行，可以为`pod`中的每个容器单独指定存活探针。如果探测失败，`kubernetes`将定期执行探针并重新启动容器。`kubernetes`有三种探测容器的机制：通过`http get`对容器发送请求，若应用接收到请求，并且响应状态码不代表错误，则任务探测成功；`TCP`套接字探针尝试与容器指定端口建立`TCP`连接，若长连接正常建立则探测成功；`exec`探针在容器中执行任意命令，并检查命令的退出返回码。

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: kubia-liveness
spec:
  containers:
  - image: luksa/kubia-unhealthy
    name: kubia
    livenessProbe:
      httpGet:
        path: /
        port: 8080
      initialDelaySeconds: 15
```

`kubia-liveness-probe-initial-delay.yaml`文件中在`livenessProbe`中指定了通过`httpGet`探测的探针地址检测应用的状态，为了防止容器启动时通过探针地址检测应用状态，可以通过设置`initialDelaySeconds`指定应用启动间隔时间（像`spingboot`应用的`/health`端点就非常合适）。

了解`ReplicationController`组件：`ReplicationController`是一种`kubernetes`资源，可确保它的`pod`始终保持运行状态。如果`pod`因任何原因消失，则`ReplicationController`会注意到缺少了`pod`并创建替代`pod`。`ReplicationController`的工作是确保`pod`的数量始终与其标签选择器匹配，若不匹配则`rc`会根据需要，采取适当的操作来协调`pod`的数量。`label selector`用于确定`rc`作用域内有哪些`pod`、`replica count`指定应运行的`pod`数量、`pod template`用于创建新的`pod`副本。

```yaml
apiVersion: v1
kind: ReplicationController
metadata:
  name: kubia
spec:
  replicas: 3
  selector:
    app: kubia
  template:
    metadata:
      labels:
        app: kubia
    spec:
      containers:
      - name: kubia
        image: luksa/kubia
        ports:
        - containerPort: 8080
```

`kubia-rc.yaml`文件定义，在`yaml`中`selector`指定了符合标签的选择器`app: kubia`。若删除的`rc`创建的一个`pod`，则其会自动创建新的`pod`使得副本的数量达到`yaml`文件配置的数量。若要将`pod`移出`rc`作用域，可以通过更改`pod`的标签将其从`rc`的作用域中进行移除，`--overwrite`参数是必要的，否则`kubectl`将只是打印出警告，并不会更改标签。对于修改`rc`的`template`只会对之后新创建的`pod`有影响，而对之前已有的`pod`不会造成影响。若需要对`pod`进行水平扩展，可以通过修改`edit`调整`replicas:10`的属性，或者通过命令行`kubectl scale rc kubia --replication=10`进行调整。

```shell
sam@elementoryos:~$ sudo kubectl create -f kubia-rc.yaml
ReplicationController "kubia" created
sam@elementoryos:~$ sudo kubectl label pod kubia-demdck app=foo --overwrite
# 通过kubectl更改rc的template内容
sam@elementoryos:~$ sudo kubectl edit rc kubia
```

当要删除`rc`则可以通过`kubectl delete`进行操作，`rc`所管理的所有`pod`也会被删除。若需要保留`pod`的时候，则需要在命令行添加`--cascade=false`的配置，当删除`replicationController`后，其之前所管理的`pod`就独立。

`ReplicaSet`的引入：最初`ReplicationController`是用于复制和在异常时重新调度节点的唯一`kubernetes`组件，后来引入了`ReplicaSet`的类似资源。它是新一代的`rc`并且会将其完全替换掉。`ReplicaSet`的行为与`rc`完全相同，但`pod`选择器的表达能力更强。在`yaml`文件配置中其`apiVersion`内容为`apps/v1beta2`，其`kind`类型为`ReplicaSet`类型。

```shell
sam@elementoryos:~$ sudo kubectl delete rs kubia
```

引入`DaemonSet`组件：要在所有集群结点上运行一个`pod`，需要创建一个`DaemonSet`对象。`DaemonSet`确保创建足够的`pod`，并在自己的节点上部署每个`pod`。尽管`ReplicaSet(ReplicationController)`确保集群中存在期望数量的`pod`副本，但`DaemonSet`并没有期望的副本的概念。它不需要，因为它的工作是确保一个`pod`匹配它的选择器并在每个节点上运行。

在`DaemonSet`的`yml`配置文件中，其`apiVersion`内容为`apps/v1beta2`，`kind`类型为`DeamonSet`。在删除`DaemonSet`时候其所管理`pod`也会被一并删除。

```shell
sam@elementoryos:~$ sudo kubectl create -d ssd-monitor-deamonset.yaml
# view all DaemonSet components in kubernetes
sam@elementoryos:~$ sudo kubectl get ds
```

介绍`Kubernetes Job`资源：`kubernetes`通过`Job`资源提供对短任务的支持，在发生节点故障时，该节点上由`Job`管理的`pod`将按照`ReplicaSet`的`pod`的方式，重新安排到其他节点。如果进程本身异常退出（进程返回错误退出代码时），可以将`Job`配置为重新启动容器。

```yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: batch-job
spec:
  completions: 5
  parallelism: 2
  schedule: "0,15,30,45 * * * *"
  template:
    metadata:
      labels:
        app: batch-job
    spec:
      restartPolicy: OnFailure
      containers:
      - name: main
        image: luksa/batch-job
```

`Job`是`batch API`组`v1`版本的一部分，`yaml`定义了一个`Job`类型的资源，它将运行`luksa/batch-job`镜像，该镜像调用一个运行`120`秒的进程，然后退出。在`pod`的定义中，可以指定在容器中运行的进程结束时，`kubernetes`会做什么？这是通过`pod`配置的属性`restartPolicy`完成的，默认为`Always`配置 在`Job`中使用`OnFailure`的策略。可以在`yaml`文件中指定`parallelism: 2`来指定任务的并行度，通过创建`cronJob`资源在`yaml`中指定‘`schedule: 0,15,30,45 * * * *`定时任务表达式。`startingDeadlineSeconds: 15`指定`pod`最迟必须在预定时间后`15`秒开始执行。

```shell
sam@elementoryos:~/kubernetes$ sudo kubectl create -f kubernetes-job.yaml 
job.batch/batch-job created
sam@elementoryos:~/kubernetes$ sudo kubectl get jobs
NAME        COMPLETIONS   DURATION   AGE
batch-job   0/1           47s        47s
sam@elementoryos:~/kubernetes$ sudo kubectl get pods
NAME              READY   STATUS    RESTARTS   AGE
batch-job-nzbmv   1/1     Running   0          108s
sam@elementoryos:~/kubernetes$ sudo kubectl logs batch-job-nzbmv
Sun Nov 17 09:09:01 UTC 2019 Batch job starting
```



`service`服务：让客户端发现`pod`并与之通信

> `kubernetes`服务是一种为一组功能相同`pod`提供单一不变的接入点的资源，当服务存在时，它的`ip`地址和端口不变。客户端通过固定`ip`和`port`建立连接，这种连接会被路由到提供该服务的任意一个`pod`上。通过这种方式，客户端不需要知道每个`pod`的地址，这样这些`pod`就可以在集群中被随时创建或者移除。

可以使用`kubectl expose`命令创建服务，`rc`是`replicationcontroller`的缩写。由于`minikube`不支持`LoadBalance`类型的服务，因此服务的`external-ip`地址为`<none>`。

```shell
sam@elementoryos:~/kubernetes$ sudo kubectl expose rc kubia --type=LoadBalancer --name kubia-http
service "kubia-http" exposed
sam@elementoryos:~/kubernetes$ sudo kubectl get services
NAME         TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
kubernetes   ClusterIP   10.96.0.1        <none>        443/TCP          2d5h
kubia        ClusterIP   10.111.211.203   <none>        80/TCP,443/TCP   22h
sam@elementoryos:~/kubernetes$ sudo kubectl get pods
NAME          READY   STATUS    RESTARTS   AGE
kubia-9vds6   1/1     Running   0          23h
kubia-cpjvx   1/1     Running   0          23h
kubia-hs5vq   1/1     Running   0          23h
```

另一种是使用`yaml`描述文件`kubia-svc.yaml`来创建服务，使用`sudo kubectl create -f kubia-svc.yaml ` 。`service`也是通过`selector`筛选符合条件的`pod`，通过`ports`对端口进行转发。

```yaml
apiVersion: v1
kind: Service
metadata:
  name: kubia
spec:
  ports:
  - port: 80
    targetPort: 8080
  selector:
    app: kubia
```

从内部集群测试服务，可以通过`kubectl exec`命令在一个已经存在的`pod`中执行`curl`命令，其作用和`docker exec`命令比较类似。在`kubernetes`命令中`--`代表着`kubectl`命令项的结束，在`--`后的内容是在`pod`内部需要执行的命令。

```shell
sam@elementoryos:~/kubernetes$ sudo kubectl exec kubia-9vds6 -- curl -s http://10.111.211.203
You've hit kubia-cpjvx
```

通过环境变量发现服务：在`pod`开始的时候，`kubernetes`会初始化一系列的环境变量指向现在存在的服务。一旦选择了目标`pod`，通过在容器中运行`env`来列出所有的环境变量。在`ENV`列出的环境变量中，`KUBIA_SERVICE_HOST`和`KUBIA_SERVICE_PORT`分表代表了`kubia`服务的`ip`地址和端口号。

```shell
sam@elementoryos:~/kubernetes$ sudo kubectl exec kubia-9vds6 env
PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
HOSTNAME=kubia-9vds6
KUBERNETES_PORT_443_TCP_PORT=443
KUBERNETES_PORT_443_TCP_ADDR=10.96.0.1
KUBERNETES_SERVICE_HOST=10.96.0.1
KUBERNETES_SERVICE_PORT=443
KUBERNETES_SERVICE_PORT_HTTPS=443
KUBERNETES_PORT=tcp://10.96.0.1:443
KUBERNETES_PORT_443_TCP=tcp://10.96.0.1:443
KUBERNETES_PORT_443_TCP_PROTO=tcp
NPM_CONFIG_LOGLEVEL=info
NODE_VERSION=7.9.0
YARN_VERSION=0.22.0
HOME=/root
```

通过`dns`发现服务：在`kube-system`命名空间下列出的所有`pod`信息，其中一个为`coredns-755587fdc8`。每个服务从内部`dns`服务器中获得一个`dns`条目，客户端的`pod`在知道服务名称的情况下可以通过全限定域名`(FQDN)`来访问，而不是诉诸于环境变量。前端`pod`可以通过`backend-database.default.svc.cluster.local`访问后端数据库服务：`backend-database`对应于服务名称，`default`表示服务在其中定义的名称空间，`svc.cluster.local`是在所有集群本地服务名称中使用的可配置集群域后缀。

```shell
sam@elementoryos:~/kubernetes$ sudo kubectl get pods --namespace kube-system
NAME                               READY   STATUS             RESTARTS   AGE
coredns-755587fdc8-nz7s8           0/1     CrashLoopBackOff   80         2d6h
etcd-minikube                      1/1     Running            0          2d6h
kube-addon-manager-minikube        1/1     Running            0          2d6h
kube-apiserver-minikube            1/1     Running            0          2d6h
kube-controller-manager-minikube   1/1     Running            0          2d6h
kube-proxy-gczr4                   1/1     Running            0          2d6h
kube-scheduler-minikube            1/1     Running            0          2d6h
storage-provisioner                1/1     Running            0          2d6h
```

由于`kubernetes`容器编排中`kube-dns`服务不可用，因而在`pod`内部无法实现通过`service.namespace.clustername`访问`exposed`服务。在`pod`内部`/etc/resolv.conf`文件中保存内容与`host`文件类似。在`curl`这个服务是工作的，但却是`ping`不通的，因为服务的集群`ip`是一个虚拟`ip`，并且只有在于服务端口结合时才有意义。

```shell
sam@elementoryos:~/kubernetes$ sudo kubectl exec -it kubia-9vds6 bash
[sudo] password for sam: ******        
root@kubia-9vds6:/# curl http://kubia.default.svc.cluster.local
curl: (6) Could not resolve host: kubia.default.svc.cluster.local
root@kubia-9vds6:/# curl http://kubia.default
curl: (6) Could not resolve host: kubia.default
root@kubia-9vds6:/# curl http://kubia        
curl: (6) Could not resolve host: kubia

root@kubia-9vds6:/# cat /etc/resolv.conf 
nameserver 10.96.0.10
search default.svc.cluster.local svc.cluster.local cluster.local localdomain
```

连接集群外部的服务：在`kubernetes`中，服务并不是和`pod`直接相连的。相反，有一种资源介于两者之前——它就是`Endpoint`资源。如果之前在服务在运行过`kubectl describe`。`endpoint`资源就是暴露一个服务的`ip`地址和端口的列表，`endpoint`资源和其他`kubernetes`资源一样，所以可以使用`kubectl info`来获取它的基本信息。

```shell
sam@elementoryos:~/kubernetes$ sudo kubectl describe svc kubia
[sudo] password for sam:        
Name:              kubia
Namespace:         default
Labels:            <none>
Annotations:       <none>
Selector:          app=kubia
Type:              ClusterIP
IP:                10.111.211.203
Port:              http  80/TCP
TargetPort:        8080/TCP
Endpoints:         172.17.0.5:8080,172.17.0.6:8080,172.17.0.7:8080
Port:              https  443/TCP
TargetPort:        8443/TCP
Endpoints:         172.17.0.5:8443,172.17.0.6:8443,172.17.0.7:8443
Session Affinity:  ClientIP
Events:            <none>

sam@elementoryos:~/kubernetes$ sudo kubectl get endpoints kubia
NAME    ENDPOINTS                                                     AGE
kubia   172.17.0.5:8443,172.17.0.6:8443,172.17.0.7:8443 + 3 more...   23h
```

将服务暴露给外部客户端：服务的`pod`不仅可以在`kubernetes`内部进行调用，有时，`k8s`还需要向外部服务公开某些服务（例如`web`服务器，以便外部客户端可以访问它们）。有几种方式可以在外部访问服务：将服务类型设置为`NodePort`——每个集群节点都会在节点上打开一个端口，对于`NodePort`服务，每个集群节点在节点本身上打开一个端口，并将该端口上接收到的流量重定向到基础服务；将服务类型设置为`LoadBalance`，`NodePort`类型的一种扩展——这使得服务可以通过一个专用的负载均衡器来访问，这是由`kubernetes`中正在运行的云基础设置提供的；创建一个`Ingress`服务，这是一个完全不同的机制，通过一个`ip`地址公开多个服务。

```yaml
apiVersion: v1
kind: Service
metadata:
  name: kubia-nodeport
spec:
  type: NodePort
  ports:
  - port: 80
    targetPort: 8080
    nodePort: 30123
  selector:
    app: kubia
```

在配置文件`kubia-svc-nodeport.yaml`中，`spec`部分的`type`属性值为`NodePort`类型。其中`targetPort`表示背后`pod`的目标端口号、通过`nodePort`的集群的`30123`端口可以访问该服务。通过`kubectl get svc kubia-nodeport`可以看到`ENTERNAL-IP`列数据为`<nodes>`，表示服务可通过任何集群节点的`ip`地址访问。其中`PORT(S)`列显示集群`IP(80)`的内部端口和节点端口`(30123)`。可以使用`curl`命令通过`10.109.37.229`地址进行请求`pod`。在使用`minikube`时，可以运行`minikube service <service-name>`命令，就可以通过浏览器轻松访问`NodePort`服务。

```shell
sam@elementoryos:~/kubernetes$ sudo kubectl create -f kubia-svc-nodeport.yaml 
[sudo] password for sam:        
service/kubia-nodeport created
sam@elementoryos:~/kubernetes$ sudo kubectl get svc kubia-nodeport
NAME             TYPE       CLUSTER-IP      EXTERNAL-IP   PORT(S)        AGE
kubia-nodeport   NodePort   10.109.37.229   <none>        80:30123/TCP   17s
sam@elementoryos:~/kubernetes$ curl http://10.109.37.229:80
You've hit kubia-9vds6
sam@elementoryos:~/kubernetes$ sudo minikube service kubia-nodeport
|-----------|----------------|-------------|------------------------------|
| NAMESPACE |      NAME      | TARGET PORT |             URL              |
|-----------|----------------|-------------|------------------------------|
| default   | kubia-nodeport |             | http://192.168.170.130:30123 |
|-----------|----------------|-------------|------------------------------|
🎉  Opening kubernetes service  default/kubia-nodeport in default browser...
```

通过负载均衡将服务暴露出来，创建`LoadBalance`服务，`spec.type`的类型为`LoadBalancer`。如果没有指定特定的节点端口，`kubernetes`将会选择一个端口。如果使用的是`minikube`，尽管负载平衡器不会被分配，仍然可以通过节点端口（位于`minikube vm`的`ip`地址）访问服务。

```yaml
apiVersion: v1
kind: Service
metadata:
  name: kubia-loadbalancer
spec:
  type: LoadBalancer
  ports:
  - port: 80
    targetPort: 8080
  selector:
    app: kubia
```

```shell
sam@elementoryos:~/kubernetes$ sudo kubectl get svc kubia-loadbalancer
NAME                 TYPE           CLUSTER-IP       EXTERNAL-IP   PORT(S)        AGE
kubia-loadbalancer   LoadBalancer   10.101.132.161   <pending>     80:32608/TCP   41s
```

