---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
  name: proxy-clusterrole-kubeapiserver
rules:
- apiGroups: [""]
  resources:
  - nodes/metrics
  - nodes/proxy
  - nodes/stats
  - nodes/log
  - nodes/spec
  verbs: ["get", "list", "watch", "create"]
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: proxy-role-binding-kubernetes-master
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: proxy-clusterrole-kubeapiserver
subjects:
- apiGroup: rbac.authorization.k8s.io
  kind: User
  name: kube-apiserver
---
apiVersion: v1
kind: Namespace
metadata:
  name: joy-system

---

apiVersion: v1
kind: ServiceAccount
metadata:
  name: joy
  namespace: joy-system

---

apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: joy-admin-binding
  namespace: joy-system
  labels:
    joy.io/creator: "norman"
subjects:
- kind: ServiceAccount
  name: joy
  namespace: joy-system
roleRef:
  kind: ClusterRole
  name: joy-admin
  apiGroup: rbac.authorization.k8s.io

---

<#if PrivateRegistryConfig??>
apiVersion: v1
kind: Secret
metadata:
  name: joy-private-registry
  namespace: joy-system
type: kubernetes.io/dockerconfigjson
data:
  .dockerconfigjson: "${PrivateRegistryConfig}"

---
</#if>

apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
  name: joy-admin
  labels:
    joy.io/creator: "norman"
rules:
- apiGroups:
  - '*'
  resources:
  - '*'
  verbs:
  - '*'
- nonResourceURLs:
  - '*'
  verbs:
  - '*'

---

apiVersion: apps/v1
kind: Deployment
metadata:
  name: joy-cluster-agent
  namespace: joy-system
spec:
  selector:
    matchLabels:
      app: joy-cluster-agent
  template:
    metadata:
      labels:
        app: joy-cluster-agent
    spec:
      affinity:
        nodeAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
              - matchExpressions:
                - key: beta.kubernetes.io/os
                  operator: NotIn
                  values:
                    - windows
          preferredDuringSchedulingIgnoredDuringExecution:
          - preference:
              matchExpressions:
              - key: node-role.kubernetes.io/controller
                operator: In
                values:
                - "true"
            weight: 100
          - preference:
              matchExpressions:
              - key: node-role.kubernetes.io/controller
                operator: In
                values:
                - "true"
            weight: 100
          - preference:
              matchExpressions:
              - key: node-role.kubernetes.io/master
                operator: In
                values:
                - "true"
            weight: 100
          - preference:
              matchExpressions:
              - key: joy.io/cluster-agent
                operator: In
                values:
                - "true"
            weight: 1
      serviceAccountName: joy
      tolerations:
<#if Tolerations??>
      # Tolerations added based on found taints on controller nodes
<#--<#if Tolerations??>${Tolerations}</#if>-->
    <#list Tolerations as toleration>
      -
        <#list toleration? keys as key>
        ${key}: ${toleration["${key}"]}
        </#list>
    <#--- ${toleration}-->
    </#list>
<#else>
      # No taints or no controller nodes found, added defaults
      - effect: NoSchedule
        key: node-role.kubernetes.io/controller
        value: "true"
      - effect: NoSchedule
        key: "node-role.kubernetes.io/controller"
        operator: "Exists"
      - effect: NoSchedule
        key: "node-role.kubernetes.io/master"
        operator: "Exists"
</#if>
      containers:
        - name: cluster-register
          imagePullPolicy: IfNotPresent
          args: ["--reg_token", ${Token}, "--region", ${region_id}, "--gateway", ${gateway}]
          env:
<#if Features??>
          - name: JOY_FEATURES
            value: "${Features}"
</#if>
<#if WorkerType??>
          - name: WORKER_TYPE
            value: "${WorkerType}"
</#if>
<#if IsJKE??>
          - name: JOY_IS_JKE
            value: "${IsJKE}"
</#if>
<#if URLPlain??>
          - name: JOY_SERVER
            value: "${URLPlain}"
</#if>
<#if CAChecksum??>
          - name: JOY_CA_CHECKSUM
            value: "${CAChecksum}"
</#if>
          - name: JOY_CLUSTER
            value: "true"
          - name: JOY_K8S_MANAGED
            value: "true"
<#if ClusterRegistry??>
          - name: JOY_CLUSTER_REGISTRY
            value: "${ClusterRegistry}"
</#if>
<#if AgentEnvVars??>
     <#list AgentEnvVars?keys as key>
          - name: ${key}
            value: "${AgentEnvVars.get(key)}"
     </#list>
</#if>
          image: ${AgentImage}
          ports:
          - containerPort: 8080
          livenessProbe:
            initialDelaySeconds: 30
            periodSeconds: 10
            httpGet:
              path: /healthz
              port: 8080
     <#if PrivateRegistryConfig??>
      imagePullSecrets:
      - name: joy-private-registry
     </#if>

<#if AuthImage??>
---

apiVersion: apps/v1
kind: DaemonSet
metadata:
    name: kube-api-auth
    namespace: joy-system
spec:
  selector:
    matchLabels:
      app: kube-api-auth
  template:
    metadata:
      labels:
        app: kube-api-auth
    spec:
      affinity:
        nodeAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
              - matchExpressions:
                - key: beta.kubernetes.io/os
                  operator: NotIn
                  values:
                    - windows
                - key: node-role.kubernetes.io/controller
                  operator: In
                  values:
                    - "true"
      hostNetwork: true
      serviceAccountName: joy
      tolerations:
      - operator: Exists
      containers:
      - name: kube-api-auth
        image: ${AuthImage}
        imagePullPolicy: IfNotPresent
        volumeMounts:
        - name: k8s-ssl
          mountPath: /etc/kubernetes
        securityContext:
          privileged: true
      <#if PrivateRegistryConfig??>
        imagePullSecrets:
      - name: joy-private-registry
      </#if>
      volumes:
      - name: k8s-ssl
        hostPath:
          path: /etc/kubernetes
          type: DirectoryOrCreate
  updateStrategy:
    type: RollingUpdate
    rollingUpdate:
      maxUnavailable: 25%
</#if>