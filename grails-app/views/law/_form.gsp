<%@ page import="kuorum.core.FileGroup; kuorum.law.Law" %>

<r:require module="fileuploader" />
<g:set var="imageId" value="imageId_XXXXX"/>
<script>
    var typeErrorText = "${g.message(code:'uploader.error.typeError')}"
    var sizeErrorText = "${g.message(code:'uploader.error.sizeError')}"
    var minSizeErrorText = "${g.message(code:'uploader.error.minSizeError')}"
    var emptyErrorText = "${g.message(code:'uploader.error.emptyError')}"
    var onLeaveText = "${g.message(code:'uploader.error.onLeave')}"
</script>
<uploader:uploader
        id="uploaderImageId"
        multiple="false"
        url="${[controller:'file', action:"uploadImage"]}"
        sizeLimit="${FileGroup.LAW_IMAGE.maxSize}"
        allowedExtensions='["\'png\'", "\'gif\'", "\'jpeg\'", "\'jpg\'"]'
        messages='{
                typeError: typeErrorText,
                sizeError: sizeErrorText,
                minSizeError: minSizeErrorText,
                emptyError: emptyErrorText,
                onLeave: onLeaveText
            }'
        params='[fileGroup:"\"${fileGroup}\""]' >
    <uploader:onSubmit>
        $("#${imageId}").attr("alt","Cargando");
            originalImgPath = $("#${imageId}").attr("src");
            $("#${imageId}").attr("src","${g.resource(dir: 'img', file: 'spinner_small.gif')}");
    </uploader:onSubmit>
    <uploader:onProgress> console.log(loaded+' of '+total+' done so far') </uploader:onProgress>
    <uploader:onComplete>
        $("#${imageId}").attr("src",responseJSON.absolutePathImg);
        console.log(responseJSON.absolutePathImg)
    %{--$("#${imageId}").attr("src",responseJSON.absolutePathImg);--}%
        %{--cropImage("${imageId}", "${fieldToUpdateId}", responseJSON.absolutePathImg, fileName);--}%
    </uploader:onComplete>
    <uploader:onCancel> alert('you cancelled the upload'); </uploader:onCancel>
</uploader:uploader>

<img src="" id="${imageId}"/>


<div class="fieldcontain ${hasErrors(bean: lawInstance, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="law.description.label" default="Description" />
		
	</label>
	<g:textField name="description" value="${lawInstance?.description}" />
</div>

<div class="fieldcontain ${hasErrors(bean: lawInstance, field: 'hashtag', 'error')} ">
	<label for="hashtag">
		<g:message code="law.hashtag.label" default="Hashtag" />
		
	</label>
	<g:textField name="hashtag" value="${lawInstance?.hashtag}" />
</div>

<div class="fieldcontain ${hasErrors(bean: lawInstance, field: 'introduction', 'error')} ">
	<label for="introduction">
		<g:message code="law.introduction.label" default="Introduction" />
		
	</label>
	<g:textField name="introduction" value="${lawInstance?.introduction}" />
</div>


<div class="fieldcontain ${hasErrors(bean: lawInstance, field: 'realName', 'error')} ">
	<label for="realName">
		<g:message code="law.realName.label" default="Real Name" />
		
	</label>
	<g:textField name="realName" value="${lawInstance?.realName}" />
</div>

<div class="fieldcontain ${hasErrors(bean: lawInstance, field: 'region', 'error')} ">
    <label for="realName">
        <g:message code="law.region.label" default="Region" />

    </label>
    <g:select name="region" from="${regions}" optionKey="id" optionValue="name"/>
</div>

<div class="fieldcontain ${hasErrors(bean: lawInstance, field: 'institution', 'error')} ">
    <label for="realName">
        <g:message code="law.institution.label" default="Institution" />

    </label>
    <g:select name="institution" from="${institutions}" optionKey="id" optionValue="name"/>
</div>

<div class="fieldcontain ${hasErrors(bean: lawInstance, field: 'shortName', 'error')} ">
	<label for="shortName">
		<g:message code="law.shortName.label" default="Short Name" />
		
	</label>
	<g:textField name="shortName" value="${lawInstance?.shortName}" />
</div>

