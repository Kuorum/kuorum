<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.profilePoliticianExternalActivity"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
    <r:require modules="forms"/>
</head>

<content tag="leftMenu">
    <g:render template="/profile/leftMenu" model="[user:user, activeMapping:'profilePoliticianRelevantEvents', menu:menu]"/>

</content>

<content tag="mainContent">
    <formUtil:validateForm form="relevantEventsForm" bean="${command}"/>
    <g:form method="POST" mapping="profilePoliticianRelevantEvents" name="relevantEventsForm" role="form" class="box-ppal">
        <input type="hidden" name="politicianId" value="${command.politicianId}"/>
        <formUtil:dynamicComplexInputs
                command="${command}"
                field="politicianRelevantEvents"
                listClassName="kuorum.users.extendedPoliticianData.PoliticianRelevantEvent"
                formId="relevantEventsForm">
            <fieldset class="row">
                <div class="form-group col-md-6">
                    <formUtil:input field="title" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
                </div>
                <div class="form-group col-md-6">
                    <formUtil:url field="url" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
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
