package so.glad.client.http;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;

/**
 * @author Palmtale
 * 2015-03-25
 */
public final class StreamableReader implements Streamable {
    private Reader reader;
    private String charset;

    public StreamableReader(Reader reader, String charset) {
        this.reader = reader;
        this.charset = charset;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        OutputStreamWriter output = new OutputStreamWriter(outputStream, charset);
        IOUtils.copy(reader, output);
        output.flush();
    }
}
