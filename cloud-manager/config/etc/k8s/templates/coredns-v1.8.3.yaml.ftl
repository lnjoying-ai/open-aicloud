
# Based on coredns/deployment/kubernetes/coredns.yaml.sed v1.8.3
---
<#if RBACConfig == "rbac">
apiVersion: v1
kind: ServiceAccount
metadata:
  name: coredns
  namespace: kube-system
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
  labels:
    kubernetes.io/bootstrapping: rbac-defaults
  name: system:coredns
rules:
- apiGroups:
  - ""
  resources:
  - endpoints
  - services
  - pods
  - namespaces
  verbs:
  - list
  - watch
- apiGroups:
  - discovery.k8s.io
  resources:
  - endpointslices
  verbs:
  - list
  - watch
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  annotations:
    rbac.authorization.kubernetes.io/autoupdate: "true"
  labels:
    kubernetes.io/bootstrapping: rbac-defaults
  name: system:coredns
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: system:coredns
subjects:
- kind: ServiceAccount
  name: coredns
  namespace: kube-system
</#if>
---
apiVersion: v1
kind: ConfigMap
metadata:
  name: coredns
  namespace: kube-system
data:
  Corefile: |
    .:53 {
        errors
        health {
          lameduck 5s
        }
        ready
        kubernetes ${ClusterDomain} <#if ReverseCIDRs??>${ReverseCIDRs}<#else>in-addr.arpa ip6.arpa</#if> {
          pods insecure
          fallthrough in-addr.arpa ip6.arpa
        }
        prometheus :9153
	<#if UpstreamNameservers??>
        forward . <#list UpstreamNameservers as serverInfo>${serverInfo}<#if serverInfo_has_next> </#if></#list>
	<#else>
        forward . "/etc/resolv.conf"
	</#if>
        cache 30
        loop
        reload
        loadbalance
    } # STUBDOMAINS - Rancher specific change
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: coredns
  namespace: kube-system
  labels:
    k8s-app: kube-dns
    kubernetes.io/name: "CoreDNS"
spec:
  # replicas is not specified in upstream template, default is 1.
  # Will be tuned in real time if DNS horizontal auto-scaling is turned on.
  replicas: 1
  strategy:
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
    type: RollingUpdate
    rollingUpdate:
      maxUnavailable: 1
</#if>
  selector:
    matchLabels:
      k8s-app: kube-dns
  template:
    metadata:
      labels:
        k8s-app: kube-dns
      annotations:
        seccomp.security.alpha.kubernetes.io/pod: 'docker/default'
    spec:
      # Rancher specific change
      priorityClassName: ${CoreDNSPriorityClassName!"system-cluster-critical"}
<#if RBACConfig == "rbac">
      serviceAccountName: coredns
</#if>
<#if Tolerations??>
      tolerations:
      <#list Tolerations as toleration>
        -
        <#list toleration? keys as key>
          ${key}: ${toleration["${key}"]}
        </#list>
      <#--- ${toleration}-->
      </#list>
<#else>
      tolerations:
        - key: "CriticalAddonsOnly"
          operator: "Exists"
        - effect: NoExecute
          operator: Exists
        - effect: NoSchedule
          operator: Exists
</#if>
      nodeSelector:
        beta.kubernetes.io/os: linux
<#if NodeSelector??>
      <#list NodeSelector?keys as k>
        ${k}: "${NodeSelector[k]}"
      </#list>
</#if>
      affinity:
        nodeAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
              - matchExpressions:
                - key: node-role.kubernetes.io/worker
                  operator: Exists
        podAntiAffinity:
          preferredDuringSchedulingIgnoredDuringExecution:
           - weight: 100
             podAffinityTerm:
               labelSelector:
                 matchExpressions:
                   - key: k8s-app
                     operator: In
                     values: ["kube-dns"]
               topologyKey: kubernetes.io/hostname
      containers:
      - name: coredns
        image: ${CoreDNSImage}
        imagePullPolicy: IfNotPresent
        resources:
          limits:
            memory: 170Mi
          requests:
            cpu: 100m
            memory: 70Mi
        args: [ "-conf", "/etc/coredns/Corefile" ]
        volumeMounts:
        - name: config-volume
          mountPath: /etc/coredns
          readOnly: true
        ports:
        - containerPort: 53
          name: dns
          protocol: UDP
        - containerPort: 53
          name: dns-tcp
          protocol: TCP
        - containerPort: 9153
          name: metrics
          protocol: TCP
        livenessProbe:
          httpGet:
            path: /health
            port: 8080
            scheme: HTTP
          initialDelaySeconds: 60
          timeoutSeconds: 5
          successThreshold: 1
          failureThreshold: 5
        readinessProbe:
          httpGet:
            path: /ready
            port: 8181
            scheme: HTTP
        securityContext:
          allowPrivilegeEscalation: false
          capabilities:
            add:
            - NET_BIND_SERVICE
            drop:
            - all
          readOnlyRootFilesystem: true
      dnsPolicy: Default
      volumes:
        - name: config-volume
          configMap:
            name: coredns
            items:
            - key: Corefile
              path: Corefile
