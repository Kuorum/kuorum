<g:applyLayout name="mainWidget">
    <head>
        <title>Valoración político</title>
        <r:require module="comparativeChart"/>
    </head>
    <body>
    <section>
        <h1>Comparacion politicos</h1>
        <div>
            <div id="comparativeValChart" data-urlJson="${createLink(mapping: 'comparingPoliticianRate', absolute: true, params:params)}"></div>
        </div>
    </section>
    </body>

</g:applyLayout>