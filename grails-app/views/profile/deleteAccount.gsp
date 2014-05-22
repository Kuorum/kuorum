<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.deleteAccount"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="profile.deleteAccount.salutation"/>,
        <br>
        <span class="user-name">${user.name}</span>
    </h1>
    <p><g:message code="profile.deleteAccount.description"/></p>
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileDeleteAccount', menu:menu]"/>

</content>
<content tag="mainContent">
    <h1><g:message code="profile.deleteAccount.title"/></h1>
    <g:form mapping="profileDeleteAccount" role="form" name="deleteAccountForm">
        <input type="hidden" name="forever" value=""/>
        <p><g:message code="profile.deleteAccount.requestExplanation"/></p>
        <div class="form-group textarea">
            <formUtil:textArea command="${command}" field="explanation" id="why" cssLabel="sr-only" rows="3"/>
            %{--<label for="why" class="sr-only"><g:message code="profile.deleteAccount.why"/></label>--}%
            %{--<textarea id="why" class="form-control" rows="3"></textarea>--}%
        </div>
        <p><g:message code="profile.deleteAccount.requestExplanation2"/></p>
        <div class="form-group">
            <!-- <input type="submit" value="Os voya a dar una segnuda oportunidad, pero quiero que leáis esto" class="btn btn-grey btn-lg"> -->
            <!-- como input es muy largo y no podemos meter salto de línea, así que uso button -->
            <button type="button" class="btn btn-grey btn-lg"><g:message code="profile.deleteAccount.oneChance"/></button>
            <a href="#" class="cancel"><g:message code="profile.deleteAccount.deleteForever"/></a>
        </div>
    </g:form>
</content>
