<div class="container-fluid">
    <div class="row">
        <div class="col-xs-12 col-sm-3">
            <div itemtype="http://schema.org/Person" itemscope class="user">
                <userUtil:showUser user="${user}"/>
            </div>
        </div>
        <div class="col-xs-12 col-sm-6">
            <ul class="activity">
                <li><span class="counter">${user.gamification.numEggs}</span> <span class="icon-Flaticon_17919"></span> <br> <g:message code="profile.kuorumStore.eggs.description"/> </li>
                <li><span class="counter">${user.gamification.numCorns}</span> <span class="icon-Flaticon_20188"></span> <br> <g:message code="profile.kuorumStore.corns.description"/> </li>
                <li><span class="counter">${user.gamification.numPlumes}</span> <span class="icon-Flaticon_24178"></span> <br> <g:message code="profile.kuorumStore.plumes.description"/> </li>
            </ul>
        </div>
        <div class="col-xs-12 col-sm-3">
            <userUtil:followButton user="${user}" cssExtra="btn-xs follow allow"/>
            %{--<button type="button" class="btn btn-blue pull-right follow allow">Seguir</button>--}%
        </div>
    </div>
</div>