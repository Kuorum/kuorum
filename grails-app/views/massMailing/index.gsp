<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="head.logged.account.tools.massMailing"/></title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li  class="active"><g:message code="head.logged.account.tools.massMailing"/> </li>
    </ol>

    <g:if test="${campaigns || posts || debates}">
        <div id="listCampaigns">
            <g:render template="searchCampaigns" model="[campaigns: campaigns, debates: debates, posts: posts, user: user]"/>
        </div>
    </g:if>
    <g:else>
        <div class="container-fluid box-ppal choose-campaign">


            <p><g:message code="dashboard.payment.noCampaigns" /></p>
            %{--<p>--}%
                %{--<g:link mapping="politicianCampaignsNew" class="btn inverted btn-lg">--}%
                    %{--<g:message code="tools.massMailing.list.newCampaign"/>--}%
                %{--</g:link>--}%
            %{--</p>--}%
            <g:render template="chooseCampaign"/>
        </div>
    </g:else>

</content>