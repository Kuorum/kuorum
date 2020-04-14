<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<h2 class="sr-only"><g:message code="tools.massMailing.view.report"/></h2>
<div class="pag-list-contacts clearfix">
    <div class="actions">

    </div>

</div>
<div>
     <div class="contacts-tooltip icons pull-right">
        <span class="fa-stack fa-lg animated-info-icon active"
              aria-hidden="true"
              rel="tooltip"
              data-toggle="tooltip"
              data-placement="bottom"
              title=""
              data-original-title="${g.message(code:'tools.massMailing.view.report.info')}">
            %{--<img src="${g.resource(dir: "images/icons", file: "icon-info.png")}"/>--}%
            <span class="fas fa-info-circle fa-2x"></span>
        </span>
     </div>
    <ul class="list-campaign-files">
        <g:each in="${reportsList}" var="report">
            <li><a href="${report.url}" target="_blank"><span class="${report.icon}"></span>${report.name}</a></li>
        </g:each>
    </ul>
</div>