<sec:access expression="hasPermission('${user.id}', 'kuorum.users.KuorumUser', 'edit')">
        <!-- FLECHITA PARA ABRIR MENÚ -->
    <span class="popover-trigger open-menu" rel="popover" role="button" data-toggle="popover">
        <span class="fa fa-chevron-circle-down"></span>
        <span class="sr-only"><g:message code="project.list.show.options"/></span>
    </span>
    <!-- POPOVER OPCIONES EDICIÓN -->
    <div class="popover">
        <div class="popover-more-actions edition">
            <ul>
                <li>
                    <sec:ifAnyGranted roles="ROLE_EDITOR">
                        <g:link mapping="editorKuorumAccountEdit" params="${user.encodeAsLinkProperties()}">
                            <span><g:message code="project.editMenu.edit"/></span>
                        </g:link>
                    </sec:ifAnyGranted>
                    <sec:ifAnyGranted roles="ROLE_ADMIN">
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
