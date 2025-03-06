#!/bin/sh
path=$PWD
export PYTHONPATH=$path/dependencies:$PYTHONPATH
/usr/bin/python test.py