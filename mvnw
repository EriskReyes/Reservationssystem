#!/bin/sh
# ----------------------------------------------------------------------------
# Maven Start Up Batch script
# ----------------------------------------------------------------------------
if [ -z "$JAVA_HOME" ] ; then
  JAVACMD=`which java`
else
  JAVACMD="$JAVA_HOME/bin/java"
fi

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not set and no 'java' command could be found." >&2
  exit 1
fi

MAVEN_PROJECTBASEDIR="${MAVEN_BASEDIR:-$(pwd)}"
MAVEN_WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
MAVEN_WRAPPER_PROPERTIES="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.properties"

if [ -r "$MAVEN_WRAPPER_PROPERTIES" ]; then
  DISTRIBUTION_URL=$(grep "distributionUrl" "$MAVEN_WRAPPER_PROPERTIES" | sed 's/.*distributionUrl=//')
fi

if [ -r "$MAVEN_WRAPPER_JAR" ]; then
    exec "$JAVACMD" -jar "$MAVEN_WRAPPER_JAR" "$@"
fi

exec mvn "$@"
