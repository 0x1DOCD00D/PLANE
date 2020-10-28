#!/bin/bash
STARTCONFLICT="^<<<<<<< HEAD"
SEPARATOR="^======="
ENDCONFLICT="^>>>>>>> "

if [ ! -f $1 ]; then
  echo "The input to $0 should be a filename"
  exit 1
fi
filename=$1
while read line; do
  echo $line
  if [[ $line =~ $STARTCONFLICT* ]]; then
    echo "It's there."
  fi
done <$filename
exit 0
