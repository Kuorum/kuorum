<sec:ifAnyGranted roles="ROLE_ADMIN">
    <g:render template="/admin/superAdminMenu" model="[activeMapping:activeMapping, menu:menu]"/>
</sec:ifAnyGranted>
<sec:ifAnyGranted roles="ROLE_POLITICIAN">
    <g:render template="/admin/politicianAdminMenu" model="[activeMapping:activeMapping, menu:menu]"/>
</sec:ifAnyGranted>