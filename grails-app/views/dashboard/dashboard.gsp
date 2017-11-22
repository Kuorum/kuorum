<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.dashboard.crmUser.normal"/></title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
    <r:require modules="post"/>
    <g:if test="${tour}">
        <r:require module="tour"/>
    </g:if>
</head>

<content tag="mainContent">
    <div class="row dashboard">
        <div class="col-md-8">
            <g:render template="/dashboard/payment/dashboardModules/campaignNewCampaign" model="[lastCampaign:lastCampaign, durationDays:durationDays]"/>
            <g:if test="${debates || posts}">
                %{--<h2 class="campaign-list-title">${g.message(code:"dashboard.payment.followingCampaignList.title")}</h2>--}%
            </g:if>
            %{--<g:if test="${debates && posts}">--}%
            %{--<ul id="campaign-sorter" class="nav nav-pills nav-underline hidden-xs">--}%
                %{--<li class="active"><a href="#latest"><g:message code="search.filters.all"/> </a></li>--}%
                    %{--<li><a href="#posts"><g:message code="search.filters.SolrType.POST"/></a></li>--}%
                    %{--<li><a href="#debates"><g:message code="search.filters.SolrType.DEBATE"/></a></li>--}%
                %{--</ul>--}%
            %{--</g:if>--}%

            <ul class="search-list clearfix" data-addCampaignsByUserUrl="${g.createLink(mapping:'politicianCampaignsLists' )}" id="campaign-list-id" >
                <li class="info-empty hidden">
                    <div class="box-ppal">
                        <p>
                            <span class="text-empty-campaignList">
                                <g:message code='dashboard.payment.followingCampaignList.empty'/>
                            </span>
                            <span class="icon-empty-campaignList">
                                <img src="${g.resource(dir: "images/icons", file: "icon-info.png")}"/>
                            </span>
                        </p>

                    </div>
                </li>
                <li class="info-empty hidden"></li>
                <g:render template="/campaigns/cards/campaignsList" model="[campaigns:campaigns, showAuthor: showAuthor]" />
            </ul>
            <!-- ver más -->
            <nav:loadMoreLink
                    formId="campaign-list-loadMore"
                    mapping="dashboardCampaignsSeeMore"
                    parentId="campaign-list-id"
                    callback="campaignListCallback"
                    pagination="${[max:10]}"
                    numElements="${totalCampaigns}"/>
            <r:script>
                function campaignListCallback(){
                    sortCampaigns.orderList();
                    youtubeHelper.replaceAllWrongYoutubeImages();
                }
            </r:script>
        </div>
        <div class="col-md-4">
            <g:render template="/dashboard/payment/dashboardModules/dashboardPoliticianProfile" model="[user:user, emptyEditableData:emptyEditableData, numberCampaigns:numberCampaigns]"/>
            <g:render template="/dashboard/payment/dashboardModules/followOtherPoliticians" model="[recommendedUsers:recommendations]"/>
        </div>
    </div>
</content>