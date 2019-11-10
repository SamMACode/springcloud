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



