#!/bin/bash

if [ -z "$1" ]; then
  echo "Usage: ./launcher_test.sh NomDuTest"
fi

TEST_NAME="$1"

echo "Compiling test files..."

javac -cp "lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar:src" "src/Test/${TEST_NAME}.java"

echo "Running tests..."
java -cp "lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar:src" org.junit.runner.JUnitCore "Test.${TEST_NAME}"