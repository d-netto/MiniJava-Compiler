#!/bin/bash
touch tmp.s
java -cp "lib/antlr-4.9.2-complete.jar:bin/src" "Main" "$1" "tmp.s"
gcc -no-pie -O0 tmp.s -o tmp
./tmp
rm tmp tmp.s