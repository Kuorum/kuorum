<%@ page import="kuorum.core.FileType; kuorum.core.FileGroup" %>
<input type="hidden" name="isDraft" value="false"/>
<h1><g:message code="post.edit.step1.intro.head" args="[project.hashtag, project.region.name]" encodeAs="raw"/> <span class="fa fa-lightbulb-o fa-lg pull-right"></span></h1>
<fieldset class="title">
    <div class="form-group">
        <label for="titlePost" class="sr-only"><g:message code="post.edit.step1.postTitle.label"/> </label>
        <div class="textareaContainer">
            <textarea name="title"  class="form-control counted  ${hasErrors(bean: command, field: 'title', 'error')}" rows="3" placeholder="${g.message(code:'post.edit.step1.postTitle.placeholder')}" id="titlePost" tabindex="13" required minlength="2">${command.title}</textarea>
            <g:if test="${hasErrors(bean: command, field: 'title', 'error')}">
                <span for="titlePost" class="error">${g.fieldError(bean: command, field: 'title')}</span>
            </g:if>
        </div>
        <div id="charInit" class="hidden"><g:message code="post.edit.step1.postTitle.chars.limitCharacters"/> <span>${formUtil.postTitleLimitChars(project:project)}</span></div>
        <div id="charNum"><g:message code="post.edit.step1.postTitle.chars.leftCharacters"/> <span></span> <g:message code="post.edit.step1.postTitle.chars.characters"/></div>
    </div>
</fieldset>
<fieldset class="text">
    <div class="form-group">
        <label for="textPropuesta" class="sr-only"><g:message code="post.edit.step1.postText.label"/></label>
        <div class="textareaContainer">
            <textarea
                    name="textPost"
                    class="form-control counted texteditor ${hasErrors(bean: command, field: 'textPost', 'error')}"
                    rows="8"
                    placeholder="Explica tu propuesta"
                    id="textPropuesta"
                    required aria-required="true">${command.textPost}</textarea>
            <g:if test="${hasErrors(bean: command, field: 'textPost', 'error')}">
                <span for="textPost" class="error">${g.fieldError(bean: command, field: 'textPost')}</span>
            </g:if>
        </div>
        %{--<div id="charInitTextProj" class="hidden">Tienes un límite de caracteres de <span>5000</span></div>--}%
        %{--<div id="charNumTextProj" class="charNum">Quedan <span>5000</span> caracteres</div>--}%
    </div>
</fieldset>

<fieldset class="multimedia">
    <span class="span-label sr-only"><g:message code="post.edit.step1.multimedia.label"/></span>
    <g:hiddenField name="fileType" value="${command.fileType}"/>
    <script>
    $(function(){
        $('.multimedia ul.nav a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            var activatedTab = $(e.target)
            var previousTab = $(e.relatedTarget)
            $(".multimedia input[name=fileType]").val(activatedTab.attr("data-fileType"))
        })
    });
    </script>
    <ul class="nav nav-pills nav-justified">
        <li class="${!command.fileType?'active':''}">
            <a href="#postMultimediaNone" data-toggle="tab" data-fileType=""><g:message code="post.edit.step1.none.label"/></a>
        </li>
        <li class="${command.fileType == FileType.IMAGE?'active':''}">
            <a href="#postUploadImage" data-toggle="tab" data-fileType="${kuorum.core.FileType.IMAGE}"><g:message code="post.edit.step1.image.label"/></a>
        </li>
        <li class="${command.fileType == FileType.YOUTUBE?'active':''}">
            <a href="#postUploadYoutube" data-toggle="tab" data-fileType="${kuorum.core.FileType.YOUTUBE}"><g:message code="post.edit.step1.video.label"/></a>
        </li>
    </ul>
    <div class="tab-content">
        <div class="tab-pane ${!command.fileType?'in active':''}" id="postMultimediaNone">
            <g:message code="post.edit.step1.none.description"/>
        </div>
        <div class="tab-pane fade ${command.fileType == FileType.IMAGE?'in active':''}" id="postUploadImage">
            <div class="form-group image" data-multimedia-switch="on" data-multimedia-type="${kuorum.core.FileType.IMAGE}">
                <formUtil:editImage command="${command}" field="imageId" fileGroup="${ FileGroup.POST_IMAGE}" labelCssClass="sr-only"/>
            </div>
        </div>
        <div class="tab-pane fade ${command.fileType == FileType.YOUTUBE?'in active':''}" id="postUploadYoutube">
            <div class="form-group video" data-multimedia-switch="on" data-multimedia-type="${kuorum.core.FileType.YOUTUBE}">
                <label for="videoPost" class="sr-only"><g:message code="post.edit.step1.video.label"/></label>
                <input name="videoPost" type="url" value="${command.videoPost}" class="form-control ${hasErrors(bean: command, field: 'videoPost', 'error')}" id="videoPost" placeholder="http://" tabindex="16">
                <g:if test="${hasErrors(bean: command, field: 'videoPost', 'error')}">
                    <span for="textPost" class="error">${g.fieldError(bean: command, field: 'videoPost')}</span>
                </g:if>
            </div>
        </div>
    </div>

</fieldset>
<fieldset class="btns text-right">
    <div class="form-group">
        <a href="#" class="cancel saveDraft"><g:message code="post.edit.step1.saveDraft"/> </a>
        <input type="submit" class="btn btn-lg" value="${message(code:'post.edit.step1.save')}">
    </div>
</fieldset>