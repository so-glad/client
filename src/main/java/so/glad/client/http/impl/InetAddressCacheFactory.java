package so.glad.client.http.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import so.glad.client.http.InetAddressFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * InetSocketAddressFactory
 *
 * @author Palmtale
 * 2015-03-25
 */
public class InetAddressCacheFactory implements InetAddressFactory {
	
	private static Logger logger = LoggerFactory.getLogger(InetAddressCacheFactory.class);
    private Map<String, InetAddress> inetAddressMap =
            new ConcurrentHashMap<String, InetAddress>();

    @Override
    public InetAddress get(String host) throws UnknownHostException {
        InetAddress inetAddress = inetAddressMap.get(host);
        if(inetAddress != null) {
        	logger.debug("get address from cache for {}", host);
            return inetAddress;
        }
        InetAddress[] inetAddresses = InetAddress.getAllByName(host);
        if(inetAddresses == null || inetAddresses.length == 0) {
            return null;
        }
        inetAddress = inetAddresses[0];
        logger.info("init address {}: {}", host, inetAddress.getHostAddress());
        inetAddressMap.put(host, inetAddress);
        return inetAddress;
    }

    public void addInetAddress(InetAddress inetAddress) {
        inetAddressMap.put(inetAddress.getHostName(), inetAddress);
    }

    @Override
    public InetAddress reset(String host) throws UnknownHostException {
        InetAddress currentInetAddress = inetAddressMap.get(host);
        if(currentInetAddress == null) {
            return get(host);
        }

        InetAddress[] inetAddresses = InetAddress.getAllByName(host);
        if(inetAddresses == null || inetAddresses.length == 0) {
            return currentInetAddress;
        }

        InetAddress inetAddress = null;
        for (InetAddress address : inetAddresses) {
            if(!address.getHostAddress().equals(currentInetAddress.getHostAddress())) {
                inetAddress = address;
                break;
            }
        }
        if (inetAddress != null) {
        	logger.info("update address {}: {}", host, inetAddress.getHostAddress());
            inetAddressMap.put(host, inetAddress);
            return inetAddress;
        }
        return currentInetAddress;
    }

    public void resetAll() {
        for (Map.Entry<String, InetAddress> entry : inetAddressMap.entrySet()) {
            try {
                reset(entry.getKey());
            } catch (UnknownHostException ignored) {
            }
        }
    }

    public Runnable createResetRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                InetAddressCacheFactory.this.resetAll();
            }
        };
    }
}
