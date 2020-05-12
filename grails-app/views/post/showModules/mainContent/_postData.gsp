<!-- ^leader-post !-->
<div class="leader-post">
    <g:render template="/campaigns/showModules/campaignDataMultimedia" model="[campaign: post]"/>
    <div class="header">
        <h1 class="title" itemprop="headline">${post.title}</h1>
        <userUtil:showUser user="${postUser}" showRole="true" itemprop="author"/>
        <g:render template="/campaigns/showModules/eventCallToActionMovile" model="[campaign:post]"/>
        <g:render template="/campaigns/showModules/campaignDataDatePublished" model="[campaign:post, campaignUser:postUser, editMappingName:'postEditContent']"/>
    </div>

    <div class="body" itemprop="articleBody">
        ${raw(post.body)}
    </div>

    <div class="footer clearfix">
        <g:render template="/campaigns/showModules/campaignDataLabels" model="[causes:post.causes]"/>
        <g:render template="/campaigns/showModules/campaignDataSocial" model="[campaign:post]"/>

        <g:if test="${post.event}">
            <g:render template="/debate/showModules/mainContent/eventIcon" model="[campaign:post]"/>
        </g:if>
        <g:else>
            <g:render template="/post/showModules/mainContent/postDataIcon" model="[post:post]"/>
        </g:else>
    </div>
</div> <!-- ^leader-post !-->