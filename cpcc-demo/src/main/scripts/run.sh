#!/bin/sh

die () { echo "$*" >&2; exit 1; }

cd $(dirname $0)

v=$(java -version 2>&1 | awk -F\" 'NR == 1 {print $2}');
case "$v" in
	1.[89]*) ;;
	*) die "Can not use Java version '$v'. Please provide Java version 1.8 or newer!"; ;;
esac

CP=$(ls lib/*.jar | tr '\012' ':');

java -cp "$CP" cpcc.demo.launcher.Launcher
