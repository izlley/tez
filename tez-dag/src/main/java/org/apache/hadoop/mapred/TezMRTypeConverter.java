/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.mapred;

import org.apache.hadoop.mapreduce.TaskAttemptID;
import org.apache.hadoop.mapreduce.TaskType;
import org.apache.tez.engine.records.TezDAGID;
import org.apache.tez.engine.records.TezTaskAttemptID;
import org.apache.tez.engine.records.TezTaskID;
import org.apache.tez.engine.records.TezVertexID;

public class TezMRTypeConverter {

  // FIXME hack alert assumimg only one dag per application
  public static TaskAttemptID fromTez(TezTaskAttemptID attemptId) {
    TezTaskID taskId = attemptId.getTaskID();
    TezVertexID vertexId = taskId.getVertexID();
    TezDAGID dagId = vertexId.getDAGId();
    return new TaskAttemptID(
        Long.toString(dagId.getApplicationId().getClusterTimestamp()),
        dagId.getApplicationId().getId(),
        (vertexId.getId() == 0 ? TaskType.MAP : TaskType.REDUCE),
        taskId.getId(), attemptId.getId());
  }

}
