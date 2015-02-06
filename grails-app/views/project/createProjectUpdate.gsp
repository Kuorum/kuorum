<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="projectUpdate.title" args="[project.hashtag]"/> </title>
    <meta name="layout" content="columnCLayout">
    <parameter name="extraCssContainer" value="config" />
</head>
<content tag="mainContent">
    <formUtil:validateForm bean="${projectUpdateCommand}" form="updateProject"/>
    <g:form method="POST" mapping="projectUpdate" params="${project.encodeAsLinkProperties()}" role="form" name="updateProject" id="updateProject" class="box-ppal" >
        <h1><g:message code="projectUpdate.form.label" args="[project.hashtag, project.region]"/>  <span class="icon2-update pull-right"></span></h1>
        <fieldset class="title">
            <div class="form-group">
                <formUtil:textArea command="${projectUpdateCommand}" field="description" required="true"/>
            </div>
        </fieldset>


        <fieldset class="multimedia">
            <span class="span-label sr-only"><g:message code="projectUpdate.form.upload.imageOrVideoOrNothing" /></span>
            <input type="hidden" name="fileType" value="" id="fileType">
            <ul class="nav nav-pills nav-justified">
                <li class="active">
                    <a href="#projectMultimediaNone" data-toggle="tab" data-filetype=""><g:message code="projectUpdate.form.upload.nothing" /></a>
                </li>
                <li>
                    <a href="#projectUploadImage" data-toggle="tab" data-filetype="IMAGE"><g:message code="projectUpdate.form.upload.image" /></a>
                </li>
                <li>
                    <a href="#projectUploadYoutube" data-toggle="tab" data-filetype="YOUTUBE"><g:message code="projectUpdate.form.upload.video" /></a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane in active" id="projectMultimediaNone">
                </div>

                <div class="tab-pane fade" id="projectUploadImage">
                    <div class="form-group image" data-multimedia-switch="on" data-multimedia-type="IMAGE">
                        <formUtil:editImage command="${projectUpdateCommand}" field="photoId" fileGroup="${kuorum.core.FileGroup.PROJECT_IMAGE}"/>
                    </div>
                </div>

                <div class="tab-pane fade" id="projectUploadYoutube">
                    <div class="form-group video" data-multimedia-switch="on" data-multimedia-type="YOUTUBE">
                        <formUtil:url command="${projectUpdateCommand}" field="urlYoutubeId" required="true"/>
                    </div>
                </div>

            </div>
        </fieldset>
        <fieldset class="btns text-right">
            <div class="form-group">
                <a href="#" class="cancel" tabindex="24"><g:message code="projectUpdate.form.saveDraft" /></a>
                <input type="submit" class="btn btn-lg" value="${message(code:'projectUpdate.form.publish')}">
            </div>
        </fieldset>
    </g:form>
</content>

<!-- ********************************************************************************************************* -->
<!-- ********** ASIDE: COLUMNA LATERAL CON INFORMACIÓN RELACIONADA CON LA PRINCIPAL ************************** -->
<content tag="cColumn">
    <section class="boxes performance">
        <h1 class="text-center"><g:message code="projectUpdate.aside.improve.label" /></h1>
        <p class="text-center"><g:message code="projectUpdate.aside.improve.text" /></p>
        <div class="video">
            <a href="#" class="front">
                <span class="fa fa-play-circle fa-4x"></span>
                <img src="http://img.youtube.com/vi/fQDQO4VRpF8/hqdefault.jpg">
            </a>
            <iframe class="youtube" itemprop="video" height="360" src="//www.youtube.com/embed/fQDQO4VRpF8?fs=1&rel=0&showinfo=0&showsearch=0&autoplay=1" frameborder="0" allowfullscreen></iframe>
        </div>
        <p class="text-center"><g:message code="projectUpdate.aside.improve.resume" /></p>
        <a href="#" class="btn btn-blue btn-lg improve"><g:message code="projectUpdate.aside.improve.button" /></a>
    </section>
</content>
<!-- ********************************************************************************************************* -->

