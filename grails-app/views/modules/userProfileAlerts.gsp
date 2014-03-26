
<h1>ALERTS</h1>
<g:if test="${alerts}">
    <g:each in="${alerts}" var="alert" status="i">

        ${i} .- ${alert.mailType.nameTemplate}<br/>
    </g:each>
</g:if>
<g:else>
    NO ALERTS
</g:else>
