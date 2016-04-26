#!/bin/bash

. $(dirname $0)/profile.sh

for vehicle in "${!RVS[@]}";
do
	$CPCC_DIR/bin/start_rv.sh $vehicle
done
