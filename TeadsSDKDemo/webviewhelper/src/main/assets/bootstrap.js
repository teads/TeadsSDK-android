/**
 * @module Teads JS Utils for inRead
 * @date 03-2022
 * @copyright Teads <http://www.teads.tv>
 *
 * ⚠️ This bootstrap has been provided to give you a hand in your integration webview.
 * It's not designed to work on every integration, it may need to be customised to suit your needs
 *
 */

(function () {
  var verticalSpacer = 10;
  var showHideTimerDuration = 100;
  var intervalCheckPosition = 500;
  var opened = false;
  var bridge, teadsContainer, finalSize, intervalPosition, offset, heightSup, ratio, maxHeight;
  // command use to communicate with WebViewController JS Bridge
  var command = {
    trigger: {
      ready: "onTeadsJsLibReady",
      error: "handleError",
      position: "onSlotUpdated",
      startShow: "onSlotStartShow",
      startHide: "onSlotStartHide"
    },
    handler: {
      insert: "nativePlayerToJsInsertPlaceholder",
      update: "nativePlayerToJsUpdatePlaceholder",
      remove: "nativePlayerToJsRemovePlaceholder",
      show: "nativePlayerToJsShowPlaceholder",
      hide: "nativePlayerToJsHidePlaceholder",
      getCoo: "nativePlayerToJsGetTargetGeometry"
    }
  };
  var lastGeometry = {
    "top": 0,
    "left": 0,
    "bottom": 0,
    "right":  0,
    "ratio": 0
  }


  // The platform int
  var UNKNOWN_OS = 0;
  var ANDROID_OS = 1;
  var IOS_OS = 2;

  // The running platform int (ANDROID, IOS, UNKNOWN)
  var platformType;

  /*****************************************
   * Method called by WebViewController (through bridge) *
   *****************************************/

  // Check if WebViewController JS Bridge is present, set handler, and say JS Ready for WebViewController
  var sendJsLibReady = function () {
    platformType = getPlatformType();
    if (platformType === IOS_OS) {
      setBridgeHandler(window.webkit.messageHandlers);
    } else {
      setBridgeHandler(window.TeadsSDK);
    }
    bridge.callHandler(command.trigger.ready);
  };

  // find a slot on the document with selector and set placeholder (closed)
  var insertPlaceholder = function (selector) {
    var insertionSlot = findSlot(selector);

    if (insertionSlot) {
      setPlaceholderDiv(insertionSlot);
      // inform native about the placeholder geometry
      sendTargetGeometry();
    } else {
      bridge.callHandler(command.trigger.error, 'noSlotAvailable');
    }
  };

  var updatePlaceholder = function (data) {
    heightSup = data.offsetHeight;
    ratio = data.ratioVideo;
    maxHeight = data.maxHeight;
    // send to native the coo
    sendTargetGeometry();

    if (opened) {
      // if already opened when updating size, update placeholder to !
      showPlaceholder();
    }
  };

  // remove placeholder from document
  var removePlaceholder = function () {
    tryOrLog(function () {
      if (teadsContainer && teadsContainer.parentNode) {
        teadsContainer.parentNode.removeChild(teadsContainer);
      }
    }, 'removePlaceholder');
  };

  // open placeholder with transition
  var showPlaceholder = function () {
    tryOrLog(function () {
      opened = true;
      var heightPx = finalSize.height + "px";
      teadsContainer.style.height = heightPx;
      teadsContainer.style.minHeight = heightPx;  // Lock minimum height to prevent collapse during scroll
      // send status on native side
      bridge.callHandler(command.trigger.startShow);
      // start interval position check, if position change, informe native side
      intervalPosition = setInterval(checkPosition, intervalCheckPosition);
      // Add scroll listener for continuous updates with throttling
      window.addEventListener('scroll', onScrollEventImmediate, {passive: true});
    }, 'showPlaceholder');
  };

  // close placeholder with transition
  var hidePlaceholder = function () {
    tryOrLog(function () {
      opened = false;
      // set height to active transition
      teadsContainer.style.height = "0.1px";
      // send status on native side
      bridge.callHandler(command.trigger.startHide);
      // Remove scroll listeners when hiding
      window.removeEventListener('scroll', onScrollEvent);
      window.removeEventListener('scroll', onScrollEventImmediate);
    }, 'hidePlaceholder');
  };

  // send placeholder's coordinate to WebViewController for player positioning
  var sendTargetGeometry = function () {
    tryOrLog(function () {
      if (teadsContainer) {
        offset = getPageOffset(teadsContainer);

        var height = ratio ? (offset.w / ratio) + heightSup : 0;
        finalSize = {
          width: height > maxHeight ? (maxHeight - heightSup) * ratio : offset.w,
          height: height > maxHeight ? maxHeight : height
        };

        if(opened) {
          var heightPx = finalSize.height + "px";
          teadsContainer.style.height = heightPx;
          teadsContainer.style.minHeight = heightPx;  // Lock minimum height to prevent collapse
          teadsContainer.style.maxHeight = heightPx;  // Lock maximum height for stability
        }

        //Left margin is equal to the x offset + half of the delta between the
        //width offset and the real player width
        var leftMargin = offset.x + (offset.w - finalSize.width) / 2;
        var json = {
          "top": parseInt(offset.y),
          "left": parseInt(leftMargin),
          "bottom": parseInt(offset.y + finalSize.height),
          "right": parseInt(leftMargin + finalSize.width),
          "ratio": parseFloat(window.devicePixelRatio)
        };

        if (isEqualToLastGeometry(json)) return

        console.log("[JS] Sending geometry - top: " + json.top +
                    ", window.pageYOffset: " + window.pageYOffset +
                    ", teadsNativeScrollY: " + window.teadsNativeScrollY +
                    ", opened: " + opened);

        bridge.callHandler(command.trigger.position, json);
      }
    }, 'sendTargetGeometry');
  };

  /**************************
   *     Internal method    *
   **************************/

  var getPlatformType = function () {
    return tryOrLog(function () {
      if (/android/i.test(navigator.userAgent.toLowerCase())) {
        return ANDROID_OS;
      } else if (/iphone|ipad|ipod/i.test(navigator.userAgent.toLowerCase())) {
        return IOS_OS;
      } else { return UNKNOWN_OS; }
    }, 'getPlatformType');
  };

  // register handler on the WebViewController JS Bridge
  var setBridgeHandler = function (wvBridge) {
    tryOrLog(function () {
      bridge = wvBridge;
      window.utils = {
        insertPlaceholder: insertPlaceholder,
        updatePlaceholder: updatePlaceholder,
        showPlaceholder: showPlaceholder,
        hidePlaceholder: hidePlaceholder,
        removePlaceholder: removePlaceholder,
        sendTargetGeometry: sendTargetGeometry
      };

      bridge.callHandler = function (fct, params) {
        platformType = getPlatformType();
        if (this[fct]) {
          if (typeof params === 'object') {
            if (platformType === IOS_OS) {
              this[fct].postMessage(JSON.stringify(params));
            } else {
              var p = [];
              for (var i in params) {
                p.push(params[i]);
              }
              this[fct].apply(this, p);
            }
          } else {
            if (platformType === IOS_OS) {
              this[fct].postMessage(params);
            } else {
              this[fct].apply(this, params);
            }
          }
        } else {
          console.error(fct, 'not present on bridge', params)
        }
      };
    }, 'setBridgeHandler')
  };

  // set placeholder size, create it, and put it before "element" on document, then send coordinates to WebViewController
  var setPlaceholderDiv = function (element) {
    tryOrLog(function () {
      var parent = element.parentNode;

      teadsContainer = createTeadsContainer();

      parent.insertBefore(teadsContainer, element);
    }, 'setPlaceholderDiv')
  };

  // create and return a setted div
  var createTeadsContainer = function () {
    return tryOrLog(function () {
      var container = document.createElement("center");
      container.style.display = "block";  // Ensure block-level element that reserves space
      container.style.margin = verticalSpacer + "px auto " + verticalSpacer + "px auto";
      container.style.padding = "0";
      container.style.backgroundColor = "transparent";
      container.style.width = "100%";
      container.style.height = "0px";
      container.style.overflow = "hidden";  // Prevent content from flowing into ad space
      container.style.willChange = "height";  // Hint to browser to optimize height changes
      container.style.transition = "none";  // No animation, instant height changes
      container.style.boxSizing = "border-box";  // Include padding/border in height calculation
      container.style.position = "relative";  // Establish positioning context
      return container;
    }, 'createTeadsContainer')
  };

  // get element position on document (coordinate)
  var getPageOffset = function (element) {
    return tryOrLog(function () {
      var box = element.getBoundingClientRect();

      // Send viewport-relative position - native will calculate absolute position
      // using its own accurate scroll position
      var pos = {
        x: box.left,
        y: box.top,  // Position relative to viewport, not document
        w: box.right - box.left,
        h: box.bottom - box.top
      };
      return pos;
    }, 'getPageOffset')
  };

  // Initialize native scroll tracking
  window.teadsNativeScrollY = 0;

  // get the scroll of window
  var getDocumentScroll = function () {
    return tryOrLog(function () {
      // Use native scroll position if available (injected from Android native code)
      // This is necessary because window.pageYOffset returns 0 in Android WebView
      // when the native container is scrolling instead of the HTML document
      var scrollY = (typeof window.teadsNativeScrollY !== 'undefined')
        ? window.teadsNativeScrollY
        : window.pageYOffset;

      return {
        x: window.pageXOffset,
        y: scrollY
      }
    }, 'getDocumentScroll')
  };

  // find a slot in document with a CSS selector given by WebViewController (or automatic if no selector provided)
  var findSlot = function (selector) {
    return tryOrLog(function () {

      var items = document.querySelectorAll(selector);

      if (items.length) {
        return items[0]
      }
      return null;
    }, 'findSlot')
  };

  var checkPosition = function () {
    tryOrLog(function () {
      if (JSON.stringify(offset) !== JSON.stringify(getPageOffset(teadsContainer))) {
        sendTargetGeometry();
      }
    }, 'checkPosition')
  };

  var scrollEndTimer;
  var onScrollEvent = function() {
    // Send geometry update on scroll end (debounced)
    clearTimeout(scrollEndTimer);
    scrollEndTimer = setTimeout(function() {
      tryOrLog(function() {
        if (opened && teadsContainer) {
          sendTargetGeometry();
        }
      }, 'onScrollEvent');
    }, 100);
  };

  // Throttled scroll updates - send position frequently but not on every single frame
  var lastScrollUpdate = 0;
  var SCROLL_THROTTLE = 50; // ms - send updates every 50ms max (20 times per second)

  var onScrollEventImmediate = function() {
    tryOrLog(function() {
      var now = Date.now();
      if (opened && teadsContainer && (now - lastScrollUpdate) >= SCROLL_THROTTLE) {
        lastScrollUpdate = now;
        sendTargetGeometry();
      }
    }, 'onScrollEventImmediate');
  };

  var tryOrLog = function (cbk, fctName) {
    try {
      return cbk()
    } catch (e) {
      if (bridge && bridge.callHandler) {
        if (window.TeadsSDK || window.webkit.messageHandlers) {
          bridge.callHandler(command.trigger.error, fctName + ": " + e)
        }
      } else {
        console.error(fctName, e)
      }
    }
  }

    var lastGeometry = {
      "top": 0,
      "left": 0,
      "bottom": 0,
      "right":  0,
      "ratio": 0
    }

  var isEqualToLastGeometry = function(geometry) {
    if (lastGeometry.top === geometry.top &&
        lastGeometry.bottom === geometry.bottom &&
        lastGeometry.left === geometry.left &&
        lastGeometry.right === geometry.right &&
        lastGeometry.ratio === geometry.ratio)
        return true;

    lastGeometry = geometry;
    return false;
  }
  // START !
  sendJsLibReady();
})();
