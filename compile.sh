#!/bin/sh

export MVN=`which mvn | rev | cut -d'/' -f 1`

if [ "$MVN" = "nvm" ]
then 
  mvn clean verify
else
  if [ -d target ]
  then
    rm -rf target
  fi
  javac -classpath target/classes -d target/classes src/main/java/com/kodewerk/mastermind/*.java src/main/java/com/kodewerk/math/*.java
fi
