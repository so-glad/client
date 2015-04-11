package so.glad.client.http;

/**
 * The URLSetting is a setting for a specific url alias,
 * used for HttpConnectionFactory to create HttpConnection
 * @author Palmtale
 * 2015-03-25
 * @see HttpConnectionFactory
 * @see HttpConnection
 */
public class URLSetting {
    private int timeout;
    private int connectTimeout;

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        URLSetting that = (URLSetting) o;

        if (connectTimeout != that.connectTimeout) return false;
        if (timeout != that.timeout) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = timeout;
        result = 31 * result + connectTimeout;
        return result;
    }
}
