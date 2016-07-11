<sec:ifNotGranted roles="ROLE_EDITOR">
    <g:if test="${user?.editorRules?.requestedEditor}">
        <div class="ico-info">
            <span class="popover-trigger" data-trigger="manual" rel="popover" tabindex="100" role="button" data-toggle="popover" id="seeMore-politicalLeaningIndex">
                <span class="fa fa-info-circle front">
                    <span class="sr-only"><g:message code="dashboard.userProfile.advise.editorRequest.title"/></span>
                </span>
                <span class="fa fa-info-circle border"></span>
                <span class="fa fa-info-circle shadow"></span>
            </span>
            <div class="popover">
                <g:message code="dashboard.userProfile.advise.editorRequest.text"/>
            </div>
        </div>
    </g:if>
</sec:ifNotGranted>