<%@ page import="kuorum.core.model.CommissionType; kuorum.core.model.VoteType; kuorum.core.model.PostType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    %{--<title>${project.shortName}</title>--}%
    <meta name="layout" content="normalLayout">
    <parameter name="extraCssContainer" value="onecol list" />
</head>


<content tag="mainContent">
    <div class="intro">
        <h1><g:message code="list.projects.title"/> </h1>
        <p><g:message code="list.projects.description"/></p>
    </div>

    <div class="block-results">

        <g:each in="${groupProjects}" var="region">
            <h1>
                <g:link class="grey" mapping="projects" params="[regionName:region.key.encodeAsKuorumUrl()]">
                    ${region.key}
                </g:link>
            </h1>
            <g:each in="${region.value}" var="group">
                <g:set var="i18nCommission" value="${message(code:"${CommissionType.canonicalName}.${group.commission}")}"/>
                <!-- is necesary toLowerCase because i18nCommission.encode doens't work -->
                <g:set var="i18nCommissionUrl" value="${i18nCommission.toLowerCase().encodeAsKuorumUrl()}"/>
                <h3>
                    <g:link class="grey" mapping="projects" params="[regionName:region.key.encodeAsKuorumUrl(), commission:i18nCommissionUrl]">
                        Proyectos sobre ${i18nCommission.toLowerCase()}
                    </g:link>
                </h3>
                <ul>
                    <g:each in="${group.elements}" var="project">
                        <li> <g:link mapping="projectShow" params="${project.encodeAsLinkProperties()}">${project.name}</g:link> </li>
                    </g:each>
                </ul>
            </g:each>

        </g:each>
</content>