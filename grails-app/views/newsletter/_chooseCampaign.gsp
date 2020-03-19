<ul class="actionIcons">
    <sec:ifAnyGranted roles="ROLE_CAMPAIGN_NEWSLETTER">
        <li class="fontIcon">
            <!--<span class="fa-stack fa-lg active"-->
            <g:link mapping="politicianMassMailingNew" role="button" class="actionIcons"
                    rel="tooltip"
                    data-toggle="tooltip"
                    data-placement="bottom"
                    title=""
                    data-original-title="${g.message(code:'dashboard.payment.chooseCampaign.tooltip.newsletter')}">
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
                    data-original-title="${g.message(code:'dashboard.payment.chooseCampaign.tooltip.post')}">
                <span class="fal fa-newspaper"></span>
                <span class="label"><g:message code="tools.campaign.new.post"/></span>
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
                    data-original-title="${g.message(code:'dashboard.payment.chooseCampaign.tooltip.debate')}">
                <span class="fal fa-comments"></span>
                <span class="label"><g:message code="tools.campaign.new.debate"/></span>
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
                    data-original-title="${g.message(code:'dashboard.payment.chooseCampaign.tooltip.event')}">
                <span class="fal fa-calendar-star"></span>
                <span class="label"><g:message code="tools.campaign.new.event"/></span>
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
                    data-original-title="${g.message(code:'dashboard.payment.chooseCampaign.tooltip.survey')}">
                <span class="fal fa-chart-bar"></span>
                <span class="label"><g:message code="tools.campaign.new.survey"/></span>
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
                    data-original-title="${g.message(code:'dashboard.payment.chooseCampaign.tooltip.petition')}">
                <span class="fal fa-microphone"></span>
                <span class="label"><g:message code="tools.campaign.new.petition"/></span>
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
                    data-original-title="${g.message(code:'dashboard.payment.chooseCampaign.tooltip.participatoryBudget')}">
                <span class="fal fa-money-bill-alt"></span>
                <span class="label"><g:message code="tools.campaign.new.participatoryBudget"/></span>
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
                    data-original-title="${g.message(code:'dashboard.payment.chooseCampaign.tooltip.districtProposal')}">
                <span class="fal fa-rocket"></span>
                <span class="label"><g:message code="tools.campaign.new.districtProposal"/></span>
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