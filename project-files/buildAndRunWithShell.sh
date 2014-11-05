#!/bin/bash

SCRIPTPATH=$( cd $(dirname $0) ; pwd -P )

echo Running game

if [ "$1" = "-b" ]; then
	echo Building game first

	cd $SCRIPTPATH/../
	mkdir build 2> /dev/null
	cd src
	javac *.java -d ../build/
else
	echo "(-b to build and run)"
fi

if [ ! -d "$SCRIPTPATH/../build" ];then
	echo You must build first
else
	cd $SCRIPTPATH/../build
	java Main
fi

