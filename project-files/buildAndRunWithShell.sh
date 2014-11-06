#!/bin/bash

SCRIPTPATH=$( cd $(dirname $0) ; pwd -P )
outDir="out"

echo Running game

if [ "$1" = "-b" ]; then
	echo Building game first

	cd $SCRIPTPATH/../
	mkdir $outDir 2> /dev/null
	cd src
	javac *.java -d ../$outDir/ 2> ../$outDir/lastBuild.log

	if [[ $? != 0 ]];then
		less ../$outDir/lastBuild.log
		echo There were errors in compilation. Aborting.

		exit
	fi
else
	echo "(-b to build and run)"
fi

if [ ! -d "$SCRIPTPATH/../$outDir" ];then
	echo You must build first
else
	cd $SCRIPTPATH/../$outDir
	echo -----------------------------------------
	java Main
fi

