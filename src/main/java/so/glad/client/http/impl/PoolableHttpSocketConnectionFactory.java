package so.glad.client.http.impl;

import so.glad.client.http.*;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Palmtale
 * 2015-03-25
 */
public class PoolableHttpSocketConnectionFactory extends BaseConnectionFactory implements PoolableHttpConnectionFactory {
    private ConcurrentHashMap<String, SocketPool> socketPools = new ConcurrentHashMap<String, SocketPool>();

    private int idleTimeout = DEFAULT_IDLE_TIMEOUT;

    private boolean idleCheck;

    private ScheduledExecutorService idleCheckScheduler;

    private SSLSocketFactory sslSocketFactory;

    private InetAddressFactory inetAddressFactory = new InetAddressFactoryImpl();

    public PoolableHttpSocketConnectionFactory() {
        idleCheck = false;
    }

    @Override
    public HttpConnection getHttpConnection(URLWrapper urlWrapper) throws IOException {
        PoolableHttpSocketConnection httpSocketConnection = new PoolableHttpSocketConnection();
        URL url = urlWrapper.getURL();
        SocketPool socketPool = socketPools.get(urlWrapper.getUrlString());
        if (socketPool == null) {
            socketPool = getSocketPool(urlWrapper);
        }

        httpSocketConnection.setSocketWrapper(socketPool.getSocketWrapper());
        httpSocketConnection.setPath(url.getPath());
        httpSocketConnection.setRequestParameters(HttpUtils.getParameters(url));
        httpSocketConnection.setPoolableHttpSocketConnectionFactory(this);
        return httpSocketConnection;
    }

    private synchronized SocketPool getSocketPool(URLWrapper urlWrapper) throws IOException {
        SocketPool socketPool = socketPools.get(urlWrapper.getUrlString());
        if (socketPool == null) {
            socketPool = new SocketPool((URLPoolSetting) getURLSetting(urlWrapper.getAlias()),
                    urlWrapper.getUrlString());
            socketPool.setSSLSocketFactory(this.getSSLSocketFactory());
            socketPools.put(urlWrapper.getUrlString(), socketPool);
        }
        socketPool.setSocketPoolSetting((URLPoolSetting) getURLSetting(urlWrapper.getAlias()));
        return socketPool;
    }

    public void release(SocketWrapper socketWrapper) {
        if (socketWrapper == null) {
            throw new IllegalArgumentException("Release socket is null");
        }
        socketPools.get(socketWrapper.getURL()).release(socketWrapper);
        if (!idleCheck) {
            scheduleIdleCheck();
        }
    }

    @Override
    public synchronized void setIdleTimeout(int second) {
        idleTimeout = second;
    }

    @Override
    public void destroy() {
        for (SocketPool socketPool : this.socketPools.values()) {
            socketPool.closeIdleSocket(-1);
        }
    }

    private synchronized void scheduleIdleCheck() {
        if (idleCheck) {
            return;
        }
        if (Constant.LOG.isDebugEnabled()) {
            Constant.LOG.debug("Idle socket checking scheduled at " + idleTimeout + " second delay");
        }

        Runnable runnable = new Runnable() {
            public void run() {
                closeIdleSocket();

                if (needIdleCheck()) {
                    scheduleIdleCheck();
                } else {
                    idleCheckScheduler.shutdown();
                    idleCheckScheduler = null;
                    idleCheck = false;
                    if (Constant.LOG.isDebugEnabled()) {
                        Constant.LOG.debug("No idle socket");
                    }
                }
            }
        };
        idleCheckScheduler = idleCheckScheduler == null ? Executors.newSingleThreadScheduledExecutor() : idleCheckScheduler;
        idleCheckScheduler.scheduleAtFixedRate(runnable, idleTimeout, idleTimeout/2, TimeUnit.SECONDS);
        idleCheck = true;
    }

    private boolean needIdleCheck() {
        if (Constant.LOG.isDebugEnabled()) {
            Constant.LOG.debug("Idle socket checking");
        }
        for (SocketPool socketPool : this.socketPools.values()) {
            if (socketPool.hasIdleSocket()) {
                return true;
            }
        }
        return false;
    }

    private synchronized void closeIdleSocket() {
        if (Constant.LOG.isDebugEnabled()) {
            Constant.LOG.debug("Idle socket closing");
        }
        for (SocketPool socketPool : this.socketPools.values()) {
            socketPool.closeIdleSocket(HttpUtils.getTimeoutMilliseconds(idleTimeout));
        }
    }

    public SSLSocketFactory getSSLSocketFactory() {
        return sslSocketFactory;
    }

    public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    @Override
    public URLSetting getDefaultSetting() {
        URLPoolSetting defaultSettingURL = (URLPoolSetting) this.defaultSetting;
        if (defaultSettingURL == null) {
            defaultSettingURL = new URLPoolSetting();
            defaultSettingURL.setTimeout(Constant.DEFAULT_TIMEOUT);
            defaultSettingURL.setConnectTimeout(Constant.DEFAULT_CONNECT_TIMEOUT);
            defaultSettingURL.setMaxSize(DEFAULT_CONNECTION_SIZE);
        }
        return defaultSettingURL;
    }

    public InetAddressFactory getInetAddressFactory() {
        return inetAddressFactory;
    }

    public void setInetAddressFactory(InetAddressFactory inetAddressFactory) {
        this.inetAddressFactory = inetAddressFactory;
    }
}
