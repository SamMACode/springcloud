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

