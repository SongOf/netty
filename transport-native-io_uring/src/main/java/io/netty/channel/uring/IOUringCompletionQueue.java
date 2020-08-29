/*
 * Copyright 2020 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.channel.uring;

import io.netty.util.internal.PlatformDependent;

final class IOUringCompletionQueue {

  //these offsets are used to access specific properties
  //CQE (https://github.com/axboe/liburing/blob/master/src/include/liburing/io_uring.h#L162)
  private static final int CQE_USER_DATA_FIELD = 0;
  private static final int CQE_RES_FIELD = 8;
  private static final int CQE_FLAGS_FIELD = 12;

  private static final int CQE_SIZE = 16;

  private static final int IORING_ENTER_GETEVENTS = 1;

  //these unsigned integer pointers(shared with the kernel) will be changed by the kernel
  private final long kHeadAddress;
  private final long kTailAddress;
  private final long kringMaskAddress;
  private final long kringEntries;
  private final long kOverflowAddress;

  private final long completionQueueArrayAddress;

  private final int ringSize;
  private final long ringAddress;
  private final int ringFd;

  IOUringCompletionQueue(long kHeadAddress, long kTailAddress, long kringMaskAddress, long kringEntries,
      long kOverflowAddress, long completionQueueArrayAddress, int ringSize, long ringAddress, int ringFd) {
    this.kHeadAddress = kHeadAddress;
    this.kTailAddress = kTailAddress;
    this.kringMaskAddress = kringMaskAddress;
    this.kringEntries = kringEntries;
    this.kOverflowAddress = kOverflowAddress;
    this.completionQueueArrayAddress = completionQueueArrayAddress;
    this.ringSize = ringSize;
    this.ringAddress = ringAddress;
    this.ringFd = ringFd;
  }

  public int process(IOUringCompletionQueueCallback callback) throws Exception {
      int i = 0;
      for (;;) {
          long head = toUnsignedLong(PlatformDependent.getIntVolatile(kHeadAddress));
          if (head != toUnsignedLong(PlatformDependent.getInt(kTailAddress))) {
              long index = head & toUnsignedLong(PlatformDependent.getInt(kringMaskAddress));
              long cqe = index * CQE_SIZE + completionQueueArrayAddress;

              long udata = PlatformDependent.getLong(cqe + CQE_USER_DATA_FIELD);
              int res = PlatformDependent.getInt(cqe + CQE_RES_FIELD);
              long flags = toUnsignedLong(PlatformDependent.getInt(cqe + CQE_FLAGS_FIELD));

              //Ensure that the kernel only sees the new value of the head index after the CQEs have been read.
              PlatformDependent.putIntOrdered(kHeadAddress, (int) (head + 1));

              int fd = (int) (udata >> 32);
              int opMask = (int) udata;
              short op = (short) (opMask >> 16);
              short mask = (short) opMask;

              i++;
              if (!callback.handle(fd, res, flags, op, mask)) {
                  break;
              }
          } else {
              if (i == 0) {
                  return -1;
              }
              return i;
          }
      }
      return i;
  }

  interface IOUringCompletionQueueCallback {
      boolean handle(int fd, int res, long flags, int op, int mask);
  }

  public boolean ioUringWaitCqe() {
    //IORING_ENTER_GETEVENTS -> wait until an event is completely processed
    int ret = Native.ioUringEnter(ringFd, 0, 1, IORING_ENTER_GETEVENTS);
    if (ret < 0) {
        //Todo throw exception!
        return false;
    } else if (ret == 0) {
        return true;
    }
    //Todo throw Exception!
    return false;
  }

  public long getKHeadAddress() {
    return this.kHeadAddress;
  }

  public long getKTailAddress() {
    return this.kTailAddress;
  }

  public long getKringMaskAddress() {
    return this.kringMaskAddress;
  }

  public long getKringEntries() {
    return this.kringEntries;
  }

  public long getKOverflowAddress() {
    return this.kOverflowAddress;
  }

  public long getCompletionQueueArrayAddress() {
    return this.completionQueueArrayAddress;
  }

  public int getRingSize() {
    return this.ringSize;
  }

  public long getRingAddress() {
    return this.ringAddress;
  }

  //Todo Integer.toUnsignedLong -> maven checkstyle error
  public static long toUnsignedLong(int x) {
    return ((long) x) & 0xffffffffL;
  }
}