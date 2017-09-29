<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="landingLeaders.head.title"/></title>
    <meta name="layout" content="landingLeadersLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'landingLeaders.head.title'),
                      kuorumDescription:g.message(code:'landingLeaders.head.description'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images/landing', file:'leaders-01.jpg')
              ]"/>
</head>

<content tag="main">
    <g:render template="/dashboard/landingLeaders/leadersCarousel" model="[command:command, msgPrefix:'landingLeaders', siteKey:siteKey]"/>
</content>

<content tag="howItWorks">
    <g:render template="/dashboard/landingLeaders/howItWorks" model="[command:command, msgPrefix:'landingLeaders']"/>
</content>

<content tag="engage">
    <g:render template="/dashboard/landingLeaders/leadersFeaturesCustom" model="[msgPrefix:'landingLeaders', imgBackground:'features-leaders.png']"/>
</content>

<content tag="organizations">
    <g:render template="/dashboard/landingLeaders/leadersTrustUs"  model=
    "[
            msgPrefix:'landingLeaders',
            users:[[
                           img:'testimony-david-burrowes.jpg',
                           name:'David Burrowes ',
                           logo:'logo-conservatives.png',
                           logoAlt:'Conservatives',
                           quote:g.message(code: 'landingLeaders.trustUs.user1.quote'),
                           role:g.message(code: 'landingLeaders.trustUs.user1.role')
                   ],[
                           img:'testimony-cat-smith.jpg',
                           name:'Cat Smith ',
                           logo:'logo-labour.png',
                           logoAlt:'Labour',
                           quote:g.message(code: 'landingLeaders.trustUs.user2.quote'),
                           role:g.message(code: 'landingLeaders.trustUs.user2.role')
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