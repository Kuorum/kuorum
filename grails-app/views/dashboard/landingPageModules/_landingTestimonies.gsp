

<ul class="testimonies-list clearfix">
    <li>
        <g:render template="/dashboard/landingPageModules/landingTestimony"
        model="[
                image:g.resource(dir:'images', file:'manuelaCarmena.jpg'),
                name:'Manuela Carmena',
                testimony:'I had a dream that something like Kuorum could exist',
                position:'Manuela Carmena - MAYOR OF MADRID'
        ]"/>
    </li>
    <li>
        <g:render template="/dashboard/landingPageModules/landingTestimony"
                  model="[
                          image:g.resource(dir:'images', file:'davidBurrowes.jpg'),
                          name:'David Burrowes',
                          testimony:'Kuorum closes the gap between citizens and politicians',
                          position:'David Burrowes - CONGRESMAN'
                  ]"/>
    </li>
    <li>
        <g:render template="/dashboard/landingPageModules/landingTestimony"
                  model="[
                          image:g.resource(dir:'images', file:'catSmith.jpg'),
                          name:'Cat Smith',
                          testimony:'Nobody believed that we could win that campaign but we did',
                          position:'Cat Smith - MEMBER OF PARLIAMENT'
                  ]"/>
    </li>
</ul>