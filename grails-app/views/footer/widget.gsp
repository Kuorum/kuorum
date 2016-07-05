<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.widget"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'page.title.footer.widget'),
                      kuorumDescription:g.message(code:'page.title.footer.widget.description'),
                      kuorumImage:r.resource(dir:'images', file:'landingSearch-rrss.png', absolute:true)
              ]"/>
    <link rel="canonical" href="https://${request.serverName}${g.link(mapping: 'footerWidget')}" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenuPress" model="[activeMapping:'footerWidget']"/>
</content>

<content tag="mainContent">
    <h1><g:message code="layout.footer.widget"/></h1>
    <p><g:message code="footer.menu.footerWidget.description1"/></p>
    <p><g:message code="footer.menu.footerWidget.description2"/></p>
    <p><g:message code="footer.menu.footerWidget.description3"/></p>
    <img src="${resource(dir: 'images', file: 'widget-examples.png')}" alt="widget-example" itemprop="image">
    <p></p>
</content>
