#!/usr/bin/env python
# -*- coding: utf-8 -*-
from pyhive import presto
import socket
import time
import json

metric_list = []

metric_types = {
  "HeartbeatFailureDetector": "select activecount,failedcount,totalcount from jmx.current.\"com.facebook.presto.failureDetector:name=HeartbeatFailureDetector\"",
  "QueryManager": "select runningqueries,\"failedqueries.fiveminute.count\" as failedqueriesfiveminute,\"internalfailures.fiveminute.count\" as internalfailuresfiveminute,\"externalfailures.fiveminute.count\" as externalfailuresfiveminute,\"usererrorfailures.fiveminute.count\" as usererrorfailuresfiveminute,\"startedqueries.fiveminute.count\" as startedqueriesfiveminute,\"executiontime.fiveminutes.p50\" as executiontimefiveminute from jmx.current.\"com.facebook.presto.execution:name=QueryManager\"",
  "GeneralFreeMemory": "select FreeDistributedBytes,blockednodes,nodes from jmx.current.\"com.facebook.presto.memory:name=general,type=clustermemorypool\""}

cursor = presto.connect(host='', port=0).cursor()


def check_state(metric_info):
  global cursor
  cursor.execute(metric_info[1])
  columns = cursor.description
  for row in cursor:
    for (index, column) in enumerate(row):
      metric_list.append({
        "metric": "presto.{0}".format(metric_info[0]),
        "endpoint": socket.gethostname(),
        "timestamp": int(time.time()),
        "step": 60,
        "counterType": "GAUGE",
        "tags": "type={0}".format(columns[index][0]),
        "value": row[index]
      })

if __name__ == '__main__':
  try:
    map(check_state, metric_types.items())
    print(json.dumps(metric_list))
  except:
    cursor = presto.connect(host='', port=0).cursor()