package org.mitallast.queue.queue.transactional.mmap.data;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

public interface QueueMessageAppendSegment {

    void read(ByteBuf buffer, long offset, int length) throws IOException;

    long append(ByteBuf buffer) throws IOException;
}
