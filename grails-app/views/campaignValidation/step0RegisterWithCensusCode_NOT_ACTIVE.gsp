<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login" args="[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name]"/> </title>
    <meta name="layout" content="centerColumn1Layout">
    <parameter name="showHeadSearch" value="false"/>
    <parameter name="showNavBar" value="false"/>
    <parameter name="extraHeadCss" value="landing"/>
    <parameter name="disableLogoLink" value="true"/>
    <r:require modules="forms"/>
</head>

<content tag="title">
    <g:message code="kuorum.web.commands.profile.directCensusLogin.notActive.title"/>
</content>

<content tag="mainContent">
        <fieldset class="row">
            <div class="form-group col-xs-12 center">
                <g:if test="${campaign.startDate}">
                    <g:set var="timeToStart"><kuorumDate:humanDate date="${campaign.startDate}" itemprop="datePublished" id="reloadCountDown"/></g:set>
                    <p><g:message code="kuorum.web.commands.profile.directCensusLogin.notActive.intro" args="[contact.name, timeToStart]" encodeAs="raw"/></p>
                    <div class="event-date-time">
                        <p class="strong">
                            <g:formatDate formatName="default.date.format.long"  date="${campaign.startDate}" timeZone="${campaign.user.timeZone}"/>
                        </p>
                        <p class="strong">
                            <g:formatDate type="time" style="SHORT"  date="${campaign.startDate}" timeZone="${campaign.user.timeZone}"/>
                            <kuorumDate:printTimeZoneName date="${campaign.startDate}" zoneInfo="${campaign.user.timeZone}"/>
                        </p>
                    </div>
                </g:if>
            </div>
        </fieldset>
    <r:script>
        function reloadAfterTime(){
            var limit = new Date($("#reloadCountDown").attr("datetime"));
            var dif = new Date().getTime() - limit.getTime();
            if (dif > 0){
                location.reload();
            }
        }
        var t=setInterval(reloadAfterTime,1000*30);
    </r:script>
</content>

