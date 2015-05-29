<formUtil:validateForm bean="${basicPersonalDataCommand}" form="basicUserDataForm"/>
<g:form url="[mapping:'projectVoteNoTotalUser', params:project.encodeAsLinkProperties()]" method="post" name="basicUserDataForm" role="form" autocomplete="off" >
    <div class="form-group">
        <formUtil:selectNation command="${basicPersonalDataCommand}" field="country" cssClass="sr-only"/>
    </div>
    <div class="row">
        <div class="form-group col-xs-6">
            <formUtil:input command="${basicPersonalDataCommand}" field="postalCode" labelCssClass="sr-only" showCharCounter="false"/>
        </div>
        <div class="form-group col-xs-6 userData">
            <formUtil:input command="${basicPersonalDataCommand}" field="year" labelCssClass="sr-only"/>
        </div>
    </div>
    <div class="form-group groupRadio">
        <formUtil:radioEnum command="${basicPersonalDataCommand}" field="gender" labelCssClass="sr-only"/>
    </div>
    <div class="form-group voting" id="partialUserTryingToVote">
        <ul>
            <li>
                <a href="#" role="button" data-voteType="${kuorum.core.model.VoteType.POSITIVE}"><span class="icon-smiley fa-3x"></span><g:message code="project.vote.yes"/></a>
            </li>
            <li>
                <a href="#" role="button" data-voteType="${kuorum.core.model.VoteType.NEGATIVE}"><span class="icon-sad fa-3x"></span><g:message code="project.vote.no"/></a>
            </li>
            <li>
                <a href="#" role="button" data-voteType="${kuorum.core.model.VoteType.ABSTENTION}"><span class="icon-neutral fa-3x"></span><g:message code="project.vote.abs"/></a>
            </li>
            <li>
                <a href="#" role="button" data-voteType="" class="design"><span class="fa fa-lightbulb-o fa-3x"></span><g:message code="project.vote.newPost"/></a>
            </li>
        </ul>
    </div>
    <input type="hidden" name="voteType"/>
</g:form>