#!/usr/bin
# -*- coding:utf-8 -*-
import logging
from nagacore.context import Context


from pyspark.sql import SparkSession

def cmd_test():

  context = Context()
  logging.info('---------config:{0}-----------'.format(context.config))

  path = context.config.get('hdfs.file.path')
  hdfs = context.get_file_system()
  m = hdfs.ls(path)
  logging.info(path)
  print(m)

  spark = SparkSession.builder.enableHiveSupport().getOrCreate()
  logging.info("get spark session")

  p_files = spark.sparkContext.binaryFiles(path)
  logging.info("read files from hdfs :{0}".format(path))

  spark.stop()


if __name__ == '__main__':
  cmd_test()
