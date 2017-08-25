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

        function deleteLastCharacter(text) {
            return text[text.length - 1]
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
            renderPanelContent: function (panelEl, suggestions) {
                //this.getSuggestions(this.word, panelEl, this.buildPanel)
                this.buildPanel(panelEl, suggestions, this)
            },
            destroyPanelContent: function () {
            },
            activeTriggerList: ["@"],
            triggerClassNameMap: {"#": "medium-editor-mention-hash", "@": "medium-editor-mention-at"},
            activeTriggerClassNameMap: {"#": "medium-editor-mention-hash-active", "@": "medium-editor-mention-at-active"},
            hideOnBlurDelay: 300,
            attributeJsonDataLiNode:"data-jsonData",
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
                if ( -1 !== this.activeTriggerList.indexOf(this.trigger) && this.word.length > 1) {
                    //var ulNode = document.getElementsByClassName("medium-editor-mention-panel-active")[0].firstChild;
                    var ulNode = this.mentionPanel.firstElementChild
                    if (this.isActivePanel() && MediumEditor.util.isKey(e, MediumEditor.util.keyCode.UP)) {
                        var selectedLi = ulNode.querySelector("li." + classSelected);
                        if (!selectedLi) {
                            ulNode.lastChild.classList.add(classSelected);
                        } else {
                            selectedLi.classList.remove(classSelected)
                            selectedLi = selectedLi.previousElementSibling;
                            if (!selectedLi) {
                                selectedLi = ulNode.lastChild;
                            }
                            selectedLi.classList.add(classSelected);
                        }
                    } else if (this.isActivePanel() && MediumEditor.util.isKey(e, MediumEditor.util.keyCode.DOWN)) {
                        var selectedLi = ulNode.querySelector("li." + classSelected);
                        if (!selectedLi) {
                            ulNode.firstChild.classList.add(classSelected);
                        } else {
                            selectedLi.classList.remove(classSelected)
                            selectedLi = selectedLi.nextElementSibling;
                            if (!selectedLi) {
                                selectedLi = ulNode.firstChild;
                            }
                            selectedLi.classList.add(classSelected);
                        }
                    } else if (this.isActivePanel() && MediumEditor.util.isKey(e, MediumEditor.util.keyCode.LEFT)) {
                        //console.log("left")
                    } else if (this.isActivePanel() && MediumEditor.util.isKey(e, [MediumEditor.util.keyCode.RIGHT, MediumEditor.util.keyCode.ENTER])) {
                        var selectedLi = ulNode.querySelector("li." + classSelected);
                        if (selectedLi) {
                            this.handleSelectMention(this.getNodeJsonData(selectedLi))
                        }
                    } else {
                        this.showPanel();
                    }
                }else{
                    this.hidePanel(MediumEditor.util.isKey(event, [MediumEditor.util.keyCode.LEFT]));
                }
            },
            getNodeJsonData:function(nodeLI){
                return JSON.parse(nodeLI.getAttribute(this.attributeJsonDataLiNode));
            },
            hidePanel: function (isArrowTowardsLeft) {
                this.mentionPanel.classList.remove("medium-editor-mention-panel-active");
                var extraActivePanelClassName = this.extraActivePanelClassName || this.extraActiveClassName;
                if (extraActivePanelClassName && this.mentionPanel.classList.remove(extraActivePanelClassName), this.activeMentionAt && (this.activeMentionAt.classList.remove(this.activeTriggerClassName), this.extraActiveTriggerClassName && this.activeMentionAt.classList.remove(this.extraActiveTriggerClassName)), this.activeMentionAt) {
                    var i = this.activeMentionAt;
                    var parentNode = i.parentNode;
                    var previousSibling = i.previousSibling;
                    var nextSibling = i.nextSibling;
                    var firstChild = i.firstChild;
                    var siblingNode = isArrowTowardsLeft ? previousSibling : nextSibling;
                    var c = void 0;
                    siblingNode ? 3 !== siblingNode.nodeType ? (c = this.document.createTextNode(""), parentNode.insertBefore(c, siblingNode)) : c = siblingNode : (c = this.document.createTextNode(""), parentNode.appendChild(c));
                    var u = deleteLastCharacter(firstChild.textContent);
                    var m = 0 === u.trim().length;
                    if (m) {
                        var g = firstChild.textContent;
                        firstChild.textContent = g.substr(0, g.length - 1), c.textContent = "" + u + c.textContent
                    } else 0 === c.textContent.length && firstChild.textContent.length > 1 && (c.textContent = " ");
                    isArrowTowardsLeft ? mediumEditor["default"].selection.select(this.document, c, c.length) : mediumEditor["default"].selection.select(this.document, c, Math.min(c.length, 1)), firstChild.textContent.length <= 1 && (this.base.saveSelection(), s(this.activeMentionAt, this.document), this.base.restoreSelection()), this.activeMentionAt = null
                }
            },
            getSuggestions:function(prefix, callback){
                var suggestions = [
                    {alias:prefix+"1", name:prefix.slice(1)+'(1)',link:"http://mydomain.com/"+prefix.slice(1)+"1", avatar:"https://kuorumorg.s3.amazonaws.com/UsersFiles/56379cb8e4b0068dceee05a1.jpg"},
                    {alias:prefix+"2", name:prefix.slice(1)+'(2)',link:"http://mydomain.com/"+prefix.slice(1)+"2", avatar:"https://kuorumorg.s3.amazonaws.com/UsersFiles/56379cb8e4b0068dceee05a1.jpg"}
                ];
                if (prefix.length >6){
                    suggestions=[];
                }
                callback(suggestions)
            },
            buildPanel:function (panelEl, suggestions, editor){
                panelEl.innerHTML = "";
                var nodeUL = document.createElement("UL");
                suggestions.forEach(function(suggestion){
                    // STRUCTURE
                    var divWrapper=document.createElement("DIV");
                    divWrapper.className="medium-suggestion-wrapper";
                    var avatarImg = document.createElement("img");
                    avatarImg.setAttribute("src",suggestion.avatar);
                    avatarImg.className="medium-suggestion-img";
                    divWrapper.appendChild(avatarImg);
                    var divWrapperNames=document.createElement("div");
                    divWrapperNames.className="medium-suggestion-wrapper-names";
                    var name = document.createElement("span");
                    name.className="medium-suggestion-name";
                    var nameNode = document.createTextNode(suggestion.name);
                    name.appendChild(nameNode);
                    var alias = document.createElement("span");
                    alias.className="medium-suggestion-alias";
                    var aliasNode = document.createTextNode(suggestion.alias);
                    alias.appendChild(aliasNode);
                    divWrapperNames.appendChild(name);
                    divWrapperNames.appendChild(alias);
                    divWrapper.appendChild(divWrapperNames);
                    // LI NODE
                    var nodeLI = document.createElement("LI");
                    nodeLI.setAttribute(editor.attributeJsonDataLiNode, JSON.stringify(suggestion))
                    nodeLI.addEventListener("click", function(e){editor.handleSelectMention(editor.getNodeJsonData(nodeLI))})


                    nodeLI.appendChild(divWrapper)
                    nodeUL.appendChild(nodeLI)
                })
                panelEl.appendChild(nodeUL);

            },
            getWordFromSelection: function (target, initialDiff) {
                function getWordPosition(position, diff, activeTriggers) {
                    var prevText = textContent[position - 1];
                    if (null === prevText || void 0 === prevText){
                        return position;
                    }
                    //if (0 === prevText.trim().length || 0 >= position || textContent.length < position){
                    if (activeTriggers.indexOf(prevText)>=0 || 0 >= position || textContent.length < position){
                        return position -1;
                    }else{
                        return getWordPosition(position + diff, diff,activeTriggers);
                    }
                }

                var range = mediumEditor["default"].selection.getSelectionRange(this.document);
                var startContainer = range.startContainer;
                var offset = range.startOffset;
                var endContainer = range.endContainer;
                //console.log(startContainer)
                //console.log(offset)
                //console.log(endContainer)
                if (startContainer === endContainer) {
                    var textContent = startContainer.textContent;
                    this.wordStart = getWordPosition(offset + initialDiff, -1,this.activeTriggerList)
                    this.wordEnd = getWordPosition(offset + initialDiff, 1,this.activeTriggerList) - 1
                    this.word = textContent.slice(this.wordStart, this.wordEnd)
                    this.trigger = this.word.slice(0, 1)
                    this.triggerClassName = this.triggerClassNameMap[this.trigger]
                    this.activeTriggerClassName = this.activeTriggerClassNameMap[this.trigger]
                    this.extraTriggerClassName = this.extraTriggerClassNameMap[this.trigger]
                    this.extraActiveTriggerClassName = this.extraActiveTriggerClassNameMap[this.trigger]
                }
            },
            showPanel: function () {

                var editor = this;
                this.getSuggestions(this.word, function(suggestions){
                    if (suggestions.length>0){
                        if(!editor.isActivePanel()){
                            editor.activatePanel();
                            editor.wrapWordInMentionAt()
                        }
                        editor.positionPanel();
                        editor.updatePanelContent(suggestions)
                    }else{
                        editor.hidePanel();
                    }
                });
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
                    console.log(range)
                    if (range.startContainer.parentNode.classList.contains(this.triggerClassName))this.activeMentionAt = range.startContainer.parentNode; else {
                        var nextWordEnd = Math.min(this.wordEnd, range.startContainer.textContent.length);
                        range.setStart(range.startContainer, this.wordStart);
                        range.setEnd(range.startContainer, nextWordEnd);
                        var element = this.buildMentionElement();
                        range.surroundContents(element);
                        selection.removeAllRanges();
                        mediumEditor["default"].selection.select(this.document, this.activeMentionAt.firstChild, this.word.length)
                    }
                    this.activeMentionAt.classList.add(this.activeTriggerClassName), this.extraActiveTriggerClassName && this.activeMentionAt.classList.add(this.extraActiveTriggerClassName)
                }
            },
            buildMentionElement: function(){
                var element = this.document.createElement(this.tagName);
                element.classList.add(this.triggerClassName);
                element.classList.add("mention-no-valid");
                this.extraTriggerClassName && element.classList.add(this.extraTriggerClassName);
                this.activeMentionAt = element;
                return element;
            },
            positionPanel: function () {
                var e = this.activeMentionAt.getBoundingClientRect(), t = e.bottom, i = e.left, n = e.width, a = this.window, s = a.pageXOffset, r = a.pageYOffset;
                this.mentionPanel.style.top = r + t + "px", this.mentionPanel.style.left = s + i + n + "px"
            },
            updatePanelContent: function (suggestions) {
                this.renderPanelContent(this.mentionPanel, suggestions)
            },
            handleSelectMention: function (nodeData) {
                if (nodeData.link) {
                    var textNode = this.activeMentionAt.firstChild;
                    this.addNodeAttributes(this.activeMentionAt,nodeData);
                    textNode.textContent = nodeData.name;
                    this.activeMentionAt.setAttribute("href", nodeData.link)
                    this.activeMentionAt.setAttribute("data-alias", nodeData.alias)
                    this.activeMentionAt.className=this.activeMentionAt.className.replace(/mention-no-valid/,'')
                    mediumEditor["default"].selection.select(this.document, textNode, nodeData.name.length);
                    var target = this.base.getFocusedElement();
                    target && this.base.events.updateInput(target, {target: target, currentTarget: target});
                }
                this.hidePanel(false)
            },
            addNodeAttributes: function(textNode, nodeData){

            }
        });
        t["default"] = h
    }, function (t, i) {
        t.exports = e
    }])
});
//# sourceMappingURL=index.min.js.map