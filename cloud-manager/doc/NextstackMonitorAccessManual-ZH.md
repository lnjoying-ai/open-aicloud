# nextstack监控采集

## nextstack监控采集

### 1 已有prometheus实例情况

可以采用remote write 或remote read方式部署。

修改prometheus.yml配置文件

#### 1.1 remote write模式

当前的prometheus 写入prometheus server。

```plaintext
global:
  external_labels:
    cloud_id: next_cloud_id
remote_write:
  - url: "http://xxxx:19090/api/v1/write"
    remote_timeout: 30s
    basic_auth:
      username: admin
      password: admin
    follow_redirects: true
    queue_config:
      capacity: 2500
      max_shards: 200
      min_shards: 1
      max_samples_per_send: 500
      batch_send_deadline: 5s
      min_backoff: 30ms
      max_backoff: 5s
    metadata_config:
      send: true
      send_interval: 1m
      max_samples_per_send: 500
    tls_config:
      insecure_skip_verify: true  
    
```

(1) 复制上面的配置文件到prometheus.yml文件中

（2）在云端平台的【运维管理】->【系统服务】-> 【prometheus】中查看prometheus server 的remote write地址,修改上面的url。查看认证信息，修改basic\_auth的username和password。

（3）查看nextstack云id, 修改next_cloud_id值。

（4）重启prometheus。

（5）如果是自签证书，需要设置insecure\_skip\_verify为true.

#### 1.2 remote read模式

##### 1.2.1 修改prometheus server配置

```plaintext
remote_read:
  - url: 'http://xxxx/api/v1/read'
    read_recent: true
```

(1) 在云端平台的【运维管理】->【系统服务】-> 【prometheus】中查看prometheus server，修改config

(2) 复制上面的配置文件到config中,，修改xxxx为要读取的nextstack prometheus 地址，点击保存。

##### 1.2.2 修改nextstack prometheus 配置

```plaintext
global:
  external_labels:
    cloud_id: next_cloud_id
```

（1）查看nextstack云id, 修改next_cloud_id值。

（2）重启prometheus。

### 2 没有prometheus实例情况

#### 2.1 部署实例

可以通过平台部署promtheus agent，也可以手动部署。然后参考上述已有实例操作。

```plaintext
version: '3'

networks:
  monitor-net:
    driver: bridge

services:

  prometheus:
    image: lnjoying/prometheus:v2.47.2
    container_name: lnjoying-prometheus-agent
    volumes:
      - /var/lnjoying/monitor/prometheus:/etc/prometheus
      - /var/lnjoying/monitor/prometheus/data:/prometheus
      - ${prometheus_yml}:/etc/prometheus/prometheus.yml
      - /var/lnjoying/volume/monitor/target_files:/root/monitor/target_files
    command:
      - '--config.file=/etc/prometheus/prometheus.yml'
      - '--web.console.libraries=/etc/prometheus/console_libraries'
      - '--web.console.templates=/etc/prometheus/consoles'
      - '--web.enable-lifecycle'
      - "--enable-feature=agent"
    restart: unless-stopped
    ports:
      - "${EXPOSE_PORT_HTTP_PROMETHEUS_AGENT}:9090"
    user: root
    expose:
      - 9090
    networks:
      - monitor-net
    labels:
      org.label-schema.group: "lnjoying"
```

prometheus.yml参考

```text/x-yaml
global:
  scrape_interval:     15s
  evaluation_interval: 15s


rule_files:
#- "alert.rules"

scrape_configs:
  - job_name: 'node'
    file_sd_configs:
      - files:
          - /root/monitor/target_files/LIGHT_NODE/*.json
        refresh_interval: 1m

  - job_name: 'cadvisor'
    file_sd_configs:
      - files:
          - /root/monitor/target_files/CADVISOR/*.json
        refresh_interval: 1m
    metric_relabel_configs:
      - source_labels: [ id ]
        regex: /docker/([a-f0-9]+)$
        target_label: container_ref_id
        replacement: $1

  - job_name: 'prometheus-agent'
    scrape_interval: 10s
    static_configs:
      - targets: ['localhost:9090']
```

### 3 修改libvirt 抓取target的labels

在job\_name同一级补充下面的配置，

```plaintext
metric_relabel_configs:
  - source_labels: [cmpUserId]
    target_label: user_id
    regex: '(.*)'
    replacement: '$1'
    action: replace
  - source_labels: [cmpTenantId]
    target_label: bp_id
    regex: '(.*)'
    replacement: '$1'
    action: replace
```