<!-- ^leader-post !-->
<div class="leader-post">
    <g:render template="/campaigns/showModules/campaignDataMultimedia" model="[campaign: petition]"/>
    <div class="header">
        <h1 class="title" itemprop="headline">${petition.title}</h1>
        <userUtil:showUser user="${petitionUser}" showRole="true" itemprop="author"/>
        <g:render template="/campaigns/showModules/campaignDataDatePublished" model="[campaign:petition, campaignUser:petitionUser, editMappingName:'petitionEditContent', editable:true]"/>
    </div>

    <div class="body" itemprop="articleBody">
        ${raw(petition.body)}
    </div>

    <div class="footer clearfix">
        <g:render template="/campaigns/showModules/campaignDataLabels" model="[causes:petition.causes]"/>
        <g:render template="/campaigns/showModules/campaignDataSocial" model="[campaign:petition]"/>

        <g:render template="/petition/showModules/mainContent/petitionDataIcon" model="[petition:petition]"/>
    </div>
</div> <!-- ^leader-post !-->