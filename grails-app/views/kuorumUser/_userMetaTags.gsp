%{--Page description. No longer than 155 characters.--}%
<meta name="description" content="${user.bio?user.bio:''}" />

<!-- Google Authorship and Publisher Markup -->
%{--<link rel="author" href="https://plus.google.com/[Google+_Profile]/posts"/>--}%
%{--<link rel="publisher" href=”https://plus.google.com/[Google+_Page_Profile]"/>--}%

<!-- Schema.org markup for Google+ -->
<meta itemprop="name" content="${user.name}">
<meta itemprop="description" content="${user.bio?user.bio:''}">
%{--<meta itemprop="image" content="${post.multimedia?.url}">--}%
<meta itemprop="image" content="${image.userImgSrc(user:user)}" />


<!-- Twitter Card data -->
<meta name="twitter:card" content="summary" />
<meta name="twitter:image" content="${image.userImgSrc(user:user)}">
<meta name="twitter:site" content="@kuorumorg">
<meta name="twitter:title" content="${user.name}">
%{--Page description less than 200 characters--}%
<meta name="twitter:description" content="${user.bio?user.bio:''}">
<g:if test="${user.socialLinks?.twitter}">
    <meta name="twitter:creator" content="@${user.socialLinks.twitter}">
</g:if>
<!-- Twitter summary card with large image must be at least 280x150px -->
%{--<meta name="twitter:image:src" content="${post.multimedia?.url}">--}%

<!-- Open Graph data -->
<meta property="og:title" content="${user.name}" />
<meta property="og:type" content="article" />
<meta property="og:url" content="${g.createLink(mapping:'userShow', params:user.encodeAsLinkProperties(), absolute:true)}" />
<meta property="og:image" content="${image.userImgSrc(user:user)}" />
<meta name="title" content="${user.name}" />
<meta name="description" content="${user.bio?user.bio:''}" />
<meta property="og:description" content="${user.bio?user.bio:''}" />
<meta property="og:site_name" content="${message(code: 'kuorum.name')}" />
%{--<meta property="article:published_time" content="${formatDate(date:user.dateCreated, format:'yyyy-MM-dd')}" />--}%
%{--<meta property="article:modified_time" content="${formatDate(date:user.dateCreated, format:'yyyy-MM-dd')}" />--}%
%{--<meta property="article:section" content="User Section" />--}%
%{--<meta property="article:tag" content="${post.project.hashtag}" />--}%
%{--<meta property="fb:admins" content="Facebook numberic ID" />--}%