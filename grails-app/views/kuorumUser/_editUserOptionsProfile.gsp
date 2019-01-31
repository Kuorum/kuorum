<sec:access expression="hasPermission('${user.id}', 'org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO', 'edit')">
        <!-- FLECHITA PARA ABRIR MENÚ -->
    <span class="popover-trigger open-menu" rel="popover" role="button" data-toggle="popover">
        <span class="fal fa-chevron-circle-down"></span>
        <span class="sr-only"><g:message code="project.list.show.options"/></span>
    </span>
    <!-- POPOVER OPCIONES EDICIÓN -->
    <div class="popover">
        <div class="popover-more-actions edition">
            <ul>
                <li>
                    <sec:access expression="hasPermission('${user.id}', 'org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO', 'edit')">
                        <userUtil:ifUserIsTheLoggedOne user="${user}">
                            <g:link mapping="profileEditUser">
                                <span><g:message code="project.editMenu.edit"/></span>
                            </g:link>
                        </userUtil:ifUserIsTheLoggedOne>
                        <userUtil:elseIfUserNotIsTheLoggedOne user="${user}">
                            <g:link mapping="editorKuorumAccountEdit" params="${user.encodeAsLinkProperties()}">
                                <span><g:message code="project.editMenu.edit"/></span>
                            </g:link>
                        </userUtil:elseIfUserNotIsTheLoggedOne>
                    </sec:access>
                    <sec:ifAnyGranted roles="ROLE_SUPER_ADMIN">
                        <g:link mapping="editorAdminUserRights" params="${user.encodeAsLinkProperties()}">
                            <span><g:message code="admin.menu.user.editRights"/></span>
                        </g:link>
                    </sec:ifAnyGranted>
                </li>
            </ul>
        </div>
    </div>
    <!-- FIN POPOVER OPCIONES EDICIÓN -->
</sec:access>
