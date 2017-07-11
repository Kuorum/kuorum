<!-- FILTRO Y BUSCADOR DE CAMPAÑAS -->
<div class="box-ppal filterbox">
    <form class="form-horizontal" id="search-form-campaign">
        <fieldset class="form-group" id="toFilters">
            <div class="col-sm-3">
                <label for="filterCampaigns" class="sr-only">
                    <g:message code="tools.massMailing.list.filter.title"/> :
                </label>
                <select name="filterCampaigns" class="form-control input-lg" id="filterCampaigns">
                    <option value="all" name="all" id="all"><g:message code="tools.massMailing.list.filter.all"/> </option>
                    <g:each in="${['newsletter', 'debate', 'post']}" var="campaignType">
                        <option value="${campaignType}" name="${campaignType}" id="${campaignType}">
                            <g:message code="${'tools.campaign.new'}.${campaignType}"/>
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
                    <input type="text" class="form-control searchCampaigns" name="searchCampaign" id="searchCampaign" placeholder="${g.message(code:'tools.campaign.filter.quickSearch.placeHolder')}"/>
                </div>
            </div>
            <div class="col-sm-1">
                <g:link mapping="politicianCampaignsNew" class="btn btn-blue inverted">
                    <g:message code="tools.massMailing.list.newCampaign"/>
                </g:link>
            </div>
        </fieldset>
    </form>
</div>
<!-- LISTADO DE CAMPAÑAS -->
<div class="box-ppal list-campaigns">
    <div id="campaignsOrderOptions" class="box-order-options clearfix">
        <span><g:message code="tools.massMailing.list.order.by"/> :</span>
        <ul class="sort-options">
            <li><a href="#" role="button" class="sort active asc" data-sort="timestamp"><g:message code="tools.massMailing.list.order.timeSent"/></a></li>
            <li><a href="#" role="button" class="sort" data-sort="title"><g:message code="tools.massMailing.list.order.title"/></a></li>
            <li><a href="#" role="button" class="sort" data-sort="recip-number"><g:message code="tools.massMailing.list.order.recipients"/></a></li>
            <li><a href="#" role="button" class="sort" data-sort="open-number"><g:message code="tools.massMailing.list.order.openRate"/></a></li>
            <li><a href="#" role="button" class="sort" data-sort="click-number"><g:message code="tools.massMailing.list.order.clickRate"/></a></li>
        </ul>
        <div class="pag-list-campaigns">
            <div class="pagination">
                <ul class="paginationTop"></ul>
                <span class="counterList">Total of <span class="totalList"></span></span>
            </div>
        </div>
    </div>
    <ul id="campaignsList" class="list">
        <g:each in="${campaigns}" var="campaign" status="i">
            <g:render template="liCampaign" model="[campaign:campaign, user:user, idx:i]"/>
        </g:each>
        <g:each in="${debates}" var="debate" status="i">
            <g:render template="liCampaignDebate" model="[debate: debate, user: user, idx: debate.id]"/>
        </g:each>
        <g:each in="${posts}" var="post" status="i">
            <g:render template="liCampaignPost" model="[post: post, user: user, idx: post.id]"/>
        </g:each>
    </ul>

    <div class="pag-list-campaigns clearfix">
        <div class="pagination">
            <ul class="paginationBottom"></ul>
            <span class="counterList">Total of <span class="totalList"></span></span>
        </div>
    </div>


    <!-- MODAL AUTH PROFILE CHANGES -->
    <div class="modal fade in" id="modalEditScheduled" tabindex="-1" role="dialog" aria-labelledby="modalEditScheduledTitle" aria-hidden="true">
        <div class="modal-dialog ">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                        <span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                    </button>
                    <h4>
                        <g:message code="tools.massMailing.editCampaignModal.title"/>
                    </h4>
                </div>
                <div class="modal-body">
                    <p><g:message code="tools.massMailing.editCampaignModal.text"/> </p>
                    <fieldset class="text-right">
                        <a href="#" class="btn btn-grey-light btn-lg" data-dismiss="modal" id="modalEditScheduledButtonClose">
                            <g:message code="tools.massMailing.editCampaignModal.cancel"/>
                        </a>
                        <a href="#" class="btn btn-blue inverted btn-lg" id="modalEditScheduledButtonOk">
                            <g:message code="tools.massMailing.editCampaignModal.pause"/>
                        </a>
                    </fieldset>

                </div>
            </div>
        </div>
    </div>

    <!-- MODAL CONFIRM  DELETE-->
    <div class="modal fade in" id="campaignDeleteConfirm" tabindex="-1" role="dialog" aria-labelledby="campaignDeleteTitle" aria-hidden="true">
        <div class="modal-dialog ">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                        <span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                    </button>
                    <h4 id="campaignDeleteTitle">
                        <g:message code="tools.massMailing.deleteCampaignModal.title"/>
                    </h4>
                </div>
                <div class="modal-body">
                    <a href="#LinkOverwitedWithJS" role="button" class="btn btn-blue inverted btn-lg deleteCampaignBtn">
                        <g:message code="tools.massMailing.deleteCampaignModal.button"/>
                    </a>
                </div>
            </div>
        </div>
    </div>

</div>