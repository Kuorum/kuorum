<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.profilePoliticianExternalActivity"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
    <r:require modules="forms"/>
</head>

<content tag="leftMenu">
    <g:render template="/profile/leftMenu" model="[user:user, activeMapping:'profileChangeEmail', menu:menu]"/>

</content>

<content tag="mainContent">
    <formUtil:validateForm form="knownForForm" bean="${command}"/>
    <g:form method="POST" mapping="profilePoliticianExternalActivity" name="knownForForm" role="form" class="box-ppal">
        <input type="hidden" name="politicianId" value="${command.politicianId}"/>
        <formUtil:dynamicComplexInputs
            command="${command}"
            field="externalPoliticianActivities"
            listClassName="kuorum.users.extendedPoliticianData.ExternalPoliticianActivity"
            formId="knownForForm">
            <fieldset class="row">
                <div class="form-group col-md-12">
                    <formUtil:input field="title" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
                </div>
                <div class="form-group col-md-4">
                    <formUtil:date field="date" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
                </div>
                <div class="form-group col-md-8">
                    <formUtil:url field="link" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
                </div>
            </fieldset>
            <fieldset class="row">
                <div class="form-group col-md-6">
                    <formUtil:input field="actionType" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
                </div>
                <div class="form-group col-md-6">
                    <formUtil:input field="outcomeType" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
                </div>
            </fieldset>
            <fieldset class="row">
                <div class="col-md-12 text-right">
                    <button type="button" class="btn btn-default removeButton"><i class="fa fa-minus"></i></button>
                </div>
            </fieldset>
        </formUtil:dynamicComplexInputs>


        <fieldset>
            <div class="form-group">
                <div class="col-xs-5 col-xs-offset-1">
                    <button type="submit" class="btn btn-default">Submit</button>
                </div>
            </div>
        </fieldset>
    </g:form>
</content>
