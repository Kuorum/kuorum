<div id="proposal_${proposal.id}" class="conversation-box">
    <div class="header clearfix">
        <userUtil:showUserByAlias alias="${proposal.userAlias}" extraCss="pull-left"/>
        <span class="time-ago middle-point left"><kuorumDate:humanDate date="${proposal.datePublished}"/> </span>
        <ul class="icons pull-right">
            <li>
                <button class="pin-propusal ${proposal.pined?'active':''}" type="button" data-toggle="tooltip" data-placement="bottom" title="" data-original-title="Pin this proposal if you think it is useful for you or your team">
                    <span class="fa-stack fa-lg" aria-hidden="true">
                        <span class="fa fa-circle dark fa-stack-2x"></span>
                        <span class="fa fa-flag-o fa-stack-1x fa-inverse"></span>
                    </span>
                </button>
            </li>
        </ul>
    </div>
    <div class="body">
        <p>
            ${proposal.body}
        </p>
    </div>
    <div class="actions">
        <button type="button" class="btn-see-more stack" data-anchor="conversation-box">
            <span class="pull-left fa-stack fa-lg" aria-hidden="true">
                <span class="fa fa-circle dark fa-stack-2x"></span>
                <span class="angle fa fa-stack-1x fa-inverse fa-angle-down"></span>
            </span>
        </button>
    </div>

    <div class="footer">
        <div class="clearfix">
            <ul class="social pull-left">
                <li>
                    <a href="#" target="_blank">
                        <span class="fa-stack fa-lg" aria-hidden="true">
                            <span class="fa fa-circle dark fa-stack-2x"></span>
                            <span class="fa fa-twitter fa-stack-1x fa-inverse"></span>
                        </span>
                    </a>
                </li>
                <li>
                    <a href="#" target="_blank">
                        <span class="fa-stack fa-lg" aria-hidden="true">
                            <span class="fa fa-circle dark fa-stack-2x"></span>
                            <span class="fa fa-facebook fa-stack-1x fa-inverse"></span>
                        </span>
                    </a>
                </li>
                <li>
                    <a href="#" target="_blank">
                        <span class="fa-stack fa-lg" aria-hidden="true">
                            <span class="fa fa-circle dark fa-stack-2x"></span>
                            <span class="fa fa-linkedin fa-stack-1x fa-inverse"></span>
                        </span>
                    </a>
                </li>
                <li>
                    <a href="#" target="_blank">
                        <span class="fa-stack fa-lg" aria-hidden="true">
                            <span class="fa fa-circle dark fa-stack-2x"></span>
                            <span class="fa fa-google-plus fa-stack-1x fa-inverse"></span>
                        </span>
                    </a>
                </li>
            </ul>

            <div class="comment-counter pull-right">
                <button type="button" class="delete">
                    <span class="middle-point right delete">delete</span>
                </button>
                <button type="button" class="comment">
                    <span class="fa fa-comment-o" aria-hidden="true"></span>
                    <span class="number">36</span>
                </button>
                <button type="button">
                    <span class="fa fa-heart-o" aria-hidden="true"></span>
                    <span class="number">36</span>
                </button>
            </div>
        </div>
    </div>
</div>