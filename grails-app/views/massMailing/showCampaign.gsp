<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">
            <g:message code="head.logged.account.tools.massMailing.show" args="[campaign.name]"/>
    </g:set>

    <title>${breadCrumbName}</title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />

</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianMassMailing"><g:message code="head.logged.account.tools.massMailing"/></g:link></li>
        <li class="active">${breadCrumbName}</li>
    </ol>
    <div class="box-ppal">
        <ul class="nav nav-tabs simple" data-tabs="tabs">
            <li role="presentation" class="active"><a href="#stats" data-toggle="tab"><g:message code="tools.massMailing.view.stats"/></a></li>
            <li role="presentation"><a href="#recipients" data-toggle="tab"><g:message code="tools.massMailing.list.recipients"/></a></li>
            <li role="presentation"><a href="#viewemail" data-toggle="tab"><g:message code="tools.massMailing.view.viewMail"/></a></li>
        </ul>
        <div id="tabs-stats-campaign" class="tab-content">
            <div class="tab-pane active" id="stats">
                <h2 class="sr-only"><g:message code="tools.massMailing.view.stats"/></h2>
                <ul class="activity">
                    <li class="posts">
                        <span class='recip-number'><campaignUtil:campaignsSent campaign="${campaign}"/></span>
                        <g:message code="tools.massMailing.list.recipients"/>
                    </li>
                    <li class="posts">
                        <span class='open-number'><campaignUtil:openRate campaign="${campaign}"/></span>
                        <g:message code="tools.massMailing.list.opens"/>
                    </li>
                    <li class="posts">
                        <span class='click-number'><campaignUtil:clickRate campaign="${campaign}"/></span>
                        <g:message code="tools.massMailing.list.click"/>
                    </li>
                </ul>
                <h3>24-hour performance</h3>
                <div id="campaignStatsContainer">
                    <g:message code="tools.feature.notReady"/>
                </div>
            </div>
            <div class="tab-pane" id="recipients">
                <h2 class="sr-only"><g:message code="tools.massMailing.list.recipients"/></h2>
                <div class="pag-list-contacts clearfix">
                    <nav:contactPagination
                        currentPage="${0}"
                        sizePage="${trackingPage.size}"
                        ulClasss="paginationTop"
                        total="${trackingPage.total}"/>
                </div>
                <table class="table">
                    <thead>
                    <tr>
                        <th><g:message code="kuorum.web.commands.payment.contact.ContactCommand.name.label"/></th>
                        <th><g:message code="tools.massMailing.list.opens"/></th>
                        <th><g:message code="tools.massMailing.list.click"/></th>
                        <th><g:message code="tools.contact.list.contact.engagement"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <!-- EACH BLOCK -->
                    <g:each in="${trackingPage.data}" var="trackingMail">
                        <tr>
                            <td><span class="only-mobile"><g:message code="kuorum.web.commands.payment.contact.ContactCommand.name.label"/>: </span>${trackingMail.contact.name}</td>
                            <td><span class="only-mobile"><g:message code="tools.massMailing.list.opens"/>: </span>${trackingMail.numOpens}</td>
                            <td><span class="only-mobile"><g:message code="tools.massMailing.list.click"/>: </span>${trackingMail.numClicks}</td>
                            <td>
                                <span class="only-mobile"><g:message code="tools.contact.list.contact.engagement"/></span>
                                <contactUtil:engagement concat="${trackingMail.contact}"/>
                            </td>
                        </tr>
                    </g:each>
                    <!-- END EACH BLOCK -->
                    </tbody>
                </table>
                <div class="pag-list-contacts clearfix">
                    <nav:contactPagination
                        currentPage="${0}"
                        sizePage="${trackingPage.size}"
                        ulClasss="paginationBottom"
                        total="${trackingPage.total}"/>
                </div>
            </div>
            <div class="tab-pane" id="viewemail">
                <h2 class="sr-only"><g:message code="tools.massMailing.view.viewMail"/></h2>
                <iframe id="campaignEmailContainer">
                    iframe para cargar el html del email
                </iframe>
            </div>
        </div>
    </div>
</content>