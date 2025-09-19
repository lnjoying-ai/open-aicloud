apiVersion: v1
kind: Config
clusters:
  - cluster:
      api-version: v1
      certificate-authority-data: ${cacrt}
      server: ${kubernetesURL}
    name: ${clusterName}
contexts:
  - context:
      cluster: ${clusterName}
      user: ${componentName}-${clusterName}
    name: ${clusterName}
current-context: ${clusterName}
users:
  - name: ${componentName}-${clusterName}
    user:
      client-certificate-data: ${crt}
      client-key-data: ${key}