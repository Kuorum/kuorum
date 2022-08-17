<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.dashboard.crmUser.normal"/></title>
    <meta name="layout" content="basicPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${_domainName}">
    <r:require modules="campaignsTargetsListResourcesRequired"/>
</head>

<content tag="mainContent">
    <div class="row dashboard">
        <div class="col-md-8">

        %{--<g:if test="${debates && posts}">--}%
        %{--<ul id="campaign-sorter" class="nav nav-pills nav-underline hidden-xs">--}%
        %{--<li class="active"><a href="#latest"><g:message code="search.filters.all"/> </a></li>--}%
        %{--<li><a href="#posts"><g:message code="search.filters.SolrType.POST"/></a></li>--}%
        %{--<li><a href="#debates"><g:message code="search.filters.SolrType.DEBATE"/></a></li>--}%
        %{--</ul>--}%
        %{--</g:if>--}%

            <div class="info-campaigns-empty hidden">
                <div class="box-ppal">
                    <p>
                        <span class="text-empty-campaignList">
                            <g:if test="${_isSocialNetwork}">
                                <g:message code='dashboard.payment.followingCampaignList.empty'/>
                            </g:if>
                            <g:else>
                                <g:message code='dashboard.payment.followinList.empty'/>
                            </g:else>
                        </span>
                        <span class="icon-empty-campaignList">
                            <span class="fas fa-info-circle"></span>
                        </span>
                    </p>

                </div>
            </div>

            <ul class="search-list clearfix"
                data-addCampaignsByUserUrl="${g.createLink(mapping: 'dashboardCampaignsSeeMore')}"
                data-callback="campaignListCallback" id="campaign-list-id">
                <g:render template="/campaigns/cards/campaignsList" model="[campaigns: starredCampaigns, showAuthor: showAuthor, highlighted:true]"/>
                <g:render template="/campaigns/cards/campaignsList" model="[campaigns: campaigns, showAuthor: showAuthor, highlighted:false]"/>
            </ul>
            <!-- ver más -->
            <nav:loadMoreLink
                    formId="campaign-list-loadMore"
                    mapping="dashboardCampaignsSeeMore"
                    parentId="campaign-list-id"
                    callback="campaignListCallback"
                    pagination="${[max: 10]}"
                    cssClass="hidden"
                    delayed="true"
                    numElements="${totalCampaigns}"/>
            <r:script>
                function campaignListCallback() {
                    sortCampaigns.orderList();
                    prepareYoutubeVideosClick();
                }
            </r:script>
        </div>

        <div class="col-md-4">
            <g:render template="/dashboard/payment/dashboardModules/dashboardPoliticianProfile"
                      model="[user: user, emptyEditableData: emptyEditableData, numberCampaigns: numberCampaigns]"/>
            <g:if test="${_isSocialNetwork}">
                <g:render template="/dashboard/payment/dashboardModules/followOtherPoliticians"/>
            </g:if>
        </div>
    </div>
</content>