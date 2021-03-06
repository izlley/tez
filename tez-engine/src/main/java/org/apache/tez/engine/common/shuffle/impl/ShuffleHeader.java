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
package org.apache.tez.engine.common.shuffle.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;

/**
 * Shuffle Header information that is sent by the TaskTracker and 
 * deciphered by the Fetcher thread of Reduce task
 *
 */
@InterfaceAudience.Private
@InterfaceStability.Stable
public class ShuffleHeader implements Writable {

  /**
   * The longest possible length of task attempt id that we will accept.
   */
  private static final int MAX_ID_LENGTH = 1000;

  String mapId;
  long uncompressedLength;
  long compressedLength;
  int forReduce;
  
  public ShuffleHeader() { }
  
  public ShuffleHeader(String mapId, long compressedLength,
      long uncompressedLength, int forReduce) {
    this.mapId = mapId;
    this.compressedLength = compressedLength;
    this.uncompressedLength = uncompressedLength;
    this.forReduce = forReduce;
  }
  
  public long getUncompressedLength() {
    return uncompressedLength;
  }

  public long getCompressedLength() {
    return compressedLength;
  }

  public void readFields(DataInput in) throws IOException {
    mapId = WritableUtils.readStringSafely(in, MAX_ID_LENGTH);
    compressedLength = WritableUtils.readVLong(in);
    uncompressedLength = WritableUtils.readVLong(in);
    forReduce = WritableUtils.readVInt(in);
  }

  public void write(DataOutput out) throws IOException {
    Text.writeString(out, mapId);
    WritableUtils.writeVLong(out, compressedLength);
    WritableUtils.writeVLong(out, uncompressedLength);
    WritableUtils.writeVInt(out, forReduce);
  }
}
