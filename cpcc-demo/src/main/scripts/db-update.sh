#!/bin/sh
# -----------------------------------------------------------------------------
# @(#) db-update.sh - Database update script
# -----------------------------------------------------------------------------
#
# Usage: db-update.sh dbUrl file1 [file2 ...]
#

cd $(dirname $0)/..;

. bin/profile.sh

[ "x$*" = "x" ] && die "Usage:  $(basename $0) dbUrl file1 [file2 ...]";

update_hsqldb () {
	URL="$1"; shift; CP="$CPCC_DIR/lib/sqltool-${COM_HSQLDB_VERSION}.jar";

	COUNT=$(java -jar $CP --sql 'select count(*) from devices;' "--inlineRc=url=${URL},user=sa,password=");

	[ $COUNT -ne 0 ] && echo "Database '$URL' has been already initialized." && exit 0;

	echo "Executing scripts $*";
	java -jar $CP "--inlineRc=url=${URL},user=sa,password=" --autoCommit $*;
}

update_h2db () {
	URL="$1"; shift; CP="$CPCC_DIR/lib/h2-${COM_H2DB_VERSION}.jar";
	TMP="tmp.$$";
	echo "select count(*) from devices;" > $TMP;
	COUNT=$(java -cp $CP org.h2.tools.RunScript -url "${URL}" -script "$TMP" -user sa -showResults | awk '/^--> / {print $2}');
	rm $TMP;
	[ "$COUNT" -ne 0 ] && echo "Database '$URL' has been already initialized." && exit 0;

	for s in $*; do
		echo "Executing script $s";
		java -cp $CP org.h2.tools.RunScript -url "${URL}" -script "$s" -user sa;
	done
}

# update_hsqldb $*;
update_h2db $*;
