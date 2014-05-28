<ul class="socialGo clearfix">
    %{--<li><a href="#" class="btn tw"><span class="fa fa-twitter fa-lg"></span> Entrar con Twitter</a></li>--}%
    <li>
        <div class="hidden" id="facebookLoginContainer">
            <facebookAuth:connect id="facebookLogin" type="server"/>
        </div>
        <a href="#" onclick="$('#facebookLoginContainer a')[0].click(); return false;" class="btn fb">
            <span class="fa fa-facebook fa-lg"></span>
            <g:message code="login.rrss.facebook"/>
        </a>
    </li>
    <li>
        <oauth:connect provider="google" id="google-connect-link" class="btn gog">
            <span class="fa fa-google-plus fa-lg"></span>
            <g:message code="login.rrss.google"/>
        </oauth:connect>
    </li>
    %{--<li><a href="#" class="btn lin"><span class="fa fa-linkedin fa-lg"></span> Entrar con LinkedIn</a></li>--}%
</ul>