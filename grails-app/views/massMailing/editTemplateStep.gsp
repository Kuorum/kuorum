<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">${campaign.name}
        %{--<g:if test="${command?.subject}">--}%
            %{--<g:message code="head.logged.account.tools.massMailing.edit" args="[command.subject]"/>--}%
        %{--</g:if>--}%
        %{--<g:else>--}%
            %{--<g:message code="head.logged.account.tools.massMailing.new"/>--}%
        %{--</g:else>--}%
    </g:set>

    <title>${breadCrumbName}</title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />

</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianCampaigns"><g:message code="head.logged.account.tools.massMailing"/></g:link></li>
        <li><g:link mapping="politicianCampaignsNew"><g:message code="tools.campaign.new.title"/></g:link></li>
        <li class="active">${breadCrumbName}</li>
    </ol>
    <g:render template="types/templateStep" model="[command: command, campaign:campaign]"/>

    <g:render template="timeZoneSelectorPopUp"/>
</content>