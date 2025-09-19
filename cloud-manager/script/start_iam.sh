#!/bin/bash

HOME_OPT="$BASE_OPT -Dlog4j.configurationFile=$LOG_CFG_HOME/iam_log4j2.xml"
JAVA_OPTS="$JAVA_OPTS -server -XX:CICompilerCount=2 -XX:+UseG1GC -XX:ConcGCThreads=1 -XX:ParallelGCThreads=2 -Xmx1g -Xms500m -Xmn400m -Xss256k -XX:+ExplicitGCInvokesConcurrent -XX:MaxDirectMemorySize=256m"
IAM_RESOURCE_OPTS="$RESOURCE_OPTS"
if [ $IAM_REST_PORT ];then
  IAM_RESOURCE_OPTS="$IAM_RESOURCE_OPTS -Dservicecomb.rest.address=0.0.0.0:$IAM_REST_PORT"
fi

if [ x$JUSTICE_DEBUG == xtrue ];then
    DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5016"
    JAVA_OPTS="$JAVA_OPTS $DEBUG_OPTS"
fi

echo "******-----     start iam    ----*******"

java $HOME_OPT $JAVA_OPTS $IAM_RESOURCE_OPTS -jar ${BIN_HOME}/com.justice.iam.jar  1> /dev/null 2> ${LOG_OUT}/iam.out  &
sleep 4