
# Based on kubernetes/cluster/addons/dns/nodelocaldns/nodelocaldns.yaml v1.21.1
<#if RBACConfig == "rbac">
apiVersion: v1
kind: ServiceAccount
metadata:
  name: node-local-dns
  namespace: kube-system
  labels:
    kubernetes.io/cluster-service: "true"
    addonmanager.kubernetes.io/mode: Reconcile
</#if>
---
apiVersion: v1
kind: Service
metadata:
  name: kube-dns-upstream
  namespace: kube-system
  labels:
    k8s-app: kube-dns
    kubernetes.io/cluster-service: "true"
    addonmanager.kubernetes.io/mode: Reconcile
    kubernetes.io/name: "KubeDNSUpstream"
spec:
  ports:
  - name: dns
    port: 53
    protocol: UDP
    targetPort: 53
  - name: dns-tcp
    port: 53
    protocol: TCP
    targetPort: 53
  selector:
    k8s-app: kube-dns
---
apiVersion: v1
kind: ConfigMap
metadata:
  name: node-local-dns
  namespace: kube-system
  labels:
    addonmanager.kubernetes.io/mode: Reconcile
data:
  Corefile: |
    ${ClusterDomain}:53 {
        errors
        cache {
                success 9984 30
                denial 9984 5
        }
        reload
        loop
        bind ${IPAddress} ${ClusterDNSServer}
        forward . __PILLAR__CLUSTER__DNS__ {
                force_tcp
        }
        prometheus :9253
        health ${IPAddress}:8080
        }
    in-addr.arpa:53 {
        errors
        cache 30
        reload
        loop
        bind ${IPAddress} ${ClusterDNSServer}
        forward . __PILLAR__CLUSTER__DNS__ {
                force_tcp
        }
        prometheus :9253
        }
    ip6.arpa:53 {
        errors
        cache 30
        reload
        loop
        bind ${IPAddress} ${ClusterDNSServer}
        forward . __PILLAR__CLUSTER__DNS__ {
                force_tcp
        }
        prometheus :9253
        }
    .:53 {
        errors
        cache 30
        reload
        loop
        bind ${IPAddress} ${ClusterDNSServer}
        forward . __PILLAR__UPSTREAM__SERVERS__
        prometheus :9253
        }
---
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: node-local-dns
  namespace: kube-system
  labels:
    k8s-app: node-local-dns
    kubernetes.io/cluster-service: "true"
    addonmanager.kubernetes.io/mode: Reconcile
spec:
  updateStrategy:
<#if UpdateStrategy??>
    <#list UpdateStrategy?keys as key>
    <#assign itemUp = UpdateStrategy[key]>
    <#if (itemUp?is_hash) && (itemUp?size>0)>
    ${key}:
      <#list itemUp? keys as itemKey>
        <#if itemUp[itemKey]??>
      ${itemKey}: ${itemUp["${itemKey}"]}
        </#if>
      </#list>
    <#else>
    ${key}: ${UpdateStrategy["${key}"]}
    </#if>
  </#list>
<#else>
    rollingUpdate:
      maxUnavailable: 1
</#if>
  selector:
    matchLabels:
      k8s-app: node-local-dns
  template:
    metadata:
       labels:
          k8s-app: node-local-dns
    spec:
# Rancher specific change
      priorityClassName: ${NodeLocalDNSPriorityClassName!"system-node-critical"}
<#if RBACConfig == "rbac">
      serviceAccountName: node-local-dns
</#if>
      affinity:
        nodeAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
              - matchExpressions:
                - key: beta.kubernetes.io/os
                  operator: NotIn
                  values:
                    - windows
      hostNetwork: true
<#if NodeSelector??>
      nodeSelector:
      <#list NodeSelector?keys as k>
        ${k}: "${NodeSelector[k]}"
      </#list>
</#if>
      dnsPolicy: Default  # Don't use cluster DNS.
      tolerations:
      - operator: Exists
      containers:
      - name: node-cache
        image: ${NodelocalImage}
        resources:
          requests:
            cpu: 25m
            memory: 5Mi
        args: [ "-localip", "${IPAddress},${ClusterDNSServer}", "-conf", "/etc/Corefile", "-upstreamsvc", "kube-dns-upstream" ]
        securityContext:
          privileged: true
        ports:
        - containerPort: 53
          name: dns
          protocol: UDP
        - containerPort: 53
          name: dns-tcp
          protocol: TCP
        - containerPort: 9253
          name: metrics
          protocol: TCP
        livenessProbe:
          httpGet:
            host: ${IPAddress}
            path: /health
            port: 8080
          initialDelaySeconds: 60
          timeoutSeconds: 5
        volumeMounts:
        - mountPath: /run/xtables.lock
          name: xtables-lock
          readOnly: false
        - name: config-volume
          mountPath: /etc/coredns
        - name: kube-dns-config
          mountPath: /etc/kube-dns
      volumes:
      - name: xtables-lock
        hostPath:
          path: /run/xtables.lock
          type: FileOrCreate
      - name: kube-dns-config
        configMap:
          name: kube-dns
          optional: true
      - name: config-volume
        configMap:
          name: node-local-dns
          items:
            - key: Corefile
              path: Corefile.base
---
# A headless service is a service with a service IP but instead of load-balancing it will return the IPs of our associated Pods.
# We use this to expose metrics to Prometheus.
apiVersion: v1
kind: Service
metadata:
  labels:
    k8s-app: node-local-dns
  name: node-local-dns
  namespace: kube-system
spec:
  clusterIP: None
  ports:
    - name: metrics
      port: 9253
      targetPort: 9253
  selector:
    k8s-app: node-local-dns
