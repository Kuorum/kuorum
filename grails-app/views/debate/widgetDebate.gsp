<g:applyLayout name="mainWidget">

    <head>
        <style>
        ${raw(params.customCss)}
        </style>
        <r:require modules="debate,widgetResizer"/>
        <g:set var="widgetActive" value="${true}" scope="page"/>
    </head>



    <section id="main" class="clearfix">
        <g:render template="/debate/showModules/mainContent" model="[debate: debate, debateUser: debateUser,proposalPage:proposalPage, poweredByKuorum:true,eventData:eventData,eventRegistration:eventRegistration]" />
        <g:render template="/layouts/modals/modalLogin"/>
    </section>
    <header>
        <h1>
            <a href="https://kuorum.org" id="brand" class="navbar-brand" target="_blank">
                <img src="${resource(dir: 'images', file: 'logo@2x.png')}" alt="Kuorum.org">
                <span class="hidden">${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name}</span>
            </a>
        </h1>
    </header>
    %{--<script src="${g.resource(dir: '/js/widget/', file: 'widget-helper.js')}"/>--}%
    %{--<script src="${g.resource(dir: '/js/widget/iframe-resizer', file: 'iframeResizer.contentWindow.min.js')}"/>--}%

    </body>

</g:applyLayout>