<input type="hidden" name="isDraft" value="false"/>
<fieldset class="title">
    <div class="form-group">
        <formUtil:textArea command="${command}" field="shortName" required="true"/>
    </div>
</fieldset>

<fieldset class="hashtag-time">
    <div class="row form-group">
        <div class="col-xs-12 col-sm-8">
            <formUtil:input command="${command}" field="hashtag" required="true" maxlength="17" disabled="${editableHashtag}"/>
        </div>
        <div class="col-xs-12 col-sm-4">
            <formUtil:date command="${command}" field="deadline" required="true"/>
        </div>
    </div>
</fieldset>

<fieldset class="text">
    <div class="form-group">
        <formUtil:textArea command="${command}" field="description" required="true" texteditor="texteditor"/>
    </div>
</fieldset>

<fieldset class="multimedia">
    <span class="span-label sr-only"><g:message code="admin.createProject.upload.imageOrVideo" /></span>
    <input type="hidden" name="fileType" value="" id="fileType">
    <ul class="nav nav-pills nav-justified">
        <li class="${command.photoId || (command.errors?.getFieldError('photoId')?.codes?.contains('imageOrUrlYoutubeRequired') && command.errors?.getFieldError('videoPost')?.codes?.contains('imageOrUrlYoutubeRequired'))?'active':''}">
            <a href="#projectUploadImage" data-toggle="tab" data-filetype="IMAGE"><g:message code="admin.createProject.upload.image" /></a>
        </li>
        <li class="${command.videoPost?'active':''}">
            <a href="#projectUploadYoutube" data-toggle="tab" data-filetype="YOUTUBE"><g:message code="admin.createProject.upload.video" /></a>
        </li>
    </ul>
    <div class="tab-content">
        <div class="tab-pane fade ${command.photoId || (command.errors?.getFieldError('photoId')?.codes?.contains('imageOrUrlYoutubeRequired') && command.errors?.getFieldError('videoPost')?.codes?.contains('imageOrUrlYoutubeRequired'))?'in active':''}" id="projectUploadImage">
            <div class="form-group image" data-multimedia-switch="on" data-multimedia-type="IMAGE">
                <formUtil:editImage command="${command}" field="photoId" fileGroup="${kuorum.core.FileGroup.PROJECT_IMAGE}"/>
            </div>
        </div>

        <div class="tab-pane fade ${command.videoPost?'in active':''}" id="projectUploadYoutube">
            <div class="form-group video" data-multimedia-switch="on" data-multimedia-type="YOUTUBE">
                <formUtil:url command="${command}" field="videoPost" required="true"/>
            </div>
        </div>
    </div>
</fieldset>

<div class="form-group interest">
    <formUtil:selectMultipleCommissions command="${command}" field="commissions"/>
</div>

<fieldset class="uploadPdf">
    <formUtil:editPdf command="${command}" field="pdfFileId" fileGroup="${kuorum.core.FileGroup.PDF}"/>
</fieldset>