
<#if RBACConfig == "rbac">
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
  labels:
    k8s-app: metrics-server
    rbac.authorization.k8s.io/aggregate-to-admin: "true"
    rbac.authorization.k8s.io/aggregate-to-edit: "true"
    rbac.authorization.k8s.io/aggregate-to-view: "true"
  name: system:aggregated-metrics-reader
rules:
- apiGroups:
  - metrics.k8s.io
  resources:
  - pods
  - nodes
  verbs:
  - get
  - list
  - watch
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
  labels:
    k8s-app: metrics-server
  name: system:metrics-server
rules:
- apiGroups:
  - ""
  resources:
  - pods
  - nodes
  - nodes/stats
  - namespaces
  - configmaps
  verbs:
  - get
  - list
  - watch
---
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  labels:
    k8s-app: metrics-server
  name: metrics-server-auth-reader
  namespace: kube-system
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: Role
  name: extension-apiserver-authentication-reader
subjects:
- kind: ServiceAccount
  name: metrics-server
  namespace: kube-system
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  labels:
    k8s-app: metrics-server
  name: metrics-server:system:auth-delegator
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: system:auth-delegator
subjects:
- kind: ServiceAccount
  name: metrics-server
  namespace: kube-system
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  labels:
    k8s-app: metrics-server
  name: system:metrics-server
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: system:metrics-server
subjects:
- kind: ServiceAccount
  name: metrics-server
  namespace: kube-system
</#if>
---
apiVersion: v1
kind: ServiceAccount
metadata:
  labels:
    k8s-app: metrics-server
  name: metrics-server
  namespace: kube-system
---
apiVersion: v1
kind: Service
metadata:
  labels:
    k8s-app: metrics-server
  name: metrics-server
  namespace: kube-system
spec:
  ports:
  - name: https
    port: 443
    protocol: TCP
    targetPort: https
  selector:
    k8s-app: metrics-server
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: metrics-server
  namespace: kube-system
  labels:
    k8s-app: metrics-server
spec:
<#if Replicas??>
  replicas: ${Replicas}
</#if>
  selector:
    matchLabels:
      k8s-app: metrics-server
<#if UpdateStrategy??>
  strategy:
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
</#if>
  template:
    metadata:
      name: metrics-server
      labels:
        k8s-app: metrics-server
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
                - key: node-role.kubernetes.io/worker
                  operator: Exists
<#if NodeSelector??>
      nodeSelector:
      <#list NodeSelector?keys as k>
        ${k}: "${NodeSelector[k]}"
      </#list>
</#if>
      serviceAccountName: metrics-server
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
      volumes:
      - emptyDir: {}
        name: tmp-dir
      # Rancher specific change
      priorityClassName: ${CoreDNSPriorityClassName!"system-cluster-critical"}
      containers:
      - name: metrics-server
        image: ${MetricsServerImage}
        imagePullPolicy: IfNotPresent
        livenessProbe:
          failureThreshold: 3
          httpGet:
            path: /livez
            port: https
            scheme: HTTPS
          periodSeconds: 10
        args:
        - --cert-dir=/tmp
        - --secure-port=4443
        # Rancher specific: connecting to kubelet using insecure tls
        - --kubelet-insecure-tls
        - --kubelet-preferred-address-types=InternalIP
        - --logtostderr
<#if Options??>
        <#list Options?keys as k>
        -  --${k}: "${Options[k]}"
        </#list>
</#if>
        ports:
        - containerPort: 4443
          name: https
          protocol: TCP
        readinessProbe:
          failureThreshold: 3
          httpGet:
            path: /readyz
            port: https
            scheme: HTTPS
          periodSeconds: 10
        securityContext:
          readOnlyRootFilesystem: true
          runAsNonRoot: true
          runAsUser: 1000
        volumeMounts:
        - mountPath: /tmp
          name: tmp-dir
---
apiVersion: apiregistration.k8s.io/v1
kind: APIService
metadata:
  labels:
    k8s-app: metrics-server
  name: v1beta1.metrics.k8s.io
spec:
  group: metrics.k8s.io
  groupPriorityMinimum: 100
  insecureSkipTLSVerify: true
  service:
    name: metrics-server
    namespace: kube-system
  version: v1beta1
  versionPriority: 100
---
