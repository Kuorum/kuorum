<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="footerPress.head.title"/></title>
    <meta name="layout" content="landingFooterLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'footerPress.head.title'),
                      kuorumDescription:g.message(code:'footerPress.head.description'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images/landing', file:'press.jpg')
              ]"/>
</head>

<content tag="main">
    <g:render template="/landing/commonModules/slider" model="[msgPrefix:'footerPress']"/>
</content>

<content tag="footerLeftColumn">
    <g:render template="/footer/footerModules/leftColumn" model="[ msgPrefix:'footerPress']"/>
</content>

<content tag="footerSection">

    <g:set var="docSendLink" value="https://docsend.com/view/ux5z5yc"/>
    <g:if test="${lang=='es'}">
        <g:set var="docSendLink" value="https://docsend.com/view/hfn672m"/>
    </g:if>

    <div class="section-header">
        <p><g:message code="footerPress.content.text" args="[docSendLink, createLink(mapping:'footerContactUs')]"/></p>
    </div>


</content>