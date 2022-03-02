<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title><g:message code="customRegister.stepDomainValidation.title"/></title>
    <meta name="layout" content="centerColumn1Layout">
    <parameter name="showHeadSearch" value="false"/>
    <parameter name="showNavBar" value="false"/>
    <parameter name="extraHeadCss" value="landing"/>
    <parameter name="disableLogoLink" value="true"/>

</head>

<content tag="mainContent">
    <div class="container-fluid politician-messages box-ppal">
        <div class="form-group col-xs-12 center">
            <h1><g:message code="campaign.closed.token.error.title"/></h1>
        </div>

        <div class="form-group col-xs-12 center">
            <h3><g:message code="campaign.closed.token.error.message" args="[contact.name, campaign.endDate]"/></h3>
        </div>
    </div>

    <div class="link-wrapper" id="campaign-${campaign.id}" data-datepublished="${campaign.datePublished.time}">
        <g:link mapping="surveyShow" params="${campaign.encodeAsLinkProperties()}" class="hidden"></g:link>
        <div class="card-header-photo">
            ${campaign.title}
            <g:if test="${campaign.photoUrl}">
                <img src="${campaign.photoUrl}" alt="${campaign.title}">
            </g:if>
            <g:elseif test="${campaign.videoUrl}">
                <image:showYoutube youtube="${campaign.videoUrl}" campaign="${campaign}"/>
            </g:elseif>
            <g:else>
                <div class="imagen-shadowed-main-color-domain">
                    <img class="empty" src="${g.resource(dir: "images", file: "emptyCampaign.png")}"
                         alt="${campaign.title}"/>
                </div>
            </g:else>
        </div>
    </div>
</content>

</html>