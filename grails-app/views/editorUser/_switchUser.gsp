<sec:ifNotSwitched>
    %{--<sec:ifAllGranted roles='ROLE_SWITCH_USER'>--}%
    %{--<sec:ifAllGranted roles='ROLE_ADMIN'>--}%
        <a herf="#" id="switchUserLink"><g:message code="switchUser.admin.switchUser"/></a>
        <form id="switchUserForm" action='${request.contextPath}/j_spring_security_switch_user' method='POST' class="hidden">
            <input type='text' name='j_username' value="${user.email}"/><br/>
        </form>
        <r:script>
            $(function(){
                $("#switchUserLink").on('click',function(e){
                    e.preventDefault();
                    $('#switchUserForm').submit();
                })
            })
        </r:script>
    %{--</sec:ifAllGranted>--}%
</sec:ifNotSwitched>