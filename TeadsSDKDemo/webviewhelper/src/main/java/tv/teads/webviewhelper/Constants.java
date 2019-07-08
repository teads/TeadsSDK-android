/*
 * Copyright (c) Teads 2018.
 */

package tv.teads.webviewhelper;

/**
 * Utils constants
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
