<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.politicianProfile.title" args="[politician.fullName]"/></title>
    <g:set var="schema" value="http://schema.org/Person" scope="request"/>
    <parameter name="schema" value="http://schema.org/Person" />
    <meta name="layout" content="columnCLayout">
    <parameter name="specialContainerCssClass" value="userprofile" />
    <parameter name="specialMainContentCssClass" value="politician-card" />
    <g:render template="userMetaTags" model="[user:politician]"/>
    <link rel="canonical" href="${g.createLink(mapping:"userShow", params: politician.encodeAsLinkProperties(), absolute:true)}"/>
</head>

<content tag="mainContent">
    <div class="panel panel-default">
        <div class='profile-header'>
            <g:render template="editUserOptionsProfile" model="[user:politician]"/>
            <img src="${image.userImgProfile(user:politician)}" alt="${g.message(code:'page.politicianProfile.imageProfile.alt', args:[politician.fullName])}">

        </div>
        <div class="panel-body">
            <div class="row">
                <div class="col-xs-12 profile-pic-col">
                    <div class="profile-pic text-center">
                        <img alt="${message(code:'page.politicianProfile.imageAvatar.alt', args: [politician.fullName])}"
                             class="img-circle"
                             data-src="holder.js/140x140"
                             src="${image.userImgSrc(user:politician)}"
                             data-holder-rendered="true"
                             itemprop="image">
                            <userUtil:userTypeIcon user="${politician}"/>
                    </div>
                </div>
            </div><!--/.row -->
            <div class="row extra-padding">
                <div class='col-sm-7 profile-title'>
                    <h1 itemprop="name">${politician.fullName}</h1>
                    <cite itemprop="jobTitle"><userUtil:politicianPosition user="${politician}"/></cite>
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
                %{--<g:message code="kuorumUser.show.module.followers.title" args="[politician.numFollowers]"/>--}%
                <userUtil:ifIsFollower user="${politician}">
                    <i class="fa fa-check-circle-o"></i><g:message code="kuorumUser.popover.follower"/>
                </userUtil:ifIsFollower>
            </div>
            <div class="extra-padding" id="politician-bio">
                <g:render template="/kuorumUser/userShowTemplates/userNews" model="[user:politician, userNews:userNews]"/>
                <h4><g:message code="politician.bio"/></h4>
                <p class="limit-height" data-collapsedHeight="50"  itemprop="description">${politician.bio}</p>
            </div><!--/.extra-padding -->
        </div><!--/.panel-body-->
    </div><!--/.panel panel-default -->


    %{--<ul id="proposal-option" class="nav nav-pills nav-underline">--}%
        %{--<li class="active"><a href="#latest"><g:message code="debate.proposals.nav.latest"/> </a></li>--}%
        %{--<li><a href="#oldest"><g:message code="debate.proposals.nav.oldest"/></a></li>--}%
        %{--<li><a href="#best"><g:message code="debate.proposals.nav.best"/></a></li>--}%
        %{--<li><a href="#pinned"><g:message code="debate.proposals.nav.pinned"/></a></li>--}%
    %{--</ul>--}%

    <ul class="campaign-list clearfix">
        <g:each in="${debates}" var="debate">
            <g:render template="activity/debateList" model="[debate:debate, user:politician]"/>
        </g:each>
    </ul>
</content>

<content tag="cColumn">

    <g:render template="userShowTemplates/columnC/socialButtonsColumnC" model="[user:politician]"/>
    %{--<g:render template="showExtendedPoliticianTemplates/columnC/contactPolitician" model="[politician:politician]"/>--}%
    <g:render template="userShowTemplates/columnC/subscribeForm" model="[user:politician]"/>
    <g:render template="/dashboard/dashboardModules/supportedCauses" model="[user:politician, supportedCauses:causes]"/>
    <g:render template="/modules/recommendedUsers" model="[recommendedUsers:recommendPoliticians, boxTitle:g.message(code:'modules.similarPoliticians.title')]"/>
    %{--<g:render template="showExtendedPoliticianTemplates/columnC/valuationChart" model="[user:politician]"/>--}%
    <g:render template="userShowTemplates/columnC/professionalDetails" model="[politician:politician]"/>
    <g:render template="userShowTemplates/columnC/quickNotes" model="[politician:politician]"/>
</content>


<content tag="extraRowData">

</content>

<content tag="preFooterSections">
    <g:render template="userShowTemplates/latestProjects" model="[politician:politician, userProjects:userProjects, divCss:'main']"/>
    <g:render template="userShowTemplates/recommendedPoliticians" model="[politician:politician, recommendPoliticians:recommendPoliticians]"/>

</content>

<content tag="modals">
    <g:if test="${campaign}">
        <g:render template="userShowTemplates/modals/modalElection" model="[politician:politician, campaign:campaign]"/>
    </g:if>
    <g:render template="userShowTemplates/modals/modalContact" model="[politician:politician, causes:causes]"/>
</content>