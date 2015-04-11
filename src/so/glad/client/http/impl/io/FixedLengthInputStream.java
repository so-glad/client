package so.glad.client.http.impl.io;

import org.slf4j.Logger;
import so.glad.client.http.Constant;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * FixedLengthInputStream is a inputstream wrapper to read specific
 * length data from the source stream
 *
 * @author Palmtale
 * 2015-03-25
 */
public final class FixedLengthInputStream extends FilterInputStream {
    private final Logger log = Constant.LOG;

    int availableLength;
    public FixedLengthInputStream(InputStream in, int length) {
        super(in);
        this.availableLength = length;
        if(log.isDebugEnabled()) {
            log.debug("InputStream " + in.getClass().getName());
        }
    }

    @Override
    public int read() throws IOException {
        //return super.read();
        if(availableLength > 0) {
            int result = super.read();
            if (result != -1) {
                availableLength = availableLength - 1;
            } else {
                availableLength = 0;
            }
            return result;
        } else {
            return -1;
        }
    }

    @Override
    public int read(byte[] b, int off, int length) throws IOException {
        if(availableLength <= 0) {
            return -1;
        }
        if(length > availableLength) {
            length = availableLength;
        }

        int result = super.read(b, off, length);
        if(log.isDebugEnabled()) {
            log.debug("Read Length>> " + result);
        }
        if(result > 0) {
            availableLength = availableLength - result;
        } else {
            availableLength = 0;
        }

        return result;
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    @Override
    public long skip(long n) throws IOException {
        n = n > availableLength ? availableLength : n;
        availableLength = availableLength - (int) n;
        return super.skip(n);
    }

    @Override
    public int available() throws IOException {
        return availableLength;
    }

    @Override
    public void mark(int readlimit) {
    }

    @Override
    public void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }

    @Override
    public boolean markSupported() {
        return false;
    }
}
