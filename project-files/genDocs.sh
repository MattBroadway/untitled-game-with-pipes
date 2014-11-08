#!/bin/bash

SCRIPTPATH=$( cd $(dirname $0) ; pwd -P )

mkdir $SCRIPTPATH/../doc/jdoc 2> /dev/null

# -private: documents all attributes and methods, even private ones
# -use: generates a page for each class to say where it has been used
# -windowtitle: the title used for the html pages
javadoc -classpath "$SCRIPTPATH/../out" -d "$SCRIPTPATH/../doc/jdoc" -private -use -windowtitle PipesGame $SCRIPTPATH/../src/*.java

firefox $SCRIPTPATH/../doc/jdoc/index.html
