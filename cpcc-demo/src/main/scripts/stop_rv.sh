#!/bin/sh
# -----------------------------------------------------------------------------
# @(#) stop_rv.sh - real vehicle stop script.
# -----------------------------------------------------------------------------
#
# Usage: start_rv.sh RV-name
#

cd $(dirname $0)/..;
   
. bin/profile.sh

[ "x$*" = "x" ] && die "Usage:  $(basename $0) RV-name";

APP_CONTEXT_PATH=$1;
case "$APP_CONTEXT_PATH" in
   GS01)  APP_WAR_FILE="$(ls $CPCC_DIR/war/cpcc-gs-web*.war | tail -1)"; IX=00; ;;
   RV0[1-7])  APP_WAR_FILE="$(ls $CPCC_DIR/war/cpcc-rv-web*.war | tail -1)"; IX=${1/RV/}; ;;
   *) echo "Can not stop Real Vehicle $1" >&2; exit 1; ;;
esac

CATALINA_BASE=$CPCC_DIR/work/$APP_CONTEXT_PATH;

[ -f $CATALINA_BASE/logs/jvm.pid ] || die "$APP_CONTEXT_PATH is not running.";

kill -TERM $(cat $CATALINA_BASE/logs/jvm.pid) \
   && echo "$APP_CONTEXT_PATH ($(cat $CATALINA_BASE/logs/jvm.pid)) stopped using SIGTERM." \
   || echo "$APP_CONTEXT_PATH was not running!";

rm -f $CATALINA_BASE/logs/jvm.pid
