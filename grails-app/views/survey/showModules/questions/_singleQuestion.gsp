<!-- ^survey-question-single-answer !-->
<li class="comment-box survey-question single-answer no-padding" data-question-pos="4">
    <div class="survery-question-title padding-box">
        ${question.text}
    </div>
    <div class="survey-question-answers" data-answer-selected="" data-question-order="1">
        <g:each in="${question.options}" var="option">
            <g:render template="/survey/showModules/questions/singleQuestionOption" model="[survey:survey, question:question, option:option]"/>
        </g:each>
    </div>
    <div class="footer padding-box">
        <ul class="social">
            <li>
                <a href="#" target="_blank">
                    <span class="fa-stack" aria-hidden="true">
                        <span class="fa fa-circle dark fa-stack-2x"></span>
                        <span class="fa fa-twitter fa-stack-1x fa-inverse"></span>
                    </span>
                </a>
            </li>
            <li>
                <a href="#" target="_blank">
                    <span class="fa-stack" aria-hidden="true">
                        <span class="fa fa-circle dark fa-stack-2x"></span>
                        <span class="fa fa-facebook fa-stack-1x fa-inverse"></span>
                    </span>
                </a>
            </li>
            <li>
                <a href="#" target="_blank">
                    <span class="fa-stack" aria-hidden="true">
                        <span class="fa fa-circle dark fa-stack-2x"></span>
                        <span class="fa fa-linkedin fa-stack-1x fa-inverse"></span>
                    </span>
                </a>
            </li>
            <li>
                <a href="#" target="_blank">
                    <span class="fa-stack" aria-hidden="true">
                        <span class="fa fa-circle dark fa-stack-2x"></span>
                        <span class="fa fa-google-plus fa-stack-1x fa-inverse"></span>
                    </span>
                </a>
            </li>
        </ul>
        <div class="actions next-section pull-right">
            <a href="#" target="_blank">I am the survey owner, skip questions</a>
            <button type="button" class="btn btn-blue btn-lg publish publish-proposal disabled" data-clicked="false" data-userloggedalias="" data-posturl="https://kuorum.org/ajax/addProposal" data-debateid="108" data-debatealias="toledoparticipa">
                Next
            </button>
        </div>
    </div>
</li><!-- ^survey-question-single-answer !-->