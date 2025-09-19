# AICloud安装教程

## 一、要求

系统类型：Linux

架构：x86

权限：root sudo

系统发行版本：Ubuntu20.04

内核版本：建议4.19以上

软件：docker & docker-compose

网络：接入互联网，最好有公网(根据需求)

端口：

| 端口 | 说明 |
| --- | --- |
| 5432 | postgres |
| 9080 | cloud web |
| 7867 | websocket |
| 16443 | cluster-server |
| 16005 | frps |

## 二、安装

### 2.1、下载脚本

```text/x-yaml
cd /home/lnjoying
wget http://edge.lnjoying.com:9080/lnjoying/aicloud/install.sh
```

### 2.2、执行脚本

```text/x-yaml
  sudo bash install.sh aicloud
```

注：

1、如果/home/lnjoying目录不存在，建议创建一个lnjoying的用户后再进行操作

2、脚本目前支持加密或不加密模式  需手动确认


### 2.3、检查容器健康状态

执行
```text/x-yaml
docker ps -a
```
检查所有容器的运行状态是否为UP，健康状态是否为healthy。待所有容器运行状态为UP，健康状态为healthy后执行
```text/x-yaml
docker restart aicloud-iam
```

### 2.4、系统相关配置

#### 2.4.1 打开平台，进入“系统管理”-“配置中心”

