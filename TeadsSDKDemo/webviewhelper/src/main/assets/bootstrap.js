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
  var intervalCheckPosition = 2000; // Only for DOM change detection (position is now stable)
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

  // Document marker for accurate position calculation
  // The marker stays at document origin (0,0) and allows atomic position calculation
  var documentMarker = null;

  /**
   * Creates a marker at document position (0,0) for accurate position calculation.
   * Using two simultaneous getBoundingClientRect() calls, we can calculate
   * true document-absolute position regardless of scroll state.
   */
  var createDocumentMarker = function() {
    if (documentMarker && documentMarker.parentNode) return documentMarker;

    // Create a wrapper with position:relative to establish positioning context
    var wrapper = document.createElement('div');
    wrapper.id = '__teads_marker_wrapper__';
    wrapper.style.cssText = 'position:relative;top:0;left:0;margin:0;padding:0;width:0;height:0;overflow:visible';

    // Create the marker inside the wrapper
    documentMarker = document.createElement('div');
    documentMarker.id = '__teads_position_marker__';
    documentMarker.style.cssText = 'position:absolute;top:0;left:0;width:0;height:0;visibility:hidden;pointer-events:none';

    wrapper.appendChild(documentMarker);

    // Insert as first child of body to ensure it's at document origin
    if (document.body.firstChild) {
      document.body.insertBefore(wrapper, document.body.firstChild);
    } else {
      document.body.appendChild(wrapper);
    }

    console.log("[JS] Document marker created at origin");
    return documentMarker;
  };

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
      // Stop interval position check if still running
      if (intervalPosition) {
        clearInterval(intervalPosition);
        intervalPosition = null;
      }
      cleanupLayoutObserver();
      stopScrollWatcher();
      // Clean up the document marker
      if (documentMarker && documentMarker.parentNode) {
        var wrapper = documentMarker.parentNode;
        if (wrapper.parentNode) {
          wrapper.parentNode.removeChild(wrapper);
        }
        documentMarker = null;
      }
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
      // start interval position check for DOM changes
      intervalPosition = setInterval(checkPosition, intervalCheckPosition);
      // Start scroll watcher for fresh position updates during scroll
      startScrollWatcher();
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
      // Stop interval position check
      if (intervalPosition) {
        clearInterval(intervalPosition);
        intervalPosition = null;
      }
      // Stop scroll watcher
      stopScrollWatcher();
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
          "scrollY": 0,  // Kept for API compatibility, not used in marker-based positioning
          "ratio": parseFloat(window.devicePixelRatio)
        };

        if (isEqualToLastGeometry(json)) return

        console.log("[JS] Sending document-absolute geometry - documentY: " + json.top +
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

      // Setup MutationObserver after slot is inserted
      setupLayoutObserver();
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
  // Uses MARKER-BASED calculation for TRUE document-absolute position
  // Both getBoundingClientRect() calls happen in the same JS frame (atomic)
  // so scroll cancels out: (documentY - scrollY) - (-scrollY) = documentY
  var getPageOffset = function (element) {
    return tryOrLog(function () {
      // Ensure marker exists at document origin
      var marker = createDocumentMarker();

      // ATOMIC: Both calls happen in the same JS execution frame
      var markerRect = marker.getBoundingClientRect();
      var elementRect = element.getBoundingClientRect();

      // markerRect.top = 0 - scrollY = -scrollY (marker is at document top)
      // elementRect.top = documentY - scrollY (element's viewport position)
      // Difference = (documentY - scrollY) - (-scrollY) = documentY
      // The scroll cancels out mathematically!
      var documentY = elementRect.top - markerRect.top;
      var documentX = elementRect.left - markerRect.left;

      var pos = {
        x: documentX,
        y: documentY,  // TRUE document-absolute (scroll-independent)
        w: elementRect.width,
        h: elementRect.height
      };
      return pos;
    }, 'getPageOffset')
  };

  // get the scroll of window (used for document-absolute position calculation)
  var getDocumentScroll = function () {
    return tryOrLog(function () {
      return {
        x: window.pageXOffset || 0,
        y: window.pageYOffset || document.documentElement.scrollTop || 0
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

  // With marker-based positioning, scroll tracking is no longer needed
  // The marker gives TRUE document-absolute position regardless of scroll state
  // Layout changes are detected by MutationObserver instead
  var startScrollWatcher = function() {
    // No-op: marker-based positioning is scroll-independent
  };

  var stopScrollWatcher = function() {
    // No-op: no scroll watcher to stop
  };

  // MutationObserver for layout change detection (images loading, DOM changes, etc.)
  var layoutObserver = null;
  var lastKnownViewportTop = 0;
  var layoutCheckTimer;

  var setupLayoutObserver = function() {
    if (!teadsContainer || layoutObserver) return;

    layoutObserver = new MutationObserver(function(mutations) {
      // Debounce: only check after mutations settle
      clearTimeout(layoutCheckTimer);
      layoutCheckTimer = setTimeout(function() {
        if (!teadsContainer || !opened) return;

        var box = teadsContainer.getBoundingClientRect();
        var newViewportTop = box.top;

        // Only notify native if viewport position changed significantly (>5px)
        // JS sends document-absolute position; native converts to viewport-relative using scrollY
        if (Math.abs(newViewportTop - lastKnownViewportTop) > 5) {
          console.log("[JS] Layout change detected - viewportTop changed from " + lastKnownViewportTop +
                      " to " + newViewportTop);
          lastKnownViewportTop = newViewportTop;
          sendTargetGeometry();
        }
      }, 100);
    });

    layoutObserver.observe(document.body, {
      childList: true,
      subtree: true,
      attributes: true,
      attributeFilter: ['style', 'class', 'src'] // Include 'src' for image loading
    });

    console.log("[JS] MutationObserver setup for layout change detection");
  };

  var cleanupLayoutObserver = function() {
    if (layoutObserver) {
      layoutObserver.disconnect();
      layoutObserver = null;
    }
    clearTimeout(layoutCheckTimer);
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
