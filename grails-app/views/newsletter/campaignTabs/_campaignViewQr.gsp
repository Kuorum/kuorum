<div class="qr-code-container">
    <div class="qr-code-title">
        <h1>
            <g:message code="tools.massMailing.view.qrView.title"/>
        </h1>

        <h2>
            <g:message code="tools.massMailing.view.qrView.subtitle"/>
        </h2>
    </div>

    <div class="qr-code-body">
        <div class="box-ppal center qr-code-body-image">
            <h3><g:message code="tools.massMailing.view.qrView.section.image.title"/></h3>

            <div id="qr-code" class="text-center" data-qr-width="250" data-qr-height="250"
                 data-qr-url="${g.createLink(mapping: 'joinDomainCheck', params: [qrCode: campaign.qrCode])}">
                <!--Aqui va el QR-->
            </div>
        </div>

        <div class="box-ppal center qr-code-body-text">
            <div class="qr-text-container">
                <div class="qr-text-link">
                    <h3><g:message code="tools.massMailing.view.qrView.section.pin.link.title"/></h3>
                    <g:link mapping="joinDomain">
                        <g:createLink mapping="joinDomain"/>
                    </g:link>
                </div>

                <div class="qr-text-pin">
                    <h3><g:message code="tools.massMailing.view.qrView.section.pin.link.pin.title"/></h3>

                    <p><g:message code="tools.massMailing.view.qrView.section.pin.link.pin.desc"/></p>
                    <g:link mapping="joinDomainCheck" params="[qrCode: campaign.qrCode]">${campaign.qrCode}</g:link>
                </div>
            </div>
        </div>
    </div>
</div>