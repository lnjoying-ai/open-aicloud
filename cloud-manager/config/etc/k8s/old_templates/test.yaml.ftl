
"kind": "${kind}",
"apiVersion": "${apiVersion}",
"metadata":
    "name": "${metadataName}",
    "labels":
        "name": "${lablesName}",
        "version": "${lablesVersion}"
"spec":
    "containers":
        "name": "${containerName}",
        "image": "${imageName}",
        "ports":
            <#list portList as portInfo>
            "containerPort":${portInfo.containerPort?c}
            <#--<#if portInfo_has_next></#if>-->
            </#list>

<#if UpdateYmal??>
        update: ${UpdateYmal}
</#if>