![image.png](https://alidocs.oss-cn-zhangjiakou.aliyuncs.com/res/BKM7qe8xv9xdnpj8/img/7e4c7dba-6521-4ccd-b027-faeed3aa1b00.png)

#### 2.4.2 修改com.justice.cluster-manager.config配置

默认配置如下

```text/x-yaml
clusterServer:
  url: 127.0.0.1:9080
  inner: 127.0.0.1
clusterManager:
  addonFilePath: /root/down/k8s/addoncfg
  addonFileUrl: http://27.221.79.190:9080/k8s/addoncfg/
 cluster:
  registry-url: hub.lnjoying.io:18443 
```

配置解释

【必须修改】clusterServer.url 指的是云端的服务地址，将默认值127.0.0.1:9080修改成平台对外提供服务的ip和port即可。

【可以修改】clusterServer.inner 

【可以修改】clusterManager.addonFilePath 指的是addon文件的位置，不建议修改，修改后需要同步修改lnjoying-justice-cloud的文件挂载，修改不正确时可能会出现部署k8s时获取不到addon文件的情况

【必须修改】clusterManager.addonFileUrl 指的是云端对外提供的文件下载路径，将默认值27.221.79.190:9080修改成平台对外提供服务的ip和port即可

【可以修改】registry-url表示的是集群组件使用的镜像库地址，对应的项目需要是公开访问权限。

**2.4.3 修改com.justice.ecrm.config配置**

默认配置如下

```text/x-yaml
install:
  installScriptUrl: lnjoying.io:9080/lnjoying/v10/edge_cloud/agent/main.sh
  cloudAddr: 27.221.89.120:9188
  gw:
    localPort: 31107
    restPort: 41107
    image: lnjoying/edge-gw:latest
  agent:
    restPort: 41107
    image: lnjoying/edge-agent:latest
version:
  agentVersions: '[]'
service_agent:
  image: lnjoying/cluster-agent:v0.3.0
```

配置解释

【可以修改】install.installScriptUrl 指的是agent和gw安装脚本的路径，可以根据情况修改，建议使用秒如提供的文件下载路径

【必须修改】install.cloudAddr 指的是云端的网关监听地址，将默认值27.221.89.120:9188修改成平台对外提供服务的ip和云端监听网关连接的端口（默认9188）即可。

【可以修改】install.gw.localPort

【可以修改】install.gw.restPort

【建议修改】install.gw.image 指的是网关的镜像，默认配置的是docker hub中的tag为latest的镜像，建议修改为指定版本v0.8.0，防止出现latest镜像更新后不兼容的情况。

【可以修改】install.agent.restPort

【建议修改】install.agent.image 指的是agent的镜像，默认配置的是docker hub中的tag为latest的镜像，建议修改为指定版本v0.8.0，防止出现latest镜像更新后不兼容的情况。

【建议修改】version.agentVersions 指的是agent的升级版本信息，需要配置为当前云端支持的agent信息，格式如'\[{"version":"0.7.1","image\_name":"lnjoying/edge-agent:v0.7.1","image\_hash":{"linux/amd64":"sha256:2a1d3b67bb2d8504d476b0de59acd5d178107801d7011d7b4fc6cbb1c2651b31","linux/arm64":"sha256:2a1d3b67bb2d8504d476b0de59acd5d178107801d7011d7b4fc6cbb1c2651b31"}}\]'

【可以修改】service\_agent.image 服务agent的默认镜像版本

**2.4.4 修改com.justice.cis.config配置**

默认配置如下

```text/x-yaml
ws:
  port: 7867
  urlPrefix: wss://27.221.79.185:9080/wss/
```

配置解释

【可以修改】ws.port 

【必须修改】ws.urlPrefix 指的是websocket访问地址前缀，将默认值中27.221.79.185:9080修改成平台对外提供服务的ip和port即可。

**2.4.5 修改com.justice.aos.config.frp配置**

默认配置如下

```text/x-yaml
tty:
  proxy_server_url: /%s/tty
  frps_server_port: 7000
  #frpc客户端访问frps的地址，可能是私网也可能是公网地址
  frps_server_addr: 112.25.78.90
  #用户从公网可以访问的地址，可以是域名，也可以是ip
  frp_addr: 112.25.78.90:16005
```

配置解释

【可以修改】tty.proxy\_server\_url 此地址为tty外网访问的地址，需要按照frps可访问的地址设置。后缀/%s/tty暂不支持修改。如果此项没设置，使用tty.frp\_addr设置项。

【可以修改】tty.frps\_server\_port

【必须修改】tty.frps\_server\_addr 此处设置frp server的地址， 例如：192.168.1.2 ，可以将frp server和gw部署在同一台机器上，将默认值中112.25.78.90修改成平台对外提供服务的ip即可。

【建议修改】tty.frp\_addr 此处设置域名，可以为公网地址或者公网地址对应的域名。如果tty.proxy\_server\_url项为空，此处必须修改为可用的地址。

**2.4.6  邮箱修改**

**2.4.6.1  邮箱发件人修改**

**dataId:** com.justice.api.config.notify

默认配置如下：

```text/x-yaml
mail:
  host: smtp.163.com
  username: lnjoying@163.com
  password: 
  auth: "true"
  endableStarttls: "true"
  requiredStarttls: "true"
  sendFrom: lnjoying@163.com
```

配置解释:

  可以修改邮件账号服务器地址， 发件人的账号、密码。

**2.4.6.2  邮件模板修改**

**dataId:** com.justice.api.config.notify.auth\_verification\_code

默认配置如下：

```plaintext
尊敬的用户,\n
\n
您好！\n
\n
您正在设置邮箱地址。验证码：%s，验证码有效时间：%s分钟。请勿向任何人包括客服提供验证码。\n
\n
感谢您选择我们！\n
\n
秒如官方\n
\n
联系我们邮件：service@lnjoying.com
```

配置解释:

 按照客户情况配置。

**2.4.7  修改微信小程序认证信息**

**dataId:** com.justice.ums.wx.config

默认配置如下：

```text/x-yaml
default-weixin-password: joying@365
wx:
  miniapp:
    appid: xxxx
    secret: xxxx
    config-storage:
      #      redis:
      #        database: 1
      #        host: ${REDIS_HOST:127.0.0.1}
      #        port: ${REDIS_PORT:6379}
      #        password: ${REDIS_PASSWORD:hello}
      #        max-active: ${MAX_CONNECT:10}
      #        max-idle: ${MAX_IDLE:10}
      #        max-wait-millis: ${MAX_WAIT_MILLIS:1000000}
      type: Jedis

```

配置解释:

  【必须修改】修改appid 和secret为申请的账号和密码。

其他项目暂时不支持修改。

**2.4.8  修改com.justice.cmp.config配置**

**dataId:** com.justice.cmp.config

默认配置如下：

```text/x-yaml
cloud_server:
  url: 127.0.0.1:9080
  token: 624d3e8b-5b59-4da0-bb4e-2864ce804458
#  inner: 127.0.0.1:16443
service_gw:
  url: 127.0.0.1:16443
cloud_agent:
  image: lnjoying/cluster-agent:v0.3.0
```

配置解释:

  【必须修改】cloud\_server.url 指的是云端的服务地址，将默认值127.0.0.1:9080修改成平台对外提供服务的ip和port即可。

【可以修改】cloud\_server.token cmp模块表示，建议定期修改

【可以修改】service\_gw.url servicegw的代理服务地址和端口，可以根据实际情况修改

【可以修改】cloud\_agent.image cloudagent的镜像，可以根据实际情况修改。

**2.4.9  修改跨域配置**

**dataId:** com.justice.iam.common.config

默认配置如下：

```text/x-yaml
cors-allow-origins: "*"
```

配置解释:

【可以修改】cors-allow-origins设置允许哪些域可以访问，例如：[https://192.168.1.250:9080](https://192.168.1.250:9080)。多个按照逗号分隔。

**2.4.10  运维监控修改**

**dataId:** com.justice.omc.config

默认配置如下：

```text/x-yaml
send.sms: false
send.email: true
```

配置解释:

【可以修改】send.sms, 告警是否使用短信，如果为true，则发送短信

【可以修改】send.email, 告警是否使用邮件，如果为true，则发送邮件，默认为true


**2.5、清理文件，重启系统**

【必须清理】待系统正常运行后，清理默认的配置文件。

```plaintext
rm /home/lnjoying/mount/justice/config/etc/cluster_server_root_ca.yaml
rm /home/lnjoying/mount/justice/config/etc/cluster_server.yaml
rm /home/lnjoying/mount/justice/config/etc/ssl/ssl.key
rm /home/lnjoying/mount/justice/config/etc/ssl/ssl.pem
```

【可选】如果需要使用已有的ssl证书，将证书放在以下路径，文件名为ssl.key和ssl.pem。

```plaintext
/home/lnjoying/mount/justice/config/etc/ssl/
```

并修改到/home/lnjoying/mount/justice/config/etc/cluster\_server\_root\_ca.yaml中

修改方式为

![image.png](https://alidocs.oss-cn-zhangjiakou.aliyuncs.com/res/EPd6l2kXxyddn7Ma/img/8c494a0f-f439-4aeb-8aa3-0adf08c82a34.png)

【必须重启】重启云端aicloud-sys。

```plaintext
docker restart aicloud-sys
```

【必须重启】重启云端aicloud-nginx。

```plaintext
docker restart aicloud-nginx
```

## 2.6、nginx配置修改

### 2.6.1修改tty

【**必须】**在边缘云所在机器上修改如下配置：

如果直接通过本机ip对外提供服务（未绑定域名，未做请求转发），可以跳过该步骤，也可以不跳过。

```text/x-yaml
#修改nginx中tty的转发地址，将下方“x.x.x.x:x”修改为frps对外提供服务的ip和端口
sed -i 's/$host:16005/x.x.x.x:x/g' /home/lnjoying/mount/nginx/nginx.conf
#重启nginx容器
docker restart lnjoying-nginx
```


## 三、验证

netstat -alnpt #查看端口

http://ip:9080 #访问前端查看是否正常