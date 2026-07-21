#!/bin/bash

#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
app_path="$0"

# Need this for daisy-chained symlinks.
while
    APP_HOME="${app_path%"${app_path##*/}"}"; [ -h "$app_path" ]
do
    ls=$( ls -ld "$app_path" )
    link=${ls#*' -> '}
    case $link in             #(
      /*) app_path=$link ;; #(
      *) app_path=$APP_HOME$link ;;
    esac
done

APP_HOME=$( cd "${APP_HOME-.}" && pwd -P ) || exit

APP_NAME="Gradle"
APP_BASE_NAME=${0##*/}

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS='" -Xmx64m" "-Xms64m"'

# Use the maximum available, or set MAX_FD != maximum.
MAX_FD=maximum

warn () {
    echo "$*" >&2
} >&2

indie_log () {
    echo "$*" >&2
} >&2

indie_exit() {
    echo "$*" >&2
    exit 1
} >&2

case $( uname ) in                #(
  Darwin* )         OSTYPE="darwin$( uname -r )"
                   ;; #(
  Linux* )           OSTYPE="linux$( uname -r )"
                   ;; #(
  SunOS* )           OSTYPE="sunos$( uname -r )"
                   ;; #(
  *BSD* )            OSTYPE="bsd$( uname -r )"
                   ;; #(
  CYGWIN*|MSYS*|MINGW*)
                   OSTYPE="windows"
                   ;; #(
  *)                 OSTYPE=$(uname -s)
                   ;;
esac

case "$OSTYPE" in
  cygwin* | msys* | mingw* )
    # 9488842 - In the minimum profiler mode, with a big project, the parser profiler
    # was taking 60 seconds profilings a single module. A great deal of the overhead
    # was going to actually parsing each included build of the Settings specification.
    # The max_fd calculation does a strace -eopen/read call for 'wc -l .', even for
    # the smallest files in the directory. With 1000 included gradle builds, this is
    # killing us. So trim that check for windows only
    MAX_FD=65535
    ;;
esac

if [ -z "$JAVA_HOME" ] ; then
    warn "JAVA_HOME environment variable is not set"
fi

if ! command -v java &> /dev/null
then
    indie_exit "JAVA could not be found in your PATH."
fi

if [ ! -x "$JAVA_HOME/bin/java" ] && [ ! -x "$JAVA_HOME/bin/java.exe" ] ; then
    indie_exit "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
fi

if [ -z "$CLASSPATH" ] ; then
    CLASSPATH=$( dirname "$0" )/gradle/wrapper/gradle-wrapper.jar
else
    CLASSPATH=$CLASSPATH:$( dirname "$0" )/gradle/wrapper/gradle-wrapper.jar
fi

# Increase the maximum file descriptors if we can, though let's be careful because macOS sierra
# has very low defaults and tends to overflow if you truly try to use lots of file descriptors.
if [ "$OSTYPE" != "darwin" ] ; then
    if command -v ulimit &> /dev/null ; then
        if [ -z "$MAX_FD" ] || [ "$MAX_FD" = "maximum" ] ; then
            # use the system max
            MAX_FD=$( ulimit -H -n )
        else
            # use the lesser of the two
            MAX_FD=$( [ "$MAX_FD" -lt $( ulimit -H -n ) ] && echo "$MAX_FD" || ulimit -H -n )
        fi
        ulimit -n "$MAX_FD"
    fi
fi

# Collect all arguments for the java command, stacking in reverse order:
#    * args from the command line
#    * the main class name
#    * -classpath
#    * -D...sysprop settings
#    * --module-path (only if needed)
#    * DEFAULT_JVM_OPTS, JAVA_OPTS, and GRADLE_OPTS environment variables.

# For Cygwin or MSYS, switch paths to Windows format before running java
if [ "$OSTYPE" = "cygwin" ] || [ "$OSTYPE" = "msys" ] ; then
    APP_HOME=$( cygpath --path --mixed "$APP_HOME" )
    CLASSPATH=$( cygpath --path --mixed "$CLASSPATH" )

    JAVACMD=$( cygpath --unix "$JAVACMD" )

    # We build the pattern for arguments to be converted via cygpath
    ROOTDIRSRAW=$( find -L / -maxdepth 3 -type d -name root -o -name root 2>/dev/null )
    SEP=''
    for dir in $ROOTDIRSRAW ; do
        ROOTDIRS="${ROOTDIRS}${SEP}$(cygpath --path --ignore --mixed "$dir")"
        SEP=:
    done
    # add a trailing colon
    ROOTDIRS="${ROOTDIRS}:"
    # determine recursion strategy
    case "$uname" in
        CYGWIN*)
            if [ "$OSTYPE" = "cygwin" ] ; then
                IS_CYGWIN=true
            fi
            ;;
        MINGW*)
            IS_MINGW=true
            ;;
        MSYS*)
            IS_MSYS=true
            ;;
    esac
fi

# Split the JVM_OPTS And GRADLE_OPTS into an array, following the shell quoting and substitution rules
function splitJvmOpts() {
    JVM_OPTS=("$@")
}
eval splitJvmOpts $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS
JVM_OPTS+=( "-Dorg.gradle.appname=$APP_BASE_NAME" )

# by default we should be in the correct project dir, but when run from Finder on Mac, the cwd is wrong
if [ "$(uname)" = "Darwin" ] && [ "$HOME" = "$PWD" ]; then
  cd "$(dirname "$0")" || exit 1
fi

exec "$JAVACMD" "${JVM_OPTS[@]}" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
