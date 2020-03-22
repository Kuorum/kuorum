<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.politicianProfile.title" args="[politician.fullName, _domainName]"/></title>
    <g:set var="schemaData" value="${[schema:'http://schema.org/Person', name:politician.fullName]}" scope="request"/>
    <meta name="layout" content="columnCLayout">
    <parameter name="specialContainerCssClass" value="userprofile" />
    <parameter name="specialMainContentCssClass" value="politician-card" />
    <g:render template="userMetaTags" model="[user:politician]"/>
    <link rel="canonical" href="${g.createLink(mapping:"userShow", params: politician.encodeAsLinkProperties(), absolute:true)}"/>
    <r:require modules="post, districtProposal, kuorumUser"/>
</head>

<content tag="mainContent">
    <div class="panel panel-default">
        <div class='profile-header'>
            <g:render template="editUserOptionsProfile" model="[user:politician]"/>
            <image:userImgProfile user="${politician}" alt="${g.message(code:'page.politicianProfile.imageProfile.alt', args:[politician.fullName])}"/>
        </div>
        <div class="panel-body">
            <div class="row">
                <div class="col-xs-12 profile-pic-col">
                    <div class="profile-pic text-center">
                        <img alt="${message(code:'page.politicianProfile.imageAvatar.alt', args: [politician.fullName])}"
                             class="img-circle"
                             src="${image.userImgSrc(user:politician)}"
                             itemprop="image">
                            <userUtil:userTypeIcon user="${politician}"/>
                    </div>
                </div>
            </div><!--/.row -->
            <div class="row extra-padding">
                <div class='col-sm-7 profile-title'>
                    <h1 itemprop="name">${politician.fullName}</h1>
                    <cite itemprop="jobTitle"><userUtil:userRegionName user="${politician}"/></cite>
                    <p class='party' itemprop="affiliation"><span  itemscope itemtype="http://schema.org/Organization">${userUtil.roleName(user:politician)}</span></p>
                </div>
                <div class="col-sm-5">
                    <g:render template="politicianValuation" model="[user:politician, userReputation:userReputation]"/>
                    <div class="follow-btn-group">
                        <userUtil:followButton user="${politician}" cssExtra="inverted"/>
                        <userUtil:contactButton user="${politician}" cssExtra="inverted"/>
                    </div>
                </div>
            </div>
            <div class="extra-padding text-left following">
                <userUtil:ifIsFollower user="${politician}">
                    <i class="fal fa-check-circle"></i><g:message code="kuorumUser.popover.follower"/>
                </userUtil:ifIsFollower>
            </div>
            <div class="extra-padding" id="politician-bio">
                <g:render template="/kuorumUser/userShowTemplates/userNews" model="[user:politician, userNews:userNews]"/>
                <g:if test="${politician.bio}">
                    <h4><g:message code="politician.bio"/></h4>
                    <div class="limit-height" data-collapsedHeight="100"  itemprop="description">
                        <div class="clearfix">${politician.bio}</div>
                    </div>
                %{--<p class="limit-height" data-collapsedHeight="50"  itemprop="description">${politician.bio}</p>--}%
                </g:if>
            </div><!--/.extra-padding -->
        </div><!--/.panel-body-->
    </div><!--/.panel panel-default -->

    <g:if test="${debates && posts}">
    <ul id="campaign-sorter" class="nav nav-pills nav-underline hidden-xs">
        <li class="active"><a href="#latest"><g:message code="search.filters.all"/> </a></li>
        <li><a href="#posts"><g:message code="search.filters.SolrType.POST"/></a></li>
        <li><a href="#debates"><g:message code="search.filters.SolrType.DEBATE"/></a></li>
    </ul>
    </g:if>

    <ul class="search-list clearfix">
        <g:render template="/campaigns/cards/campaignsList" model="[campaigns:campaigns]"/>
    </ul>
</content>

<content tag="cColumn">

    <g:render template="userShowTemplates/columnC/socialButtonsColumnC" model="[user:politician]"/>
    %{--<g:render template="showExtendedPoliticianTemplates/columnC/contactPolitician" model="[politician:politician]"/>--}%
    <g:render template="/dashboard/dashboardModules/supportedCauses" model="[user:politician, supportedCauses:causes]"/>
    <g:render template="userShowTemplates/columnC/recommendedUsers" model="[user:politician,boxTitle:g.message(code:'modules.similarPoliticians.title')]"/>
    <g:render template="userShowTemplates/columnC/quickNotes" model="[politician:politician]"/>
</content>

<content tag="modals">
        <g:render template="userShowTemplates/modals/modalContact" model="[politician:politician, causes:causes]"/>

</content>