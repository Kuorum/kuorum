<g:if test="${!user.editorRules || !user.editorRules.discardEditorWarns || user.editorRules.requestedEditor==null}">
    <section class="panel panel-default collaborate">
        <div class="panel-heading">
            <h3 class="panel-title"><g:message code="modules.ipdbRecruitment.title"/></h3>
        </div>
        <div class="panel-body">
            <p><g:message code="modules.ipdbRecruitment.text"/></p>
            <ul>
                <li><a href="#"><g:message code="modules.ipdbRecruitment.buttons.neverAsk"/></a></li>
                <li><a href="#" class="btn btn-blue inverted"><g:message code="modules.ipdbRecruitment.buttons.notNow"/></a></li>
                <li><a href="#" class="btn btn-blue"><g:message code="modules.ipdbRecruitment.buttons.yes"/></a></li>
            </ul>
        </div>
    </section>
</g:if>