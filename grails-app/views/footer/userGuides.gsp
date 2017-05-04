<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.userGuides"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info team" />
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'page.title.footer.userGuides'),
                      kuorumDescription:g.message(code:'page.title.footer.userGuides.description'),
                      kuorumImage:r.resource(dir:'images', file:'landingSearch-rrss.png', absolute:true)
              ]"/>
</head>

<content tag="leftMenu">
    <g:render template="leftMenuGuide" model="[activeMapping:'footerUserGuides']"/>
</content>

<content tag="mainContent">
    <ul class="list-team">
        <li>
            <div class="box-ppal">
                <a href="https://s3-eu-west-1.amazonaws.com/kuorumorg/static/users/KuorumUserGuide1(${langGuides}).pdf" target="_blank">
                    <img class="guide" src="${resource(dir: 'images', file: 'guide1(eng).png')}" alt="Guide I" itemprop="image">
                </a>
                <p itemprop="description"><g:message code="footer.menu.userGuide1.description"/></p>
            </div>
        </li>
        <li>
            <div class="box-ppal">
                <a href="https://s3-eu-west-1.amazonaws.com/kuorumorg/static/users/KuorumUserGuide2(${langGuides}).pdf" target="_blank">
                    <img class="guide" src="${resource(dir: 'images', file: 'guide2(eng).png')}" alt="Guide II" itemprop="image">
                </a>
                <p itemprop="description"><g:message code="footer.menu.userGuide2.description"/></p>
            </div>
        </li>
    </ul>
</content>
