<li id="commentPos_${pos}" style="display:${display?:'block'} ">
    <script>
        function removeComment(liId){
            $("#"+liId).hide('slow')
        }
    </script>
    <div class="wrapper">
        <div class="row">
            <div class="col-md-8 user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
                <userUtil:showUser user="${comment.kuorumUser}" showRole="true"/>
            </div><!-- /autor -->
            <span class="col-md-4 text-right">
                <kuorumDate:humanDate date="${comment.dateCreated}"/>
            </span>
        </div>
        <p><span class="say">Dice:</span> ${comment.text}</p>
    </div>
    <div class="actions clearfix">
        <postUtil:ifCommentIsDeletable post="${post}" commentPosition="${pos}">
            <g:remoteLink url="[mapping:'postDelComment', params:post.encodeAsLinkProperties() + [commentPosition:pos]]" onSuccess="removeComment('commentPos_${pos}')" class="pull-left">
                <g:message code="post.show.comments.delete"/>
            </g:remoteLink>
        </postUtil:ifCommentIsDeletable>
        %{--<ul class="pull-right">--}%
            %{--<li>14 <a href="#" class="plus disabled">+</a></li>--}%
            %{--<li>23 <a href="#" class="minus">-</a></li>--}%
        %{--</ul>--}%
    </div>
</li>