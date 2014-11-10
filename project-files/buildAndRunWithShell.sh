#!/bin/bash

SCRIPTPATH=$( cd $(dirname $0) ; pwd -P )
outDir="out"


while getopts ":lb" opt; do
case "$opt" in
	l)
		echo Updating libraries
		bash ./getLibs.sh
		;;
	b)
		echo Building the game

		cd $SCRIPTPATH/../
		mkdir $outDir 2> /dev/null
		cd src
		javac *.java -classpath $SCRIPTPATH/../lib/out/ -d ../$outDir/ 2> ../$outDir/lastBuild.log

		if [[ $? != 0 ]];then
			less ../$outDir/lastBuild.log
			echo There were errors in compilation. Aborting.

			exit
		fi
		;;
	\?)
		echo "invalid option"
		;;
esac
done

echo Running game

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

