<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="contest.ranking.title" args="[contest.title]"/></title>
    <meta name="layout" content="basicPlainLayout">

    <meta itemprop="name" content="${_domainName}">
    <r:require modules="rankingList"/>
    <g:set var="rankingReady"
           value="${[org.kuorum.rest.model.communication.contest.ContestStatusDTO.VOTING, org.kuorum.rest.model.communication.contest.ContestStatusDTO.RESULTS].contains(contest.status)}"/>
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="campaignShow" params="${contest.encodeAsLinkProperties()}">${contest.title}</g:link></li>
        <li class="active"><g:message code="contest.ranking.breadcrumb.ranking"/></li>
    </ol>

    <g:if test="${rankingReady}">
        <g:render template="ranking/rankingTable" model="[contest: contest]"/>
    </g:if>
    <g:else>
        <div class="box-ppal list-campaigns">
            <g:message code="contest.ranking.noReady"/>
        </div>
    </g:else>

</content>