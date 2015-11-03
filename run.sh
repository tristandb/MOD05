#!/usr/bin/env bash

INPUT_PIPE_NAME="breakoutInput"

INPUT_PIPE_LOCATION=$(mkfifo "/tmp/$INPUT_PIPE_NAME")
VIDEO_PIPE_LOCATION=$(mkfifo "/tmp/$VIDEO_PIPE_NAME")

./spyinterface "$INPUT_PIPE_LOCATION" "$VIDEO_PIPE_LOCATION" &

java -jar Breakout.jar --width=800 --height=600 --fullscreen=true --input=camera --pipename="$INPUT_PIPE_LOCATION" --camera="$VIDEO_PIPE_LOCATION"