#!/bin/bash

HOME_OPT="$BASE_OPT -Dlog4j.configurationFile=$LOG_CFG_HOME/cis_log4j2.xml"
JAVA_OPTS="$JAVA_OPTS -server -XX:CICompilerCount=2 -XX:+UseG1GC -XX:ConcGCThreads=1 -XX:ParallelGCThreads=2 -Xmx1g -Xms500m -Xmn400m -Xss256k -XX:+ExplicitGCInvokesConcurrent -XX:MaxDirectMemorySize=256m"
if [ x$JUSTICE_DEBUG == xtrue ];then
    DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5006"
    JAVA_OPTS="$JAVA_OPTS $DEBUG_OPTS"
fi

DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5006"
CIS_RESOURCE_OPTS="$RESOURCE_OPTS"
if [ $CIS_REST_PORT ];then
  CIS_RESOURCE_OPTS="$CIS_RESOURCE_OPTS -Dservicecomb.rest.address=0.0.0.0:$CIS_REST_PORT"
fi
if [ $CIS_HIGHWAY_PORT ];then
  CIS_RESOURCE_OPTS="$CIS_RESOURCE_OPTS -Dservicecomb.highway.address=0.0.0.0:$CIS_HIGHWAY_PORT"
fi

#echo "******-----   start cis      ----*******"
#java $HOME_OPT $JAVA_OPTS -jar ${BIN_HOME}/com.justice.cis.jar  1> /dev/null 2> ${LOG_OUT}/cis.out  &

echo "******-----   start cis      ----*******"
java $HOME_OPT $JAVA_OPTS $CIS_RESOURCE_OPTS -jar ${BIN_HOME}/com.justice.cis.jar  1> /dev/null 2> ${LOG_OUT}/cis.out  &
sleep 5