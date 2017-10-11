<sec:ifAnyGranted roles="ROLE_ADMIN">
    <g:render template="/admin/superAdminMenu" model="[activeMapping:activeMapping]"/>
</sec:ifAnyGranted>