package so.glad.client.http;

/**
 * URLPoolSetting is a pool setting for a specific url,
 * used for PoolableHttpConnectionFactory
 *
 * @author Palmtale
 * 2015-03-25
 * @see PoolableHttpConnectionFactory
 */
public final class URLPoolSetting extends URLSetting {

    private int maxSize;

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        URLPoolSetting that = (URLPoolSetting) o;

        if (maxSize != that.maxSize) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + maxSize;
        return result;
    }
}
