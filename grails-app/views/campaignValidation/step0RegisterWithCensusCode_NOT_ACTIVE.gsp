<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="customRegister.stepDomainValidation.title"/></title>
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
    <div class="signup stepDomainValidation count-down">
        <fieldset aria-live="polite" class="row">
            <div class="form-group col-xs-12 center">
                <g:if test="${campaign.startDate}">
                    <p class="count-down-info"><g:message code="kuorum.web.commands.profile.directCensusLogin.notActive.intro" args="[campaign.title]" encodeAs="raw"/></p>
                    <kuorumDate:printCountDown date="${campaign.startDate}" id="reloadCountDown" cssClass="count-down-timer"/>
                    <g:set var="campaignOpenDate"><g:formatDate formatName="default.date.format.long"  date="${campaign.startDate}" timeZone="${campaign.user.timeZone}"/></g:set>
                    <g:set var="campaignOpenTime"><g:formatDate type="time" style="SHORT"  date="${campaign.startDate}" timeZone="${campaign.user.timeZone}"/> <kuorumDate:printTimeZoneName date="${campaign.startDate}" zoneInfo="${campaign.user.timeZone}"/></g:set>
                    <p class="count-down-footer"><g:message code="kuorum.web.commands.profile.directCensusLogin.notActive.footer" args="[campaignOpenDate, campaignOpenTime]" encodeAs="raw"/></p>
                </g:if>
            </div>
        </fieldset>
    </div>
    <r:script>
        function reloadAfterTime(){
            var limit = new Date($("#reloadCountDown").attr("datetime"));
            var diff = limit.getTime() - (new Date().getTime())
            if (diff < 0){
                pageLoadingOn("End census login countdown")
                clearInterval(countDownInterval)
                location.reload();
            }else{
                var diffSeconds = Math.floor(diff / 1000) % 60;
                var diffMinutes = Math.floor((diff) / (60 * 1000)) % 60;
                var diffHours = Math.floor(diff / (60 * 60 * 1000)) % 24;
                var diffDays = Math.floor(diff / (24 * 60 * 60 * 1000));
                var totalHours = diffDays*24+diffHours;
                var printableSeconds = ('00'+diffSeconds).slice(-2);
                var printableMins = ('00'+diffMinutes).slice(-2);
                var printableHours = totalHours;
                if (totalHours <100){
                    printableHours = ('00'+totalHours).slice(-2);
                }
                var countDownText = printableHours+":"+printableMins+":"+printableSeconds;
                $("#reloadCountDown").html(countDownText)
            }
        }
        var countDownInterval=setInterval(reloadAfterTime,1000);
    </r:script>
</content>

