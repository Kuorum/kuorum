
<projectUtil:ifAllowedToUpdateProject project="${project}">
<!-- FLECHITA PARA ABRIR MENÚ -->
    <span class="popover-trigger open-menu" rel="popover" role="button" data-toggle="popover">
        <span class="fa fa-chevron-down"></span>
        <span class="sr-only"><g:message code="project.editMenu.title"/></span>
    </span>
    <!-- POPOVER OPCIONES EDICIÓN -->
    <div class="popover">
        <div class="popover-more-actions edition">
            <ul>
                <li>
                    <g:link mapping="projectEdit" params="${project.encodeAsLinkProperties()}">
                        <span><g:message code="project.editMenu.edit"/></span>
                    </g:link>
                </li>
                <li>
                    <g:link mapping="projectUpdate" params="${project.encodeAsLinkProperties()}">
                        <span><g:message code="project.editMenu.update"/></span>
                    </g:link>
                </li>
            </ul>
        </div>
    </div>
    <!-- FIN POPOVER OPCIONES EDICIÓN -->
</projectUtil:ifAllowedToUpdateProject>
