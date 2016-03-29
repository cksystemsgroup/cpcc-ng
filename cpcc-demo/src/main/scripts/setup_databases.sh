#!/bin/sh
# -----------------------------------------------------------------------------
# @(#) setup_databases.sh - Database setup script
# -----------------------------------------------------------------------------
#
# Usage: setup_databases.sh
#

cd $(dirname $0)/..;

. bin/profile.sh

ensureDir "$DBDIR" || exit 1;
ensureDir "$LOGDIR" || exit 1;
ensureDir "$WORKDIR" || exit 1;

for DB in GS01 RV01 RV02 RV03 RV04 RV05 RV06 RV07;
do
	echo "Updating database $DB";
	# url="jdbc:hsqldb:file://$DBDIR/$DB";
	url="jdbc:h2:file:$DBDIR/$DB;create=true"
	$CPCC_DIR/bin/liquibase-update.sh "$url";
	$CPCC_DIR/bin/db-update.sh "$url" "$CPCC_DIR/conf/db-setup-all.sql" "$CPCC_DIR/conf/db-setup-${DB}-rv.sql" "$CPCC_DIR/conf/db-setup-${DB}-cam.sql"
done
