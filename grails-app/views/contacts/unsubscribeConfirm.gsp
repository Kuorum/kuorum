<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="tools.contact.unsubscribe.success.title" args="[user.name]"/></title>
    <meta name="layout" content="columnCLayout">
    <parameter name="specialMainRowCssClass" value=""/>

    <meta name="robots" content="noindex">
</head>

<content tag="mainContent">
    <div class="container-fluid box-ppal unsubscribe">
        <div class="row">
            <div class="col-xs-12 profile-pic-col">
                <h4><g:message code="tools.contact.unsubscribe.success.title" args="[user.name]"/></h4>
                <div class="profile-pic">
                    <span class="unsubscribe-text">
                        <g:message code="tools.contact.unsubscribe.success.text" args="[user.name]"/>
                    </span>
                </div>
            </div>
        </div>
    </div>
</content>



%{--<content tag="cColumn">--}%
    %{--<section class="panel panel-default unsubscribe-panel">--}%
        %{--<div class="panel-heading">--}%
            %{--<h3 class="panel-title"><g:message code="tools.contact.unsubscribe.panel.title" args="[contact.name]"/></h3>--}%
        %{--</div>--}%
        %{--<div class="panel-body text-center">--}%

            %{--<g:form mapping="userUnsubscribe" params="[userId:user.id.toString()]" method="POST">--}%
                %{--<input type="hidden" name="email" value="${contact.email}"/>--}%
                %{--<input type="hidden" name="digest" value="${digest}"/>--}%
                %{--<fieldset class="row">--}%
                    %{--<div class="form-group col-md-12">--}%
                        %{--<input type="submit" class="btn btn-blue inverted col-md-12" value="${g.message(code:'tools.contact.unsubscribe.panel.form.submit')}">--}%
                    %{--</div>--}%
                %{--</fieldset>--}%
            %{--</g:form>--}%
        %{--</div>--}%
    %{--</section>--}%
%{--</content>--}%