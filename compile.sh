#!/bin/bash
# compile.sh
find src/main/java -name "*.java" > sources.txt
javac -d out/ @sources.txt
rm sources.txt
echo "Build complete. Output in 'out/' directory."
