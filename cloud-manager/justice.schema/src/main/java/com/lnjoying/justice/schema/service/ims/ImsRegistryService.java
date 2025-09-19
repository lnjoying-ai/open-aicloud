package com.lnjoying.justice.schema.service.ims;

import io.swagger.annotations.ApiParam;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * ims registry service
 *
 * @author merak
 **/
public interface ImsRegistryService {

    /**
     * query by registryId and userId
     * @param registryId
     * @param bpId
     * @param userId
     * @return
     */
    Registry getRegistry(@ApiParam(name = "registryId")@NotNull String registryId, @ApiParam(name = "bpId") String bpId, @ApiParam(name = "userId") String userId);

    Registry getRegistryByUrlAndName(@ApiParam(name = "registryUrl")@NotNull String registryUrl, @ApiParam(name = "userName") String userName);

    String getRegistryUrl(@ApiParam(name = "registryId")@NotNull String registryId);

    final class Registry implements Serializable {

        private static final long serialVersionUID = -5970373514918069294L;

        private String registryId;

        private String registryUrl;

        private String registryUserName;

        /**
         * Use base 64 encoding
         */
        private String registryPassword;

        public String getRegistryId() {
            return registryId;
        }

        public void setRegistryId(String registryId) {
            this.registryId = registryId;
        }

        public String getRegistryUrl() {
            return registryUrl;
        }

        public void setRegistryUrl(String registryUrl) {
            this.registryUrl = registryUrl;
        }

        public String getRegistryUserName() {
            return registryUserName;
        }

        public void setRegistryUserName(String registryUserName) {
            this.registryUserName = registryUserName;
        }

        public String getRegistryPassword() {
            return registryPassword;
        }

        public void setRegistryPassword(String registryPassword) {
            this.registryPassword = registryPassword;
        }
    }
}
