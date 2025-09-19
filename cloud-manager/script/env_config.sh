#!/bin/bash
export APP_HOME=~/justice_cloud
export CONFIG_BASE_HOME=${HOME}/justice_config
export ETC_HOME=${CONFIG_BASE_HOME}/etc
export LOG_CFG_HOME=${CONFIG_BASE_HOME}/log
export BIN_HOME=${APP_HOME}/bin
export LOG_OUT=${APP_HOME}/out
export LOG_DIR=${APP_HOME}/logs
export BASE_OPT="-Dfile.encoding=UTF-8 -Dlj_config=$ETC_HOME"
export RESOURCE_OPTS=

#export JUSTICE_DEBUG=false
# servicecomb
if [ $SC_ADDRESS ];then
  RESOURCE_OPTS="$RESOURCE_OPTS -Dservicecomb.service.registry.address=$SC_ADDRESS"
fi
if [ $INSPECTOR ];then
  RESOURCE_OPTS="$RESOURCE_OPTS -Dservicecomb.inspector.enabled=$INSPECTOR"
else
  RESOURCE_OPTS="$RESOURCE_OPTS -Dservicecomb.inspector.enabled=false"
fi
if [ $PUBLISH_ADDRESS ];then
  RESOURCE_OPTS="$RESOURCE_OPTS -Dservicecomb.service.publishAddress=$PUBLISH_ADDRESS"
fi
if [ $ZIPKIN_ADDRESS ];then
  RESOURCE_OPTS="$RESOURCE_OPTS -Dservicecomb.handler.tracing.collector.address=$ZIPKIN_ADDRESS"
fi