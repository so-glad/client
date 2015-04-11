package so.glad.client.http.impl;

import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author Palmtale
 * 2015-03-25
 */
public final class SocketWrapper {
    private static final long DEFAULT_MAX_ALIVE_TIMEOUT = 1000L * 60;

    private Integer id;
    private String url;
    private Socket socket;
    private boolean isIdle;
    private long idleStartTime;
    private long createTime;
    private long maxAliveTimeout;
    private long idleTimeout;

    public SocketWrapper(Socket socket, String url) {
        this.socket = socket;
        isIdle = false;
        this.url = url;
        id = new Random().nextInt(1000);
        createTime = System.currentTimeMillis();
        maxAliveTimeout = DEFAULT_MAX_ALIVE_TIMEOUT;
        idleTimeout = -1;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public boolean isIdle() {
        return isIdle;
    }

    public void setIdle(boolean idle) {
        isIdle = idle;
        if (isIdle) {
            idleStartTime = System.currentTimeMillis();
        } else {
            idleStartTime = 0L;
        }
    }

    public long getIdleTime() {
        if (!isIdle()) {
            return -1L;
        }
        return System.currentTimeMillis() - idleStartTime;
    }

    public long getAliveTime() {
        return System.currentTimeMillis() - createTime;
    }

    public String getURL() {
        return url;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        int timeout = -1;
        try {
            timeout = socket.getSoTimeout();
        } catch (SocketException e) {
        }
        return "SocketWrapper{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", socket=" + socket +
                ", (timeout=" + timeout + ")" +
                ", isIdle=" + isIdle +
                ", idleStartTime=" + toString(idleStartTime) +
                ", createTime=" + toString(createTime) +
                ", maxAliveTimeout=" + maxAliveTimeout +
                '}';
    }

    private static String toString(long millisecond) {
        if(millisecond == 0L) {
            return "0";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss SSS");
        return simpleDateFormat.format(new Date(millisecond));
    }

    public long getMaxAliveTimeout() {
        return maxAliveTimeout;
    }

    public void setMaxAliveTimeout(long maxAliveTimeout) {
        this.maxAliveTimeout = maxAliveTimeout;
    }

    public boolean canAlive() {
        boolean canAlive = true;
        if (!this.isIdle()) {
            return true;
        }
        if(this.getIdleTime() > 0 && idleTimeout > 0) {
            canAlive = this.getIdleTime() < this.getIdleTimeout();
        }
        return canAlive && this.getAliveTime() < this.getMaxAliveTimeout();
    }

    public long getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(long idleTimeout) {
        this.idleTimeout = idleTimeout;
    }
}
