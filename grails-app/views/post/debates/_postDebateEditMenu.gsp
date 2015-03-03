<sec:ifAnyGranted roles="ROLE_ADMIN">
    <!-- FLECHITA PARA ABRIR MENÚ -->
    <span class="popover-trigger open-menu" rel="popover" role="button" data-toggle="popover">
        <span class="fa fa-chevron-down"></span>
        <span class="sr-only">Ver opciones edición</span>
    </span>
    <!-- POPOVER OPCIONES EDICIÓN -->
    <div class="popover">
        <div class="popover-more-actions edition">
            <ul>
                <li>
                    <a href="#">
                        <span>Publicar</span>
                    </a>
                </li>
                <li>
                    <a href="#">
                        <span>Editar</span>
                    </a>
                </li>
                <li>
                    <a href="#" data-toggle="modal" data-target="#eliminar-debate">Eliminar</a>
                </li>
            </ul>
        </div>
    </div>
    <!-- FIN POPOVER OPCIONES EDICIÓN -->

    <!-- Modal eliminar-debate -->
    <div class="modal fade" id="eliminar-debate" tabindex="-1" role="dialog" aria-labelledby="eliminarDebate" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                    <h4 class="modal-title" id="eliminarDebate">Eliminar esta entrada del debate</h4>
                </div>
                <div class="modal-body clearfix">
                    <p>¿Está seguro de que desea eliminar esta entrada del debate?</p>
                    <a href="#" class="btn btn-grey pull-right">Eliminar</a>
                </div>
            </div>
        </div>
    </div>
</sec:ifAnyGranted>