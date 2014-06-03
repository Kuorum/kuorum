<li class="activist">
    <div class="row">
        <span class="col-md-8">
            <kuorumDate:humanDate date="${debate.dateCreated}"/>
        </span>
        <div itemtype="http://schema.org/Person" itemscope="" itemprop="author" class="col-md-4 user author text-right">
            <userUtil:showUser user="${debate.kuorumUser}" showRole="true"/>
        </div><!-- /autor -->
    </div>
    <p>${debate.text}</p>
</li>