apiVersion: batch/v1
kind: Job
metadata:
  name: jke-network-plugin-deploy-job
  namespace: kube-system
spec:
  backoffLimit: 10
  template:
    metadata:
       name: jke-deploy
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
        tolerations:
        - operator: Exists
        hostNetwork: true
        serviceAccountName: jke-job-deployer
        nodeName: s190
        containers:
          - name: rke-network-plugin-pod
            image: lnjoying/joykube:v1.20.9
            command: [ "kubectl", "apply", "-f" , "http://27.221.79.190:9080/addoncfg/network.yaml"]
        restartPolicy: Never`