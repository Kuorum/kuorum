<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${politician.name}</title>
    <meta name="layout" content="columnCLayout">
    <parameter name="specialContainerCssClass" value="userprofile" />
    <g:render template="userMetaTags" model="[user:politician]"/>
    <r:require modules="newStyle" />
</head>


<content tag="subHeader">
</content>

<content tag="mainContent">
    <div class="panel panel-default">
        <div class='card-header' id='obama-header' style="background: url(${image.userImgProfile(user:politician)}) 50% 50% no-repeat;">
            <userUtil:followButton user="${politician}">
                <i class="fa fa-plus"></i>
            </userUtil:followButton>
        </div>
        <div class="panel-body">
            <div class="row">
                <div class="col-xs-12 col-sm-3 profile-pic-col">
                    <div class="profile-pic">
                        <img alt="${politician.name}"
                             class="img-circle"
                             data-src="holder.js/140x140"
                             src="${image.userImgSrc(user:politician)}"
                             data-holder-rendered="true">
                        <i class="fa fa-check"></i>
                    </div>

                </div>
                <div class='col-xs-7 col-sm-5 profile-title'>
                    <h2>${politician.name}</h2>
                    <cite>${politician?.professionalDetails?.position?:"N/A"}, ${politician?.professionalDetails?.region?.name?:"N/A"}</cite>
                    <p class='party'>${userUtil.roleName(user:politician)}</p>
                </div>
                <div class="col-xs-5 col-sm-4 following">
                    %{--<strong>Followers 58K</strong><br/>--}%
                    <strong><g:message code="kuorumUser.show.module.followers.title" args="[politician.numFollowers]"/></strong><br/>
                    <userUtil:ifIsFollower user="${politician}">
                        <i class="fa fa-check-circle-o"></i>
                        <g:message code="kuorumUser.popover.follower"/>
                    </userUtil:ifIsFollower>
                </div>
            </div><!--/.row -->

            <div class="extra-padding">
                <g:if test="${politician.tags}">
                    <h4><g:message code="politician.causes"/> </h4>
                    <ul class='causes-tags'>
                        <g:each in="${politician.tags}" var="tag">
                            <li>
                                <g:link mapping="searcherSearch" params="[type:UserType.POLITICIAN, word:tag]">
                                    ${tag}
                                </g:link>
                            </li>
                        </g:each>
                    </ul>
                </g:if>
                <g:if test="${politician.relevantEvents}">
                    <h4><g:message code="politician.knownFor"/></h4>
                    <ul class='known-for'>
                        <g:each in="${politician.relevantEvents}" var="relevantEvent">
                            <li><a href="${relevantEvent.url}" _target="blank">${relevantEvent.title}</a></li>
                        </g:each>
                    </ul>
                </g:if>
                <h4><g:message code="politician.bio"/></h4>
                <p>${politician.bio}</p>
            </div><!--/.extra-padding -->
        </div><!--/.panel-body-->
    </div><!--/.panel panel-default -->

    <g:render template="showExtendedPoliticianTemplates/politicianLastActivity" model="[politician: politician]"/>
</content>

<content tag="cColumn">
    <g:render template="showExtendedPoliticianTemplates/columnC/socialButtonsExtendedPoliticianColumnC" model="[politician:politician]"/>
    <g:render template="showExtendedPoliticianTemplates/columnC/contactPolitician" model="[politician:politician]"/>
    <g:render template="showExtendedPoliticianTemplates/columnC/leaningIndex" model="[politician:politician]"/>
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