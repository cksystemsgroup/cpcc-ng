#!/bin/echo Source this script instead of running it!

export CPCC_DIR=$(pwd);
export DBDIR=$CPCC_DIR/db;
export LOGDIR=$CPCC_DIR/logs;
export LIBDIR=$CPCC_DIR/lib;
export WORKDIR=$CPCC_DIR/work;

export COM_H2DB_VERSION="${com.h2database.version}";
export COM_HSQLDB_VERSION="${com.hsqldb.version}";
export LIQUIBASE_VERSION="${liquibase-version}";
export SNAKEYAML_VERSION="${snakeyaml-version}";
export TOMCAT_VERSION="${tomcat8Version}";
export CPCC_VERSION="${project.parent.version}";
export C3P0_VERSION="${c3p0.version}";
export MCHANGE_COMMONS_VERSION="${mchange-commons-version}";
		
die () { echo "$*" >&2; exit 1; }

ensureDir () {
   [ -d "$1" ] && return 0;
   echo "Creating folder '$1'" >&2;
   mkdir -p "$1";
   [ -d "$1" ] && return 0;
   echo "Could not create folder '$1'" >&2;
   return 1;
}

v=$(java -version 2>&1 | awk -F\" 'NR == 1 {print $2}');
case "$v" in
	1.[89]*) ;;
	*) die "Can not use Java version '$v'. Please provide Java version 1.8 or newer!"; ;;
esac
