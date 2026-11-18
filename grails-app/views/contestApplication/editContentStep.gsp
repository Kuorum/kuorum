<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">${campaign.name}</g:set>

    <title>${breadCrumbName}</title>
    <meta name="layout" content="basicPlainLayout">

</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianCampaigns"><g:message
                code="head.logged.account.tools.massMailing"/></g:link></li>
        <li><g:link mapping="politicianCampaignsNew"><g:message code="tools.campaign.new.title"/></g:link></li>
        <li class="active">${breadCrumbName}</li>
    </ol>
    <%-- A persisted application type wins over the domain flag (null -> the tag falls back to the domain flag) --%>
    <contestApplication:hasCollaborator var="hasCollaborator" hasCollaborator="${campaign.contestApplicationType ? campaign.contestApplicationType.name() == 'COLLABORATOR' : null}"/>
    <g:render template="/campaigns/edit/contentStep" model="[
            attachEvent     : false,
            command         : command,
            numberRecipients: numberRecipients,
            status          : status,
            mappings        : [
                    saveAndSentButtons: false,
                    hideScheduler     : true,
                    step              : 'content',
//                    settings          : 'contestApplicationEditSettings',
                    content           : 'contestApplicationEditContent',
                    environment       : 'contestApplicationEditScope',
                    association       : hasCollaborator ? 'contestApplicationEditAssociation' : null,
                    showResult        : 'contestApplicationShow',
                    authorizations    : 'contestApplicationEditAuthorizations',
                    next              : 'contestApplicationEditAuthorizations']]"/>
</content>