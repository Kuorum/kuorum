<sec:ifNotGranted roles="ROLE_POLITICIAN">
    <g:if test="${user.requestedPolitician}">
        <div class="ico-info">
            <span class="popover-trigger" data-trigger="manual" rel="popover" tabindex="100" role="button" data-toggle="popover" id="seeMore-politicalLeaningIndex">
                <span class="fa fa-info-circle front">
                    <span class="sr-only"><g:message code="dashboard.userProfile.advise.politicianRequest.text"/></span>
                </span>
                <span class="fa fa-info-circle border"></span>
                <span class="fa fa-info-circle shadow"></span>
            </span>
            <div class="popover">
                <g:message code="dashboard.userProfile.advise.politicianRequest.text"/>
            </div>
        </div>
    </g:if>
</sec:ifNotGranted>