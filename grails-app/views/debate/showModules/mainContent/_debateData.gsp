<!-- ^leader-post !-->
<div class="leader-post">
    <g:render template="showModules/mainContent/debateDataMultimedia" model="[debate:debate]"/>

    <div class="header">
        <h1 class="title">${debate.title}</h1>

        <userUtil:showUser user="${debateUser}"/>

        <div class="clearfix">
            <span class="time-ago pull-left"><kuorumDate:humanDate date="${debate.datePublished}"/> </span>

            <div class="actions call-to-action-mobile">
                <button
                        type="button"
                        class="btn btn-blue btn-xl btn-circle"
                >
                    <span class="fa fa-angle-double-down fa-2x"></span>
                </button>
            </div>

            <userUtil:ifUserIsTheLoggedOne user="${debateUser}">
                <g:link class="edit" mapping="debateEditContent" params="${debate.encodeAsLinkProperties()}">
                    <span class="fa fa-pencil-square-o pull-right fa-2x" aria-hidden="true"></span>
                </g:link>
            </userUtil:ifUserIsTheLoggedOne>
        </div>
    </div>

    <div class="body">
        ${raw(debate.body)}
    </div>

    <div class="footer clearfix">
        %{--<g:render template="/debate/showModules/mainContent/debateDataLabels" model="[debate:debate]"/>--}%
        <g:render template="/debate/showModules/mainContent/debateDataSocial" model="[debate:debate, debateUser:debateUser]"/>

        <div class="comment-counter pull-right">
            <button type="button">
                <span class="fa fa-lightbulb-o" aria-hidden="true"></span>
                <span class="number">${proposalPage.total}</span>
            </button>
        </div>
    </div>
</div> <!-- ^leader-post !-->