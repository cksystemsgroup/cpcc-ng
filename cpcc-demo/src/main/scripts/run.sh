#!/bin/bash

die () { echo "$*" >&2; exit 1; }

export CPCC_DIR=$(dirname $(dirname $0));
cd $CPCC_DIR

v=$(java -version 2>&1 | awk -F\" 'NR == 1 {print $2}');
case "$v" in
	1.[89]*) ;;
	*) die "Can not use Java version '$v'. Please provide Java version 1.8 or newer!"; ;;
esac

# CP=$(ls lib/*.jar | tr '\012' ':');

# java -cp "$CP" cpcc.demo.launcher.Launcher

$CPCC_DIR/bin/start_db.sh || die "Can not start database.";

