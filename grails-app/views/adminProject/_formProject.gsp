
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
    <formUtil:checkBox command="${command}" field="availableStats"/>
</div>

<div class="form-group">
    <div class="row">
        <div class="col-xs-6">
            <formUtil:selectDomainObject command="${command}" field="region" values="${regions}" />
        </div>
        <div class="col-xs-6">
            <formUtil:selectDomainObject command="${command}" field="institution" values="${institutions}" />
        </div>
    </div>
</div>
<div class="form-group">
    <formUtil:selectEnum command="${command}" field="status"/>
</div>

<div class="form-group">
    <formUtil:selectMultipleCommissions command="${command}" field="commissions"/>
</div>

<div class="form-group">
    <formUtil:editImage command="${command}" field="photoId" fileGroup="${kuorum.core.FileGroup.PROJECT_IMAGE}"/>
</div>

<div class="form-group">
    <formUtil:input command="${command}" field="urlPdf" required="true"/>
</div>