#!/bin/bash -e

NATIVE_IMAGE=$(env native-image || true)
[ -z "$NATIVE_IMAGE" ] && NATIVE_IMAGE=/home/yrodiere/tools/java/graalvm20.3/bin/native-image

mvn clean package

JAR=target/graalvm-methodhandle-1.0-SNAPSHOT.jar
NATIVE_EXEC=target/graalvm-methodhandle-runner

echo 1>&2 "==============================================="
echo 1>&2 "JVM test:"
java -jar $JAR

echo 1>&2 "==============================================="
echo 1>&2 "native-image compilation:"
$NATIVE_IMAGE -J-Dsun.nio.ch.maxUpdateArraySize=100 -J-Duser.language=en -J-Dfile.encoding=UTF-8 --initialize-at-build-time= -H:InitialCollectionPolicy=com.oracle.svm.core.genscavenge.CollectionPolicy\$BySpaceAndTime -H:+JNI -jar "$JAR" -H:FallbackThreshold=0 -H:+ReportExceptionStackTraces -H:-AddAllCharsets -H:EnableURLProtocols=http -H:NativeLinkerOption=-no-pie --no-server -H:-UseServiceLoaderFeature -H:+StackTrace "$NATIVE_EXEC"

echo 1>&2 "==============================================="
echo 1>&2 "native-image test:"
"$NATIVE_EXEC"
