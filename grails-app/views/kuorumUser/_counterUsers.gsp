<span class="ajax popover-trigger more-users" rel="popover" role="button" data-toggle="popover">
    <span class="counter">${total}</span>
</span>
${label}
<!-- POPOVER PARA SACAR LISTAS DE USUARIOS -->
<div class="popover">
    <button type="button" class="close" aria-hidden="true"  data-dismiss="popover"><span class="fal fa-times-circle fa"></span><span class="sr-only"><g:message code="kuorumUser.popover.close"/></span></button>
    <a href="${ajaxUrl}" class="hidden" rel="nofollow">${linkText}</a>
    <div class="popover-user-list">
        <p>${title}</p>
        <div class="scroll">
            <ul>

                <li>loading</li>
            </ul>
        </div><!-- /.contenedor scroll -->
    </div><!-- /popover-user-list -->
</div>