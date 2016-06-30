#!/bin/bash
# -----------------------------------------------------------------------------
# @(#) setup_demo.sh - interactively configure ground stations, real vehicles,
#                       and operation areas 
# -----------------------------------------------------------------------------
#
# Usage: setup_demo.sh
#

. $(dirname $0)/profile.sh no-setup

setup() {
	BASE_PORT='';
	[ -f $(dirname $0)/setup_databases.sh ] && BASE_PORT=$(awk -F"'" '/CONNECTOR_PORT\[/ && /GS01/ { print $4 }' < $(dirname $0)/setup_databases.sh);
	[ "$BASE_PORT" = "" -a -f $WORKDIR/temp/db-setup-all.sql ] && BASE_PORT=$(grep GS01 $WORKDIR/temp/db-setup-all.sql | sed -e 's#^.*http://localhost:##' -e 's#.,.GROUND_STATION.,.*$##');
	[ "$BASE_PORT" = "" ] && BASE_PORT=$(grep GS01 $1/db-setup-all.sql | sed -e 's#^.*http://localhost:##' -e 's#.,.GROUND_STATION.,.*$##')

	while true; do 
		SEL=$(askUser 'Please select the base port number ('"$BASE_PORT"')' '[0-9]*' '')
		[ "$SEL" = "" ] && break;
		[ "$SEL" -ge '8000' -a "$SEL" -le "30000" ] && BASE_PORT=$SEL && break;
	done
	
	echo "Setup using configuration in $1 and base port number $BASE_PORT"	
	ensureDir "$WORKDIR" || exit 1;

	ensureDir "$WORKDIR/temp"  || exit 1;
	
	for f in $1/*.sql; do
		perl -pe 's[(http://localhost:)(\d+)]{$1.($2-8000+'"$BASE_PORT"')}egis' < $f > $WORKDIR/temp/$(basename $f);
	done
			
	info "Creating file $CONFIG"
	{
		echo "GS_OPTS='-Xmx1500m -Xss256k '"
		echo "RV_OPTS='-Xmx900m -Xss128k '"
		echo
		echo "declare -A RVS"
		ls -1 $WORKDIR/temp/db-setup-*-rv.sql | sed -e 's#^.*/db-setup-##' -e 's#-rv\.sql$##' | while read rv; do echo "RVS['$rv']='$rv'"; done;
		echo "declare -A CONNECTOR_PORT"
		echo "declare -A SHUTDOWN_PORT"
		perl -ne 'm#(\047[^\047]+\047),\047(http://localhost:)(\d+)# and print "CONNECTOR_PORT[",$1,"]=\047",$3,"\047\n","SHUTDOWN_PORT[",$1,"]=\047",$3+5,"\047\n"; ' \
			< $WORKDIR/temp/db-setup-all.sql | sort
		echo
		echo "CAMERAS=false"
		echo "DB_SCRIPT_DIR=$WORKDIR/temp"
		echo		
	} > $CONFIG;
	
	$(dirname $0)/setup_databases.sh 
}


while true
do
	[ -f "$CONFIG" ] && YN=$(askUser 'You already ran the setup. Do you want to overwrite it? (y|N)' '[yYnN]' '')
	[ "$YN" = "n" -o "$YN" = "N" ] && break;
	
	[ -f "$CONFIG" ] && YN=$(askUser 'Overwriting your setup will delete all databases. Do you want that? (y|N)' '[yYnN]' '')
	[ "$YN" = "n" -o "$YN" = "N" ] && break;
	
	$(dirname $0)/shutdown.sh
	
	info "Removing all existing databases."
	rm -rf $DBDIR/*
	
	CONFIGS="$(ls -1 ${SETUPS_DIR} | awk '{printf "%3d: %s\n", NR, $0}')"
	NR=$(echo "$CONFIGS" | wc -l | awk '{print $1}')

	info "The following configurations are available:";
	echo "$CONFIGS";
	
	SEL=$(askUser 'Please select one of the configurations above (1..'"$NR"')' '[0-9][0-9]*' '')
	[ "$SEL" -ge '1' -a "$SEL" -le "$NR" ] && setup "$SETUPS_DIR/$(echo "$CONFIGS" | awk 'NR == '"$SEL"' {print $2}')" && break;
done
