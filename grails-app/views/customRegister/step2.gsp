<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login"/> </title>
    <meta name="layout" content="register1ColumnLayout">
</head>

<content tag="title">
    Sign up
</content>

<content tag="mainContent">
    <ol class="stepsSign">
        <li>1</li>
        <li>2</li>
        <li class="active">3</li>
    </ol>
    <formUtil:validateForm bean="${command}" form="sign" autocomplete="off"/>
    <g:form mapping="register" name="sign" role="form" method="POST" autocomplete="off"  class="signup">
        <fieldset class="row">
            <div class="form-group col-md-6">
                <formUtil:input
                        command="${command}"
                        field="alias"
                        cssClass="form-control input-lg"
                        showLabel="true"
                        showCharCounter="false"
                        required="true"/>
            </div>
            <div class="form-group col-md-6">
                <input type="text" name="autocompleteNameOff" style="display:none" data-ays-ignore="true"/>
                <formUtil:password
                        command="${command}"
                        field="password"
                        showLabel="true"
                        cssClass="form-control input-lg"
                        required="true"/>
            </div>
        </fieldset>
        <fieldset class="row">
            <div class="form-group col-md-6">
                <formUtil:selectEnum
                        command="${command}"
                        field="language"
                        showLabel="true"/>
            </div>
            <div class="form-group col-md-6">
                <div class="pull-left prefix">
                    <formUtil:input command="${command}" field="phonePrefix" showLabel="true"/>
                </div>
                <div class="pull-left phone">
                    <formUtil:input command="${command}" field="phone" showLabel="true"/>
                </div>
                <span class="help-block">
                    <g:message code="kuorum.web.commands.customRegister.Step2Command.phone-phonePrefix.helpBlock"/>
                </span>
            </div>
        </fieldset>

        <div class="form-group">
            <label><g:message code="customRegister.step2.choseUserType.label"/> </label>
            <input type="submit" value="${g.message(code:'customRegister.step2.choseUserType.politician')}" class="btn btn-lg">
            <input type="submit" value="${g.message(code:'customRegister.step2.choseUserType.citizen')}" class="btn btn-blue btn-lg">
        </div>
    </g:form>
    <script>
        $(document).ready(function() {
            $('input[name=name]').focus();
        });
    </script>
</content>

