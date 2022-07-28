<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">${campaign.name}</g:set>

    <title>${breadCrumbName}</title>
    <meta name="layout" content="basicPlainLayout">
    <!-- Schema.org markup for Google+ -->
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianCampaigns"><g:message
                code="head.logged.account.tools.massMailing"/></g:link></li>
        <li><g:link mapping="politicianCampaignsNew"><g:message code="tools.campaign.new.title"/></g:link></li>
        <li class="active">${breadCrumbName}</li>
    </ol>
    <g:render template="/contestApplication/editModules/editContestApplicationAuthorizations" model="[
            attachEvent     : false,
            command         : command,
            numberRecipients: numberRecipients,
            status          : status,
            mappings        : [
                    saveAndSentButtons: true,
                    hideScheduler     : true,
                    step              : 'authorizations',
//                    settings          : 'contestApplicationEditSettings',
                    content           : 'contestApplicationEditContent',
                    environment       : 'contestApplicationEditScope',
                    authorizations    : 'contestApplicationEditAuthorizations',
                    showResult        : 'contestApplicationShow',
                    next              : 'contestApplicationEditContent']]"/>
</content>