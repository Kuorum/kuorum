<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.deleteAccount"/> </title>
    <meta name="layout" content="leftMenuConfigLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenuAccount" model="[user:user, activeMapping:'profileDeleteAccount', menu:menu]"/>
</content>
<content tag="titleContent">
    <h1><g:message code="profile.menu.profileDeleteAccount"/></h1>
    <h3><g:message code="profile.menu.profileDeleteAccount.subtitle"/></h3>
</content>
<content tag="mainContent">
    <g:form mapping="profileDeleteAccount" role="form" name="deleteAccountForm">
        %{--<h1><g:message code="profile.deleteAccount.title"/></h1>--}%
        <input type="hidden" name="forever" value=""/>
        <p><g:message code="profile.deleteAccount.requestExplanation"/></p>
        <div class="form-group textarea">
            <formUtil:textArea command="${command}" field="explanation" id="why" cssLabel="sr-only" rows="3"/>
            %{--<label for="why" class="sr-only"><g:message code="profile.deleteAccount.why"/></label>--}%
            %{--<textarea id="why" class="form-control" rows="3"></textarea>--}%
        </div>
        <fieldset class="form-group text-right">
        <div class="form-group">
            <!-- <input type="submit" value="Os voya a dar una segnuda oportunidad, pero quiero que leáis esto" class="btn btn-grey btn-lg"> -->
            <!-- como input es muy largo y no podemos meter salto de línea, así que uso button -->
            <a href="#" class="btn btn-lg btn-grey inverted"><g:message code="profile.deleteAccount.deleteForever"/></a>
            <button type="button" class="btn btn-blue btn-lg"><g:message code="profile.deleteAccount.oneChance"/></button>
        </div>
        </fieldset>
    </g:form>
    <r:script>
        $("#deleteAccountForm a").on("click", function(e){
            e.preventDefault();
            $("#deleteAccountForm input[name=forever]").val("true");
            $("#deleteAccountForm").submit()
        });
        $("#deleteAccountForm button").on("click", function(e){
            e.preventDefault();
            $("#deleteAccountForm input[name=forever]").val("false");
            $("#deleteAccountForm").submit()
        });
    </r:script>
</content>
