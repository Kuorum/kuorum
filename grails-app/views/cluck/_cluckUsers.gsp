<div class="main-kakareo row">
    <div class="col-md-5 user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
        <userUtil:showUser user="${post.owner}" showRole="true"/>
    </div>

    <div class="col-md-7 text-right sponsor">
        <userUtil:showListUsers users="${post.sponsors.kuorumUser}" visibleUsers="1" messagesPrefix="post.show.boxes.like.userList"/>
    </div><!-- /patrocinadores -->

</div>