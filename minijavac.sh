#!/bin/bash

ant build

touch tmp.s
java -cp "lib/antlr-4.9.2-complete.jar:bin/src" "Main" "$1" "tmp.s"
gcc tmp.s
rm tmp.s