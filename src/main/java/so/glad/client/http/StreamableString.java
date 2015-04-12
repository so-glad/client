package so.glad.client.http;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Palmtale
 * 2015-03-25
 */
public final class StreamableString implements Streamable {
    private String string;
    private String charset;

    public StreamableString(String string, String charset) {
        this.string = string;
        this.charset = charset;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        IOUtils.copy(new ByteArrayInputStream(string.getBytes(charset)), outputStream);
    }
}
