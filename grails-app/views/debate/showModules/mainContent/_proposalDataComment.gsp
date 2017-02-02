<li class="conversation-box-comment" id="comment_${comment.id}">
    <div class="header clearfix">
        <userUtil:showUserByAlias alias="${comment.userAlias}" extraCss="pull-left"/>
        <span class="time-ago middle-point left"><kuorumDate:humanDate date="${comment.datePublished}"/> </span>
    </div>
    <div class="body">
        ${raw(comment.body)}
    </div>
    <div class="footer clearfix">
        <div class="pull-right">
            <button type="button">
                <span class="middle-point right delete">delete</span>
            </button>

            <button type="button" class="angle">
                <span class="fa fa-angle-up" aria-hidden="true"></span>
            </button>
            <button type="button" class="angle">
                <span class="fa fa-angle-down" aria-hidden="true"></span>
            </button>

            <span class="number">${comment.votes}</span>
        </div>
    </div>
</li>