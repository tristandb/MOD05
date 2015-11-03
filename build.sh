#!/usr/bin/env bash

filedate="$(date +%Y%m%d%H%M%S)"
tmpdir="$(mktemp -d)"
curdir="$(pwd)"

cp ./prepare_pi.sh "$tmpdir"
cp ./picasender/spyinterface.py "$tmpdir"
cp ./target/AugmentedBreakout-1.0-SNAPSHOT-jar-with-dependencies.jar "$tmpdir"
cp ./run.sh "$tmpdir"
cd "$tmpdir"
tar -pczf "$curdir/Breakout-$filedate.tar.gz"  ./

rm -r "$tmpdir"
