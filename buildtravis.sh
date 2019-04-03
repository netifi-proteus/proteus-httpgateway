#!/usr/bin/env bash
echo -e "TRAVIS_BRANCH=$TRAVIS_BRANCH"
echo -e "TRAVIS_TAG=$TRAVIS_TAG"
echo -e "TRAVIS_COMMIT=${TRAVIS_COMMIT::7}"
echo -e "TRAVIS_PULL_REQUEST=$TRAVIS_PULL_REQUEST"

if [ "$TRAVIS_PULL_REQUEST" != "false" ]; then
    # Pull Request
    echo -e "Build Pull Request #$TRAVIS_PULL_REQUEST => Branch [$TRAVIS_BRANCH]"
    ./gradlew clean build --stacktrace --refresh-dependencies
elif [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "develop" ] && [ "$TRAVIS_TAG" == "" ]; then
    # Develop Branch
    echo -e 'Build Branch with Snapshot => Branch ['$TRAVIS_BRANCH']'
    echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
    export ORG_GRADLE_PROJECT_commitHash=${TRAVIS_COMMIT::7}
    ./gradlew -PversionSuffix=".BUILD-SNAPSHOT" clean build artifactoryPublish pushImage --stacktrace
elif [ "$TRAVIS_PULL_REQUEST" == "false" ] && [[ "$TRAVIS_BRANCH" == release/* ]] && [ "$TRAVIS_TAG" == "" ]; then
    # Release Branch
    echo -e 'Build Branch for Release => Branch ['$TRAVIS_BRANCH']'
    echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
    ./gradlew -PversionSuffix="-RC" clean build artifactoryPublish pushImage --stacktrace
elif [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then
    # Master Branch
    echo -e 'Build Master for Release => Branch ['$TRAVIS_BRANCH']'
    echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
    export ORG_GRADLE_PROJECT_isRelease=true
    ./gradlew clean build --stacktrace --refresh-dependencies
elif [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_TAG" != "" ]; then
    # Tag
    echo -e 'Build Tag for Release => Tag ['$TRAVIS_TAG']'
    echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
    export ORG_GRADLE_PROJECT_isRelease=true
    ./gradlew clean build bintrayUpload pushImage --stacktrace --refresh-dependencies
else
    # Feature Branch
    echo -e 'Build Branch => Branch ['$TRAVIS_BRANCH']'
    ./gradlew -PversionSuffix=".BUILD-SNAPSHOT" clean build artifactoryPublish pushImage --stacktrace --refresh-dependencies
fi
