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
    <div id="listCampaigns">
        <ol class="breadcrumb">
            <li  class="active"><g:message code="head.logged.account.tools.massMailing"/> </li>
        </ol>

        <!-- FILTRO Y BUSCADOR DE CAMPAÑAS -->
        <div class="box-ppal">
            <form class="form-horizontal">
                <fieldset class="form-group" id="toFilters">
                    <div class="col-sm-3">
                        <label for="filterCampaigns" class="sr-only">
                            <g:message code="tools.massMailing.list.filter.title"/> :
                        </label>
                        <select name="filterCampaigns" class="form-control input-lg" id="filterCampaigns">
                            <option value="all" name="all" id="all"><g:message code="tools.massMailing.list.filter.all"/> </option>
                            <g:each in="${org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.values()}" var="campaingStatus">
                                <option value="${campaingStatus}" name="${campaingStatus}" id="${campaingStatus}">
                                    <g:message code="${org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.class.name}.${campaingStatus}"/>
                                </option>
                            </g:each>
                        </select>
                    </div>
                    <div class="col-sm-3">
                        <span id="infoFilterCampaigns">
                            <span class="totalList"></span>
                            <g:message code="tools.massMailing.list.filter.counterText"/>
                            <span class="filtered"></span>
                        </span>
                    </div>
                    <div class="col-sm-3 col-md-3 col-md-offset-1">
                        <div class="searchContainer">
                            <input type="text" class="form-control searchCampaigns" name="searchCampaign" id="searchCampaign"/>
                        </div>
                    </div>
                    <div class="col-sm-1">
                        <g:link mapping="politicianMassMailingNew" class="btn btn-blue inverted">
                            <g:message code="tools.massMailing.list.newCampaign"/>
                        </g:link>
                    </div>
                </fieldset>
            </form>
        </div>
        <!-- LISTADO DE CAMPAÑAS -->
        <div class="box-ppal list-campaigns">
            <div id="campaignsOrderOptions" class="clearfix">
                <span><g:message code="tools.massMailing.list.order.by"/> :</span>
                <ul>
                    <li><a href="#" role="button" class="sort" data-sort="timestamp"><g:message code="tools.massMailing.list.order.timeSent"/></a></li>
                    <li><a href="#" role="button" class="sort" data-sort="title"><g:message code="tools.massMailing.list.order.title"/></a></li>
                    <li><a href="#" role="button" class="sort" data-sort="recip-number"><g:message code="tools.massMailing.list.order.recipients"/></a></li>
                    <li><a href="#" role="button" class="sort" data-sort="open-number"><g:message code="tools.massMailing.list.order.openRate"/></a></li>
                    <li><a href="#" role="button" class="sort" data-sort="click-number"><g:message code="tools.massMailing.list.order.clickRate"/></a></li>
                </ul>
                <div class="pag-list-campaigns">
                    <ul class="paginationTop"></ul>
                    <span class="counterList">Total of <span class="totalList"></span></span>
                </div>
            </div>
            <ul id="campaignsList" class="list">
                <g:each in="${campaigns}" var="campaign" status="i">
                    <g:render template="listCampaign" model="[campaign:campaign, idx:i]"/>
                </g:each>
                <li class="SENT" id="1">
                    <span class="state">SENT</span>
                    <h3 class="title"><a href="#">It’s time to build a better country for everybody!</a></h3>
                    <p class="name">Sent <span class="date"><span class="timestamp" type="hidden" val="1465748640" />(Tue, June 12, 2016, 4:24 pm)</span> - Name of saved filter</p>
                    <ul>
                        <li class="recipients"><span class="recip-number">5214</span> recipients</li>
                        <li class="open"><span class="open-number">24,3</span> open</li>
                        <li class="click"><span class="click-number">3,0</span> click</li>
                    </ul>
                    <a href="#" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="SCHEDULED" id="2">
                    <span class="state">SCHEDULED</span>
                    <h3 class="title"><a href="#">Let’s talk about climate change (EDITED)</a></h3>
                    <p class="name">Scheduled <span class="date"><input class="timestamp" type="hidden" val="1474547040" />(Sat, Sep 22, 2016, 12:24 pm)</span> - Name of saved filter</p>
                    <ul>
                        <li class="recipients"><span class="recip-number"></span> recipients</li>
                        <li class="open"><span class="open-number"></span> open</li>
                        <li class="click"><span class="click-number"></span> click</li>
                    </ul>
                    <a href="#" class="campaignEdit"><span class="fa fa-edit"></span> <span class="sr-only">Edit<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="DRAFT" id="3">
                    <span class="state">DRAFT</span>
                    <h3 class="title"><a href="#">More jobs for the North</a></h3>
                    <p class="name"></p>
                    <ul>
                        <li class="recipients"><span class="recip-number"></span> recipients</li>
                        <li class="open"><span class="open-number"></span> open</li>
                        <li class="click"><span class="click-number"></span> click</li>
                    </ul>
                    <a href="#" class="campaignEdit"><span class="fa fa-edit"></span> <span class="sr-only">Edit<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="SENT" id="4">
                    <span class="state">SENT</span>
                    <h3 class="title"><a href="#">Support our businesses for the future</a></h3>
                    <p class="name">Sent <span class="date"><input class="timestamp" type="hidden" val="1467468780" />(Tue, July 2, 2016, 2:13 pm)</span> - Name of saved filter</p>
                    <ul>
                        <li class="recipients"><span class="recip-number">352</span> recipients</li>
                        <li class="open"><span class="open-number">70,6</span> open</li>
                        <li class="click"><span class="click-number">15,6</span> click</li>
                    </ul>
                    <a href="#" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="SENT" id="5">
                    <span class="state">SENT</span>
                    <h3 class="title"><a href="#">It’s time to build a better country for everybody!</a></h3>
                    <p class="name">Sent <span class="date"><input class="timestamp" type="hidden" val="1465748640" />(Tue, June 12, 2016, 4:24 pm)</span> - Name of saved filter</p>
                    <ul>
                        <li class="recipients"><span class="recip-number">4214</span> recipients</li>
                        <li class="open"><span class="open-number">24,3</span> open</li>
                        <li class="click"><span class="click-number">3,0</span> click</li>
                    </ul>
                    <a href="#" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="DRAFT" id="6">
                    <span class="state">DRAFT</span>
                    <h3 class="title"><a href="#">Let’s talk about climate change</a></h3>
                    <p class="name"></p>
                    <ul>
                        <li class="recipients"><span class="recip-number"></span> recipients</li>
                        <li class="open"><span class="open-number"></span> open</li>
                        <li class="click"><span class="click-number"></span> click</li>
                    </ul>
                    <a href="#" class="campaignEdit"><span class="fa fa-edit"></span> <span class="sr-only">Edit<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="DRAFT" id="7">
                    <span class="state">DRAFT</span>
                    <h3 class="title"><a href="#">More jobs for the North</a></h3>
                    <p class="name"></p>
                    <ul>
                        <li class="recipients"><span class="recip-number"></span> recipients</li>
                        <li class="open"><span class="open-number"></span> open</li>
                        <li class="click"><span class="click-number"></span> click</li>
                    </ul>
                    <a href="#" class="campaignEdit"><span class="fa fa-edit"></span> <span class="sr-only">Edit<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="SENT" id="8">
                    <span class="state">SENT</span>
                    <h3 class="title"><a href="#">Support our businesses for the future</a></h3>
                    <p class="name">Sent <span class="date"><input class="timestamp" type="hidden" val="1461529200" />(Tue, April 24, 2016, 8:20 pm)</span> - Name of saved filter</p>
                    <ul>
                        <li class="recipients"><span class="recip-number">352</span> recipients</li>
                        <li class="open"><span class="open-number">70,6</span> open</li>
                        <li class="click"><span class="click-number">15,6</span> click</li>
                    </ul>
                    <a href="#" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="DRAFT" id="9">
                    <span class="state">DRAFT</span>
                    <h3 class="title"><a href="#">It’s time to build a better country for everybody!</a></h3>
                    <p class="name"></p>
                    <ul>
                        <li class="recipients"><span class="recip-number"></span> recipients</li>
                        <li class="open"><span class="open-number"></span> open</li>
                        <li class="click"><span class="click-number"></span> click</li>
                    </ul>
                    <a href="#" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="SCHEDULED" id="10">
                    <span class="state">SCHEDULED</span>
                    <h3 class="title"><a href="#">Let’s talk about climate change</a></h3>
                    <p class="name">Scheduled <span class="date"><input class="timestamp" type="hidden" val="1475065440" />(Sat, Sep 28, 2016, 12:24 pm)</span> - Name of saved filter</p>
                    <ul>
                        <li class="recipients"><span class="recip-number"></span> recipients</li>
                        <li class="open"><span class="open-number"></span> open</li>
                        <li class="click"><span class="click-number"></span> click</li>
                    </ul>
                    <a href="#" class="campaignEdit"><span class="fa fa-edit"></span> <span class="sr-only">Edit<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="DRAFT" id="11">
                    <span class="state">DRAFT</span>
                    <h3 class="title"><a href="#">Save the children</a></h3>
                    <p class="name"></p>
                    <ul>
                        <li class="recipients"><span class="recip-number"></span> recipients</li>
                        <li class="open"><span class="open-number"></span> open</li>
                        <li class="click"><span class="click-number"></span> click</li>
                    </ul>
                    <a href="#" class="campaignEdit"><span class="fa fa-edit"></span> <span class="sr-only">Edit<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="SENT" id="12">
                    <span class="state">SENT</span>
                    <h3 class="title"><a href="#">Nothing to declare</a></h3>
                    <p class="name">Sent <span class="date"><input class="timestamp" type="hidden" val="1469885040" />(Mon, July 30, 2016, 13:24 pm)</span> - Name of saved filter</p>
                    <ul>
                        <li class="recipients"><span class="recip-number">852</span> recipients</li>
                        <li class="open"><span class="open-number">80,6</span> open</li>
                        <li class="click"><span class="click-number">18,6</span> click</li>
                    </ul>
                    <a href="#" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="SENT" id="13">
                    <span class="state">SENT</span>
                    <h3 class="title"><a href="#">A better country for everybody!</a></h3>
                    <p class="name">Sent <span class="date"><input class="timestamp" type="hidden" val="1475065440" />(Sat, Sep 28, 2016, 12:24 pm)</span> - Name of saved filter</p>
                    <ul>
                        <li class="recipients"><span class="recip-number">214</span> recipients</li>
                        <li class="open"><span class="open-number">29,3</span> open</li>
                        <li class="click"><span class="click-number">2,0</span> click</li>
                    </ul>
                    <a href="#" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="SCHEDULED" id="14">
                    <span class="state">SCHEDULED</span>
                    <h3 class="title"><a href="#">Let's talk about no more guns :-)</a></h3>
                    <p class="name">Scheduled <span class="date"><input class="timestamp" type="hidden" val="1472840640" />(Fri, Sep 2, 2016, 18:24 pm)</span> - Name of saved filter</p>
                    <ul>
                        <li class="recipients"><span class="recip-number"></span> recipients</li>
                        <li class="open"><span class="open-number"></span> open</li>
                        <li class="click"><span class="click-number"></span> click</li>
                    </ul>
                    <a href="#" class="campaignEdit"><span class="fa fa-edit"></span> <span class="sr-only">Edit<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="DRAFT" id="15">
                    <span class="state">DRAFT</span>
                    <h3 class="title"><a href="#">Jobs for the South</a></h3>
                    <p class="name"></p>
                    <ul>
                        <li class="recipients"><span class="recip-number"></span> recipients</li>
                        <li class="open"><span class="open-number"></span> open</li>
                        <li class="click"><span class="click-number"></span> click</li>
                    </ul>
                    <a href="#" class="campaignEdit"><span class="fa fa-edit"></span> <span class="sr-only">Edit<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="SENT" id="16">
                    <span class="state">SENT</span>
                    <h3 class="title"><a href="#">Our businesses for the future</a></h3>
                    <p class="name">Sent <span class="date"><input class="timestamp" type="hidden" val="1462389840" />(Tue, May 4, 2016, 7:24 pm)</span> - Name of saved filter</p>
                    <ul>
                        <li class="recipients"><span class="recip-number">3952</span> recipients</li>
                        <li class="open"><span class="open-number">40,6</span> open</li>
                        <li class="click"><span class="click-number">25,6</span> click</li>
                    </ul>
                    <a href="#" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="SENT" id="17">
                    <span class="state">SENT</span>
                    <h3 class="title"><a href="#">It’s time to build a better country for everybody!</a></h3>
                    <p class="name">Sent <span class="date"><input class="timestamp" type="hidden" val="1465748940" />(Tue, June 12, 2016, 4:29 pm)</span> - Name of saved filter</p>
                    <ul>
                        <li class="recipients"><span class="recip-number">1203</span> recipients</li>
                        <li class="open"><span class="open-number">24,3</span> open</li>
                        <li class="click"><span class="click-number">3,0</span> click</li>
                    </ul>
                    <a href="#" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="SCHEDULED" id="18">
                    <span class="state">SCHEDULED</span>
                    <h3 class="title"><a href="#">Let’s talk about climate change</a></h3>
                    <p class="name">Scheduled <span class="date"><input class="timestamp" type="hidden" val="1474372800" />(Sat, Sep 20, 2016, 12:00 pm)</span> - Name of saved filter</p>
                    <ul>
                        <li class="recipients"><span class="recip-number"></span> recipients</li>
                        <li class="open"><span class="open-number"></span> open</li>
                        <li class="click"><span class="click-number"></span> click</li>
                    </ul>
                    <a href="#" class="campaignEdit"><span class="fa fa-edit"></span> <span class="sr-only">Edit<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="DRAFT" id="19">
                    <span class="state">DRAFT</span>
                    <h3 class="title"><a href="#">More jobs for the North</a></h3>
                    <p class="name"></p>
                    <ul>
                        <li class="recipients"><span class="recip-number"></span> recipients</li>
                        <li class="open"><span class="open-number"></span> open</li>
                        <li class="click"><span class="click-number"></span> click</li>
                    </ul>
                    <a href="#" class="campaignEdit"><span class="fa fa-edit"></span> <span class="sr-only">Edit<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="SENT" id="20">
                    <span class="state">SENT</span>
                    <h3 class="title"><a href="#">Support our businesses for the future</a></h3>
                    <p class="name">Sent <span class="date"><input class="timestamp" type="hidden" val="1466343000" />(Tue, June 19, 2016, 1:30 pm)</span> - Name of saved filter</p>
                    <ul>
                        <li class="recipients"><span class="recip-number">352</span> recipients</li>
                        <li class="open"><span class="open-number">70,6</span> open</li>
                        <li class="click"><span class="click-number">15,6</span> click</li>
                    </ul>
                    <a href="#" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="DRAFT" id="21">
                    <span class="state">DRAFT</span>
                    <h3 class="title"><a href="#">It’s time to build a better country for everybody!</a></h3>
                    <p class="name"></p>
                    <ul>
                        <li class="recipients"><span class="recip-number"></span> recipients</li>
                        <li class="open"><span class="open-number"></span> open</li>
                        <li class="click"><span class="click-number"></span> click</li>
                    </ul>
                    <a href="#" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="SCHEDULED" id="22">
                    <span class="state">SCHEDULED</span>
                    <h3 class="title"><a href="#">Let’s talk about climate change</a></h3>
                    <p class="name">Scheduled <span class="date"><input class="timestamp" type="hidden" val="1481460300" />(Sat, Dec 11, 2016, 12:45 pm)</span> - Name of saved filter</p>
                    <ul>
                        <li class="recipients"><span class="recip-number"></span> recipients</li>
                        <li class="open"><span class="open-number"></span> open</li>
                        <li class="click"><span class="click-number"></span> click</li>
                    </ul>
                    <a href="#" class="campaignEdit"><span class="fa fa-edit"></span> <span class="sr-only">Edit<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="DRAFT" id="23">
                    <span class="state">DRAFT</span>
                    <h3 class="title"><a href="#">More jobs for the North</a></h3>
                    <p class="name"></p>
                    <ul>
                        <li class="recipients"><span class="recip-number"></span> recipients</li>
                        <li class="open"><span class="open-number"></span> open</li>
                        <li class="click"><span class="click-number"></span> click</li>
                    </ul>
                    <a href="#" class="campaignEdit"><span class="fa fa-edit"></span> <span class="sr-only">Edit<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="DRAFT" id="24">
                    <span class="state">DRAFT</span>
                    <h3 class="title"><a href="#">Support our businesses for the future</a></h3>
                    <p class="name"></p>
                    <ul>
                        <li class="recipients"><span class="recip-number"></span> recipients</li>
                        <li class="open"><span class="open-number"></span> open</li>
                        <li class="click"><span class="click-number"></span> click</li>
                    </ul>
                    <a href="#" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="SENT" id="25">
                    <span class="state">SENT</span>
                    <h3 class="title"><a href="#">It’s time to build a better country for everybody!</a></h3>
                    <p class="name">Sent <span class="date"><input class="timestamp" type="hidden" val="1453656000" />(Tue, Jan 24, 2016, 5:20 pm)</span> - Name of saved filter</p>
                    <ul>
                        <li class="recipients"><span class="recip-number">8932</span> recipients</li>
                        <li class="open"><span class="open-number">24,3</span> open</li>
                        <li class="click"><span class="click-number">3,0</span> click</li>
                    </ul>
                    <a href="#" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="SCHEDULED" id="26">
                    <span class="state">SCHEDULED</span>
                    <h3 class="title"><a href="#">Let’s talk about climate change</a></h3>
                    <p class="name">Scheduled <span class="date"><input class="timestamp" type="hidden" val="1475360640" />(Sat, Oct 1, 2016, 10:24 pm)</span> - Name of saved filter</p>
                    <ul>
                        <li class="recipients"><span class="recip-number"></span> recipients</li>
                        <li class="open"><span class="open-number"></span> open</li>
                        <li class="click"><span class="click-number"></span> click</li>
                    </ul>
                    <a href="#" class="campaignEdit"><span class="fa fa-edit"></span> <span class="sr-only">Edit<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="DRAFT" id="27">
                    <span class="state">DRAFT</span>
                    <h3 class="title"><a href="#">Save the children</a></h3>
                    <p class="name"></p>
                    <ul>
                        <li class="recipients"><span class="recip-number"></span> recipients</li>
                        <li class="open"><span class="open-number"></span> open</li>
                        <li class="click"><span class="click-number"></span> click</li>
                    </ul>
                    <a href="#" class="campaignEdit"><span class="fa fa-edit"></span> <span class="sr-only">Edit<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="SENT" id="28">
                    <span class="state">SENT</span>
                    <h3 class="title"><a href="#">Nothing to declare</a></h3>
                    <p class="name">Sent <span class="date"><input class="timestamp" type="hidden" val="1469883600" />(Mon, July 30, 2016, 13:00 pm)</span> - Name of saved filter</p>
                    <ul>
                        <li class="recipients"><span class="recip-number">852</span> recipients</li>
                        <li class="open"><span class="open-number">80,6</span> open</li>
                        <li class="click"><span class="click-number">18,6</span> click</li>
                    </ul>
                    <a href="#" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="DRAFT" id="29">
                    <span class="state">DRAFT</span>
                    <h3 class="title"><a href="#">A different way of thinking</a></h3>
                    <p class="name"></p>
                    <ul>
                        <li class="recipients"><span class="recip-number"></span> recipients</li>
                        <li class="open"><span class="open-number"></span> open</li>
                        <li class="click"><span class="click-number"></span> click</li>
                    </ul>
                    <a href="#" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="SCHEDULED" id="30">
                    <span class="state">SCHEDULED</span>
                    <h3 class="title"><a href="#">Talk about poverty</a></h3>
                    <p class="name">Scheduled <span class="date"><input class="timestamp" type="hidden" val="1472842500" />(Fri, Sep 2, 2016, 18:55 pm)</span> - Name of saved filter</p>
                    <ul>
                        <li class="recipients"><span class="recip-number"></span> recipients</li>
                        <li class="open"><span class="open-number"></span> open</li>
                        <li class="click"><span class="click-number"></span> click</li>
                    </ul>
                    <a href="#" class="campaignEdit"><span class="fa fa-edit"></span> <span class="sr-only">Edit<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="DRAFT" id="31">
                    <span class="state">DRAFT</span>
                    <h3 class="title"><a href="#">Jobs for the South</a></h3>
                    <p class="name"></p>
                    <ul>
                        <li class="recipients"><span class="recip-number"></span> recipients</li>
                        <li class="open"><span class="open-number"></span> open</li>
                        <li class="click"><span class="click-number"></span> click</li>
                    </ul>
                    <a href="#" class="campaignEdit"><span class="fa fa-edit"></span> <span class="sr-only">Edit<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="SENT" id="32">
                    <span class="state">SENT</span>
                    <h3 class="title"><a href="#">Our businesses for the future</a></h3>
                    <p class="name">Sent <span class="date"><input class="timestamp" type="hidden" val="1460283600" />(Tue, April 10, 2016, 10:20 pm)</span> - Name of saved filter</p>
                    <ul>
                        <li class="recipients"><span class="recip-number">397</span> recipients</li>
                        <li class="open"><span class="open-number">40,6</span> open</li>
                        <li class="click"><span class="click-number">25,6</span> click</li>
                    </ul>
                    <a href="#" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

                <li class="SCHEDULED" id="33">
                    <span class="state">SCHEDULED</span>
                    <h3 class="title"><a href="#">It’s time to nothing</a></h3>
                    <p class="name">Scheduled <span class="date"><input class="timestamp" type="hidden" val="1460283600" />(Tue, April 10, 2016, 10:20 pm)</span> - Name of saved filter</p>
                    <ul>
                        <li class="recipients"><span class="recip-number"></span> recipients</li>
                        <li class="open"><span class="open-number"></span> open</li>
                        <li class="click"><span class="click-number"></span> click</li>
                    </ul>
                    <a href="#" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats<span></a>
                    <a href="#" role="button" class="campaignDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
                </li>

            </ul>

            <div class="pag-list-campaigns clearfix">
                <ul class="paginationBottom"></ul>
                <span class="counterList">Total of <span class="totalList"></span></span>
            </div>
        </div>
    </div>
</content>