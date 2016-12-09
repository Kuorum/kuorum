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
            %{--<userUtil:isWeceUser>--}%
                <li role="presentation" class="${campaignId?'disabled':''}"><g:link mapping="projectCreate"><g:message code="tools.campaign.type.debate"/></g:link></li>
            %{--</userUtil:isWeceUser>
            <userUtil:isNotWeceUser>
                <li role="presentation" class="${campaignId?'disabled':''}"><a href="#debate" data-toggle="tab"><g:message code="tools.campaign.type.debate"/></a></li>
            </userUtil:isNotWeceUser>--}%
            <li role="presentation" class="${campaignId?'disabled':''}"><a href="#petition" data-toggle="tab"><g:message code="tools.campaign.type.petition"/></a></li>
            <li role="presentation" class="${campaignId?'disabled':''}"><a href="#survey" data-toggle="tab"><g:message code="tools.campaign.type.survey"/></a></li>
        </ul>
        <div id="tabs-new-campaign" class="tab-content">
            <div class="tab-pane active" id="newsletter">
                <g:render template="types/massMailing" model="[command:command, filters:filters, totalContacts:totalContacts, campaignId:campaignId, anonymousFilter:anonymousFilter]"/>
            </div>
            <div class="tab-pane" id="debate">
                <g:render template="types/notDone"/>
            </div>
            <div class="tab-pane" id="petition">
                <g:render template="types/notDone"/>
            </div>
            <div class="tab-pane" id="survey">
                <g:render template="types/notDone"/>
            </div>
        </div>
    </div>

    <!-- First MassMailing Popup-->
    <g:if test="${showTimeZonePopup}">
        <div class="modal fade in" id="enterTimeZone" tabindex="-1" role="dialog" aria-labelledby="contactDeleteTitle" aria-hidden="true">
            <div class="modal-dialog ">
                <div class="modal-content">
                    <formUtil:validateForm bean="${profileCommand}" form="timeZoneForm" dirtyControl="false"/>
                    <g:form method="POST" mapping="politicianMassMailingSaveTimeZone" name="timeZoneForm" role="form" class="submitOrangeButton" autocomplete="noFill">
                        <div class="modal-header"><g:message code="modal.timeZone.header"/></div>
                        <div class="modal-body">
                            <fieldset class="time-zone">
                                <div class="row form-group">
                                    <div class="col-xs-12 col-sm-4">
                                        <formUtil:selectTimeZone command="${profileCommand}" field="timeZoneId" required="true"/>
                                    </div>
                                </div>
                            </fieldset>
                        </div>
                        <div class="modal-footer">
                            <button class="btn" type="submit"><g:message code="modal.timeZone.send"/></button>
                        </div>
                    </g:form>
                </div>
            </div>
        </div>
        <script>
            $(function() {
                $("#enterTimeZone").modal({
                    backdrop: 'static',
                    keyboard: false
                }).modal('show');
            });
        </script>
    </g:if>

</content>