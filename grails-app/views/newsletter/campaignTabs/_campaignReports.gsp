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
    <table class="table download-reports">
        <thead>
        <tr>
            <th>Type</th>
            <th>File Name </th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${reportsList}" var="report">
            <tr>
                <td><span class="${report.icon}"></span></td>
                <td>${report.name}</td>
                <td><a href="${report.url}" target="_blank"><span class="fa fa-download"></span> </a></td>
            </tr>
        </g:each>
        </tbody>
    </table>
</div>