#!/bin/bash

SCRIPTPATH=$( cd $(dirname $0) ; pwd -P )

cd $SCRIPTPATH/../
mkdir lib 2>/dev/null
mkdir lib/out 2>/dev/null
cd lib

if [[ -d "JSON-java" ]];then
	echo pulling JSON-java
	cd JSON-java
	git pull
	cd ..
else
	echo cloning JSON-java
	git clone https://github.com/douglascrockford/JSON-java.git
fi

echo compiling JSON-java
javac JSON-java/*.java -d out > out/JSON-java-build.txt
