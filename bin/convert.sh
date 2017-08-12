#!/bin/bash
workdir=$1
input=$2
task=$3
ffmpeg -i $workdir/$input -c:v libx264 $workdir/$task.mp4
ffmpeg -i $workdir/$input -c:v libx264 $workdir/$task.wmv
ffmpeg -i $workdir/$input -c:v libx264 $workdir/$task.mov
ffmpeg -i $workdir/$input -c:v libx264 $workdir/$task.avi
