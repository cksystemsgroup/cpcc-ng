#!/bin/bash
# -----------------------------------------------------------------------------
# @(#) liquibase-update.sh - Liquibase update script
# -----------------------------------------------------------------------------
#
# Usage: liquibase-update.sh dbUrl
#

URL="$1";

. $(dirname $0)/profile.sh

[ "x$*" = "x" ] && die "Usage:  $(basename $0) dbUrl";

cd $CPCC_DIR/classes;

CP="$LIBDIR/h2-${COM_H2DB_VERSION}.jar:$LIBDIR/liquibase-core-${LIQUIBASE_VERSION}.jar";
CP="$CP:$LIBDIR/snakeyaml-${SNAKEYAML_VERSION}.jar:$LIBDIR/slf4j-api-${SLF4J_VERSION}.jar";
CP="$CP:$LIBDIR/logback-core-${LOGBACK_VERSION}.jar:$LIBDIR/logback-classic-${LOGBACK_VERSION}.jar";
DRV='org.h2.Driver';

java -classpath $CP liquibase.integration.commandline.Main \
        --driver="$DRV" \
        --url="$URL" \
        --username='sa' \
        --password='' \
        --changeLogFile=dbchange/update.xml \
    update
