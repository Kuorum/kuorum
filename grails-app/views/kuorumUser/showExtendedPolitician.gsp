<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${politician.name}</title>
    <meta name="layout" content="columnCLayout">
    <parameter name="specialContainerCssClass" value="userprofile" />
    <parameter name="specialMainContentCssClass" value="politician-card" />
    <g:render template="userMetaTags" model="[user:politician]"/>
</head>

<content tag="mainContent">
    <div class="panel panel-default">
        <div class='profile-header'>
            <sec:ifAnyGranted roles="ROLE_ADMIN">
                <!-- FLECHITA PARA ABRIR MENÚ -->
                <span class="popover-trigger open-menu" rel="popover" role="button" data-toggle="popover">
                    <span class="fa fa-chevron-down"></span>
                    <span class="sr-only"><g:message code="project.list.show.options"/></span>
                </span>
                <!-- POPOVER OPCIONES EDICIÓN -->
                <div class="popover">
                    <div class="popover-more-actions edition">
                        <ul>
                            <li>
                                <g:link mapping="adminEditUser" params="${politician.encodeAsLinkProperties()}">
                                    <span><g:message code="project.editMenu.edit"/></span>
                                </g:link>
                            </li>
                        </ul>
                    </div>
                </div>
                <!-- FIN POPOVER OPCIONES EDICIÓN -->
            </sec:ifAnyGranted>
            <img src="${image.userImgProfile(user:politician)}" alt="${politician.name}">

        </div>
        <div class="panel-body">
            <div class="row">
                <div class="col-xs-12 profile-pic-col">
                    <div class="profile-pic text-center">
                        <img alt="${politician.name}"
                             class="img-circle"
                             data-src="holder.js/140x140"
                             src="${image.userImgSrc(user:politician)}"
                             data-holder-rendered="true">
                        %{--<i class="fa fa-check"></i>--}%
                    </div>
                </div>
            </div><!--/.row -->
            <div class="row extra-padding">
                <div class='col-xs-12 col-sm-8 profile-title'>
                    <h2>${politician.name}</h2>
                    <cite><userUtil:politicianPosition user="${politician}"/></cite>
                    <p class='party'>${userUtil.roleName(user:politician)}</p>
                </div>
                <div class="col-xs-12 col-sm-4 follow-btn-group">
                    <userUtil:followButton user="${politician}" cssExtra="inverted"/>
                    <userUtil:contactButton user="${politician}" cssExtra="inverted"/>
                </div>
            </div>
            <div class="extra-padding text-left following">
                %{--<g:message code="kuorumUser.show.module.followers.title" args="[politician.numFollowers]"/>--}%
                <userUtil:ifIsFollower user="${politician}">
                    <i class="fa fa-check-circle-o"></i><g:message code="kuorumUser.popover.follower"/>
                </userUtil:ifIsFollower>
            </div>
            <div class="extra-padding" id="politician-bio">
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
                            <li>
                                <g:if test="${relevantEvent.url}">
                                    <a href="${relevantEvent.url}" target="_blank">
                                        ${relevantEvent.title}
                                    </a>
                                </g:if>
                                <g:else>
                                    ${relevantEvent.title}
                                </g:else>
                            </li>
                        </g:each>
                    </ul>
                </g:if>
                <h4><g:message code="politician.bio"/></h4>
                <p class="limit-height-bio">${politician.bio}</p>
                </div><!--/.extra-padding -->
        </div><!--/.panel-body-->
    </div><!--/.panel panel-default -->

    <g:render template="showExtendedPoliticianTemplates/politicianLastActivity" model="[politician: politician]"/>
</content>

<content tag="cColumn">
    <g:render template="showExtendedPoliticianTemplates/columnC/socialButtonsExtendedPoliticianColumnC" model="[user:politician]"/>
    %{--<g:render template="showExtendedPoliticianTemplates/columnC/contactPolitician" model="[politician:politician]"/>--}%
    <g:render template="showExtendedPoliticianTemplates/columnC/leaningIndex" model="[politician:politician]"/>
    <div id="extendedPolitician"><g:render template="/modules/recommendedUsers" model="[recommendedUsers:recommendPoliticians]"/></div>
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
    <g:render template="showExtendedPoliticianTemplates/modals/modalContact" model="[politician:politician]"/>
</content>