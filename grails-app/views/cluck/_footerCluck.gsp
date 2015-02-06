<footer class="row">
    %{--<g:render template="/cluck/footerCluck/footerCluckPostType" model="[post:post, displayingColumnC:displayingColumnC]"/>--}%
    <ul class="col-xs-2 col-sm-2 col-md-4 info-kak">
        <li itemprop="keywords">
            <projectUtil:showProjectRegionIcon project="${post.project}"/>
        </li>
        <li class="hidden-xs hidden-sm" itemprop="datePublished">
            <kuorumDate:humanDate date="${post.dateCreated}"/>
        </li>
    </ul>
    <sec:ifLoggedIn>
        <ul class="col-xs-10 col-sm-10 col-md-8 actions-kak">
            <g:render template="/cluck/footerCluck/footerCluckReadLater" model="[post:post, displayingColumnC:displayingColumnC]"/>
            <g:render template="/cluck/footerCluck/footerCluckLikeButton" model="[post:post, displayingColumnC:displayingColumnC]"/>
            <g:render template="/cluck/footerCluck/footerCluckKakareoButton" model="[post:post, displayingColumnC:displayingColumnC]"/>
            <g:render template="/cluck/footerCluck/footerCluckMoreActions" model="[post:post]"/>
        </ul>
    </sec:ifLoggedIn>
</footer>
