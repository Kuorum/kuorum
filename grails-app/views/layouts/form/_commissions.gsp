<label>${label}</label>

<div class="interestContainer clearfix">
    <div class="all clearfix">
        <g:each in="${kuorum.core.model.CommissionType.values().sort{it.order}-[kuorum.core.model.CommissionType.OTHERS]}" var="commissionType">
            <input type="checkbox" name="${field}" id="pretty-check-${commissionType}" value="${commissionType}" class="check" ${checkedCommissions.contains(commissionType)?'checked':''}>
            <label for="${commissionType}"><span class="icon-${commissionType}"></span><g:message code="kuorum.core.model.CommissionType.${commissionType}"/></label>
        </g:each>
    </div><!-- /.all -->
</div>
<g:if test="${errorMessage}">
    <span for="relevantCommissions" class="error">${errorMessage}</span>
</g:if>