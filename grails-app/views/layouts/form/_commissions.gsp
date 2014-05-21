<span class="span-label">${label}</span>
<label class="checkbox-inline pull-right">
    <input type="checkbox" id="selectAll" value="allInterest"> <g:message code="customRegister.step3.form.relevantCommissions.selectAll"/>
</label>
<p class="help-block">${subLabel}</p>

<div class="interestContainer clearfix">
    <div class="all clearfix">
        <g:each in="${kuorum.core.model.CommissionType.values()-[kuorum.core.model.CommissionType.OTHERS]}" var="commissionType">
            <input type="checkbox" name="${field}" id="${commissionType}" value="${commissionType}" class="check" ${checkedCommissions.contains(commissionType)?'checked':''}>
            <label for="${commissionType}"><span class="icon-${commissionType}"></span><g:message code="kuorum.core.model.CommissionType.${commissionType}"/></label>
        </g:each>
    </div><!-- /.all -->
    <label class="checkbox-inline"><input type="checkbox" id="others" value="Otros intereses">
        <g:message code="customRegister.step3.form.relevantCommissions.OTHERS"/>
    </label>
    <p class="help-block">
        ${helpBlock}
    </p>
</div>
<g:if test="${errorMessage}">
    <span for="relevantCommissions" class="error">${errorMessage}</span>
</g:if>