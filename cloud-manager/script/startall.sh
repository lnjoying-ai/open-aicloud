#!/bin/bash
source ./env_config.sh
./start_sys.sh
./start_cls.sh
./start_cmp.sh
./start_servicemanager.sh
./start_ecrm.sh
./start_scheduler.sh
./start_cis.sh
./start_aos.sh
./start_ims.sh
./start_servicegw.sh
./start_apiserver.sh
./start_iam.sh
./start_omc.sh