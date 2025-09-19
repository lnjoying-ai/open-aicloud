#!/bin/bash

for pid in `ps -aef | grep com.justice | awk '{print $2;}'`
do
    kill -9 $pid
done
