package so.glad.client.http.impl;


import so.glad.client.http.Constant;
import so.glad.client.http.HttpUtils;
import so.glad.client.http.InetAddressFactory;
import so.glad.client.http.URLPoolSetting;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Palmtale
 * 2015-03-25
 */
public final class SocketPool {
    private SSLSocketFactory sslSocketFactory;
    private URLPoolSetting urlPoolSetting;
    private String url;

    private Queue<SocketWrapper> idleSocketWrappers;
    private int runningSocketCount;

    private long maxWaitTimeout = 1000L * 60 * 60;

    private long unit = 1000L * 2;

    private final Object lock;

    private InetAddressFactory inetAddressFactory = new InetAddressFactoryImpl();

    public SocketPool(URLPoolSetting urlPoolSetting, String url) {
        this.urlPoolSetting = urlPoolSetting;
        idleSocketWrappers = new LinkedList<SocketWrapper>();
        runningSocketCount = 0;
        this.url = url;
        lock = new Object();
    }

    public SocketWrapper getSocketWrapper() throws IOException {
        SocketWrapper socketWrapper = createOrGetSocketWrapper();
        if (socketWrapper != null) {
            return socketWrapper;
        }

        long waitTimeout = 0L;
        while (waitTimeout < getMaxWaitTimeout()) {
            socketWrapper = createOrGetSocketWrapper();
            if (socketWrapper != null) {
                return socketWrapper;
            }
            try {
                Thread.sleep(getUnit());
                waitTimeout = waitTimeout + getUnit();
            } catch (InterruptedException e) {
                throw new IllegalStateException("Wait for socket timeout", e);
            }
        }
        throw new IllegalStateException("Wait for socket timeout");
    }

    private SocketWrapper createOrGetSocketWrapper() throws IOException {
        synchronized (lock) {
            if (idleSocketWrappers.size() > 0) {
                if (Constant.LOG.isDebugEnabled()) {
                    Constant.LOG.debug("get socket from pool [" + idleSocketWrappers.size() + "]");
                }
                SocketWrapper socketWrapper = idleSocketWrappers.poll();
                if (!socketWrapper.canAlive()) {
                    closeSocket(socketWrapper);
                    return createOrGetSocketWrapper();
                }
                runningSocketCount++;
                socketWrapper.setIdle(false);
                if (Constant.LOG.isDebugEnabled()) {
                    Constant.LOG.debug(socketWrapper + " got from pool [" +
                            idleSocketWrappers.size() + "]");
                }
                return socketWrapper;
            } else if (runningSocketCount < urlPoolSetting.getMaxSize()) {
                if (Constant.LOG.isDebugEnabled()) {
                    Constant.LOG.debug("create socket at max [" + this.runningSocketCount + "]");
                }
                Socket socket = SocketUtils.createSocket(getSSLSocketFactory(), getInetAddressFactory(), new URL(url),
                        urlPoolSetting.getConnectTimeout());
                socket.setKeepAlive(true);
                socket.setSoTimeout(HttpUtils.getTimeoutMilliseconds(urlPoolSetting.getTimeout()));
                SocketWrapper socketWrapper = new SocketWrapper(socket, url);
                socketWrapper.setIdle(false);
                runningSocketCount++;
                if (Constant.LOG.isDebugEnabled()) {
                    Constant.LOG.debug(socketWrapper + " created for [" + this.runningSocketCount + "]");
                }
                return socketWrapper;
            }
        }
        return null;
    }

    public URLPoolSetting getSocketPoolSetting() {
        return urlPoolSetting;
    }

    public void setSocketPoolSetting(URLPoolSetting urlPoolSetting) {
        this.urlPoolSetting = urlPoolSetting;
    }

    public String getURL() {
        return url;
    }

    public void release(SocketWrapper socketWrapper) {
        Constant.LOG.debug("release the socket");
        synchronized (lock) {
            if (!socketWrapper.canAlive()) {
                Constant.LOG.debug("close socket out of max alive time");
                closeSocket(socketWrapper);
            } else {
                Constant.LOG.debug("return socket " + socketWrapper + " to pool [" + idleSocketWrappers.size() + "]");
                socketWrapper.setIdle(true);
                idleSocketWrappers.add(socketWrapper);
            }
            runningSocketCount = runningSocketCount - 1;
        }
    }

    public boolean hasIdleSocket() {
        if (Constant.LOG.isDebugEnabled()) {
            Constant.LOG.debug("Idle socket [" + idleSocketWrappers.size() + "] of pool [" + url + "]");
        }
        return idleSocketWrappers.size() > 0;
    }

    public void closeIdleSocket(int idleTimeout) {
        synchronized (lock) {
            SocketWrapper socketWrapper = idleSocketWrappers.peek();
            if (Constant.LOG.isDebugEnabled()) {
                Constant.LOG.debug("holding [" + idleSocketWrappers.size() + "] sockets of pool [" + url + "]");
            }
            while (socketWrapper != null && socketWrapper.getIdleTime() > idleTimeout) {
                socketWrapper = idleSocketWrappers.poll();
                closeSocket(socketWrapper);

                socketWrapper = idleSocketWrappers.peek();
            }
        }
    }

    private void closeSocket(SocketWrapper socketWrapper) {
        Socket socket = socketWrapper.getSocket();
        try {
            socket.close();
            if (Constant.LOG.isDebugEnabled()) {
                Constant.LOG.debug("close socket [" + socketWrapper + "]");
            }
        } catch (Exception e) {
            Constant.LOG.debug("close socket failed", e);
        }
        socketWrapper.setSocket(null);
    }

    public SSLSocketFactory getSSLSocketFactory() {
        return sslSocketFactory;
    }

    public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    public int getRunningSocket() {
        return runningSocketCount;
    }

    public int getIdleSocket() {
        return idleSocketWrappers.size();
    }

    public long getMaxWaitTimeout() {
        return maxWaitTimeout;
    }

    public void setMaxWaitTimeout(long maxWaitTimeout) {
        this.maxWaitTimeout = maxWaitTimeout;
    }

    public long getUnit() {
        return unit;
    }

    public void setUnit(long unit) {
        this.unit = unit;
    }

    public InetAddressFactory getInetAddressFactory() {
        return inetAddressFactory;
    }

    public void setInetAddressFactory(InetAddressFactory inetAddressFactory) {
        this.inetAddressFactory = inetAddressFactory;
    }
}
