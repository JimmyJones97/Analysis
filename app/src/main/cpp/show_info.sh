#!/bin/sh
echo "\$0 = '$0'"
ii=1
for arg in "$@"; do
    echo "\$$ii = '$arg'"
    ii=`expr $ii + 1`
done
env