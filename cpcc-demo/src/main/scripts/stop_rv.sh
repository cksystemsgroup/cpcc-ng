#!/bin/bash
# -----------------------------------------------------------------------------
# @(#) stop_rv.sh - real vehicle stop script.
# -----------------------------------------------------------------------------
#
# Usage: stop_rv.sh RV-name
#

. $(dirname $0)/profile.sh no-setup

[ "x$*" = "x" ] && die "Usage:  $(basename $0) RV-name";

APP_CONTEXT_PATH=$1;

[ "${RVS[$1]}" = "$1" ] || die "Real Vehicle $1 is not configured. Shutdown aborted.";

case "$APP_CONTEXT_PATH" in
	GS*) ;;
	RV*) ;;
	*) echo "Can not stop Real Vehicle $1" >&2; exit 1; ;;
esac

CATALINA_BASE=$CPCC_DIR/work/$APP_CONTEXT_PATH;

[ -f $CATALINA_BASE/logs/jvm.pid ] || die "$APP_CONTEXT_PATH is not running.";

PID=$(cat $CATALINA_BASE/logs/jvm.pid)
kill -TERM $PID \
	&& info "$APP_CONTEXT_PATH ($PID) stopped using SIGTERM." \
	|| info "$APP_CONTEXT_PATH was not running!";

counter=0;
while ps -p $PID -o comm= > /dev/null; do ((x++));
	[ $x -gt 10 ] && info "$APP_CONTEXT_PATH ($PID) stopped using SIGKILL." && kill -KILL $PID && break || sleep 1;
done 

rm -f $CATALINA_BASE/logs/jvm.pid
