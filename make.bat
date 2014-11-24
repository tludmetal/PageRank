@echo off

echo            ********************************************
echo            *      Page Rank Execution Program         *  
echo            ********************************************

set arg1=%1
set arg2=%2

javac .\src\com\sewn\pagerank\*.java .\src\PageRankCalculator.java

cd src
java PageRankCalculator %arg1% %arg2%
cd ..
