<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="footerAboutUs.head.title"/></title>
    <meta name="layout" content="landingFooterLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'footerAboutUs.head.title'),
                      kuorumDescription:g.message(code:'footerAboutUs.head.description'),
                      kuorumImage:r.resource(dir:'images/landing', file:'what-is-kuorum.jpg', absolute:true)
              ]"/>
</head>

<content tag="main">
    <g:render template="/landing/commonModules/slider" model="[msgPrefix:'footerAboutUs']"/>
</content>

<content tag="footerLeftColumn">
    <g:render template="/footer/footerModules/leftColumn" model="[ msgPrefix:'footerAboutUs']"/>
</content>

<content tag="footerSection">
    <div class="section-header">
        <p><g:message code="footerAboutUs.content.text.1"/></p>
        <p><g:message code="footerAboutUs.content.text.2"/></p>
        <p><g:message code="footerAboutUs.content.text.3" args="[createLink(mapping:'landingCaseStudy')]"/></p>
    </div>
</content>