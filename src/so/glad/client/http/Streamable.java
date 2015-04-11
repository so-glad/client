package so.glad.client.http;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Palmtale
 * 2015-03-25
 */
public interface Streamable {
    public void write(OutputStream outputStream) throws IOException;
}
