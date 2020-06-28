#!/bin/bash
# -----------------------------------------------------------------------------
# @(#) db-update.sh - Database update script
# -----------------------------------------------------------------------------
#
# Usage: db-update.sh dbUrl file1 [file2 ...]
#

. $(dirname $0)/profile.sh

[ "x$*" = "x" ] && die "Usage:  $(basename $0) dbUrl file1 [file2 ...]";

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

update_h2db $*;
