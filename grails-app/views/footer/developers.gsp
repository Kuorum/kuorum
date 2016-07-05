<%@ page import="kuorum.core.model.UserType; kuorum.users.KuorumUser" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.developers"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'page.title.footer.editors'),
                      kuorumDescription:g.message(code:'page.title.footer.editors.description'),
                      kuorumImage:r.resource(dir:'images', file:'landingSearch-rrss.png', absolute:true)
              ]"/>
    <link rel="canonical" href="https://${request.serverName}${g.createLink(mapping: 'footerDevelopers')}" />

</head>

<content tag="leftMenu">
    <g:render template="leftMenuGuide" model="[activeMapping:'footerDevelopers']"/>
</content>

<content tag="mainContent">
    <h1><g:message code="layout.footer.editors"/></h1>
    <p><g:message code="footer.menu.footerWhatIsKuorum.forEditors.description1"/></p>
    <p><g:message code="footer.menu.footerWhatIsKuorum.forEditors.description2" args="[Math.round(kuorum.users.KuorumUser.countByUserType(kuorum.core.model.UserType.POLITICIAN)/1000)*1000]"/></p>
    <p><g:message code="footer.menu.footerWhatIsKuorum.forEditors.description3" args="[g.createLink(mapping:'landingCitizens', absolute: true)]"/></p>
    <p><img src="${resource(dir: 'images', file: 'ipdb-kuorum.png')}" alt="we-build-transparency" itemprop="image"></p>
</content>
