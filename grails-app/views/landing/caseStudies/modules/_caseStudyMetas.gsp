<title><g:message code="landingCaseStudy.${caseStudyId}.content.title"/></title>
<g:render template="/dashboard/landingMetaTags"
          model="[
                  kuorumTitle:g.message(code:'landingCaseStudy.'+caseStudyId+'.content.title'),
                  kuorumDescription:g.message(code:'landingCaseStudy.'+caseStudyId+'.card.subtitle'),
                  kuorumImage:r.resource(dir:'images/landing/landingCaseStudy/'+caseStudyId+'/', file:'card.jpg', absolute:true)
          ]"/>
