
<li>
    <g:render template="/debate/showModules/mainContent/proposalDataInfo" model="[debate:debate,debateUser:debateUser, proposal:proposal]"/>
    <g:render template="/debate/showModules/mainContent/proposalDataComments" model="[debate:debate,proposal:proposal]"/>
</li>