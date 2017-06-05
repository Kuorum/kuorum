<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="landingOrganizations.head.title"/></title>
    <meta name="layout" content="landingLeadersLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'landingOrganizations.head.title'),
                      kuorumDescription:g.message(code:'landingOrganizations.head.description'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images/landing', file:'leaders-01.jpg')
              ]"/>
</head>

<content tag="main">
    <g:render template="/dashboard/landingLeaders/leadersCarousel" model="[command:command, msgPrefix:'landingOrganizations']"/>
</content>

<content tag="howItWorks">
    <g:render template="/dashboard/landingLeaders/howItWorks" model="[command:command, msgPrefix:'landingOrganizations']"/>
</content>

<content tag="engage">
    <g:render template="/dashboard/landingLeaders/leadersFeaturesCustom" model="[msgPrefix:'landingOrganizations', imgBackground:'features-organizations.png']"/>
</content>

<content tag="organizations">
    <g:render template="/dashboard/landingLeaders/leadersTrustUs"  model=
    "[
            msgPrefix:'landingOrganizations',
            users:[[
                           img:'testimony-thai-jungpanich.jpg',
                           name:'Thai Jungpanich ',
                           logo:'logo-oxfam.png',
                           logoAlt:'Oxfam',
                           quote:g.message(code: 'landingOrganizations.trustUs.user1.quote'),
                           role:g.message(code: 'landingOrganizations.trustUs.user1.role')
                   ],[
                           img:'testimony-gabriel-gonzalez.jpg',
                           name:'Gabriel Gonzalez ',
                           logo:'logo-unicef.png',
                           logoAlt:'Unicef',
                           quote:g.message(code: 'landingOrganizations.trustUs.user2.quote'),
                           role:g.message(code: 'landingOrganizations.trustUs.user2.role')
                   ]
            ]
    ]"/>
</content>

<content tag="caseStudy">
    <g:render template="/dashboard/landingLeaders/leadersDownloadCaseStudy" model="[command:command]"/>
</content>

<content tag="features">
    <g:render template="/dashboard/landingLeaders/leadersFeaturesGeneric"/>
</content>