<meta name="description" content="${post.title}" />

<meta itemprop="name" content="${post.title}">
<meta itemprop="description" content="${post.title}">

<!-- Twitter Card data -->
<meta name="twitter:card" content="summary_large_image">
<meta name="twitter:site" content="@kuorumorg">
<meta name="twitter:title" content="${post.project.hashtag}">
%{--Page description less than 200 characters--}%
<meta name="twitter:description" content="${post.title}">
<g:if test="${post.owner.socialLinks?.twitter}">
    <meta name="twitter:creator" content="@${post.owner.socialLinks.twitter}">
</g:if>

<!-- Open Graph data -->
<meta property="og:title" content="${post.title}" />
<meta property="og:type" content="article" />
<meta property="og:url" content="${g.createLink(mapping:'postShow', params:post.encodeAsLinkProperties(), absolute:true)}" />
<g:if test="${post.multimedia && post.multimedia.fileType == kuorum.core.FileType.IMAGE}">
    <meta property="og:image" content="${post.multimedia?.url}" />
    <meta itemprop="image" content="${post.multimedia?.url}" />
    <meta name="twitter:image:src" content="${post.multimedia?.url}">
</g:if>
<g:if test="${post.multimedia && post.multimedia.fileType == kuorum.core.FileType.YOUTUBE}">
    <meta property="og:image" content="${image.generateYoutubeImageUrl(youtube:post.multimedia, big: true)}" />
    <meta itemprop="image" content="${image.generateYoutubeImageUrl(youtube:post.multimedia, big: true)}" />
    <meta name="twitter:image:src" content="${image.generateYoutubeImageUrl(youtube:post.multimedia, big: true)}">
</g:if>
<meta property="og:description" content="${post.title}" />
<meta property="og:site_name" content="${message(code: 'kuorum.name')}" />
<meta property="article:published_time" content="${formatDate(date:post.dateCreated, format:'yyyy-MM-dd')}" />
<meta property="article:modified_time" content="${formatDate(date:post.dateCreated, format:'yyyy-MM-dd')}" />
%{--<meta property="article:section" content="Article Section" />--}%
<meta property="article:tag" content="${post.project.hashtag}" />
%{--<meta property="fb:admins" content="Facebook numberic ID" />--}%