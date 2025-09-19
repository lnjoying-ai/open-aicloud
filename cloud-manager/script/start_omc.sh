#!/bin/bash

HOME_OPT="$BASE_OPT -Dlog4j.configurationFile=$LOG_CFG_HOME/omc_log4j2.xml"
JAVA_OPTS="$JAVA_OPTS -server -XX:CICompilerCount=2 -XX:+UseG1GC -XX:ConcGCThreads=1 -XX:ParallelGCThreads=2 -Xmx1g -Xms500m -Xmn400m -Xss256k -XX:+ExplicitGCInvokesConcurrent -XX:MaxDirectMemorySize=256m"
OMC_RESOURCE_OPTS="$RESOURCE_OPTS"
if [ $OMC_REST_PORT ];then
  OMC_RESOURCE_OPTS="$OMC_RESOURCE_OPTS -Dservicecomb.rest.address=0.0.0.0:$OMC_REST_PORT"
fi
if [ $OMC_HIGHWAY_PORT ];then
  OMC_RESOURCE_OPTS="$OMC_RESOURCE_OPTS -Dservicecomb.highway.address=0.0.0.0:$OMC_HIGHWAY_PORT"
fi

if [ x$JUSTICE_DEBUG == xtrue ];then
    DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5001"
    JAVA_OPTS="$JAVA_OPTS $DEBUG_OPTS"
fi

echo "******-----    start omc    ----*******"

java $HOME_OPT $JAVA_OPTS $OMC_RESOURCE_OPTS -jar ${BIN_HOME}/com.justice.omc.jar  1 > /dev/null 2> ${LOG_OUT}/omc.out  &
sleep 2