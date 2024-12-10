#!/bin/bash
SOURCES="transema/Main.java"
OUT_DIR="$(dirname $0)/out"
RESULT_JAR_FILE="transema-with-main"

rm -rf $OUT_DIR
mkdir $OUT_DIR

# javac -h "transema/codegen/native" -cp "$(dirname $0)/../kframework/usr/lib/kframework/lib/java/*":. $SOURCES
javac -cp "$(dirname $0)/../kframework/usr/lib/kframework/lib/java/*":"transema/*":. -d $OUT_DIR $SOURCES
jar cvf $RESULT_JAR_FILE -C $OUT_DIR $(dirname $0)