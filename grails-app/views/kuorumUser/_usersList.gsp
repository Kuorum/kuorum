<span class="state">${messages.intro}</span>
<ul class="${cssClass}">
    <g:each in="${visibleUsersList}" var="user">
        <userUtil:showUser user="${user}" showName="false" htmlWrapper="li"/>
    </g:each>
    <g:if test="${hiddenUsersList}">
        <li>
            <span data-toggle="popover" role="button" rel="popover" class="popover-trigger more-users">
                <span class="sr-only">${messages.seeMore}</span>
                <span class="counter">+${total}</span>
            </span>
            <!-- POPOVER PARA SACAR LISTAS DE USUARIOS -->
            <div class="popover">
                <button aria-hidden="true" class="close" type="button" data-dismiss="popover"><span class="fal fa-times-circle fa"></span><span class="sr-only">Cerrar</span></button>
                <a rel="nofollow" class="hidden" href="#">${messages.showUserList}</a>
                <div class="popover-user-list">
                    <p>${messages.userListTitle}</p>
                    <div class="scroll">
                        <ul>
                            <g:render template="/kuorumUser/embebedUsersList" model="[users:hiddenUsersList]"/>
                        </ul>
                    </div><!-- /.contenedor scroll -->
                </div><!-- /popover-user-list -->

            </div>
            <!-- FIN POPOVER PARA SACAR LISTAS DE USUARIOS -->
        </li>
    </g:if>
</ul><!-- /.user-list-images -->