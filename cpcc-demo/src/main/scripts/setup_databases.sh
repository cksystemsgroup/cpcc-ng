#!/bin/bash
# -----------------------------------------------------------------------------
# @(#) setup_databases.sh - Database setup script
# -----------------------------------------------------------------------------
#
# Usage: setup_databases.sh
#

. $(dirname $0)/profile.sh no-setup

ensureDir "$DBDIR" || exit 1;
ensureDir "$LOGDIR" || exit 1;
ensureDir "$WORKDIR" || exit 1;

for DB in "${!RVS[@]}";
do
	echo "Creating database $DB";
	# url="jdbc:hsqldb:file://$DBDIR/$DB";
	url="jdbc:h2:file:$DBDIR/$DB"
	$CPCC_DIR/bin/liquibase-update.sh "$url";
	SCRIPTS="$DB_SCRIPT_DIR/db-setup-all.sql $DB_SCRIPT_DIR/db-setup-${DB}-rv.sql"
	$CAMERAS && SCRIPTS="$SCRIPTS $DB_SCRIPT_DIR/db-setup-${DB}-cam.sql"
	$CPCC_DIR/bin/db-update.sh "$url" $SCRIPTS
done
