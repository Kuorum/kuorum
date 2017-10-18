<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="footerHistory.head.title"/></title>
    <meta name="layout" content="landingFooterLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'footerHistory.head.title'),
                      kuorumDescription:g.message(code:'footerHistory.head.description'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images/landing', file:'leaders-01.jpg')
              ]"/>
</head>

<content tag="main">
    <g:render template="/landing/commonModules/slider" model="[msgPrefix:'footerHistory']"/>
</content>

<content tag="footerLeftColumn">
    <g:render template="/footer/footerModules/leftColumn" model="[ msgPrefix:'footerHistory']"/>
</content>

<content tag="footerSection">
    <div class="section-header">
        <p><g:message code="footerHistory.content.text.1" /></p>
        <p><g:message code="footerHistory.content.text.2" /></p>
    </div>
</content>