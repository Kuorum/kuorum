<!-- ^leader-post !-->
<div class="leader-post">
    <g:render template="showModules/mainContent/debateDataMultimedia" model="[debate:debate, poweredByKuorum:poweredByKuorum]"/>

    <div class="header">
        <h1 class="title" itemprop="headline">${debate.title}</h1>

        <userUtil:showUser user="${debateUser}" showRole="true" itemprop="author"/>

        <div class="clearfix">
            <span class="time-ago pull-left"><kuorumDate:humanDate date="${debate.datePublished}" itemprop="datePublished"/> </span>

            <g:if test="${eventData && !eventRegistration}">
                <div class="actions call-to-action-mobile call-mobile-event-confirm event-unconfirmed"
                     data-userLoggedAlias="${userUtil.loggedUserAlias()}"
                     data-postUrl="${g.createLink(mapping: 'eventConfirmAssistance')}"
                     data-debateId="${debate.id}">
                    %{--EVENT DATA - CHAPU BORRAR --}%
                    <button type="button" class="btn btn-orange btn-lg call-message">
                        <g:message code="event.callToAction.button"/>
                    </button>
                    <span class="fa fa-caret-down arrow"></span>
                    <button type="button" class="btn btn-orange btn-xl btn-circle call-button">
                        <span class="fa fa-ticket fa-2x"></span>
                    </button>
                </div>
            </g:if>
            <div class="actions call-to-action-mobile add-proposal">
                %{--EVENT DATA - CHAPU BORRAR --}%
                <button type="button" class="btn btn-blue btn-lg call-message">
                    <g:if test="${eventRegistration}">
                        <g:message code="event.callToAction.success.mobile"/>
                    </g:if>
                    <g:else>
                        <g:message code="debate.proposals.callToAction.mobile.message"/>
                    </g:else>
                </button>
                <span class="fa fa-caret-down arrow"></span>
                <button type="button" class="btn btn-blue btn-xl btn-circle call-button">
                    <span class="fa fa-lightbulb-o fa-2x"></span>
                </button>
            </div>

            <userUtil:ifUserIsTheLoggedOne user="${debateUser}">
                %{--campaignList contains the js to open modal when the debate is scheduled --}%
                <r:require modules="campaignList"/>

                <g:set var="modal" value="${debate.newsletter.status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SCHEDULED ?'modalEditScheduled':''}"/>
                <g:link class="edit ${modal}" mapping="debateEditContent" params="${debate.encodeAsLinkProperties()}">
                    <span class="fa fa-pencil-square-o pull-right fa-2x" aria-hidden="true"></span>
                </g:link>
            </userUtil:ifUserIsTheLoggedOne>
        </div>
    </div>

    <div class="body" itemprop="articleBody">
        ${raw(debate.body)}
    </div>

    <div class="footer clearfix">
        %{--<g:render template="/debate/showModules/mainContent/debateDataLabels" model="[debate:debate]"/>--}%
        <g:render template="/debate/showModules/mainContent/debateDataSocial" model="[debate:debate, debateUser:debateUser]"/>

        <g:if test="${debate.campaignStatusRSDTO == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT}">
            <div class="comment-counter pull-right">
                <button type="button">
                    <span class="fa fa-lightbulb-o" aria-hidden="true"></span>
                    <span class="number">${proposalPage.total}</span>
                </button>
            </div>
        </g:if>
    </div>
</div> <!-- ^leader-post !-->