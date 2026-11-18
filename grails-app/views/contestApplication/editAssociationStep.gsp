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
    <g:render template="/contestApplication/editModules/editContestApplicationAssociation" model="[
            attachEvent     : false,
            campaign        : campaign,
            command         : command,
            numberRecipients: numberRecipients,
            status          : status,
            mappings        : [
                    saveAndSentButtons: false,
                    step              : 'association',
                    environment       : 'contestApplicationEditScope',
                    association       : 'contestApplicationEditAssociation',
                    content           : 'contestApplicationEditContent',
                    authorizations    : 'contestApplicationEditAuthorizations',
                    showResult        : 'contestApplicationShow',
                    next              : 'contestApplicationEditContent']]"/>
</content>
