<!-- ^leader-post !-->
<div class="leader-post">
    <g:render template="/campaigns/showModules/campaignDataMultimedia" model="[campaign: debate, poweredByKuorum:poweredByKuorum]"/>
    <div class="header">
        <h1 class="title" itemprop="headline">${debate.title}</h1>
        <userUtil:showUser user="${debateUser}" showRole="true" itemprop="author"/>
        <g:render template="/debate/showModules/mainContent/debateDataImageCallToAction" model="[debate:debate]"/>
        <g:render template="/campaigns/showModules/campaignDataDatePublished" model="[campaign:debate, campaignUser:debateUser, editMappingName:'debateEditContent', editable:true]"/>
    </div>

    <div class="body" itemprop="articleBody">
        ${raw(debate.body)}
    </div>

    <div class="footer clearfix">
        <g:render template="/campaigns/showModules/campaignDataLabels" model="[causes:debate.causes]"/>
        <g:render template="/campaigns/showModules/campaignDataSocial" model="[campaign:debate]"/>

        <g:if test="${debate.event}">
            %{-- EVENT ICON --}%
            <g:render template="/debate/showModules/mainContent/eventIcon" model="[campaign:debate]"/>
        </g:if>
        <g:else>
            %{-- DEBATE ICON --}%
            <g:render template="/debate/showModules/mainContent/debateDataIcon" model="[debate:debate, numberProposals:proposalPage.total]"/>
        </g:else>
    </div>
</div> <!-- ^leader-post !-->