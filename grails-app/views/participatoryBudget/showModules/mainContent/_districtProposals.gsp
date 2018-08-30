
<g:render template="/participatoryBudget/showModules/cCallToAction" model="[participatoryBudget:participatoryBudget]"/>
<ul class="nav nav-pills nav-underline" id="participatoryBudget-districtProposals-list-tab">
    <g:each in="${participatoryBudget.districts}" var="district">
        <li><a href="#${district.name}" data-districtId="${district.id}">${district.name}</a></li>
    </g:each>
</ul>

<div id="participatoryBudget-districtProposals-list">
    <g:each in="${participatoryBudget.districts}" var="district">
        <div style="display: none" id="proposal-district-${district.id}">
            <g:if test="${participatoryBudget.status == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.BALLOT}">
                <div class="comment-box">
                    <div class="row">
                        <div class="col-md-3 label">
                            <g:message code="participatoryBudget.progressBar.label" />
                            <g:message code="kuorum.multidomain.money" args="[district.budget.encodeAsReducedPrice()]"/>
                        </div>
                        <div class="col-md-9 campaign-progress-bar-wrapper">
                            <div class="campaign-progress-bar" data-width="${Math.round(district.amountUserInvested/district.budget*100)}">
                                <div class="pop-up">
                                    <g:set var="userInvestement" value="${g.message(code:'kuorum.multidomain.money', args: [district.amountUserInvested])}" />
                                    <g:message code="participatoryBudget.progressBar.tooltip" args="[userInvestement, district.name]"/>
                                    <div class="arrow"></div>
                                </div>
                                <div class="progress-bar-custom">
                                    <div class="progress-bar-custom-done"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </g:if>
            <ul class="search-list clearfix" data-page="0" data-loadProposals="${g.createLink(mapping:'participatoryBudgetDistrictProposals', params:participatoryBudget.encodeAsLinkProperties()+[districtId:district.id, page:0])}">
                %{--<g:each in="${proposalPage.data}" var="proposal">--}%
                %{--<g:render template="/campaigns/cards/campaignsList" model="[campaigns:campaigns]"/>--}%
                %{--</g:each>--}%
            </ul>
        </div>
    </g:each>
</div>