#!/bin/sh

if [ -d classes ]
then
    mkdir classes
fi

javac -classpath classes -d classes src/main/java/com/kodewerk/mastermind/*.java src/main/java/com/kodewerk/math/*.java
