<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.editProject.title" args="[project.hashtag]"/> </title>
    <meta name="layout" content="columnCLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="edit-project"/>
    <g:form url="[mapping:'projectEdit', params:project.encodeAsLinkProperties()]" method="POST" name="edit-project" role="form" class="box-ppal">
        <h1><g:message code='admin.editProject.region.label' args="[project.hashtag, region.name]" encodeAs="raw"/><span class="hashtag pull-right">#</span></h1>
        <g:render template="/project/formProject" model="[command:command, editableHashtag :true]"/>
        <fieldset class="btns text-right">
            <div class="form-group">
                <g:if test="${!project.published}">
                    <a href="#" class="btn btn-grey cancel saveDraft">${message(code:'admin.createProject.saveDraft')}</a>
                </g:if>
                <input type="submit" class="btn btn-lg" value="${message(code:'admin.createProject.publish')}" />
            </div>
        </fieldset>

    </g:form>
</content>

<!-- ********************************************************************************************************* -->
<!-- ********** ASIDE: COLUMNA LATERAL CON INFORMACIÓN RELACIONADA CON LA PRINCIPAL ************************** -->

<content tag="cColumn">

    <g:render template="formProjectColumnC" model="[region:region]"/>

</content>

<!-- ********************************************************************************************************* -->
