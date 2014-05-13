<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    %{--<title>${law.shortName}</title>--}%
    <meta name="layout" content="normalLayout">
    <parameter name="extraCssContainer" value="onecol list" />
</head>


<content tag="mainContent">
    <div class="intro">
        <h1><g:message code="list.citizens.POLITICIAN.title"/> </h1>
        <p><g:message code="list.citizens.POLITICIAN.description"/></p>
    </div>

    <div class="block-results">

        <g:each in="${groupPoliticians}" var="institution">
            <h1>
                <g:link mapping="politicians" params="[institutionName:institution.key]">
                    ${institution.key}
                </g:link>
            </h1>
            <g:each in="${institution.value}" var="group">
                <h3>
                    <g:link mapping="politicians" params="[regionName:institution.key, regionIso3166_2:group.iso3166_2]">
                        <g:message code="list.citizens.POLITICIAN.byRegion" args="[group.regionName]"/>
                    </g:link>
                </h3>
                <ul>
                    <g:each in="${group.politicians}" var="user">
                        <li> <g:link mapping="userShow" params="${user.encodeAsLinkProperties()}">${user.name}</g:link> </li>
                    </g:each>
                </ul>
            </g:each>

        </g:each>
</content>