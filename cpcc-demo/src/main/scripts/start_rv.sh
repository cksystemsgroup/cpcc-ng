#!/bin/bash
# -----------------------------------------------------------------------------
# @(#) start_rv.sh - real vehicle start script.
# -----------------------------------------------------------------------------
#
# Usage: start_rv.sh RV-name
#

. $(dirname $0)/profile.sh

[ "x$*" = "x" ] && die "Usage:  $(basename $0) RV-name";

APP_CONTEXT_PATH=$1;

[ "${RVS[$1]}" = "$1" ] || die "Real Vehicle $1 is not configured. Start aborted.";

case "$APP_CONTEXT_PATH" in
	GS*)  APP_WAR_FILE="$(ls $CPCC_DIR/war/cpcc-gs-web*.war | tail -1)"; OPTS="$GS_OPTS"; ;;
	RV*)  APP_WAR_FILE="$(ls $CPCC_DIR/war/cpcc-rv-web*.war | tail -1)"; OPTS="$RV_OPTS"; ;;
	*) echo "Can not start Real Vehicle $1" >&2; exit 1; ;;
esac

CATALINA_BASE=$CPCC_DIR/work/$APP_CONTEXT_PATH;

[ -f $CATALINA_BASE/logs/jvm.pid ] && die "$APP_CONTEXT_PATH is already running.";

echo "Removing old setup in $CATALINA_BASE";
rm -rf $CATALINA_BASE/conf/* $CATALINA_BASE/webapps/* $CATALINA_BASE/temp/*;

for d in $CATALINA_BASE $CATALINA_BASE/conf $CATALINA_BASE/webapps $CATALINA_BASE/logs $CATALINA_BASE/work $CATALINA_BASE/temp; do ensureDir $d; done
cp $CPCC_DIR/conf/*.xml $CPCC_DIR/conf/*.properties $CATALINA_BASE/conf

OPTS="$OPTS -Duser.timezone=CET -Dfile.encoding=UTF-8 -Djava.awt.headless=true $CATALINA_OPTS";
OPTS="$OPTS -Dcatalina.base=$CATALINA_BASE -Dshutdown.port=${SHUTDOWN_PORT[$APP_CONTEXT_PATH]} -Dhttp.connector.port=${CONNECTOR_PORT[$APP_CONTEXT_PATH]}";
# OPTS="$OPTS -Dapp.base=webapps -Dapp.context.path=$APP_CONTEXT_PATH -Dapp.war.file=$APP_WAR_FILE -Ddb.directory=$DBDIR";
OPTS="$OPTS -Dapp.base=webapps -Dapp.context.path=$APP_CONTEXT_PATH -Dapp.war.file=$APP_WAR_FILE"
#OPTS="$OPTS -Dhibernate.dialect=org.hibernate.dialect.HSQLDialect -Ddb.driver=org.hsqldb.jdbc.JDBCDriver -Ddb.url=jdbc:hsqldb:file://${DBDIR}/${APP_CONTEXT_PATH}";
OPTS="$OPTS -Dhibernate.dialect=org.hibernate.dialect.H2Dialect -Ddb.driver=org.h2.Driver -Ddb.url=jdbc:h2:file:${DBDIR}/${APP_CONTEXT_PATH};MVCC=true;AUTOCOMMIT=OFF;RETENTION_TIME=0;CACHE_SIZE=65536";

CP="$CATALINA_BASE/conf";
CP="$CP:$LIBDIR/c3p0-${C3P0_VERSION}.jar";
CP="$CP:$LIBDIR/cpcc-demo-${CPCC_VERSION}.jar";
CP="$CP:$LIBDIR/h2-${COM_H2DB_VERSION}.jar";
#CP="$CP:$LIBDIR/hsqldb-${COM_HSQLDB_VERSION}.jar";
CP="$CP:$LIBDIR/liquibase-core-${LIQUIBASE_VERSION}.jar";
CP="$CP:$LIBDIR/mchange-commons-java-${MCHANGE_COMMONS_VERSION}.jar";
CP="$CP:$LIBDIR/snakeyaml-${SNAKEYAML_VERSION}.jar";
CP="$CP:$LIBDIR/tomcat-annotations-api-${TOMCAT_VERSION}.jar";
CP="$CP:$LIBDIR/tomcat-api-${TOMCAT_VERSION}.jar";
CP="$CP:$LIBDIR/tomcat-catalina-${TOMCAT_VERSION}.jar";
CP="$CP:$LIBDIR/tomcat-catalina-ant-${TOMCAT_VERSION}.jar";
CP="$CP:$LIBDIR/tomcat-catalina-ha-${TOMCAT_VERSION}.jar";
CP="$CP:$LIBDIR/tomcat-coyote-${TOMCAT_VERSION}.jar";
CP="$CP:$LIBDIR/tomcat-dbcp-${TOMCAT_VERSION}.jar";
CP="$CP:$LIBDIR/tomcat-el-api-${TOMCAT_VERSION}.jar";
CP="$CP:$LIBDIR/tomcat-jasper-${TOMCAT_VERSION}.jar";
CP="$CP:$LIBDIR/tomcat-jasper-el-${TOMCAT_VERSION}.jar";
CP="$CP:$LIBDIR/tomcat-jdbc-${TOMCAT_VERSION}.jar";
CP="$CP:$LIBDIR/tomcat-jni-${TOMCAT_VERSION}.jar";
CP="$CP:$LIBDIR/tomcat-jsp-api-${TOMCAT_VERSION}.jar";
CP="$CP:$LIBDIR/tomcat-juli-${TOMCAT_VERSION}.jar";
CP="$CP:$LIBDIR/tomcat-servlet-api-${TOMCAT_VERSION}.jar";
CP="$CP:$LIBDIR/tomcat-storeconfig-${TOMCAT_VERSION}.jar";
CP="$CP:$LIBDIR/tomcat-tribes-${TOMCAT_VERSION}.jar";
CP="$CP:$LIBDIR/tomcat-util-${TOMCAT_VERSION}.jar";
CP="$CP:$LIBDIR/tomcat-util-scan-${TOMCAT_VERSION}.jar";
CP="$CP:$LIBDIR/tomcat-websocket-api-${TOMCAT_VERSION}.jar";

[ -f "$LIBDIR/scrutiny-javaagent-1.2.1.jar" ] && AGENT="-javaagent:$LIBDIR/scrutiny-javaagent-1.2.1.jar=scrutiny.logs.dir=$CPCC_DIR/work/javaagent-$APP_CONTEXT_PATH"

cd $CATALINA_BASE
# exec "$JAVA_HOME/bin/java" -cp $CP $AGENT $OPTS org.apache.catalina.startup.Bootstrap start
"$JAVA_HOME/bin/java" -cp $CP $AGENT $OPTS org.apache.catalina.startup.Bootstrap start >> $CATALINA_BASE/logs/catalina.out 2>&1 &
echo $! > $CATALINA_BASE/logs/jvm.pid

echo "$APP_CONTEXT_PATH started as PID $(cat $CATALINA_BASE/logs/jvm.pid)";
