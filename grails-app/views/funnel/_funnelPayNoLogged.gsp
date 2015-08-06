<div role="tabpanel">
    <!-- Nav tabs -->
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active">
            <label data-target="#tengoCuenta"><input id="no-tengo" name="cuenta" type="radio" checked><g:message code="funnel.payment.noAccount"/></label>
        </li>
        <li role="presentation">
            <label data-target="#noTengoCuenta"><input id="tengo" name="cuenta" type="radio"><g:message code="funnel.payment.account"/></label>
        </li>
    </ul>
    <!-- Tab panes -->
    <div class="tab-content">
        <div role="tabpanel" class="tab-pane fade in active" id="tengoCuenta">
            <formUtil:validateForm bean="${command}" form="sign-comprar"/>
            <g:form mapping="funnelSubscription" role="form" class="form-inline funnelFormDetectUser" name="sign-comprar" method="post">
                <input type="hidden" name="offerType" value="${offerType}"/>
                <div class="form-group">
                    <formUtil:input command="${command}" field="name" labelCssClass="sr-only" showCharCounter="false"/>
                </div>
                <div class="form-group">
                    <formUtil:input command="${command}" field="email" labelCssClass="sr-only"/>
                </div>
            %{--<div class="row tipo-pago">--}%
            %{--<div class="col-sm-6">--}%
            %{--<a href="#" class="pago tarjeta">--}%
            %{--<span>Pagar con Tarjeta</span>--}%
            %{--</a>--}%
            %{--</div>--}%
            %{--<div class="col-sm-6">--}%
            %{--<a href="#" class="pago paypal">--}%
            %{--<span>Pagar con Paypal</span>--}%
            %{--</a>--}%
            %{--</div>--}%
            %{--</div>--}%
                <div class="form-group">
                    <input type="submit" value="${g.message(code:'funnel.payment.button')}" class="btn btn-lg">
                </div>
            </g:form>
        </div>
        <div role="tabpanel" class="tab-pane fade funnelFormDetectUser" id="noTengoCuenta">
            <formUtil:validateForm bean="${command}" form="sign-comprar"/>
            <g:form mapping="funnelLoggin" role="form" class="form-inline" name="login-comprar" method="post">
                <input type="hidden" name="offerType" value="${offerType}"/>
                <div class="form-group">
                    <formUtil:input command="${command}" field="email" labelCssClass="sr-only" showCharCounter="false"/>
                </div>
                <div class="form-group">
                    <formUtil:password command="${command}" field="password"/>
                </div>
                <div class="form-group">
                    <input type="submit" value="${g.message(code:'funnel.payment.button')}" class="btn btn-lg">
                </div>
            </g:form>
        </div>
    </div>
</div>