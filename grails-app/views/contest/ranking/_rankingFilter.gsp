<%@ page import="org.kuorum.rest.model.communication.contest.ContestApplicationFocusTypeDTO" %>
<div class="box-ppal filterbox">
    <form class="form-horizontal" id="search-form-campaign">
        <fieldset aria-live="polite" class="form-group">
            <div class="col-sm-3 col-xs-12">
                <label for="filterCampaignsByCause"><g:message code="contest.ranking.filter.quickSearch"/></label>

                <div class="searchContainer">
                    <input type="text" class="form-control searchRankingCampaign" name="searchRankingCampaign"
                           id="searchRankingCampaign"/>
                </div>
            </div>

            <div class="col-sm-3">
                <label for="filterCampaignsByCause"><g:message code="contest.ranking.filter.byCause"/></label>
                <select name="filterCampaignsByCause" class="form-control rankingFilter" id="filterCampaignsByCause"
                        data-filter="ranking-cause">
                    <option value="all" name="all" id="all"><g:message
                            code="tools.massMailing.list.filter.all"/></option>

                    <g:each in="${contest.causes}" var="cause">
                        <option value="${cause}" id="${cause}">${cause}</option>
                    </g:each>
                </select>
            </div>

            <div class="col-sm-3">
                <label for="filterCampaignsByFocusType"><g:message
                        code="org.kuorum.rest.model.communication.contest.ContestApplicationFocusTypeDTO.label"/></label>
                <select name="filterCampaignsByFocusType" class="form-control rankingFilter"
                        id="filterCampaignsByFocusType" data-filter="ranking-contest-focusType">
                    <option value="all" name="all" id="all"><g:message
                            code="tools.massMailing.list.filter.all"/></option>

                    <g:each in="${org.kuorum.rest.model.communication.contest.ContestApplicationFocusTypeDTO.values()}"
                            var="focusType">
                        <option><g:message
                                code="org.kuorum.rest.model.communication.contest.ContestApplicationFocusTypeDTO.${focusType}"/></option>
                    </g:each>
                </select>
            </div>

            <div class="col-sm-3">
                <label for="filterCampaignsByFocusType"><g:message
                        code="org.kuorum.rest.model.communication.contest.ContestApplicationActivityTypeDTO.label"/></label>
                <select name="filterCampaignsByActivityType" class="form-control rankingFilter"
                        id="filterCampaignsByActivityType" data-filter="ranking-contest-activityType">
                    <option value="all" name="all" id="all"><g:message
                            code="tools.massMailing.list.filter.all"/></option>

                    <g:each in="${org.kuorum.rest.model.communication.contest.ContestApplicationActivityTypeDTO.values()}"
                            var="activityType">
                        <option><g:message
                                code="org.kuorum.rest.model.communication.contest.ContestApplicationActivityTypeDTO.${activityType}"/></option>
                    </g:each>
                </select>
            </div>
        </fieldset>
    </form>
</div>