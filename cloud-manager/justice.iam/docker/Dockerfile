FROM swr.cn-north-4.myhuaweicloud.com/lnjoying/lnjoying_jdk8_ubuntu:v0.2.1

COPY com.justice.iam.jar /root/justice_cloud/bin/
COPY cloud-manager/script/ /root/justice_cloud/script/

WORKDIR /root/justice_cloud/script

ENTRYPOINT ["/bin/bash", "-c"]
CMD ["source ./env_config.sh && ./start_iam.sh && ./sleep.sh"]