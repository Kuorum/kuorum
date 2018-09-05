<g:set var="percentageComplete" value="${g.formatNumber(number:emptyEditableData.percentage, type:"number",maxFractionDigits:0)}%"/>
<div class="box-ppal" id="politicianProfile">
    <div class="user" itemscope="" itemtype="http://schema.org/Person">
        <div id="profileInfo" class="clearfix">
            <div id="progressProfile">
                <div id="progressProfileFill" data-progress="${percentageComplete}"></div>
                <img src="${image.userImgSrc(user:user)}" alt="${user.name}" class="user-img big" itemprop="image">
            </div>
            <div id="progressLine"><div id="progressLineFill" data-progress="${percentageComplete}"></div></div>
            <span>
                <a href="#" role="button" id="openAddInfo" class="dropdown-toggle" data-toggle="dropdown">
                    <g:message code="dashboard.payment.profile.percentageComplete.percentage" args="[percentageComplete]"/>
                    <span class="fas fa-caret-down fa-lg"></span>
                </a>
                <ul id="addInfo" class="dropdown-menu dropdown-menu-right" aria-labelledby="openAddInfo" role="menu">
                    <g:each in="${emptyEditableData.fields}" var="fieldToCheck">
                        <g:if test="${fieldToCheck.uncompleted}">
                            <li>
                                <g:link mapping="${fieldToCheck.urlMapping}" itemprop="url">
                                    <span class="text"><g:message code="profile.menu.${fieldToCheck.urlMapping}"/></span>
                                    <span class="counter">${fieldToCheck.completed}/${fieldToCheck.total}</span>
                                </g:link>
                            </li>
                        </g:if>
                    </g:each>
                </ul>

            </span>
        </div>
        <g:link mapping="userShow" params="${user.encodeAsLinkProperties()}" itemprop="url">
            <h2>${user.name}</h2>
        </g:link>
        <h3><userUtil:userRegionName user="${user}"/></h3>
        <ul class="activity">
            <li class="followers">
                <userUtil:counterFollowers user="${user}"/>
            </li>
            <li class="following">
                <userUtil:counterFollowing user="${user}"/>
            </li>
            <li class="posts">
                <span>${numberCampaigns}</span>
                <g:message code="dashboard.payment.profile.campaigns"/>
            </li>
        </ul>
    </div>
</div>