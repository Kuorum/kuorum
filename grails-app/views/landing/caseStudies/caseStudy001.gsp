<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="landingLeaders.head.title"/></title>
    <meta name="layout" content="individualCaseStudyLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'landingLeaders.head.title'),
                      kuorumDescription:g.message(code:'landingLeaders.head.description'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images/landing', file:'leaders-01.jpg')
              ]"/>
</head>

<content tag="breadcrumb">
    <g:link mapping="landingServices" class="breadcrumb">
        <g:message code="landingCaseStudy.page.breadcrumb.home"/>
    </g:link>
    >
    <g:link mapping="landingCaseStudy" class="breadcrumb">
        <g:message code="landingCaseStudy.page.breadcrumb.casesStudy"/>
    </g:link>
</content>

<content tag="main">
    <g:render template="/landing/commonModules/slider" model="[msgPrefix:'caseStudy'+caseStudyId]"/>
    <g:render template="/dashboard/landingOrganizationsModules/modalRequestDemo"/>
</content>

<content tag="caseStudyBody">
    <h1><g:message code="landingCaseStudy.${caseStudyId}.content.title"/> </h1>
    <p><g:message code="landingCaseStudy.001.content.text.1"/> </p>
    <p><g:message code="landingCaseStudy.001.content.text.2"/> </p>
    %{--<iframe width="560" height="315" src="https://www.youtube.com/embed/GcvNO1YlNCg" frameborder="0" allowfullscreen></iframe>--}%
    <image:showYoutube youtube="https://www.youtube.com/embed/GcvNO1YlNCg"/>
    <p><g:message code="landingCaseStudy.001.content.text.3"/> </p>
    <g:render template="/landing/caseStudies/modules/requestCaseStudy" model="[caseStudyId:caseStudyId]"/>
</content>

<content tag="casesStudyGrid">
    <g:render template="/landing/caseStudies/modules/otherCases" model="[otherCases:suggestedCaseStudies, sectionName:'landingCaseStudy']"/>
    <div class="row section-body request-demo">
        <div id="request-demo-btn" class="col-md-4 col-md-offset-4">
            <a href="#" class="btn btn-lg btn-orange btn-sign-up btn-open-modal-request-demo"><g:message code="landingCaseStudy.page.requestDemo"/> </a>
        </div>
    </div>
</content>