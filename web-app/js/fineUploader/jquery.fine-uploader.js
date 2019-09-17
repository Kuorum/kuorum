// Fine Uploader 5.14.2 - (c) 2013-present Widen Enterprises, Inc. MIT licensed. http://fineuploader.com
(function(global) {
    (function($) {
        "use strict";
        var $el, pluginOptions = [ "uploaderType", "endpointType" ];
        function init(options) {
            var xformedOpts = transformVariables(options || {}), newUploaderInstance = getNewUploaderInstance(xformedOpts);
            uploader(newUploaderInstance);
            addCallbacks(xformedOpts, newUploaderInstance);
            return $el;
        }
        function getNewUploaderInstance(params) {
            var uploaderType = pluginOption("uploaderType"), namespace = pluginOption("endpointType");
            if (uploaderType) {
                uploaderType = uploaderType.charAt(0).toUpperCase() + uploaderType.slice(1).toLowerCase();
                if (namespace) {
                    return new miu[namespace]["FineUploader" + uploaderType](params);
                }
                return new miu["FineUploader" + uploaderType](params);
            } else {
                if (namespace) {
                    return new miu[namespace].FineUploader(params);
                }
                return new miu.FineUploader(params);
            }
        }
        function dataStore(key, val) {
            var data = $el.data("fineuploader");
            if (val) {
                if (data === undefined) {
                    data = {};
                }
                data[key] = val;
                $el.data("fineuploader", data);
            } else {
                if (data === undefined) {
                    return null;
                }
                return data[key];
            }
        }
        function uploader(instanceToStore) {
            return dataStore("uploader", instanceToStore);
        }
        function pluginOption(option, optionVal) {
            return dataStore(option, optionVal);
        }
        function addCallbacks(transformedOpts, newUploaderInstance) {
            var callbacks = transformedOpts.callbacks = {};
            $.each(newUploaderInstance._options.callbacks, function(prop, nonJqueryCallback) {
                var name, callbackEventTarget;
                name = /^on(\w+)/.exec(prop)[1];
                name = name.substring(0, 1).toLowerCase() + name.substring(1);
                callbackEventTarget = $el;
                callbacks[prop] = function() {
                    var originalArgs = Array.prototype.slice.call(arguments), transformedArgs = [], nonJqueryCallbackRetVal, jqueryEventCallbackRetVal;
                    $.each(originalArgs, function(idx, arg) {
                        transformedArgs.push(maybeWrapInJquery(arg));
                    });
                    nonJqueryCallbackRetVal = nonJqueryCallback.apply(this, originalArgs);
                    try {
                        jqueryEventCallbackRetVal = callbackEventTarget.triggerHandler(name, transformedArgs);
                    } catch (error) {
                        miu.log("Caught error in Fine Uploader jQuery event handler: " + error.message, "error");
                    }
                    if (nonJqueryCallbackRetVal != null) {
                        return nonJqueryCallbackRetVal;
                    }
                    return jqueryEventCallbackRetVal;
                };
            });
            newUploaderInstance._options.callbacks = callbacks;
        }
        function transformVariables(source, dest) {
            var xformed, arrayVals;
            if (dest === undefined) {
                if (source.uploaderType !== "basic") {
                    xformed = {
                        element: $el[0]
                    };
                } else {
                    xformed = {};
                }
            } else {
                xformed = dest;
            }
            $.each(source, function(prop, val) {
                if ($.inArray(prop, pluginOptions) >= 0) {
                    pluginOption(prop, val);
                } else if (val instanceof $) {
                    xformed[prop] = val[0];
                } else if ($.isPlainObject(val)) {
                    xformed[prop] = {};
                    transformVariables(val, xformed[prop]);
                } else if ($.isArray(val)) {
                    arrayVals = [];
                    $.each(val, function(idx, arrayVal) {
                        var arrayObjDest = {};
                        if (arrayVal instanceof $) {
                            $.merge(arrayVals, arrayVal);
                        } else if ($.isPlainObject(arrayVal)) {
                            transformVariables(arrayVal, arrayObjDest);
                            arrayVals.push(arrayObjDest);
                        } else {
                            arrayVals.push(arrayVal);
                        }
                    });
                    xformed[prop] = arrayVals;
                } else {
                    xformed[prop] = val;
                }
            });
            if (dest === undefined) {
                return xformed;
            }
        }
        function isValidCommand(command) {
            return $.type(command) === "string" && !command.match(/^_/) && uploader()[command] !== undefined;
        }
        function delegateCommand(command) {
            var xformedArgs = [], origArgs = Array.prototype.slice.call(arguments, 1), retVal;
            transformVariables(origArgs, xformedArgs);
            retVal = uploader()[command].apply(uploader(), xformedArgs);
            return maybeWrapInJquery(retVal);
        }
        function maybeWrapInJquery(val) {
            var transformedVal = val;
            if (val != null && typeof val === "object" && (val.nodeType === 1 || val.nodeType === 9) && val.cloneNode) {
                transformedVal = $(val);
            }
            return transformedVal;
        }
        $.fn.fineUploader = function(optionsOrCommand) {
            var self = this, selfArgs = arguments, retVals = [];
            this.each(function(index, el) {
                $el = $(el);
                if (uploader() && isValidCommand(optionsOrCommand)) {
                    retVals.push(delegateCommand.apply(self, selfArgs));
                    if (self.length === 1) {
                        return false;
                    }
                } else if (typeof optionsOrCommand === "object" || !optionsOrCommand) {
                    init.apply(self, selfArgs);
                } else {
                    $.error("Method " + optionsOrCommand + " does not exist on jQuery.fineUploader");
                }
            });
            if (retVals.length === 1) {
                return retVals[0];
            } else if (retVals.length > 1) {
                return retVals;
            }
            return this;
        };
    })(jQuery);
    (function($) {
        "use strict";
        var rootDataKey = "fineUploaderDnd", $el;
        function init(options) {
            if (!options) {
                options = {};
            }
            options.dropZoneElements = [ $el ];
            var xformedOpts = transformVariables(options);
            addCallbacks(xformedOpts);
            dnd(new miu.DragAndDrop(xformedOpts));
            return $el;
        }
        function dataStore(key, val) {
            var data = $el.data(rootDataKey);
            if (val) {
                if (data === undefined) {
                    data = {};
                }
                data[key] = val;
                $el.data(rootDataKey, data);
            } else {
                if (data === undefined) {
                    return null;
                }
                return data[key];
            }
        }
        function dnd(instanceToStore) {
            return dataStore("dndInstance", instanceToStore);
        }
        function addCallbacks(transformedOpts) {
            var callbacks = transformedOpts.callbacks = {};
            $.each(new miu.DragAndDrop.callbacks(), function(prop, func) {
                var name = prop, $callbackEl;
                $callbackEl = $el;
                callbacks[prop] = function() {
                    var args = Array.prototype.slice.call(arguments), jqueryHandlerResult = $callbackEl.triggerHandler(name, args);
                    return jqueryHandlerResult;
                };
            });
        }
        function transformVariables(source, dest) {
            var xformed, arrayVals;
            if (dest === undefined) {
                xformed = {};
            } else {
                xformed = dest;
            }
            $.each(source, function(prop, val) {
                if (val instanceof $) {
                    xformed[prop] = val[0];
                } else if ($.isPlainObject(val)) {
                    xformed[prop] = {};
                    transformVariables(val, xformed[prop]);
                } else if ($.isArray(val)) {
                    arrayVals = [];
                    $.each(val, function(idx, arrayVal) {
                        if (arrayVal instanceof $) {
                            $.merge(arrayVals, arrayVal);
                        } else {
                            arrayVals.push(arrayVal);
                        }
                    });
                    xformed[prop] = arrayVals;
                } else {
                    xformed[prop] = val;
                }
            });
            if (dest === undefined) {
                return xformed;
            }
        }
        function isValidCommand(command) {
            return $.type(command) === "string" && command === "dispose" && dnd()[command] !== undefined;
        }
        function delegateCommand(command) {
            var xformedArgs = [], origArgs = Array.prototype.slice.call(arguments, 1);
            transformVariables(origArgs, xformedArgs);
            return dnd()[command].apply(dnd(), xformedArgs);
        }
        $.fn.fineUploaderDnd = function(optionsOrCommand) {
            var self = this, selfArgs = arguments, retVals = [];
            this.each(function(index, el) {
                $el = $(el);
                if (dnd() && isValidCommand(optionsOrCommand)) {
                    retVals.push(delegateCommand.apply(self, selfArgs));
                    if (self.length === 1) {
                        return false;
                    }
                } else if (typeof optionsOrCommand === "object" || !optionsOrCommand) {
                    init.apply(self, selfArgs);
                } else {
                    $.error("Method " + optionsOrCommand + " does not exist in Fine Uploader's DnD module.");
                }
            });
            if (retVals.length === 1) {
                return retVals[0];
            } else if (retVals.length > 1) {
                return retVals;
            }
            return this;
        };
    })(jQuery);
    var miu = function(element) {
        "use strict";
        return {
            hide: function() {
                element.style.display = "none";
                return this;
            },
            attach: function(type, fn) {
                if (element.addEventListener) {
                    element.addEventListener(type, fn, false);
                } else if (element.attachEvent) {
                    element.attachEvent("on" + type, fn);
                }
                return function() {
                    miu(element).detach(type, fn);
                };
            },
            detach: function(type, fn) {
                if (element.removeEventListener) {
                    element.removeEventListener(type, fn, false);
                } else if (element.attachEvent) {
                    element.detachEvent("on" + type, fn);
                }
                return this;
            },
            contains: function(descendant) {
                if (!descendant) {
                    return false;
                }
                if (element === descendant) {
                    return true;
                }
                if (element.contains) {
                    return element.contains(descendant);
                } else {
                    return !!(descendant.compareDocumentPosition(element) & 8);
                }
            },
            insertBefore: function(elementB) {
                elementB.parentNode.insertBefore(element, elementB);
                return this;
            },
            remove: function() {
                element.parentNode.removeChild(element);
                return this;
            },
            css: function(styles) {
                if (element.style == null) {
                    throw new miu.Error("Can't apply style to node as it is not on the HTMLElement prototype chain!");
                }
                if (styles.opacity != null) {
                    if (typeof element.style.opacity !== "string" && typeof element.filters !== "undefined") {
                        styles.filter = "alpha(opacity=" + Math.round(100 * styles.opacity) + ")";
                    }
                }
                miu.extend(element.style, styles);
                return this;
            },
            hasClass: function(name, considerParent) {
                var re = new RegExp("(^| )" + name + "( |$)");
                return re.test(element.className) || !!(considerParent && re.test(element.parentNode.className));
            },
            addClass: function(name) {
                if (!miu(element).hasClass(name)) {
                    element.className += " " + name;
                }
                return this;
            },
            removeClass: function(name) {
                var re = new RegExp("(^| )" + name + "( |$)");
                element.className = element.className.replace(re, " ").replace(/^\s+|\s+$/g, "");
                return this;
            },
            getByClass: function(className, first) {
                var candidates, result = [];
                if (first && element.querySelector) {
                    return element.querySelector("." + className);
                } else if (element.querySelectorAll) {
                    return element.querySelectorAll("." + className);
                }
                candidates = element.getElementsByTagName("*");
                miu.each(candidates, function(idx, val) {
                    if (miu(val).hasClass(className)) {
                        result.push(val);
                    }
                });
                return first ? result[0] : result;
            },
            getFirstByClass: function(className) {
                return miu(element).getByClass(className, true);
            },
            children: function() {
                var children = [], child = element.firstChild;
                while (child) {
                    if (child.nodeType === 1) {
                        children.push(child);
                    }
                    child = child.nextSibling;
                }
                return children;
            },
            setText: function(text) {
                element.innerText = text;
                element.textContent = text;
                return this;
            },
            clearText: function() {
                return miu(element).setText("");
            },
            hasAttribute: function(attrName) {
                var attrVal;
                if (element.hasAttribute) {
                    if (!element.hasAttribute(attrName)) {
                        return false;
                    }
                    return /^false$/i.exec(element.getAttribute(attrName)) == null;
                } else {
                    attrVal = element[attrName];
                    if (attrVal === undefined) {
                        return false;
                    }
                    return /^false$/i.exec(attrVal) == null;
                }
            }
        };
    };
    (function() {
        "use strict";
        miu.canvasToBlob = function(canvas, mime, quality) {
            return miu.dataUriToBlob(canvas.toDataURL(mime, quality));
        };
        miu.dataUriToBlob = function(dataUri) {
            var arrayBuffer, byteString, createBlob = function(data, mime) {
                var BlobBuilder = window.BlobBuilder || window.WebKitBlobBuilder || window.MozBlobBuilder || window.MSBlobBuilder, blobBuilder = BlobBuilder && new BlobBuilder();
                if (blobBuilder) {
                    blobBuilder.append(data);
                    return blobBuilder.getBlob(mime);
                } else {
                    return new Blob([ data ], {
                        type: mime
                    });
                }
            }, intArray, mimeString;
            if (dataUri.split(",")[0].indexOf("base64") >= 0) {
                byteString = atob(dataUri.split(",")[1]);
            } else {
                byteString = decodeURI(dataUri.split(",")[1]);
            }
            mimeString = dataUri.split(",")[0].split(":")[1].split(";")[0];
            arrayBuffer = new ArrayBuffer(byteString.length);
            intArray = new Uint8Array(arrayBuffer);
            miu.each(byteString, function(idx, character) {
                intArray[idx] = character.charCodeAt(0);
            });
            return createBlob(arrayBuffer, mimeString);
        };
        miu.log = function(message, level) {
            if (window.console) {
                if (!level || level === "info") {
                    window.console.log(message);
                } else {
                    if (window.console[level]) {
                        window.console[level](message);
                    } else {
                        window.console.log("<" + level + "> " + message);
                    }
                }
            }
        };
        miu.isObject = function(variable) {
            return variable && !variable.nodeType && Object.prototype.toString.call(variable) === "[object Object]";
        };
        miu.isFunction = function(variable) {
            return typeof variable === "function";
        };
        miu.isArray = function(value) {
            return Object.prototype.toString.call(value) === "[object Array]" || value && window.ArrayBuffer && value.buffer && value.buffer.constructor === ArrayBuffer;
        };
        miu.isItemList = function(maybeItemList) {
            return Object.prototype.toString.call(maybeItemList) === "[object DataTransferItemList]";
        };
        miu.isNodeList = function(maybeNodeList) {
            return Object.prototype.toString.call(maybeNodeList) === "[object NodeList]" || maybeNodeList.item && maybeNodeList.namedItem;
        };
        miu.isString = function(maybeString) {
            return Object.prototype.toString.call(maybeString) === "[object String]";
        };
        miu.trimStr = function(string) {
            if (String.prototype.trim) {
                return string.trim();
            }
            return string.replace(/^\s+|\s+$/g, "");
        };
        miu.format = function(str) {
            var args = Array.prototype.slice.call(arguments, 1), newStr = str, nextIdxToReplace = newStr.indexOf("{}");
            miu.each(args, function(idx, val) {
                var strBefore = newStr.substring(0, nextIdxToReplace), strAfter = newStr.substring(nextIdxToReplace + 2);
                newStr = strBefore + val + strAfter;
                nextIdxToReplace = newStr.indexOf("{}", nextIdxToReplace + val.length);
                if (nextIdxToReplace < 0) {
                    return false;
                }
            });
            return newStr;
        };
        miu.isFile = function(maybeFile) {
            return window.File && Object.prototype.toString.call(maybeFile) === "[object File]";
        };
        miu.isFileList = function(maybeFileList) {
            return window.FileList && Object.prototype.toString.call(maybeFileList) === "[object FileList]";
        };
        miu.isFileOrInput = function(maybeFileOrInput) {
            return miu.isFile(maybeFileOrInput) || miu.isInput(maybeFileOrInput);
        };
        miu.isInput = function(maybeInput, notFile) {
            var evaluateType = function(type) {
                var normalizedType = type.toLowerCase();
                if (notFile) {
                    return normalizedType !== "file";
                }
                return normalizedType === "file";
            };
            if (window.HTMLInputElement) {
                if (Object.prototype.toString.call(maybeInput) === "[object HTMLInputElement]") {
                    if (maybeInput.type && evaluateType(maybeInput.type)) {
                        return true;
                    }
                }
            }
            if (maybeInput.tagName) {
                if (maybeInput.tagName.toLowerCase() === "input") {
                    if (maybeInput.type && evaluateType(maybeInput.type)) {
                        return true;
                    }
                }
            }
            return false;
        };
        miu.isBlob = function(maybeBlob) {
            if (window.Blob && Object.prototype.toString.call(maybeBlob) === "[object Blob]") {
                return true;
            }
        };
        miu.isXhrUploadSupported = function() {
            var input = document.createElement("input");
            input.type = "file";
            return input.multiple !== undefined && typeof File !== "undefined" && typeof FormData !== "undefined" && typeof miu.createXhrInstance().upload !== "undefined";
        };
        miu.createXhrInstance = function() {
            if (window.XMLHttpRequest) {
                return new XMLHttpRequest();
            }
            try {
                return new ActiveXObject("MSXML2.XMLHTTP.3.0");
            } catch (error) {
                miu.log("Neither XHR or ActiveX are supported!", "error");
                return null;
            }
        };
        miu.isFolderDropSupported = function(dataTransfer) {
            return dataTransfer.items && dataTransfer.items.length > 0 && dataTransfer.items[0].webkitGetAsEntry;
        };
        miu.isFileChunkingSupported = function() {
            return !miu.androidStock() && miu.isXhrUploadSupported() && (File.prototype.slice !== undefined || File.prototype.webkitSlice !== undefined || File.prototype.mozSlice !== undefined);
        };
        miu.sliceBlob = function(fileOrBlob, start, end) {
            var slicer = fileOrBlob.slice || fileOrBlob.mozSlice || fileOrBlob.webkitSlice;
            return slicer.call(fileOrBlob, start, end);
        };
        miu.arrayBufferToHex = function(buffer) {
            var bytesAsHex = "", bytes = new Uint8Array(buffer);
            miu.each(bytes, function(idx, byt) {
                var byteAsHexStr = byt.toString(16);
                if (byteAsHexStr.length < 2) {
                    byteAsHexStr = "0" + byteAsHexStr;
                }
                bytesAsHex += byteAsHexStr;
            });
            return bytesAsHex;
        };
        miu.readBlobToHex = function(blob, startOffset, length) {
            var initialBlob = miu.sliceBlob(blob, startOffset, startOffset + length), fileReader = new FileReader(), promise = new miu.Promise();
            fileReader.onload = function() {
                promise.success(miu.arrayBufferToHex(fileReader.result));
            };
            fileReader.onerror = promise.failure;
            fileReader.readAsArrayBuffer(initialBlob);
            return promise;
        };
        miu.extend = function(first, second, extendNested) {
            miu.each(second, function(prop, val) {
                if (extendNested && miu.isObject(val)) {
                    if (first[prop] === undefined) {
                        first[prop] = {};
                    }
                    miu.extend(first[prop], val, true);
                } else {
                    first[prop] = val;
                }
            });
            return first;
        };
        miu.override = function(target, sourceFn) {
            var super_ = {}, source = sourceFn(super_);
            miu.each(source, function(srcPropName, srcPropVal) {
                if (target[srcPropName] !== undefined) {
                    super_[srcPropName] = target[srcPropName];
                }
                target[srcPropName] = srcPropVal;
            });
            return target;
        };
        miu.indexOf = function(arr, elt, from) {
            if (arr.indexOf) {
                return arr.indexOf(elt, from);
            }
            from = from || 0;
            var len = arr.length;
            if (from < 0) {
                from += len;
            }
            for (;from < len; from += 1) {
                if (arr.hasOwnProperty(from) && arr[from] === elt) {
                    return from;
                }
            }
            return -1;
        };
        miu.getUniqueId = function() {
            return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function(c) {
                var r = Math.random() * 16 | 0, v = c == "x" ? r : r & 3 | 8;
                return v.toString(16);
            });
        };
        miu.ie = function() {
            return navigator.userAgent.indexOf("MSIE") !== -1 || navigator.userAgent.indexOf("Trident") !== -1;
        };
        miu.ie7 = function() {
            return navigator.userAgent.indexOf("MSIE 7") !== -1;
        };
        miu.ie8 = function() {
            return navigator.userAgent.indexOf("MSIE 8") !== -1;
        };
        miu.ie10 = function() {
            return navigator.userAgent.indexOf("MSIE 10") !== -1;
        };
        miu.ie11 = function() {
            return miu.ie() && navigator.userAgent.indexOf("rv:11") !== -1;
        };
        miu.edge = function() {
            return navigator.userAgent.indexOf("Edge") >= 0;
        };
        miu.safari = function() {
            return navigator.vendor !== undefined && navigator.vendor.indexOf("Apple") !== -1;
        };
        miu.chrome = function() {
            return navigator.vendor !== undefined && navigator.vendor.indexOf("Google") !== -1;
        };
        miu.opera = function() {
            return navigator.vendor !== undefined && navigator.vendor.indexOf("Opera") !== -1;
        };
        miu.firefox = function() {
            return !miu.edge() && !miu.ie11() && navigator.userAgent.indexOf("Mozilla") !== -1 && navigator.vendor !== undefined && navigator.vendor === "";
        };
        miu.windows = function() {
            return navigator.platform === "Win32";
        };
        miu.android = function() {
            return navigator.userAgent.toLowerCase().indexOf("android") !== -1;
        };
        miu.androidStock = function() {
            return miu.android() && navigator.userAgent.toLowerCase().indexOf("chrome") < 0;
        };
        miu.ios6 = function() {
            return miu.ios() && navigator.userAgent.indexOf(" OS 6_") !== -1;
        };
        miu.ios7 = function() {
            return miu.ios() && navigator.userAgent.indexOf(" OS 7_") !== -1;
        };
        miu.ios8 = function() {
            return miu.ios() && navigator.userAgent.indexOf(" OS 8_") !== -1;
        };
        miu.ios800 = function() {
            return miu.ios() && navigator.userAgent.indexOf(" OS 8_0 ") !== -1;
        };
        miu.ios = function() {
            return navigator.userAgent.indexOf("iPad") !== -1 || navigator.userAgent.indexOf("iPod") !== -1 || navigator.userAgent.indexOf("iPhone") !== -1;
        };
        miu.iosChrome = function() {
            return miu.ios() && navigator.userAgent.indexOf("CriOS") !== -1;
        };
        miu.iosSafari = function() {
            return miu.ios() && !miu.iosChrome() && navigator.userAgent.indexOf("Safari") !== -1;
        };
        miu.iosSafariWebView = function() {
            return miu.ios() && !miu.iosChrome() && !miu.iosSafari();
        };
        miu.preventDefault = function(e) {
            if (e.preventDefault) {
                e.preventDefault();
            } else {
                e.returnValue = false;
            }
        };
        miu.toElement = function() {
            var div = document.createElement("div");
            return function(html) {
                div.innerHTML = html;
                var element = div.firstChild;
                div.removeChild(element);
                return element;
            };
        }();
        miu.each = function(iterableItem, callback) {
            var keyOrIndex, retVal;
            if (iterableItem) {
                if (window.Storage && iterableItem.constructor === window.Storage) {
                    for (keyOrIndex = 0; keyOrIndex < iterableItem.length; keyOrIndex++) {
                        retVal = callback(iterableItem.key(keyOrIndex), iterableItem.getItem(iterableItem.key(keyOrIndex)));
                        if (retVal === false) {
                            break;
                        }
                    }
                } else if (miu.isArray(iterableItem) || miu.isItemList(iterableItem) || miu.isNodeList(iterableItem)) {
                    for (keyOrIndex = 0; keyOrIndex < iterableItem.length; keyOrIndex++) {
                        retVal = callback(keyOrIndex, iterableItem[keyOrIndex]);
                        if (retVal === false) {
                            break;
                        }
                    }
                } else if (miu.isString(iterableItem)) {
                    for (keyOrIndex = 0; keyOrIndex < iterableItem.length; keyOrIndex++) {
                        retVal = callback(keyOrIndex, iterableItem.charAt(keyOrIndex));
                        if (retVal === false) {
                            break;
                        }
                    }
                } else {
                    for (keyOrIndex in iterableItem) {
                        if (Object.prototype.hasOwnProperty.call(iterableItem, keyOrIndex)) {
                            retVal = callback(keyOrIndex, iterableItem[keyOrIndex]);
                            if (retVal === false) {
                                break;
                            }
                        }
                    }
                }
            }
        };
        miu.bind = function(oldFunc, context) {
            if (miu.isFunction(oldFunc)) {
                var args = Array.prototype.slice.call(arguments, 2);
                return function() {
                    var newArgs = miu.extend([], args);
                    if (arguments.length) {
                        newArgs = newArgs.concat(Array.prototype.slice.call(arguments));
                    }
                    return oldFunc.apply(context, newArgs);
                };
            }
            throw new Error("first parameter must be a function!");
        };
        miu.obj2url = function(obj, temp, prefixDone) {
            var uristrings = [], prefix = "&", add = function(nextObj, i) {
                var nextTemp = temp ? /\[\]$/.test(temp) ? temp : temp + "[" + i + "]" : i;
                if (nextTemp !== "undefined" && i !== "undefined") {
                    uristrings.push(typeof nextObj === "object" ? miu.obj2url(nextObj, nextTemp, true) : Object.prototype.toString.call(nextObj) === "[object Function]" ? encodeURIComponent(nextTemp) + "=" + encodeURIComponent(nextObj()) : encodeURIComponent(nextTemp) + "=" + encodeURIComponent(nextObj));
                }
            };
            if (!prefixDone && temp) {
                prefix = /\?/.test(temp) ? /\?$/.test(temp) ? "" : "&" : "?";
                uristrings.push(temp);
                uristrings.push(miu.obj2url(obj));
            } else if (Object.prototype.toString.call(obj) === "[object Array]" && typeof obj !== "undefined") {
                miu.each(obj, function(idx, val) {
                    add(val, idx);
                });
            } else if (typeof obj !== "undefined" && obj !== null && typeof obj === "object") {
                miu.each(obj, function(prop, val) {
                    add(val, prop);
                });
            } else {
                uristrings.push(encodeURIComponent(temp) + "=" + encodeURIComponent(obj));
            }
            if (temp) {
                return uristrings.join(prefix);
            } else {
                return uristrings.join(prefix).replace(/^&/, "").replace(/%20/g, "+");
            }
        };
        miu.obj2FormData = function(obj, formData, arrayKeyName) {
            if (!formData) {
                formData = new FormData();
            }
            miu.each(obj, function(key, val) {
                key = arrayKeyName ? arrayKeyName + "[" + key + "]" : key;
                if (miu.isObject(val)) {
                    miu.obj2FormData(val, formData, key);
                } else if (miu.isFunction(val)) {
                    formData.append(key, val());
                } else {
                    formData.append(key, val);
                }
            });
            return formData;
        };
        miu.obj2Inputs = function(obj, form) {
            var input;
            if (!form) {
                form = document.createElement("form");
            }
            miu.obj2FormData(obj, {
                append: function(key, val) {
                    input = document.createElement("input");
                    input.setAttribute("name", key);
                    input.setAttribute("value", val);
                    form.appendChild(input);
                }
            });
            return form;
        };
        miu.parseJson = function(json) {
            if (window.JSON && miu.isFunction(JSON.parse)) {
                return JSON.parse(json);
            } else {
                return eval("(" + json + ")");
            }
        };
        miu.getExtension = function(filename) {
            var extIdx = filename.lastIndexOf(".") + 1;
            if (extIdx > 0) {
                return filename.substr(extIdx, filename.length - extIdx);
            }
        };
        miu.getFilename = function(blobOrFileInput) {
            if (miu.isInput(blobOrFileInput)) {
                return blobOrFileInput.value.replace(/.*(\/|\\)/, "");
            } else if (miu.isFile(blobOrFileInput)) {
                if (blobOrFileInput.fileName !== null && blobOrFileInput.fileName !== undefined) {
                    return blobOrFileInput.fileName;
                }
            }
            return blobOrFileInput.name;
        };
        miu.DisposeSupport = function() {
            var disposers = [];
            return {
                dispose: function() {
                    var disposer;
                    do {
                        disposer = disposers.shift();
                        if (disposer) {
                            disposer();
                        }
                    } while (disposer);
                },
                attach: function() {
                    var args = arguments;
                    this.addDisposer(miu(args[0]).attach.apply(this, Array.prototype.slice.call(arguments, 1)));
                },
                addDisposer: function(disposeFunction) {
                    disposers.push(disposeFunction);
                }
            };
        };
    })();
    (function() {
        "use strict";
        if (typeof define === "function" && define.amd) {
            define(function() {
                return miu;
            });
        } else if (typeof module !== "undefined" && module.exports) {
            module.exports = miu;
        } else {
            global.miu = miu;
        }
    })();
    (function() {
        "use strict";
        miu.Error = function(message) {
            this.message = "[Fine Uploader " + miu.version + "] " + message;
        };
        miu.Error.prototype = new Error();
    })();
    miu.version = "5.14.2";
    miu.supportedFeatures = function() {
        "use strict";
        var supportsUploading, supportsUploadingBlobs, supportsFileDrop, supportsAjaxFileUploading, supportsFolderDrop, supportsChunking, supportsResume, supportsUploadViaPaste, supportsUploadCors, supportsDeleteFileXdr, supportsDeleteFileCorsXhr, supportsDeleteFileCors, supportsFolderSelection, supportsImagePreviews, supportsUploadProgress;
        function testSupportsFileInputElement() {
            var supported = true, tempInput;
            try {
                tempInput = document.createElement("input");
                tempInput.type = "file";
                miu(tempInput).hide();
                if (tempInput.disabled) {
                    supported = false;
                }
            } catch (ex) {
                supported = false;
            }
            return supported;
        }
        function isChrome21OrHigher() {
            return (miu.chrome() || miu.opera()) && navigator.userAgent.match(/Chrome\/[2][1-9]|Chrome\/[3-9][0-9]/) !== undefined;
        }
        function isChrome14OrHigher() {
            return (miu.chrome() || miu.opera()) && navigator.userAgent.match(/Chrome\/[1][4-9]|Chrome\/[2-9][0-9]/) !== undefined;
        }
        function isCrossOriginXhrSupported() {
            if (window.XMLHttpRequest) {
                var xhr = miu.createXhrInstance();
                return xhr.withCredentials !== undefined;
            }
            return false;
        }
        function isXdrSupported() {
            return window.XDomainRequest !== undefined;
        }
        function isCrossOriginAjaxSupported() {
            if (isCrossOriginXhrSupported()) {
                return true;
            }
            return isXdrSupported();
        }
        function isFolderSelectionSupported() {
            return document.createElement("input").webkitdirectory !== undefined;
        }
        function isLocalStorageSupported() {
            try {
                return !!window.localStorage && miu.isFunction(window.localStorage.setItem);
            } catch (error) {
                return false;
            }
        }
        function isDragAndDropSupported() {
            var span = document.createElement("span");
            return ("draggable" in span || "ondragstart" in span && "ondrop" in span) && !miu.android() && !miu.ios();
        }
        supportsUploading = testSupportsFileInputElement();
        supportsAjaxFileUploading = supportsUploading && miu.isXhrUploadSupported();
        supportsUploadingBlobs = supportsAjaxFileUploading && !miu.androidStock();
        supportsFileDrop = supportsAjaxFileUploading && isDragAndDropSupported();
        supportsFolderDrop = supportsFileDrop && isChrome21OrHigher();
        supportsChunking = supportsAjaxFileUploading && miu.isFileChunkingSupported();
        supportsResume = supportsAjaxFileUploading && supportsChunking && isLocalStorageSupported();
        supportsUploadViaPaste = supportsAjaxFileUploading && isChrome14OrHigher();
        supportsUploadCors = supportsUploading && (window.postMessage !== undefined || supportsAjaxFileUploading);
        supportsDeleteFileCorsXhr = isCrossOriginXhrSupported();
        supportsDeleteFileXdr = isXdrSupported();
        supportsDeleteFileCors = isCrossOriginAjaxSupported();
        supportsFolderSelection = isFolderSelectionSupported();
        supportsImagePreviews = supportsAjaxFileUploading && window.FileReader !== undefined;
        supportsUploadProgress = function() {
            if (supportsAjaxFileUploading) {
                return !miu.androidStock() && !miu.iosChrome();
            }
            return false;
        }();
        return {
            ajaxUploading: supportsAjaxFileUploading,
            blobUploading: supportsUploadingBlobs,
            canDetermineSize: supportsAjaxFileUploading,
            chunking: supportsChunking,
            deleteFileCors: supportsDeleteFileCors,
            deleteFileCorsXdr: supportsDeleteFileXdr,
            deleteFileCorsXhr: supportsDeleteFileCorsXhr,
            dialogElement: !!window.HTMLDialogElement,
            fileDrop: supportsFileDrop,
            folderDrop: supportsFolderDrop,
            folderSelection: supportsFolderSelection,
            imagePreviews: supportsImagePreviews,
            imageValidation: supportsImagePreviews,
            itemSizeValidation: supportsAjaxFileUploading,
            pause: supportsChunking,
            progressBar: supportsUploadProgress,
            resume: supportsResume,
            scaling: supportsImagePreviews && supportsUploadingBlobs,
            tiffPreviews: miu.safari(),
            unlimitedScaledImageSize: !miu.ios(),
            uploading: supportsUploading,
            uploadCors: supportsUploadCors,
            uploadCustomHeaders: supportsAjaxFileUploading,
            uploadNonMultipart: supportsAjaxFileUploading,
            uploadViaPaste: supportsUploadViaPaste
        };
    }();
    miu.isGenericPromise = function(maybePromise) {
        "use strict";
        return !!(maybePromise && maybePromise.then && miu.isFunction(maybePromise.then));
    };
    miu.Promise = function() {
        "use strict";
        var successArgs, failureArgs, successCallbacks = [], failureCallbacks = [], doneCallbacks = [], state = 0;
        miu.extend(this, {
            then: function(onSuccess, onFailure) {
                if (state === 0) {
                    if (onSuccess) {
                        successCallbacks.push(onSuccess);
                    }
                    if (onFailure) {
                        failureCallbacks.push(onFailure);
                    }
                } else if (state === -1) {
                    onFailure && onFailure.apply(null, failureArgs);
                } else if (onSuccess) {
                    onSuccess.apply(null, successArgs);
                }
                return this;
            },
            done: function(callback) {
                if (state === 0) {
                    doneCallbacks.push(callback);
                } else {
                    callback.apply(null, failureArgs === undefined ? successArgs : failureArgs);
                }
                return this;
            },
            success: function() {
                state = 1;
                successArgs = arguments;
                if (successCallbacks.length) {
                    miu.each(successCallbacks, function(idx, callback) {
                        callback.apply(null, successArgs);
                    });
                }
                if (doneCallbacks.length) {
                    miu.each(doneCallbacks, function(idx, callback) {
                        callback.apply(null, successArgs);
                    });
                }
                return this;
            },
            failure: function() {
                state = -1;
                failureArgs = arguments;
                if (failureCallbacks.length) {
                    miu.each(failureCallbacks, function(idx, callback) {
                        callback.apply(null, failureArgs);
                    });
                }
                if (doneCallbacks.length) {
                    miu.each(doneCallbacks, function(idx, callback) {
                        callback.apply(null, failureArgs);
                    });
                }
                return this;
            }
        });
    };
    miu.BlobProxy = function(referenceBlob, onCreate) {
        "use strict";
        miu.extend(this, {
            referenceBlob: referenceBlob,
            create: function() {
                return onCreate(referenceBlob);
            }
        });
    };
    miu.UploadButton = function(o) {
        "use strict";
        var self = this, disposeSupport = new miu.DisposeSupport(), options = {
            acceptFiles: null,
            element: null,
            focusClass: "miu-upload-button-focus",
            folders: false,
            hoverClass: "miu-upload-button-hover",
            ios8BrowserCrashWorkaround: false,
            multiple: false,
            name: "miufile",
            onChange: function(input) {},
            title: null
        }, input, buttonId;
        miu.extend(options, o);
        buttonId = miu.getUniqueId();
        function createInput() {
            var input = document.createElement("input");
            input.setAttribute(miu.UploadButton.BUTTON_ID_ATTR_NAME, buttonId);
            input.setAttribute("title", options.title);
            self.setMultiple(options.multiple, input);
            if (options.folders && miu.supportedFeatures.folderSelection) {
                input.setAttribute("webkitdirectory", "");
            }
            if (options.acceptFiles) {
                input.setAttribute("accept", options.acceptFiles);
            }
            input.setAttribute("type", "file");
            input.setAttribute("name", options.name);
            miu(input).css({
                position: "absolute",
                right: 0,
                top: 0,
                fontFamily: "Arial",
                fontSize: miu.ie() && !miu.ie8() ? "3500px" : "118px",
                margin: 0,
                padding: 0,
                cursor: "pointer",
                opacity: 0
            });
            !miu.ie7() && miu(input).css({
                height: "100%"
            });
            options.element.appendChild(input);
            disposeSupport.attach(input, "change", function() {
                options.onChange(input);
            });
            disposeSupport.attach(input, "mouseover", function() {
                miu(options.element).addClass(options.hoverClass);
            });
            disposeSupport.attach(input, "mouseout", function() {
                miu(options.element).removeClass(options.hoverClass);
            });
            disposeSupport.attach(input, "focus", function() {
                miu(options.element).addClass(options.focusClass);
            });
            disposeSupport.attach(input, "blur", function() {
                miu(options.element).removeClass(options.focusClass);
            });
            return input;
        }
        miu(options.element).css({
            position: "relative",
            overflow: "hidden",
            direction: "ltr"
        });
        miu.extend(this, {
            getInput: function() {
                return input;
            },
            getButtonId: function() {
                return buttonId;
            },
            setMultiple: function(isMultiple, optInput) {
                var input = optInput || this.getInput();
                if (options.ios8BrowserCrashWorkaround && miu.ios8() && (miu.iosChrome() || miu.iosSafariWebView())) {
                    input.setAttribute("multiple", "");
                } else {
                    if (isMultiple) {
                        input.setAttribute("multiple", "");
                    } else {
                        input.removeAttribute("multiple");
                    }
                }
            },
            setAcceptFiles: function(acceptFiles) {
                if (acceptFiles !== options.acceptFiles) {
                    input.setAttribute("accept", acceptFiles);
                }
            },
            reset: function() {
                if (input.parentNode) {
                    miu(input).remove();
                }
                miu(options.element).removeClass(options.focusClass);
                input = null;
                input = createInput();
            }
        });
        input = createInput();
    };
    miu.UploadButton.BUTTON_ID_ATTR_NAME = "miu-button-id";
    miu.UploadData = function(uploaderProxy) {
        "use strict";
        var data = [], byUuid = {}, byStatus = {}, byProxyGroupId = {}, byBatchId = {};
        function getDataByIds(idOrIds) {
            if (miu.isArray(idOrIds)) {
                var entries = [];
                miu.each(idOrIds, function(idx, id) {
                    entries.push(data[id]);
                });
                return entries;
            }
            return data[idOrIds];
        }
        function getDataByUuids(uuids) {
            if (miu.isArray(uuids)) {
                var entries = [];
                miu.each(uuids, function(idx, uuid) {
                    entries.push(data[byUuid[uuid]]);
                });
                return entries;
            }
            return data[byUuid[uuids]];
        }
        function getDataByStatus(status) {
            var statusResults = [], statuses = [].concat(status);
            miu.each(statuses, function(index, statusEnum) {
                var statusResultIndexes = byStatus[statusEnum];
                if (statusResultIndexes !== undefined) {
                    miu.each(statusResultIndexes, function(i, dataIndex) {
                        statusResults.push(data[dataIndex]);
                    });
                }
            });
            return statusResults;
        }
        miu.extend(this, {
            addFile: function(spec) {
                var status = spec.status || miu.status.SUBMITTING, id = data.push({
                    name: spec.name,
                    originalName: spec.name,
                    uuid: spec.uuid,
                    size: spec.size == null ? -1 : spec.size,
                    status: status
                }) - 1;
                if (spec.batchId) {
                    data[id].batchId = spec.batchId;
                    if (byBatchId[spec.batchId] === undefined) {
                        byBatchId[spec.batchId] = [];
                    }
                    byBatchId[spec.batchId].push(id);
                }
                if (spec.proxyGroupId) {
                    data[id].proxyGroupId = spec.proxyGroupId;
                    if (byProxyGroupId[spec.proxyGroupId] === undefined) {
                        byProxyGroupId[spec.proxyGroupId] = [];
                    }
                    byProxyGroupId[spec.proxyGroupId].push(id);
                }
                data[id].id = id;
                byUuid[spec.uuid] = id;
                if (byStatus[status] === undefined) {
                    byStatus[status] = [];
                }
                byStatus[status].push(id);
                spec.onBeforeStatusChange && spec.onBeforeStatusChange(id);
                uploaderProxy.onStatusChange(id, null, status);
                return id;
            },
            retrieve: function(optionalFilter) {
                if (miu.isObject(optionalFilter) && data.length) {
                    if (optionalFilter.id !== undefined) {
                        return getDataByIds(optionalFilter.id);
                    } else if (optionalFilter.uuid !== undefined) {
                        return getDataByUuids(optionalFilter.uuid);
                    } else if (optionalFilter.status) {
                        return getDataByStatus(optionalFilter.status);
                    }
                } else {
                    return miu.extend([], data, true);
                }
            },
            reset: function() {
                data = [];
                byUuid = {};
                byStatus = {};
                byBatchId = {};
            },
            setStatus: function(id, newStatus) {
                var oldStatus = data[id].status, byStatusOldStatusIndex = miu.indexOf(byStatus[oldStatus], id);
                byStatus[oldStatus].splice(byStatusOldStatusIndex, 1);
                data[id].status = newStatus;
                if (byStatus[newStatus] === undefined) {
                    byStatus[newStatus] = [];
                }
                byStatus[newStatus].push(id);
                uploaderProxy.onStatusChange(id, oldStatus, newStatus);
            },
            uuidChanged: function(id, newUuid) {
                var oldUuid = data[id].uuid;
                data[id].uuid = newUuid;
                byUuid[newUuid] = id;
                delete byUuid[oldUuid];
            },
            updateName: function(id, newName) {
                data[id].name = newName;
            },
            updateSize: function(id, newSize) {
                data[id].size = newSize;
            },
            setParentId: function(targetId, parentId) {
                data[targetId].parentId = parentId;
            },
            getIdsInProxyGroup: function(id) {
                var proxyGroupId = data[id].proxyGroupId;
                if (proxyGroupId) {
                    return byProxyGroupId[proxyGroupId];
                }
                return [];
            },
            getIdsInBatch: function(id) {
                var batchId = data[id].batchId;
                return byBatchId[batchId];
            }
        });
    };
    miu.status = {
        SUBMITTING: "submitting",
        SUBMITTED: "submitted",
        REJECTED: "rejected",
        QUEUED: "queued",
        CANCELED: "canceled",
        PAUSED: "paused",
        UPLOADING: "uploading",
        UPLOAD_RETRYING: "retrying upload",
        UPLOAD_SUCCESSFUL: "upload successful",
        UPLOAD_FAILED: "upload failed",
        DELETE_FAILED: "delete failed",
        DELETING: "deleting",
        DELETED: "deleted"
    };
    (function() {
        "use strict";
        miu.basePublicApi = {
            addBlobs: function(blobDataOrArray, params, endpoint) {
                this.addFiles(blobDataOrArray, params, endpoint);
            },
            addInitialFiles: function(cannedFileList) {
                var self = this;
                miu.each(cannedFileList, function(index, cannedFile) {
                    self._addCannedFile(cannedFile);
                });
            },
            addFiles: function(data, params, endpoint) {
                this._maybeHandleIos8SafariWorkaround();
                var batchId = this._storedIds.length === 0 ? miu.getUniqueId() : this._currentBatchId, processBlob = miu.bind(function(blob) {
                    this._handleNewFile({
                        blob: blob,
                        name: this._options.blobs.defaultName
                    }, batchId, verifiedFiles);
                }, this), processBlobData = miu.bind(function(blobData) {
                    this._handleNewFile(blobData, batchId, verifiedFiles);
                }, this), processCanvas = miu.bind(function(canvas) {
                    var blob = miu.canvasToBlob(canvas);
                    this._handleNewFile({
                        blob: blob,
                        name: this._options.blobs.defaultName + ".png"
                    }, batchId, verifiedFiles);
                }, this), processCanvasData = miu.bind(function(canvasData) {
                    var normalizedQuality = canvasData.quality && canvasData.quality / 100, blob = miu.canvasToBlob(canvasData.canvas, canvasData.type, normalizedQuality);
                    this._handleNewFile({
                        blob: blob,
                        name: canvasData.name
                    }, batchId, verifiedFiles);
                }, this), processFileOrInput = miu.bind(function(fileOrInput) {
                    if (miu.isInput(fileOrInput) && miu.supportedFeatures.ajaxUploading) {
                        var files = Array.prototype.slice.call(fileOrInput.files), self = this;
                        miu.each(files, function(idx, file) {
                            self._handleNewFile(file, batchId, verifiedFiles);
                        });
                    } else {
                        this._handleNewFile(fileOrInput, batchId, verifiedFiles);
                    }
                }, this), normalizeData = function() {
                    if (miu.isFileList(data)) {
                        data = Array.prototype.slice.call(data);
                    }
                    data = [].concat(data);
                }, self = this, verifiedFiles = [];
                this._currentBatchId = batchId;
                if (data) {
                    normalizeData();
                    miu.each(data, function(idx, fileContainer) {
                        if (miu.isFileOrInput(fileContainer)) {
                            processFileOrInput(fileContainer);
                        } else if (miu.isBlob(fileContainer)) {
                            processBlob(fileContainer);
                        } else if (miu.isObject(fileContainer)) {
                            if (fileContainer.blob && fileContainer.name) {
                                processBlobData(fileContainer);
                            } else if (fileContainer.canvas && fileContainer.name) {
                                processCanvasData(fileContainer);
                            }
                        } else if (fileContainer.tagName && fileContainer.tagName.toLowerCase() === "canvas") {
                            processCanvas(fileContainer);
                        } else {
                            self.log(fileContainer + " is not a valid file container!  Ignoring!", "warn");
                        }
                    });
                    this.log("Received " + verifiedFiles.length + " files.");
                    this._prepareItemsForUpload(verifiedFiles, params, endpoint);
                }
            },
            cancel: function(id) {
                this._handler.cancel(id);
            },
            cancelAll: function() {
                var storedIdsCopy = [], self = this;
                miu.extend(storedIdsCopy, this._storedIds);
                miu.each(storedIdsCopy, function(idx, storedFileId) {
                    self.cancel(storedFileId);
                });
                this._handler.cancelAll();
            },
            clearStoredFiles: function() {
                this._storedIds = [];
            },
            continueUpload: function(id) {
                var uploadData = this._uploadData.retrieve({
                    id: id
                });
                if (!miu.supportedFeatures.pause || !this._options.chunking.enabled) {
                    return false;
                }
                if (uploadData.status === miu.status.PAUSED) {
                    this.log(miu.format("Paused file ID {} ({}) will be continued.  Not paused.", id, this.getName(id)));
                    this._uploadFile(id);
                    return true;
                } else {
                    this.log(miu.format("Ignoring continue for file ID {} ({}).  Not paused.", id, this.getName(id)), "error");
                }
                return false;
            },
            deleteFile: function(id) {
                return this._onSubmitDelete(id);
            },
            doesExist: function(fileOrBlobId) {
                return this._handler.isValid(fileOrBlobId);
            },
            drawThumbnail: function(fileId, imgOrCanvas, maxSize, fromServer, customResizeFunction) {
                var promiseToReturn = new miu.Promise(), fileOrUrl, options;
                if (this._imageGenerator) {
                    fileOrUrl = this._thumbnailUrls[fileId];
                    options = {
                        customResizeFunction: customResizeFunction,
                        maxSize: maxSize > 0 ? maxSize : null,
                        scale: maxSize > 0
                    };
                    if (!fromServer && miu.supportedFeatures.imagePreviews) {
                        fileOrUrl = this.getFile(fileId);
                    }
                    if (fileOrUrl == null) {
                        promiseToReturn.failure({
                            container: imgOrCanvas,
                            error: "File or URL not found."
                        });
                    } else {
                        this._imageGenerator.generate(fileOrUrl, imgOrCanvas, options).then(function success(modifiedContainer) {
                            promiseToReturn.success(modifiedContainer);
                        }, function failure(container, reason) {
                            promiseToReturn.failure({
                                container: container,
                                error: reason || "Problem generating thumbnail"
                            });
                        });
                    }
                } else {
                    promiseToReturn.failure({
                        container: imgOrCanvas,
                        error: "Missing image generator module"
                    });
                }
                return promiseToReturn;
            },
            getButton: function(fileId) {
                return this._getButton(this._buttonIdsForFileIds[fileId]);
            },
            getEndpoint: function(fileId) {
                return this._endpointStore.get(fileId);
            },
            getFile: function(fileOrBlobId) {
                return this._handler.getFile(fileOrBlobId) || null;
            },
            getInProgress: function() {
                return this._uploadData.retrieve({
                    status: [ miu.status.UPLOADING, miu.status.UPLOAD_RETRYING, miu.status.QUEUED ]
                }).length;
            },
            getName: function(id) {
                return this._uploadData.retrieve({
                    id: id
                }).name;
            },
            getParentId: function(id) {
                var uploadDataEntry = this.getUploads({
                    id: id
                }), parentId = null;
                if (uploadDataEntry) {
                    if (uploadDataEntry.parentId !== undefined) {
                        parentId = uploadDataEntry.parentId;
                    }
                }
                return parentId;
            },
            getResumableFilesData: function() {
                return this._handler.getResumableFilesData();
            },
            getSize: function(id) {
                return this._uploadData.retrieve({
                    id: id
                }).size;
            },
            getNetUploads: function() {
                return this._netUploaded;
            },
            getRemainingAllowedItems: function() {
                var allowedItems = this._currentItemLimit;
                if (allowedItems > 0) {
                    return allowedItems - this._netUploadedOrQueued;
                }
                return null;
            },
            getUploads: function(optionalFilter) {
                return this._uploadData.retrieve(optionalFilter);
            },
            getUuid: function(id) {
                return this._uploadData.retrieve({
                    id: id
                }).uuid;
            },
            log: function(str, level) {
                if (this._options.debug && (!level || level === "info")) {
                    miu.log("[Fine Uploader " + miu.version + "] " + str);
                } else if (level && level !== "info") {
                    miu.log("[Fine Uploader " + miu.version + "] " + str, level);
                }
            },
            pauseUpload: function(id) {
                var uploadData = this._uploadData.retrieve({
                    id: id
                });
                if (!miu.supportedFeatures.pause || !this._options.chunking.enabled) {
                    return false;
                }
                if (miu.indexOf([ miu.status.UPLOADING, miu.status.UPLOAD_RETRYING ], uploadData.status) >= 0) {
                    if (this._handler.pause(id)) {
                        this._uploadData.setStatus(id, miu.status.PAUSED);
                        return true;
                    } else {
                        this.log(miu.format("Unable to pause file ID {} ({}).", id, this.getName(id)), "error");
                    }
                } else {
                    this.log(miu.format("Ignoring pause for file ID {} ({}).  Not in progress.", id, this.getName(id)), "error");
                }
                return false;
            },
            removeFileRef: function(id) {
                this._handler.expunge(id);
            },
            reset: function() {
                this.log("Resetting uploader...");
                this._handler.reset();
                this._storedIds = [];
                this._autoRetries = [];
                this._retryTimeouts = [];
                this._preventRetries = [];
                this._thumbnailUrls = [];
                miu.each(this._buttons, function(idx, button) {
                    button.reset();
                });
                this._paramsStore.reset();
                this._endpointStore.reset();
                this._netUploadedOrQueued = 0;
                this._netUploaded = 0;
                this._uploadData.reset();
                this._buttonIdsForFileIds = [];
                this._pasteHandler && this._pasteHandler.reset();
                this._options.session.refreshOnReset && this._refreshSessionData();
                this._succeededSinceLastAllComplete = [];
                this._failedSinceLastAllComplete = [];
                this._totalProgress && this._totalProgress.reset();
            },
            retry: function(id) {
                return this._manualRetry(id);
            },
            scaleImage: function(id, specs) {
                var self = this;
                return miu.Scaler.prototype.scaleImage(id, specs, {
                    log: miu.bind(self.log, self),
                    getFile: miu.bind(self.getFile, self),
                    uploadData: self._uploadData
                });
            },
            setCustomHeaders: function(headers, id) {
                this._customHeadersStore.set(headers, id);
            },
            setDeleteFileCustomHeaders: function(headers, id) {
                this._deleteFileCustomHeadersStore.set(headers, id);
            },
            setDeleteFileEndpoint: function(endpoint, id) {
                this._deleteFileEndpointStore.set(endpoint, id);
            },
            setDeleteFileParams: function(params, id) {
                this._deleteFileParamsStore.set(params, id);
            },
            setEndpoint: function(endpoint, id) {
                this._endpointStore.set(endpoint, id);
            },
            setForm: function(elementOrId) {
                this._updateFormSupportAndParams(elementOrId);
            },
            setItemLimit: function(newItemLimit) {
                this._currentItemLimit = newItemLimit;
            },
            setName: function(id, newName) {
                this._uploadData.updateName(id, newName);
            },
            setParams: function(params, id) {
                this._paramsStore.set(params, id);
            },
            setUuid: function(id, newUuid) {
                return this._uploadData.uuidChanged(id, newUuid);
            },
            setStatus: function(id, newStatus) {
                var fileRecord = this.getUploads({
                    id: id
                });
                if (!fileRecord) {
                    throw new miu.Error(id + " is not a valid file ID.");
                }
                switch (newStatus) {
                  case miu.status.DELETED:
                    this._onDeleteComplete(id, null, false);
                    break;

                  case miu.status.DELETE_FAILED:
                    this._onDeleteComplete(id, null, true);
                    break;

                  default:
                    var errorMessage = "Method setStatus called on '" + name + "' not implemented yet for " + newStatus;
                    this.log(errorMessage);
                    throw new miu.Error(errorMessage);
                }
            },
            uploadStoredFiles: function() {
                if (this._storedIds.length === 0) {
                    this._itemError("noFilesError");
                } else {
                    this._uploadStoredFiles();
                }
            }
        };
        miu.basePrivateApi = {
            _addCannedFile: function(sessionData) {
                var self = this;
                return this._uploadData.addFile({
                    uuid: sessionData.uuid,
                    name: sessionData.name,
                    size: sessionData.size,
                    status: miu.status.UPLOAD_SUCCESSFUL,
                    onBeforeStatusChange: function(id) {
                        sessionData.deleteFileEndpoint && self.setDeleteFileEndpoint(sessionData.deleteFileEndpoint, id);
                        sessionData.deleteFileParams && self.setDeleteFileParams(sessionData.deleteFileParams, id);
                        if (sessionData.thumbnailUrl) {
                            self._thumbnailUrls[id] = sessionData.thumbnailUrl;
                        }
                        self._netUploaded++;
                        self._netUploadedOrQueued++;
                    }
                });
            },
            _annotateWithButtonId: function(file, associatedInput) {
                if (miu.isFile(file)) {
                    file.miuButtonId = this._getButtonId(associatedInput);
                }
            },
            _batchError: function(message) {
                this._options.callbacks.onError(null, null, message, undefined);
            },
            _createDeleteHandler: function() {
                var self = this;
                return new miu.DeleteFileAjaxRequester({
                    method: this._options.deleteFile.method.toUpperCase(),
                    maxConnections: this._options.maxConnections,
                    uuidParamName: this._options.request.uuidName,
                    customHeaders: this._deleteFileCustomHeadersStore,
                    paramsStore: this._deleteFileParamsStore,
                    endpointStore: this._deleteFileEndpointStore,
                    cors: this._options.cors,
                    log: miu.bind(self.log, self),
                    onDelete: function(id) {
                        self._onDelete(id);
                        self._options.callbacks.onDelete(id);
                    },
                    onDeleteComplete: function(id, xhrOrXdr, isError) {
                        self._onDeleteComplete(id, xhrOrXdr, isError);
                        self._options.callbacks.onDeleteComplete(id, xhrOrXdr, isError);
                    }
                });
            },
            _createPasteHandler: function() {
                var self = this;
                return new miu.PasteSupport({
                    targetElement: this._options.paste.targetElement,
                    callbacks: {
                        log: miu.bind(self.log, self),
                        pasteReceived: function(blob) {
                            self._handleCheckedCallback({
                                name: "onPasteReceived",
                                callback: miu.bind(self._options.callbacks.onPasteReceived, self, blob),
                                onSuccess: miu.bind(self._handlePasteSuccess, self, blob),
                                identifier: "pasted image"
                            });
                        }
                    }
                });
            },
            _createStore: function(initialValue, _readOnlyValues_) {
                var store = {}, catchall = initialValue, perIdReadOnlyValues = {}, readOnlyValues = _readOnlyValues_, copy = function(orig) {
                    if (miu.isObject(orig)) {
                        return miu.extend({}, orig);
                    }
                    return orig;
                }, getReadOnlyValues = function() {
                    if (miu.isFunction(readOnlyValues)) {
                        return readOnlyValues();
                    }
                    return readOnlyValues;
                }, includeReadOnlyValues = function(id, existing) {
                    if (readOnlyValues && miu.isObject(existing)) {
                        miu.extend(existing, getReadOnlyValues());
                    }
                    if (perIdReadOnlyValues[id]) {
                        miu.extend(existing, perIdReadOnlyValues[id]);
                    }
                };
                return {
                    set: function(val, id) {
                        if (id == null) {
                            store = {};
                            catchall = copy(val);
                        } else {
                            store[id] = copy(val);
                        }
                    },
                    get: function(id) {
                        var values;
                        if (id != null && store[id]) {
                            values = store[id];
                        } else {
                            values = copy(catchall);
                        }
                        includeReadOnlyValues(id, values);
                        return copy(values);
                    },
                    addReadOnly: function(id, values) {
                        if (miu.isObject(store)) {
                            if (id === null) {
                                if (miu.isFunction(values)) {
                                    readOnlyValues = values;
                                } else {
                                    readOnlyValues = readOnlyValues || {};
                                    miu.extend(readOnlyValues, values);
                                }
                            } else {
                                perIdReadOnlyValues[id] = perIdReadOnlyValues[id] || {};
                                miu.extend(perIdReadOnlyValues[id], values);
                            }
                        }
                    },
                    remove: function(fileId) {
                        return delete store[fileId];
                    },
                    reset: function() {
                        store = {};
                        perIdReadOnlyValues = {};
                        catchall = initialValue;
                    }
                };
            },
            _createUploadDataTracker: function() {
                var self = this;
                return new miu.UploadData({
                    getName: function(id) {
                        return self.getName(id);
                    },
                    getUuid: function(id) {
                        return self.getUuid(id);
                    },
                    getSize: function(id) {
                        return self.getSize(id);
                    },
                    onStatusChange: function(id, oldStatus, newStatus) {
                        self._onUploadStatusChange(id, oldStatus, newStatus);
                        self._options.callbacks.onStatusChange(id, oldStatus, newStatus);
                        self._maybeAllComplete(id, newStatus);
                        if (self._totalProgress) {
                            setTimeout(function() {
                                self._totalProgress.onStatusChange(id, oldStatus, newStatus);
                            }, 0);
                        }
                    }
                });
            },
            _createUploadButton: function(spec) {
                var self = this, acceptFiles = spec.accept || this._options.validation.acceptFiles, allowedExtensions = spec.allowedExtensions || this._options.validation.allowedExtensions, button;
                function allowMultiple() {
                    if (miu.supportedFeatures.ajaxUploading) {
                        if (self._options.workarounds.iosEmptyVideos && miu.ios() && !miu.ios6() && self._isAllowedExtension(allowedExtensions, ".mov")) {
                            return false;
                        }
                        if (spec.multiple === undefined) {
                            return self._options.multiple;
                        }
                        return spec.multiple;
                    }
                    return false;
                }
                button = new miu.UploadButton({
                    acceptFiles: acceptFiles,
                    element: spec.element,
                    focusClass: this._options.classes.buttonFocus,
                    folders: spec.folders,
                    hoverClass: this._options.classes.buttonHover,
                    ios8BrowserCrashWorkaround: this._options.workarounds.ios8BrowserCrash,
                    multiple: allowMultiple(),
                    name: this._options.request.inputName,
                    onChange: function(input) {
                        self._onInputChange(input);
                    },
                    title: spec.title == null ? this._options.text.fileInputTitle : spec.title
                });
                this._disposeSupport.addDisposer(function() {
                    button.dispose();
                });
                self._buttons.push(button);
                return button;
            },
            _createUploadHandler: function(additionalOptions, namespace) {
                var self = this, lastOnProgress = {}, options = {
                    debug: this._options.debug,
                    maxConnections: this._options.maxConnections,
                    cors: this._options.cors,
                    paramsStore: this._paramsStore,
                    endpointStore: this._endpointStore,
                    chunking: this._options.chunking,
                    resume: this._options.resume,
                    blobs: this._options.blobs,
                    log: miu.bind(self.log, self),
                    preventRetryParam: this._options.retry.preventRetryResponseProperty,
                    onProgress: function(id, name, loaded, total) {
                        if (loaded < 0 || total < 0) {
                            return;
                        }
                        if (lastOnProgress[id]) {
                            if (lastOnProgress[id].loaded !== loaded || lastOnProgress[id].total !== total) {
                                self._onProgress(id, name, loaded, total);
                                self._options.callbacks.onProgress(id, name, loaded, total);
                            }
                        } else {
                            self._onProgress(id, name, loaded, total);
                            self._options.callbacks.onProgress(id, name, loaded, total);
                        }
                        lastOnProgress[id] = {
                            loaded: loaded,
                            total: total
                        };
                    },
                    onComplete: function(id, name, result, xhr) {
                        delete lastOnProgress[id];
                        var status = self.getUploads({
                            id: id
                        }).status, retVal;
                        if (status === miu.status.UPLOAD_SUCCESSFUL || status === miu.status.UPLOAD_FAILED) {
                            return;
                        }
                        retVal = self._onComplete(id, name, result, xhr);
                        if (retVal instanceof miu.Promise) {
                            retVal.done(function() {
                                self._options.callbacks.onComplete(id, name, result, xhr);
                            });
                        } else {
                            self._options.callbacks.onComplete(id, name, result, xhr);
                        }
                    },
                    onCancel: function(id, name, cancelFinalizationEffort) {
                        var promise = new miu.Promise();
                        self._handleCheckedCallback({
                            name: "onCancel",
                            callback: miu.bind(self._options.callbacks.onCancel, self, id, name),
                            onFailure: promise.failure,
                            onSuccess: function() {
                                cancelFinalizationEffort.then(function() {
                                    self._onCancel(id, name);
                                });
                                promise.success();
                            },
                            identifier: id
                        });
                        return promise;
                    },
                    onUploadPrep: miu.bind(this._onUploadPrep, this),
                    onUpload: function(id, name) {
                        self._onUpload(id, name);
                        self._options.callbacks.onUpload(id, name);
                    },
                    onUploadChunk: function(id, name, chunkData) {
                        self._onUploadChunk(id, chunkData);
                        self._options.callbacks.onUploadChunk(id, name, chunkData);
                    },
                    onUploadChunkSuccess: function(id, chunkData, result, xhr) {
                        self._options.callbacks.onUploadChunkSuccess.apply(self, arguments);
                    },
                    onResume: function(id, name, chunkData) {
                        return self._options.callbacks.onResume(id, name, chunkData);
                    },
                    onAutoRetry: function(id, name, responseJSON, xhr) {
                        return self._onAutoRetry.apply(self, arguments);
                    },
                    onUuidChanged: function(id, newUuid) {
                        self.log("Server requested UUID change from '" + self.getUuid(id) + "' to '" + newUuid + "'");
                        self.setUuid(id, newUuid);
                    },
                    getName: miu.bind(self.getName, self),
                    getUuid: miu.bind(self.getUuid, self),
                    getSize: miu.bind(self.getSize, self),
                    setSize: miu.bind(self._setSize, self),
                    getDataByUuid: function(uuid) {
                        return self.getUploads({
                            uuid: uuid
                        });
                    },
                    isQueued: function(id) {
                        var status = self.getUploads({
                            id: id
                        }).status;
                        return status === miu.status.QUEUED || status === miu.status.SUBMITTED || status === miu.status.UPLOAD_RETRYING || status === miu.status.PAUSED;
                    },
                    getIdsInProxyGroup: self._uploadData.getIdsInProxyGroup,
                    getIdsInBatch: self._uploadData.getIdsInBatch
                };
                miu.each(this._options.request, function(prop, val) {
                    options[prop] = val;
                });
                options.customHeaders = this._customHeadersStore;
                if (additionalOptions) {
                    miu.each(additionalOptions, function(key, val) {
                        options[key] = val;
                    });
                }
                return new miu.UploadHandlerController(options, namespace);
            },
            _fileOrBlobRejected: function(id) {
                this._netUploadedOrQueued--;
                this._uploadData.setStatus(id, miu.status.REJECTED);
            },
            _formatSize: function(bytes) {
                if (bytes === 0) {
                    return bytes + this._options.text.sizeSymbols[0];
                }
                var i = -1;
                do {
                    bytes = bytes / 1e3;
                    i++;
                } while (bytes > 999);
                return Math.max(bytes, .1).toFixed(1) + this._options.text.sizeSymbols[i];
            },
            _generateExtraButtonSpecs: function() {
                var self = this;
                this._extraButtonSpecs = {};
                miu.each(this._options.extraButtons, function(idx, extraButtonOptionEntry) {
                    var multiple = extraButtonOptionEntry.multiple, validation = miu.extend({}, self._options.validation, true), extraButtonSpec = miu.extend({}, extraButtonOptionEntry);
                    if (multiple === undefined) {
                        multiple = self._options.multiple;
                    }
                    if (extraButtonSpec.validation) {
                        miu.extend(validation, extraButtonOptionEntry.validation, true);
                    }
                    miu.extend(extraButtonSpec, {
                        multiple: multiple,
                        validation: validation
                    }, true);
                    self._initExtraButton(extraButtonSpec);
                });
            },
            _getButton: function(buttonId) {
                var extraButtonsSpec = this._extraButtonSpecs[buttonId];
                if (extraButtonsSpec) {
                    return extraButtonsSpec.element;
                } else if (buttonId === this._defaultButtonId) {
                    return this._options.button;
                }
            },
            _getButtonId: function(buttonOrFileInputOrFile) {
                var inputs, fileInput, fileBlobOrInput = buttonOrFileInputOrFile;
                if (fileBlobOrInput instanceof miu.BlobProxy) {
                    fileBlobOrInput = fileBlobOrInput.referenceBlob;
                }
                if (fileBlobOrInput && !miu.isBlob(fileBlobOrInput)) {
                    if (miu.isFile(fileBlobOrInput)) {
                        return fileBlobOrInput.miuButtonId;
                    } else if (fileBlobOrInput.tagName.toLowerCase() === "input" && fileBlobOrInput.type.toLowerCase() === "file") {
                        return fileBlobOrInput.getAttribute(miu.UploadButton.BUTTON_ID_ATTR_NAME);
                    }
                    inputs = fileBlobOrInput.getElementsByTagName("input");
                    miu.each(inputs, function(idx, input) {
                        if (input.getAttribute("type") === "file") {
                            fileInput = input;
                            return false;
                        }
                    });
                    if (fileInput) {
                        return fileInput.getAttribute(miu.UploadButton.BUTTON_ID_ATTR_NAME);
                    }
                }
            },
            _getNotFinished: function() {
                return this._uploadData.retrieve({
                    status: [ miu.status.UPLOADING, miu.status.UPLOAD_RETRYING, miu.status.QUEUED, miu.status.SUBMITTING, miu.status.SUBMITTED, miu.status.PAUSED ]
                }).length;
            },
            _getValidationBase: function(buttonId) {
                var extraButtonSpec = this._extraButtonSpecs[buttonId];
                return extraButtonSpec ? extraButtonSpec.validation : this._options.validation;
            },
            _getValidationDescriptor: function(fileWrapper) {
                if (fileWrapper.file instanceof miu.BlobProxy) {
                    return {
                        name: miu.getFilename(fileWrapper.file.referenceBlob),
                        size: fileWrapper.file.referenceBlob.size
                    };
                }
                return {
                    name: this.getUploads({
                        id: fileWrapper.id
                    }).name,
                    size: this.getUploads({
                        id: fileWrapper.id
                    }).size
                };
            },
            _getValidationDescriptors: function(fileWrappers) {
                var self = this, fileDescriptors = [];
                miu.each(fileWrappers, function(idx, fileWrapper) {
                    fileDescriptors.push(self._getValidationDescriptor(fileWrapper));
                });
                return fileDescriptors;
            },
            _handleCameraAccess: function() {
                if (this._options.camera.ios && miu.ios()) {
                    var acceptIosCamera = "image/*;capture=camera", button = this._options.camera.button, buttonId = button ? this._getButtonId(button) : this._defaultButtonId, optionRoot = this._options;
                    if (buttonId && buttonId !== this._defaultButtonId) {
                        optionRoot = this._extraButtonSpecs[buttonId];
                    }
                    optionRoot.multiple = false;
                    if (optionRoot.validation.acceptFiles === null) {
                        optionRoot.validation.acceptFiles = acceptIosCamera;
                    } else {
                        optionRoot.validation.acceptFiles += "," + acceptIosCamera;
                    }
                    miu.each(this._buttons, function(idx, button) {
                        if (button.getButtonId() === buttonId) {
                            button.setMultiple(optionRoot.multiple);
                            button.setAcceptFiles(optionRoot.acceptFiles);
                            return false;
                        }
                    });
                }
            },
            _handleCheckedCallback: function(details) {
                var self = this, callbackRetVal = details.callback();
                if (miu.isGenericPromise(callbackRetVal)) {
                    this.log(details.name + " - waiting for " + details.name + " promise to be fulfilled for " + details.identifier);
                    return callbackRetVal.then(function(successParam) {
                        self.log(details.name + " promise success for " + details.identifier);
                        details.onSuccess(successParam);
                    }, function() {
                        if (details.onFailure) {
                            self.log(details.name + " promise failure for " + details.identifier);
                            details.onFailure();
                        } else {
                            self.log(details.name + " promise failure for " + details.identifier);
                        }
                    });
                }
                if (callbackRetVal !== false) {
                    details.onSuccess(callbackRetVal);
                } else {
                    if (details.onFailure) {
                        this.log(details.name + " - return value was 'false' for " + details.identifier + ".  Invoking failure callback.");
                        details.onFailure();
                    } else {
                        this.log(details.name + " - return value was 'false' for " + details.identifier + ".  Will not proceed.");
                    }
                }
                return callbackRetVal;
            },
            _handleNewFile: function(file, batchId, newFileWrapperList) {
                var self = this, uuid = miu.getUniqueId(), size = -1, name = miu.getFilename(file), actualFile = file.blob || file, handler = this._customNewFileHandler ? this._customNewFileHandler : miu.bind(self._handleNewFileGeneric, self);
                if (!miu.isInput(actualFile) && actualFile.size >= 0) {
                    size = actualFile.size;
                }
                handler(actualFile, name, uuid, size, newFileWrapperList, batchId, this._options.request.uuidName, {
                    uploadData: self._uploadData,
                    paramsStore: self._paramsStore,
                    addFileToHandler: function(id, file) {
                        self._handler.add(id, file);
                        self._netUploadedOrQueued++;
                        self._trackButton(id);
                    }
                });
            },
            _handleNewFileGeneric: function(file, name, uuid, size, fileList, batchId) {
                var id = this._uploadData.addFile({
                    uuid: uuid,
                    name: name,
                    size: size,
                    batchId: batchId
                });
                this._handler.add(id, file);
                this._trackButton(id);
                this._netUploadedOrQueued++;
                fileList.push({
                    id: id,
                    file: file
                });
            },
            _handlePasteSuccess: function(blob, extSuppliedName) {
                var extension = blob.type.split("/")[1], name = extSuppliedName;
                if (name == null) {
                    name = this._options.paste.defaultName;
                }
                name += "." + extension;
                this.addFiles({
                    name: name,
                    blob: blob
                });
            },
            _handleDeleteSuccess: function(id) {
                if (this.getUploads({
                    id: id
                }).status !== miu.status.DELETED) {
                    var name = this.getName(id);
                    this._netUploadedOrQueued--;
                    this._netUploaded--;
                    this._handler.expunge(id);
                    this._uploadData.setStatus(id, miu.status.DELETED);
                    this.log("Delete request for '" + name + "' has succeeded.");
                }
            },
            _handleDeleteFailed: function(id, xhrOrXdr) {
                var name = this.getName(id);
                this._uploadData.setStatus(id, miu.status.DELETE_FAILED);
                this.log("Delete request for '" + name + "' has failed.", "error");
                if (!xhrOrXdr || xhrOrXdr.withCredentials === undefined) {
                    this._options.callbacks.onError(id, name, "Delete request failed", xhrOrXdr);
                } else {
                    this._options.callbacks.onError(id, name, "Delete request failed with response code " + xhrOrXdr.status, xhrOrXdr);
                }
            },
            _initExtraButton: function(spec) {
                var button = this._createUploadButton({
                    accept: spec.validation.acceptFiles,
                    allowedExtensions: spec.validation.allowedExtensions,
                    element: spec.element,
                    folders: spec.folders,
                    multiple: spec.multiple,
                    title: spec.fileInputTitle
                });
                this._extraButtonSpecs[button.getButtonId()] = spec;
            },
            _initFormSupportAndParams: function() {
                this._formSupport = miu.FormSupport && new miu.FormSupport(this._options.form, miu.bind(this.uploadStoredFiles, this), miu.bind(this.log, this));
                if (this._formSupport && this._formSupport.attachedToForm) {
                    this._paramsStore = this._createStore(this._options.request.params, this._formSupport.getFormInputsAsObject);
                    this._options.autoUpload = this._formSupport.newAutoUpload;
                    if (this._formSupport.newEndpoint) {
                        this._options.request.endpoint = this._formSupport.newEndpoint;
                    }
                } else {
                    this._paramsStore = this._createStore(this._options.request.params);
                }
            },
            _isDeletePossible: function() {
                if (!miu.DeleteFileAjaxRequester || !this._options.deleteFile.enabled) {
                    return false;
                }
                if (this._options.cors.expected) {
                    if (miu.supportedFeatures.deleteFileCorsXhr) {
                        return true;
                    }
                    if (miu.supportedFeatures.deleteFileCorsXdr && this._options.cors.allowXdr) {
                        return true;
                    }
                    return false;
                }
                return true;
            },
            _isAllowedExtension: function(allowed, fileName) {
                var valid = false;
                if (!allowed.length) {
                    return true;
                }
                miu.each(allowed, function(idx, allowedExt) {
                    if (miu.isString(allowedExt)) {
                        var extRegex = new RegExp("\\." + allowedExt + "$", "i");
                        if (fileName.match(extRegex) != null) {
                            valid = true;
                            return false;
                        }
                    }
                });
                return valid;
            },
            _itemError: function(code, maybeNameOrNames, item) {
                var message = this._options.messages[code], allowedExtensions = [], names = [].concat(maybeNameOrNames), name = names[0], buttonId = this._getButtonId(item), validationBase = this._getValidationBase(buttonId), extensionsForMessage, placeholderMatch;
                function r(name, replacement) {
                    message = message.replace(name, replacement);
                }
                miu.each(validationBase.allowedExtensions, function(idx, allowedExtension) {
                    if (miu.isString(allowedExtension)) {
                        allowedExtensions.push(allowedExtension);
                    }
                });
                extensionsForMessage = allowedExtensions.join(", ").toLowerCase();
                r("{file}", this._options.formatFileName(name));
                r("{extensions}", extensionsForMessage);
                r("{sizeLimit}", this._formatSize(validationBase.sizeLimit));
                r("{minSizeLimit}", this._formatSize(validationBase.minSizeLimit));
                placeholderMatch = message.match(/(\{\w+\})/g);
                if (placeholderMatch !== null) {
                    miu.each(placeholderMatch, function(idx, placeholder) {
                        r(placeholder, names[idx]);
                    });
                }
                this._options.callbacks.onError(null, name, message, undefined);
                return message;
            },
            _manualRetry: function(id, callback) {
                if (this._onBeforeManualRetry(id)) {
                    this._netUploadedOrQueued++;
                    this._uploadData.setStatus(id, miu.status.UPLOAD_RETRYING);
                    if (callback) {
                        callback(id);
                    } else {
                        this._handler.retry(id);
                    }
                    return true;
                }
            },
            _maybeAllComplete: function(id, status) {
                var self = this, notFinished = this._getNotFinished();
                if (status === miu.status.UPLOAD_SUCCESSFUL) {
                    this._succeededSinceLastAllComplete.push(id);
                } else if (status === miu.status.UPLOAD_FAILED) {
                    this._failedSinceLastAllComplete.push(id);
                }
                if (notFinished === 0 && (this._succeededSinceLastAllComplete.length || this._failedSinceLastAllComplete.length)) {
                    setTimeout(function() {
                        self._onAllComplete(self._succeededSinceLastAllComplete, self._failedSinceLastAllComplete);
                    }, 0);
                }
            },
            _maybeHandleIos8SafariWorkaround: function() {
                var self = this;
                if (this._options.workarounds.ios8SafariUploads && miu.ios800() && miu.iosSafari()) {
                    setTimeout(function() {
                        window.alert(self._options.messages.unsupportedBrowserIos8Safari);
                    }, 0);
                    throw new miu.Error(this._options.messages.unsupportedBrowserIos8Safari);
                }
            },
            _maybeParseAndSendUploadError: function(id, name, response, xhr) {
                if (!response.success) {
                    if (xhr && xhr.status !== 200 && !response.error) {
                        this._options.callbacks.onError(id, name, "XHR returned response code " + xhr.status, xhr);
                    } else {
                        var errorReason = response.error ? response.error : this._options.text.defaultResponseError;
                        this._options.callbacks.onError(id, name, errorReason, xhr);
                    }
                }
            },
            _maybeProcessNextItemAfterOnValidateCallback: function(validItem, items, index, params, endpoint) {
                var self = this;
                if (items.length > index) {
                    if (validItem || !this._options.validation.stopOnFirstInvalidFile) {
                        setTimeout(function() {
                            var validationDescriptor = self._getValidationDescriptor(items[index]), buttonId = self._getButtonId(items[index].file), button = self._getButton(buttonId);
                            self._handleCheckedCallback({
                                name: "onValidate",
                                callback: miu.bind(self._options.callbacks.onValidate, self, validationDescriptor, button),
                                onSuccess: miu.bind(self._onValidateCallbackSuccess, self, items, index, params, endpoint),
                                onFailure: miu.bind(self._onValidateCallbackFailure, self, items, index, params, endpoint),
                                identifier: "Item '" + validationDescriptor.name + "', size: " + validationDescriptor.size
                            });
                        }, 0);
                    } else if (!validItem) {
                        for (;index < items.length; index++) {
                            self._fileOrBlobRejected(items[index].id);
                        }
                    }
                }
            },
            _onAllComplete: function(successful, failed) {
                this._totalProgress && this._totalProgress.onAllComplete(successful, failed, this._preventRetries);
                this._options.callbacks.onAllComplete(miu.extend([], successful), miu.extend([], failed));
                this._succeededSinceLastAllComplete = [];
                this._failedSinceLastAllComplete = [];
            },
            _onAutoRetry: function(id, name, responseJSON, xhr, callback) {
                var self = this;
                self._preventRetries[id] = responseJSON[self._options.retry.preventRetryResponseProperty];
                if (self._shouldAutoRetry(id, name, responseJSON)) {
                    var retryWaitPeriod = self._options.retry.autoAttemptDelay * 1e3;
                    self._maybeParseAndSendUploadError.apply(self, arguments);
                    self._options.callbacks.onAutoRetry(id, name, self._autoRetries[id]);
                    self._onBeforeAutoRetry(id, name);
                    self._uploadData.setStatus(id, miu.status.UPLOAD_RETRYING);
                    self._retryTimeouts[id] = setTimeout(function() {
                        self.log("Starting retry for " + name + "...");
                        if (callback) {
                            callback(id);
                        } else {
                            self._handler.retry(id);
                        }
                    }, retryWaitPeriod);
                    return true;
                }
            },
            _onBeforeAutoRetry: function(id, name) {
                this.log("Waiting " + this._options.retry.autoAttemptDelay + " seconds before retrying " + name + "...");
            },
            _onBeforeManualRetry: function(id) {
                var itemLimit = this._currentItemLimit, fileName;
                if (this._preventRetries[id]) {
                    this.log("Retries are forbidden for id " + id, "warn");
                    return false;
                } else if (this._handler.isValid(id)) {
                    fileName = this.getName(id);
                    if (this._options.callbacks.onManualRetry(id, fileName) === false) {
                        return false;
                    }
                    if (itemLimit > 0 && this._netUploadedOrQueued + 1 > itemLimit) {
                        this._itemError("retryFailTooManyItems");
                        return false;
                    }
                    this.log("Retrying upload for '" + fileName + "' (id: " + id + ")...");
                    return true;
                } else {
                    this.log("'" + id + "' is not a valid file ID", "error");
                    return false;
                }
            },
            _onCancel: function(id, name) {
                this._netUploadedOrQueued--;
                clearTimeout(this._retryTimeouts[id]);
                var storedItemIndex = miu.indexOf(this._storedIds, id);
                if (!this._options.autoUpload && storedItemIndex >= 0) {
                    this._storedIds.splice(storedItemIndex, 1);
                }
                this._uploadData.setStatus(id, miu.status.CANCELED);
            },
            _onComplete: function(id, name, result, xhr) {
                if (!result.success) {
                    this._netUploadedOrQueued--;
                    this._uploadData.setStatus(id, miu.status.UPLOAD_FAILED);
                    if (result[this._options.retry.preventRetryResponseProperty] === true) {
                        this._preventRetries[id] = true;
                    }
                } else {
                    if (result.thumbnailUrl) {
                        this._thumbnailUrls[id] = result.thumbnailUrl;
                    }
                    this._netUploaded++;
                    this._uploadData.setStatus(id, miu.status.UPLOAD_SUCCESSFUL);
                }
                this._maybeParseAndSendUploadError(id, name, result, xhr);
                return result.success ? true : false;
            },
            _onDelete: function(id) {
                this._uploadData.setStatus(id, miu.status.DELETING);
            },
            _onDeleteComplete: function(id, xhrOrXdr, isError) {
                var name = this.getName(id);
                if (isError) {
                    this._handleDeleteFailed(id, xhrOrXdr);
                } else {
                    this._handleDeleteSuccess(id);
                }
            },
            _onInputChange: function(input) {
                var fileIndex;
                if (miu.supportedFeatures.ajaxUploading) {
                    for (fileIndex = 0; fileIndex < input.files.length; fileIndex++) {
                        this._annotateWithButtonId(input.files[fileIndex], input);
                    }
                    this.addFiles(input.files);
                } else if (input.value.length > 0) {
                    this.addFiles(input);
                }
                miu.each(this._buttons, function(idx, button) {
                    button.reset();
                });
            },
            _onProgress: function(id, name, loaded, total) {
                this._totalProgress && this._totalProgress.onIndividualProgress(id, loaded, total);
            },
            _onSubmit: function(id, name) {},
            _onSubmitCallbackSuccess: function(id, name) {
                this._onSubmit.apply(this, arguments);
                this._uploadData.setStatus(id, miu.status.SUBMITTED);
                this._onSubmitted.apply(this, arguments);
                if (this._options.autoUpload) {
                    this._options.callbacks.onSubmitted.apply(this, arguments);
                    this._uploadFile(id);
                } else {
                    this._storeForLater(id);
                    this._options.callbacks.onSubmitted.apply(this, arguments);
                }
            },
            _onSubmitDelete: function(id, onSuccessCallback, additionalMandatedParams) {
                var uuid = this.getUuid(id), adjustedOnSuccessCallback;
                if (onSuccessCallback) {
                    adjustedOnSuccessCallback = miu.bind(onSuccessCallback, this, id, uuid, additionalMandatedParams);
                }
                if (this._isDeletePossible()) {
                    this._handleCheckedCallback({
                        name: "onSubmitDelete",
                        callback: miu.bind(this._options.callbacks.onSubmitDelete, this, id),
                        onSuccess: adjustedOnSuccessCallback || miu.bind(this._deleteHandler.sendDelete, this, id, uuid, additionalMandatedParams),
                        identifier: id
                    });
                    return true;
                } else {
                    this.log("Delete request ignored for ID " + id + ", delete feature is disabled or request not possible " + "due to CORS on a user agent that does not support pre-flighting.", "warn");
                    return false;
                }
            },
            _onSubmitted: function(id) {},
            _onTotalProgress: function(loaded, total) {
                this._options.callbacks.onTotalProgress(loaded, total);
            },
            _onUploadPrep: function(id) {},
            _onUpload: function(id, name) {
                this._uploadData.setStatus(id, miu.status.UPLOADING);
            },
            _onUploadChunk: function(id, chunkData) {},
            _onUploadStatusChange: function(id, oldStatus, newStatus) {
                if (newStatus === miu.status.PAUSED) {
                    clearTimeout(this._retryTimeouts[id]);
                }
            },
            _onValidateBatchCallbackFailure: function(fileWrappers) {
                var self = this;
                miu.each(fileWrappers, function(idx, fileWrapper) {
                    self._fileOrBlobRejected(fileWrapper.id);
                });
            },
            _onValidateBatchCallbackSuccess: function(validationDescriptors, items, params, endpoint, button) {
                var errorMessage, itemLimit = this._currentItemLimit, proposedNetFilesUploadedOrQueued = this._netUploadedOrQueued;
                if (itemLimit === 0 || proposedNetFilesUploadedOrQueued <= itemLimit) {
                    if (items.length > 0) {
                        this._handleCheckedCallback({
                            name: "onValidate",
                            callback: miu.bind(this._options.callbacks.onValidate, this, validationDescriptors[0], button),
                            onSuccess: miu.bind(this._onValidateCallbackSuccess, this, items, 0, params, endpoint),
                            onFailure: miu.bind(this._onValidateCallbackFailure, this, items, 0, params, endpoint),
                            identifier: "Item '" + items[0].file.name + "', size: " + items[0].file.size
                        });
                    } else {
                        this._itemError("noFilesError");
                    }
                } else {
                    this._onValidateBatchCallbackFailure(items);
                    errorMessage = this._options.messages.tooManyItemsError.replace(/\{netItems\}/g, proposedNetFilesUploadedOrQueued).replace(/\{itemLimit\}/g, itemLimit);
                    this._batchError(errorMessage);
                }
            },
            _onValidateCallbackFailure: function(items, index, params, endpoint) {
                var nextIndex = index + 1;
                this._fileOrBlobRejected(items[index].id, items[index].file.name);
                this._maybeProcessNextItemAfterOnValidateCallback(false, items, nextIndex, params, endpoint);
            },
            _onValidateCallbackSuccess: function(items, index, params, endpoint) {
                var self = this, nextIndex = index + 1, validationDescriptor = this._getValidationDescriptor(items[index]);
                this._validateFileOrBlobData(items[index], validationDescriptor).then(function() {
                    self._upload(items[index].id, params, endpoint);
                    self._maybeProcessNextItemAfterOnValidateCallback(true, items, nextIndex, params, endpoint);
                }, function() {
                    self._maybeProcessNextItemAfterOnValidateCallback(false, items, nextIndex, params, endpoint);
                });
            },
            _prepareItemsForUpload: function(items, params, endpoint) {
                if (items.length === 0) {
                    this._itemError("noFilesError");
                    return;
                }
                var validationDescriptors = this._getValidationDescriptors(items), buttonId = this._getButtonId(items[0].file), button = this._getButton(buttonId);
                this._handleCheckedCallback({
                    name: "onValidateBatch",
                    callback: miu.bind(this._options.callbacks.onValidateBatch, this, validationDescriptors, button),
                    onSuccess: miu.bind(this._onValidateBatchCallbackSuccess, this, validationDescriptors, items, params, endpoint, button),
                    onFailure: miu.bind(this._onValidateBatchCallbackFailure, this, items),
                    identifier: "batch validation"
                });
            },
            _preventLeaveInProgress: function() {
                var self = this;
                this._disposeSupport.attach(window, "beforeunload", function(e) {
                    if (self.getInProgress()) {
                        e = e || window.event;
                        e.returnValue = self._options.messages.onLeave;
                        return self._options.messages.onLeave;
                    }
                });
            },
            _refreshSessionData: function() {
                var self = this, options = this._options.session;
                if (miu.Session && this._options.session.endpoint != null) {
                    if (!this._session) {
                        miu.extend(options, {
                            cors: this._options.cors
                        });
                        options.log = miu.bind(this.log, this);
                        options.addFileRecord = miu.bind(this._addCannedFile, this);
                        this._session = new miu.Session(options);
                    }
                    setTimeout(function() {
                        self._session.refresh().then(function(response, xhrOrXdr) {
                            self._sessionRequestComplete();
                            self._options.callbacks.onSessionRequestComplete(response, true, xhrOrXdr);
                        }, function(response, xhrOrXdr) {
                            self._options.callbacks.onSessionRequestComplete(response, false, xhrOrXdr);
                        });
                    }, 0);
                }
            },
            _sessionRequestComplete: function() {},
            _setSize: function(id, newSize) {
                this._uploadData.updateSize(id, newSize);
                this._totalProgress && this._totalProgress.onNewSize(id);
            },
            _shouldAutoRetry: function(id, name, responseJSON) {
                var uploadData = this._uploadData.retrieve({
                    id: id
                });
                if (!this._preventRetries[id] && this._options.retry.enableAuto && uploadData.status !== miu.status.PAUSED) {
                    if (this._autoRetries[id] === undefined) {
                        this._autoRetries[id] = 0;
                    }
                    if (this._autoRetries[id] < this._options.retry.maxAutoAttempts) {
                        this._autoRetries[id] += 1;
                        return true;
                    }
                }
                return false;
            },
            _storeForLater: function(id) {
                this._storedIds.push(id);
            },
            _trackButton: function(id) {
                var buttonId;
                if (miu.supportedFeatures.ajaxUploading) {
                    buttonId = this._handler.getFile(id).miuButtonId;
                } else {
                    buttonId = this._getButtonId(this._handler.getInput(id));
                }
                if (buttonId) {
                    this._buttonIdsForFileIds[id] = buttonId;
                }
            },
            _updateFormSupportAndParams: function(formElementOrId) {
                this._options.form.element = formElementOrId;
                this._formSupport = miu.FormSupport && new miu.FormSupport(this._options.form, miu.bind(this.uploadStoredFiles, this), miu.bind(this.log, this));
                if (this._formSupport && this._formSupport.attachedToForm) {
                    this._paramsStore.addReadOnly(null, this._formSupport.getFormInputsAsObject);
                    this._options.autoUpload = this._formSupport.newAutoUpload;
                    if (this._formSupport.newEndpoint) {
                        this.setEndpoint(this._formSupport.newEndpoint);
                    }
                }
            },
            _upload: function(id, params, endpoint) {
                var name = this.getName(id);
                if (params) {
                    this.setParams(params, id);
                }
                if (endpoint) {
                    this.setEndpoint(endpoint, id);
                }
                this._handleCheckedCallback({
                    name: "onSubmit",
                    callback: miu.bind(this._options.callbacks.onSubmit, this, id, name),
                    onSuccess: miu.bind(this._onSubmitCallbackSuccess, this, id, name),
                    onFailure: miu.bind(this._fileOrBlobRejected, this, id, name),
                    identifier: id
                });
            },
            _uploadFile: function(id) {
                if (!this._handler.upload(id)) {
                    this._uploadData.setStatus(id, miu.status.QUEUED);
                }
            },
            _uploadStoredFiles: function() {
                var idToUpload, stillSubmitting, self = this;
                while (this._storedIds.length) {
                    idToUpload = this._storedIds.shift();
                    this._uploadFile(idToUpload);
                }
                stillSubmitting = this.getUploads({
                    status: miu.status.SUBMITTING
                }).length;
                if (stillSubmitting) {
                    miu.log("Still waiting for " + stillSubmitting + " files to clear submit queue. Will re-parse stored IDs array shortly.");
                    setTimeout(function() {
                        self._uploadStoredFiles();
                    }, 1e3);
                }
            },
            _validateFileOrBlobData: function(fileWrapper, validationDescriptor) {
                var self = this, file = function() {
                    if (fileWrapper.file instanceof miu.BlobProxy) {
                        return fileWrapper.file.referenceBlob;
                    }
                    return fileWrapper.file;
                }(), name = validationDescriptor.name, size = validationDescriptor.size, buttonId = this._getButtonId(fileWrapper.file), validationBase = this._getValidationBase(buttonId), validityChecker = new miu.Promise();
                validityChecker.then(function() {}, function() {
                    self._fileOrBlobRejected(fileWrapper.id, name);
                });
                if (miu.isFileOrInput(file) && !this._isAllowedExtension(validationBase.allowedExtensions, name)) {
                    this._itemError("typeError", name, file);
                    return validityChecker.failure();
                }
                if (!this._options.validation.allowEmpty && size === 0) {
                    this._itemError("emptyError", name, file);
                    return validityChecker.failure();
                }
                if (size > 0 && validationBase.sizeLimit && size > validationBase.sizeLimit) {
                    this._itemError("sizeError", name, file);
                    return validityChecker.failure();
                }
                if (size > 0 && size < validationBase.minSizeLimit) {
                    this._itemError("minSizeError", name, file);
                    return validityChecker.failure();
                }
                if (miu.ImageValidation && miu.supportedFeatures.imagePreviews && miu.isFile(file)) {
                    new miu.ImageValidation(file, miu.bind(self.log, self)).validate(validationBase.image).then(validityChecker.success, function(errorCode) {
                        self._itemError(errorCode + "ImageError", name, file);
                        validityChecker.failure();
                    });
                } else {
                    validityChecker.success();
                }
                return validityChecker;
            },
            _wrapCallbacks: function() {
                var self, safeCallback, prop;
                self = this;
                safeCallback = function(name, callback, args) {
                    var errorMsg;
                    try {
                        return callback.apply(self, args);
                    } catch (exception) {
                        errorMsg = exception.message || exception.toString();
                        self.log("Caught exception in '" + name + "' callback - " + errorMsg, "error");
                    }
                };
                for (prop in this._options.callbacks) {
                    (function() {
                        var callbackName, callbackFunc;
                        callbackName = prop;
                        callbackFunc = self._options.callbacks[callbackName];
                        self._options.callbacks[callbackName] = function() {
                            return safeCallback(callbackName, callbackFunc, arguments);
                        };
                    })();
                }
            }
        };
    })();
    (function() {
        "use strict";
        miu.FineUploaderBasic = function(o) {
            var self = this;
            this._options = {
                debug: false,
                button: null,
                multiple: true,
                maxConnections: 3,
                disableCancelForFormUploads: false,
                autoUpload: true,
                request: {
                    customHeaders: {},
                    endpoint: "/server/upload",
                    filenameParam: "miufilename",
                    forceMultipart: true,
                    inputName: "miufile",
                    method: "POST",
                    params: {},
                    paramsInBody: true,
                    totalFileSizeName: "miutotalfilesize",
                    uuidName: "miuuuid"
                },
                validation: {
                    allowedExtensions: [],
                    sizeLimit: 0,
                    minSizeLimit: 0,
                    itemLimit: 0,
                    stopOnFirstInvalidFile: true,
                    acceptFiles: null,
                    image: {
                        maxHeight: 0,
                        maxWidth: 0,
                        minHeight: 0,
                        minWidth: 0
                    },
                    allowEmpty: false
                },
                callbacks: {
                    onSubmit: function(id, name) {},
                    onSubmitted: function(id, name) {},
                    onComplete: function(id, name, responseJSON, maybeXhr) {},
                    onAllComplete: function(successful, failed) {},
                    onCancel: function(id, name) {},
                    onUpload: function(id, name) {},
                    onUploadChunk: function(id, name, chunkData) {},
                    onUploadChunkSuccess: function(id, chunkData, responseJSON, xhr) {},
                    onResume: function(id, fileName, chunkData) {},
                    onProgress: function(id, name, loaded, total) {},
                    onTotalProgress: function(loaded, total) {},
                    onError: function(id, name, reason, maybeXhrOrXdr) {},
                    onAutoRetry: function(id, name, attemptNumber) {},
                    onManualRetry: function(id, name) {},
                    onValidateBatch: function(fileOrBlobData) {},
                    onValidate: function(fileOrBlobData) {},
                    onSubmitDelete: function(id) {},
                    onDelete: function(id) {},
                    onDeleteComplete: function(id, xhrOrXdr, isError) {},
                    onPasteReceived: function(blob) {},
                    onStatusChange: function(id, oldStatus, newStatus) {},
                    onSessionRequestComplete: function(response, success, xhrOrXdr) {}
                },
                messages: {
                    typeError: "{file} has an invalid extension. Valid extension(s): {extensions}.",
                    sizeError: "{file} is too large, maximum file size is {sizeLimit}.",
                    minSizeError: "{file} is too small, minimum file size is {minSizeLimit}.",
                    emptyError: "{file} is empty, please select files again without it.",
                    noFilesError: "No files to upload.",
                    tooManyItemsError: "Too many items ({netItems}) would be uploaded.  Item limit is {itemLimit}.",
                    maxHeightImageError: "Image is too tall.",
                    maxWidthImageError: "Image is too wide.",
                    minHeightImageError: "Image is not tall enough.",
                    minWidthImageError: "Image is not wide enough.",
                    retryFailTooManyItems: "Retry failed - you have reached your file limit.",
                    onLeave: "The files are being uploaded, if you leave now the upload will be canceled.",
                    unsupportedBrowserIos8Safari: "Unrecoverable error - this browser does not permit file uploading of any kind due to serious bugs in iOS8 Safari.  Please use iOS8 Chrome until Apple fixes these issues."
                },
                retry: {
                    enableAuto: false,
                    maxAutoAttempts: 3,
                    autoAttemptDelay: 5,
                    preventRetryResponseProperty: "preventRetry"
                },
                classes: {
                    buttonHover: "miu-upload-button-hover",
                    buttonFocus: "miu-upload-button-focus"
                },
                chunking: {
                    enabled: false,
                    concurrent: {
                        enabled: false
                    },
                    mandatory: false,
                    paramNames: {
                        partIndex: "miupartindex",
                        partByteOffset: "miupartbyteoffset",
                        chunkSize: "miuchunksize",
                        totalFileSize: "miutotalfilesize",
                        totalParts: "miutotalparts"
                    },
                    partSize: 2e6,
                    success: {
                        endpoint: null
                    }
                },
                resume: {
                    enabled: false,
                    recordsExpireIn: 7,
                    paramNames: {
                        resuming: "miuresume"
                    }
                },
                formatFileName: function(fileOrBlobName) {
                    return fileOrBlobName;
                },
                text: {
                    defaultResponseError: "Upload failure reason unknown",
                    fileInputTitle: "file input",
                    sizeSymbols: [ "kB", "MB", "GB", "TB", "PB", "EB" ]
                },
                deleteFile: {
                    enabled: false,
                    method: "DELETE",
                    endpoint: "/server/upload",
                    customHeaders: {},
                    params: {}
                },
                cors: {
                    expected: false,
                    sendCredentials: false,
                    allowXdr: false
                },
                blobs: {
                    defaultName: "misc_data"
                },
                paste: {
                    targetElement: null,
                    defaultName: "pasted_image"
                },
                camera: {
                    ios: false,
                    button: null
                },
                extraButtons: [],
                session: {
                    endpoint: null,
                    params: {},
                    customHeaders: {},
                    refreshOnReset: true
                },
                form: {
                    element: "miu-form",
                    autoUpload: false,
                    interceptSubmit: true
                },
                scaling: {
                    customResizer: null,
                    sendOriginal: true,
                    orient: true,
                    defaultType: null,
                    defaultQuality: 80,
                    failureText: "Failed to scale",
                    includeExif: false,
                    sizes: []
                },
                workarounds: {
                    iosEmptyVideos: true,
                    ios8SafariUploads: true,
                    ios8BrowserCrash: false
                }
            };
            miu.extend(this._options, o, true);
            this._buttons = [];
            this._extraButtonSpecs = {};
            this._buttonIdsForFileIds = [];
            this._wrapCallbacks();
            this._disposeSupport = new miu.DisposeSupport();
            this._storedIds = [];
            this._autoRetries = [];
            this._retryTimeouts = [];
            this._preventRetries = [];
            this._thumbnailUrls = [];
            this._netUploadedOrQueued = 0;
            this._netUploaded = 0;
            this._uploadData = this._createUploadDataTracker();
            this._initFormSupportAndParams();
            this._customHeadersStore = this._createStore(this._options.request.customHeaders);
            this._deleteFileCustomHeadersStore = this._createStore(this._options.deleteFile.customHeaders);
            this._deleteFileParamsStore = this._createStore(this._options.deleteFile.params);
            this._endpointStore = this._createStore(this._options.request.endpoint);
            this._deleteFileEndpointStore = this._createStore(this._options.deleteFile.endpoint);
            this._handler = this._createUploadHandler();
            this._deleteHandler = miu.DeleteFileAjaxRequester && this._createDeleteHandler();
            if (this._options.button) {
                this._defaultButtonId = this._createUploadButton({
                    element: this._options.button,
                    title: this._options.text.fileInputTitle
                }).getButtonId();
            }
            this._generateExtraButtonSpecs();
            this._handleCameraAccess();
            if (this._options.paste.targetElement) {
                if (miu.PasteSupport) {
                    this._pasteHandler = this._createPasteHandler();
                } else {
                    this.log("Paste support module not found", "error");
                }
            }
            this._preventLeaveInProgress();
            this._imageGenerator = miu.ImageGenerator && new miu.ImageGenerator(miu.bind(this.log, this));
            this._refreshSessionData();
            this._succeededSinceLastAllComplete = [];
            this._failedSinceLastAllComplete = [];
            this._scaler = miu.Scaler && new miu.Scaler(this._options.scaling, miu.bind(this.log, this)) || {};
            if (this._scaler.enabled) {
                this._customNewFileHandler = miu.bind(this._scaler.handleNewFile, this._scaler);
            }
            if (miu.TotalProgress && miu.supportedFeatures.progressBar) {
                this._totalProgress = new miu.TotalProgress(miu.bind(this._onTotalProgress, this), function(id) {
                    var entry = self._uploadData.retrieve({
                        id: id
                    });
                    return entry && entry.size || 0;
                });
            }
            this._currentItemLimit = this._options.validation.itemLimit;
        };
        miu.FineUploaderBasic.prototype = miu.basePublicApi;
        miu.extend(miu.FineUploaderBasic.prototype, miu.basePrivateApi);
    })();
    miu.AjaxRequester = function(o) {
        "use strict";
        var log, shouldParamsBeInQueryString, queue = [], requestData = {}, options = {
            acceptHeader: null,
            validMethods: [ "PATCH", "POST", "PUT" ],
            method: "POST",
            contentType: "application/x-www-form-urlencoded",
            maxConnections: 3,
            customHeaders: {},
            endpointStore: {},
            paramsStore: {},
            mandatedParams: {},
            allowXRequestedWithAndCacheControl: true,
            successfulResponseCodes: {
                DELETE: [ 200, 202, 204 ],
                PATCH: [ 200, 201, 202, 203, 204 ],
                POST: [ 200, 201, 202, 203, 204 ],
                PUT: [ 200, 201, 202, 203, 204 ],
                GET: [ 200 ]
            },
            cors: {
                expected: false,
                sendCredentials: false
            },
            log: function(str, level) {},
            onSend: function(id) {},
            onComplete: function(id, xhrOrXdr, isError) {},
            onProgress: null
        };
        miu.extend(options, o);
        log = options.log;
        if (miu.indexOf(options.validMethods, options.method) < 0) {
            throw new Error("'" + options.method + "' is not a supported method for this type of request!");
        }
        function isSimpleMethod() {
            return miu.indexOf([ "GET", "POST", "HEAD" ], options.method) >= 0;
        }
        function containsNonSimpleHeaders(headers) {
            var containsNonSimple = false;
            miu.each(containsNonSimple, function(idx, header) {
                if (miu.indexOf([ "Accept", "Accept-Language", "Content-Language", "Content-Type" ], header) < 0) {
                    containsNonSimple = true;
                    return false;
                }
            });
            return containsNonSimple;
        }
        function isXdr(xhr) {
            return options.cors.expected && xhr.withCredentials === undefined;
        }
        function getCorsAjaxTransport() {
            var xhrOrXdr;
            if (window.XMLHttpRequest || window.ActiveXObject) {
                xhrOrXdr = miu.createXhrInstance();
                if (xhrOrXdr.withCredentials === undefined) {
                    xhrOrXdr = new XDomainRequest();
                    xhrOrXdr.onload = function() {};
                    xhrOrXdr.onerror = function() {};
                    xhrOrXdr.ontimeout = function() {};
                    xhrOrXdr.onprogress = function() {};
                }
            }
            return xhrOrXdr;
        }
        function getXhrOrXdr(id, suppliedXhr) {
            var xhrOrXdr = requestData[id].xhr;
            if (!xhrOrXdr) {
                if (suppliedXhr) {
                    xhrOrXdr = suppliedXhr;
                } else {
                    if (options.cors.expected) {
                        xhrOrXdr = getCorsAjaxTransport();
                    } else {
                        xhrOrXdr = miu.createXhrInstance();
                    }
                }
                requestData[id].xhr = xhrOrXdr;
            }
            return xhrOrXdr;
        }
        function dequeue(id) {
            var i = miu.indexOf(queue, id), max = options.maxConnections, nextId;
            delete requestData[id];
            queue.splice(i, 1);
            if (queue.length >= max && i < max) {
                nextId = queue[max - 1];
                sendRequest(nextId);
            }
        }
        function onComplete(id, xdrError) {
            var xhr = getXhrOrXdr(id), method = options.method, isError = xdrError === true;
            dequeue(id);
            if (isError) {
                log(method + " request for " + id + " has failed", "error");
            } else if (!isXdr(xhr) && !isResponseSuccessful(xhr.status)) {
                isError = true;
                log(method + " request for " + id + " has failed - response code " + xhr.status, "error");
            }
            options.onComplete(id, xhr, isError);
        }
        function getParams(id) {
            var onDemandParams = requestData[id].additionalParams, mandatedParams = options.mandatedParams, params;
            if (options.paramsStore.get) {
                params = options.paramsStore.get(id);
            }
            if (onDemandParams) {
                miu.each(onDemandParams, function(name, val) {
                    params = params || {};
                    params[name] = val;
                });
            }
            if (mandatedParams) {
                miu.each(mandatedParams, function(name, val) {
                    params = params || {};
                    params[name] = val;
                });
            }
            return params;
        }
        function sendRequest(id, optXhr) {
            var xhr = getXhrOrXdr(id, optXhr), method = options.method, params = getParams(id), payload = requestData[id].payload, url;
            options.onSend(id);
            url = createUrl(id, params, requestData[id].additionalQueryParams);
            if (isXdr(xhr)) {
                xhr.onload = getXdrLoadHandler(id);
                xhr.onerror = getXdrErrorHandler(id);
            } else {
                xhr.onreadystatechange = getXhrReadyStateChangeHandler(id);
            }
            registerForUploadProgress(id);
            xhr.open(method, url, true);
            if (options.cors.expected && options.cors.sendCredentials && !isXdr(xhr)) {
                xhr.withCredentials = true;
            }
            setHeaders(id);
            log("Sending " + method + " request for " + id);
            if (payload) {
                xhr.send(payload);
            } else if (shouldParamsBeInQueryString || !params) {
                xhr.send();
            } else if (params && options.contentType && options.contentType.toLowerCase().indexOf("application/x-www-form-urlencoded") >= 0) {
                xhr.send(miu.obj2url(params, ""));
            } else if (params && options.contentType && options.contentType.toLowerCase().indexOf("application/json") >= 0) {
                xhr.send(JSON.stringify(params));
            } else {
                xhr.send(params);
            }
            return xhr;
        }
        function createUrl(id, params, additionalQueryParams) {
            var endpoint = options.endpointStore.get(id), addToPath = requestData[id].addToPath;
            if (addToPath != undefined) {
                endpoint += "/" + addToPath;
            }
            if (shouldParamsBeInQueryString && params) {
                endpoint = miu.obj2url(params, endpoint);
            }
            if (additionalQueryParams) {
                endpoint = miu.obj2url(additionalQueryParams, endpoint);
            }
            return endpoint;
        }
        function getXhrReadyStateChangeHandler(id) {
            return function() {
                if (getXhrOrXdr(id).readyState === 4) {
                    onComplete(id);
                }
            };
        }
        function registerForUploadProgress(id) {
            var onProgress = options.onProgress;
            if (onProgress) {
                getXhrOrXdr(id).upload.onprogress = function(e) {
                    if (e.lengthComputable) {
                        onProgress(id, e.loaded, e.total);
                    }
                };
            }
        }
        function getXdrLoadHandler(id) {
            return function() {
                onComplete(id);
            };
        }
        function getXdrErrorHandler(id) {
            return function() {
                onComplete(id, true);
            };
        }
        function setHeaders(id) {
            var xhr = getXhrOrXdr(id), customHeaders = options.customHeaders, onDemandHeaders = requestData[id].additionalHeaders || {}, method = options.method, allHeaders = {};
            if (!isXdr(xhr)) {
                options.acceptHeader && xhr.setRequestHeader("Accept", options.acceptHeader);
                if (options.allowXRequestedWithAndCacheControl) {
                    if (!options.cors.expected || (!isSimpleMethod() || containsNonSimpleHeaders(customHeaders))) {
                        xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                        xhr.setRequestHeader("Cache-Control", "no-cache");
                    }
                }
                if (options.contentType && (method === "POST" || method === "PUT")) {
                    xhr.setRequestHeader("Content-Type", options.contentType);
                }
                miu.extend(allHeaders, miu.isFunction(customHeaders) ? customHeaders(id) : customHeaders);
                miu.extend(allHeaders, onDemandHeaders);
                miu.each(allHeaders, function(name, val) {
                    xhr.setRequestHeader(name, val);
                });
            }
        }
        function isResponseSuccessful(responseCode) {
            return miu.indexOf(options.successfulResponseCodes[options.method], responseCode) >= 0;
        }
        function prepareToSend(id, optXhr, addToPath, additionalParams, additionalQueryParams, additionalHeaders, payload) {
            requestData[id] = {
                addToPath: addToPath,
                additionalParams: additionalParams,
                additionalQueryParams: additionalQueryParams,
                additionalHeaders: additionalHeaders,
                payload: payload
            };
            var len = queue.push(id);
            if (len <= options.maxConnections) {
                return sendRequest(id, optXhr);
            }
        }
        shouldParamsBeInQueryString = options.method === "GET" || options.method === "DELETE";
        miu.extend(this, {
            initTransport: function(id) {
                var path, params, headers, payload, cacheBuster, additionalQueryParams;
                return {
                    withPath: function(appendToPath) {
                        path = appendToPath;
                        return this;
                    },
                    withParams: function(additionalParams) {
                        params = additionalParams;
                        return this;
                    },
                    withQueryParams: function(_additionalQueryParams_) {
                        additionalQueryParams = _additionalQueryParams_;
                        return this;
                    },
                    withHeaders: function(additionalHeaders) {
                        headers = additionalHeaders;
                        return this;
                    },
                    withPayload: function(thePayload) {
                        payload = thePayload;
                        return this;
                    },
                    withCacheBuster: function() {
                        cacheBuster = true;
                        return this;
                    },
                    send: function(optXhr) {
                        if (cacheBuster && miu.indexOf([ "GET", "DELETE" ], options.method) >= 0) {
                            params.miutimestamp = new Date().getTime();
                        }
                        return prepareToSend(id, optXhr, path, params, additionalQueryParams, headers, payload);
                    }
                };
            },
            canceled: function(id) {
                dequeue(id);
            }
        });
    };
    miu.UploadHandler = function(spec) {
        "use strict";
        var proxy = spec.proxy, fileState = {}, onCancel = proxy.onCancel, getName = proxy.getName;
        miu.extend(this, {
            add: function(id, fileItem) {
                fileState[id] = fileItem;
                fileState[id].temp = {};
            },
            cancel: function(id) {
                var self = this, cancelFinalizationEffort = new miu.Promise(), onCancelRetVal = onCancel(id, getName(id), cancelFinalizationEffort);
                onCancelRetVal.then(function() {
                    if (self.isValid(id)) {
                        fileState[id].canceled = true;
                        self.expunge(id);
                    }
                    cancelFinalizationEffort.success();
                });
            },
            expunge: function(id) {
                delete fileState[id];
            },
            getThirdPartyFileId: function(id) {
                return fileState[id].key;
            },
            isValid: function(id) {
                return fileState[id] !== undefined;
            },
            reset: function() {
                fileState = {};
            },
            _getFileState: function(id) {
                return fileState[id];
            },
            _setThirdPartyFileId: function(id, thirdPartyFileId) {
                fileState[id].key = thirdPartyFileId;
            },
            _wasCanceled: function(id) {
                return !!fileState[id].canceled;
            }
        });
    };
    miu.UploadHandlerController = function(o, namespace) {
        "use strict";
        var controller = this, chunkingPossible = false, concurrentChunkingPossible = false, chunking, preventRetryResponse, log, handler, options = {
            paramsStore: {},
            maxConnections: 3,
            chunking: {
                enabled: false,
                multiple: {
                    enabled: false
                }
            },
            log: function(str, level) {},
            onProgress: function(id, fileName, loaded, total) {},
            onComplete: function(id, fileName, response, xhr) {},
            onCancel: function(id, fileName) {},
            onUploadPrep: function(id) {},
            onUpload: function(id, fileName) {},
            onUploadChunk: function(id, fileName, chunkData) {},
            onUploadChunkSuccess: function(id, chunkData, response, xhr) {},
            onAutoRetry: function(id, fileName, response, xhr) {},
            onResume: function(id, fileName, chunkData) {},
            onUuidChanged: function(id, newUuid) {},
            getName: function(id) {},
            setSize: function(id, newSize) {},
            isQueued: function(id) {},
            getIdsInProxyGroup: function(id) {},
            getIdsInBatch: function(id) {}
        }, chunked = {
            done: function(id, chunkIdx, response, xhr) {
                var chunkData = handler._getChunkData(id, chunkIdx);
                handler._getFileState(id).attemptingResume = false;
                delete handler._getFileState(id).temp.chunkProgress[chunkIdx];
                handler._getFileState(id).loaded += chunkData.size;
                options.onUploadChunkSuccess(id, handler._getChunkDataForCallback(chunkData), response, xhr);
            },
            finalize: function(id) {
                var size = options.getSize(id), name = options.getName(id);
                log("All chunks have been uploaded for " + id + " - finalizing....");
                handler.finalizeChunks(id).then(function(response, xhr) {
                    log("Finalize successful for " + id);
                    var normaizedResponse = upload.normalizeResponse(response, true);
                    options.onProgress(id, name, size, size);
                    handler._maybeDeletePersistedChunkData(id);
                    upload.cleanup(id, normaizedResponse, xhr);
                }, function(response, xhr) {
                    var normaizedResponse = upload.normalizeResponse(response, false);
                    log("Problem finalizing chunks for file ID " + id + " - " + normaizedResponse.error, "error");
                    if (normaizedResponse.reset) {
                        chunked.reset(id);
                    }
                    if (!options.onAutoRetry(id, name, normaizedResponse, xhr)) {
                        upload.cleanup(id, normaizedResponse, xhr);
                    }
                });
            },
            handleFailure: function(chunkIdx, id, response, xhr) {
                var name = options.getName(id);
                log("Chunked upload request failed for " + id + ", chunk " + chunkIdx);
                handler.clearCachedChunk(id, chunkIdx);
                var responseToReport = upload.normalizeResponse(response, false), inProgressIdx;
                if (responseToReport.reset) {
                    chunked.reset(id);
                } else {
                    inProgressIdx = miu.indexOf(handler._getFileState(id).chunking.inProgress, chunkIdx);
                    if (inProgressIdx >= 0) {
                        handler._getFileState(id).chunking.inProgress.splice(inProgressIdx, 1);
                        handler._getFileState(id).chunking.remaining.unshift(chunkIdx);
                    }
                }
                if (!handler._getFileState(id).temp.ignoreFailure) {
                    if (concurrentChunkingPossible) {
                        handler._getFileState(id).temp.ignoreFailure = true;
                        log(miu.format("Going to attempt to abort these chunks: {}. These are currently in-progress: {}.", JSON.stringify(Object.keys(handler._getXhrs(id))), JSON.stringify(handler._getFileState(id).chunking.inProgress)));
                        miu.each(handler._getXhrs(id), function(ckid, ckXhr) {
                            log(miu.format("Attempting to abort file {}.{}. XHR readyState {}. ", id, ckid, ckXhr.readyState));
                            ckXhr.abort();
                            ckXhr._cancelled = true;
                        });
                        handler.moveInProgressToRemaining(id);
                        connectionManager.free(id, true);
                    }
                    if (!options.onAutoRetry(id, name, responseToReport, xhr)) {
                        upload.cleanup(id, responseToReport, xhr);
                    }
                }
            },
            hasMoreParts: function(id) {
                return !!handler._getFileState(id).chunking.remaining.length;
            },
            nextPart: function(id) {
                var nextIdx = handler._getFileState(id).chunking.remaining.shift();
                if (nextIdx >= handler._getTotalChunks(id)) {
                    nextIdx = null;
                }
                return nextIdx;
            },
            reset: function(id) {
                log("Server or callback has ordered chunking effort to be restarted on next attempt for item ID " + id, "error");
                handler._maybeDeletePersistedChunkData(id);
                handler.reevaluateChunking(id);
                handler._getFileState(id).loaded = 0;
            },
            sendNext: function(id) {
                var size = options.getSize(id), name = options.getName(id), chunkIdx = chunked.nextPart(id), chunkData = handler._getChunkData(id, chunkIdx), resuming = handler._getFileState(id).attemptingResume, inProgressChunks = handler._getFileState(id).chunking.inProgress || [];
                if (handler._getFileState(id).loaded == null) {
                    handler._getFileState(id).loaded = 0;
                }
                if (resuming && options.onResume(id, name, chunkData) === false) {
                    chunked.reset(id);
                    chunkIdx = chunked.nextPart(id);
                    chunkData = handler._getChunkData(id, chunkIdx);
                    resuming = false;
                }
                if (chunkIdx == null && inProgressChunks.length === 0) {
                    chunked.finalize(id);
                } else {
                    log(miu.format("Sending chunked upload request for item {}.{}, bytes {}-{} of {}.", id, chunkIdx, chunkData.start + 1, chunkData.end, size));
                    options.onUploadChunk(id, name, handler._getChunkDataForCallback(chunkData));
                    inProgressChunks.push(chunkIdx);
                    handler._getFileState(id).chunking.inProgress = inProgressChunks;
                    if (concurrentChunkingPossible) {
                        connectionManager.open(id, chunkIdx);
                    }
                    if (concurrentChunkingPossible && connectionManager.available() && handler._getFileState(id).chunking.remaining.length) {
                        chunked.sendNext(id);
                    }
                    if (chunkData.blob.size === 0) {
                        log(miu.format("Chunk {} for file {} will not be uploaded, zero sized chunk.", chunkIdx, id), "error");
                        chunked.handleFailure(chunkIdx, id, "File is no longer available", null);
                    } else {
                        handler.uploadChunk(id, chunkIdx, resuming).then(function success(response, xhr) {
                            log("Chunked upload request succeeded for " + id + ", chunk " + chunkIdx);
                            handler.clearCachedChunk(id, chunkIdx);
                            var inProgressChunks = handler._getFileState(id).chunking.inProgress || [], responseToReport = upload.normalizeResponse(response, true), inProgressChunkIdx = miu.indexOf(inProgressChunks, chunkIdx);
                            log(miu.format("Chunk {} for file {} uploaded successfully.", chunkIdx, id));
                            chunked.done(id, chunkIdx, responseToReport, xhr);
                            if (inProgressChunkIdx >= 0) {
                                inProgressChunks.splice(inProgressChunkIdx, 1);
                            }
                            handler._maybePersistChunkedState(id);
                            if (!chunked.hasMoreParts(id) && inProgressChunks.length === 0) {
                                chunked.finalize(id);
                            } else if (chunked.hasMoreParts(id)) {
                                chunked.sendNext(id);
                            } else {
                                log(miu.format("File ID {} has no more chunks to send and these chunk indexes are still marked as in-progress: {}", id, JSON.stringify(inProgressChunks)));
                            }
                        }, function failure(response, xhr) {
                            chunked.handleFailure(chunkIdx, id, response, xhr);
                        }).done(function() {
                            handler.clearXhr(id, chunkIdx);
                        });
                    }
                }
            }
        }, connectionManager = {
            _open: [],
            _openChunks: {},
            _waiting: [],
            available: function() {
                var max = options.maxConnections, openChunkEntriesCount = 0, openChunksCount = 0;
                miu.each(connectionManager._openChunks, function(fileId, openChunkIndexes) {
                    openChunkEntriesCount++;
                    openChunksCount += openChunkIndexes.length;
                });
                return max - (connectionManager._open.length - openChunkEntriesCount + openChunksCount);
            },
            free: function(id, dontAllowNext) {
                var allowNext = !dontAllowNext, waitingIndex = miu.indexOf(connectionManager._waiting, id), connectionsIndex = miu.indexOf(connectionManager._open, id), nextId;
                delete connectionManager._openChunks[id];
                if (upload.getProxyOrBlob(id) instanceof miu.BlobProxy) {
                    log("Generated blob upload has ended for " + id + ", disposing generated blob.");
                    delete handler._getFileState(id).file;
                }
                if (waitingIndex >= 0) {
                    connectionManager._waiting.splice(waitingIndex, 1);
                } else if (allowNext && connectionsIndex >= 0) {
                    connectionManager._open.splice(connectionsIndex, 1);
                    nextId = connectionManager._waiting.shift();
                    if (nextId >= 0) {
                        connectionManager._open.push(nextId);
                        upload.start(nextId);
                    }
                }
            },
            getWaitingOrConnected: function() {
                var waitingOrConnected = [];
                miu.each(connectionManager._openChunks, function(fileId, chunks) {
                    if (chunks && chunks.length) {
                        waitingOrConnected.push(parseInt(fileId));
                    }
                });
                miu.each(connectionManager._open, function(idx, fileId) {
                    if (!connectionManager._openChunks[fileId]) {
                        waitingOrConnected.push(parseInt(fileId));
                    }
                });
                waitingOrConnected = waitingOrConnected.concat(connectionManager._waiting);
                return waitingOrConnected;
            },
            isUsingConnection: function(id) {
                return miu.indexOf(connectionManager._open, id) >= 0;
            },
            open: function(id, chunkIdx) {
                if (chunkIdx == null) {
                    connectionManager._waiting.push(id);
                }
                if (connectionManager.available()) {
                    if (chunkIdx == null) {
                        connectionManager._waiting.pop();
                        connectionManager._open.push(id);
                    } else {
                        (function() {
                            var openChunksEntry = connectionManager._openChunks[id] || [];
                            openChunksEntry.push(chunkIdx);
                            connectionManager._openChunks[id] = openChunksEntry;
                        })();
                    }
                    return true;
                }
                return false;
            },
            reset: function() {
                connectionManager._waiting = [];
                connectionManager._open = [];
            }
        }, simple = {
            send: function(id, name) {
                handler._getFileState(id).loaded = 0;
                log("Sending simple upload request for " + id);
                handler.uploadFile(id).then(function(response, optXhr) {
                    log("Simple upload request succeeded for " + id);
                    var responseToReport = upload.normalizeResponse(response, true), size = options.getSize(id);
                    options.onProgress(id, name, size, size);
                    upload.maybeNewUuid(id, responseToReport);
                    upload.cleanup(id, responseToReport, optXhr);
                }, function(response, optXhr) {
                    log("Simple upload request failed for " + id);
                    var responseToReport = upload.normalizeResponse(response, false);
                    if (!options.onAutoRetry(id, name, responseToReport, optXhr)) {
                        upload.cleanup(id, responseToReport, optXhr);
                    }
                });
            }
        }, upload = {
            cancel: function(id) {
                log("Cancelling " + id);
                options.paramsStore.remove(id);
                connectionManager.free(id);
            },
            cleanup: function(id, response, optXhr) {
                var name = options.getName(id);
                options.onComplete(id, name, response, optXhr);
                if (handler._getFileState(id)) {
                    handler._clearXhrs && handler._clearXhrs(id);
                }
                connectionManager.free(id);
            },
            getProxyOrBlob: function(id) {
                return handler.getProxy && handler.getProxy(id) || handler.getFile && handler.getFile(id);
            },
            initHandler: function() {
                var handlerType = namespace ? miu[namespace] : miu.traditional, handlerModuleSubtype = miu.supportedFeatures.ajaxUploading ? "Xhr" : "Form";
                handler = new handlerType[handlerModuleSubtype + "UploadHandler"](options, {
                    getDataByUuid: options.getDataByUuid,
                    getName: options.getName,
                    getSize: options.getSize,
                    getUuid: options.getUuid,
                    log: log,
                    onCancel: options.onCancel,
                    onProgress: options.onProgress,
                    onUuidChanged: options.onUuidChanged
                });
                if (handler._removeExpiredChunkingRecords) {
                    handler._removeExpiredChunkingRecords();
                }
            },
            isDeferredEligibleForUpload: function(id) {
                return options.isQueued(id);
            },
            maybeDefer: function(id, blob) {
                if (blob && !handler.getFile(id) && blob instanceof miu.BlobProxy) {
                    options.onUploadPrep(id);
                    log("Attempting to generate a blob on-demand for " + id);
                    blob.create().then(function(generatedBlob) {
                        log("Generated an on-demand blob for " + id);
                        handler.updateBlob(id, generatedBlob);
                        options.setSize(id, generatedBlob.size);
                        handler.reevaluateChunking(id);
                        upload.maybeSendDeferredFiles(id);
                    }, function(errorMessage) {
                        var errorResponse = {};
                        if (errorMessage) {
                            errorResponse.error = errorMessage;
                        }
                        log(miu.format("Failed to generate blob for ID {}.  Error message: {}.", id, errorMessage), "error");
                        options.onComplete(id, options.getName(id), miu.extend(errorResponse, preventRetryResponse), null);
                        upload.maybeSendDeferredFiles(id);
                        connectionManager.free(id);
                    });
                } else {
                    return upload.maybeSendDeferredFiles(id);
                }
                return false;
            },
            maybeSendDeferredFiles: function(id) {
                var idsInGroup = options.getIdsInProxyGroup(id), uploadedThisId = false;
                if (idsInGroup && idsInGroup.length) {
                    log("Maybe ready to upload proxy group file " + id);
                    miu.each(idsInGroup, function(idx, idInGroup) {
                        if (upload.isDeferredEligibleForUpload(idInGroup) && !!handler.getFile(idInGroup)) {
                            uploadedThisId = idInGroup === id;
                            upload.now(idInGroup);
                        } else if (upload.isDeferredEligibleForUpload(idInGroup)) {
                            return false;
                        }
                    });
                } else {
                    uploadedThisId = true;
                    upload.now(id);
                }
                return uploadedThisId;
            },
            maybeNewUuid: function(id, response) {
                if (response.newUuid !== undefined) {
                    options.onUuidChanged(id, response.newUuid);
                }
            },
            normalizeResponse: function(originalResponse, successful) {
                var response = originalResponse;
                if (!miu.isObject(originalResponse)) {
                    response = {};
                    if (miu.isString(originalResponse) && !successful) {
                        response.error = originalResponse;
                    }
                }
                response.success = successful;
                return response;
            },
            now: function(id) {
                var name = options.getName(id);
                if (!controller.isValid(id)) {
                    throw new miu.Error(id + " is not a valid file ID to upload!");
                }
                options.onUpload(id, name);
                if (chunkingPossible && handler._shouldChunkThisFile(id)) {
                    chunked.sendNext(id);
                } else {
                    simple.send(id, name);
                }
            },
            start: function(id) {
                var blobToUpload = upload.getProxyOrBlob(id);
                if (blobToUpload) {
                    return upload.maybeDefer(id, blobToUpload);
                } else {
                    upload.now(id);
                    return true;
                }
            }
        };
        miu.extend(this, {
            add: function(id, file) {
                handler.add.apply(this, arguments);
            },
            upload: function(id) {
                if (connectionManager.open(id)) {
                    return upload.start(id);
                }
                return false;
            },
            retry: function(id) {
                if (concurrentChunkingPossible) {
                    handler._getFileState(id).temp.ignoreFailure = false;
                }
                if (connectionManager.isUsingConnection(id)) {
                    return upload.start(id);
                } else {
                    return controller.upload(id);
                }
            },
            cancel: function(id) {
                var cancelRetVal = handler.cancel(id);
                if (miu.isGenericPromise(cancelRetVal)) {
                    cancelRetVal.then(function() {
                        upload.cancel(id);
                    });
                } else if (cancelRetVal !== false) {
                    upload.cancel(id);
                }
            },
            cancelAll: function() {
                var waitingOrConnected = connectionManager.getWaitingOrConnected(), i;
                if (waitingOrConnected.length) {
                    for (i = waitingOrConnected.length - 1; i >= 0; i--) {
                        controller.cancel(waitingOrConnected[i]);
                    }
                }
                connectionManager.reset();
            },
            getFile: function(id) {
                if (handler.getProxy && handler.getProxy(id)) {
                    return handler.getProxy(id).referenceBlob;
                }
                return handler.getFile && handler.getFile(id);
            },
            isProxied: function(id) {
                return !!(handler.getProxy && handler.getProxy(id));
            },
            getInput: function(id) {
                if (handler.getInput) {
                    return handler.getInput(id);
                }
            },
            reset: function() {
                log("Resetting upload handler");
                controller.cancelAll();
                connectionManager.reset();
                handler.reset();
            },
            expunge: function(id) {
                if (controller.isValid(id)) {
                    return handler.expunge(id);
                }
            },
            isValid: function(id) {
                return handler.isValid(id);
            },
            getResumableFilesData: function() {
                if (handler.getResumableFilesData) {
                    return handler.getResumableFilesData();
                }
                return [];
            },
            getThirdPartyFileId: function(id) {
                if (controller.isValid(id)) {
                    return handler.getThirdPartyFileId(id);
                }
            },
            pause: function(id) {
                if (controller.isResumable(id) && handler.pause && controller.isValid(id) && handler.pause(id)) {
                    connectionManager.free(id);
                    handler.moveInProgressToRemaining(id);
                    return true;
                }
                return false;
            },
            isResumable: function(id) {
                return !!handler.isResumable && handler.isResumable(id);
            }
        });
        miu.extend(options, o);
        log = options.log;
        chunkingPossible = options.chunking.enabled && miu.supportedFeatures.chunking;
        concurrentChunkingPossible = chunkingPossible && options.chunking.concurrent.enabled;
        preventRetryResponse = function() {
            var response = {};
            response[options.preventRetryParam] = true;
            return response;
        }();
        upload.initHandler();
    };
    miu.WindowReceiveMessage = function(o) {
        "use strict";
        var options = {
            log: function(message, level) {}
        }, callbackWrapperDetachers = {};
        miu.extend(options, o);
        miu.extend(this, {
            receiveMessage: function(id, callback) {
                var onMessageCallbackWrapper = function(event) {
                    callback(event.data);
                };
                if (window.postMessage) {
                    callbackWrapperDetachers[id] = miu(window).attach("message", onMessageCallbackWrapper);
                } else {
                    log("iframe message passing not supported in this browser!", "error");
                }
            },
            stopReceivingMessages: function(id) {
                if (window.postMessage) {
                    var detacher = callbackWrapperDetachers[id];
                    if (detacher) {
                        detacher();
                    }
                }
            }
        });
    };
    miu.FormUploadHandler = function(spec) {
        "use strict";
        var options = spec.options, handler = this, proxy = spec.proxy, formHandlerInstanceId = miu.getUniqueId(), onloadCallbacks = {}, detachLoadEvents = {}, postMessageCallbackTimers = {}, isCors = options.isCors, inputName = options.inputName, getUuid = proxy.getUuid, log = proxy.log, corsMessageReceiver = new miu.WindowReceiveMessage({
            log: log
        });
        function expungeFile(id) {
            delete detachLoadEvents[id];
            if (isCors) {
                clearTimeout(postMessageCallbackTimers[id]);
                delete postMessageCallbackTimers[id];
                corsMessageReceiver.stopReceivingMessages(id);
            }
            var iframe = document.getElementById(handler._getIframeName(id));
            if (iframe) {
                iframe.setAttribute("src", "javascript:false;");
                miu(iframe).remove();
            }
        }
        function getFileIdForIframeName(iframeName) {
            return iframeName.split("_")[0];
        }
        function initIframeForUpload(name) {
            var iframe = miu.toElement("<iframe src='javascript:false;' name='" + name + "' />");
            iframe.setAttribute("id", name);
            iframe.style.display = "none";
            document.body.appendChild(iframe);
            return iframe;
        }
        function registerPostMessageCallback(iframe, callback) {
            var iframeName = iframe.id, fileId = getFileIdForIframeName(iframeName), uuid = getUuid(fileId);
            onloadCallbacks[uuid] = callback;
            detachLoadEvents[fileId] = miu(iframe).attach("load", function() {
                if (handler.getInput(fileId)) {
                    log("Received iframe load event for CORS upload request (iframe name " + iframeName + ")");
                    postMessageCallbackTimers[iframeName] = setTimeout(function() {
                        var errorMessage = "No valid message received from loaded iframe for iframe name " + iframeName;
                        log(errorMessage, "error");
                        callback({
                            error: errorMessage
                        });
                    }, 1e3);
                }
            });
            corsMessageReceiver.receiveMessage(iframeName, function(message) {
                log("Received the following window message: '" + message + "'");
                var fileId = getFileIdForIframeName(iframeName), response = handler._parseJsonResponse(message), uuid = response.uuid, onloadCallback;
                if (uuid && onloadCallbacks[uuid]) {
                    log("Handling response for iframe name " + iframeName);
                    clearTimeout(postMessageCallbackTimers[iframeName]);
                    delete postMessageCallbackTimers[iframeName];
                    handler._detachLoadEvent(iframeName);
                    onloadCallback = onloadCallbacks[uuid];
                    delete onloadCallbacks[uuid];
                    corsMessageReceiver.stopReceivingMessages(iframeName);
                    onloadCallback(response);
                } else if (!uuid) {
                    log("'" + message + "' does not contain a UUID - ignoring.");
                }
            });
        }
        miu.extend(this, new miu.UploadHandler(spec));
        miu.override(this, function(super_) {
            return {
                add: function(id, fileInput) {
                    super_.add(id, {
                        input: fileInput
                    });
                    fileInput.setAttribute("name", inputName);
                    if (fileInput.parentNode) {
                        miu(fileInput).remove();
                    }
                },
                expunge: function(id) {
                    expungeFile(id);
                    super_.expunge(id);
                },
                isValid: function(id) {
                    return super_.isValid(id) && handler._getFileState(id).input !== undefined;
                }
            };
        });
        miu.extend(this, {
            getInput: function(id) {
                return handler._getFileState(id).input;
            },
            _attachLoadEvent: function(iframe, callback) {
                var responseDescriptor;
                if (isCors) {
                    registerPostMessageCallback(iframe, callback);
                } else {
                    detachLoadEvents[iframe.id] = miu(iframe).attach("load", function() {
                        log("Received response for " + iframe.id);
                        if (!iframe.parentNode) {
                            return;
                        }
                        try {
                            if (iframe.contentDocument && iframe.contentDocument.body && iframe.contentDocument.body.innerHTML == "false") {
                                return;
                            }
                        } catch (error) {
                            log("Error when attempting to access iframe during handling of upload response (" + error.message + ")", "error");
                            responseDescriptor = {
                                success: false
                            };
                        }
                        callback(responseDescriptor);
                    });
                }
            },
            _createIframe: function(id) {
                var iframeName = handler._getIframeName(id);
                return initIframeForUpload(iframeName);
            },
            _detachLoadEvent: function(id) {
                if (detachLoadEvents[id] !== undefined) {
                    detachLoadEvents[id]();
                    delete detachLoadEvents[id];
                }
            },
            _getIframeName: function(fileId) {
                return fileId + "_" + formHandlerInstanceId;
            },
            _initFormForUpload: function(spec) {
                var method = spec.method, endpoint = spec.endpoint, params = spec.params, paramsInBody = spec.paramsInBody, targetName = spec.targetName, form = miu.toElement("<form method='" + method + "' enctype='multipart/form-data'></form>"), url = endpoint;
                if (paramsInBody) {
                    miu.obj2Inputs(params, form);
                } else {
                    url = miu.obj2url(params, endpoint);
                }
                form.setAttribute("action", url);
                form.setAttribute("target", targetName);
                form.style.display = "none";
                document.body.appendChild(form);
                return form;
            },
            _parseJsonResponse: function(innerHtmlOrMessage) {
                var response = {};
                try {
                    response = miu.parseJson(innerHtmlOrMessage);
                } catch (error) {
                    log("Error when attempting to parse iframe upload response (" + error.message + ")", "error");
                }
                return response;
            }
        });
    };
    miu.XhrUploadHandler = function(spec) {
        "use strict";
        var handler = this, namespace = spec.options.namespace, proxy = spec.proxy, chunking = spec.options.chunking, resume = spec.options.resume, chunkFiles = chunking && spec.options.chunking.enabled && miu.supportedFeatures.chunking, resumeEnabled = resume && spec.options.resume.enabled && chunkFiles && miu.supportedFeatures.resume, getName = proxy.getName, getSize = proxy.getSize, getUuid = proxy.getUuid, getEndpoint = proxy.getEndpoint, getDataByUuid = proxy.getDataByUuid, onUuidChanged = proxy.onUuidChanged, onProgress = proxy.onProgress, log = proxy.log;
        function abort(id) {
            miu.each(handler._getXhrs(id), function(xhrId, xhr) {
                var ajaxRequester = handler._getAjaxRequester(id, xhrId);
                xhr.onreadystatechange = null;
                xhr.upload.onprogress = null;
                xhr.abort();
                ajaxRequester && ajaxRequester.canceled && ajaxRequester.canceled(id);
            });
        }
        miu.extend(this, new miu.UploadHandler(spec));
        miu.override(this, function(super_) {
            return {
                add: function(id, blobOrProxy) {
                    if (miu.isFile(blobOrProxy) || miu.isBlob(blobOrProxy)) {
                        super_.add(id, {
                            file: blobOrProxy
                        });
                    } else if (blobOrProxy instanceof miu.BlobProxy) {
                        super_.add(id, {
                            proxy: blobOrProxy
                        });
                    } else {
                        throw new Error("Passed obj is not a File, Blob, or proxy");
                    }
                    handler._initTempState(id);
                    resumeEnabled && handler._maybePrepareForResume(id);
                },
                expunge: function(id) {
                    abort(id);
                    handler._maybeDeletePersistedChunkData(id);
                    handler._clearXhrs(id);
                    super_.expunge(id);
                }
            };
        });
        miu.extend(this, {
            clearCachedChunk: function(id, chunkIdx) {
                delete handler._getFileState(id).temp.cachedChunks[chunkIdx];
            },
            clearXhr: function(id, chunkIdx) {
                var tempState = handler._getFileState(id).temp;
                if (tempState.xhrs) {
                    delete tempState.xhrs[chunkIdx];
                }
                if (tempState.ajaxRequesters) {
                    delete tempState.ajaxRequesters[chunkIdx];
                }
            },
            finalizeChunks: function(id, responseParser) {
                var lastChunkIdx = handler._getTotalChunks(id) - 1, xhr = handler._getXhr(id, lastChunkIdx);
                if (responseParser) {
                    return new miu.Promise().success(responseParser(xhr), xhr);
                }
                return new miu.Promise().success({}, xhr);
            },
            getFile: function(id) {
                return handler.isValid(id) && handler._getFileState(id).file;
            },
            getProxy: function(id) {
                return handler.isValid(id) && handler._getFileState(id).proxy;
            },
            getResumableFilesData: function() {
                var resumableFilesData = [];
                handler._iterateResumeRecords(function(key, uploadData) {
                    handler.moveInProgressToRemaining(null, uploadData.chunking.inProgress, uploadData.chunking.remaining);
                    var data = {
                        name: uploadData.name,
                        remaining: uploadData.chunking.remaining,
                        size: uploadData.size,
                        uuid: uploadData.uuid
                    };
                    if (uploadData.key) {
                        data.key = uploadData.key;
                    }
                    resumableFilesData.push(data);
                });
                return resumableFilesData;
            },
            isResumable: function(id) {
                return !!chunking && handler.isValid(id) && !handler._getFileState(id).notResumable;
            },
            moveInProgressToRemaining: function(id, optInProgress, optRemaining) {
                var inProgress = optInProgress || handler._getFileState(id).chunking.inProgress, remaining = optRemaining || handler._getFileState(id).chunking.remaining;
                if (inProgress) {
                    log(miu.format("Moving these chunks from in-progress {}, to remaining.", JSON.stringify(inProgress)));
                    inProgress.reverse();
                    miu.each(inProgress, function(idx, chunkIdx) {
                        remaining.unshift(chunkIdx);
                    });
                    inProgress.length = 0;
                }
            },
            pause: function(id) {
                if (handler.isValid(id)) {
                    log(miu.format("Aborting XHR upload for {} '{}' due to pause instruction.", id, getName(id)));
                    handler._getFileState(id).paused = true;
                    abort(id);
                    return true;
                }
            },
            reevaluateChunking: function(id) {
                if (chunking && handler.isValid(id)) {
                    var state = handler._getFileState(id), totalChunks, i;
                    delete state.chunking;
                    state.chunking = {};
                    totalChunks = handler._getTotalChunks(id);
                    if (totalChunks > 1 || chunking.mandatory) {
                        state.chunking.enabled = true;
                        state.chunking.parts = totalChunks;
                        state.chunking.remaining = [];
                        for (i = 0; i < totalChunks; i++) {
                            state.chunking.remaining.push(i);
                        }
                        handler._initTempState(id);
                    } else {
                        state.chunking.enabled = false;
                    }
                }
            },
            updateBlob: function(id, newBlob) {
                if (handler.isValid(id)) {
                    handler._getFileState(id).file = newBlob;
                }
            },
            _clearXhrs: function(id) {
                var tempState = handler._getFileState(id).temp;
                miu.each(tempState.ajaxRequesters, function(chunkId) {
                    delete tempState.ajaxRequesters[chunkId];
                });
                miu.each(tempState.xhrs, function(chunkId) {
                    delete tempState.xhrs[chunkId];
                });
            },
            _createXhr: function(id, optChunkIdx) {
                return handler._registerXhr(id, optChunkIdx, miu.createXhrInstance());
            },
            _getAjaxRequester: function(id, optChunkIdx) {
                var chunkIdx = optChunkIdx == null ? -1 : optChunkIdx;
                return handler._getFileState(id).temp.ajaxRequesters[chunkIdx];
            },
            _getChunkData: function(id, chunkIndex) {
                var chunkSize = chunking.partSize, fileSize = getSize(id), fileOrBlob = handler.getFile(id), startBytes = chunkSize * chunkIndex, endBytes = startBytes + chunkSize >= fileSize ? fileSize : startBytes + chunkSize, totalChunks = handler._getTotalChunks(id), cachedChunks = this._getFileState(id).temp.cachedChunks, blob = cachedChunks[chunkIndex] || miu.sliceBlob(fileOrBlob, startBytes, endBytes);
                cachedChunks[chunkIndex] = blob;
                return {
                    part: chunkIndex,
                    start: startBytes,
                    end: endBytes,
                    count: totalChunks,
                    blob: blob,
                    size: endBytes - startBytes
                };
            },
            _getChunkDataForCallback: function(chunkData) {
                return {
                    partIndex: chunkData.part,
                    startByte: chunkData.start + 1,
                    endByte: chunkData.end,
                    totalParts: chunkData.count
                };
            },
            _getLocalStorageId: function(id) {
                var formatVersion = "5.0", name = getName(id), size = getSize(id), chunkSize = chunking.partSize, endpoint = getEndpoint(id);
                return miu.format("miu{}resume{}-{}-{}-{}-{}", namespace, formatVersion, name, size, chunkSize, endpoint);
            },
            _getMimeType: function(id) {
                return handler.getFile(id).type;
            },
            _getPersistableData: function(id) {
                return handler._getFileState(id).chunking;
            },
            _getTotalChunks: function(id) {
                if (chunking) {
                    var fileSize = getSize(id), chunkSize = chunking.partSize;
                    return Math.ceil(fileSize / chunkSize);
                }
            },
            _getXhr: function(id, optChunkIdx) {
                var chunkIdx = optChunkIdx == null ? -1 : optChunkIdx;
                return handler._getFileState(id).temp.xhrs[chunkIdx];
            },
            _getXhrs: function(id) {
                return handler._getFileState(id).temp.xhrs;
            },
            _iterateResumeRecords: function(callback) {
                if (resumeEnabled) {
                    miu.each(localStorage, function(key, item) {
                        if (key.indexOf(miu.format("miu{}resume", namespace)) === 0) {
                            var uploadData = JSON.parse(item);
                            callback(key, uploadData);
                        }
                    });
                }
            },
            _initTempState: function(id) {
                handler._getFileState(id).temp = {
                    ajaxRequesters: {},
                    chunkProgress: {},
                    xhrs: {},
                    cachedChunks: {}
                };
            },
            _markNotResumable: function(id) {
                handler._getFileState(id).notResumable = true;
            },
            _maybeDeletePersistedChunkData: function(id) {
                var localStorageId;
                if (resumeEnabled && handler.isResumable(id)) {
                    localStorageId = handler._getLocalStorageId(id);
                    if (localStorageId && localStorage.getItem(localStorageId)) {
                        localStorage.removeItem(localStorageId);
                        return true;
                    }
                }
                return false;
            },
            _maybePrepareForResume: function(id) {
                var state = handler._getFileState(id), localStorageId, persistedData;
                if (resumeEnabled && state.key === undefined) {
                    localStorageId = handler._getLocalStorageId(id);
                    persistedData = localStorage.getItem(localStorageId);
                    if (persistedData) {
                        persistedData = JSON.parse(persistedData);
                        if (getDataByUuid(persistedData.uuid)) {
                            handler._markNotResumable(id);
                        } else {
                            log(miu.format("Identified file with ID {} and name of {} as resumable.", id, getName(id)));
                            onUuidChanged(id, persistedData.uuid);
                            state.key = persistedData.key;
                            state.chunking = persistedData.chunking;
                            state.loaded = persistedData.loaded;
                            state.attemptingResume = true;
                            handler.moveInProgressToRemaining(id);
                        }
                    }
                }
            },
            _maybePersistChunkedState: function(id) {
                var state = handler._getFileState(id), localStorageId, persistedData;
                if (resumeEnabled && handler.isResumable(id)) {
                    localStorageId = handler._getLocalStorageId(id);
                    persistedData = {
                        name: getName(id),
                        size: getSize(id),
                        uuid: getUuid(id),
                        key: state.key,
                        chunking: state.chunking,
                        loaded: state.loaded,
                        lastUpdated: Date.now()
                    };
                    try {
                        localStorage.setItem(localStorageId, JSON.stringify(persistedData));
                    } catch (error) {
                        log(miu.format("Unable to save resume data for '{}' due to error: '{}'.", id, error.toString()), "warn");
                    }
                }
            },
            _registerProgressHandler: function(id, chunkIdx, chunkSize) {
                var xhr = handler._getXhr(id, chunkIdx), name = getName(id), progressCalculator = {
                    simple: function(loaded, total) {
                        var fileSize = getSize(id);
                        if (loaded === total) {
                            onProgress(id, name, fileSize, fileSize);
                        } else {
                            onProgress(id, name, loaded >= fileSize ? fileSize - 1 : loaded, fileSize);
                        }
                    },
                    chunked: function(loaded, total) {
                        var chunkProgress = handler._getFileState(id).temp.chunkProgress, totalSuccessfullyLoadedForFile = handler._getFileState(id).loaded, loadedForRequest = loaded, totalForRequest = total, totalFileSize = getSize(id), estActualChunkLoaded = loadedForRequest - (totalForRequest - chunkSize), totalLoadedForFile = totalSuccessfullyLoadedForFile;
                        chunkProgress[chunkIdx] = estActualChunkLoaded;
                        miu.each(chunkProgress, function(chunkIdx, chunkLoaded) {
                            totalLoadedForFile += chunkLoaded;
                        });
                        onProgress(id, name, totalLoadedForFile, totalFileSize);
                    }
                };
                xhr.upload.onprogress = function(e) {
                    if (e.lengthComputable) {
                        var type = chunkSize == null ? "simple" : "chunked";
                        progressCalculator[type](e.loaded, e.total);
                    }
                };
            },
            _registerXhr: function(id, optChunkIdx, xhr, optAjaxRequester) {
                var xhrsId = optChunkIdx == null ? -1 : optChunkIdx, tempState = handler._getFileState(id).temp;
                tempState.xhrs = tempState.xhrs || {};
                tempState.ajaxRequesters = tempState.ajaxRequesters || {};
                tempState.xhrs[xhrsId] = xhr;
                if (optAjaxRequester) {
                    tempState.ajaxRequesters[xhrsId] = optAjaxRequester;
                }
                return xhr;
            },
            _removeExpiredChunkingRecords: function() {
                var expirationDays = resume.recordsExpireIn;
                handler._iterateResumeRecords(function(key, uploadData) {
                    var expirationDate = new Date(uploadData.lastUpdated);
                    expirationDate.setDate(expirationDate.getDate() + expirationDays);
                    if (expirationDate.getTime() <= Date.now()) {
                        log("Removing expired resume record with key " + key);
                        localStorage.removeItem(key);
                    }
                });
            },
            _shouldChunkThisFile: function(id) {
                var state = handler._getFileState(id);
                if (!state.chunking) {
                    handler.reevaluateChunking(id);
                }
                return state.chunking.enabled;
            }
        });
    };
    miu.DeleteFileAjaxRequester = function(o) {
        "use strict";
        var requester, options = {
            method: "DELETE",
            uuidParamName: "miuuuid",
            endpointStore: {},
            maxConnections: 3,
            customHeaders: function(id) {
                return {};
            },
            paramsStore: {},
            cors: {
                expected: false,
                sendCredentials: false
            },
            log: function(str, level) {},
            onDelete: function(id) {},
            onDeleteComplete: function(id, xhrOrXdr, isError) {}
        };
        miu.extend(options, o);
        function getMandatedParams() {
            if (options.method.toUpperCase() === "POST") {
                return {
                    _method: "DELETE"
                };
            }
            return {};
        }
        requester = miu.extend(this, new miu.AjaxRequester({
            acceptHeader: "application/json",
            validMethods: [ "POST", "DELETE" ],
            method: options.method,
            endpointStore: options.endpointStore,
            paramsStore: options.paramsStore,
            mandatedParams: getMandatedParams(),
            maxConnections: options.maxConnections,
            customHeaders: function(id) {
                return options.customHeaders.get(id);
            },
            log: options.log,
            onSend: options.onDelete,
            onComplete: options.onDeleteComplete,
            cors: options.cors
        }));
        miu.extend(this, {
            sendDelete: function(id, uuid, additionalMandatedParams) {
                var additionalOptions = additionalMandatedParams || {};
                options.log("Submitting delete file request for " + id);
                if (options.method === "DELETE") {
                    requester.initTransport(id).withPath(uuid).withParams(additionalOptions).send();
                } else {
                    additionalOptions[options.uuidParamName] = uuid;
                    requester.initTransport(id).withParams(additionalOptions).send();
                }
            }
        });
    };
    (function() {
        function detectSubsampling(img) {
            var iw = img.naturalWidth, ih = img.naturalHeight, canvas = document.createElement("canvas"), ctx;
            if (iw * ih > 1024 * 1024) {
                canvas.width = canvas.height = 1;
                ctx = canvas.getContext("2d");
                ctx.drawImage(img, -iw + 1, 0);
                return ctx.getImageData(0, 0, 1, 1).data[3] === 0;
            } else {
                return false;
            }
        }
        function detectVerticalSquash(img, iw, ih) {
            var canvas = document.createElement("canvas"), sy = 0, ey = ih, py = ih, ctx, data, alpha, ratio;
            canvas.width = 1;
            canvas.height = ih;
            ctx = canvas.getContext("2d");
            ctx.drawImage(img, 0, 0);
            data = ctx.getImageData(0, 0, 1, ih).data;
            while (py > sy) {
                alpha = data[(py - 1) * 4 + 3];
                if (alpha === 0) {
                    ey = py;
                } else {
                    sy = py;
                }
                py = ey + sy >> 1;
            }
            ratio = py / ih;
            return ratio === 0 ? 1 : ratio;
        }
        function renderImageToDataURL(img, blob, options, doSquash) {
            var canvas = document.createElement("canvas"), mime = options.mime || "image/jpeg", promise = new miu.Promise();
            renderImageToCanvas(img, blob, canvas, options, doSquash).then(function() {
                promise.success(canvas.toDataURL(mime, options.quality || .8));
            });
            return promise;
        }
        function maybeCalculateDownsampledDimensions(spec) {
            var maxPixels = 5241e3;
            if (!miu.ios()) {
                throw new miu.Error("Downsampled dimensions can only be reliably calculated for iOS!");
            }
            if (spec.origHeight * spec.origWidth > maxPixels) {
                return {
                    newHeight: Math.round(Math.sqrt(maxPixels * (spec.origHeight / spec.origWidth))),
                    newWidth: Math.round(Math.sqrt(maxPixels * (spec.origWidth / spec.origHeight)))
                };
            }
        }
        function renderImageToCanvas(img, blob, canvas, options, doSquash) {
            var iw = img.naturalWidth, ih = img.naturalHeight, width = options.width, height = options.height, ctx = canvas.getContext("2d"), promise = new miu.Promise(), modifiedDimensions;
            ctx.save();
            if (options.resize) {
                return renderImageToCanvasWithCustomResizer({
                    blob: blob,
                    canvas: canvas,
                    image: img,
                    imageHeight: ih,
                    imageWidth: iw,
                    orientation: options.orientation,
                    resize: options.resize,
                    targetHeight: height,
                    targetWidth: width
                });
            }
            if (!miu.supportedFeatures.unlimitedScaledImageSize) {
                modifiedDimensions = maybeCalculateDownsampledDimensions({
                    origWidth: width,
                    origHeight: height
                });
                if (modifiedDimensions) {
                    miu.log(miu.format("Had to reduce dimensions due to device limitations from {}w / {}h to {}w / {}h", width, height, modifiedDimensions.newWidth, modifiedDimensions.newHeight), "warn");
                    width = modifiedDimensions.newWidth;
                    height = modifiedDimensions.newHeight;
                }
            }
            transformCoordinate(canvas, width, height, options.orientation);
            if (miu.ios()) {
                (function() {
                    if (detectSubsampling(img)) {
                        iw /= 2;
                        ih /= 2;
                    }
                    var d = 1024, tmpCanvas = document.createElement("canvas"), vertSquashRatio = doSquash ? detectVerticalSquash(img, iw, ih) : 1, dw = Math.ceil(d * width / iw), dh = Math.ceil(d * height / ih / vertSquashRatio), sy = 0, dy = 0, tmpCtx, sx, dx;
                    tmpCanvas.width = tmpCanvas.height = d;
                    tmpCtx = tmpCanvas.getContext("2d");
                    while (sy < ih) {
                        sx = 0;
                        dx = 0;
                        while (sx < iw) {
                            tmpCtx.clearRect(0, 0, d, d);
                            tmpCtx.drawImage(img, -sx, -sy);
                            ctx.drawImage(tmpCanvas, 0, 0, d, d, dx, dy, dw, dh);
                            sx += d;
                            dx += dw;
                        }
                        sy += d;
                        dy += dh;
                    }
                    ctx.restore();
                    tmpCanvas = tmpCtx = null;
                })();
            } else {
                ctx.drawImage(img, 0, 0, width, height);
            }
            canvas.miuImageRendered && canvas.miuImageRendered();
            promise.success();
            return promise;
        }
        function renderImageToCanvasWithCustomResizer(resizeInfo) {
            var blob = resizeInfo.blob, image = resizeInfo.image, imageHeight = resizeInfo.imageHeight, imageWidth = resizeInfo.imageWidth, orientation = resizeInfo.orientation, promise = new miu.Promise(), resize = resizeInfo.resize, sourceCanvas = document.createElement("canvas"), sourceCanvasContext = sourceCanvas.getContext("2d"), targetCanvas = resizeInfo.canvas, targetHeight = resizeInfo.targetHeight, targetWidth = resizeInfo.targetWidth;
            transformCoordinate(sourceCanvas, imageWidth, imageHeight, orientation);
            targetCanvas.height = targetHeight;
            targetCanvas.width = targetWidth;
            sourceCanvasContext.drawImage(image, 0, 0);
            resize({
                blob: blob,
                height: targetHeight,
                image: image,
                sourceCanvas: sourceCanvas,
                targetCanvas: targetCanvas,
                width: targetWidth
            }).then(function success() {
                targetCanvas.miuImageRendered && targetCanvas.miuImageRendered();
                promise.success();
            }, promise.failure);
            return promise;
        }
        function transformCoordinate(canvas, width, height, orientation) {
            switch (orientation) {
              case 5:
              case 6:
              case 7:
              case 8:
                canvas.width = height;
                canvas.height = width;
                break;

              default:
                canvas.width = width;
                canvas.height = height;
            }
            var ctx = canvas.getContext("2d");
            switch (orientation) {
              case 2:
                ctx.translate(width, 0);
                ctx.scale(-1, 1);
                break;

              case 3:
                ctx.translate(width, height);
                ctx.rotate(Math.PI);
                break;

              case 4:
                ctx.translate(0, height);
                ctx.scale(1, -1);
                break;

              case 5:
                ctx.rotate(.5 * Math.PI);
                ctx.scale(1, -1);
                break;

              case 6:
                ctx.rotate(.5 * Math.PI);
                ctx.translate(0, -height);
                break;

              case 7:
                ctx.rotate(.5 * Math.PI);
                ctx.translate(width, -height);
                ctx.scale(-1, 1);
                break;

              case 8:
                ctx.rotate(-.5 * Math.PI);
                ctx.translate(-width, 0);
                break;

              default:
                break;
            }
        }
        function MegaPixImage(srcImage, errorCallback) {
            var self = this;
            if (window.Blob && srcImage instanceof Blob) {
                (function() {
                    var img = new Image(), URL = window.URL && window.URL.createObjectURL ? window.URL : window.webkitURL && window.webkitURL.createObjectURL ? window.webkitURL : null;
                    if (!URL) {
                        throw Error("No createObjectURL function found to create blob url");
                    }
                    img.src = URL.createObjectURL(srcImage);
                    self.blob = srcImage;
                    srcImage = img;
                })();
            }
            if (!srcImage.naturalWidth && !srcImage.naturalHeight) {
                srcImage.onload = function() {
                    var listeners = self.imageLoadListeners;
                    if (listeners) {
                        self.imageLoadListeners = null;
                        setTimeout(function() {
                            for (var i = 0, len = listeners.length; i < len; i++) {
                                listeners[i]();
                            }
                        }, 0);
                    }
                };
                srcImage.onerror = errorCallback;
                this.imageLoadListeners = [];
            }
            this.srcImage = srcImage;
        }
        MegaPixImage.prototype.render = function(target, options) {
            options = options || {};
            var self = this, imgWidth = this.srcImage.naturalWidth, imgHeight = this.srcImage.naturalHeight, width = options.width, height = options.height, maxWidth = options.maxWidth, maxHeight = options.maxHeight, doSquash = !this.blob || this.blob.type === "image/jpeg", tagName = target.tagName.toLowerCase(), opt;
            if (this.imageLoadListeners) {
                this.imageLoadListeners.push(function() {
                    self.render(target, options);
                });
                return;
            }
            if (width && !height) {
                height = imgHeight * width / imgWidth << 0;
            } else if (height && !width) {
                width = imgWidth * height / imgHeight << 0;
            } else {
                width = imgWidth;
                height = imgHeight;
            }
            if (maxWidth && width > maxWidth) {
                width = maxWidth;
                height = imgHeight * width / imgWidth << 0;
            }
            if (maxHeight && height > maxHeight) {
                height = maxHeight;
                width = imgWidth * height / imgHeight << 0;
            }
            opt = {
                width: width,
                height: height
            }, miu.each(options, function(optionsKey, optionsValue) {
                opt[optionsKey] = optionsValue;
            });
            if (tagName === "img") {
                (function() {
                    var oldTargetSrc = target.src;
                    renderImageToDataURL(self.srcImage, self.blob, opt, doSquash).then(function(dataUri) {
                        target.src = dataUri;
                        oldTargetSrc === target.src && target.onload();
                    });
                })();
            } else if (tagName === "canvas") {
                renderImageToCanvas(this.srcImage, this.blob, target, opt, doSquash);
            }
            if (typeof this.onrender === "function") {
                this.onrender(target);
            }
        };
        miu.MegaPixImage = MegaPixImage;
    })();
    miu.ImageGenerator = function(log) {
        "use strict";
        function isImg(el) {
            return el.tagName.toLowerCase() === "img";
        }
        function isCanvas(el) {
            return el.tagName.toLowerCase() === "canvas";
        }
        function isImgCorsSupported() {
            return new Image().crossOrigin !== undefined;
        }
        function isCanvasSupported() {
            var canvas = document.createElement("canvas");
            return canvas.getContext && canvas.getContext("2d");
        }
        function determineMimeOfFileName(nameWithPath) {
            var pathSegments = nameWithPath.split("/"), name = pathSegments[pathSegments.length - 1].split("?")[0], extension = miu.getExtension(name);
            extension = extension && extension.toLowerCase();
            switch (extension) {
              case "jpeg":
              case "jpg":
                return "image/jpeg";

              case "png":
                return "image/png";

              case "bmp":
                return "image/bmp";

              case "gif":
                return "image/gif";

              case "tiff":
              case "tif":
                return "image/tiff";
            }
        }
        function isCrossOrigin(url) {
            var targetAnchor = document.createElement("a"), targetProtocol, targetHostname, targetPort;
            targetAnchor.href = url;
            targetProtocol = targetAnchor.protocol;
            targetPort = targetAnchor.port;
            targetHostname = targetAnchor.hostname;
            if (targetProtocol.toLowerCase() !== window.location.protocol.toLowerCase()) {
                return true;
            }
            if (targetHostname.toLowerCase() !== window.location.hostname.toLowerCase()) {
                return true;
            }
            if (targetPort !== window.location.port && !miu.ie()) {
                return true;
            }
            return false;
        }
        function registerImgLoadListeners(img, promise) {
            img.onload = function() {
                img.onload = null;
                img.onerror = null;
                promise.success(img);
            };
            img.onerror = function() {
                img.onload = null;
                img.onerror = null;
                log("Problem drawing thumbnail!", "error");
                promise.failure(img, "Problem drawing thumbnail!");
            };
        }
        function registerCanvasDrawImageListener(canvas, promise) {
            canvas.miuImageRendered = function() {
                promise.success(canvas);
            };
        }
        function registerThumbnailRenderedListener(imgOrCanvas, promise) {
            var registered = isImg(imgOrCanvas) || isCanvas(imgOrCanvas);
            if (isImg(imgOrCanvas)) {
                registerImgLoadListeners(imgOrCanvas, promise);
            } else if (isCanvas(imgOrCanvas)) {
                registerCanvasDrawImageListener(imgOrCanvas, promise);
            } else {
                promise.failure(imgOrCanvas);
                log(miu.format("Element container of type {} is not supported!", imgOrCanvas.tagName), "error");
            }
            return registered;
        }
        function draw(fileOrBlob, container, options) {
            var drawPreview = new miu.Promise(), identifier = new miu.Identify(fileOrBlob, log), maxSize = options.maxSize, orient = options.orient == null ? true : options.orient, megapixErrorHandler = function() {
                container.onerror = null;
                container.onload = null;
                log("Could not render preview, file may be too large!", "error");
                drawPreview.failure(container, "Browser cannot render image!");
            };
            identifier.isPreviewable().then(function(mime) {
                var dummyExif = {
                    parse: function() {
                        return new miu.Promise().success();
                    }
                }, exif = orient ? new miu.Exif(fileOrBlob, log) : dummyExif, mpImg = new miu.MegaPixImage(fileOrBlob, megapixErrorHandler);
                if (registerThumbnailRenderedListener(container, drawPreview)) {
                    exif.parse().then(function(exif) {
                        var orientation = exif && exif.Orientation;
                        mpImg.render(container, {
                            maxWidth: maxSize,
                            maxHeight: maxSize,
                            orientation: orientation,
                            mime: mime,
                            resize: options.customResizeFunction
                        });
                    }, function(failureMsg) {
                        log(miu.format("EXIF data could not be parsed ({}).  Assuming orientation = 1.", failureMsg));
                        mpImg.render(container, {
                            maxWidth: maxSize,
                            maxHeight: maxSize,
                            mime: mime,
                            resize: options.customResizeFunction
                        });
                    });
                }
            }, function() {
                log("Not previewable");
                drawPreview.failure(container, "Not previewable");
            });
            return drawPreview;
        }
        function drawOnCanvasOrImgFromUrl(url, canvasOrImg, draw, maxSize, customResizeFunction) {
            var tempImg = new Image(), tempImgRender = new miu.Promise();
            registerThumbnailRenderedListener(tempImg, tempImgRender);
            if (isCrossOrigin(url)) {
                tempImg.crossOrigin = "anonymous";
            }
            tempImg.src = url;
            tempImgRender.then(function rendered() {
                registerThumbnailRenderedListener(canvasOrImg, draw);
                var mpImg = new miu.MegaPixImage(tempImg);
                mpImg.render(canvasOrImg, {
                    maxWidth: maxSize,
                    maxHeight: maxSize,
                    mime: determineMimeOfFileName(url),
                    resize: customResizeFunction
                });
            }, draw.failure);
        }
        function drawOnImgFromUrlWithCssScaling(url, img, draw, maxSize) {
            registerThumbnailRenderedListener(img, draw);
            miu(img).css({
                maxWidth: maxSize + "px",
                maxHeight: maxSize + "px"
            });
            img.src = url;
        }
        function drawFromUrl(url, container, options) {
            var draw = new miu.Promise(), scale = options.scale, maxSize = scale ? options.maxSize : null;
            if (scale && isImg(container)) {
                if (isCanvasSupported()) {
                    if (isCrossOrigin(url) && !isImgCorsSupported()) {
                        drawOnImgFromUrlWithCssScaling(url, container, draw, maxSize);
                    } else {
                        drawOnCanvasOrImgFromUrl(url, container, draw, maxSize);
                    }
                } else {
                    drawOnImgFromUrlWithCssScaling(url, container, draw, maxSize);
                }
            } else if (isCanvas(container)) {
                drawOnCanvasOrImgFromUrl(url, container, draw, maxSize);
            } else if (registerThumbnailRenderedListener(container, draw)) {
                container.src = url;
            }
            return draw;
        }
        miu.extend(this, {
            generate: function(fileBlobOrUrl, container, options) {
                if (miu.isString(fileBlobOrUrl)) {
                    log("Attempting to update thumbnail based on server response.");
                    return drawFromUrl(fileBlobOrUrl, container, options || {});
                } else {
                    log("Attempting to draw client-side image preview.");
                    return draw(fileBlobOrUrl, container, options || {});
                }
            }
        });
        this._testing = {};
        this._testing.isImg = isImg;
        this._testing.isCanvas = isCanvas;
        this._testing.isCrossOrigin = isCrossOrigin;
        this._testing.determineMimeOfFileName = determineMimeOfFileName;
    };
    miu.Exif = function(fileOrBlob, log) {
        "use strict";
        var TAG_IDS = [ 274 ], TAG_INFO = {
            274: {
                name: "Orientation",
                bytes: 2
            }
        };
        function parseLittleEndian(hex) {
            var result = 0, pow = 0;
            while (hex.length > 0) {
                result += parseInt(hex.substring(0, 2), 16) * Math.pow(2, pow);
                hex = hex.substring(2, hex.length);
                pow += 8;
            }
            return result;
        }
        function seekToApp1(offset, promise) {
            var theOffset = offset, thePromise = promise;
            if (theOffset === undefined) {
                theOffset = 2;
                thePromise = new miu.Promise();
            }
            miu.readBlobToHex(fileOrBlob, theOffset, 4).then(function(hex) {
                var match = /^ffe([0-9])/.exec(hex), segmentLength;
                if (match) {
                    if (match[1] !== "1") {
                        segmentLength = parseInt(hex.slice(4, 8), 16);
                        seekToApp1(theOffset + segmentLength + 2, thePromise);
                    } else {
                        thePromise.success(theOffset);
                    }
                } else {
                    thePromise.failure("No EXIF header to be found!");
                }
            });
            return thePromise;
        }
        function getApp1Offset() {
            var promise = new miu.Promise();
            miu.readBlobToHex(fileOrBlob, 0, 6).then(function(hex) {
                if (hex.indexOf("ffd8") !== 0) {
                    promise.failure("Not a valid JPEG!");
                } else {
                    seekToApp1().then(function(offset) {
                        promise.success(offset);
                    }, function(error) {
                        promise.failure(error);
                    });
                }
            });
            return promise;
        }
        function isLittleEndian(app1Start) {
            var promise = new miu.Promise();
            miu.readBlobToHex(fileOrBlob, app1Start + 10, 2).then(function(hex) {
                promise.success(hex === "4949");
            });
            return promise;
        }
        function getDirEntryCount(app1Start, littleEndian) {
            var promise = new miu.Promise();
            miu.readBlobToHex(fileOrBlob, app1Start + 18, 2).then(function(hex) {
                if (littleEndian) {
                    return promise.success(parseLittleEndian(hex));
                } else {
                    promise.success(parseInt(hex, 16));
                }
            });
            return promise;
        }
        function getIfd(app1Start, dirEntries) {
            var offset = app1Start + 20, bytes = dirEntries * 12;
            return miu.readBlobToHex(fileOrBlob, offset, bytes);
        }
        function getDirEntries(ifdHex) {
            var entries = [], offset = 0;
            while (offset + 24 <= ifdHex.length) {
                entries.push(ifdHex.slice(offset, offset + 24));
                offset += 24;
            }
            return entries;
        }
        function getTagValues(littleEndian, dirEntries) {
            var TAG_VAL_OFFSET = 16, tagsToFind = miu.extend([], TAG_IDS), vals = {};
            miu.each(dirEntries, function(idx, entry) {
                var idHex = entry.slice(0, 4), id = littleEndian ? parseLittleEndian(idHex) : parseInt(idHex, 16), tagsToFindIdx = tagsToFind.indexOf(id), tagValHex, tagName, tagValLength;
                if (tagsToFindIdx >= 0) {
                    tagName = TAG_INFO[id].name;
                    tagValLength = TAG_INFO[id].bytes;
                    tagValHex = entry.slice(TAG_VAL_OFFSET, TAG_VAL_OFFSET + tagValLength * 2);
                    vals[tagName] = littleEndian ? parseLittleEndian(tagValHex) : parseInt(tagValHex, 16);
                    tagsToFind.splice(tagsToFindIdx, 1);
                }
                if (tagsToFind.length === 0) {
                    return false;
                }
            });
            return vals;
        }
        miu.extend(this, {
            parse: function() {
                var parser = new miu.Promise(), onParseFailure = function(message) {
                    log(miu.format("EXIF header parse failed: '{}' ", message));
                    parser.failure(message);
                };
                getApp1Offset().then(function(app1Offset) {
                    log(miu.format("Moving forward with EXIF header parsing for '{}'", fileOrBlob.name === undefined ? "blob" : fileOrBlob.name));
                    isLittleEndian(app1Offset).then(function(littleEndian) {
                        log(miu.format("EXIF Byte order is {} endian", littleEndian ? "little" : "big"));
                        getDirEntryCount(app1Offset, littleEndian).then(function(dirEntryCount) {
                            log(miu.format("Found {} APP1 directory entries", dirEntryCount));
                            getIfd(app1Offset, dirEntryCount).then(function(ifdHex) {
                                var dirEntries = getDirEntries(ifdHex), tagValues = getTagValues(littleEndian, dirEntries);
                                log("Successfully parsed some EXIF tags");
                                parser.success(tagValues);
                            }, onParseFailure);
                        }, onParseFailure);
                    }, onParseFailure);
                }, onParseFailure);
                return parser;
            }
        });
        this._testing = {};
        this._testing.parseLittleEndian = parseLittleEndian;
    };
    miu.Identify = function(fileOrBlob, log) {
        "use strict";
        function isIdentifiable(magicBytes, questionableBytes) {
            var identifiable = false, magicBytesEntries = [].concat(magicBytes);
            miu.each(magicBytesEntries, function(idx, magicBytesArrayEntry) {
                if (questionableBytes.indexOf(magicBytesArrayEntry) === 0) {
                    identifiable = true;
                    return false;
                }
            });
            return identifiable;
        }
        miu.extend(this, {
            isPreviewable: function() {
                var self = this, identifier = new miu.Promise(), previewable = false, name = fileOrBlob.name === undefined ? "blob" : fileOrBlob.name;
                log(miu.format("Attempting to determine if {} can be rendered in this browser", name));
                log("First pass: check type attribute of blob object.");
                if (this.isPreviewableSync()) {
                    log("Second pass: check for magic bytes in file header.");
                    miu.readBlobToHex(fileOrBlob, 0, 4).then(function(hex) {
                        miu.each(self.PREVIEWABLE_MIME_TYPES, function(mime, bytes) {
                            if (isIdentifiable(bytes, hex)) {
                                if (mime !== "image/tiff" || miu.supportedFeatures.tiffPreviews) {
                                    previewable = true;
                                    identifier.success(mime);
                                }
                                return false;
                            }
                        });
                        log(miu.format("'{}' is {} able to be rendered in this browser", name, previewable ? "" : "NOT"));
                        if (!previewable) {
                            identifier.failure();
                        }
                    }, function() {
                        log("Error reading file w/ name '" + name + "'.  Not able to be rendered in this browser.");
                        identifier.failure();
                    });
                } else {
                    identifier.failure();
                }
                return identifier;
            },
            isPreviewableSync: function() {
                var fileMime = fileOrBlob.type, isRecognizedImage = miu.indexOf(Object.keys(this.PREVIEWABLE_MIME_TYPES), fileMime) >= 0, previewable = false, name = fileOrBlob.name === undefined ? "blob" : fileOrBlob.name;
                if (isRecognizedImage) {
                    if (fileMime === "image/tiff") {
                        previewable = miu.supportedFeatures.tiffPreviews;
                    } else {
                        previewable = true;
                    }
                }
                !previewable && log(name + " is not previewable in this browser per the blob's type attr");
                return previewable;
            }
        });
    };
    miu.Identify.prototype.PREVIEWABLE_MIME_TYPES = {
        "image/jpeg": "ffd8ff",
        "image/gif": "474946",
        "image/png": "89504e",
        "image/bmp": "424d",
        "image/tiff": [ "49492a00", "4d4d002a" ]
    };
    miu.ImageValidation = function(blob, log) {
        "use strict";
        function hasNonZeroLimits(limits) {
            var atLeastOne = false;
            miu.each(limits, function(limit, value) {
                if (value > 0) {
                    atLeastOne = true;
                    return false;
                }
            });
            return atLeastOne;
        }
        function getWidthHeight() {
            var sizeDetermination = new miu.Promise();
            new miu.Identify(blob, log).isPreviewable().then(function() {
                var image = new Image(), url = window.URL && window.URL.createObjectURL ? window.URL : window.webkitURL && window.webkitURL.createObjectURL ? window.webkitURL : null;
                if (url) {
                    image.onerror = function() {
                        log("Cannot determine dimensions for image.  May be too large.", "error");
                        sizeDetermination.failure();
                    };
                    image.onload = function() {
                        sizeDetermination.success({
                            width: this.width,
                            height: this.height
                        });
                    };
                    image.src = url.createObjectURL(blob);
                } else {
                    log("No createObjectURL function available to generate image URL!", "error");
                    sizeDetermination.failure();
                }
            }, sizeDetermination.failure);
            return sizeDetermination;
        }
        function getFailingLimit(limits, dimensions) {
            var failingLimit;
            miu.each(limits, function(limitName, limitValue) {
                if (limitValue > 0) {
                    var limitMatcher = /(max|min)(Width|Height)/.exec(limitName), dimensionPropName = limitMatcher[2].charAt(0).toLowerCase() + limitMatcher[2].slice(1), actualValue = dimensions[dimensionPropName];
                    switch (limitMatcher[1]) {
                      case "min":
                        if (actualValue < limitValue) {
                            failingLimit = limitName;
                            return false;
                        }
                        break;

                      case "max":
                        if (actualValue > limitValue) {
                            failingLimit = limitName;
                            return false;
                        }
                        break;
                    }
                }
            });
            return failingLimit;
        }
        this.validate = function(limits) {
            var validationEffort = new miu.Promise();
            log("Attempting to validate image.");
            if (hasNonZeroLimits(limits)) {
                getWidthHeight().then(function(dimensions) {
                    var failingLimit = getFailingLimit(limits, dimensions);
                    if (failingLimit) {
                        validationEffort.failure(failingLimit);
                    } else {
                        validationEffort.success();
                    }
                }, validationEffort.success);
            } else {
                validationEffort.success();
            }
            return validationEffort;
        };
    };
    miu.Session = function(spec) {
        "use strict";
        var options = {
            endpoint: null,
            params: {},
            customHeaders: {},
            cors: {},
            addFileRecord: function(sessionData) {},
            log: function(message, level) {}
        };
        miu.extend(options, spec, true);
        function isJsonResponseValid(response) {
            if (miu.isArray(response)) {
                return true;
            }
            options.log("Session response is not an array.", "error");
        }
        function handleFileItems(fileItems, success, xhrOrXdr, promise) {
            var someItemsIgnored = false;
            success = success && isJsonResponseValid(fileItems);
            if (success) {
                miu.each(fileItems, function(idx, fileItem) {
                    if (fileItem.uuid == null) {
                        someItemsIgnored = true;
                        options.log(miu.format("Session response item {} did not include a valid UUID - ignoring.", idx), "error");
                    } else if (fileItem.name == null) {
                        someItemsIgnored = true;
                        options.log(miu.format("Session response item {} did not include a valid name - ignoring.", idx), "error");
                    } else {
                        try {
                            options.addFileRecord(fileItem);
                            return true;
                        } catch (err) {
                            someItemsIgnored = true;
                            options.log(err.message, "error");
                        }
                    }
                    return false;
                });
            }
            promise[success && !someItemsIgnored ? "success" : "failure"](fileItems, xhrOrXdr);
        }
        this.refresh = function() {
            var refreshEffort = new miu.Promise(), refreshCompleteCallback = function(response, success, xhrOrXdr) {
                handleFileItems(response, success, xhrOrXdr, refreshEffort);
            }, requesterOptions = miu.extend({}, options), requester = new miu.SessionAjaxRequester(miu.extend(requesterOptions, {
                onComplete: refreshCompleteCallback
            }));
            requester.queryServer();
            return refreshEffort;
        };
    };
    miu.SessionAjaxRequester = function(spec) {
        "use strict";
        var requester, options = {
            endpoint: null,
            customHeaders: {},
            params: {},
            cors: {
                expected: false,
                sendCredentials: false
            },
            onComplete: function(response, success, xhrOrXdr) {},
            log: function(str, level) {}
        };
        miu.extend(options, spec);
        function onComplete(id, xhrOrXdr, isError) {
            var response = null;
            if (xhrOrXdr.responseText != null) {
                try {
                    response = miu.parseJson(xhrOrXdr.responseText);
                } catch (err) {
                    options.log("Problem parsing session response: " + err.message, "error");
                    isError = true;
                }
            }
            options.onComplete(response, !isError, xhrOrXdr);
        }
        requester = miu.extend(this, new miu.AjaxRequester({
            acceptHeader: "application/json",
            validMethods: [ "GET" ],
            method: "GET",
            endpointStore: {
                get: function() {
                    return options.endpoint;
                }
            },
            customHeaders: options.customHeaders,
            log: options.log,
            onComplete: onComplete,
            cors: options.cors
        }));
        miu.extend(this, {
            queryServer: function() {
                var params = miu.extend({}, options.params);
                options.log("Session query request.");
                requester.initTransport("sessionRefresh").withParams(params).withCacheBuster().send();
            }
        });
    };
    miu.Scaler = function(spec, log) {
        "use strict";
        var self = this, customResizeFunction = spec.customResizer, includeOriginal = spec.sendOriginal, orient = spec.orient, defaultType = spec.defaultType, defaultQuality = spec.defaultQuality / 100, failedToScaleText = spec.failureText, includeExif = spec.includeExif, sizes = this._getSortedSizes(spec.sizes);
        miu.extend(this, {
            enabled: miu.supportedFeatures.scaling && sizes.length > 0,
            getFileRecords: function(originalFileUuid, originalFileName, originalBlobOrBlobData) {
                var self = this, records = [], originalBlob = originalBlobOrBlobData.blob ? originalBlobOrBlobData.blob : originalBlobOrBlobData, identifier = new miu.Identify(originalBlob, log);
                if (identifier.isPreviewableSync()) {
                    miu.each(sizes, function(idx, sizeRecord) {
                        var outputType = self._determineOutputType({
                            defaultType: defaultType,
                            requestedType: sizeRecord.type,
                            refType: originalBlob.type
                        });
                        records.push({
                            uuid: miu.getUniqueId(),
                            name: self._getName(originalFileName, {
                                name: sizeRecord.name,
                                type: outputType,
                                refType: originalBlob.type
                            }),
                            blob: new miu.BlobProxy(originalBlob, miu.bind(self._generateScaledImage, self, {
                                customResizeFunction: customResizeFunction,
                                maxSize: sizeRecord.maxSize,
                                orient: orient,
                                type: outputType,
                                quality: defaultQuality,
                                failedText: failedToScaleText,
                                includeExif: includeExif,
                                log: log
                            }))
                        });
                    });
                    records.push({
                        uuid: originalFileUuid,
                        name: originalFileName,
                        size: originalBlob.size,
                        blob: includeOriginal ? originalBlob : null
                    });
                } else {
                    records.push({
                        uuid: originalFileUuid,
                        name: originalFileName,
                        size: originalBlob.size,
                        blob: originalBlob
                    });
                }
                return records;
            },
            handleNewFile: function(file, name, uuid, size, fileList, batchId, uuidParamName, api) {
                var self = this, buttonId = file.miuButtonId || file.blob && file.blob.miuButtonId, scaledIds = [], originalId = null, addFileToHandler = api.addFileToHandler, uploadData = api.uploadData, paramsStore = api.paramsStore, proxyGroupId = miu.getUniqueId();
                miu.each(self.getFileRecords(uuid, name, file), function(idx, record) {
                    var blobSize = record.size, id;
                    if (record.blob instanceof miu.BlobProxy) {
                        blobSize = -1;
                    }
                    id = uploadData.addFile({
                        uuid: record.uuid,
                        name: record.name,
                        size: blobSize,
                        batchId: batchId,
                        proxyGroupId: proxyGroupId
                    });
                    if (record.blob instanceof miu.BlobProxy) {
                        scaledIds.push(id);
                    } else {
                        originalId = id;
                    }
                    if (record.blob) {
                        addFileToHandler(id, record.blob);
                        fileList.push({
                            id: id,
                            file: record.blob
                        });
                    } else {
                        uploadData.setStatus(id, miu.status.REJECTED);
                    }
                });
                if (originalId !== null) {
                    miu.each(scaledIds, function(idx, scaledId) {
                        var params = {
                            miuparentuuid: uploadData.retrieve({
                                id: originalId
                            }).uuid,
                            miuparentsize: uploadData.retrieve({
                                id: originalId
                            }).size
                        };
                        params[uuidParamName] = uploadData.retrieve({
                            id: scaledId
                        }).uuid;
                        uploadData.setParentId(scaledId, originalId);
                        paramsStore.addReadOnly(scaledId, params);
                    });
                    if (scaledIds.length) {
                        (function() {
                            var param = {};
                            param[uuidParamName] = uploadData.retrieve({
                                id: originalId
                            }).uuid;
                            paramsStore.addReadOnly(originalId, param);
                        })();
                    }
                }
            }
        });
    };
    miu.extend(miu.Scaler.prototype, {
        scaleImage: function(id, specs, api) {
            "use strict";
            if (!miu.supportedFeatures.scaling) {
                throw new miu.Error("Scaling is not supported in this browser!");
            }
            var scalingEffort = new miu.Promise(), log = api.log, file = api.getFile(id), uploadData = api.uploadData.retrieve({
                id: id
            }), name = uploadData && uploadData.name, uuid = uploadData && uploadData.uuid, scalingOptions = {
                customResizer: specs.customResizer,
                sendOriginal: false,
                orient: specs.orient,
                defaultType: specs.type || null,
                defaultQuality: specs.quality,
                failedToScaleText: "Unable to scale",
                sizes: [ {
                    name: "",
                    maxSize: specs.maxSize
                } ]
            }, scaler = new miu.Scaler(scalingOptions, log);
            if (!miu.Scaler || !miu.supportedFeatures.imagePreviews || !file) {
                scalingEffort.failure();
                log("Could not generate requested scaled image for " + id + ".  " + "Scaling is either not possible in this browser, or the file could not be located.", "error");
            } else {
                miu.bind(function() {
                    var record = scaler.getFileRecords(uuid, name, file)[0];
                    if (record && record.blob instanceof miu.BlobProxy) {
                        record.blob.create().then(scalingEffort.success, scalingEffort.failure);
                    } else {
                        log(id + " is not a scalable image!", "error");
                        scalingEffort.failure();
                    }
                }, this)();
            }
            return scalingEffort;
        },
        _determineOutputType: function(spec) {
            "use strict";
            var requestedType = spec.requestedType, defaultType = spec.defaultType, referenceType = spec.refType;
            if (!defaultType && !requestedType) {
                if (referenceType !== "image/jpeg") {
                    return "image/png";
                }
                return referenceType;
            }
            if (!requestedType) {
                return defaultType;
            }
            if (miu.indexOf(Object.keys(miu.Identify.prototype.PREVIEWABLE_MIME_TYPES), requestedType) >= 0) {
                if (requestedType === "image/tiff") {
                    return miu.supportedFeatures.tiffPreviews ? requestedType : defaultType;
                }
                return requestedType;
            }
            return defaultType;
        },
        _getName: function(originalName, scaledVersionProperties) {
            "use strict";
            var startOfExt = originalName.lastIndexOf("."), versionType = scaledVersionProperties.type || "image/png", referenceType = scaledVersionProperties.refType, scaledName = "", scaledExt = miu.getExtension(originalName), nameAppendage = "";
            if (scaledVersionProperties.name && scaledVersionProperties.name.trim().length) {
                nameAppendage = " (" + scaledVersionProperties.name + ")";
            }
            if (startOfExt >= 0) {
                scaledName = originalName.substr(0, startOfExt);
                if (referenceType !== versionType) {
                    scaledExt = versionType.split("/")[1];
                }
                scaledName += nameAppendage + "." + scaledExt;
            } else {
                scaledName = originalName + nameAppendage;
            }
            return scaledName;
        },
        _getSortedSizes: function(sizes) {
            "use strict";
            sizes = miu.extend([], sizes);
            return sizes.sort(function(a, b) {
                if (a.maxSize > b.maxSize) {
                    return 1;
                }
                if (a.maxSize < b.maxSize) {
                    return -1;
                }
                return 0;
            });
        },
        _generateScaledImage: function(spec, sourceFile) {
            "use strict";
            var self = this, customResizeFunction = spec.customResizeFunction, log = spec.log, maxSize = spec.maxSize, orient = spec.orient, type = spec.type, quality = spec.quality, failedText = spec.failedText, includeExif = spec.includeExif && sourceFile.type === "image/jpeg" && type === "image/jpeg", scalingEffort = new miu.Promise(), imageGenerator = new miu.ImageGenerator(log), canvas = document.createElement("canvas");
            log("Attempting to generate scaled version for " + sourceFile.name);
            imageGenerator.generate(sourceFile, canvas, {
                maxSize: maxSize,
                orient: orient,
                customResizeFunction: customResizeFunction
            }).then(function() {
                var scaledImageDataUri = canvas.toDataURL(type, quality), signalSuccess = function() {
                    log("Success generating scaled version for " + sourceFile.name);
                    var blob = miu.dataUriToBlob(scaledImageDataUri);
                    scalingEffort.success(blob);
                };
                if (includeExif) {
                    self._insertExifHeader(sourceFile, scaledImageDataUri, log).then(function(scaledImageDataUriWithExif) {
                        scaledImageDataUri = scaledImageDataUriWithExif;
                        signalSuccess();
                    }, function() {
                        log("Problem inserting EXIF header into scaled image.  Using scaled image w/out EXIF data.", "error");
                        signalSuccess();
                    });
                } else {
                    signalSuccess();
                }
            }, function() {
                log("Failed attempt to generate scaled version for " + sourceFile.name, "error");
                scalingEffort.failure(failedText);
            });
            return scalingEffort;
        },
        _insertExifHeader: function(originalImage, scaledImageDataUri, log) {
            "use strict";
            var reader = new FileReader(), insertionEffort = new miu.Promise(), originalImageDataUri = "";
            reader.onload = function() {
                originalImageDataUri = reader.result;
                insertionEffort.success(miu.ExifRestorer.restore(originalImageDataUri, scaledImageDataUri));
            };
            reader.onerror = function() {
                log("Problem reading " + originalImage.name + " during attempt to transfer EXIF data to scaled version.", "error");
                insertionEffort.failure();
            };
            reader.readAsDataURL(originalImage);
            return insertionEffort;
        },
        _dataUriToBlob: function(dataUri) {
            "use strict";
            var byteString, mimeString, arrayBuffer, intArray;
            if (dataUri.split(",")[0].indexOf("base64") >= 0) {
                byteString = atob(dataUri.split(",")[1]);
            } else {
                byteString = decodeURI(dataUri.split(",")[1]);
            }
            mimeString = dataUri.split(",")[0].split(":")[1].split(";")[0];
            arrayBuffer = new ArrayBuffer(byteString.length);
            intArray = new Uint8Array(arrayBuffer);
            miu.each(byteString, function(idx, character) {
                intArray[idx] = character.charCodeAt(0);
            });
            return this._createBlob(arrayBuffer, mimeString);
        },
        _createBlob: function(data, mime) {
            "use strict";
            var BlobBuilder = window.BlobBuilder || window.WebKitBlobBuilder || window.MozBlobBuilder || window.MSBlobBuilder, blobBuilder = BlobBuilder && new BlobBuilder();
            if (blobBuilder) {
                blobBuilder.append(data);
                return blobBuilder.getBlob(mime);
            } else {
                return new Blob([ data ], {
                    type: mime
                });
            }
        }
    });
    miu.ExifRestorer = function() {
        var ExifRestorer = {};
        ExifRestorer.KEY_STR = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv" + "wxyz0123456789+/" + "=";
        ExifRestorer.encode64 = function(input) {
            var output = "", chr1, chr2, chr3 = "", enc1, enc2, enc3, enc4 = "", i = 0;
            do {
                chr1 = input[i++];
                chr2 = input[i++];
                chr3 = input[i++];
                enc1 = chr1 >> 2;
                enc2 = (chr1 & 3) << 4 | chr2 >> 4;
                enc3 = (chr2 & 15) << 2 | chr3 >> 6;
                enc4 = chr3 & 63;
                if (isNaN(chr2)) {
                    enc3 = enc4 = 64;
                } else if (isNaN(chr3)) {
                    enc4 = 64;
                }
                output = output + this.KEY_STR.charAt(enc1) + this.KEY_STR.charAt(enc2) + this.KEY_STR.charAt(enc3) + this.KEY_STR.charAt(enc4);
                chr1 = chr2 = chr3 = "";
                enc1 = enc2 = enc3 = enc4 = "";
            } while (i < input.length);
            return output;
        };
        ExifRestorer.restore = function(origFileBase64, resizedFileBase64) {
            var expectedBase64Header = "data:image/jpeg;base64,";
            if (!origFileBase64.match(expectedBase64Header)) {
                return resizedFileBase64;
            }
            var rawImage = this.decode64(origFileBase64.replace(expectedBase64Header, ""));
            var segments = this.slice2Segments(rawImage);
            var image = this.exifManipulation(resizedFileBase64, segments);
            return expectedBase64Header + this.encode64(image);
        };
        ExifRestorer.exifManipulation = function(resizedFileBase64, segments) {
            var exifArray = this.getExifArray(segments), newImageArray = this.insertExif(resizedFileBase64, exifArray), aBuffer = new Uint8Array(newImageArray);
            return aBuffer;
        };
        ExifRestorer.getExifArray = function(segments) {
            var seg;
            for (var x = 0; x < segments.length; x++) {
                seg = segments[x];
                if (seg[0] == 255 & seg[1] == 225) {
                    return seg;
                }
            }
            return [];
        };
        ExifRestorer.insertExif = function(resizedFileBase64, exifArray) {
            var imageData = resizedFileBase64.replace("data:image/jpeg;base64,", ""), buf = this.decode64(imageData), separatePoint = buf.indexOf(255, 3), mae = buf.slice(0, separatePoint), ato = buf.slice(separatePoint), array = mae;
            array = array.concat(exifArray);
            array = array.concat(ato);
            return array;
        };
        ExifRestorer.slice2Segments = function(rawImageArray) {
            var head = 0, segments = [];
            while (1) {
                if (rawImageArray[head] == 255 & rawImageArray[head + 1] == 218) {
                    break;
                }
                if (rawImageArray[head] == 255 & rawImageArray[head + 1] == 216) {
                    head += 2;
                } else {
                    var length = rawImageArray[head + 2] * 256 + rawImageArray[head + 3], endPoint = head + length + 2, seg = rawImageArray.slice(head, endPoint);
                    segments.push(seg);
                    head = endPoint;
                }
                if (head > rawImageArray.length) {
                    break;
                }
            }
            return segments;
        };
        ExifRestorer.decode64 = function(input) {
            var output = "", chr1, chr2, chr3 = "", enc1, enc2, enc3, enc4 = "", i = 0, buf = [];
            var base64test = /[^A-Za-z0-9\+\/\=]/g;
            if (base64test.exec(input)) {
                throw new Error("There were invalid base64 characters in the input text.  " + "Valid base64 characters are A-Z, a-z, 0-9, '+', '/',and '='");
            }
            input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
            do {
                enc1 = this.KEY_STR.indexOf(input.charAt(i++));
                enc2 = this.KEY_STR.indexOf(input.charAt(i++));
                enc3 = this.KEY_STR.indexOf(input.charAt(i++));
                enc4 = this.KEY_STR.indexOf(input.charAt(i++));
                chr1 = enc1 << 2 | enc2 >> 4;
                chr2 = (enc2 & 15) << 4 | enc3 >> 2;
                chr3 = (enc3 & 3) << 6 | enc4;
                buf.push(chr1);
                if (enc3 != 64) {
                    buf.push(chr2);
                }
                if (enc4 != 64) {
                    buf.push(chr3);
                }
                chr1 = chr2 = chr3 = "";
                enc1 = enc2 = enc3 = enc4 = "";
            } while (i < input.length);
            return buf;
        };
        return ExifRestorer;
    }();
    miu.TotalProgress = function(callback, getSize) {
        "use strict";
        var perFileProgress = {}, totalLoaded = 0, totalSize = 0, lastLoadedSent = -1, lastTotalSent = -1, callbackProxy = function(loaded, total) {
            if (loaded !== lastLoadedSent || total !== lastTotalSent) {
                callback(loaded, total);
            }
            lastLoadedSent = loaded;
            lastTotalSent = total;
        }, noRetryableFiles = function(failed, retryable) {
            var none = true;
            miu.each(failed, function(idx, failedId) {
                if (miu.indexOf(retryable, failedId) >= 0) {
                    none = false;
                    return false;
                }
            });
            return none;
        }, onCancel = function(id) {
            updateTotalProgress(id, -1, -1);
            delete perFileProgress[id];
        }, onAllComplete = function(successful, failed, retryable) {
            if (failed.length === 0 || noRetryableFiles(failed, retryable)) {
                callbackProxy(totalSize, totalSize);
                this.reset();
            }
        }, onNew = function(id) {
            var size = getSize(id);
            if (size > 0) {
                updateTotalProgress(id, 0, size);
                perFileProgress[id] = {
                    loaded: 0,
                    total: size
                };
            }
        }, updateTotalProgress = function(id, newLoaded, newTotal) {
            var oldLoaded = perFileProgress[id] ? perFileProgress[id].loaded : 0, oldTotal = perFileProgress[id] ? perFileProgress[id].total : 0;
            if (newLoaded === -1 && newTotal === -1) {
                totalLoaded -= oldLoaded;
                totalSize -= oldTotal;
            } else {
                if (newLoaded) {
                    totalLoaded += newLoaded - oldLoaded;
                }
                if (newTotal) {
                    totalSize += newTotal - oldTotal;
                }
            }
            callbackProxy(totalLoaded, totalSize);
        };
        miu.extend(this, {
            onAllComplete: onAllComplete,
            onStatusChange: function(id, oldStatus, newStatus) {
                if (newStatus === miu.status.CANCELED || newStatus === miu.status.REJECTED) {
                    onCancel(id);
                } else if (newStatus === miu.status.SUBMITTING) {
                    onNew(id);
                }
            },
            onIndividualProgress: function(id, loaded, total) {
                updateTotalProgress(id, loaded, total);
                perFileProgress[id] = {
                    loaded: loaded,
                    total: total
                };
            },
            onNewSize: function(id) {
                onNew(id);
            },
            reset: function() {
                perFileProgress = {};
                totalLoaded = 0;
                totalSize = 0;
            }
        });
    };
    miu.PasteSupport = function(o) {
        "use strict";
        var options, detachPasteHandler;
        options = {
            targetElement: null,
            callbacks: {
                log: function(message, level) {},
                pasteReceived: function(blob) {}
            }
        };
        function isImage(item) {
            return item.type && item.type.indexOf("image/") === 0;
        }
        function registerPasteHandler() {
            detachPasteHandler = miu(options.targetElement).attach("paste", function(event) {
                var clipboardData = event.clipboardData;
                if (clipboardData) {
                    miu.each(clipboardData.items, function(idx, item) {
                        if (isImage(item)) {
                            var blob = item.getAsFile();
                            options.callbacks.pasteReceived(blob);
                        }
                    });
                }
            });
        }
        function unregisterPasteHandler() {
            if (detachPasteHandler) {
                detachPasteHandler();
            }
        }
        miu.extend(options, o);
        registerPasteHandler();
        miu.extend(this, {
            reset: function() {
                unregisterPasteHandler();
            }
        });
    };
    miu.FormSupport = function(options, startUpload, log) {
        "use strict";
        var self = this, interceptSubmit = options.interceptSubmit, formEl = options.element, autoUpload = options.autoUpload;
        miu.extend(this, {
            newEndpoint: null,
            newAutoUpload: autoUpload,
            attachedToForm: false,
            getFormInputsAsObject: function() {
                if (formEl == null) {
                    return null;
                }
                return self._form2Obj(formEl);
            }
        });
        function determineNewEndpoint(formEl) {
            if (formEl.getAttribute("action")) {
                self.newEndpoint = formEl.getAttribute("action");
            }
        }
        function validateForm(formEl, nativeSubmit) {
            if (formEl.checkValidity && !formEl.checkValidity()) {
                log("Form did not pass validation checks - will not upload.", "error");
                nativeSubmit();
            } else {
                return true;
            }
        }
        function maybeUploadOnSubmit(formEl) {
            var nativeSubmit = formEl.submit;
            miu(formEl).attach("submit", function(event) {
                event = event || window.event;
                if (event.preventDefault) {
                    event.preventDefault();
                } else {
                    event.returnValue = false;
                }
                validateForm(formEl, nativeSubmit) && startUpload();
            });
            formEl.submit = function() {
                validateForm(formEl, nativeSubmit) && startUpload();
            };
        }
        function determineFormEl(formEl) {
            if (formEl) {
                if (miu.isString(formEl)) {
                    formEl = document.getElementById(formEl);
                }
                if (formEl) {
                    log("Attaching to form element.");
                    determineNewEndpoint(formEl);
                    interceptSubmit && maybeUploadOnSubmit(formEl);
                }
            }
            return formEl;
        }
        formEl = determineFormEl(formEl);
        this.attachedToForm = !!formEl;
    };
    miu.extend(miu.FormSupport.prototype, {
        _form2Obj: function(form) {
            "use strict";
            var obj = {}, notIrrelevantType = function(type) {
                var irrelevantTypes = [ "button", "image", "reset", "submit" ];
                return miu.indexOf(irrelevantTypes, type.toLowerCase()) < 0;
            }, radioOrCheckbox = function(type) {
                return miu.indexOf([ "checkbox", "radio" ], type.toLowerCase()) >= 0;
            }, ignoreValue = function(el) {
                if (radioOrCheckbox(el.type) && !el.checked) {
                    return true;
                }
                return el.disabled && el.type.toLowerCase() !== "hidden";
            }, selectValue = function(select) {
                var value = null;
                miu.each(miu(select).children(), function(idx, child) {
                    if (child.tagName.toLowerCase() === "option" && child.selected) {
                        value = child.value;
                        return false;
                    }
                });
                return value;
            };
            miu.each(form.elements, function(idx, el) {
                if ((miu.isInput(el, true) || el.tagName.toLowerCase() === "textarea") && notIrrelevantType(el.type) && !ignoreValue(el)) {
                    obj[el.name] = el.value;
                } else if (el.tagName.toLowerCase() === "select" && !ignoreValue(el)) {
                    var value = selectValue(el);
                    if (value !== null) {
                        obj[el.name] = value;
                    }
                }
            });
            return obj;
        }
    });
    miu.traditional = miu.traditional || {};
    miu.traditional.FormUploadHandler = function(options, proxy) {
        "use strict";
        var handler = this, getName = proxy.getName, getUuid = proxy.getUuid, log = proxy.log;
        function getIframeContentJson(id, iframe) {
            var response, doc, innerHtml;
            try {
                doc = iframe.contentDocument || iframe.contentWindow.document;
                innerHtml = doc.body.innerHTML;
                log("converting iframe's innerHTML to JSON");
                log("innerHTML = " + innerHtml);
                if (innerHtml && innerHtml.match(/^<pre/i)) {
                    innerHtml = doc.body.firstChild.firstChild.nodeValue;
                }
                response = handler._parseJsonResponse(innerHtml);
            } catch (error) {
                log("Error when attempting to parse form upload response (" + error.message + ")", "error");
                response = {
                    success: false
                };
            }
            return response;
        }
        function createForm(id, iframe) {
            var params = options.paramsStore.get(id), method = options.method.toLowerCase() === "get" ? "GET" : "POST", endpoint = options.endpointStore.get(id), name = getName(id);
            params[options.uuidName] = getUuid(id);
            params[options.filenameParam] = name;
            return handler._initFormForUpload({
                method: method,
                endpoint: endpoint,
                params: params,
                paramsInBody: options.paramsInBody,
                targetName: iframe.name
            });
        }
        this.uploadFile = function(id) {
            var input = handler.getInput(id), iframe = handler._createIframe(id), promise = new miu.Promise(), form;
            form = createForm(id, iframe);
            form.appendChild(input);
            handler._attachLoadEvent(iframe, function(responseFromMessage) {
                log("iframe loaded");
                var response = responseFromMessage ? responseFromMessage : getIframeContentJson(id, iframe);
                handler._detachLoadEvent(id);
                if (!options.cors.expected) {
                    miu(iframe).remove();
                }
                if (response.success) {
                    promise.success(response);
                } else {
                    promise.failure(response);
                }
            });
            log("Sending upload request for " + id);
            form.submit();
            miu(form).remove();
            return promise;
        };
        miu.extend(this, new miu.FormUploadHandler({
            options: {
                isCors: options.cors.expected,
                inputName: options.inputName
            },
            proxy: {
                onCancel: options.onCancel,
                getName: getName,
                getUuid: getUuid,
                log: log
            }
        }));
    };
    miu.traditional = miu.traditional || {};
    miu.traditional.XhrUploadHandler = function(spec, proxy) {
        "use strict";
        var handler = this, getName = proxy.getName, getSize = proxy.getSize, getUuid = proxy.getUuid, log = proxy.log, multipart = spec.forceMultipart || spec.paramsInBody, addChunkingSpecificParams = function(id, params, chunkData) {
            var size = getSize(id), name = getName(id);
            params[spec.chunking.paramNames.partIndex] = chunkData.part;
            params[spec.chunking.paramNames.partByteOffset] = chunkData.start;
            params[spec.chunking.paramNames.chunkSize] = chunkData.size;
            params[spec.chunking.paramNames.totalParts] = chunkData.count;
            params[spec.totalFileSizeName] = size;
            if (multipart) {
                params[spec.filenameParam] = name;
            }
        }, allChunksDoneRequester = new miu.traditional.AllChunksDoneAjaxRequester({
            cors: spec.cors,
            endpoint: spec.chunking.success.endpoint,
            log: log
        }), createReadyStateChangedHandler = function(id, xhr) {
            var promise = new miu.Promise();
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4) {
                    var result = onUploadOrChunkComplete(id, xhr);
                    if (result.success) {
                        promise.success(result.response, xhr);
                    } else {
                        promise.failure(result.response, xhr);
                    }
                }
            };
            return promise;
        }, getChunksCompleteParams = function(id) {
            var params = spec.paramsStore.get(id), name = getName(id), size = getSize(id);
            params[spec.uuidName] = getUuid(id);
            params[spec.filenameParam] = name;
            params[spec.totalFileSizeName] = size;
            params[spec.chunking.paramNames.totalParts] = handler._getTotalChunks(id);
            return params;
        }, isErrorUploadResponse = function(xhr, response) {
            return miu.indexOf([ 200, 201, 202, 203, 204 ], xhr.status) < 0 || !response.success || response.reset;
        }, onUploadOrChunkComplete = function(id, xhr) {
            var response;
            log("xhr - server response received for " + id);
            log("responseText = " + xhr.responseText);
            response = parseResponse(true, xhr);
            return {
                success: !isErrorUploadResponse(xhr, response),
                response: response
            };
        }, parseResponse = function(upload, xhr) {
            var response = {};
            try {
                log(miu.format("Received response status {} with body: {}", xhr.status, xhr.responseText));
                response = miu.parseJson(xhr.responseText);
            } catch (error) {
                upload && log("Error when attempting to parse xhr response text (" + error.message + ")", "error");
            }
            return response;
        }, sendChunksCompleteRequest = function(id) {
            var promise = new miu.Promise();
            allChunksDoneRequester.complete(id, handler._createXhr(id), getChunksCompleteParams(id), spec.customHeaders.get(id)).then(function(xhr) {
                promise.success(parseResponse(false, xhr), xhr);
            }, function(xhr) {
                promise.failure(parseResponse(false, xhr), xhr);
            });
            return promise;
        }, setParamsAndGetEntityToSend = function(params, xhr, fileOrBlob, id) {
            var formData = new FormData(), method = spec.method, endpoint = spec.endpointStore.get(id), name = getName(id), size = getSize(id);
            params[spec.uuidName] = getUuid(id);
            params[spec.filenameParam] = name;
            if (multipart) {
                params[spec.totalFileSizeName] = size;
            }
            if (!spec.paramsInBody) {
                if (!multipart) {
                    params[spec.inputName] = name;
                }
                endpoint = miu.obj2url(params, endpoint);
            }
            xhr.open(method, endpoint, true);
            if (spec.cors.expected && spec.cors.sendCredentials) {
                xhr.withCredentials = true;
            }
            if (multipart) {
                if (spec.paramsInBody) {
                    miu.obj2FormData(params, formData);
                }
                formData.append(spec.inputName, fileOrBlob);
                return formData;
            }
            return fileOrBlob;
        }, setUploadHeaders = function(id, xhr) {
            var extraHeaders = spec.customHeaders.get(id), fileOrBlob = handler.getFile(id);
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
            xhr.setRequestHeader("Cache-Control", "no-cache");
            if (!multipart) {
                xhr.setRequestHeader("Content-Type", "application/octet-stream");
                xhr.setRequestHeader("X-Mime-Type", fileOrBlob.type);
            }
            miu.each(extraHeaders, function(name, val) {
                xhr.setRequestHeader(name, val);
            });
        };
        miu.extend(this, {
            uploadChunk: function(id, chunkIdx, resuming) {
                var chunkData = handler._getChunkData(id, chunkIdx), xhr = handler._createXhr(id, chunkIdx), size = getSize(id), promise, toSend, params;
                promise = createReadyStateChangedHandler(id, xhr);
                handler._registerProgressHandler(id, chunkIdx, chunkData.size);
                params = spec.paramsStore.get(id);
                addChunkingSpecificParams(id, params, chunkData);
                if (resuming) {
                    params[spec.resume.paramNames.resuming] = true;
                }
                toSend = setParamsAndGetEntityToSend(params, xhr, chunkData.blob, id);
                setUploadHeaders(id, xhr);
                xhr.send(toSend);
                return promise;
            },
            uploadFile: function(id) {
                var fileOrBlob = handler.getFile(id), promise, xhr, params, toSend;
                xhr = handler._createXhr(id);
                handler._registerProgressHandler(id);
                promise = createReadyStateChangedHandler(id, xhr);
                params = spec.paramsStore.get(id);
                toSend = setParamsAndGetEntityToSend(params, xhr, fileOrBlob, id);
                setUploadHeaders(id, xhr);
                xhr.send(toSend);
                return promise;
            }
        });
        miu.extend(this, new miu.XhrUploadHandler({
            options: miu.extend({
                namespace: "traditional"
            }, spec),
            proxy: miu.extend({
                getEndpoint: spec.endpointStore.get
            }, proxy)
        }));
        miu.override(this, function(super_) {
            return {
                finalizeChunks: function(id) {
                    if (spec.chunking.success.endpoint) {
                        return sendChunksCompleteRequest(id);
                    } else {
                        return super_.finalizeChunks(id, miu.bind(parseResponse, this, true));
                    }
                }
            };
        });
    };
    miu.traditional.AllChunksDoneAjaxRequester = function(o) {
        "use strict";
        var requester, method = "POST", options = {
            cors: {
                allowXdr: false,
                expected: false,
                sendCredentials: false
            },
            endpoint: null,
            log: function(str, level) {}
        }, promises = {}, endpointHandler = {
            get: function(id) {
                return options.endpoint;
            }
        };
        miu.extend(options, o);
        requester = miu.extend(this, new miu.AjaxRequester({
            acceptHeader: "application/json",
            validMethods: [ method ],
            method: method,
            endpointStore: endpointHandler,
            allowXRequestedWithAndCacheControl: false,
            cors: options.cors,
            log: options.log,
            onComplete: function(id, xhr, isError) {
                var promise = promises[id];
                delete promises[id];
                if (isError) {
                    promise.failure(xhr);
                } else {
                    promise.success(xhr);
                }
            }
        }));
        miu.extend(this, {
            complete: function(id, xhr, params, headers) {
                var promise = new miu.Promise();
                options.log("Submitting All Chunks Done request for " + id);
                promises[id] = promise;
                requester.initTransport(id).withParams(params).withHeaders(headers).send(xhr);
                return promise;
            }
        });
    };
    miu.DragAndDrop = function(o) {
        "use strict";
        var options, HIDE_ZONES_EVENT_NAME = "miu-hidezones", HIDE_BEFORE_ENTER_ATTR = "miu-hide-dropzone", uploadDropZones = [], droppedFiles = [], disposeSupport = new miu.DisposeSupport();
        options = {
            dropZoneElements: [],
            allowMultipleItems: true,
            classes: {
                dropActive: null
            },
            callbacks: new miu.DragAndDrop.callbacks()
        };
        miu.extend(options, o, true);
        function uploadDroppedFiles(files, uploadDropZone) {
            var filesAsArray = Array.prototype.slice.call(files);
            options.callbacks.dropLog("Grabbed " + files.length + " dropped files.");
            uploadDropZone.dropDisabled(false);
            options.callbacks.processingDroppedFilesComplete(filesAsArray, uploadDropZone.getElement());
        }
        function traverseFileTree(entry) {
            var parseEntryPromise = new miu.Promise();
            if (entry.isFile) {
                entry.file(function(file) {
                    var name = entry.name, fullPath = entry.fullPath, indexOfNameInFullPath = fullPath.indexOf(name);
                    fullPath = fullPath.substr(0, indexOfNameInFullPath);
                    if (fullPath.charAt(0) === "/") {
                        fullPath = fullPath.substr(1);
                    }
                    file.miuPath = fullPath;
                    droppedFiles.push(file);
                    parseEntryPromise.success();
                }, function(fileError) {
                    options.callbacks.dropLog("Problem parsing '" + entry.fullPath + "'.  FileError code " + fileError.code + ".", "error");
                    parseEntryPromise.failure();
                });
            } else if (entry.isDirectory) {
                getFilesInDirectory(entry).then(function allEntriesRead(entries) {
                    var entriesLeft = entries.length;
                    miu.each(entries, function(idx, entry) {
                        traverseFileTree(entry).done(function() {
                            entriesLeft -= 1;
                            if (entriesLeft === 0) {
                                parseEntryPromise.success();
                            }
                        });
                    });
                    if (!entries.length) {
                        parseEntryPromise.success();
                    }
                }, function readFailure(fileError) {
                    options.callbacks.dropLog("Problem parsing '" + entry.fullPath + "'.  FileError code " + fileError.code + ".", "error");
                    parseEntryPromise.failure();
                });
            }
            return parseEntryPromise;
        }
        function getFilesInDirectory(entry, reader, accumEntries, existingPromise) {
            var promise = existingPromise || new miu.Promise(), dirReader = reader || entry.createReader();
            dirReader.readEntries(function readSuccess(entries) {
                var newEntries = accumEntries ? accumEntries.concat(entries) : entries;
                if (entries.length) {
                    setTimeout(function() {
                        getFilesInDirectory(entry, dirReader, newEntries, promise);
                    }, 0);
                } else {
                    promise.success(newEntries);
                }
            }, promise.failure);
            return promise;
        }
        function handleDataTransfer(dataTransfer, uploadDropZone) {
            var pendingFolderPromises = [], handleDataTransferPromise = new miu.Promise();
            options.callbacks.processingDroppedFiles();
            uploadDropZone.dropDisabled(true);
            if (dataTransfer.files.length > 1 && !options.allowMultipleItems) {
                options.callbacks.processingDroppedFilesComplete([]);
                options.callbacks.dropError("tooManyFilesError", "");
                uploadDropZone.dropDisabled(false);
                handleDataTransferPromise.failure();
            } else {
                droppedFiles = [];
                if (miu.isFolderDropSupported(dataTransfer)) {
                    miu.each(dataTransfer.items, function(idx, item) {
                        var entry = item.webkitGetAsEntry();
                        if (entry) {
                            if (entry.isFile) {
                                droppedFiles.push(item.getAsFile());
                            } else {
                                pendingFolderPromises.push(traverseFileTree(entry).done(function() {
                                    pendingFolderPromises.pop();
                                    if (pendingFolderPromises.length === 0) {
                                        handleDataTransferPromise.success();
                                    }
                                }));
                            }
                        }
                    });
                } else {
                    droppedFiles = dataTransfer.files;
                }
                if (pendingFolderPromises.length === 0) {
                    handleDataTransferPromise.success();
                }
            }
            return handleDataTransferPromise;
        }
        function setupDropzone(dropArea) {
            var dropZone = new miu.UploadDropZone({
                HIDE_ZONES_EVENT_NAME: HIDE_ZONES_EVENT_NAME,
                element: dropArea,
                onEnter: function(e) {
                    miu(dropArea).addClass(options.classes.dropActive);
                    e.stopPropagation();
                },
                onLeaveNotDescendants: function(e) {
                    miu(dropArea).removeClass(options.classes.dropActive);
                },
                onDrop: function(e) {
                    handleDataTransfer(e.dataTransfer, dropZone).then(function() {
                        uploadDroppedFiles(droppedFiles, dropZone);
                    }, function() {
                        options.callbacks.dropLog("Drop event DataTransfer parsing failed.  No files will be uploaded.", "error");
                    });
                }
            });
            disposeSupport.addDisposer(function() {
                dropZone.dispose();
            });
            miu(dropArea).hasAttribute(HIDE_BEFORE_ENTER_ATTR) && miu(dropArea).hide();
            uploadDropZones.push(dropZone);
            return dropZone;
        }
        function isFileDrag(dragEvent) {
            var fileDrag;
            miu.each(dragEvent.dataTransfer.types, function(key, val) {
                if (val === "Files") {
                    fileDrag = true;
                    return false;
                }
            });
            return fileDrag;
        }
        function leavingDocumentOut(e) {
            if (miu.firefox()) {
                return !e.relatedTarget;
            }
            if (miu.safari()) {
                return e.x < 0 || e.y < 0;
            }
            return e.x === 0 && e.y === 0;
        }
        function setupDragDrop() {
            var dropZones = options.dropZoneElements, maybeHideDropZones = function() {
                setTimeout(function() {
                    miu.each(dropZones, function(idx, dropZone) {
                        miu(dropZone).hasAttribute(HIDE_BEFORE_ENTER_ATTR) && miu(dropZone).hide();
                        miu(dropZone).removeClass(options.classes.dropActive);
                    });
                }, 10);
            };
            miu.each(dropZones, function(idx, dropZone) {
                var uploadDropZone = setupDropzone(dropZone);
                if (dropZones.length && miu.supportedFeatures.fileDrop) {
                    disposeSupport.attach(document, "dragenter", function(e) {
                        if (!uploadDropZone.dropDisabled() && isFileDrag(e)) {
                            miu.each(dropZones, function(idx, dropZone) {
                                if (dropZone instanceof HTMLElement && miu(dropZone).hasAttribute(HIDE_BEFORE_ENTER_ATTR)) {
                                    miu(dropZone).css({
                                        display: "block"
                                    });
                                }
                            });
                        }
                    });
                }
            });
            disposeSupport.attach(document, "dragleave", function(e) {
                if (leavingDocumentOut(e)) {
                    maybeHideDropZones();
                }
            });
            disposeSupport.attach(miu(document).children()[0], "mouseenter", function(e) {
                maybeHideDropZones();
            });
            disposeSupport.attach(document, "drop", function(e) {
                e.preventDefault();
                maybeHideDropZones();
            });
            disposeSupport.attach(document, HIDE_ZONES_EVENT_NAME, maybeHideDropZones);
        }
        setupDragDrop();
        miu.extend(this, {
            setupExtraDropzone: function(element) {
                options.dropZoneElements.push(element);
                setupDropzone(element);
            },
            removeDropzone: function(element) {
                var i, dzs = options.dropZoneElements;
                for (i in dzs) {
                    if (dzs[i] === element) {
                        return dzs.splice(i, 1);
                    }
                }
            },
            dispose: function() {
                disposeSupport.dispose();
                miu.each(uploadDropZones, function(idx, dropZone) {
                    dropZone.dispose();
                });
            }
        });
    };
    miu.DragAndDrop.callbacks = function() {
        "use strict";
        return {
            processingDroppedFiles: function() {},
            processingDroppedFilesComplete: function(files, targetEl) {},
            dropError: function(code, errorSpecifics) {
                miu.log("Drag & drop error code '" + code + " with these specifics: '" + errorSpecifics + "'", "error");
            },
            dropLog: function(message, level) {
                miu.log(message, level);
            }
        };
    };
    miu.UploadDropZone = function(o) {
        "use strict";
        var disposeSupport = new miu.DisposeSupport(), options, element, preventDrop, dropOutsideDisabled;
        options = {
            element: null,
            onEnter: function(e) {},
            onLeave: function(e) {},
            onLeaveNotDescendants: function(e) {},
            onDrop: function(e) {}
        };
        miu.extend(options, o);
        element = options.element;
        function dragoverShouldBeCanceled() {
            return miu.safari() || miu.firefox() && miu.windows();
        }
        function disableDropOutside(e) {
            if (!dropOutsideDisabled) {
                if (dragoverShouldBeCanceled) {
                    disposeSupport.attach(document, "dragover", function(e) {
                        e.preventDefault();
                    });
                } else {
                    disposeSupport.attach(document, "dragover", function(e) {
                        if (e.dataTransfer) {
                            e.dataTransfer.dropEffect = "none";
                            e.preventDefault();
                        }
                    });
                }
                dropOutsideDisabled = true;
            }
        }
        function isValidFileDrag(e) {
            if (!miu.supportedFeatures.fileDrop) {
                return false;
            }
            var effectTest, dt = e.dataTransfer, isSafari = miu.safari();
            effectTest = miu.ie() && miu.supportedFeatures.fileDrop ? true : dt.effectAllowed !== "none";
            return dt && effectTest && (dt.files || !isSafari && dt.types.contains && dt.types.contains("Files"));
        }
        function isOrSetDropDisabled(isDisabled) {
            if (isDisabled !== undefined) {
                preventDrop = isDisabled;
            }
            return preventDrop;
        }
        function triggerHidezonesEvent() {
            var hideZonesEvent;
            function triggerUsingOldApi() {
                hideZonesEvent = document.createEvent("Event");
                hideZonesEvent.initEvent(options.HIDE_ZONES_EVENT_NAME, true, true);
            }
            if (window.CustomEvent) {
                try {
                    hideZonesEvent = new CustomEvent(options.HIDE_ZONES_EVENT_NAME);
                } catch (err) {
                    triggerUsingOldApi();
                }
            } else {
                triggerUsingOldApi();
            }
            document.dispatchEvent(hideZonesEvent);
        }
        function attachEvents() {
            disposeSupport.attach(element, "dragover", function(e) {
                if (!isValidFileDrag(e)) {
                    return;
                }
                var effect = miu.ie() && miu.supportedFeatures.fileDrop ? null : e.dataTransfer.effectAllowed;
                if (effect === "move" || effect === "linkMove") {
                    e.dataTransfer.dropEffect = "move";
                } else {
                    e.dataTransfer.dropEffect = "copy";
                }
                e.stopPropagation();
                e.preventDefault();
            });
            disposeSupport.attach(element, "dragenter", function(e) {
                if (!isOrSetDropDisabled()) {
                    if (!isValidFileDrag(e)) {
                        return;
                    }
                    options.onEnter(e);
                }
            });
            disposeSupport.attach(element, "dragleave", function(e) {
                if (!isValidFileDrag(e)) {
                    return;
                }
                options.onLeave(e);
                var relatedTarget = document.elementFromPoint(e.clientX, e.clientY);
                if (miu(this).contains(relatedTarget)) {
                    return;
                }
                options.onLeaveNotDescendants(e);
            });
            disposeSupport.attach(element, "drop", function(e) {
                if (!isOrSetDropDisabled()) {
                    if (!isValidFileDrag(e)) {
                        return;
                    }
                    e.preventDefault();
                    e.stopPropagation();
                    options.onDrop(e);
                    triggerHidezonesEvent();
                }
            });
        }
        disableDropOutside();
        attachEvents();
        miu.extend(this, {
            dropDisabled: function(isDisabled) {
                return isOrSetDropDisabled(isDisabled);
            },
            dispose: function() {
                disposeSupport.dispose();
            },
            getElement: function() {
                return element;
            }
        });
    };
    (function() {
        "use strict";
        miu.uiPublicApi = {
            addInitialFiles: function(cannedFileList) {
                this._parent.prototype.addInitialFiles.apply(this, arguments);
                this._templating.addCacheToDom();
            },
            clearStoredFiles: function() {
                this._parent.prototype.clearStoredFiles.apply(this, arguments);
                this._templating.clearFiles();
            },
            addExtraDropzone: function(element) {
                this._dnd && this._dnd.setupExtraDropzone(element);
            },
            removeExtraDropzone: function(element) {
                if (this._dnd) {
                    return this._dnd.removeDropzone(element);
                }
            },
            getItemByFileId: function(id) {
                if (!this._templating.isHiddenForever(id)) {
                    return this._templating.getFileContainer(id);
                }
            },
            reset: function() {
                this._parent.prototype.reset.apply(this, arguments);
                this._templating.reset();
                if (!this._options.button && this._templating.getButton()) {
                    this._defaultButtonId = this._createUploadButton({
                        element: this._templating.getButton(),
                        title: this._options.text.fileInputTitle
                    }).getButtonId();
                }
                if (this._dnd) {
                    this._dnd.dispose();
                    this._dnd = this._setupDragAndDrop();
                }
                this._totalFilesInBatch = 0;
                this._filesInBatchAddedToUi = 0;
                this._setupClickAndEditEventHandlers();
            },
            setName: function(id, newName) {
                var formattedFilename = this._options.formatFileName(newName);
                this._parent.prototype.setName.apply(this, arguments);
                this._templating.updateFilename(id, formattedFilename);
            },
            pauseUpload: function(id) {
                var paused = this._parent.prototype.pauseUpload.apply(this, arguments);
                paused && this._templating.uploadPaused(id);
                return paused;
            },
            continueUpload: function(id) {
                var continued = this._parent.prototype.continueUpload.apply(this, arguments);
                continued && this._templating.uploadContinued(id);
                return continued;
            },
            getId: function(fileContainerOrChildEl) {
                return this._templating.getFileId(fileContainerOrChildEl);
            },
            getDropTarget: function(fileId) {
                var file = this.getFile(fileId);
                return file.miuDropTarget;
            }
        };
        miu.uiPrivateApi = {
            _getButton: function(buttonId) {
                var button = this._parent.prototype._getButton.apply(this, arguments);
                if (!button) {
                    if (buttonId === this._defaultButtonId) {
                        button = this._templating.getButton();
                    }
                }
                return button;
            },
            _removeFileItem: function(fileId) {
                this._templating.removeFile(fileId);
            },
            _setupClickAndEditEventHandlers: function() {
                this._fileButtonsClickHandler = miu.FileButtonsClickHandler && this._bindFileButtonsClickEvent();
                this._focusinEventSupported = !miu.firefox();
                if (this._isEditFilenameEnabled()) {
                    this._filenameClickHandler = this._bindFilenameClickEvent();
                    this._filenameInputFocusInHandler = this._bindFilenameInputFocusInEvent();
                    this._filenameInputFocusHandler = this._bindFilenameInputFocusEvent();
                }
            },
            _setupDragAndDrop: function() {
                var self = this, dropZoneElements = this._options.dragAndDrop.extraDropzones, templating = this._templating, defaultDropZone = templating.getDropZone();
                defaultDropZone && dropZoneElements.push(defaultDropZone);
                return new miu.DragAndDrop({
                    dropZoneElements: dropZoneElements,
                    allowMultipleItems: this._options.multiple,
                    classes: {
                        dropActive: this._options.classes.dropActive
                    },
                    callbacks: {
                        processingDroppedFiles: function() {
                            templating.showDropProcessing();
                        },
                        processingDroppedFilesComplete: function(files, targetEl) {
                            templating.hideDropProcessing();
                            miu.each(files, function(idx, file) {
                                file.miuDropTarget = targetEl;
                            });
                            if (files.length) {
                                self.addFiles(files, null, null);
                            }
                        },
                        dropError: function(code, errorData) {
                            self._itemError(code, errorData);
                        },
                        dropLog: function(message, level) {
                            self.log(message, level);
                        }
                    }
                });
            },
            _bindFileButtonsClickEvent: function() {
                var self = this;
                return new miu.FileButtonsClickHandler({
                    templating: this._templating,
                    log: function(message, lvl) {
                        self.log(message, lvl);
                    },
                    onDeleteFile: function(fileId) {
                        self.deleteFile(fileId);
                    },
                    onCancel: function(fileId) {
                        self.cancel(fileId);
                    },
                    onRetry: function(fileId) {
                        self.retry(fileId);
                    },
                    onPause: function(fileId) {
                        self.pauseUpload(fileId);
                    },
                    onContinue: function(fileId) {
                        self.continueUpload(fileId);
                    },
                    onGetName: function(fileId) {
                        return self.getName(fileId);
                    }
                });
            },
            _isEditFilenameEnabled: function() {
                return this._templating.isEditFilenamePossible() && !this._options.autoUpload && miu.FilenameClickHandler && miu.FilenameInputFocusHandler && miu.FilenameInputFocusHandler;
            },
            _filenameEditHandler: function() {
                var self = this, templating = this._templating;
                return {
                    templating: templating,
                    log: function(message, lvl) {
                        self.log(message, lvl);
                    },
                    onGetUploadStatus: function(fileId) {
                        return self.getUploads({
                            id: fileId
                        }).status;
                    },
                    onGetName: function(fileId) {
                        return self.getName(fileId);
                    },
                    onSetName: function(id, newName) {
                        self.setName(id, newName);
                    },
                    onEditingStatusChange: function(id, isEditing) {
                        var miuInput = miu(templating.getEditInput(id)), miuFileContainer = miu(templating.getFileContainer(id));
                        if (isEditing) {
                            miuInput.addClass("miu-editing");
                            templating.hideFilename(id);
                            templating.hideEditIcon(id);
                        } else {
                            miuInput.removeClass("miu-editing");
                            templating.showFilename(id);
                            templating.showEditIcon(id);
                        }
                        miuFileContainer.addClass("miu-temp").removeClass("miu-temp");
                    }
                };
            },
            _onUploadStatusChange: function(id, oldStatus, newStatus) {
                this._parent.prototype._onUploadStatusChange.apply(this, arguments);
                if (this._isEditFilenameEnabled()) {
                    if (this._templating.getFileContainer(id) && newStatus !== miu.status.SUBMITTED) {
                        this._templating.markFilenameEditable(id);
                        this._templating.hideEditIcon(id);
                    }
                }
                if (oldStatus === miu.status.UPLOAD_RETRYING && newStatus === miu.status.UPLOADING) {
                    this._templating.hideRetry(id);
                    this._templating.setStatusText(id);
                    miu(this._templating.getFileContainer(id)).removeClass(this._classes.retrying);
                } else if (newStatus === miu.status.UPLOAD_FAILED) {
                    this._templating.hidePause(id);
                }
            },
            _bindFilenameInputFocusInEvent: function() {
                var spec = miu.extend({}, this._filenameEditHandler());
                return new miu.FilenameInputFocusInHandler(spec);
            },
            _bindFilenameInputFocusEvent: function() {
                var spec = miu.extend({}, this._filenameEditHandler());
                return new miu.FilenameInputFocusHandler(spec);
            },
            _bindFilenameClickEvent: function() {
                var spec = miu.extend({}, this._filenameEditHandler());
                return new miu.FilenameClickHandler(spec);
            },
            _storeForLater: function(id) {
                this._parent.prototype._storeForLater.apply(this, arguments);
                this._templating.hideSpinner(id);
            },
            _onAllComplete: function(successful, failed) {
                this._parent.prototype._onAllComplete.apply(this, arguments);
                this._templating.resetTotalProgress();
            },
            _onSubmit: function(id, name) {
                var file = this.getFile(id);
                if (file && file.miuPath && this._options.dragAndDrop.reportDirectoryPaths) {
                    this._paramsStore.addReadOnly(id, {
                        miupath: file.miuPath
                    });
                }
                this._parent.prototype._onSubmit.apply(this, arguments);
                this._addToList(id, name);
            },
            _onSubmitted: function(id) {
                if (this._isEditFilenameEnabled()) {
                    this._templating.markFilenameEditable(id);
                    this._templating.showEditIcon(id);
                    if (!this._focusinEventSupported) {
                        this._filenameInputFocusHandler.addHandler(this._templating.getEditInput(id));
                    }
                }
            },
            _onProgress: function(id, name, loaded, total) {
                this._parent.prototype._onProgress.apply(this, arguments);
                this._templating.updateProgress(id, loaded, total);
                if (total === 0 || Math.round(loaded / total * 100) === 100) {
                    this._templating.hideCancel(id);
                    this._templating.hidePause(id);
                    this._templating.hideProgress(id);
                    this._templating.setStatusText(id, this._options.text.waitingForResponse);
                    this._displayFileSize(id);
                } else {
                    this._displayFileSize(id, loaded, total);
                }
            },
            _onTotalProgress: function(loaded, total) {
                this._parent.prototype._onTotalProgress.apply(this, arguments);
                this._templating.updateTotalProgress(loaded, total);
            },
            _onComplete: function(id, name, result, xhr) {
                var parentRetVal = this._parent.prototype._onComplete.apply(this, arguments), templating = this._templating, fileContainer = templating.getFileContainer(id), self = this;
                function completeUpload(result) {
                    if (!fileContainer) {
                        return;
                    }
                    templating.setStatusText(id);
                    miu(fileContainer).removeClass(self._classes.retrying);
                    templating.hideProgress(id);
                    if (self.getUploads({
                        id: id
                    }).status !== miu.status.UPLOAD_FAILED) {
                        templating.hideCancel(id);
                    }
                    templating.hideSpinner(id);
                    if (result.success) {
                        self._markFileAsSuccessful(id);
                    } else {
                        miu(fileContainer).addClass(self._classes.fail);
                        templating.showCancel(id);
                        if (templating.isRetryPossible() && !self._preventRetries[id]) {
                            miu(fileContainer).addClass(self._classes.retryable);
                            templating.showRetry(id);
                        }
                        self._controlFailureTextDisplay(id, result);
                    }
                }
                if (parentRetVal instanceof miu.Promise) {
                    parentRetVal.done(function(newResult) {
                        completeUpload(newResult);
                    });
                } else {
                    completeUpload(result);
                }
                return parentRetVal;
            },
            _markFileAsSuccessful: function(id) {
                var templating = this._templating;
                if (this._isDeletePossible()) {
                    templating.showDeleteButton(id);
                }
                miu(templating.getFileContainer(id)).addClass(this._classes.success);
                this._maybeUpdateThumbnail(id);
            },
            _onUploadPrep: function(id) {
                this._parent.prototype._onUploadPrep.apply(this, arguments);
                this._templating.showSpinner(id);
            },
            _onUpload: function(id, name) {
                var parentRetVal = this._parent.prototype._onUpload.apply(this, arguments);
                this._templating.showSpinner(id);
                return parentRetVal;
            },
            _onUploadChunk: function(id, chunkData) {
                this._parent.prototype._onUploadChunk.apply(this, arguments);
                if (chunkData.partIndex > 0 && this._handler.isResumable(id)) {
                    this._templating.allowPause(id);
                }
            },
            _onCancel: function(id, name) {
                this._parent.prototype._onCancel.apply(this, arguments);
                this._removeFileItem(id);
                if (this._getNotFinished() === 0) {
                    this._templating.resetTotalProgress();
                }
            },
            _onBeforeAutoRetry: function(id) {
                var retryNumForDisplay, maxAuto, retryNote;
                this._parent.prototype._onBeforeAutoRetry.apply(this, arguments);
                this._showCancelLink(id);
                if (this._options.retry.showAutoRetryNote) {
                    retryNumForDisplay = this._autoRetries[id];
                    maxAuto = this._options.retry.maxAutoAttempts;
                    retryNote = this._options.retry.autoRetryNote.replace(/\{retryNum\}/g, retryNumForDisplay);
                    retryNote = retryNote.replace(/\{maxAuto\}/g, maxAuto);
                    this._templating.setStatusText(id, retryNote);
                    miu(this._templating.getFileContainer(id)).addClass(this._classes.retrying);
                }
            },
            _onBeforeManualRetry: function(id) {
                if (this._parent.prototype._onBeforeManualRetry.apply(this, arguments)) {
                    this._templating.resetProgress(id);
                    miu(this._templating.getFileContainer(id)).removeClass(this._classes.fail);
                    this._templating.setStatusText(id);
                    this._templating.showSpinner(id);
                    this._showCancelLink(id);
                    return true;
                } else {
                    miu(this._templating.getFileContainer(id)).addClass(this._classes.retryable);
                    this._templating.showRetry(id);
                    return false;
                }
            },
            _onSubmitDelete: function(id) {
                var onSuccessCallback = miu.bind(this._onSubmitDeleteSuccess, this);
                this._parent.prototype._onSubmitDelete.call(this, id, onSuccessCallback);
            },
            _onSubmitDeleteSuccess: function(id, uuid, additionalMandatedParams) {
                if (this._options.deleteFile.forceConfirm) {
                    this._showDeleteConfirm.apply(this, arguments);
                } else {
                    this._sendDeleteRequest.apply(this, arguments);
                }
            },
            _onDeleteComplete: function(id, xhr, isError) {
                this._parent.prototype._onDeleteComplete.apply(this, arguments);
                this._templating.hideSpinner(id);
                if (isError) {
                    this._templating.setStatusText(id, this._options.deleteFile.deletingFailedText);
                    this._templating.showDeleteButton(id);
                } else {
                    this._removeFileItem(id);
                }
            },
            _sendDeleteRequest: function(id, uuid, additionalMandatedParams) {
                this._templating.hideDeleteButton(id);
                this._templating.showSpinner(id);
                this._templating.setStatusText(id, this._options.deleteFile.deletingStatusText);
                this._deleteHandler.sendDelete.apply(this, arguments);
            },
            _showDeleteConfirm: function(id, uuid, mandatedParams) {
                var fileName = this.getName(id), confirmMessage = this._options.deleteFile.confirmMessage.replace(/\{filename\}/g, fileName), uuid = this.getUuid(id), deleteRequestArgs = arguments, self = this, retVal;
                retVal = this._options.showConfirm(confirmMessage);
                if (miu.isGenericPromise(retVal)) {
                    retVal.then(function() {
                        self._sendDeleteRequest.apply(self, deleteRequestArgs);
                    });
                } else if (retVal !== false) {
                    self._sendDeleteRequest.apply(self, deleteRequestArgs);
                }
            },
            _addToList: function(id, name, canned) {
                var prependData, prependIndex = 0, dontDisplay = this._handler.isProxied(id) && this._options.scaling.hideScaled, record;
                if (this._options.display.prependFiles) {
                    if (this._totalFilesInBatch > 1 && this._filesInBatchAddedToUi > 0) {
                        prependIndex = this._filesInBatchAddedToUi - 1;
                    }
                    prependData = {
                        index: prependIndex
                    };
                }
                if (!canned) {
                    if (this._options.disableCancelForFormUploads && !miu.supportedFeatures.ajaxUploading) {
                        this._templating.disableCancel();
                    }
                    if (!this._options.multiple) {
                        record = this.getUploads({
                            id: id
                        });
                        this._handledProxyGroup = this._handledProxyGroup || record.proxyGroupId;
                        if (record.proxyGroupId !== this._handledProxyGroup || !record.proxyGroupId) {
                            this._handler.cancelAll();
                            this._clearList();
                            this._handledProxyGroup = null;
                        }
                    }
                }
                if (canned) {
                    this._templating.addFileToCache(id, this._options.formatFileName(name), prependData, dontDisplay);
                    this._templating.updateThumbnail(id, this._thumbnailUrls[id], true, this._options.thumbnails.customResizer);
                } else {
                    this._templating.addFile(id, this._options.formatFileName(name), prependData, dontDisplay);
                    this._templating.generatePreview(id, this.getFile(id), this._options.thumbnails.customResizer);
                }
                this._filesInBatchAddedToUi += 1;
                if (canned || this._options.display.fileSizeOnSubmit && miu.supportedFeatures.ajaxUploading) {
                    this._displayFileSize(id);
                }
            },
            _clearList: function() {
                this._templating.clearFiles();
                this.clearStoredFiles();
            },
            _displayFileSize: function(id, loadedSize, totalSize) {
                var size = this.getSize(id), sizeForDisplay = this._formatSize(size);
                if (size >= 0) {
                    if (loadedSize !== undefined && totalSize !== undefined) {
                        sizeForDisplay = this._formatProgress(loadedSize, totalSize);
                    }
                    this._templating.updateSize(id, sizeForDisplay);
                }
            },
            _formatProgress: function(uploadedSize, totalSize) {
                var message = this._options.text.formatProgress;
                function r(name, replacement) {
                    message = message.replace(name, replacement);
                }
                r("{percent}", Math.round(uploadedSize / totalSize * 100));
                r("{total_size}", this._formatSize(totalSize));
                return message;
            },
            _controlFailureTextDisplay: function(id, response) {
                var mode, responseProperty, failureReason;
                mode = this._options.failedUploadTextDisplay.mode;
                responseProperty = this._options.failedUploadTextDisplay.responseProperty;
                if (mode === "custom") {
                    failureReason = response[responseProperty];
                    if (!failureReason) {
                        failureReason = this._options.text.failUpload;
                    }
                    this._templating.setStatusText(id, failureReason);
                    if (this._options.failedUploadTextDisplay.enableTooltip) {
                        this._showTooltip(id, failureReason);
                    }
                } else if (mode === "default") {
                    this._templating.setStatusText(id, this._options.text.failUpload);
                } else if (mode !== "none") {
                    this.log("failedUploadTextDisplay.mode value of '" + mode + "' is not valid", "warn");
                }
            },
            _showTooltip: function(id, text) {
                this._templating.getFileContainer(id).title = text;
            },
            _showCancelLink: function(id) {
                if (!this._options.disableCancelForFormUploads || miu.supportedFeatures.ajaxUploading) {
                    this._templating.showCancel(id);
                }
            },
            _itemError: function(code, name, item) {
                var message = this._parent.prototype._itemError.apply(this, arguments);
                this._options.showMessage(message);
            },
            _batchError: function(message) {
                this._parent.prototype._batchError.apply(this, arguments);
                this._options.showMessage(message);
            },
            _setupPastePrompt: function() {
                var self = this;
                this._options.callbacks.onPasteReceived = function() {
                    var message = self._options.paste.namePromptMessage, defaultVal = self._options.paste.defaultName;
                    return self._options.showPrompt(message, defaultVal);
                };
            },
            _fileOrBlobRejected: function(id, name) {
                this._totalFilesInBatch -= 1;
                this._parent.prototype._fileOrBlobRejected.apply(this, arguments);
            },
            _prepareItemsForUpload: function(items, params, endpoint) {
                this._totalFilesInBatch = items.length;
                this._filesInBatchAddedToUi = 0;
                this._parent.prototype._prepareItemsForUpload.apply(this, arguments);
            },
            _maybeUpdateThumbnail: function(fileId) {
                var thumbnailUrl = this._thumbnailUrls[fileId], fileStatus = this.getUploads({
                    id: fileId
                }).status;
                if (fileStatus !== miu.status.DELETED && (thumbnailUrl || this._options.thumbnails.placeholders.waitUntilResponse || !miu.supportedFeatures.imagePreviews)) {
                    this._templating.updateThumbnail(fileId, thumbnailUrl, this._options.thumbnails.customResizer);
                }
            },
            _addCannedFile: function(sessionData) {
                var id = this._parent.prototype._addCannedFile.apply(this, arguments);
                this._addToList(id, this.getName(id), true);
                this._templating.hideSpinner(id);
                this._templating.hideCancel(id);
                this._markFileAsSuccessful(id);
                return id;
            },
            _setSize: function(id, newSize) {
                this._parent.prototype._setSize.apply(this, arguments);
                this._templating.updateSize(id, this._formatSize(newSize));
            },
            _sessionRequestComplete: function() {
                this._templating.addCacheToDom();
                this._parent.prototype._sessionRequestComplete.apply(this, arguments);
            }
        };
    })();
    miu.FineUploader = function(o, namespace) {
        "use strict";
        var self = this;
        this._parent = namespace ? miu[namespace].FineUploaderBasic : miu.FineUploaderBasic;
        this._parent.apply(this, arguments);
        miu.extend(this._options, {
            element: null,
            button: null,
            listElement: null,
            dragAndDrop: {
                extraDropzones: [],
                reportDirectoryPaths: false
            },
            text: {
                formatProgress: "{percent}% of {total_size}",
                failUpload: "Upload failed",
                waitingForResponse: "Processing...",
                paused: "Paused"
            },
            template: "miu-template",
            classes: {
                retrying: "miu-upload-retrying",
                retryable: "miu-upload-retryable",
                success: "miu-upload-success",
                fail: "miu-upload-fail",
                editable: "miu-editable",
                hide: "miu-hide",
                dropActive: "miu-upload-drop-area-active"
            },
            failedUploadTextDisplay: {
                mode: "default",
                responseProperty: "error",
                enableTooltip: true
            },
            messages: {
                tooManyFilesError: "You may only drop one file",
                unsupportedBrowser: "Unrecoverable error - this browser does not permit file uploading of any kind."
            },
            retry: {
                showAutoRetryNote: true,
                autoRetryNote: "Retrying {retryNum}/{maxAuto}..."
            },
            deleteFile: {
                forceConfirm: false,
                confirmMessage: "Are you sure you want to delete {filename}?",
                deletingStatusText: "Deleting...",
                deletingFailedText: "Delete failed"
            },
            display: {
                fileSizeOnSubmit: false,
                prependFiles: false
            },
            paste: {
                promptForName: false,
                namePromptMessage: "Please name this image"
            },
            thumbnails: {
                customResizer: null,
                maxCount: 0,
                placeholders: {
                    waitUntilResponse: false,
                    notAvailablePath: null,
                    waitingPath: null
                },
                timeBetweenThumbs: 750
            },
            scaling: {
                hideScaled: false
            },
            showMessage: function(message) {
                if (self._templating.hasDialog("alert")) {
                    return self._templating.showDialog("alert", message);
                } else {
                    setTimeout(function() {
                        window.alert(message);
                    }, 0);
                }
            },
            showConfirm: function(message) {
                if (self._templating.hasDialog("confirm")) {
                    return self._templating.showDialog("confirm", message);
                } else {
                    return window.confirm(message);
                }
            },
            showPrompt: function(message, defaultValue) {
                if (self._templating.hasDialog("prompt")) {
                    return self._templating.showDialog("prompt", message, defaultValue);
                } else {
                    return window.prompt(message, defaultValue);
                }
            }
        }, true);
        miu.extend(this._options, o, true);
        this._templating = new miu.Templating({
            log: miu.bind(this.log, this),
            templateIdOrEl: this._options.template,
            containerEl: this._options.element,
            fileContainerEl: this._options.listElement,
            button: this._options.button,
            imageGenerator: this._imageGenerator,
            classes: {
                hide: this._options.classes.hide,
                editable: this._options.classes.editable
            },
            limits: {
                maxThumbs: this._options.thumbnails.maxCount,
                timeBetweenThumbs: this._options.thumbnails.timeBetweenThumbs
            },
            placeholders: {
                waitUntilUpdate: this._options.thumbnails.placeholders.waitUntilResponse,
                thumbnailNotAvailable: this._options.thumbnails.placeholders.notAvailablePath,
                waitingForThumbnail: this._options.thumbnails.placeholders.waitingPath
            },
            text: this._options.text
        });
        if (this._options.workarounds.ios8SafariUploads && miu.ios800() && miu.iosSafari()) {
            this._templating.renderFailure(this._options.messages.unsupportedBrowserIos8Safari);
        } else if (!miu.supportedFeatures.uploading || this._options.cors.expected && !miu.supportedFeatures.uploadCors) {
            this._templating.renderFailure(this._options.messages.unsupportedBrowser);
        } else {
            this._wrapCallbacks();
            this._templating.render();
            this._classes = this._options.classes;
            if (!this._options.button && this._templating.getButton()) {
                this._defaultButtonId = this._createUploadButton({
                    element: this._templating.getButton(),
                    title: this._options.text.fileInputTitle
                }).getButtonId();
            }
            this._setupClickAndEditEventHandlers();
            if (miu.DragAndDrop && miu.supportedFeatures.fileDrop) {
                this._dnd = this._setupDragAndDrop();
            }
            if (this._options.paste.targetElement && this._options.paste.promptForName) {
                if (miu.PasteSupport) {
                    this._setupPastePrompt();
                } else {
                    this.log("Paste support module not found.", "error");
                }
            }
            this._totalFilesInBatch = 0;
            this._filesInBatchAddedToUi = 0;
        }
    };
    miu.extend(miu.FineUploader.prototype, miu.basePublicApi);
    miu.extend(miu.FineUploader.prototype, miu.basePrivateApi);
    miu.extend(miu.FineUploader.prototype, miu.uiPublicApi);
    miu.extend(miu.FineUploader.prototype, miu.uiPrivateApi);
    miu.Templating = function(spec) {
        "use strict";
        var FILE_ID_ATTR = "miu-file-id", FILE_CLASS_PREFIX = "miu-file-id-", THUMBNAIL_MAX_SIZE_ATTR = "miu-max-size", THUMBNAIL_SERVER_SCALE_ATTR = "miu-server-scale", HIDE_DROPZONE_ATTR = "miu-hide-dropzone", DROPZPONE_TEXT_ATTR = "miu-drop-area-text", IN_PROGRESS_CLASS = "miu-in-progress", HIDDEN_FOREVER_CLASS = "miu-hidden-forever", fileBatch = {
            content: document.createDocumentFragment(),
            map: {}
        }, isCancelDisabled = false, generatedThumbnails = 0, thumbnailQueueMonitorRunning = false, thumbGenerationQueue = [], thumbnailMaxSize = -1, options = {
            log: null,
            limits: {
                maxThumbs: 0,
                timeBetweenThumbs: 750
            },
            templateIdOrEl: "miu-template",
            containerEl: null,
            fileContainerEl: null,
            button: null,
            imageGenerator: null,
            classes: {
                hide: "miu-hide",
                editable: "miu-editable"
            },
            placeholders: {
                waitUntilUpdate: false,
                thumbnailNotAvailable: null,
                waitingForThumbnail: null
            },
            text: {
                paused: "Paused"
            }
        }, selectorClasses = {
            button: "miu-upload-button-selector",
            alertDialog: "miu-alert-dialog-selector",
            dialogCancelButton: "miu-cancel-button-selector",
            confirmDialog: "miu-confirm-dialog-selector",
            dialogMessage: "miu-dialog-message-selector",
            dialogOkButton: "miu-ok-button-selector",
            promptDialog: "miu-prompt-dialog-selector",
            uploader: "miu-uploader-selector",
            drop: "miu-upload-drop-area-selector",
            list: "miu-upload-list-selector",
            progressBarContainer: "miu-progress-bar-container-selector",
            progressBar: "miu-progress-bar-selector",
            totalProgressBarContainer: "miu-total-progress-bar-container-selector",
            totalProgressBar: "miu-total-progress-bar-selector",
            file: "miu-upload-file-selector",
            spinner: "miu-upload-spinner-selector",
            size: "miu-upload-size-selector",
            cancel: "miu-upload-cancel-selector",
            pause: "miu-upload-pause-selector",
            continueButton: "miu-upload-continue-selector",
            deleteButton: "miu-upload-delete-selector",
            retry: "miu-upload-retry-selector",
            statusText: "miu-upload-status-text-selector",
            editFilenameInput: "miu-edit-filename-selector",
            editNameIcon: "miu-edit-filename-icon-selector",
            dropText: "miu-upload-drop-area-text-selector",
            dropProcessing: "miu-drop-processing-selector",
            dropProcessingSpinner: "miu-drop-processing-spinner-selector",
            thumbnail: "miu-thumbnail-selector"
        }, previewGeneration = {}, cachedThumbnailNotAvailableImg = new miu.Promise(), cachedWaitingForThumbnailImg = new miu.Promise(), log, isEditElementsExist, isRetryElementExist, templateHtml, container, fileList, showThumbnails, serverScale, cacheThumbnailPlaceholders = function() {
            var notAvailableUrl = options.placeholders.thumbnailNotAvailable, waitingUrl = options.placeholders.waitingForThumbnail, spec = {
                maxSize: thumbnailMaxSize,
                scale: serverScale
            };
            if (showThumbnails) {
                if (notAvailableUrl) {
                    options.imageGenerator.generate(notAvailableUrl, new Image(), spec).then(function(updatedImg) {
                        cachedThumbnailNotAvailableImg.success(updatedImg);
                    }, function() {
                        cachedThumbnailNotAvailableImg.failure();
                        log("Problem loading 'not available' placeholder image at " + notAvailableUrl, "error");
                    });
                } else {
                    cachedThumbnailNotAvailableImg.failure();
                }
                if (waitingUrl) {
                    options.imageGenerator.generate(waitingUrl, new Image(), spec).then(function(updatedImg) {
                        cachedWaitingForThumbnailImg.success(updatedImg);
                    }, function() {
                        cachedWaitingForThumbnailImg.failure();
                        log("Problem loading 'waiting for thumbnail' placeholder image at " + waitingUrl, "error");
                    });
                } else {
                    cachedWaitingForThumbnailImg.failure();
                }
            }
        }, displayWaitingImg = function(thumbnail) {
            var waitingImgPlacement = new miu.Promise();
            cachedWaitingForThumbnailImg.then(function(img) {
                maybeScalePlaceholderViaCss(img, thumbnail);
                if (!thumbnail.src) {
                    thumbnail.src = img.src;
                    thumbnail.onload = function() {
                        thumbnail.onload = null;
                        show(thumbnail);
                        waitingImgPlacement.success();
                    };
                } else {
                    waitingImgPlacement.success();
                }
            }, function() {
                hide(thumbnail);
                waitingImgPlacement.success();
            });
            return waitingImgPlacement;
        }, generateNewPreview = function(id, blob, spec) {
            var thumbnail = getThumbnail(id);
            log("Generating new thumbnail for " + id);
            blob.miuThumbnailId = id;
            return options.imageGenerator.generate(blob, thumbnail, spec).then(function() {
                generatedThumbnails++;
                show(thumbnail);
                previewGeneration[id].success();
            }, function() {
                previewGeneration[id].failure();
                if (!options.placeholders.waitUntilUpdate) {
                    maybeSetDisplayNotAvailableImg(id, thumbnail);
                }
            });
        }, generateNextQueuedPreview = function() {
            if (thumbGenerationQueue.length) {
                thumbnailQueueMonitorRunning = true;
                var queuedThumbRequest = thumbGenerationQueue.shift();
                if (queuedThumbRequest.update) {
                    processUpdateQueuedPreviewRequest(queuedThumbRequest);
                } else {
                    processNewQueuedPreviewRequest(queuedThumbRequest);
                }
            } else {
                thumbnailQueueMonitorRunning = false;
            }
        }, getCancel = function(id) {
            return getTemplateEl(getFile(id), selectorClasses.cancel);
        }, getContinue = function(id) {
            return getTemplateEl(getFile(id), selectorClasses.continueButton);
        }, getDialog = function(type) {
            return getTemplateEl(container, selectorClasses[type + "Dialog"]);
        }, getDelete = function(id) {
            return getTemplateEl(getFile(id), selectorClasses.deleteButton);
        }, getDropProcessing = function() {
            return getTemplateEl(container, selectorClasses.dropProcessing);
        }, getEditIcon = function(id) {
            return getTemplateEl(getFile(id), selectorClasses.editNameIcon);
        }, getFile = function(id) {
            return fileBatch.map[id] || miu(fileList).getFirstByClass(FILE_CLASS_PREFIX + id);
        }, getFilename = function(id) {
            return getTemplateEl(getFile(id), selectorClasses.file);
        }, getPause = function(id) {
            return getTemplateEl(getFile(id), selectorClasses.pause);
        }, getProgress = function(id) {
            if (id == null) {
                return getTemplateEl(container, selectorClasses.totalProgressBarContainer) || getTemplateEl(container, selectorClasses.totalProgressBar);
            }
            return getTemplateEl(getFile(id), selectorClasses.progressBarContainer) || getTemplateEl(getFile(id), selectorClasses.progressBar);
        }, getRetry = function(id) {
            return getTemplateEl(getFile(id), selectorClasses.retry);
        }, getSize = function(id) {
            return getTemplateEl(getFile(id), selectorClasses.size);
        }, getSpinner = function(id) {
            return getTemplateEl(getFile(id), selectorClasses.spinner);
        }, getTemplateEl = function(context, cssClass) {
            return context && miu(context).getFirstByClass(cssClass);
        }, getThumbnail = function(id) {
            return showThumbnails && getTemplateEl(getFile(id), selectorClasses.thumbnail);
        }, hide = function(el) {
            el && miu(el).addClass(options.classes.hide);
        }, maybeScalePlaceholderViaCss = function(placeholder, thumbnail) {
            var maxWidth = placeholder.style.maxWidth, maxHeight = placeholder.style.maxHeight;
            if (maxHeight && maxWidth && !thumbnail.style.maxWidth && !thumbnail.style.maxHeight) {
                miu(thumbnail).css({
                    maxWidth: maxWidth,
                    maxHeight: maxHeight
                });
            }
        }, maybeSetDisplayNotAvailableImg = function(id, thumbnail) {
            var previewing = previewGeneration[id] || new miu.Promise().failure(), notAvailableImgPlacement = new miu.Promise();
            cachedThumbnailNotAvailableImg.then(function(img) {
                previewing.then(function() {
                    notAvailableImgPlacement.success();
                }, function() {
                    maybeScalePlaceholderViaCss(img, thumbnail);
                    thumbnail.onload = function() {
                        thumbnail.onload = null;
                        notAvailableImgPlacement.success();
                    };
                    thumbnail.src = img.src;
                    show(thumbnail);
                });
            });
            return notAvailableImgPlacement;
        }, parseAndGetTemplate = function() {
            var scriptEl, scriptHtml, fileListNode, tempTemplateEl, fileListHtml, defaultButton, dropArea, thumbnail, dropProcessing, dropTextEl, uploaderEl;
            log("Parsing template");
            if (options.templateIdOrEl == null) {
                throw new Error("You MUST specify either a template element or ID!");
            }
            if (miu.isString(options.templateIdOrEl)) {
                scriptEl = document.getElementById(options.templateIdOrEl);
                if (scriptEl === null) {
                    throw new Error(miu.format("Cannot find template script at ID '{}'!", options.templateIdOrEl));
                }
                scriptHtml = scriptEl.innerHTML;
            } else {
                if (options.templateIdOrEl.innerHTML === undefined) {
                    throw new Error("You have specified an invalid value for the template option!  " + "It must be an ID or an Element.");
                }
                scriptHtml = options.templateIdOrEl.innerHTML;
            }
            scriptHtml = miu.trimStr(scriptHtml);
            tempTemplateEl = document.createElement("div");
            tempTemplateEl.appendChild(miu.toElement(scriptHtml));
            uploaderEl = miu(tempTemplateEl).getFirstByClass(selectorClasses.uploader);
            if (options.button) {
                defaultButton = miu(tempTemplateEl).getFirstByClass(selectorClasses.button);
                if (defaultButton) {
                    miu(defaultButton).remove();
                }
            }
            if (!miu.DragAndDrop || !miu.supportedFeatures.fileDrop) {
                dropProcessing = miu(tempTemplateEl).getFirstByClass(selectorClasses.dropProcessing);
                if (dropProcessing) {
                    miu(dropProcessing).remove();
                }
            }
            dropArea = miu(tempTemplateEl).getFirstByClass(selectorClasses.drop);
            if (dropArea && !miu.DragAndDrop) {
                log("DnD module unavailable.", "info");
                miu(dropArea).remove();
            }
            if (!miu.supportedFeatures.fileDrop) {
                uploaderEl.removeAttribute(DROPZPONE_TEXT_ATTR);
                if (dropArea && miu(dropArea).hasAttribute(HIDE_DROPZONE_ATTR)) {
                    miu(dropArea).css({
                        display: "none"
                    });
                }
            } else if (miu(uploaderEl).hasAttribute(DROPZPONE_TEXT_ATTR) && dropArea) {
                dropTextEl = miu(dropArea).getFirstByClass(selectorClasses.dropText);
                dropTextEl && miu(dropTextEl).remove();
            }
            thumbnail = miu(tempTemplateEl).getFirstByClass(selectorClasses.thumbnail);
            if (!showThumbnails) {
                thumbnail && miu(thumbnail).remove();
            } else if (thumbnail) {
                thumbnailMaxSize = parseInt(thumbnail.getAttribute(THUMBNAIL_MAX_SIZE_ATTR));
                thumbnailMaxSize = thumbnailMaxSize > 0 ? thumbnailMaxSize : null;
                serverScale = miu(thumbnail).hasAttribute(THUMBNAIL_SERVER_SCALE_ATTR);
            }
            showThumbnails = showThumbnails && thumbnail;
            isEditElementsExist = miu(tempTemplateEl).getByClass(selectorClasses.editFilenameInput).length > 0;
            isRetryElementExist = miu(tempTemplateEl).getByClass(selectorClasses.retry).length > 0;
            fileListNode = miu(tempTemplateEl).getFirstByClass(selectorClasses.list);
            if (fileListNode == null) {
                throw new Error("Could not find the file list container in the template!");
            }
            fileListHtml = fileListNode.innerHTML;
            fileListNode.innerHTML = "";
            if (tempTemplateEl.getElementsByTagName("DIALOG").length) {
                document.createElement("dialog");
            }
            log("Template parsing complete");
            return {
                template: miu.trimStr(tempTemplateEl.innerHTML),
                fileTemplate: miu.trimStr(fileListHtml)
            };
        }, prependFile = function(el, index, fileList) {
            var parentEl = fileList, beforeEl = parentEl.firstChild;
            if (index > 0) {
                beforeEl = miu(parentEl).children()[index].nextSibling;
            }
            parentEl.insertBefore(el, beforeEl);
        }, processNewQueuedPreviewRequest = function(queuedThumbRequest) {
            var id = queuedThumbRequest.id, optFileOrBlob = queuedThumbRequest.optFileOrBlob, relatedThumbnailId = optFileOrBlob && optFileOrBlob.miuThumbnailId, thumbnail = getThumbnail(id), spec = {
                customResizeFunction: queuedThumbRequest.customResizeFunction,
                maxSize: thumbnailMaxSize,
                orient: true,
                scale: true
            };
            if (miu.supportedFeatures.imagePreviews) {
                if (thumbnail) {
                    if (options.limits.maxThumbs && options.limits.maxThumbs <= generatedThumbnails) {
                        maybeSetDisplayNotAvailableImg(id, thumbnail);
                        generateNextQueuedPreview();
                    } else {
                        displayWaitingImg(thumbnail).done(function() {
                            previewGeneration[id] = new miu.Promise();
                            previewGeneration[id].done(function() {
                                setTimeout(generateNextQueuedPreview, options.limits.timeBetweenThumbs);
                            });
                            if (relatedThumbnailId != null) {
                                useCachedPreview(id, relatedThumbnailId);
                            } else {
                                generateNewPreview(id, optFileOrBlob, spec);
                            }
                        });
                    }
                } else {
                    generateNextQueuedPreview();
                }
            } else if (thumbnail) {
                displayWaitingImg(thumbnail);
                generateNextQueuedPreview();
            }
        }, processUpdateQueuedPreviewRequest = function(queuedThumbRequest) {
            var id = queuedThumbRequest.id, thumbnailUrl = queuedThumbRequest.thumbnailUrl, showWaitingImg = queuedThumbRequest.showWaitingImg, thumbnail = getThumbnail(id), spec = {
                customResizeFunction: queuedThumbRequest.customResizeFunction,
                scale: serverScale,
                maxSize: thumbnailMaxSize
            };
            if (thumbnail) {
                if (thumbnailUrl) {
                    if (options.limits.maxThumbs && options.limits.maxThumbs <= generatedThumbnails) {
                        maybeSetDisplayNotAvailableImg(id, thumbnail);
                        generateNextQueuedPreview();
                    } else {
                        if (showWaitingImg) {
                            displayWaitingImg(thumbnail);
                        }
                        return options.imageGenerator.generate(thumbnailUrl, thumbnail, spec).then(function() {
                            show(thumbnail);
                            generatedThumbnails++;
                            setTimeout(generateNextQueuedPreview, options.limits.timeBetweenThumbs);
                        }, function() {
                            maybeSetDisplayNotAvailableImg(id, thumbnail);
                            setTimeout(generateNextQueuedPreview, options.limits.timeBetweenThumbs);
                        });
                    }
                } else {
                    maybeSetDisplayNotAvailableImg(id, thumbnail);
                    generateNextQueuedPreview();
                }
            }
        }, setProgressBarWidth = function(id, percent) {
            var bar = getProgress(id), progressBarSelector = id == null ? selectorClasses.totalProgressBar : selectorClasses.progressBar;
            if (bar && !miu(bar).hasClass(progressBarSelector)) {
                bar = miu(bar).getFirstByClass(progressBarSelector);
            }
            if (bar) {
                miu(bar).css({
                    width: percent + "%"
                });
                bar.setAttribute("aria-valuenow", percent);
            }
        }, show = function(el) {
            el && miu(el).removeClass(options.classes.hide);
        }, useCachedPreview = function(targetThumbnailId, cachedThumbnailId) {
            var targetThumbnail = getThumbnail(targetThumbnailId), cachedThumbnail = getThumbnail(cachedThumbnailId);
            log(miu.format("ID {} is the same file as ID {}.  Will use generated thumbnail from ID {} instead.", targetThumbnailId, cachedThumbnailId, cachedThumbnailId));
            previewGeneration[cachedThumbnailId].then(function() {
                generatedThumbnails++;
                previewGeneration[targetThumbnailId].success();
                log(miu.format("Now using previously generated thumbnail created for ID {} on ID {}.", cachedThumbnailId, targetThumbnailId));
                targetThumbnail.src = cachedThumbnail.src;
                show(targetThumbnail);
            }, function() {
                previewGeneration[targetThumbnailId].failure();
                if (!options.placeholders.waitUntilUpdate) {
                    maybeSetDisplayNotAvailableImg(targetThumbnailId, targetThumbnail);
                }
            });
        };
        miu.extend(options, spec);
        log = options.log;
        if (!miu.supportedFeatures.imagePreviews) {
            options.limits.timeBetweenThumbs = 0;
            options.limits.maxThumbs = 0;
        }
        container = options.containerEl;
        showThumbnails = options.imageGenerator !== undefined;
        templateHtml = parseAndGetTemplate();
        cacheThumbnailPlaceholders();
        miu.extend(this, {
            render: function() {
                log("Rendering template in DOM.");
                generatedThumbnails = 0;
                container.innerHTML = templateHtml.template;
                hide(getDropProcessing());
                this.hideTotalProgress();
                fileList = options.fileContainerEl || getTemplateEl(container, selectorClasses.list);
                log("Template rendering complete");
            },
            renderFailure: function(message) {
                var cantRenderEl = miu.toElement(message);
                container.innerHTML = "";
                container.appendChild(cantRenderEl);
            },
            reset: function() {
                this.render();
            },
            clearFiles: function() {
                fileList.innerHTML = "";
            },
            disableCancel: function() {
                isCancelDisabled = true;
            },
            addFile: function(id, name, prependInfo, hideForever, batch) {
                var fileEl = miu.toElement(templateHtml.fileTemplate), fileNameEl = getTemplateEl(fileEl, selectorClasses.file), uploaderEl = getTemplateEl(container, selectorClasses.uploader), fileContainer = batch ? fileBatch.content : fileList, thumb;
                if (batch) {
                    fileBatch.map[id] = fileEl;
                }
                miu(fileEl).addClass(FILE_CLASS_PREFIX + id);
                uploaderEl.removeAttribute(DROPZPONE_TEXT_ATTR);
                if (fileNameEl) {
                    miu(fileNameEl).setText(name);
                    fileNameEl.setAttribute("title", name);
                }
                fileEl.setAttribute(FILE_ID_ATTR, id);
                if (prependInfo) {
                    prependFile(fileEl, prependInfo.index, fileContainer);
                } else {
                    fileContainer.appendChild(fileEl);
                }
                if (hideForever) {
                    fileEl.style.display = "none";
                    miu(fileEl).addClass(HIDDEN_FOREVER_CLASS);
                } else {
                    hide(getProgress(id));
                    hide(getSize(id));
                    hide(getDelete(id));
                    hide(getRetry(id));
                    hide(getPause(id));
                    hide(getContinue(id));
                    if (isCancelDisabled) {
                        this.hideCancel(id);
                    }
                    thumb = getThumbnail(id);
                    if (thumb && !thumb.src) {
                        cachedWaitingForThumbnailImg.then(function(waitingImg) {
                            thumb.src = waitingImg.src;
                            if (waitingImg.style.maxHeight && waitingImg.style.maxWidth) {
                                miu(thumb).css({
                                    maxHeight: waitingImg.style.maxHeight,
                                    maxWidth: waitingImg.style.maxWidth
                                });
                            }
                            show(thumb);
                        });
                    }
                }
            },
            addFileToCache: function(id, name, prependInfo, hideForever) {
                this.addFile(id, name, prependInfo, hideForever, true);
            },
            addCacheToDom: function() {
                fileList.appendChild(fileBatch.content);
                fileBatch.content = document.createDocumentFragment();
                fileBatch.map = {};
            },
            removeFile: function(id) {
                miu(getFile(id)).remove();
            },
            getFileId: function(el) {
                var currentNode = el;
                if (currentNode) {
                    while (currentNode.getAttribute(FILE_ID_ATTR) == null) {
                        currentNode = currentNode.parentNode;
                    }
                    return parseInt(currentNode.getAttribute(FILE_ID_ATTR));
                }
            },
            getFileList: function() {
                return fileList;
            },
            markFilenameEditable: function(id) {
                var filename = getFilename(id);
                filename && miu(filename).addClass(options.classes.editable);
            },
            updateFilename: function(id, name) {
                var filenameEl = getFilename(id);
                if (filenameEl) {
                    miu(filenameEl).setText(name);
                    filenameEl.setAttribute("title", name);
                }
            },
            hideFilename: function(id) {
                hide(getFilename(id));
            },
            showFilename: function(id) {
                show(getFilename(id));
            },
            isFileName: function(el) {
                return miu(el).hasClass(selectorClasses.file);
            },
            getButton: function() {
                return options.button || getTemplateEl(container, selectorClasses.button);
            },
            hideDropProcessing: function() {
                hide(getDropProcessing());
            },
            showDropProcessing: function() {
                show(getDropProcessing());
            },
            getDropZone: function() {
                return getTemplateEl(container, selectorClasses.drop);
            },
            isEditFilenamePossible: function() {
                return isEditElementsExist;
            },
            hideRetry: function(id) {
                hide(getRetry(id));
            },
            isRetryPossible: function() {
                return isRetryElementExist;
            },
            showRetry: function(id) {
                show(getRetry(id));
            },
            getFileContainer: function(id) {
                return getFile(id);
            },
            showEditIcon: function(id) {
                var icon = getEditIcon(id);
                icon && miu(icon).addClass(options.classes.editable);
            },
            isHiddenForever: function(id) {
                return miu(getFile(id)).hasClass(HIDDEN_FOREVER_CLASS);
            },
            hideEditIcon: function(id) {
                var icon = getEditIcon(id);
                icon && miu(icon).removeClass(options.classes.editable);
            },
            isEditIcon: function(el) {
                return miu(el).hasClass(selectorClasses.editNameIcon, true);
            },
            getEditInput: function(id) {
                return getTemplateEl(getFile(id), selectorClasses.editFilenameInput);
            },
            isEditInput: function(el) {
                return miu(el).hasClass(selectorClasses.editFilenameInput, true);
            },
            updateProgress: function(id, loaded, total) {
                var bar = getProgress(id), percent;
                if (bar && total > 0) {
                    percent = Math.round(loaded / total * 100);
                    if (percent === 100) {
                        hide(bar);
                    } else {
                        show(bar);
                    }
                    setProgressBarWidth(id, percent);
                }
            },
            updateTotalProgress: function(loaded, total) {
                this.updateProgress(null, loaded, total);
            },
            hideProgress: function(id) {
                var bar = getProgress(id);
                bar && hide(bar);
            },
            hideTotalProgress: function() {
                this.hideProgress();
            },
            resetProgress: function(id) {
                setProgressBarWidth(id, 0);
                this.hideTotalProgress(id);
            },
            resetTotalProgress: function() {
                this.resetProgress();
            },
            showCancel: function(id) {
                if (!isCancelDisabled) {
                    var cancel = getCancel(id);
                    cancel && miu(cancel).removeClass(options.classes.hide);
                }
            },
            hideCancel: function(id) {
                hide(getCancel(id));
            },
            isCancel: function(el) {
                return miu(el).hasClass(selectorClasses.cancel, true);
            },
            allowPause: function(id) {
                show(getPause(id));
                hide(getContinue(id));
            },
            uploadPaused: function(id) {
                this.setStatusText(id, options.text.paused);
                this.allowContinueButton(id);
                hide(getSpinner(id));
            },
            hidePause: function(id) {
                hide(getPause(id));
            },
            isPause: function(el) {
                return miu(el).hasClass(selectorClasses.pause, true);
            },
            isContinueButton: function(el) {
                return miu(el).hasClass(selectorClasses.continueButton, true);
            },
            allowContinueButton: function(id) {
                show(getContinue(id));
                hide(getPause(id));
            },
            uploadContinued: function(id) {
                this.setStatusText(id, "");
                this.allowPause(id);
                show(getSpinner(id));
            },
            showDeleteButton: function(id) {
                show(getDelete(id));
            },
            hideDeleteButton: function(id) {
                hide(getDelete(id));
            },
            isDeleteButton: function(el) {
                return miu(el).hasClass(selectorClasses.deleteButton, true);
            },
            isRetry: function(el) {
                return miu(el).hasClass(selectorClasses.retry, true);
            },
            updateSize: function(id, text) {
                var size = getSize(id);
                if (size) {
                    show(size);
                    miu(size).setText(text);
                }
            },
            setStatusText: function(id, text) {
                var textEl = getTemplateEl(getFile(id), selectorClasses.statusText);
                if (textEl) {
                    if (text == null) {
                        miu(textEl).clearText();
                    } else {
                        miu(textEl).setText(text);
                    }
                }
            },
            hideSpinner: function(id) {
                miu(getFile(id)).removeClass(IN_PROGRESS_CLASS);
                hide(getSpinner(id));
            },
            showSpinner: function(id) {
                miu(getFile(id)).addClass(IN_PROGRESS_CLASS);
                show(getSpinner(id));
            },
            generatePreview: function(id, optFileOrBlob, customResizeFunction) {
                if (!this.isHiddenForever(id)) {
                    thumbGenerationQueue.push({
                        id: id,
                        customResizeFunction: customResizeFunction,
                        optFileOrBlob: optFileOrBlob
                    });
                    !thumbnailQueueMonitorRunning && generateNextQueuedPreview();
                }
            },
            updateThumbnail: function(id, thumbnailUrl, showWaitingImg, customResizeFunction) {
                if (!this.isHiddenForever(id)) {
                    thumbGenerationQueue.push({
                        customResizeFunction: customResizeFunction,
                        update: true,
                        id: id,
                        thumbnailUrl: thumbnailUrl,
                        showWaitingImg: showWaitingImg
                    });
                    !thumbnailQueueMonitorRunning && generateNextQueuedPreview();
                }
            },
            hasDialog: function(type) {
                return miu.supportedFeatures.dialogElement && !!getDialog(type);
            },
            showDialog: function(type, message, defaultValue) {
                var dialog = getDialog(type), messageEl = getTemplateEl(dialog, selectorClasses.dialogMessage), inputEl = dialog.getElementsByTagName("INPUT")[0], cancelBtn = getTemplateEl(dialog, selectorClasses.dialogCancelButton), okBtn = getTemplateEl(dialog, selectorClasses.dialogOkButton), promise = new miu.Promise(), closeHandler = function() {
                    cancelBtn.removeEventListener("click", cancelClickHandler);
                    okBtn && okBtn.removeEventListener("click", okClickHandler);
                    promise.failure();
                }, cancelClickHandler = function() {
                    cancelBtn.removeEventListener("click", cancelClickHandler);
                    dialog.close();
                }, okClickHandler = function() {
                    dialog.removeEventListener("close", closeHandler);
                    okBtn.removeEventListener("click", okClickHandler);
                    dialog.close();
                    promise.success(inputEl && inputEl.value);
                };
                dialog.addEventListener("close", closeHandler);
                cancelBtn.addEventListener("click", cancelClickHandler);
                okBtn && okBtn.addEventListener("click", okClickHandler);
                if (inputEl) {
                    inputEl.value = defaultValue;
                }
                messageEl.textContent = message;
                dialog.showModal();
                return promise;
            }
        });
    };
    miu.UiEventHandler = function(s, protectedApi) {
        "use strict";
        var disposer = new miu.DisposeSupport(), spec = {
            eventType: "click",
            attachTo: null,
            onHandled: function(target, event) {}
        };
        miu.extend(this, {
            addHandler: function(element) {
                addHandler(element);
            },
            dispose: function() {
                disposer.dispose();
            }
        });
        function addHandler(element) {
            disposer.attach(element, spec.eventType, function(event) {
                event = event || window.event;
                var target = event.target || event.srcElement;
                spec.onHandled(target, event);
            });
        }
        miu.extend(protectedApi, {
            getFileIdFromItem: function(item) {
                return item.miuFileId;
            },
            getDisposeSupport: function() {
                return disposer;
            }
        });
        miu.extend(spec, s);
        if (spec.attachTo) {
            addHandler(spec.attachTo);
        }
    };
    miu.FileButtonsClickHandler = function(s) {
        "use strict";
        var inheritedInternalApi = {}, spec = {
            templating: null,
            log: function(message, lvl) {},
            onDeleteFile: function(fileId) {},
            onCancel: function(fileId) {},
            onRetry: function(fileId) {},
            onPause: function(fileId) {},
            onContinue: function(fileId) {},
            onGetName: function(fileId) {}
        }, buttonHandlers = {
            cancel: function(id) {
                spec.onCancel(id);
            },
            retry: function(id) {
                spec.onRetry(id);
            },
            deleteButton: function(id) {
                spec.onDeleteFile(id);
            },
            pause: function(id) {
                spec.onPause(id);
            },
            continueButton: function(id) {
                spec.onContinue(id);
            }
        };
        function examineEvent(target, event) {
            miu.each(buttonHandlers, function(buttonType, handler) {
                var firstLetterCapButtonType = buttonType.charAt(0).toUpperCase() + buttonType.slice(1), fileId;
                if (spec.templating["is" + firstLetterCapButtonType](target)) {
                    fileId = spec.templating.getFileId(target);
                    miu.preventDefault(event);
                    spec.log(miu.format("Detected valid file button click event on file '{}', ID: {}.", spec.onGetName(fileId), fileId));
                    handler(fileId);
                    return false;
                }
            });
        }
        miu.extend(spec, s);
        spec.eventType = "click";
        spec.onHandled = examineEvent;
        spec.attachTo = spec.templating.getFileList();
        miu.extend(this, new miu.UiEventHandler(spec, inheritedInternalApi));
    };
    miu.FilenameClickHandler = function(s) {
        "use strict";
        var inheritedInternalApi = {}, spec = {
            templating: null,
            log: function(message, lvl) {},
            classes: {
                file: "miu-upload-file",
                editNameIcon: "miu-edit-filename-icon"
            },
            onGetUploadStatus: function(fileId) {},
            onGetName: function(fileId) {}
        };
        miu.extend(spec, s);
        function examineEvent(target, event) {
            if (spec.templating.isFileName(target) || spec.templating.isEditIcon(target)) {
                var fileId = spec.templating.getFileId(target), status = spec.onGetUploadStatus(fileId);
                if (status === miu.status.SUBMITTED) {
                    spec.log(miu.format("Detected valid filename click event on file '{}', ID: {}.", spec.onGetName(fileId), fileId));
                    miu.preventDefault(event);
                    inheritedInternalApi.handleFilenameEdit(fileId, target, true);
                }
            }
        }
        spec.eventType = "click";
        spec.onHandled = examineEvent;
        miu.extend(this, new miu.FilenameEditHandler(spec, inheritedInternalApi));
    };
    miu.FilenameInputFocusInHandler = function(s, inheritedInternalApi) {
        "use strict";
        var spec = {
            templating: null,
            onGetUploadStatus: function(fileId) {},
            log: function(message, lvl) {}
        };
        if (!inheritedInternalApi) {
            inheritedInternalApi = {};
        }
        function handleInputFocus(target, event) {
            if (spec.templating.isEditInput(target)) {
                var fileId = spec.templating.getFileId(target), status = spec.onGetUploadStatus(fileId);
                if (status === miu.status.SUBMITTED) {
                    spec.log(miu.format("Detected valid filename input focus event on file '{}', ID: {}.", spec.onGetName(fileId), fileId));
                    inheritedInternalApi.handleFilenameEdit(fileId, target);
                }
            }
        }
        spec.eventType = "focusin";
        spec.onHandled = handleInputFocus;
        miu.extend(spec, s);
        miu.extend(this, new miu.FilenameEditHandler(spec, inheritedInternalApi));
    };
    miu.FilenameInputFocusHandler = function(spec) {
        "use strict";
        spec.eventType = "focus";
        spec.attachTo = null;
        miu.extend(this, new miu.FilenameInputFocusInHandler(spec, {}));
    };
    miu.FilenameEditHandler = function(s, inheritedInternalApi) {
        "use strict";
        var spec = {
            templating: null,
            log: function(message, lvl) {},
            onGetUploadStatus: function(fileId) {},
            onGetName: function(fileId) {},
            onSetName: function(fileId, newName) {},
            onEditingStatusChange: function(fileId, isEditing) {}
        };
        function getFilenameSansExtension(fileId) {
            var filenameSansExt = spec.onGetName(fileId), extIdx = filenameSansExt.lastIndexOf(".");
            if (extIdx > 0) {
                filenameSansExt = filenameSansExt.substr(0, extIdx);
            }
            return filenameSansExt;
        }
        function getOriginalExtension(fileId) {
            var origName = spec.onGetName(fileId);
            return miu.getExtension(origName);
        }
        function handleNameUpdate(newFilenameInputEl, fileId) {
            var newName = newFilenameInputEl.value, origExtension;
            if (newName !== undefined && miu.trimStr(newName).length > 0) {
                origExtension = getOriginalExtension(fileId);
                if (origExtension !== undefined) {
                    newName = newName + "." + origExtension;
                }
                spec.onSetName(fileId, newName);
            }
            spec.onEditingStatusChange(fileId, false);
        }
        function registerInputBlurHandler(inputEl, fileId) {
            inheritedInternalApi.getDisposeSupport().attach(inputEl, "blur", function() {
                handleNameUpdate(inputEl, fileId);
            });
        }
        function registerInputEnterKeyHandler(inputEl, fileId) {
            inheritedInternalApi.getDisposeSupport().attach(inputEl, "keyup", function(event) {
                var code = event.keyCode || event.which;
                if (code === 13) {
                    handleNameUpdate(inputEl, fileId);
                }
            });
        }
        miu.extend(spec, s);
        spec.attachTo = spec.templating.getFileList();
        miu.extend(this, new miu.UiEventHandler(spec, inheritedInternalApi));
        miu.extend(inheritedInternalApi, {
            handleFilenameEdit: function(id, target, focusInput) {
                var newFilenameInputEl = spec.templating.getEditInput(id);
                spec.onEditingStatusChange(id, true);
                newFilenameInputEl.value = getFilenameSansExtension(id);
                if (focusInput) {
                    newFilenameInputEl.focus();
                }
                registerInputBlurHandler(newFilenameInputEl, id);
                registerInputEnterKeyHandler(newFilenameInputEl, id);
            }
        });
    };
})(window);
//# sourceMappingURL=jquery.fine-uploader.js.map