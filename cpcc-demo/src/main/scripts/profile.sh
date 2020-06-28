#!/bin/echo Source this script instead of running it!

cd $(dirname $0)/..

export CPCC_DIR=$(pwd);
export DBDIR=$CPCC_DIR/db;
export LOGDIR=$CPCC_DIR/logs;
export LIBDIR=$CPCC_DIR/lib;
export WORKDIR=$CPCC_DIR/work;
export SETUPS_DIR=$CPCC_DIR/setups;
export CONFIG=$CPCC_DIR/work/config.sh
export CUSTOM_CONFIG=$CPCC_DIR/work/custom-config.sh

export COM_H2DB_VERSION="${com.h2database.version}";
export LIQUIBASE_VERSION="${liquibase-version}";
export SNAKEYAML_VERSION="${snakeyaml-version}";
export TOMCAT_VERSION="${tomcat.version}";
export CPCC_VERSION="${project.parent.version}";
export MCHANGE_COMMONS_VERSION="${mchange-commons-version}";
export LOGBACK_VERSION="${logback-version}";
export SLF4J_VERSION="${slf4j-version}";
		
die   () { echo "[ERROR] $*" >&2; exit 1; }
info  () { echo "[INFO]  $*"; }
warn  () { echo "[WARN]  $*" >&2; }
error () { echo "[ERROR] $*" >&2; }

ensureDir () {
	[ -d "$1" ] && return 0;
	echo "Creating folder '$1'" >&2;
	mkdir -p "$1";
	[ -d "$1" ] && return 0;
	echo "Could not create folder '$1'" >&2;
	return 1;
}

askUser ()
{
	txt="$1";
	regexp="$2";
	default="$3";
	x=x;
	[ "${regexp#^}" != "${regexp}" ] && x='^x' && regexp="${regexp#^}";
	b=true
	while $b
	do
		if [ "$default" != "" ]
		then
			printf "$txt [$default]: " >&2;
			read answer < /dev/tty;
			[ "$answer" = "" ] && answer="$default";
		else
			printf "$txt: " >&2;
			read answer < /dev/tty;
		fi
		[ "$regexp" = "" ] && return;
		expr "x$answer" : "$x$regexp" > /dev/null 2>&1 && b=false;
	done
	echo "$answer";
}

v=$(java -version 2>&1 | awk -F\" 'NR == 1 {print $2}');
case "$v" in
	1.[89]*) ;;
	*) die "Can not use Java version '$v'. Please provide Java version 1.8 or newer!"; ;;
esac

[ -f "$CUSTOM_CONFIG" ] && info "Including custom configuration $CUSTOM_CONFIG" && . $CUSTOM_CONFIG;

[ -f "$CONFIG" -o "$1" = "no-setup" ] || $CPCC_DIR/bin/setup_demo.sh
[ -f "$CONFIG" ] && . $CONFIG;

