FROM swr.cn-north-4.myhuaweicloud.com/lnjoying/lnjoying_jdk8_ubuntu:v0.2.1

COPY com.justice.cmp.jar /root/justice_cloud/bin/
COPY cloud-manager/script/ /root/justice_cloud/script/

EXPOSE $CIS_REST_PORT $CIS_HIGHWAY_PORT

WORKDIR /root/justice_cloud/script

ENTRYPOINT ["/bin/bash", "-c"]
CMD ["source ./env_config.sh && ./start_cmp.sh && ./sleep.sh"]