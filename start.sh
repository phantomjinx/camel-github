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
  echo "Need to specify a github authentitcation token using (-t) or as an environment variable"
  exit 1
fi

rm -rf ${HOME}/camel-github/* && \
clear && \
mvn clean \
  spring-boot:run \
  -D github.token=${GITHUB_TOKEN}
