#!/usr/bin/env bash

docker kill kube-proxy kubelet    kube-scheduler  kube-controller-manager kube-apiserver  etcd
docker rm   kube-proxy kubelet    kube-scheduler  kube-controller-manager kube-apiserver  etcd

docker  kill $(docker ps -a --no-trunc --filter name=^/k8s --format "{{.ID}}" )
docker  rm $(docker ps -a --no-trunc --filter name=^/k8s --format "{{.ID}}" )