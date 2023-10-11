#!/bin/bash

help() {
  echo "$0 -t <github authentication token>"
  exit 1
}

while getopts ":t:h" opt ; do
  case "$opt" in
    t) GITHUB_TOKEN=${OPTARG} ;;
    h) help
       ;;
    \\?) help
         ;;
  esac
done

shift `expr $OPTIND - 1`

if [ -z "${GITHUB_TOKEN}" ]; then
  echo "Need to specify a github authentication token using (-t) or as an environment variable (GITHUB_TOKEN)"
  exit 1
fi

rm -rf ${HOME}/camel-github/* && \
clear

JAR_FILE=$(find target -maxdepth 1 -name "*.jar")
if [ ! -f "${JAR_FILE}" ]; then
  echo "Using jar file: ${JAR_FILE}"
fi

java -Dgithub.token=${GITHUB_TOKEN} -jar "${JAR_FILE}"
