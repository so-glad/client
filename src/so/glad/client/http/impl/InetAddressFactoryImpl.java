package so.glad.client.http.impl;

import so.glad.client.http.InetAddressFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * InetAddressFactoryImpl
 *
 ** @author Palmtale
 * 2015-3-25
 */
public class InetAddressFactoryImpl implements InetAddressFactory {
    @Override
    public InetAddress get(String host) throws UnknownHostException {
        return InetAddress.getByName(host);
    }

    @Override
    public InetAddress reset(String host) throws UnknownHostException {
        InetAddress[] addresses = InetAddress.getAllByName(host);
        if(addresses.length < 1) {
            return null;
        }
        if(addresses.length == 1) {
            return addresses[0];
        }
        return addresses[1];
    }
}
