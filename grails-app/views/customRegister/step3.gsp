<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="customRegisterLayout">
    <parameter name="actualStep" value="3" />
</head>

<content tag="intro">
    <h1><g:message code="customRegister.step3.intro.title"/> </h1>
    <p><g:message code="customRegister.step3.intro.description"/></p>
</content>

<content tag="mainContent">
    <g:form method="POST" mapping="customRegisterStep3" name="sign3" role="form">
        <div class="form-group">
            <span class="span-label">
                <g:message code="customRegister.step3.form.relevantCommissions.label"/>
                <label class="checkbox-inline pull-right">
                    <input type="checkbox" id="selectAll" value="allInterest"> <g:message code="customRegister.step3.form.relevantCommissions.selectAll"/>
                </label>
            </span>
            <div class="interestContainer clearfix">
                <div class="all clearfix">
                    <g:each in="${kuorum.core.model.CommissionType.values()-[kuorum.core.model.CommissionType.OTHERS]}" var="commissionType">
                        <input type="checkbox" name="relevantCommissions" id="${commissionType}" value="${commissionType}" class="check" ${command.relevantCommissions.contains(commissionType)?'checked':''}>
                        <label for="${commissionType}"><span class="icon-${commissionType}"></span><g:message code="kuorum.core.model.CommissionType.${commissionType}"/></label>
                    </g:each>
                </div><!-- /.all -->
                <label class="checkbox-inline"><input type="checkbox" id="others" value="Otros intereses">
                    <g:message code="customRegister.step3.form.relevantCommissions.OTHERS"/>
                </label>
                <p class="help-block">
                    <g:message code="customRegister.step3.form.relevantCommissions.helpBlock"/>
                </p>
            </div>
            <g:if test="${hasErrors(bean: command, field: 'relevantCommissions', 'error')}">
                <span for="relevantCommissions" class="error">${g.fieldError(bean: command, field: 'relevantCommissions')}</span>
            </g:if>
        </div>
        <div class="form-group">
            <g:link mapping="customRegisterStep4" class="cancel"><g:message code="customRegister.step3.form.cancel"/></g:link>
            <input type="submit" class="btn btn-lg" value="${message(code:'customRegister.step3.form.submit')}"/>
        </div>
    </g:form>
</content>

<content tag="boxes">
    <div class="boxes">
        <h2><g:message code="customRegister.step3.lateral.title"/></h2>
        <p><g:message code="customRegister.step3.lateral.content"/></p>
    </div>
</content>
