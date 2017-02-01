<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="tools.campaign.new.title"/></title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianCampaigns"><g:message code="head.logged.account.tools.massMailing"/></g:link></li>
        <li class="active"><g:message code="tools.campaign.new.title"/></li>
    </ol>
    <div class="container-fluid box-ppal dashboard">
        <ul id="mails" class="campaigns">
            <li class="fontIcon"><g:link mapping="politicianMassMailingNew" role="button" class="mail">
                <span class="fa fa-envelope-o"></span>
                <span class="label"><g:message code="tools.campaign.new.newsLetter"/></span></g:link>
            </li>
            %{--<li class="fontIcon"><g:link mapping="debateCreate" role="button" class="mail">--}%
                %{--<span class="fa fa-newspaper-o"></span>--}%
                %{--<span class="label"><g:message code="tools.campaign.new.post"/></span></g:link>--}%
            %{--</li>--}%
            <li class="fontIcon"><g:link mapping="debateCreate" role="button" class="mail">
                <span class="fa fa-comments"></span>
                <span class="label"><g:message code="tools.campaign.new.debate"/></span></g:link>
            </li>
            %{--<li class="fontIcon"><g:link mapping="debateCreate" role="button" class="mail">--}%
                %{--<span class="fa fa-bar-chart-o"></span>--}%
                %{--<span class="label"><g:message code="tools.campaign.new.survey"/></span></g:link>--}%
            %{--</li>--}%
            %{--<li class="fontIcon"><g:link mapping="debateCreate" role="button" class="mail">--}%
                %{--<span class="fa fa-microphone"></span>--}%
                %{--<span class="label"><g:message code="tools.campaign.new.petition"/></span></g:link>--}%
            %{--</li>--}%
            %{--<li class="fontIcon"><g:link mapping="debateCreate" role="button" class="mail">--}%
                %{--<span class="fa fa-calendar-check-o"></span>--}%
                %{--<span class="label"><g:message code="tools.campaign.new.event"/></span></g:link>--}%
            %{--</li>--}%

        </ul>
    </div>
</content>