<div class="container-fluid">
    <div class="row">
        <div class="col-xs-12 col-sm-3">
            <div itemtype="http://schema.org/Person" itemscope class="user">
                <userUtil:showUser user="${user}"/>
            </div>
        </div>
        <div class="col-xs-12 col-sm-6">
            <ul class="activity">
                <g:render template="/kuorumUser/userRecordsLi" model="[user:user]"/>
            </ul>
        </div>
        <div class="col-xs-12 col-sm-3">
            <userUtil:followButton user="${user}" cssSize="btn-xs" cssExtra="follow"/>
            %{--<button type="button" class="btn btn-blue pull-right follow allow">Seguir</button>--}%
        </div>
    </div>
</div>