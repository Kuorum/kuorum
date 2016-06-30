<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.landing.editor"/></title>
    <meta name="layout" content="landingLayout">
    <parameter name="transparentHead" value="true"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'page.title.landing.editor'),
                      kuorumDescription:g.message(code:'page.title.landing.editor.description'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images', file:'background-editors.jpg')
              ]"/>
</head>

<content tag="videoAndRegister">
    <section id="main" role="main" class="landing mainEditors clearfix">
        <g:render template="landingEditors/videoAndRegister"
                  model="[command:command,
                          title:g.message(code:'landingEditors.videoAndRegister.title'),
                          subtitle:g.message(code:'landingEditors.videoAndRegister.subTitle'),
                  ]"/>
    </section>
</content>

<content tag="logos">
    <g:render template="landingEditors/editorsLogos"/>
</content>

<content tag="mainContent">
    <g:render template="landingEditors/ipdbDescription"/>
</content>
