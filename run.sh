#!/bin/sh

. ./jvm.conf

export NUMBEROFSYMBOL=100000
export LENGTH=3
export NUMBEROFTHREADS=1
export UNITOFWORK=1
export INDEX=65000
export ARGS="$NUMBEROFSYMBOL $LENGTH $NUMBEROFTHREADS $UNITOFWORK $INDEX"

export FLAGS="$COLLECTORS $MEMORY $GC_LOGGING $JITWATCH"
echo "Settings--->${FLAGS}"

#  GUI Version
java -classpath classes ${FLAGS} com.kodewerk.mastermind.MasterMindGUI $ARGS > mastermind.txt
