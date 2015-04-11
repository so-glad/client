package so.glad.client.http;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * InetAddressFactory
 *
 * @author Palmtale
 * 2015-03-25
 */
public interface InetAddressFactory {
    InetAddress get(String host) throws UnknownHostException;

    InetAddress reset(String host) throws UnknownHostException;
}
