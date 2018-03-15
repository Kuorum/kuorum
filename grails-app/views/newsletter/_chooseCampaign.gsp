<ul class="actionIcons">
    <li class="fontIcon">
        <!--<span class="fa-stack fa-lg active"-->
        <g:link mapping="politicianMassMailingNew" role="button" class="actionIcons new-campaign-tip"
                rel="tooltip"
                data-toggle="tooltip"
                data-placement="bottom"
                title=""
                data-original-title="${g.message(code:'dashboard.payment.chooseCampaign.tooltip.newsletter')}">
            <span class="fa fa-envelope-o"></span>
            <span class="label"><g:message code="tools.campaign.new.newsletter"/></span>
        </g:link>
    </li>
    <li class="fontIcon">
        <g:link mapping="postCreate" role="button" class="actionIcons new-campaign-tip"
                rel="tooltip"
                data-toggle="tooltip"
                data-placement="bottom"
                title=""
                data-original-title="${g.message(code:'dashboard.payment.chooseCampaign.tooltip.post')}">
            <span class="fa fa-newspaper-o"></span>
            <span class="label"><g:message code="tools.campaign.new.post"/></span>
        </g:link>
    </li>
    <li class="fontIcon">
        <g:link mapping="debateCreate" role="button" class="actionIcons new-campaign-tip"
                rel="tooltip"
                data-toggle="tooltip"
                data-placement="bottom"
                title=""
                data-original-title="${g.message(code:'dashboard.payment.chooseCampaign.tooltip.debate')}">
            <span class="fa fa-comments-o"></span>
            <span class="label"><g:message code="tools.campaign.new.debate"/></span>
        </g:link>
    </li>
    <li class="fontIcon">
        <g:link mapping="eventCreate" role="button" class="actionIcons new-campaign-tip"
                rel="tooltip"
                data-toggle="tooltip"
                data-placement="bottom"
                title=""
                data-original-title="${g.message(code:'dashboard.payment.chooseCampaign.tooltip.event')}">
            <span class="fa fa-calendar-check-o"></span>
            <span class="label"><g:message code="tools.campaign.new.event"/></span>
        </g:link>
    </li>
    <li class="fontIcon">
        <g:link mapping="surveyCreate" role="button" class="actionIcons new-campaign-tip"
                rel="tooltip"
                data-toggle="tooltip"
                data-placement="bottom"
                title=""
                data-original-title="${g.message(code:'dashboard.payment.chooseCampaign.tooltip.survey')}">
            <span class="fa fa-bar-chart-o"></span>
            <span class="label"><g:message code="tools.campaign.new.survey"/></span>
        </g:link>
    </li>
    %{--<li class="fontIcon disabled"><g:link mapping="debateCreate" role="button" class="mail">--}%
    %{--<span class="fa fa-microphone"></span>--}%
    %{--<span class="label"><g:message code="tools.campaign.new.petition"/></span></g:link>--}%
    %{--</li>--}%

</ul>