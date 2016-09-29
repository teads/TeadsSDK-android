#!/bin/sh
set -xe

docker_name_friendly () { echo $1 | sed -e 's/[^a-zA-Z0-9_.-]/_/g' | awk '{print tolower($0)}'; }

IMAGE=$(echo ${JOB_NAME:-$(basename `pwd`)} | cut -d/ -f1 | sed "s/[^-]*-//" | sed "s/\_.*//")
IMAGE=$(docker_name_friendly $IMAGE)
BRANCH=${gitlabSourceBranch:-${GIT_BRANCH#remotes/}}
BRANCH=${BRANCH#origin/}
BRANCH=$(docker_name_friendly ${BRANCH:-$(git symbolic-ref --short HEAD)})
HASH=${GIT_COMMIT:-$(git rev-parse HEAD)}
TAG=$(docker_name_friendly ${BUILD_TAG:-$$})

# unify GNU/BSD xargs behavior
xargs -h 2>&1 | grep -q gnu && alias xargs='xargs -r'
hash timeout 2>/dev/null || { [ "$(uname)" = "Darwin" ] && hash gtimeout 2>/dev/null && alias timeout="gtimeout"; } || { echo "gtimeout not found: install coreutils via brew"; exit 1; }

# ephemeral containers should be prefixed with $TAG so that they are automatically dumped and destroyed
containers () { docker ps -a | awk '{print $NF}' | grep "^$TAG[a-zA-Z0-9_.-]*$"; }
dump_containers () { rm -rf ./containers && mkdir ./containers && containers | xargs -L1 sh -c 'docker logs $1 > ./containers/$1.log 2>./containers/$1.err' -- ; }
destroy_containers () { containers | xargs docker kill | xargs docker rm -f -v; }
cleanup () { trap '' INT; dump_containers; destroy_containers; }
trap cleanup EXIT TERM
trap true INT

DOCKER_REGISTRY='docker-registry.teads.net'

# common changes above this line should be done upstream #
##########################################################

# Make sure caches are accessible from the host
mkdir -p ~/.gradle && chmod g+s ~/.gradle

# Test and publish image locally
chmod g+s .
docker pull ${DOCKER_REGISTRY}/android:24.4.1
docker run --rm -i \
	-e "BUILD_ID=${BUILD_ID}" \
	-e "SLAVE_AAPT_TIMEOUT=60" \
	-v ~/.gradle:/root/.gradle \
	-v `pwd`:/opt/workspace:rw -w /opt/workspace \
	-v ${JENKINS_HOME}/${ANDROID_KEY_FILE}:/opt/workspace/${ANDROID_KEY_FILE} \
      ${DOCKER_REGISTRY}/android:24.4.1 \
      sh -c "cd TeadsSDKDemo && ./gradlew clean assembleRelease \
      -Pandroid.injected.signing.store.file=/opt/workspace/${ANDROID_KEY_FILE} \
      -Pandroid.injected.signing.store.password=${ANDROID_KEY_PASSWORD} \
      -Pandroid.injected.signing.key.alias=${ANDROID_KEY_ALIAS} \
      -Pandroid.injected.signing.key.password=${ANDROID_KEY_PASSWORD}"
