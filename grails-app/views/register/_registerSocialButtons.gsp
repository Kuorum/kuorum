<ul class="socialGo clearfix">
    %{--<li><a href="#" class="btn tw"><span class="fa fa-twitter fa-lg"></span> Entrar con Twitter</a></li>--}%
    <li>
        <oauth:connect provider="facebook" id="facebook-connect-link" class="btn btn-lg fb">
            <span class="fab fa-facebook-f fa-lg"></span>
            <g:message code="login.rrss.facebook"/>
        </oauth:connect>
    </li>
    <li>
        <oauth:connect provider="google" id="google-connect-link" class="btn btn-gog btn-lg">
            %{--<span class="google-logo"></span>--}%
            <g:message code="login.rrss.google"/>
        </oauth:connect>
    </li>

    %{--<li><a href="#" class="btn lin"><span class="fab fa-linkedin-in fa-lg"></span> Entrar con LinkedIn</a></li>--}%
</ul>

%{--<script src="http://localhost:8181/kuorumRest/login/social/socialLogin.js" ></script>--}%
<r:require module="loginApi"/>
