#!/bin/bash

HOME_OPT="$BASE_OPT -Dlog4j.configurationFile=$LOG_CFG_HOME/sys_log4j2.xml"
JAVA_OPTS="$JAVA_OPTS -server -XX:CICompilerCount=2 -XX:+UseG1GC -XX:ConcGCThreads=1 -XX:ParallelGCThreads=2 -Xmx1g -Xms500m -Xmn400m -Xss256k -XX:+ExplicitGCInvokesConcurrent -XX:MaxDirectMemorySize=256m"
SYS_RESOURCE_OPTS="$RESOURCE_OPTS"
if [ $SYS_REST_PORT ];then
  SYS_RESOURCE_OPTS="$SYS_RESOURCE_OPTS -Dservicecomb.rest.address=0.0.0.0:$SYS_REST_PORT"
fi

if [ x$JUSTICE_DEBUG == xtrue ];then
    DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5002"
    JAVA_OPTS="$JAVA_OPTS $DEBUG_OPTS"
fi

echo "******-----     start sys    ----*******"

java $HOME_OPT $JAVA_OPTS $SYS_RESOURCE_OPTS -jar ${BIN_HOME}/com.justice.sys.jar  1> /dev/null 2> ${LOG_OUT}/sys.out  &
sleep 30


#int=1
#while(( $int<=10 ))
#do
#    text=$(curl -X POST  -s http://127.0.0.1:8090/SysServiceImpl/getNacos)
#    echo $text |grep  "serverAddr"
#    if test $? -eq 0
#	then
#	    echo "sys start"
#      break
#    else
#      sleep 3
#      let "int++"
#    fi
#done