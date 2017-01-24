<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">
        <g:message code="admin.createDebate.title"/>
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
            <li role="presentation" class="${debateId ? 'disabled' : ''}"><g:link mapping="politicianMassMailingNew"><g:message code="tools.campaign.type.massMailing"/></g:link></li>
            <li role="presentation" class="active"><a href="#debate" data-toggle="tab"><g:message code="tools.campaign.type.debate"/></a></li>
            <li role="presentation" class="${debateId ? 'disabled' : ''}"><a href="#petition" data-toggle="tab"><g:message code="tools.campaign.type.petition"/></a></li>
            <li role="presentation" class="${debateId ? 'disabled' : ''}"><a href="#survey" data-toggle="tab"><g:message code="tools.campaign.type.survey"/></a></li>
        </ul>
        <div id="tabs-new-campaign" class="tab-content">
            <div class="tab-pane" id="newsletter">

            </div>
            <div class="tab-pane active" id="debate">
                <g:render template="/debate/formEditDebate" model="[command: command, filters: filters, totalContacts: totalContacts, debateId: debateId, anonymousFilter: anonymousFilter]"/>
            </div>
            <div class="tab-pane" id="petition">
                <g:render template="/massMailing/types/notDone"/>
            </div>
            <div class="tab-pane" id="survey">
                <g:render template="/massMailing/types/notDone"/>
            </div>
        </div>
    </div>
</content>