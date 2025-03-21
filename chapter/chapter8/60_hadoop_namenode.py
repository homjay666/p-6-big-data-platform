#!/usr/bin/env python
# This file is part of tcollector.
# Copyright (C) 2010  The tcollector Authors.
#
# This program is free software: you can redistribute it and/or modify it
# under the terms of the GNU Lesser General Public License as published by
# the Free Software Foundation, either version 3 of the License, or (at your
# option) any later version.  This program is distributed in the hope that it
# will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
# of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
# General Public License for more details.  You should have received a copy
# of the GNU Lesser General Public License along with this program.  If not,
# see <http://www.gnu.org/licenses/>.

import os
import sys
import time

try:
    import json
except ImportError:
    json = None

sys.path.append(os.path.join(os.path.dirname(__file__), '../../'))

import utils
from hadoop_http import HadoopHttp



REPLACEMENTS = {
    "fsnamesystemstate": ["fsnamesystem"],
    "fsnamesystem": ["fsnamesystem"]
}


class HadoopNameNode(HadoopHttp):
    """
    Class that will retrieve metrics from an Apache Hadoop DataNode's jmx page.

    This requires Apache Hadoop 1.0+ or Hadoop 2.0+.
    Anything that has the jmx page will work but the best results will com from Hadoop 2.1.0+
    """

    def __init__(self):
        super(HadoopNameNode, self).__init__('hadoop', 'namenode', utils.get_hostname(), 50070)

    def emit(self):
        step = os.path.realpath(__file__).split("/")[-1].split("_", 2)[0]
        current_time = int(time.time())
        metrics = self.poll(filter_modeler_types=["FSNamesystem", "JvmMetrics", "RpcActivityForPort", "RpcDetailedActivity"])
        for context, metric_name, value in metrics:
            for k, v in REPLACEMENTS.iteritems():
                if any(c.startswith(k) for c in context):
                    context = v
            self.emit_metric(context, current_time, metric_name, value, step)
        self.print_metric()


def main(args):
    utils.drop_privileges()
    if json is None:
        utils.err("This collector requires the `json' Python module.")
        return 13  # Ask tcollector not to respawn us
    name_node_service = HadoopNameNode()
    name_node_service.emit()


if __name__ == "__main__":
    sys.exit(main(sys.argv))