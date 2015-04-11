package so.glad.client.http;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * URLWrapper is used to wrap the url and give it an alias
 *
 * @author Palmtale
 * 2015-03-25
 * @see HttpConnection
 */
public final class URLWrapper {
    String urlString;

    String alias;

    public URLWrapper(String urlString, String alias) {
        this.urlString = urlString.trim();
        this.alias = alias.trim();
    }

    public String getUrlString() {
        return urlString;
    }

    public String getAlias() {
        return alias;
    }

    public URL getURL() throws MalformedURLException {
        return new URL(urlString);
    }

    @Override
    public String toString() {
        return alias + "[" + urlString + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        URLWrapper that = (URLWrapper) o;

        if (alias != null ? !alias.equals(that.alias) : that.alias != null) return false;
        if (urlString != null ? !urlString.equals(that.urlString) : that.urlString != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = urlString != null ? urlString.hashCode() : 0;
        result = 31 * result + (alias != null ? alias.hashCode() : 0);
        return result;
    }
}
