SOURCES="$(dirname $0)/TransemaBackend.java $(dirname $0)/TransemaKModule.java"
OUT_DIR="$(dirname $0)/out"
RESULT_JAR_FILE="transema-backend-0.0.1.jar"
KFRAMEWORK_JAR_DIR="/kframework/usr/lib/kframework/lib/java/"

rm -rf $OUT_DIR
mkdir $OUT_DIR

javac -cp "$(dirname $0)/../../../kframework/usr/lib/kframework/lib/java/*":. -d $OUT_DIR $SOURCES
cp -r META-INF $OUT_DIR
jar cvf $RESULT_JAR_FILE -C $OUT_DIR $(dirname $0)

export CLASSPATH=$(dirname $0)/$RESULT_JAR_FILE
