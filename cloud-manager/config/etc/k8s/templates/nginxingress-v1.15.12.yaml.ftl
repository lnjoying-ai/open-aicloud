
# this template is intended for use by >= nginx-0.32.0
apiVersion: v1
kind: Namespace
metadata:
  name: ingress-nginx
---
kind: ConfigMap
apiVersion: v1
metadata:
  name: nginx-configuration
  namespace: ingress-nginx
  labels:
    app: ingress-nginx
data:
<#if Options??>
<#list Options?keys as k>
  ${k}: "${Options[k]}"
</#list>
</#if>
---
kind: ConfigMap
apiVersion: v1
metadata:
  name: tcp-services
  namespace: ingress-nginx
---
kind: ConfigMap
apiVersion: v1
metadata:
  name: udp-services
  namespace: ingress-nginx
<#if RBACConfig == "rbac">
---
apiVersion: v1
kind: ServiceAccount
metadata:
  name: nginx-ingress-serviceaccount
  namespace: ingress-nginx
---
apiVersion: rbac.authorization.k8s.io/v1beta1
kind: ClusterRole
metadata:
  name: nginx-ingress-clusterrole
rules:
  - apiGroups:
      - ""
    resources:
      - configmaps
      - endpoints
      - nodes
      - pods
      - secrets
    verbs:
      - list
      - watch
  - apiGroups:
      - ""
    resources:
      - nodes
    verbs:
      - get
  - apiGroups:
      - ""
    resources:
      - services
    verbs:
      - get
      - list
      - update
      - watch
  - apiGroups:
      - extensions
      - "networking.k8s.io" # k8s 1.14+
    resources:
      - ingresses
    verbs:
      - get
      - list
      - watch
  - apiGroups:
      - ""
    resources:
      - events
    verbs:
      - create
      - patch
  - apiGroups:
      - extensions
      - "networking.k8s.io" # k8s 1.14+
    resources:
      - ingresses/status
    verbs:
      - update
---
apiVersion: rbac.authorization.k8s.io/v1beta1
kind: Role
metadata:
  name: nginx-ingress-role
  namespace: ingress-nginx
rules:
  - apiGroups:
      - ""
    resources:
      - namespaces
    verbs:
      - get
  - apiGroups:
      - ""
    resources:
      - configmaps
      - pods
      - secrets
      - endpoints
    verbs:
      - get
      - list
      - watch
  - apiGroups:
      - ""
    resources:
      - services
    verbs:
      - get
      - list
      - update
      - watch
  - apiGroups:
      - extensions
      - "networking.k8s.io" # k8s 1.14+
    resources:
      - ingresses
    verbs:
      - get
      - list
      - watch
  - apiGroups:
      - extensions
      - "networking.k8s.io" # k8s 1.14+
    resources:
      - ingresses/status
    verbs:
      - update
  - apiGroups:
      - ""
    resources:
      - configmaps
    resourceNames:
      # Defaults to "<election-id>-<ingress-class>"
      # Here: "<ingress-controller-leader>-<nginx>"
      # This has to be adapted if you change either parameter
      # when launching the nginx-ingress-controller.
      - ingress-controller-leader-nginx
    verbs:
      - get
      - update
  - apiGroups:
      - ""
    resources:
      - configmaps
    verbs:
      - create
  - apiGroups:
      - ""
    resources:
      - endpoints
    verbs:
      - create
      - get
      - update
  - apiGroups:
      - ""
    resources:
      - events
    verbs:
      - create
      - patch
---
apiVersion: rbac.authorization.k8s.io/v1beta1
kind: RoleBinding
metadata:
  name: nginx-ingress-role-nisa-binding
  namespace: ingress-nginx
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: Role
  name: nginx-ingress-role
subjects:
  - kind: ServiceAccount
    name: nginx-ingress-serviceaccount
    namespace: ingress-nginx
---
apiVersion: rbac.authorization.k8s.io/v1beta1
kind: ClusterRoleBinding
metadata:
  name: nginx-ingress-clusterrole-nisa-binding
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: nginx-ingress-clusterrole
subjects:
  - kind: ServiceAccount
    name: nginx-ingress-serviceaccount
    namespace: ingress-nginx
</#if>
---
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: nginx-ingress-controller
  namespace: ingress-nginx
spec:
  selector:
    matchLabels:
      app: ingress-nginx
<#if UpdateStrategy??>
  updateStrategy:
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
      labels:
        app: ingress-nginx
      annotations:
        prometheus.io/port: '10254'
        prometheus.io/scrape: 'true'
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
      <#if NetworkMode == "hostNetwork">
      hostNetwork: true
      </#if>
      <#if DNSPolicy??>
      dnsPolicy: ${DNSPolicy}
      </#if>
# Rancher specific change
<#if NginxIngressControllerPriorityClassName??>
      priorityClassName: ${NginxIngressControllerPriorityClassName}
