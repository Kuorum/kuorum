<footer class="row">
    <g:render template="/cluck/footerCluck/footerCluckPostType" model="[post:post, displayingColumnC:true]"/>
    <sec:ifLoggedIn>
        <ul class="col-xs-10 col-sm-10 col-md-8 actions-kak">
            <g:render template="/cluck/footerCluck/footerCluckReadLater" model="[post:post, displayingColumnC:true]"/>
            <g:render template="/cluck/footerCluck/footerCluckLikeButton" model="[post:post, displayingColumnC:true]"/>
            <g:render template="/cluck/footerCluck/footerCluckKakareoButton" model="[post:post, displayingColumnC:true]"/>
            <g:render template="/cluck/footerCluck/footerCluckMoreActions" model="[post:post]"/>
        </ul>
    </sec:ifLoggedIn>
</footer>