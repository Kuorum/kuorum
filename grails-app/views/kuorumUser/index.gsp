<%@ page import="kuorum.core.model.CommissionType; kuorum.core.model.VoteType; kuorum.core.model.PostType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    %{--<title>${project.shortName}</title>--}%
    <meta name="layout" content="normalLayout">
    <parameter name="extraCssContainer" value="onecol list" />
</head>


<content tag="mainContent">
    <div class="intro">
        <h1><g:message code="list.citizens.${userType}.title"/> </h1>
        <p><g:message code="list.citizens.${userType}.description"/></p>
    </div>

    <div class="block-results">
        <h1><g:message code="list.citizens.${userType}.title.byRegion"/> </h1>
        %{--<g:each in="${users}" var="user">--}%
            %{--<h3>--}%
                %{--<g:link mapping="users" params="${userType.encodeAsLinkProperties() + [iso3166_2:group.iso3166_2]}">--}%
                    %{--${group.regionName}--}%
                %{--</g:link>--}%
            %{--</h3>--}%
                <ul>
                    <g:each in="${users}" var="user">
                        <li>
                            <g:link mapping="userShow" params="${user.encodeAsLinkProperties()}">
                                ${user.name}
                            </g:link>
                        </li>
                    </g:each>
                </ul>
        %{--</g:each>--}%
</content>