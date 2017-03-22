<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">
        <g:message code="admin.createProject.title"/>
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
        <li class="active">${breadCrumbName}</li>
    </ol>
    <div class="box-ppal campaign-new">
        <ul class="nav nav-tabs simple" data-tabs="tabs">
            <li role="presentation"><g:link mapping="politicianCampaignsNew"><g:message code="tools.campaign.type.massMailing"/></g:link></li>
            <li role="presentation" class="active"><a href="#debate" data-toggle="tab"><g:message code="tools.campaign.type.debate"/></a></li>
            <li role="presentation"><a href="#petition" data-toggle="tab"><g:message code="tools.campaign.type.petition"/></a></li>
            <li role="presentation"><a href="#survey" data-toggle="tab"><g:message code="tools.campaign.type.survey"/></a></li>
        </ul>
        <div id="tabs-new-campaign" class="tab-content">
            <div class="tab-pane" id="newsletter">

            </div>
            <div class="tab-pane active" id="debate">
                <g:form mapping="projectCreate" method="POST" name="edit-project" role="form" class="box-ppal">
                    <g:render template="/project/formProject" model="[command: command]"/>
                    <fieldset class="btns text-right">
                        <div class="form-group">
                            <a href="#" class="btn btn-grey cancel saveDraft">${message(code:'admin.createProject.saveDraft')}</a>
                            <input type="submit" class="btn btn-lg" value="${message(code:'admin.createProject.publish')}"/>
                        </div>
                    </fieldset>
                </g:form>
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