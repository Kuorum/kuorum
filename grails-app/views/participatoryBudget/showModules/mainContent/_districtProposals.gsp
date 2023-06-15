
<g:render template="/participatoryBudget/showModules/cCallToAction" model="[participatoryBudget:participatoryBudget,campaignUser:campaignUser, districtProposal: districtProposal, hideXs:false]"/>
<ul class="nav nav-pills nav-underline" id="participatory-budget-district-proposals-list-tab-tag">
    <g:each in="${participatoryBudget.districts}" var="district">
        <li><a href="#${district.name}" id="${district.name.encodeAsKuorumUrl()}" data-districtId="${district.id}">
            <g:if test="${district.allCity}"><span class="fal fa-globe" data-toggle="tooltip" title="${g.message(code:'participatoryBudget.districts.tooltip.noRestriction')}"></span> </g:if>
            ${district.name}
        </a></li>
    </g:each>
</ul>

<div id="participatoryBudget-districtProposals-list">
    <g:each in="${participatoryBudget.districts}" var="district">
        <div style="display: none" id="proposal-district-${district.id}">
            <g:if test="${participatoryBudget.status == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.BALLOT}">
                <g:render template="/participatoryBudget/showModules/mainContent/districtInvestmentProgressBar" model="[
                        participatoryBudget: participatoryBudget,
                        district           : district,
                        progressBarWidth   : Math.round(district.amountUserInvested / district.budget * 100),
                        tooltipMsg         : g.message(code: 'participatoryBudget.progressBar.BALLOT.' + participatoryBudget.participatoryBudgetType + '.tooltip', args: [district.amountUserInvested, district.name])
                ]"/>
                <ul class="nav nav-pills nav-pills-lvl2 nav-underline ">
                    <li class="active active-no-click">
                        <a href="#" data-listSelector="random" data-districtId="${district.id}">
                            <g:message code="participatoryBudget.show.listProposals.sort.random"/>
                        </a>
                    </li>
                <sec:ifLoggedIn>
                    <li>
                        <a href="#" data-listSelector="voted" data-districtId="${district.id}">
                            <g:message code="participatoryBudget.show.listProposals.sort.votedByUser"/>
                        </a>
                    </li>
                    </sec:ifLoggedIn>

                    <g:if test="${participatoryBudget.participatoryBudgetType == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetTypeDTO.BUDGET}">
                        <li>
                            <a href="#" data-listSelector="price" data-direction="DESC"
                               data-districtId="${district.id}">
                                <g:message code="participatoryBudget.show.listProposals.sort.byPrice"/><span
                                    class="fal "></span>
                            </a>
                        </li>
                    </g:if>
                </ul>
            </g:if>
            <g:elseif test="${participatoryBudget.status == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.RESULTS}">
                <g:render template="/participatoryBudget/showModules/mainContent/districtInvestmentProgressBar" model="[
                        participatoryBudget: participatoryBudget,
                        district           : district,
                        progressBarWidth   : Math.round((district.investment ?: 0) / district.budget * 100),
                        importantClass     : true,
                        tooltipMsg         : g.message(code: 'participatoryBudget.progressBar.RESULTS.' + participatoryBudget.participatoryBudgetType + '.tooltip', args: [district.investment ?: 0, district.name],)
                ]"/>
            </g:elseif>
            <ul class="search-list clearfix random"
                data-page="0"
                data-randomSeed=""
                data-loadProposals="${g.createLink(mapping:'participatoryBudgetDistrictProposals', params:participatoryBudget.encodeAsLinkProperties()+[participatoryBudgetStatus:participatoryBudget.status, districtId:district.id])}"></ul>
            <ul class="search-list clearfix price"
                data-page="0"
                data-direction="DESC"
                data-loadProposals="${g.createLink(mapping:'participatoryBudgetDistrictProposals', params:participatoryBudget.encodeAsLinkProperties()+[participatoryBudgetStatus:participatoryBudget.status, districtId:district.id])}"></ul>
        <g:if test="${participatoryBudget.status != org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS &&
                      participatoryBudget.status != org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.TECHNICAL_REVIEW}">
            <sec:ifLoggedIn>
            <ul class="search-list clearfix voted"
                data-page="0"
                data-loadProposals="${g.createLink(mapping:'participatoryBudgetDistrictProposals', params:participatoryBudget.encodeAsLinkProperties()+[participatoryBudgetStatus:participatoryBudget.status, districtId:district.id, voter:userUtil.loggedUserId()])}"
               </ul>
 %{--data-loggedUser="${userUtil.loggedUserId()}">--}%
            </sec:ifLoggedIn>
        </g:if>
            <!-- District modal overflow -->
            <g:render template="/districtProposal/showModules/mainContent/districtProposalModalErrors" model="[district:district]"/>
        </div>
    </g:each>
</div>