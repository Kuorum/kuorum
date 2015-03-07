<postUtil:ifPostIsEditable post="${post}">
    <!-- FLECHITA PARA ABRIR MENÚ -->
    <span class="popover-trigger open-menu" rel="popover" role="button" data-toggle="popover">
        <span class="fa fa-chevron-down"></span>
        <span class="sr-only"><g:message code="cluck.edit.title"/></span>
    </span>
    <!-- POPOVER OPCIONES EDICIÓN -->
    <div class="popover">
        <div class="popover-more-actions edition">
            <ul>
                <li>
                    <g:link mapping="postEdit" params="${post.encodeAsLinkProperties()}">
                        <span class="fa fa-edit fa-lg"></span><g:message code="post.show.editLink"/>
                    </g:link>
                </li>
                %{--<li>--}%
                    %{--<a href="#" data-toggle="modal" data-target="#ya-no">--}%
                        %{--<span class="">Ya no puedes editar</span>--}%
                    %{--</a>--}%
                %{--</li>--}%
            </ul>
        </div>
    </div>
</postUtil:ifPostIsEditable>
<!-- FIN POPOVER OPCIONES EDICIÓN -->