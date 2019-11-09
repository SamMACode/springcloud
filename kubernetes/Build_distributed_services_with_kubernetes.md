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

管理`docker`服务常用应用脚本：` sudo service docker start `  启动`docker`服务、` sudo service docker stop ` 停止`docker`服务、` sudo service docker restart `重启docker服务.

2）使用`minikube`在本机搭建`kubernetes`集群，简单体验`k8s`： 

```shell
sam@elementoryos:~$ sudo minikube start --vm-driver=none --image-mirror-country cn --memory=1024mb --disk-size=8192mb --registry-mirror=https://registry.docker-cn.com --image-repository='registry.cn-hangzhou.aliyuncs.com/google_containers' --bootstrapper=kubeadm --extra-config=apiserver.authorization-mode=RBAC
```



```shell
sam@elementoryos:~$ sudo kubectl create clusterrolebinding add-on-cluster-admin --clusterrole=cluster-admin --serviceaccount=kube-system:default
```



```
sam@elementoryos:~$ sudo minikube dashboard
```







