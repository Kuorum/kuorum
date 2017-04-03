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
            <g:render template="/dashboard/payment/dashboardModules/campaignNewCampaign" model="[lastCampaign:lastCampaign,durationDays:durationDays,contacts:contacts]"/>
        </div>
        <div class="col-md-4">
            <g:render template="/dashboard/payment/dashboardModules/dashboardPoliticianProfile" model="[user:user, emptyEditableData:emptyEditableData, numberCampaigns:numberCampaigns]"/>
            %{--<g:render template="/dashboard/payment/dashboardModules/followOtherPoliticians" model="[recommendedUsers:recommendedUsers]"/>--}%
        </div>
    </div>
    <div class="row contacts-campaigns">
        <div class="col-md-12">
            <g:if test="${debates || posts}">
                <h2 class="campaign-list-title">${g.message(code:"dashboard.payment.followingCampaignList.title")}</h2>
            </g:if>
            <g:if test="${debates && posts}">
                <ul id="campaign-sorter" class="nav nav-pills nav-underline hidden-xs">
                    <li class="active"><a href="#latest"><g:message code="search.filters.all"/> </a></li>
                    <li><a href="#posts"><g:message code="search.filters.SolrType.POST"/></a></li>
                    <li><a href="#debates"><g:message code="search.filters.SolrType.DEBATE"/></a></li>
                </ul>
            </g:if>

            <ul class="campaign-list clearfix">
                <g:each in="${debates}" var="debate">
                    <g:render template="/campaigns/cards/debateList" model="[debate:debate, user:politician, referred:'dashboard']" />
                </g:each>
                <g:each in="${posts}" var="post">
                    <g:render template="/campaigns/cards/postList" model="[post:post, user:politician, referred:'dashboard']"/>
                </g:each>
            </ul>
        </div>
    </div>
</content>