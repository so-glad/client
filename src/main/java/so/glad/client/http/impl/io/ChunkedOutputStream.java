package so.glad.client.http.impl.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Implements HTTP chunking support. Writes are buffered to an internal buffer (2048 default size).
 * Chunks are guaranteed to be at least as large as the buffer size (except for the last chunk).
 *
 * @author Mohammad Rezaei, Goldman, Sachs & Co.
 */
public final class ChunkedOutputStream extends OutputStream {

    // ------------------------------------------------------- Static Variables
    private static final byte CRLF[] = new byte[] {(byte) 13, (byte) 10};

    /** End chunk */
    private static final byte ENDCHUNK[] = CRLF;

    /** 0 */
    private static final byte ZERO[] = new byte[] {(byte) '0'};

    // ----------------------------------------------------- Instance Variables
    private OutputStream stream = null;

    private byte[] cache;

    private int cachePosition = 0;

    private boolean wroteLastChunk = false;

    // ----------------------------------------------------------- Constructors
    /**
     * Wraps a stream and chunks the output.
     * @param stream to wrap
     * @param bufferSize minimum chunk size (excluding last chunk)
     * @throws IOException
     * 
     * @since 3.0
     */
    public ChunkedOutputStream(OutputStream stream, int bufferSize) throws IOException {
        this.cache = new byte[bufferSize];
        this.stream = stream;
    }

    /**
     * Wraps a stream and chunks the output. The default buffer size of 2048 was chosen because
     * the chunk overhead is less than 0.5%
     * @param stream
     * @throws IOException
     */
    public ChunkedOutputStream(OutputStream stream) throws IOException {
        this(stream, 2048);
    }

    // ----------------------------------------------------------- Internal methods
    /**
     * Writes the cache out onto the underlying stream
     * @throws IOException
     * 
     * @since 3.0
     */
    protected void flushCache() throws IOException {
        if (cachePosition > 0) {
            byte chunkHeader[] = (Integer.toHexString(cachePosition) + "\r\n").getBytes("US-ASCII");
            stream.write(chunkHeader, 0, chunkHeader.length);
            stream.write(cache, 0, cachePosition);
            stream.write(ENDCHUNK, 0, ENDCHUNK.length);
            cachePosition = 0;
        }
    }

    /**
     * Writes the cache and bufferToAppend to the underlying stream
     * as one large chunk
     * @param bufferToAppend
     * @param off
     * @param len
     * @throws IOException
     * 
     * @since 3.0
     */
    protected void flushCacheWithAppend(byte bufferToAppend[], int off, int len) throws IOException {
        byte chunkHeader[] = (Integer.toHexString(cachePosition + len) + "\r\n").getBytes("US-ASCII");
        stream.write(chunkHeader, 0, chunkHeader.length);
        stream.write(cache, 0, cachePosition);
        stream.write(bufferToAppend, off, len);
        stream.write(ENDCHUNK, 0, ENDCHUNK.length);
        cachePosition = 0;
    }

    protected void writeClosingChunk() throws IOException {
        // Write the final chunk.

        stream.write(ZERO, 0, ZERO.length);
        stream.write(CRLF, 0, CRLF.length);
        stream.write(ENDCHUNK, 0, ENDCHUNK.length);
    }

    // ----------------------------------------------------------- Public Methods
    /**
     * Must be called to ensure the internal cache is flushed and the closing chunk is written.
     * @throws IOException
     * 
     * @since 3.0
     */
    public void finish() throws IOException {
        if (!wroteLastChunk) {
            flushCache();
            writeClosingChunk();
            wroteLastChunk = true;
        }
    }

    // -------------------------------------------- OutputStream Methods
    /**
     * Write the specified byte to our output stream.
     * 
     * Note: Avoid this method as it will cause an inefficient single byte chunk. 
     * Use write (byte[], int, int) instead.
     * 
     * @param b The byte to be written
     * @throws IOException if an input/output error occurs
     */
    public void write(int b) throws IOException {
        cache[cachePosition] = (byte) b;
        cachePosition++;
        if (cachePosition == cache.length) flushCache();
    }

    /**
     * Writes the array. If the array does not fit within the buffer, it is
     * not split, but rather written out as one large chunk.
     * @param b
     * @throws IOException
     * 
     * @since 3.0
     */
    public void write(byte b[]) throws IOException {
        this.write(b, 0, b.length);
    }

    public void write(byte src[], int off, int len) throws IOException {
        if (len >= cache.length - cachePosition) {
            flushCacheWithAppend(src, off, len);
        } else {
            System.arraycopy(src, off, cache, cachePosition, len);
            cachePosition += len;
        }
    }

    /**
     * Flushes the underlying stream, but leaves the internal buffer alone.
     * @throws IOException
     */
    public void flush() throws IOException {
        stream.flush();
    }

    /**
     * Finishes writing to the underlying stream, but does NOT close the underlying stream.
     * @throws IOException
     */
    public void close() throws IOException {
        finish();
        super.close();
    }
}
