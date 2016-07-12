<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.politicianProfile.title" args="[politician.name]"/></title>
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
            <img src="${image.userImgProfile(user:politician)}" alt="${g.message(code:'page.politicianProfile.imageProfile.alt', args:[politician.name])}">

        </div>
        <div class="panel-body">
            <div class="row">
                <div class="col-xs-12 profile-pic-col">
                    <div class="profile-pic text-center">
                        <img alt="${message(code:'page.politicianProfile.imageAvatar.alt', args: [politician.name])}"
                             class="img-circle"
                             data-src="holder.js/140x140"
                             src="${image.userImgSrc(user:politician)}"
                             data-holder-rendered="true"
                             itemprop="image">
                            <g:if test="${politician.enabled}">
                                <abbr title="${message(code:'politician.image.icon.enabled.text')}"><i class="fa fa-check"></i></abbr>
                            </g:if>
                            <g:elseif test="${politician.userType.equals(UserType.CANDIDATE)}">
                                <abbr title="${message(code:'politician.image.icon.candidate.text')}"><i class="fa icon-megaphone"></i></abbr>
                            </g:elseif>
                            <g:else>
                                <abbr title="${message(code:'politician.image.icon.notEnabled.text')}"><i class="fa fa-binoculars"></i></abbr>
                            </g:else>
                    </div>
                </div>
            </div><!--/.row -->
            <div class="row extra-padding">
                <div class='col-sm-7 profile-title'>
                    <h1 itemprop="name">${politician.name}</h1>
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
                <g:if test="${causes}">
                    <h4><g:message code="politician.causes"/> </h4>
                    <ul class='causes-tags'>
                        <g:each in="${causes}" var="cause">
                            <cause:show cause="${cause}"/>
                        </g:each>
                    </ul>
                </g:if>
                <g:render template="/kuorumUser/showExtendedPoliticianTemplates/userNews" model="[user:politician, userNews:userNews]"/>
                <h4><g:message code="politician.bio"/></h4>
                <p class="limit-height" data-collapsedHeight="50"  itemprop="description">${politician.bio}</p>
            </div><!--/.extra-padding -->
        </div><!--/.panel-body-->
    </div><!--/.panel panel-default -->

    <g:render template="showExtendedPoliticianTemplates/politicianLastActivity" model="[politician: politician]"/>
</content>

<content tag="cColumn">
    <g:render template="showExtendedPoliticianTemplates/columnC/socialButtonsExtendedPoliticianColumnC" model="[user:politician]"/>
    %{--<g:render template="showExtendedPoliticianTemplates/columnC/contactPolitician" model="[politician:politician]"/>--}%
    <g:render template="/modules/recommendedUsers" model="[recommendedUsers:recommendPoliticians, boxTitle:g.message(code:'modules.similarPoliticians.title')]"/>
    <g:render template="showExtendedPoliticianTemplates/columnC/valuationChart" model="[user:politician, leaningIndex:politicianLeaningIndex]"/>
    <g:render template="showExtendedPoliticianTemplates/columnC/leaningIndex" model="[user:politician, leaningIndex:politicianLeaningIndex]"/>
    <g:render template="showExtendedPoliticianTemplates/columnC/professionalDetailExtendedPolitician" model="[politician:politician]"/>
    <g:render template="showExtendedPoliticianTemplates/columnC/quickNotesExtendedPolitician" model="[politician:politician]"/>
</content>


<content tag="extraRowData">
    <g:render template="showExtendedPoliticianTemplates/politicianTimeLine" model="[politician:politician]"/>
</content>

<content tag="preFooterSections">
    <g:render template="showExtendedPoliticianTemplates/latestProjects" model="[politician:politician, userProjects:userProjects]"/>
    <g:render template="showExtendedPoliticianTemplates/recommendedPoliticians" model="[politician:politician, recommendPoliticians:recommendPoliticians]"/>

</content>

<content tag="modals">
    <g:if test="${campaign}">
        <g:render template="showExtendedPoliticianTemplates/modals/modalElection" model="[politician:politician, campaign:campaign]"/>
    </g:if>
    <g:render template="showExtendedPoliticianTemplates/modals/modalContact" model="[politician:politician, causes:causes]"/>
</content>