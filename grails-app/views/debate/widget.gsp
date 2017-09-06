<g:applyLayout name="mainWidget">

    <head>
        <style>
        ${raw(params.customCss)}
        </style>
        <r:require modules="debate"/>
    </head>



    <section id="results-widget-content" class="clearfix">
        <g:render template="/debate/showModules/mainContent" model="[debate: debate, debateUser: debateUser,proposalPage:proposalPage]" />
    </section>
    <header>
        <h1>
            <a href="https://kuorum.org" id="brand" class="navbar-brand">
                <img src="${resource(dir: 'images', file: 'logo@3x.png')}" alt="Kuorum.org">
                <span class="hidden">Kuorum.org</span>
            </a>
        </h1>
    </header>
    </body>

</g:applyLayout>