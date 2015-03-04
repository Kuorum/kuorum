<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="funnel.subscriptionPaid.title"/></title>
    <meta name="layout" content="funnelLayout">
</head>

<content tag="mainContent">
    <section id="main" role="main" class="sign purchase clearfix">
        <div class="col-sm-10 col-sm-offset-1 col-md-6 col-md-offset-1 col-lg-6 col-lg-offset-1">
            <h1><g:message code="funnel.subscriptionPaid.description1"/></h1>
            <h2><g:message code="funnel.subscriptionPaid.description2"/></h2>
        </div>
        <div class="col-sm-12 col-md-4">
            <formUtil:validateForm bean="${command}" form="sign"/>
            <g:form mapping="funnelUpdatePersonalData" method="post" name="sign" class="login no-phone" role="form">
                <div class="form-group">
                    <formUtil:selectNation command="${command}" field="country"/>
                </div>
                <div class="form-group pull-left">
                    <formUtil:telephoneWithPrefix command="${command}" field="phonePrefix"/>
                </div>
                <div class="form-group pull-left">
                    <formUtil:input command="${command}" field="telephone" type="number" required="true"/>
                </div>
                <div class="form-group">
                    <p class="cancel">
                        <g:link mapping="home"><g:message code="funnel.subscriptionPaid.personalData.dismiss"/> </g:link>
                    </p>
                    <input type="submit" class="btn btn-lg" value="${message(code:'funnel.subscriptionPaid.personalData.success')}">
                </div>
            </g:form>
        </div>
    </section>
</content>