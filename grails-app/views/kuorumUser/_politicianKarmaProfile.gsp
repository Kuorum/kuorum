<section class="boxes userkarma">
    <h1><span class="fa fa-user"></span><g:message code="kuorumUser.show.politiciankarma.title"/> </h1>
    <p><g:message code="kuorumUser.show.politiciankarma.description"/></p>
    <ul class="activity">
        <li>
            <span class="counter">${politicianStats.postDefended}</span>
            <br><g:message code="kuorumUser.show.politiciankarma.postDefended"/>
        </li>
        <li class="victories">
            <span class="counter">${politicianStats.victories}</span>
            <br><g:message code="kuorumUser.show.politiciankarma.vicotries"/>
        </li>
        <li>
            <span class="counter">${politicianStats.debates}</span>
            <br><g:message code="kuorumUser.show.politiciankarma.debates"/>
        </li>
    </ul>
    <userUtil:followButton user="${user}" cssSize="btn-lg"/>
</section>