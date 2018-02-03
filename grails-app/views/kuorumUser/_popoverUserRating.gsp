<g:form mapping="userRate" params="[userAlias: user.alias]" method="POST" class="user-rating-form rating">
    <input type="hidden" name="politicianId" value="${user.id}">
    <fieldset class="rating">
        <legend class="sr-only"><g:message code="politician.valuation.rate"/></legend>
        <g:each in="${(1..5).reverse()}" var="i">
            <input id="star${i}_${user.id}" type="radio" name="rating" value="${i}" ${Math.round(userReputation.userReputation)==i?'checked':''}>
            <label for="star${i}_${user.id}">
                <g:message code="politician.valuation.rate.value.starts" args="[i]"/>
                %{--<span class="rate-message"><g:message code="politician.valuation.rate.value.text.${i}"/></span>--}%
            </label>
        </g:each>
    </fieldset>
</g:form>