<!-- ^survey-question-single-answer !-->
<li class="comment-box survey-question single-answer no-padding ${question.completed?'answered':''}" data-question-id="${question.id}" data-numAnswers="${question.amountAnswers}" >
    <div class="survery-question-title padding-box">
        ${question.text}
    </div>
    <div class="survey-question-answers" data-answer-selected="">
        <g:each in="${question.options}" var="option">
            <g:render template="/survey/showModules/questions/singleQuestionOption" model="[survey:survey, question:question, option:option]"/>
        </g:each>
    </div>
    <div class="footer padding-box">
        <g:render template="/campaigns/showModules/campaignDataSocial" model="[campaign:survey]"/>
        <div class="actions next-section pull-right">
            <a href="#" target="_blank">
                <g:message code="survey.questions.footer.ownerNext"/>
            </a>
            <button
                    type="button"
                    class="btn btn-blue btn-lg disabled"
                    data-clicked="false"
                    data-userloggedalias=""
                    data-posturl="https://kuorum.org/ajax/addProposal"
                    data-debateid="108"
                    data-debatealias="toledoparticipa">
                <g:message code="survey.questions.footer.next"/>
            </button>
        </div>
    </div>
</li><!-- ^survey-question-single-answer !-->