---
apiVersion: v1
kind: Service
metadata:
  name: kube-dns
  namespace: kube-system
  annotations:
    prometheus.io/port: "9153"
    prometheus.io/scrape: "true"
  labels:
    k8s-app: kube-dns
    kubernetes.io/cluster-service: "true"
    kubernetes.io/name: "CoreDNS"
spec:
  selector:
    k8s-app: kube-dns
  clusterIP: ${ClusterDNSServer}
  ports:
  - name: dns
    port: 53
    protocol: UDP
  - name: dns-tcp
    port: 53
    protocol: TCP
  - name: metrics
    port: 9153
    protocol: TCP
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: coredns-autoscaler
  namespace: kube-system
  labels:
    k8s-app: coredns-autoscaler
spec:
  selector:
    matchLabels:
      k8s-app: coredns-autoscaler
  template:
    metadata:
      labels:
        k8s-app: coredns-autoscaler
    spec:
<#if RBACConfig == "rbac">
      serviceAccountName: coredns-autoscaler
</#if>
# Rancher specific change
<#if CoreDNSAutoscalerPriorityClassName??>
      priorityClassName: ${CoreDNSAutoscalerPriorityClassName}
</#if>
      nodeSelector:
        beta.kubernetes.io/os: linux
      affinity:
        nodeAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
              - matchExpressions:
                - key: node-role.kubernetes.io/worker
                  operator: Exists
<#if Tolerations??>
      tolerations:
      <#list Tolerations as toleration>
        -
        <#list toleration? keys as key>
          ${key}: ${toleration["${key}"]}
        </#list>
      <#--- ${toleration}-->
      </#list>
<#else>
      tolerations:
      - effect: NoExecute
        operator: Exists
      - effect: NoSchedule
        operator: Exists
</#if>
      containers:
      - name: autoscaler
        image: ${CoreDNSAutoScalerImage}
        resources:
            requests:
                cpu: "20m"
                memory: "10Mi"
        command:
          - /cluster-proportional-autoscaler
          - --namespace=kube-system
          - --configmap=coredns-autoscaler
          - --target=Deployment/coredns
          # When cluster is using large nodes(with more cores), "coresPerReplica" should dominate.
          # If using small nodes, "nodesPerReplica" should dominate.
<#if LinearAutoscalerParams??>
          - --default-params={"linear":${LinearAutoscalerParams}}
<#else>
          - --default-params={"linear":{"coresPerReplica":128,"nodesPerReplica":4,"min":1,"preventSinglePointFailure":true}}
</#if>
          - --nodelabels=node-role.kubernetes.io/worker=true,beta.kubernetes.io/os=linux
          - --logtostderr=true
          - --v=2
<#if RBACConfig == "rbac">
---
apiVersion: v1
kind: ServiceAccount
metadata:
  name: coredns-autoscaler
  namespace: kube-system
---
kind: ClusterRole
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: system:coredns-autoscaler
rules:
  - apiGroups: [""]
    resources: ["nodes"]
    verbs: ["list", "watch"]
  - apiGroups: [""]
    resources: ["replicationcontrollers/scale"]
    verbs: ["get", "update"]
  - apiGroups: ["extensions","apps"]
    resources: ["deployments/scale", "replicasets/scale"]
    verbs: ["get", "update"]
  - apiGroups: [""]
    resources: ["configmaps"]
    verbs: ["get", "create"]
---
kind: ClusterRoleBinding
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: system:coredns-autoscaler
subjects:
  - kind: ServiceAccount
    name: coredns-autoscaler
    namespace: kube-system
roleRef:
  kind: ClusterRole
  name: system:coredns-autoscaler
  apiGroup: rbac.authorization.k8s.io
</#if>
