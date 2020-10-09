<g:set var="campaignRoles" value="ROLE_CAMPAIGN_NEWSLETTER,ROLE_CAMPAIGN_POST,ROLE_CAMPAIGN_DEBATE,ROLE_CAMPAIGN_EVENT,ROLE_CAMPAIGN_SURVEY,ROLE_CAMPAIGN_PETITION,ROLE_CAMPAIGN_PARTICIPATORY_BUDGET,ROLE_CAMPAIGN_DISTRICT_PROPOSAL"/>
<sec:ifAnyGranted roles="${campaignRoles}">
    <g:if test="${chooseCampaignTitle}">
        <p>${raw(chooseCampaignTitle)}</p>
    </g:if>
    <ul class="actionIcons">
        <sec:ifAnyGranted roles="ROLE_CAMPAIGN_NEWSLETTER">
            <li class="fontIcon">
                <!--<span class="fa-stack fa-lg active"-->
                <g:link mapping="politicianMassMailingNew" role="button" class="actionIcons"
                        rel="tooltip"
                        data-toggle="tooltip"
                        data-placement="bottom"
                        title=""
                        data-original-title="${g.message(code:'dashboard.payment.chooseCampaign.tooltip.NEWSLETTER')}">
                    <span class="fal fa-envelope"></span>
                    <span class="label"><g:message code="tools.campaign.new.newsletter"/></span>
                </g:link>
            </li>
        </sec:ifAnyGranted>

        <sec:ifAnyGranted roles="ROLE_CAMPAIGN_POST">
            <li class="fontIcon">
                <g:link mapping="postCreate" role="button" class="actionIcons"
                        rel="tooltip"
                        data-toggle="tooltip"
                        data-placement="bottom"
                        title=""
                        data-original-title="${g.message(code:'dashboard.payment.chooseCampaign.tooltip.POST')}">
                    <span class="fal fa-newspaper"></span>
                    <span class="label"><g:message code="tools.campaign.new.POST"/></span>
                </g:link>
            </li>
        </sec:ifAnyGranted>

        <sec:ifAnyGranted roles="ROLE_CAMPAIGN_DEBATE">
            <li class="fontIcon">
                <g:link mapping="debateCreate" role="button" class="actionIcons"
                        rel="tooltip"
                        data-toggle="tooltip"
                        data-placement="bottom"
                        title=""
                        data-original-title="${g.message(code:'dashboard.payment.chooseCampaign.tooltip.DEBATE')}">
                    <span class="fal fa-comments"></span>
                    <span class="label"><g:message code="tools.campaign.new.DEBATE"/></span>
                </g:link>
            </li>
        </sec:ifAnyGranted>

        <sec:ifAnyGranted roles="ROLE_CAMPAIGN_EVENT">
            <li class="fontIcon">
                <g:link mapping="eventCreate" role="button" class="actionIcons"
                        rel="tooltip"
                        data-toggle="tooltip"
                        data-placement="bottom"
                        title=""
                        data-original-title="${g.message(code:'dashboard.payment.chooseCampaign.tooltip.EVENT')}">
                    <span class="fal fa-calendar-star"></span>
                    <span class="label"><g:message code="tools.campaign.new.EVENT"/></span>
                </g:link>
            </li>
        </sec:ifAnyGranted>


        <sec:ifAnyGranted roles="ROLE_CAMPAIGN_SURVEY">
            <li class="fontIcon">
                <g:link mapping="surveyCreate" role="button" class="actionIcons"
                        rel="tooltip"
                        data-toggle="tooltip"
                        data-placement="bottom"
                        title=""
                        data-original-title="${g.message(code:'dashboard.payment.chooseCampaign.tooltip.SURVEY')}">
                    <span class="fal fa-chart-bar"></span>
                    <span class="label"><g:message code="tools.campaign.new.SURVEY"/></span>
                </g:link>
            </li>
        </sec:ifAnyGranted>

        <sec:ifAnyGranted roles="ROLE_CAMPAIGN_PETITION">
            <li class="fontIcon">
                <g:link mapping="petitionCreate" role="button" class="actionIcons"
                        rel="tooltip"
                        data-toggle="tooltip"
                        data-placement="bottom"
                        title=""
                        data-original-title="${g.message(code:'dashboard.payment.chooseCampaign.tooltip.PETITION')}">
                    <span class="fal fa-microphone"></span>
                    <span class="label"><g:message code="tools.campaign.new.PETITION"/></span>
                </g:link>
            </li>
        </sec:ifAnyGranted>

        <sec:ifAnyGranted roles="ROLE_CAMPAIGN_PARTICIPATORY_BUDGET">
            <li class="fontIcon">
                <g:link mapping="participatoryBudgetCreate" role="button" class="actionIcons"
                        rel="tooltip"
                        data-toggle="tooltip"
                        data-placement="bottom"
                        title=""
                        data-original-title="${g.message(code:'dashboard.payment.chooseCampaign.tooltip.PARTICIPATORY_BUDGET')}">
                    <span class="fal fa-money-bill-alt"></span>
                    <span class="label"><g:message code="tools.campaign.new.PARTICIPATORY_BUDGET"/></span>
                </g:link>
            </li>
        </sec:ifAnyGranted>

        <sec:ifAnyGranted roles="ROLE_CAMPAIGN_DISTRICT_PROPOSAL">
            <li class="fontIcon active-participatory-budgets">
                <g:link mapping="participatoryBudgetList" role="button" class="actionIcons get-participatory-budgets"
                        rel="tooltip"
                        data-toggle="tooltip"
                        data-placement="bottom"
                        title=""
                        data-original-title="${g.message(code:'dashboard.payment.chooseCampaign.tooltip.DISTRICT_PROPOSAL')}">
                    <span class="fal fa-rocket"></span>
                    <span class="label"><g:message code="tools.campaign.new.DISTRICT_PROPOSAL"/></span>
                </g:link>

            <!-- Modal show active participative budgets -->
                <div class="modal modal-pb" id="listParticipatoryBudgets" tabindex="-1" role="dialog" aria-labelledby="listParticipatoryBudgetsTitle" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-title" id="listParticipatoryBudgetsTitle">
                            %{--<g:message code="dashboard.payment.chooseCampaign.tooltip.districtProposal.modal.title"/>--}%
                        </div>
                        <div class="modal-content container">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true" class="fal fa-times-circle fa"></span><span class="sr-only">Cerrar</span></button>
                            <div class="modal-header">
                                <h4>
                                    <g:message code="dashboard.payment.chooseCampaign.tooltip.districtProposal.modal.title"/>
                                </h4>
                            </div>
                            <div class="modal-body">
                                <div class="pb-list"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </li>
        </sec:ifAnyGranted>
    </ul>
</sec:ifAnyGranted>

<sec:ifNotGranted roles="${campaignRoles}">
    <p class="choose-campaign-empty">
        <g:message code="dashboard.payment.chooseCampaign.noActiveCampaigns" args="[_domainName, g.createLink(mapping:'dashboard')]" encodeAs="raw"/>
    </p>
</sec:ifNotGranted>