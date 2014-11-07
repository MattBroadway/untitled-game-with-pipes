#!/bin/bash

SCRIPTPATH=$( cd $(dirname $0) ; pwd -P )
outDir="out"

echo Running game

if [ "$1" = "-b" ]; then
	echo Building game first

	cd $SCRIPTPATH/../
	mkdir $outDir 2> /dev/null
	cd src
	javac *.java -classpath $SCRIPTPATH/../lib/out/ -d ../$outDir/ 2> ../$outDir/lastBuild.log

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
# no-dereference so that the existing res link isn't followed,
# creating another link within the res directory
	ln --symbolic --force --no-dereference "../res/" res
	echo -----------------------------------------
	java -classpath "./:$SCRIPTPATH/../lib/out/" Main
fi

