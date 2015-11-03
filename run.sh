#!/usr/bin/env bash

INPUT_PIPE_NAME="/tmp/breakoutInput"

if [ -e "$INPUT_PIPE_NAME" ]; then
	rm "$INPUT_PIPE_NAME"
fi

mkfifo "$INPUT_PIPE_NAME"

java -jar Breakout.jar --width=800 --height=600 --fullscreen=true --input=camera --pipename="$INPUT_PIPE_LOCATION" &> ./breakout_java.log & python ./spyinterface "$INPUT_PIPE_LOCATION" --enablespi=false &> ./breakout_python.log &