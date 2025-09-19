apiVersion: apiserver.config.k8s.io/v1
kind: EncryptionConfiguration
resources:
  - resources:
      - secrets
    providers:
      - aescbc:
          keys:
  <#if keyNames??>
          - name: ${keyNames.name}
            secret: ${keyNames.secret}
  </#if>
  - identity: {}