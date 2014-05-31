<%@ page import="kuorum.core.model.CommissionType; kuorum.core.model.VoteType; kuorum.core.model.PostType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    %{--<title>${law.shortName}</title>--}%
    <meta name="layout" content="normalLayout">
    <parameter name="extraCssContainer" value="onecol list" />
</head>


<content tag="mainContent">
    <div class="intro">
        <h1><g:message code="list.laws.title"/> </h1>
        <p><g:message code="list.laws.description"/></p>
        %{--<form id="all-laws" role="form" class="form-inline">--}%
            %{--<div class="form-group">--}%
                %{--<label for="region" class="sr-only">Filtrar por región</label>--}%
                %{--<select class="form-control" id="region">--}%
                    %{--<option value="Elegir region">Filtrar por región</option>--}%
                    %{--<option value="Region1">Región 1</option>--}%
                    %{--<option value="Region2">Región 2</option>--}%
                    %{--<option value="Region3">Región 3...</option>--}%
                %{--</select>--}%
            %{--</div>--}%
            %{--<div class="form-group">--}%
                %{--<label for="materia" class="sr-only">Filtrar por materia</label>--}%
                %{--<select class="form-control" id="materia">--}%
                    %{--<option value="Elegir materia">Filtrar por materia</option>--}%
                    %{--<option value="Materia1">Región 1</option>--}%
                    %{--<option value="Materia2">Región 2</option>--}%
                    %{--<option value="Materia3">Región 3...</option>--}%
                %{--</select>--}%
            %{--</div>--}%
        %{--</form>--}%
    </div>

    <div class="block-results">

        <g:each in="${groupLaws}" var="institution">
            <h1>
                <g:link mapping="laws" params="[institutionName:institution.key.encodeAsKuorumUrl()]">
                    ${institution.key}
                </g:link>
            </h1>
            <g:each in="${institution.value}" var="group">
                <g:set var="i18nCommission" value="${message(code:"${CommissionType.canonicalName}.${group.commission}")}"/>
                <!-- is necesary toLowerCase because i18nCommission.encode doens't work -->
                <g:set var="i18nCommissionUrl" value="${i18nCommission.toLowerCase().encodeAsKuorumUrl()}"/>
                <h3>
                    <g:link mapping="laws" params="[institutionName:institution.key.encodeAsKuorumUrl(), commission:i18nCommissionUrl]">
                        Leyes sobre ${i18nCommission.toLowerCase()}
                    </g:link>
                </h3>
                <ul>
                    <g:each in="${group.elements}" var="law">
                        <li> <g:link mapping="lawShow" params="${law.encodeAsLinkProperties()}">${law.name}</g:link> </li>
                    </g:each>
                </ul>
            </g:each>

        </g:each>
</content>