<!-- Modal registro/login -->
<div class="modal fade modal-user-picture"  tabindex="-1" role="dialog" aria-labelledby="registroLoginUsuario" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
%{--            <div class="modal-header">--}%
%{--                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true" class="fal fa-times-circle fa"></span><span class="sr-only">Cerrar</span></button>--}%
%{--                <h4>${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name}</h4>--}%
%{--                <h4 class="sr-only" id="registroLoginUsuario">Registro / Login usuario</h4>--}%
%{--            </div>--}%
            <div class="modal-body">
                <img alt="${message(code:'page.politicianProfile.imageAvatar.alt', args: [user.fullName])}"
                     class="img-circle"
                     src="${image.userImgSrc(user:user)}"
                     itemprop="image">
            </div>
        </div>
    </div>
</div>