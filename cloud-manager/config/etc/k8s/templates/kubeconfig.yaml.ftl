apiVersion: v1
kind: Config
clusters:
<#if nodes?size != 0>
  <#list nodes as node>
- name: ${node.clusterName}
  cluster:
    server: ${node.server}
  <#if node.cert?? >
    certificate-authority-data: ${node.cert}
  </#if>
  </#list>
</#if>

users:
- name: ${user!}
  user:
<#if token??>
    token: ${token}
<#else>
    exec:
      apiVersion: client.authentication.k8s.io/v1beta1
      args:
        - token
        - --server=${host!}
        - --user=${user!}
<#if endpointEnabled?? >
        - --cluster=${clusterId!}
</#if>
      command: justice
</#if>

contexts:
<#if nodes?size != 0>
  <#list nodes as node>
- name: ${node.clusterName}
  context:
    user: ${user}
    cluster: ${node.clusterName}
  </#list>
</#if>
current-context: ${clusterName!}