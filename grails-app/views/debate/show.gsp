<%@ page import="org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="titleMessageCode" value="${debate?.event==null?'debate.show.title':'event.show.title'}"/>
    <title><g:message code="${titleMessageCode}" args="[debate.title, _domainName]"/></title>
    <g:set var="schemaData" value="${[schema:'http://schema.org/Article', name:debate.title]}" scope="request"/>
    <meta name="layout" content="columnCLayout">
    <g:render template="/debate/debateMetaTags" model="[debate: debate, titleMessageCode:titleMessageCode]"/>
    <r:require modules="debate, forms"/>
</head>

<content tag="mainContent">
    <g:render template="/debate/showModules/mainContent" model="[debate: debate, debateUser: debateUser,proposalPage:proposalPage,eventData:eventData]" />
    <g:render template="/campaigns/columnCModules/eventCallToAction" model="[eventUser:debateUser,campaign:debate, hideSmallDevices:true]"/>
</content>

<content tag="cColumn">
    <g:if test="${debate.event}">
        <r:require modules="event"/>
        <g:render template="/campaigns/columnCModules/eventCallToAction" model="[eventUser: debateUser,campaign:debate, hideSmallDevices:false]"/>
        <g:render template="/debate/showModules/cCallToAction" model="[debate: debate, debateUser: debateUser,proposalPage:proposalPage]"/>
        <g:render template="/campaigns/columnCModules/eventInfo" model="[event:debate.event, eventUser: debateUser,displayTimeZone:displayTimeZone]"/>
    </g:if>
    <g:else>
        <g:if test="${debate.campaignStatusRSDTO == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT}">
            <g:render template="/debate/showModules/cCallToAction" model="[debate: debate, debateUser: debateUser,proposalPage:proposalPage]"/>
        </g:if>
    </g:else>

    <g:if test="${pinnedUsers}">
        <g:render template="/debate/showModules/pinnedUsers" model="[pinnedUsers:pinnedUsers, debateUser: debateUser]" />
    </g:if>
    <g:render template="/campaigns/columnCModules/campaignFiles" model="[campaignFiles:campaignFiles]"/>
    <g:render template="/debate/showModules/cColumn" model="[debate: debate, debateUser: debateUser, proposalPage:proposalPage, lastActivity:lastActivity,displayTimeZone:displayTimeZone]" />
</content>

