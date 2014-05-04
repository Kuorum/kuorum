<li class="politician">
    <div class="row">
        <div itemtype="http://schema.org/Person" itemscope="" itemprop="author" class="col-md-8 user author">
            <userUtil:showUser user="${debate.kuorumUser}" showRole="true"/>
        </div><!-- /autor -->
        <span class="col-md-4 text-right">
            <kuorumDate:humanDate date="${debate.dateCreated}"/>
        </span>
    </div>
    <p><span class="say">Dice:</span>${debate.text}</p>
</li>