package so.glad.client.http.impl.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Palmtale
 * 2015-03-25
 */
public final class NoncloseInputStream extends FilterInputStream {
    public NoncloseInputStream(InputStream in) {
        super(in);
    }

    @Override
    public void close() throws IOException {
    }

    public InputStream getInputStream() {
        return in;
    }
}
