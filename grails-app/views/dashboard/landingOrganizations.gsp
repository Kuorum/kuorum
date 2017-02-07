<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="landingLeaders.head.title"/></title>
    <meta name="layout" content="landingLeadersLayout">
    <parameter name="transparentHead" value="true"/>
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
    <g:render template="/dashboard/landingLeaders/howItWorks"/>
</content>

<content tag="engage">
    <g:render template="/dashboard/landingLeaders/leadersFeaturesCustom" model="[msgPrefix:'landingOrganizations', imgBackground:'features-organizations.png']"/>
</content>

<content tag="organizations">
    <g:render template="/dashboard/landingLeaders/leadersTrustUs"  model=
    "[
            msgPrefix:'landingOrganizations',
            users:[[
                           img:'david-burrowes.jpg',
                           name:'David Burrowes ',
                           logo:'logo-conservatives.png',
                           logoAlt:'Conservatives',
                           quote:g.message(code: 'landingCorporations.trustUs.user1.quote'),
                           role:g.message(code: 'landingCorporations.trustUs.user1.role')
                   ],[
                           img:'gabriel-gonzalez.jpg',
                           name:'Gabriel Gonzalez ',
                           logo:'logo-unicef.png',
                           logoAlt:'Unicef',
                           quote:g.message(code: 'landingCorporations.trustUs.user2.quote'),
                           role:g.message(code: 'landingCorporations.trustUs.user2.role')
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