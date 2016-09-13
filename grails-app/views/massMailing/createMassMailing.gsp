<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">
        <g:if test="${command?.subject}">
            <g:message code="head.logged.account.tools.massMailing.edit" args="[command.subject]"/>
        </g:if>
        <g:else>
            <g:message code="head.logged.account.tools.massMailing.new"/>
        </g:else>
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
        <li><g:link mapping="politicianMassMailing"><g:message code="head.logged.account.tools.massMailing"/></g:link></li>
        <li class="active">${breadCrumbName}</li>
    </ol>
    <div class="box-ppal">
        <ul class="nav nav-tabs simple" data-tabs="tabs">
            <li role="presentation" class="active"><a href="#newsletter" data-toggle="tab"><g:message code="tools.campaign.type.massMailing"/></a></li>
            <li role="presentation"><a href="#petition" data-toggle="tab"><g:message code="tools.campaign.type.petition"/></a></li>
            <li role="presentation"><g:link mapping="projectCreate"><g:message code="tools.campaign.type.debate"/></g:link></li>
            <li role="presentation"><a href="#survey" data-toggle="tab"><g:message code="tools.campaign.type.survey"/></a></li>
        </ul>
        <div id="tabs-new-campaign" class="tab-content">
            <div class="tab-pane active" id="newsletter">
                <g:render template="types/massMailing" model="[command:command, filters:filters, totalContacts:totalContacts]"/>
            </div>
            <div class="tab-pane" id="petition">
                <g:render template="types/notDone"/>
            </div>
            <div class="tab-pane" id="debate">
                <g:render template="types/notDone"/>
            </div>
            <div class="tab-pane" id="survey">
                <g:render template="types/notDone"/>
            </div>
        </div>
    </div>
</content>