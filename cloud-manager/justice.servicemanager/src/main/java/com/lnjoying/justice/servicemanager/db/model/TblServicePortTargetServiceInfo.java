package com.lnjoying.justice.servicemanager.db.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class TblServicePortTargetServiceInfo implements Serializable {
    private String id;

    private String servicePortId;

    private String description;

    private String portAllocationId;

    private String svcNode;

    private String svcIp;

    private Integer svcPort;

    private String targetSvcNode;

    private Integer targetSvcPort;

    private String protocol;

    private String site;

    private String service;

    private String targetIp;

    private Integer targetPort;

    private String cert;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getServicePortId() {
        return servicePortId;
    }

    public void setServicePortId(String servicePortId) {
        this.servicePortId = servicePortId == null ? null : servicePortId.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getPortAllocationId() {
        return portAllocationId;
    }

    public void setPortAllocationId(String portAllocationId) {
        this.portAllocationId = portAllocationId == null ? null : portAllocationId.trim();
    }

    public String getSvcNode() {
        return svcNode;
    }

    public void setSvcNode(String svcNode) {
        this.svcNode = svcNode == null ? null : svcNode.trim();
    }

    public String getSvcIp() {
        return svcIp;
    }

    public void setSvcIp(String svcIp) {
        this.svcIp = svcIp == null ? null : svcIp.trim();
    }

    public Integer getSvcPort() {
        return svcPort;
    }

    public void setSvcPort(Integer svcPort) {
        this.svcPort = svcPort;
    }

    public String getTargetSvcNode() {
        return targetSvcNode;
    }

    public void setTargetSvcNode(String targetSvcNode) {
        this.targetSvcNode = targetSvcNode == null ? null : targetSvcNode.trim();
    }

    public Integer getTargetSvcPort() {
        return targetSvcPort;
    }

    public void setTargetSvcPort(Integer targetSvcPort) {
        this.targetSvcPort = targetSvcPort;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol == null ? null : protocol.trim();
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site == null ? null : site.trim();
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service == null ? null : service.trim();
    }

    public String getTargetIp() {
        return targetIp;
    }

    public void setTargetIp(String targetIp) {
        this.targetIp = targetIp == null ? null : targetIp.trim();
    }

    public Integer getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(Integer targetPort) {
        this.targetPort = targetPort;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert == null ? null : cert.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TblServicePortTargetServiceInfo targetServiceInfo = (TblServicePortTargetServiceInfo) o;
        return (Objects.equals(targetSvcNode, targetServiceInfo.targetSvcNode) || Objects.equals(svcNode, targetServiceInfo.targetSvcNode)) &&
                (Objects.equals(targetSvcPort, targetServiceInfo.targetSvcPort) || Objects.equals(svcPort, targetServiceInfo.targetSvcPort)) &&
                Objects.equals(protocol, targetServiceInfo.protocol) &&
                Objects.equals(site, targetServiceInfo.site) &&
                Objects.equals(service, targetServiceInfo.service) &&
                Objects.equals(targetIp, targetServiceInfo.targetIp) &&
                Objects.equals(targetPort, targetServiceInfo.targetPort) &&
                Objects.equals(cert, targetServiceInfo.cert);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetSvcNode, targetSvcPort, protocol, site, service, targetIp, targetPort, cert);
    }
}