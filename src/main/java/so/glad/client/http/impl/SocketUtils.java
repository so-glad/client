package so.glad.client.http.impl;


import so.glad.client.http.Constant;
import so.glad.client.http.HttpUtils;
import so.glad.client.http.InetAddressFactory;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

/**
 * @author Palmtale
 * 2015-03-25
 */
public abstract class SocketUtils {

	public static Socket createSocket(SSLSocketFactory sslSocketFactory, InetAddressFactory inetAddressFactory,
			URL url, int connnectTimeout) throws IOException {
		int port = url.getPort();
		String protocol = url.getProtocol();
		if (port == -1 && "http".equals(protocol)) {
			port = 80;
		} else if (port == -1 && "https".equals(protocol)) {
			port = 443;
		}

		try {
			return createSocket(protocol, sslSocketFactory, inetAddressFactory.get(url.getHost()), port,
					connnectTimeout);
		} catch (Exception e) {
			return createSocket(protocol, sslSocketFactory, inetAddressFactory.reset(url.getHost()), port,
					connnectTimeout);
		}
	}

	public static Socket createSocket(String protocol, SSLSocketFactory sslSocketFactory, InetAddress address,
			int port, int connnectTimeout) throws IOException {
		int timeoutMilliseconds = HttpUtils.getTimeoutMilliseconds(connnectTimeout);
		InetSocketAddress inetSocketAddress = new InetSocketAddress(address, port);

		Socket socket;
		if ("http".equals(protocol)) {
			socket = new Socket();
		} else if ("https".equals(protocol)) {
			socket = sslSocketFactory.createSocket();
		} else {
			throw new IllegalArgumentException("Illegal protocol [" + protocol + "]");
		}

		try {
			socket.connect(inetSocketAddress, timeoutMilliseconds);
		} catch (IOException ioe) {
			if (!socket.isClosed()) {
				try {
					socket.close();
				} catch (Exception e) {
					Constant.LOG.debug("Ignore Socket close exception", e);
				}
			}
			throw ioe;
		}
		return socket;
	}

}
