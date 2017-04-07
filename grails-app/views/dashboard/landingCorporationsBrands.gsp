<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="landingCorporationsBrands.head.title"/></title>
    <meta name="layout" content="landingLeadersLayout">
    <parameter name="transparentHead" value="true"/>
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'landingCorporationsBrands.head.title'),
                      kuorumDescription:g.message(code:'landingCorporationsBrands.head.description'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images/landing', file:'leaders-01.jpg')
              ]"/>
</head>

<content tag="main">
    <g:render template="/dashboard/landingLeaders/leadersCarousel" model="[command:command, msgPrefix:'landingCorporationsBrands']"/>
    <g:render template="/dashboard/landingOrganizationsModules/modalRequestDemo"/>
</content>

<content tag="howItWorks">
    <g:render template="/dashboard/landingLeaders/howItWorks" model="[command:command, msgPrefix:'landingCorporationsBrands']"/>
</content>

<content tag="engage">
    <g:render template="/dashboard/landingLeaders/leadersFeaturesCustom" model="[msgPrefix:'landingCorporationsBrands', imgBackground:'features-corporations.png']"/>
</content>

<content tag="organizations">
    <g:render template="/dashboard/landingLeaders/leadersTrustUs"  model=
    "[
            msgPrefix:'landingCorporationsBrands',
            users:[[
                           img:'testimony-miguel-ferrer.png',
                           name:'Miguel Ferrer ',
                           logo:'logo-kreab.png',
                           logoAlt:'Kreab',
                           quote:g.message(code: 'landingCorporations.trustUs.user2.quote'),
                           role:g.message(code: 'landingCorporations.trustUs.user2.role')
                   ],[
                           img:'testimony-veronica-respaldiza.jpg',
                           name:'Verónica Respaldiza ',
                           logo:'logo-ibm.png',
                           logoAlt:'IBM',
                           quote:g.message(code: 'landingCorporations.trustUs.user1.quote'),
                           role:g.message(code: 'landingCorporations.trustUs.user1.role')
                   ]
            ]
    ]"/>
</content>

<content tag="caseStudy">
    <g:render template="/dashboard/landingLeaders/leadersDownloadCaseStudy" model="[command:command]"/>
</content>

<content tag="features">
    <g:render template="/dashboard/landingLeaders/leadersFeaturesGeneric" model="[msgPrefix:'landingCorporationsBrands']"/>
</content>