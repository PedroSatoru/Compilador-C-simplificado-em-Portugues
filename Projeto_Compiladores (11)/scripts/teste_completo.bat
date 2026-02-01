@echo off
set "ROOT=%~dp0.."
pushd "%ROOT%"
if not exist "build" mkdir "build"
if not exist "out" mkdir "out"

javac -d build src\*.java
java -cp build MainToken inputs\codigo.txt
java -cp build MainParser inputs\codigo.txt
if exist "out\saida.cpp" del /q "out\saida.cpp"
if exist "saida.txt" move /y "saida.txt" "out\saida.cpp"
g++ "out\saida.cpp" -o "out\programa.exe"
".\out\programa.exe"
popd