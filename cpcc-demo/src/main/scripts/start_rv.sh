#!/bin/sh
# -----------------------------------------------------------------------------
# @(#) start_rv.sh - Start Script Database
# -----------------------------------------------------------------------------
#
# Usage: start_rv.sh <RV name>
#

cd $(dirname $0);
   
. ./profile.sh

APP_CONTEXT_PATH=$1;
case "$APP_CONTEXT_PATH" in
   GS01)  APP_WAR_FILE="$(ls $CPCC_DIR/war/cpcc-gs-web*.war | tail -1)"; IX=00; ;;
   RV0[1-7])  APP_WAR_FILE="$(ls $CPCC_DIR/war/cpcc-rv-web*.war | tail -1)"; IX=${1/RV/}; ;;
   *) echo "Can not start Real Vehicle $1" >&2; exit 1; ;;
esac

CATALINA_BASE=$CPCC_DIR/work/$APP_CONTEXT_PATH;
rm -rf $CATALINA_BASE

for d in $CATALINA_BASE $CATALINA_BASE/conf $CATALINA_BASE/webapps $CATALINA_BASE/logs $CATALINA_BASE/work $CATALINA_BASE/temp; do ensureDir $d; done
cp $CPCC_DIR/conf/*.xml $CPCC_DIR/conf/*.properties $CATALINA_BASE/conf

OPTS="-Xmx300m -Xss256k -Duser.timezone=CET -Dfile.encoding=UTF-8 -Djava.awt.headless=true";
OPTS="$OPTS -Dcatalina.base=$CATALINA_BASE -Dshutdown.port=8${IX}5 -Dhttp.connector.port=8${IX}0"; 
# OPTS="$OPTS -Dapp.base=webapps -Dapp.context.path=$APP_CONTEXT_PATH -Dapp.war.file=$APP_WAR_FILE -Ddb.directory=$DBDIR -Dhibernate.dialect=org.hibernate.dialect.HSQLDialect";
OPTS="$OPTS -Dapp.base=webapps -Dapp.context.path=$APP_CONTEXT_PATH -Dapp.war.file=$APP_WAR_FILE"
#OPTS="$OPTS -Dhibernate.dialect=org.hibernate.dialect.HSQLDialect -Ddb.driver=org.hsqldb.jdbc.JDBCDriver -Ddb.url=jdbc:hsqldb:file://${DBDIR}/${APP_CONTEXT_PATH}";
OPTS="$OPTS -Dhibernate.dialect=org.hibernate.dialect.H2Dialect -Ddb.driver=org.h2.Driver -Ddb.url=jdbc:h2:file:${DBDIR}/${APP_CONTEXT_PATH};MVCC=true;AUTOCOMMIT=OFF";

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

exec "$JAVA_HOME/bin/java" -cp $CP $AGENT $OPTS org.apache.catalina.startup.Bootstrap start
