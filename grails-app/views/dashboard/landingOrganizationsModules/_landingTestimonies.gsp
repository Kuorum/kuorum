

<ul class="testimonies-list clearfix">
    <li>
        <g:render template="/dashboard/landingPageModules/landingTestimony"
                  model="[
                          image:g.resource(dir:'images', file:'catSmith.jpg'),
                          name:'Cat Smith',
                          testimony:g.message(code: 'landingPage.testimonies.catSmith.testimony'),
                          position:g.message(code: 'landingPage.testimonies.catSmith.position'),
                          alt:g.message(code: 'landingPage.testimonies.catSmith.alt')
                  ]"/>
    </li>
    <li>
        <g:render template="/dashboard/landingPageModules/landingTestimony"
                  model="[
                          image:g.resource(dir:'images', file:'gabrielGonzalez.png'),
                          name:'David Burrowes',
                          testimony:g.message(code: 'landingPage.testimonies.gabrielGonzalez.testimony'),
                          position:g.message(code: 'landingPage.testimonies.gabrielGonzalez.position'),
                          alt:g.message(code: 'landingPage.testimonies.gabrielGonzalez.alt')
                  ]"/>
    </li>
    <li>
        <g:render template="/dashboard/landingPageModules/landingTestimony"
                  model="[
                          image:g.resource(dir:'images', file:'manuelaCarmena.jpg'),
                          name:'Manuela Carmena',
                          testimony:g.message(code: 'landingPage.testimonies.manuelaCarmena.testimony'),
                          position:g.message(code: 'landingPage.testimonies.manuelaCarmena.position'),
                          alt:g.message(code: 'landingPage.testimonies.manuelaCarmena.alt')
                  ]"/>
    </li>
</ul>