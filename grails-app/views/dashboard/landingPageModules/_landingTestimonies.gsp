

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
                          image:g.resource(dir:'images', file:'davidBurrowes.jpg'),
                          name:'David Burrowes',
                          testimony:g.message(code: 'landingPage.testimonies.davidBurrowes.testimony'),
                          position:g.message(code: 'landingPage.testimonies.davidBurrowes.position'),
                          alt:g.message(code: 'landingPage.testimonies.davidBurrowes.alt')
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