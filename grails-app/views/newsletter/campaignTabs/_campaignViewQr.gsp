<h2 class="sr-only"><g:message code="tools.massMailing.view.qrView"/></h2>
<div class="container-fluid qr-code-container">
    <div class="row">
        <div id="qr-code" class="text-center" data-qr-width="256" data-qr-height="256">
            <!--Aqui va el QR-->
        </div>
    </div>
    <div class="row">
        <div class="text-center qr-text-container">
            <a href="${"https://" + kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.domain + "/join/" + campaign.qrCode}">
                <span class="qr-code-text text-center">${campaign.qrCode}</span>
            </a>
        </div>
    </div>
</div>