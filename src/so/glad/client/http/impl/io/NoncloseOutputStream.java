package so.glad.client.http.impl.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Palmtale
 * 2015-03-25
 */
public final class NoncloseOutputStream extends FilterOutputStream {
    public NoncloseOutputStream(OutputStream out) {
        super(out);
    }

    @Override
    public void close() throws IOException {
    }
}
