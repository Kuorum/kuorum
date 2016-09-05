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
            <li role="presentation" class="active"><a href="#stats" data-toggle="tab">Stats</a></li>
            <li role="presentation"><a href="#recipients" data-toggle="tab">Recipients</a></li>
            <li role="presentation"><a href="#viewemail" data-toggle="tab">View email</a></li>
        </ul>
        <div id="tabs-stats-campaign" class="tab-content">
            <div class="tab-pane active" id="stats">
                <h2 class="sr-only">Stats</h2>
                <ul class="activity">
                    <li class="posts">
                        <campaignUtil:camapignsSent campaign="${campaign}"/> Campaigns sent
                    </li>
                    <li class="posts">
                        <campaignUtil:openRate campaign="${campaign}"/> Open rate
                    </li>
                    <li class="posts">
                        <campaignUtil:clickRate campaign="${campaign}"/> Click rate
                    </li>
                </ul>
                <h3>24-hour performance</h3>
                <div id="campaignStatsContainer"></div>
            </div>
            <div class="tab-pane" id="recipients">
                <h2 class="sr-only">Recipients</h2>
                <div class="pag-list-contacts clearfix">
                    <ul class="paginationBottom">
                        <li class="active">
                            <a class="page" href="#">1</a>
                        </li>
                        <li>
                            <a class="page" href="#">2</a>
                        </li>
                        <li class="disabled">
                            <a class="page" href="#">...</a>
                        </li>
                        <li>
                            <a class="page" href="#">4</a>
                        </li>
                    </ul>
                    <span class="counterList">Total of <span class="totalList">33</span></span>
                </div>
                <table class="table">
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Open</th>
                        <th>Click</th>
                        <th>Contact engagement</th>
                    </tr>
                    </thead>
                    <tbody>
                    <!-- EACH BLOCK -->
                    <tr>
                        <td><span class="only-mobile">Name: </span>Leonard Hoffstader</td>
                        <td><span class="only-mobile">Open: </span>7</td>
                        <td><span class="only-mobile">Click: </span>3</td>
                        <td>
                            <span class="only-mobile">Contact engagement: </span>
                            <ul class="engagement">
                                <li><a href="#">inactive</a></li>
                                <li class="active"><a href="#">reader</a></li>
                                <li><a href="#">supported</a></li>
                                <li><a href="#">broadcaster</a></li>
                            </ul>
                        </td>
                    </tr>
                    <!-- END EACH BLOCK -->
                    </tbody>
                </table>
                <div class="pag-list-contacts clearfix">
                    <ul class="paginationBottom">
                        <li class="active">
                            <a class="page" href="#">1</a>
                        </li>
                        <li>
                            <a class="page" href="#">2</a>
                        </li>
                        <li class="disabled">
                            <a class="page" href="#">...</a>
                        </li>
                        <li>
                            <a class="page" href="#">4</a>
                        </li>
                    </ul>
                    <span class="counterList">Total of <span class="totalList">33</span></span>
                </div>
            </div>
            <div class="tab-pane" id="viewemail">
                <h2 class="sr-only">View email</h2>
                <iframe id="campaignEmailContainer">
                    iframe para cargar el html del email
                </iframe>
            </div>
        </div>
    </div>
</content>