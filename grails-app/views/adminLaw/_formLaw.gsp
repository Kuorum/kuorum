
<div class="form-group">
    <formUtil:input command="${command}" field="shortName" required="true"/>
</div>

<div class="form-group">
    <formUtil:input command="${command}" field="realName" required="true"/>
</div>

<div class="form-group">
    <formUtil:textArea command="${command}" field="introduction" required="true"/>
</div>

<div class="form-group">
    <formUtil:textArea command="${command}" field="description" required="true"/>
</div>

<div class="form-group">
    <formUtil:selectDomainObject command="${command}" field="institution" values="${institutions}" />
</div>

<div class="form-group">
    <formUtil:editImage command="${command}" field="photoId" fileGroup="${kuorum.core.FileGroup.LAW_IMAGE}"/>
</div>