package so.glad.client.http;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Palmtale
 * 2015-03-25
 */
public final class StreamableInputStream implements Streamable {
    private InputStream inputStream;

    public StreamableInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        IOUtils.copy(inputStream, outputStream);
        outputStream.flush();
    }
}
