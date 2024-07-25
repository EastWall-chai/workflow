##!/bin/bash
 # 停止原来服务并删除镜像
 echo "start"
 cd $1/install/helm
 helm template app --values app/$4.yaml > $1/$2.$3.yaml
 ln -sf $1/$2.$3.yaml  $1/$2.yaml
 #kubectl delete -f    $1/$2.yaml
 kubectl apply  -f    $1/$2.yaml
 echo "end"