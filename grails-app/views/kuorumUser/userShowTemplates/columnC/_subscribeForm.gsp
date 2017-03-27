<%@ page import="springSecurity.KuorumRegisterCommand" %>
<sec:ifNotLoggedIn>
    <section class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title"><g:message code="politician.subscribe.title" args="[user.name]"/></h3>
        </div>
        <g:set var="command" value="${new springSecurity.KuorumRegisterCommand()}"/>
        <div class="panel-body text-center">

            <div class="campaign-progress-bar-wrapper">
                %{--<h4><g:message code="register.progressBar.title" /></h4>--}%
                <div class="campaign-progress-bar">
                    <div class="pop-up">
                        <g:message code="register.progressBar.popup" args="[user.name]" />
                        <div class="arrow"></div>
                    </div>
                    <div class="progress-bar-custom">
                        <div class="progress-bar-custom-done"></div>
                    </div>
                </div>
            </div>

            <formUtil:validateForm bean="${command}" form="followAndRegister"/>
            <g:form mapping="userFollowAndRegister" params="${user.encodeAsLinkProperties()}" class="login" name="followAndRegister">
                <fieldset class="row">
                    <div class="form-group col-md-12">
                        <formUtil:input command="${command}" field="name" showCharCounter="false"/>
                    </div>
                    <div class="form-group col-md-12">
                        <formUtil:input command="${command}" field="email" type="email"/>
                    </div>
                    <div class="form-group col-md-12">
                        <input type="submit" class="btn btn-blue inverted col-md-12" value="${g.message(code: 'politician.subscribe.submit')}">
                        <span><g:message code="register.conditions" args="[g.createLink(mapping: 'footerPrivacyPolicy')]" encodeAs="raw"/></span>
                    </div>
                </fieldset>
            </g:form>
        </div>
    </section>
</sec:ifNotLoggedIn>