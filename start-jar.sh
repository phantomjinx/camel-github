#!/bin/bash

help() {
  echo "$0 -t <github authentication token> [-b github branch] [-n github name] [-o github owner] [-r github request delay]"
  exit 1
}

clear

while getopts ":b:n:o:r:t:h" opt ; do
  case "$opt" in
    b) GITHUB_REPO_BRANCH = ${OPTARG} ;;
    n) GITHUB_REPO_NAME = ${OPTARG} ;;
    o) GITHUB_REPO_OWNER = ${OPTARG} ;;
    r) GITHUB_REQUEST_DELAY=${OPTARG} ;;
    t) GITHUB_TOKEN=${OPTARG} ;;
    h) help
       ;;
    \\?) help
         ;;
  esac
done

shift `expr $OPTIND - 1`

if [ -z "${GITHUB_TOKEN}" ]; then
  echo "Need to specify a github authentitcation token using (-t) or as an environment variable (GITHUB_TOKEN)"
  exit 1
fi

if [ -z "${GITHUB_REPO_BRANCH}" ]; then
  GITHUB_REPO_BRANCH=4.x
fi

if [ -z "${GITHUB_REPO_NAME}" ]; then
  GITHUB_REPO_NAME=hawtio
fi

if [ -z "${GITHUB_REPO_OWNER}" ]; then
  GITHUB_REPO_OWNER=hawtio
fi

if [ -z "${GITHUB_REQUEST_DELAY}" ]; then
  GITHUB_REQUEST_DELAY=1500
fi

echo "Github Repo Branch: ${GITHUB_REPO_BRANCH}"
echo "Github Repo Name: ${GITHUB_REPO_NAME}"
echo "Github Repo Owner: ${GITHUB_REPO_OWNER}"
echo "Github Request Delay: ${GITHUB_REQUEST_DELAY}"

rm -rf ${HOME}/camel-github/*

JAR_FILE=$(find target -maxdepth 1 -name "*.jar")
if [ ! -f "${JAR_FILE}" ]; then
  echo "Using jar file: ${JAR_FILE}"
fi

java \
  -Dgithub.repo.branch=${GITHUB_REPO_BRANCH} \
  -Dgithub.repo.name=${GITHUB_REPO_NAME} \
  -Dgithub.repo.owner=${GITHUB_REPO_OWNER} \
  -Dgithub.request.delay=${GITHUB_REQUEST_DELAY} \
  -Dgithub.token=${GITHUB_TOKEN} \
  -jar "${JAR_FILE}"
