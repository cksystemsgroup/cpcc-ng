#!/bin/bash

. $(dirname $0)/profile.sh no-setup

for vehicle in "${!RVS[@]}";
do
	$CPCC_DIR/bin/stop_rv.sh $vehicle
done
