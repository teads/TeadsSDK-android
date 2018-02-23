/*
 * Copyright (c) Teads 2017.
 */

package tv.teads.webviewhelper;

/**
 * Utils constants
 * <p>
 * Created by Benjamin Volland on 28/08/2017.
 */

class Constants {

    /**
     * Javascript interface name injected in the webview
     */
    static final String JAVASCRIPT_INTERFACE_TAG = "TeadsSDK";

    /**
     * Timeout in milliseconds to wait for JS callback after insertion in DOM
     */
    static final int WEBVIEW_TIMEOUT = 4 * 1000;
}
