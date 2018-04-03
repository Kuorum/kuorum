<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="footerBlog.${blogId}.head.title"/></title>
    <meta name="layout" content="individualCaseStudyLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'footerBlog.'+blogId+'.head.title'),
                      kuorumDescription:g.message(code:'footerBlog.head.'+blogId+'.card.subtitle'),
                      kuorumImage:r.resource(dir:'images/landing/footerBlog/'+blogId, file:'card.jpg', absolute:true)
              ]"/>
</head>

<content tag="breadcrumb">
    <g:link mapping="landingServices" class="breadcrumb">
        <g:message code="landingCaseStudy.page.breadcrumb.home"/>
    </g:link>
    >
    <g:link mapping="footerBlog" class="breadcrumb">
        <g:message code="footerBlog.page.breadcrumb.listBlogs"/>
    </g:link>
</content>

<content tag="main">
    <g:render template="/landing/commonModules/slider" model="[msgPrefix:'footerBlog'+blogId]"/>
    <g:render template="/dashboard/landingOrganizationsModules/modalRequestDemo"/>
</content>

<content tag="caseStudyBody">
    <h1><g:message code="footerBlog.${blogId}.content.title"/> </h1>
    <p><g:message code="footerBlog.${blogId}.content.text.1"/> </p>
    <p><g:message code="footerBlog.${blogId}.content.text.2"/> </p>
    <p><g:message code="footerBlog.${blogId}.content.text.3"/> </p>
    <p><g:message code="footerBlog.${blogId}.content.text.4"/> </p>
    <p><g:message code="footerBlog.${blogId}.content.text.5"/> </p>
    <p><g:message code="footerBlog.${blogId}.content.text.6"/> </p>
    <p><g:message code="footerBlog.${blogId}.content.text.7"/> </p>
</content>

<content tag="casesStudyGrid">
    <g:render template="/landing/caseStudies/modules/otherCases" model="[otherCases:suggestedBlogs, sectionName:'footerBlog']"/>
</content>