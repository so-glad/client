package so.glad.client;

import so.glad.client.http.Streamable;
import so.glad.serializer.StreamSerializer;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Palmtale
 * 2015-03-25
 * */
public class StreamableObject implements Streamable {

    private StreamSerializer streamSerializer;
    private Object object;
    private String encoding;

    public StreamableObject(StreamSerializer streamSerializer, Object object, String encoding) {
        this.streamSerializer = streamSerializer;
        this.object = object;
        this.encoding = encoding;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        streamSerializer.marshal(object, outputStream, encoding);
    }
}
