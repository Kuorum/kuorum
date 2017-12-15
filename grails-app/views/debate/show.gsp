<%@ page import="org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="debate.show.title" args="[debate.title]"/></title>
    <g:set var="schemaData" value="${[schema:'http://schema.org/Article', name:debate.title]}" scope="request"/>
    <meta name="layout" content="columnCLayout">
    <g:render template="debateMetaTags" model="[debate: debate]"/>
    <r:require modules="debate"/>
</head>

<content tag="mainContent">
    <g:render template="showModules/mainContent" model="[debate: debate, debateUser: debateUser,proposalPage:proposalPage,eventData:eventData,eventRegistration:eventRegistration]" />
</content>

<content tag="cColumn">
    <g:if test="${debate.event}">
        <r:require modules="event"/>
        <g:render template="/campaigns/columnCModules/eventCallToAction" model="[debate: debate, debateUser: debateUser,event:debate.event,eventRegistration:eventRegistration]"/>
        <g:render template="showModules/cCallToAction" model="[debate: debate, debateUser: debateUser,proposalPage:proposalPage]"/>
        <g:render template="/campaigns/columnCModules/eventInfo" model="[event:debate.event]"/>
    </g:if>
    <g:else>
        <g:if test="${debate.campaignStatusRSDTO == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT}">
            <g:render template="showModules/cCallToAction" model="[debate: debate, debateUser: debateUser,proposalPage:proposalPage]"/>
        </g:if>
    </g:else>

    <g:if test="${pinnedUsers}">
        <g:render template="/debate/showModules/pinnedUsers" model="[pinnedUsers:pinnedUsers, debateUser: debateUser]" />
    </g:if>
    <g:render template="showModules/cColumn" model="[debate: debate, debateUser: debateUser, proposalPage:proposalPage, lastActivity:lastActivity]" />
</content>

