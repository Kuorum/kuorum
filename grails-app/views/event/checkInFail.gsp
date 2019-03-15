<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>Check in ERROR</title>
    <meta name="layout" content="basicPlainLayout">

</head>

<content tag="mainContent">

    <div class="box-ppal edit-contact-header clearfix" itemscope itemtype="http://schema.org/Person">
        <div>
            <span class="user-img"><img src="${image.contactImgSrc(contact:contact)}" alt="${contact.name} " itemprop="image"></span>
            <h2 class="event-confirm-error">
                <g:message code="event.checkIn.error.text"/> <span class="fas fa-times-octagon"></span>
            </h2>
            <h3 class="title">
                ${contact.name} ${contact.surname?:''}
            </h3>
        </div>
    </div>
</content>