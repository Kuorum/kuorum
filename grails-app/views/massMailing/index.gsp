<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="head.logged.account.tools.massMailing"/></title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li  class="active"><g:message code="head.logged.account.tools.massMailing"/> </li>
    </ol>

    <g:if test="${campaigns || posts || debates}">
        <div id="listCampaigns">
            <g:render template="searchCampaigns" model="[campaigns: campaigns, debates: debates, posts: posts, user: user]"/>
        </div>
    </g:if>
    <g:else>
        <div class="container-fluid box-ppal choose-campaign">


            <p><g:message code="dashboard.payment.noCampaigns" /></p>
            %{--<p>--}%
                %{--<g:link mapping="politicianCampaignsNew" class="btn inverted btn-lg">--}%
                    %{--<g:message code="tools.massMailing.list.newCampaign"/>--}%
                %{--</g:link>--}%
            %{--</p>--}%
            <g:render template="chooseCampaign"/>
        </div>
    </g:else>


<!-- MODAL AUTH PROFILE CHANGES -->
    <div class="modal fade in" id="modalEditScheduled" tabindex="-1" role="dialog" aria-labelledby="modalEditScheduledTitle" aria-hidden="true">
        <div class="modal-dialog ">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                        <span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                    </button>
                    <h4>
                        Hey slow down! This is an scheduled campaign
                        %{--<g:message code="kuorum.web.commands.profile.AccountDetailsCommand.password.modal.title"/>--}%
                    </h4>
                </div>
                <div class="modal-body">
                    <p>If you keep going, your campaign will automatically become a draft.</p>
                    %{--<p><g:message code="kuorum.web.commands.profile.AccountDetailsCommand.password.modal.body"/> </p>--}%
                    <fieldset class="text-right">
                        <a href="#" class="btn btn-grey-light btn-lg" data-dismiss="modal" id="modalEditScheduledButtonClose">
                            <g:message code="kuorum.web.commands.profile.AccountDetailsCommand.password.modal.cancel"/>
                        </a>
                        <a href="#" class="btn btn-blue inverted btn-lg" id="modalEditScheduledButtonOk">
                            <g:message code="kuorum.web.commands.profile.AccountDetailsCommand.password.modal.confirm"/>
                        </a>
                    </fieldset>

                </div>
            </div>
        </div>
    </div>

</content>