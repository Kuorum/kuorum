<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="landingLeaders.head.title"/></title>
    <meta name="layout" content="landingServicesLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'landingLeaders.head.title'),
                      kuorumDescription:g.message(code:'landingLeaders.head.description'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images/landing', file:'leaders-01.jpg')
              ]"/>
</head>

<content tag="main">
    <g:render template="/landing/servicesModules/leadersCarousel" model="[command:command, msgPrefix:'landingServices', siteKey:siteKey]"/>
</content>

<content tag="howItWorks">
    <g:render template="/landing/servicesModules/services" model="[command:command, msgPrefix:'landingServices']"/>
</content>

<content tag="caseStudy">
    <g:render template="/landing/servicesModules/caseStudy" model="[command:command, msgPrefix:'landingServices']"/>
</content>

<content tag="statistics">
    <g:render template="/landing/servicesModules/statistics" model="[msgPrefix:'landingServices']"/>
</content>

<content tag="trustUs">
    <g:render template="/landing/servicesModules/trustUs" model="[msgPrefix:'landingServices']"/>
</content>

<content tag="solutions">
    <g:render template="/landing/servicesModules/solutions" model="[msgPrefix:'landingServices']"/>
</content>

<content tag="engage">
    <g:render template="/dashboard/landingLeaders/leadersFeaturesCustom" model="[msgPrefix:'landingServices', imgBackground:'features-leaders.png']"/>
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

<content tag="features">
    <g:render template="/dashboard/landingLeaders/leadersFeaturesGeneric"/>
</content>