</#if>
<#if NodeSelector??>
      nodeSelector:
      <#list NodeSelector?keys as k>
        ${k}: "${NodeSelector[k]}"
      </#list>
</#if>
      <#if RBACConfig == "rbac">
      serviceAccountName: nginx-ingress-serviceaccount
      </#if>
      terminationGracePeriodSeconds: 60
      tolerations:
      - effect: NoExecute
        operator: Exists
      - effect: NoSchedule
        operator: Exists
      containers:
        - name: nginx-ingress-controller
          image: ${IngressImage}
          args:
            - /nginx-ingress-controller
            <#if DefaultBackend??>
            - --default-backend-service=$(POD_NAMESPACE)/default-http-backend
            </#if>
            - --configmap=$(POD_NAMESPACE)/nginx-configuration
            - --election-id=ingress-controller-leader
            - --ingress-class=nginx
            - --tcp-services-configmap=$(POD_NAMESPACE)/tcp-services
            - --udp-services-configmap=$(POD_NAMESPACE)/udp-services
            - --annotations-prefix=nginx.ingress.kubernetes.io
        <#if ExtraArgs??>
          <#list ExtraArgs?keys as k>
            - --${k}<#if ExtraArgs[k] != "">=${ExtraArgs[k]}</#if>
          </#list>
        </#if>
          securityContext:
          <#if NetworkMode != "none" >
            capabilities:
                drop:
                - ALL
                add:
                - NET_BIND_SERVICE
          </#if>
            runAsUser: 101
          env:
            - name: POD_NAME
              valueFrom:
                fieldRef:
                  fieldPath: metadata.name
            - name: POD_NAMESPACE
              valueFrom:
                fieldRef:
                  fieldPath: metadata.namespace
<#if ExtraEnvs??>
  <#list ExtraEnvs as ExtraEnvsInfo>
            - ${ExtraEnvsInfo}
  </#list>
</#if>
          ports:
          - name: http
            <#if NetworkMode == "hostNetwork">
            containerPort: 80
            <#elseif (NetworkMode == "hostPort" || NetworkMode == "none")>
            containerPort: ${ExtraArgs["http-port"]!"80"}
            <#if NetworkMode == "hostPort">
            hostPort: ${HTTPPort}
            </#if>
            </#if>
          - name: https
            <#if NetworkMode == "hostNetwork">
            containerPort: 443
            <#elseif (NetworkMode == "hostPort" || NetworkMode == "none")>
            containerPort: ${ExtraArgs["https-port"]!"443"}
            <#if NetworkMode == "hostPort">
            hostPort: ${HTTPPort}
            </#if>
            </#if>
          livenessProbe:
            failureThreshold: 3
            httpGet:
              path: /healthz
              port: 10254
              scheme: HTTP
            initialDelaySeconds: 10
            periodSeconds: 10
            successThreshold: 1
            timeoutSeconds: 1
          readinessProbe:
            failureThreshold: 3
            httpGet:
              path: /healthz
              port: 10254
              scheme: HTTP
            initialDelaySeconds: 10
            periodSeconds: 10
            successThreshold: 1
            timeoutSeconds: 1
<#if ExtraVolumeMounts??>
          volumeMounts:
  <#list ExtraVolumeMounts as ExtraVolumeMountsInfo>
            - ${ExtraVolumeMountsInfo}
  </#list>
</#if>

<#if ExtraVolumes??>
      volumes:
  <#list ExtraVolumes as ExtraVolumesInfo>
        - ${ExtraVolumesInfo}
  </#list>
</#if>

---
<#if DefaultBackend??>
apiVersion: apps/v1
kind: Deployment
metadata:
  name: default-http-backend
  labels:
    app: default-http-backend
  namespace: ingress-nginx
spec:
  replicas: 1
  selector:
    matchLabels:
      app: default-http-backend
  template:
    metadata:
      labels:
        app: default-http-backend
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
      terminationGracePeriodSeconds: 60
# Rancher specific change
<#if DefaultHTTPBackendPriorityClassName??>
      priorityClassName: ${DefaultHTTPBackendPriorityClassName}
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
      - effect: NoExecute
        operator: Exists
      - effect: NoSchedule
        operator: Exists
</#if>
      containers:
      - name: default-http-backend
        # Any image is permissable as long as:
        # 1. It serves a 404 page at /
        # 2. It serves 200 on a /healthz endpoint
        image: ${IngressBackend}
        livenessProbe:
          httpGet:
            path: /healthz
            port: 8080
            scheme: HTTP
          initialDelaySeconds: 30
          timeoutSeconds: 5
        ports:
        - containerPort: 8080
        resources:
          limits:
            cpu: 10m
            memory: 20Mi
          requests:
            cpu: 10m
            memory: 20Mi
---
apiVersion: v1
kind: Service
metadata:
  name: default-http-backend
  namespace: ingress-nginx
  labels:
    app: default-http-backend
spec:
  ports:
  - port: 80
    targetPort: 8080
  selector:
    app: default-http-backend
</#if>
