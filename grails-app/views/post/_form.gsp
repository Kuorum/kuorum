<%@ page import="kuorum.core.FileGroup" %>
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
                    <option value="${kuorum.core.model.PostType.HISTORY}" ${command.postType == kuorum.core.model.PostType.HISTORY?'selected':''}>&#xf02d;  <g:message code="kuorum.core.model.PostType.HISTORY"/> </option> <!-- debe venir con la primera opción que sea la que el usuario ha seleccionado en el paso anterior -->
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
            <span class="hashtag">${law.hashtag}</span>
        </div>
        <div id="charInit" class="hidden"><g:message code="post.edit.step1.postTitle.chars.limitCharacters"/> <span>${formUtil.postTitleLimitChars(law:law)}</span></div>
        <div id="charNum"><g:message code="post.edit.step1.postTitle.chars.leftCharacters"/> <span></span> <g:message code="post.edit.step1.postTitle.chars.characters"/></div>
    </div>
</fieldset>
<fieldset class="text">
    <div class="form-group">
        <label for="textPost"><g:message code="post.edit.step1.postText.label"/> </label>
        <textarea name="textPost" data-placement="bottom" class="form-control texteditor ${hasErrors(bean: command, field: 'textPost', 'error')}" rows="10" placeholder="${g.message(code: 'post.edit.step1.postText.placeHolder')}" id="textPost" tabindex="14" required>${command.textPost}</textarea>
        <g:if test="${hasErrors(bean: command, field: 'textPost', 'error')}">
            <span for="textPost" class="error">${g.fieldError(bean: command, field: 'textPost')}</span>
        </g:if>
    </div>
</fieldset>
<fieldset class="multimedia">
    <div class="form-group image">
        <label for="imageId"><g:message code="post.edit.step1.image.label"/></label>
        <formUtil:editImage command="${command}" field="imageId" fileGroup="${ FileGroup.POST_IMAGE}"/>
    </div>
    <div class="form-group video">
        <label for="videoPost"><g:message code="post.edit.step1.video.label"/></label>
        <input name="videoPost" type="url" value="${command.videoPost}" class="form-control ${hasErrors(bean: command, field: 'videoPost', 'error')}" id="videoPost" placeholder="http://" tabindex="16">
        <g:if test="${hasErrors(bean: command, field: 'videoPost', 'error')}">
            <span for="textPost" class="error">${g.fieldError(bean: command, field: 'videoPost')}</span>
        </g:if>
    </div>
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