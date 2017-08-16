!function (e, t) {
    if ("object" == typeof exports && "object" == typeof module)module.exports = t(require("MediumEditor")); else if ("function" == typeof define && define.amd)define(["MediumEditor"], t); else {
        var i = t("object" == typeof exports ? require("MediumEditor") : e.MediumEditor);
        for (var n in i)("object" == typeof exports ? exports : e)[n] = i[n]
    }
}(this, function (e) {
    return function (e) {
        function t(n) {
            if (i[n])return i[n].exports;
            var a = i[n] = {exports: {}, id: n, loaded: !1};
            return e[n].call(a.exports, a, a.exports, t), a.loaded = !0, a.exports
        }

        var i = {};
        return t.m = e, t.c = i, t.p = "", t(0)
    }([function (e, t, i) {
        "use strict";
        function n(e) {
            return e && e.__esModule ? e : {"default": e}
        }

        function a(e) {
            return e[e.length - 1]
        }

        function s(e, t) {
            var i = e.parentNode;
            mediumEditor["default"].util.unwrap(e, t);
            for (var n = i.lastChild, a = n.previousSibling; a;)3 === n.nodeType && 3 === a.nodeType && (a.textContent += n.textContent, i.removeChild(n)), n = a, a = n.previousSibling
        }

        Object.defineProperty(t, "__esModule", {value: !0}),
            t.unwrapForTextNode = s;
        var r = i(1), mediumEditor = n(r);
        var h = t.TCMention = mediumEditor["default"].Extension.extend({
            name: "mention",
            extraClassName: "",
            extraActiveClassName: "",
            extraPanelClassName: "",
            extraActivePanelClassName: "",
            extraTriggerClassNameMap: {},
            extraActiveTriggerClassNameMap: {},
            tagName: "strong",
            renderPanelContent: function (panelEl, currentMentionText, endMentionCallback) {
                this.getSuggestions(this.word, panelEl, this.buildPanel)
            },
            destroyPanelContent: function () {
            },
            activeTriggerList: ["@"],
            triggerClassNameMap: {"#": "medium-editor-mention-hash", "@": "medium-editor-mention-at"},
            activeTriggerClassNameMap: {"#": "medium-editor-mention-hash-active", "@": "medium-editor-mention-at-active"},
            hideOnBlurDelay: 300,
            attributeValueLiName:"data-value",
            init: function () {
                this.initMentionPanel(), this.attachEventHandlers()
            },
            destroy: function () {
                this.detachEventHandlers(), this.destroyMentionPanel()
            },
            initMentionPanel: function () {
                var e = this.document.createElement("div");
                e.classList.add("medium-editor-mention-panel"), (this.extraPanelClassName || this.extraClassName) && e.classList.add(this.extraPanelClassName || this.extraClassName), this.getEditorOption("elementsContainer").appendChild(e), this.mentionPanel = e
            },
            destroyMentionPanel: function () {
                this.mentionPanel && (this.mentionPanel.parentNode && (this.destroyPanelContent(this.mentionPanel), this.mentionPanel.parentNode.removeChild(this.mentionPanel)), delete this.mentionPanel)
            },
            attachEventHandlers: function () {
                var e = this;
                this.unsubscribeCallbacks = [];
                var t = function (t, i) {
                    var n = e[i].bind(e);
                    e.subscribe(t, n), e.unsubscribeCallbacks.push(function () {
                        e.base.unsubscribe(t, n)
                    })
                };
                null !== this.hideOnBlurDelay && void 0 !== this.hideOnBlurDelay && (t("blur", "handleBlur"), t("focus", "handleFocus")), t("editableKeyup", "handleKeyup")
            },
            detachEventHandlers: function () {
                this.hideOnBlurDelayId && clearTimeout(this.hideOnBlurDelayId);
                this.unsubscribeCallbacks && (this.unsubscribeCallbacks.forEach(function (e) {
                    return e()
                }), this.unsubscribeCallbacks = null)
            },
            stopDefaultActionEvent:function(event){
                return MediumEditor.util.isKey(event, [MediumEditor.util.keyCode.DOWN, MediumEditor.util.keyCode.UP, MediumEditor.util.keyCode.ENTER]) && this.isActivePanel();
            },
            handleBlur: function () {
                var e = this;
                null !== this.hideOnBlurDelay && void 0 !== this.hideOnBlurDelay && (this.hideOnBlurDelayId = setTimeout(function () {
                    e.hidePanel(!1)
                }, this.hideOnBlurDelay))
            },
            handleFocus: function () {
                this.hideOnBlurDelayId && (clearTimeout(this.hideOnBlurDelayId), this.hideOnBlurDelayId = null)
            },
            handleKeyup: function (e) {
                var keyCode = mediumEditor["default"].util.getKeyCode(e);
                var isSpace = keyCode === mediumEditor["default"].util.keyCode.SPACE;
                this.getWordFromSelection(e.target, isSpace ? -1 : 0);
                var classSelected = "mention-selected"
                if (!isSpace && -1 !== this.activeTriggerList.indexOf(this.trigger) && this.word.length > 1){
                    var ulNode = document.getElementsByClassName("medium-editor-mention-panel")[0].firstChild;
                    if (this.isActivePanel() && MediumEditor.util.isKey(e, MediumEditor.util.keyCode.UP)){
                        var selectedLi = ulNode.querySelector("li."+classSelected);
                        if (!selectedLi){
                            ulNode.lastChild.classList.add(classSelected);
                        }else{
                            selectedLi.classList.remove(classSelected)
                            selectedLi = selectedLi.previousElementSibling;
                            if (!selectedLi){
                                selectedLi = ulNode.lastChild;
                            }
                            selectedLi.classList.add(classSelected);
                        }
                    }else if (this.isActivePanel() && MediumEditor.util.isKey(e, MediumEditor.util.keyCode.DOWN)){
                        var selectedLi = ulNode.querySelector("li."+classSelected);
                        if (!selectedLi){
                            ulNode.firstChild.classList.add(classSelected);
                        }else{
                            selectedLi.classList.remove(classSelected)
                            selectedLi = selectedLi.nextElementSibling;
                            if (!selectedLi){
                                selectedLi = ulNode.firstChild;
                            }
                            selectedLi.classList.add(classSelected);
                        }
                    }else if (this.isActivePanel() && MediumEditor.util.isKey(e, MediumEditor.util.keyCode.LEFT)){
                        //console.log("left")
                    }else if (this.isActivePanel() && MediumEditor.util.isKey(e, [MediumEditor.util.keyCode.RIGHT, MediumEditor.util.keyCode.ENTER])){
                        var selectedLi = ulNode.querySelector("li."+classSelected);
                        if (selectedLi){
                            this.handleSelectMention(this.getNodeLiSuggestionValue(selectedLi))
                        }
                    }else{
                        this.showPanel();
                    }
                } else{
                    this.hidePanel(MediumEditor.util.isKey(event, MediumEditor.util.keyCode.LEFT));
                }
            },
            getNodeLiSuggestionValue:function(nodeLI){
                return nodeLI.getAttribute(this.attributeValueLiName)
            },
            hidePanel: function (e) {
                this.mentionPanel.classList.remove("medium-editor-mention-panel-active");
                var t = this.extraActivePanelClassName || this.extraActiveClassName;
                if (t && this.mentionPanel.classList.remove(t), this.activeMentionAt && (this.activeMentionAt.classList.remove(this.activeTriggerClassName), this.extraActiveTriggerClassName && this.activeMentionAt.classList.remove(this.extraActiveTriggerClassName)), this.activeMentionAt) {
                    var i = this.activeMentionAt, n = i.parentNode, r = i.previousSibling, l = i.nextSibling, h = i.firstChild, d = e ? r : l, c = void 0;
                    d ? 3 !== d.nodeType ? (c = this.document.createTextNode(""), n.insertBefore(c, d)) : c = d : (c = this.document.createTextNode(""), n.appendChild(c));
                    var u = a(h.textContent), m = 0 === u.trim().length;
                    if (m) {
                        var g = h.textContent;
                        h.textContent = g.substr(0, g.length - 1), c.textContent = "" + u + c.textContent
                    } else 0 === c.textContent.length && h.textContent.length > 1 && (c.textContent = " ");
                    e ? mediumEditor["default"].selection.select(this.document, c, c.length) : mediumEditor["default"].selection.select(this.document, c, Math.min(c.length, 1)), h.textContent.length <= 1 && (this.base.saveSelection(), s(this.activeMentionAt, this.document), this.base.restoreSelection()), this.activeMentionAt = null
                }
            },
            getSuggestions:function(prefix, panelEl, buildPanel){
                buildPanel(panelEl, [{value:prefix+"1", text:prefix.slice(1)+'(1)'}, {value:prefix+"2", text:prefix.slice(1)+'(2)'}], this)
            },
            buildPanel:function (panelEl, suggestions, editor){
                panelEl.innerHTML = "";
                var nodeUL = document.createElement("UL");
                suggestions.forEach(function(suggestion){
                    var nodeLI = document.createElement("LI");
                    nodeLI.setAttribute(editor.attributeValueLiName, suggestion.value)
                    nodeLI.addEventListener("click", function(e){editor.handleSelectMention(editor.getNodeLiSuggestionValue(nodeLI))})
                    var textnode = document.createTextNode(suggestion.text);
                    nodeLI.appendChild(textnode)
                    nodeUL.appendChild(nodeLI)
                })
                panelEl.appendChild(nodeUL);

            },
            getWordFromSelection: function (target, initialDiff) {
                function getWordPosition(position, diff) {
                    var n = l[position - 1];
                    return null === n || void 0 === n ? position : 0 === n.trim().length || 0 >= position || l.length < position ? position : getWordPosition(position + diff, diff)
                }

                var n = mediumEditor["default"].selection.getSelectionRange(this.document);
                //console.log(n)
                var startContainer = n.startContainer;
                var offset = n.startOffset;
                var endContainer = n.endContainer;
                //console.log(startContainer)
                //console.log(offset)
                //console.log(endContainer)
                if (startContainer === endContainer) {
                    var l = startContainer.textContent;
                    this.wordStart = getWordPosition(offset + initialDiff, -1)
                    this.wordEnd = getWordPosition(offset + initialDiff, 1) - 1
                    this.word = l.slice(this.wordStart, this.wordEnd)
                    this.trigger = this.word.slice(0, 1)
                    this.triggerClassName = this.triggerClassNameMap[this.trigger]
                    this.activeTriggerClassName = this.activeTriggerClassNameMap[this.trigger]
                    this.extraTriggerClassName = this.extraTriggerClassNameMap[this.trigger]
                    this.extraActiveTriggerClassName = this.extraActiveTriggerClassNameMap[this.trigger]
                }
            },
            showPanel: function () {
                this.isActivePanel() || (this.activatePanel(), this.wrapWordInMentionAt()), this.positionPanel(), this.updatePanelContent()
            },
            activatePanel: function () {
                this.mentionPanel.classList.add("medium-editor-mention-panel-active"), (this.extraActivePanelClassName || this.extraActiveClassName) && this.mentionPanel.classList.add(this.extraActivePanelClassName || this.extraActiveClassName)
            },
            isActivePanel: function(){
                return this.mentionPanel.classList.contains("medium-editor-mention-panel-active");
            },
            wrapWordInMentionAt: function () {
                var selection = this.document.getSelection();
                if (selection.rangeCount) {
                    var range = selection.getRangeAt(0).cloneRange();
                    if (range.startContainer.parentNode.classList.contains(this.triggerClassName))this.activeMentionAt = range.startContainer.parentNode; else {
                        var nextWordEnd = Math.min(this.wordEnd, range.startContainer.textContent.length);
                        range.setStart(range.startContainer, this.wordStart), range.setEnd(range.startContainer, nextWordEnd);
                        var element = this.document.createElement(this.tagName);
                        element.classList.add(this.triggerClassName);
                        this.extraTriggerClassName && element.classList.add(this.extraTriggerClassName);
                        this.activeMentionAt = element;
                        range.surroundContents(element);
                        selection.removeAllRanges();
                        selection.addRange(range);
                        mediumEditor["default"].selection.select(this.document, this.activeMentionAt.firstChild, this.word.length)
                    }
                    this.activeMentionAt.classList.add(this.activeTriggerClassName), this.extraActiveTriggerClassName && this.activeMentionAt.classList.add(this.extraActiveTriggerClassName)
                }
            },
            positionPanel: function () {
                var e = this.activeMentionAt.getBoundingClientRect(), t = e.bottom, i = e.left, n = e.width, a = this.window, s = a.pageXOffset, r = a.pageYOffset;
                this.mentionPanel.style.top = r + t + "px", this.mentionPanel.style.left = s + i + n + "px"
            },
            updatePanelContent: function () {
                this.renderPanelContent(this.mentionPanel, this.word, this.handleSelectMention.bind(this))
            },
            handleSelectMention: function (e) {
                if (e) {
                    var t = this.activeMentionAt.firstChild;
                    t.textContent = e, mediumEditor["default"].selection.select(this.document, t, e.length);
                    var i = this.base.getFocusedElement();
                    i && this.base.events.updateInput(i, {target: i, currentTarget: i}), this.hidePanel(!1)
                } else this.hidePanel(!1)
            }
        });
        t["default"] = h
    }, function (t, i) {
        t.exports = e
    }])
});
//# sourceMappingURL=index.min.js.map