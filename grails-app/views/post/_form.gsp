<%@ page import="kuorum.core.FileType; kuorum.core.FileGroup" %>
<fieldset class="type">
    <div class="form-group">
        <label for="selectType"><g:message code="post.edit.step1.postType.label"/></label>
        <div class="row">

            <!-- la siguiente lista está oculta; debe venir marcado con class="active" el <li> que corresponda pues ese aparecerá visible -->
            <ul id="typePubli" class="hidden">
                <li class="${command.postType == kuorum.core.model.PostType.HISTORY?'active':''}"><g:message code="post.edit.step1.postType.HISTORY.description"/></li>
                <li class="${command.postType == kuorum.core.model.PostType.QUESTION?'active':''}"><g:message code="post.edit.step1.postType.QUESTION.description"/></li>
                <li class="${command.postType == kuorum.core.model.PostType.PURPOSE?'active':''}"><g:message code="post.edit.step1.postType.PURPOSE.description"/></li>
            </ul>
            <p class="col-md-7" id="updateText"></p> <!-- aquí hago visible por js el texto que corresponde a la opción elegida por el usuario -->
            <div class="col-md-5">
                <select class="form-control" id="selectType" name="postType">
                    <option value="${kuorum.core.model.PostType.HISTORY}" ${command.postType == kuorum.core.model.PostType.HISTORY?'selected':''}>&#xf075;  <g:message code="kuorum.core.model.PostType.HISTORY"/> </option> <!-- debe venir con la primera opción que sea la que el usuario ha seleccionado en el paso anterior -->
                    <option value="${kuorum.core.model.PostType.QUESTION}" ${command.postType == kuorum.core.model.PostType.QUESTION?'selected':''}>&#xf059; <g:message code="kuorum.core.model.PostType.QUESTION"/></option>
                    <option value="${kuorum.core.model.PostType.PURPOSE}" ${command.postType == kuorum.core.model.PostType.PURPOSE?'selected':''}>&#xf0eb;  <g:message code="kuorum.core.model.PostType.PURPOSE"/></option>
                </select>
            </div>
        </div>
    </div><!-- /.form-group -->
</fieldset>
<fieldset class="title">
    <div class="form-group">
        <label for="titlePost"><g:message code="post.edit.step1.postTitle.label"/> </label>
        <div class="textareaContainer">
            <textarea name="title"  class="form-control counted  ${hasErrors(bean: command, field: 'title', 'error')}" rows="3" placeholder="${g.message(code:'post.edit.step1.postTitle.placeholder')}" id="titlePost" tabindex="13" required minlength="2">${command.title}</textarea>
            <g:if test="${hasErrors(bean: command, field: 'title', 'error')}">
                <span for="titlePost" class="error">${g.fieldError(bean: command, field: 'title')}</span>
            </g:if>
            <span class="hashtag">${project.hashtag}</span>
        </div>
        <div id="charInit" class="hidden"><g:message code="post.edit.step1.postTitle.chars.limitCharacters"/> <span>${formUtil.postTitleLimitChars(project:project)}</span></div>
        <div id="charNum"><g:message code="post.edit.step1.postTitle.chars.leftCharacters"/> <span></span> <g:message code="post.edit.step1.postTitle.chars.characters"/></div>
    </div>
</fieldset>
<fieldset class="text">
    <div class="form-group">
        <label for="textPost"><g:message code="post.edit.step1.postText.label"/> </label>
        %{--<textarea name="textPost" data-placement="bottom" class="form-control texteditor ${hasErrors(bean: command, field: 'textPost', 'error')}" rows="10" placeholder="${g.message(code: 'post.edit.step1.postText.placeHolder')}" id="textPost" tabindex="14" required>${command.textPost}</textarea>--}%
        <textarea name="textPost" data-placement="bottom" class="form-control texteditor ${hasErrors(bean: command, field: 'textPost', 'error')}" rows="10" id="textPost" tabindex="14" required>${command.textPost}</textarea>
        <g:if test="${hasErrors(bean: command, field: 'textPost', 'error')}">
            <span for="textPost" class="error">${g.fieldError(bean: command, field: 'textPost')}</span>
        </g:if>
    </div>
</fieldset>

<fieldset class="multimedia">
    <span class="span-label">Añade una imagen o un vídeo a tu publicación </span>
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
    %{--<div class="form-group groupRadio">--}%
        %{--<formUtil:radioEnum command="${command}" field="fileType"/>--}%
        %{--<script>--}%
            %{--$(function(){--}%
                %{--$('[data-multimedia-switch="on"]').hide()--}%
                %{--var multimediaType = "${command.fileType}";--}%
                %{--$('[data-multimedia-type="'+multimediaType+'"]').show()--}%
            %{--})--}%
        %{--</script>--}%

    %{--</div>--}%


</fieldset>
<fieldset class="page">
    <div class="form-group">
        <label for="numberPage"><g:message code="post.edit.step1.pdfPage.label"/> </label>
        <div class="form-group">
            <input name="numberPage" value="${command.numberPage}" type="number" id="numberPage" placeholder="0" tabindex="17" class="${hasErrors(bean: command, field: 'numberPage', 'error')}">
            <g:if test="${hasErrors(bean: command, field: 'numberPage', 'error')}">
                <span for="numberPage" class="error">${g.fieldError(bean: command, field: 'numberPage')}</span>
            </g:if>
            <p><g:message code="post.edit.step1.pdfPage.description"/></p>
        </div>
    </div>
</fieldset>