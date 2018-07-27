
<g:render template="/participatoryBudget/showModules/cCallToAction" model="[participatoryBudget:participatoryBudget]"/>
<ul class="nav nav-pills nav-underline" id="participatoryBudget-districtProposals-list-tab">
    <g:each in="${participatoryBudget.districts}" var="district">
        <li><a href="#${district.name}" data-districtId="${district.id}">${district.name}</a></li>
    </g:each>
</ul>

<div id="participatoryBudget-districtProposals-list">
    <g:each in="${participatoryBudget.districts}" var="district">
        <ul class="search-list clearfix" style="display: none" id="proposal-district-${district.id}" data-page="0" data-loadProposals="${g.createLink(mapping:'participatoryBudgetDistrictProposals', params:participatoryBudget.encodeAsLinkProperties()+[districtId:district.id, page:0])}">
            %{--<g:each in="${proposalPage.data}" var="proposal">--}%
            %{--<g:render template="/campaigns/cards/campaignsList" model="[campaigns:campaigns]"/>--}%
            %{--</g:each>--}%
        </ul>
    </g:each>
</div>