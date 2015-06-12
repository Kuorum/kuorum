%{--Page description. No longer than 155 characters.--}%
<meta name="description" content="${project.shortName}" />
<meta itemprop="name" content="${project.hashtag}">
<meta itemprop="description" content="${project.shortName}">

<!-- Twitter Card data -->
<meta name="twitter:site" content="@kuorumorg">
<meta name="twitter:title" content="${project.hashtag}">
%{--Page description less than 200 characters--}%
<meta name="twitter:description" content="${project.hashtag}">
<meta name="twitter:card" content="${project.hashtag}">
<meta name="twitter:title" content="${project.shortName}">
<g:if test="${project.owner.socialLinks?.twitter}">
    <meta name="twitter:creator" content="${project.owner.socialLinks?.twitter}">
</g:if>
<meta name="twitter:domain" content="kuorum.org">

<!-- Open Graph data -->
<meta property="og:title" content="${project.hashtag}" />
<meta property="og:type" content="article" />
<meta property="og:url" content="${g.createLink(mapping:'projectShow', params:project.encodeAsLinkProperties(), absolute: true)}" />
<g:if test="${project.image  && project.image.fileType == kuorum.core.FileType.IMAGE}">
    <meta itemprop="image" content="${project.image.url}">
    <meta name="twitter:image:src" content="${project.image.url}">
    <meta property="og:image" content="${project.image?.url}" />
</g:if>
<g:if test="${project.urlYoutube && project.urlYoutube.fileType == kuorum.core.FileType.YOUTUBE}">
    <meta property="og:image" content="${image.generateYoutubeImageUrl(youtube:project.urlYoutube, big:true)}" />
    <meta itemprop="image" content="${image.generateYoutubeImageUrl(youtube:project.urlYoutube, big:true)}">
    <meta name="twitter:image:src" content="${image.generateYoutubeImageUrl(youtube:project.urlYoutube, big:true)}">

</g:if>

<meta property="og:description" content="${project.description?.encodeAsRemovingHtmlTags()}" />
<meta property="og:site_name" content="${message(code: 'kuorum.name')}" />
<meta property="article:published_time" content="${formatDate(date:project.dateCreated, format:'yyyy-MM-dd')}" />
<meta property="article:modified_time" content="${formatDate(date:project.lastUpdate, format:'yyyy-MM-dd')}" />
<g:if test="${project.commissions}">
    <meta property="article:section" content="${message(code:'kuorum.core.model.CommissionType.'+project.commissions.first())}" />
</g:if>
<meta property="article:tag" content="${project.hashtag}" />
%{--<meta property="fb:admins" content="Facebook numberic ID" />--}%