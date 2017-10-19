<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="footerBlog.${blogId}.head.title"/></title>
    <meta name="layout" content="individualCaseStudyLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'footerBlog.'+blogId+'.head.title'),
                      kuorumDescription:g.message(code:'footerBlog.head.'+blogId+'.card.subtitle'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images/landing', file:'leaders-01.jpg')
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
    <p><g:message code="footerBlog.${blogId}.content.text.3" args="[createLink(mapping:'register')]"/> </p>
    <image:showYoutube youtube="https://www.youtube.com/watch?v=sitwHMFeF30"/>
</content>

<content tag="casesStudyGrid">
    <g:render template="/landing/caseStudies/modules/otherCases" model="[otherCases:suggestedBlogs, sectionName:'footerBlog']"/>
</content>