<footer class="row">
    <g:render template="/cluck/footerCluck/footerCluckPostType" model="[post:post, displayingColumnC:displayingColumnC]"/>
        <ul class="col-xs-10 col-sm-10 col-md-8 actions-kak">
            <g:render template="/cluck/footerCluck/footerCluckReadLater" model="[post:post, displayingColumnC:displayingColumnC]"/>
            <g:render template="/cluck/footerCluck/footerCluckLikeButton" model="[post:post, displayingColumnC:displayingColumnC]"/>
            <g:render template="/cluck/footerCluck/footerCluckKakareoButton" model="[post:post, displayingColumnC:displayingColumnC]"/>
            %{--<g:render template="/cluck/footerCluck/footerCluckMoreActions" model="[post:post]"/>--}%
            <li class="more-actions">
                <span class="popover-trigger more-actions" rel="popover" role="button">
                    <span class="fa fa-plus"></span> <span class="sr-only"><g:message code="cluck.footer.moreActions"/></span>
                </span>
            </li>
        </ul>
</footer>
