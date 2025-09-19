apiVersion: apps/v1
kind: Deployment
metadata:
  name: joy-terminal
  namespace: joy-system
  labels:
    app: joy-terminal
spec:
  replicas: 1
  selector:
    matchLabels:
      app: joy-terminal
  template:
    metadata:
      name: joy-terminal
      labels:
        app: joy-terminal
    spec:
      hostNetwork: true
      containers:
        - name: joy-terminal
          image: ${TerminalImage}
          imagePullPolicy: IfNotPresent
<#if IsK8s??>
          command:
            - /bin/bash
          securityContext:
            privileged: true
          tty: true
          env:
            - name: KUBECONFIG
              value: "/etc/kubernetes/ssl/kubecfg-kube-admin.yaml"
          volumeMounts:
            - mountPath: /etc/kubernetes
              name: etc-kubernetes
      volumes:
        - name: etc-kubernetes
          hostPath:
            path: /etc/kubernetes
</#if>
<#if IsK3s??>
          command:
            - /bin/sh
          securityContext:
            privileged: true
          tty: true
          volumeMounts:
            - mountPath: /etc/rancher/k3s
              name: etc-rancher
      volumes:
        - name: etc-rancher
          hostPath:
            path: /etc/rancher/k3s
</#if>