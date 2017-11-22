<!-- politician valuation -->
<div class="user-rating-container">
    <div class="popover-trigger user-rating" class="popover-trigger" data-trigger="manual" rel="popover" role="button" data-toggle="popover">
        <g:each in="${(1..5)}" var="i">
            <label for="star${i}" class="${Math.round(userReputation.userReputation)==i?'active':''}">
                <span><g:message code="politician.valuation.rate.value.text.${i}"/></span>
            </label>
        </g:each>
    </div>

    <!-- POPOVER PARA VOTACIÓN -->
    <div class="popover">
        <form action="${g.createLink(mapping:'userRate', params:user.encodeAsLinkProperties(), absolute:true)}" method="post" class="user-rating-form rating rating-profile">
            <input type="hidden" name="politicianId" value="${user.id}"/>
            <fieldset class="rating">
                <legend class="sr-only"><g:message code="politician.valuation.rate"/></legend>
                <g:each in="${(1..5).reverse()}" var="i">
                    <input id="star${i}_${user.id}" type="radio" name="rating" value="${i}" ${userReputation.evaluation==i?'checked':''}>
                    <label for="star${i}_${user.id}">
                        <g:message code="politician.valuation.rate.value.starts" args="[i]"/>
                        %{--<span class="rate-message"><g:message code="politician.valuation.rate.value.text.${i}"/></span>--}%
                    </label>
                </g:each>
            </fieldset>
        </form>
        <div class="rating-info">
            <div class="rate-number show${Math.round(userReputation.userReputation)}">
                <label><g:message code="politician.valuation.rate.myRate"/>: </label>
                <span class="counter">${userReputation.evaluation?:'-'}</span>
            </div>
            <div class="rate-number">
                <label><g:message code="politician.valuation.rate.numVotes"/>: </label>
                <span class="counter">${userReputation.numberEvaluations?:'0c'}</span>
            </div>
        </div>

        <hr>

        <!-- Statistics -->
        <div class="rating-over">
            <!-- indicar la clase de la puntuación: show1, show2, show3, show4, show5 -->
            <div class="rate-number show${Math.round(userReputation.userReputation)}">
                <label><g:message code="politician.valuation.rate.average"/>:</label>
                <span class="counter user-reputation">
                    <g:formatNumber number="${userReputation.userReputation}" maxFractionDigits="2" minFractionDigits="2"/>
                </span>
            </div>
            <span class="sr-only"><g:message code="politician.valuation.distribution"/>:</span>
            <ul>
                <g:each in="${1..5}" var="i">
                    <li>
                        <span class="star">${i} <span>★</span></span>
                        <g:set var="percentageStars" value="${Math.round((userReputation.evaluationPercentages.get(i)?:0)*100)}"/>
                        %{--<g:if test="${percentageStars > 0}">--}%
                        <div class="progress">
                            <div class="progress-bar rate-progress-bar-${i}" role="progressbar" aria-valuetransitiongoal="${percentageStars}" aria-valuemin="0" aria-valuemax="100"><span class="sr-only">${percentageStars}%</span></div>
                        </div>
                        %{--</g:if>--}%
                    </li>
                </g:each>
            </ul>
        </div>
    </div>

    <g:render template="/kuorumUser/userShowTemplates/modals/modalRatingShare" model="[user: user]"/>
</div>
