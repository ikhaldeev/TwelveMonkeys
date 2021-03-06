package com.twelvemonkeys.imageio.stream;

import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.ImageOutputStreamImpl;
import java.io.IOException;

import static com.twelvemonkeys.lang.Validate.notNull;

/**
 * ImageInputStream that writes through a delegate, but keeps local position and bit offset.
 * Note: Flushing or closing this stream will *not* have an effect on the delegate.
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @author last modified by $Author: harald.kuhr$
 * @version $Id: SubImageOutputStream.java,v 1.0 30/03/15 harald.kuhr Exp$
 */
public class SubImageOutputStream extends ImageOutputStreamImpl {
    private final ImageOutputStream stream;
    private final long startPos;

    public SubImageOutputStream(final ImageOutputStream stream) throws IOException {
        this.stream = notNull(stream, "stream");
        startPos = stream.getStreamPosition();
    }

    @Override
    public void seek(long pos) throws IOException {
        super.seek(pos);
        stream.seek(startPos + pos);
    }

    @Override
    public void write(int b) throws IOException {
        flushBits();

        stream.write(b);
        streamPos++;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        flushBits();
        stream.write(b, off, len);
        streamPos += len;
    }

    @Override
    public int read() throws IOException {
        bitOffset = 0;
        streamPos++;
        return stream.read();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        bitOffset = 0;
        int count = stream.read(b, off, len);
        streamPos += count;
        return count;
    }

    @Override
    public boolean isCached() {
        return stream.isCached();
    }

    @Override
    public boolean isCachedMemory() {
        return stream.isCachedMemory();
    }

    @Override
    public boolean isCachedFile() {
        return stream.isCachedFile();
    }
}
