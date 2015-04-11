package so.glad.client.http.impl;


import so.glad.client.http.Constant;
import so.glad.client.http.HttpConnectionFactory;
import so.glad.client.http.URLSetting;

import java.util.HashMap;

/**
 * @author Palmtale
 * @since 2015-03-25
 */
public abstract class BaseConnectionFactory implements HttpConnectionFactory {
    protected HashMap<String, URLSetting> settings;
    protected URLSetting defaultSetting;

    public HashMap<String, URLSetting> getSettings() {
        return settings;
    }

    public void setSettings(HashMap<String, URLSetting> settings) {
        this.settings = settings;
    }

    public synchronized URLSetting getDefaultSetting() {
        if(defaultSetting == null) {
            defaultSetting = new URLSetting();
            defaultSetting.setTimeout(Constant.DEFAULT_TIMEOUT);
            defaultSetting.setConnectTimeout(Constant.DEFAULT_CONNECT_TIMEOUT);
        }
        return defaultSetting;
    }

    public URLSetting getURLSetting(String alias) {
        URLSetting URLSetting;
        if(settings == null) {
            return getDefaultSetting();
        }
        URLSetting = this.getSettings().get(alias);
        if(URLSetting == null) {
            URLSetting = getDefaultSetting();
        }
        return URLSetting;
    }

    public synchronized void setDefaultSetting(URLSetting defaultSettingURL) {
        this.defaultSetting = defaultSettingURL;
    }
}
