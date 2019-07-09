/*
 * Copyright (c) Teads 2018.
 */

package tv.teads.webviewhelper

/**
 * Utils constants
 */

internal object Constants {

    /**
     * Javascript interface name injected in the webview
     */
    val JAVASCRIPT_INTERFACE_TAG = "TeadsSDK"

    /**
     * Timeout in milliseconds to wait for JS callback after insertion in DOM
     */
    val WEBVIEW_TIMEOUT = 4 * 1000
}
