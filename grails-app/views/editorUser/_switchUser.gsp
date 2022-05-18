<sec:ifNotSwitched>
%{--<sec:ifAllGranted roles='ROLE_SWITCH_USER'>--}%
%{--<sec:ifAllGranted roles='ROLE_ADMIN'>--}%
    <a href="#" class="switchUserLink"><g:message code="switchUser.admin.switchUser" args="[user.alias]"/></a>

    <form class="switchUserForm" action='${request.contextPath}/j_spring_security_switch_user' method='POST'
          class="hidden">
        <input type='text' name='j_username' value="${user.email}" style="display: none"/>
    </form>
    <r:script>
        $(function () {
            $(".switchUserLink").on('click', function (e) {
                e.preventDefault();
                $(this).siblings(".switchUserForm").submit();
            })
        })
    </r:script>
%{--</sec:ifAllGranted>--}%
</sec:ifNotSwitched>