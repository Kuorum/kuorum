<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>Check in</title>
    <meta name="layout" content="basicPlainLayout">

</head>

<content tag="mainContent">

    <div class="box-ppal edit-contact-header clearfix" itemscope itemtype="http://schema.org/Person">
        <div>
            <span class="user-img"><img src="${image.contactImgSrc(contact:contact)}" alt="${contactName} " itemprop="image"></span>
            <h2 class="event-confirm-success">
                <g:message code="event.callToAction.success.text"/> <span class="far fa-check"></span>
            </h2>
            <h3 class="title">
                ${contact.name} ${contact.surname?:''}
            </h3>
            <p class="email">
                <contactUtil:printContactMail contact="${contact}"/>
            </p>
            <p class="followers">
                <span class="fal fa-users"></span><g:message code="tools.contact.list.contact.followers" args="[contact.numFollowers]"/>
            </p>
        </div>
    </div>
</content>