<%@ page import="org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${debate.title}</title>
    <meta name="layout" content="columnCLayout">
    <g:render template="debateMetaTags" model="[debate: debate]"/>
    <r:require modules="debate"/>


</head>

<content tag="mainContent">
    <g:render template="showModules/mainContent" model="[debate: debate, debateUser: debateUser,proposalPage:proposalPage]" />
</content>

<content tag="cColumn">
    <g:if test="${debate.campaignStatusRSDTO == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT}">
        <g:render template="showModules/cCallToAction" model="[debate: debate, debateUser: debateUser,proposalPage:proposalPage]"/>
    </g:if>
    <g:render template="showModules/cColumn" model="[debate: debate, debateUser: debateUser, proposalPage:proposalPage, lastActivity:lastActivity]" />
    <g:if test="${pinnedUsers}">
        <g:render template="/debate/showModules/pinnedUsers" model="[pinnedUsers:pinnedUsers, debateUser: debateUser]" />
    </g:if>
</content>

