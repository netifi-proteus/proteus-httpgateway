#!/usr/bin/env bash
echo -e "TRAVIS_BRANCH=$TRAVIS_BRANCH"
echo -e "TRAVIS_TAG=$TRAVIS_TAG"
echo -e "TRAVIS_COMMIT=$TRAVIS_COMMIT"
echo -e "TRAVIS_PULL_REQUEST=$TRAVIS_PULL_REQUEST"

if [ "$TRAVIS_PULL_REQUEST" != "" ]; then
    # Pull Request
    echo -e "Build Pull Request #$TRAVIS_PULL_REQUEST => Branch [$TRAVIS_BRANCH]"
    ./gradlew clean build --stacktrace
elif [ "$TRAVIS_PULL_REQUEST" == "" ] && [ "$TRAVIS_BRANCH" == "develop" ] && [ "$TRAVIS_TAG" == "" ]; then
    # Develop Branch
    echo -e 'Build Branch with Snapshot => Branch ['$TRAVIS_BRANCH']'
    echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin netifi.azurecr.io
    export ORG_GRADLE_PROJECT_commitHash=${TRAVIS_COMMIT::7}
    ./gradlew clean build publish docker dockerPush --stacktrace
elif [ "$TRAVIS_PULL_REQUEST" == "" ] && [[ "$TRAVIS_BRANCH" == release/* ]] && [ "$TRAVIS_TAG" == "" ]; then
    # Release Branch
    echo -e 'Build Branch for Release => Branch ['$TRAVIS_BRANCH']'
    echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin netifi.azurecr.io
    export ORG_GRADLE_PROJECT_isRelease=true
    ./gradlew clean build publish docker dockerPush --stacktrace
elif [ "$TRAVIS_PULL_REQUEST" == "" ] && [ "$TRAVIS_BRANCH" == "master" ] && [ "$TRAVIS_TAG" != "" ]; then
    # Master Branch
    echo -e 'Build Master for Release => Branch ['$TRAVIS_BRANCH'] Tag ['$TRAVIS_TAG']'
    echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin netifi.azurecr.io
    export ORG_GRADLE_PROJECT_isRelease=true
    ./gradlew clean build publish docker dockerPush --stacktrace
else
    # Feature Branch
    echo -e 'Build Branch => Branch ['$TRAVIS_BRANCH']'
    ./gradlew clean build --stacktrace
fi