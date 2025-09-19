FROM lnjoying/cluster-agent:base

ADD com.justice.agent.jar /usr/local/lnjoying

WORKDIR  /usr/local/lnjoying
ENTRYPOINT ["java", "-jar","com.justice.agent.jar"]