<sec:ifNotGranted roles="ROLE_EDITOR">
    <g:if test="${!user.editorRules?.discardEditorWarns && !user.editorRules?.requestedEditor}">
        <section class="panel panel-default collaborate" id="module-card-ipdb-recruitment">
            <div class="panel-heading">
                <h3 class="panel-title"><g:message code="modules.ipdbRecruitment.title"/></h3>
            </div>
            <div class="panel-body">
                <p><g:message code="modules.ipdbRecruitment.text"/></p>
                <ul>
                    <li><g:link mapping="editorDiscardWarns"><g:message code="modules.ipdbRecruitment.buttons.neverAsk"/></g:link></li>
                    <li><a href="#" id="module-card-ipdb-recruitment-hideWarnButton" class="btn btn-blue inverted"><g:message code="modules.ipdbRecruitment.buttons.notNow"/></a></li>
                    <li><g:link mapping="editorRequestRights" class="btn btn-blue"><g:message code="modules.ipdbRecruitment.buttons.yes"/></g:link></li>
                </ul>
            </div>
        </section>
    </g:if>
</sec:ifNotGranted>