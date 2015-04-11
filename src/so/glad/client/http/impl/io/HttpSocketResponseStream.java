package so.glad.client.http.impl.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import so.glad.client.http.Constant;
import so.glad.client.http.HttpConnection;

import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.GZIPInputStream;

/**
 * @author yangyang
 * @since 2009-3-17
 */
public final class HttpSocketResponseStream extends FilterInputStream {
    private static final Logger log = LoggerFactory.getLogger(HttpSocketResponseStream.class);
    private static final List<String> importantHeaders = new ArrayList<String>();

    static {
        importantHeaders.add(HttpConnection.HEADER_COOKIE);
        importantHeaders.add(HttpConnection.HEADER_CONTENT_TYPE);
        importantHeaders.add(HttpConnection.HEADER_CONTENT_LENGTH);
        importantHeaders.add(HttpConnection.HEADER_CONNECTION);
        importantHeaders.add(HttpConnection.HEADER_KEEP_ALIVE);
        importantHeaders.add(HttpConnection.HEADER_LOCATION);
        importantHeaders.add(HttpConnection.HEADER_WWW_AUTHENTICATE);
        importantHeaders.add(HttpConnection.HEADER_AUTHORIZATION);
        importantHeaders.add(HttpConnection.HEADER_ACCEPT_ENCODING);
        importantHeaders.add(HttpConnection.HEADER_ACCEPT);
        importantHeaders.add(HttpConnection.HEADER_USER_AGENT);
        importantHeaders.add(HttpConnection.HEADER_CONTENT_ENCODING);
        importantHeaders.add(HttpConnection.HEADER_TRANSFER_ENCODING);
        importantHeaders.add(HttpConnection.HEADER_SOAP_ACTION);
    }

    private int statusCode;
    private Properties headers;

    private boolean isHttpMetaDataReaded = false;

    private ByteArrayOutputStream buffer;

    public HttpSocketResponseStream(InputStream in) {
        super(in);
    }

    @Override
    public int read() throws IOException {
        readHttpMetaData();
        return super.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        readHttpMetaData();
        return super.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        readHttpMetaData();
        return super.read(b, off, len);
    }

    public int getStatusCode() throws IOException {
        readHttpMetaData();
        return statusCode;
    }

    public Properties getHeaders() throws IOException {
        readHttpMetaData();
        return headers;
    }

    private synchronized void readHttpMetaData() throws IOException {
        if (isHttpMetaDataReaded) {
            return;
        }
        buffer = new ByteArrayOutputStream(1024 * 4);
        if (log.isDebugEnabled()) {
            log.debug("HTTP Response Data");
        }

        String line = readLine(in);
        int skipNumber = 0;
        while (line == null || !line.startsWith("HTTP")) {
            if (log.isDebugEnabled()) {
                log.debug("Skip... " + line);
            }
            if(skipNumber > 20) {
                throw new IOException("Unexpected HTTP Response Data, StatusLine can not be found");
            }
            skipNumber++;
            line = readLine(in);
        }
        if (log.isDebugEnabled()) {
            log.debug("<<<< " + line);
        }
        int index = line.indexOf(" ");
        if (index > 0) {
            int lastIndex = line.indexOf(" ", index + 1);
            statusCode = Integer.parseInt(line.substring(index + 1, lastIndex > 0 ? lastIndex : line.length()));
        } else {
            throw new IllegalStateException("Invalid status line [" + line + "]");
        }

        headers = new Properties();
        line = readLine(in);
        while (line != null && !"".equals(line)) {
            if (log.isDebugEnabled()) {
                log.debug("<< " + line);
            }
            int headerNameIndex = line.indexOf(":");
            if (headerNameIndex > 0) {
                String headerName = line.substring(0, headerNameIndex);
                headerName = toStandardHeaderName(headerName);
                String headerValue = line.substring(headerNameIndex + 1).trim();
                if(headers.getProperty(headerName) != null) {
                    headerValue = headers.getProperty(headerName) + "; " + headerValue;
                }
                headers.setProperty(headerName, headerValue);
            }
            line = readLine(in);
        }
        buffer = null;

        if (headers.get(HttpConnection.HEADER_CONTENT_LENGTH) != null) {
            if (log.isDebugEnabled()) {
                log.debug("Length:" + headers.get(HttpConnection.HEADER_CONTENT_LENGTH));
            }
            in = new FixedLengthInputStream(in,
                    Integer.parseInt(headers.get(HttpConnection.HEADER_CONTENT_LENGTH).toString().trim()));
        } else if (headers.get(HttpConnection.HEADER_TRANSFER_ENCODING) != null
                && headers.getProperty(HttpConnection.HEADER_TRANSFER_ENCODING).equalsIgnoreCase(HttpConnection.HEADER_VALUE_TRANSFER_ENCODING_CHUNK)) {
            if (log.isDebugEnabled()) {
                log.debug("Chunk Stream");
            }
            in = new ChunkedInputStream(in);
        } else {
            log.warn("Illegal http socket response stream");
        }

        Object contentEncoding = headers.get(HttpConnection.HEADER_CONTENT_ENCODING);
        if (HttpConnection.HEADER_VALUE_CONTENT_ENCODING_GZIP.equals(contentEncoding)) {
            in = new GZIPInputStream(in);
        }
        isHttpMetaDataReaded = true;
    }

    private String readLine(InputStream inputStream) throws IOException {
        buffer.reset();
        byte b;
        b = (byte) inputStream.read();
        while (b != -1 && b != '\n') {
            buffer.write(b);
            b = (byte) inputStream.read();
        }
        if(buffer.size() < 1) {
            log.warn("Invalid stream byte [" + b + "]");
            return null;
        }
        String line = buffer.toString(Constant.HTTP_META_DEFAULT_CHARSET);
        if (line.charAt(line.length() - 1) == '\r') {
            line = line.substring(0, line.length() - 1);
        }
        return line;
    }

    public void clear() throws IOException {
        readHttpMetaData();
        if (in.read() > 0) {
            byte[] buffer = new byte[1024];
            for (int length = in.read(buffer); length > 0; length = in.read(buffer)) {
            }
        }
    }

    private String toStandardHeaderName(String headerName) {
        for (String importantHeader : importantHeaders) {
            if(headerName.equalsIgnoreCase(importantHeader)) {
                return importantHeader;
            }
        }
        return headerName;
    }
}
