%{--Page description. No longer than 155 characters.--}%
<meta name="description" content="${post.title}" />
<g:set var="postImage" value="${post.photoUrl?:post.videoUrl?g.resource(dir: "images", file: "rrss-share-video.png"):g.resource(dir: "images", file: "rrss-share-noimage.png")}"/>

<!-- Google Authorship and Publisher Markup -->
%{--<link rel="author" href="https://plus.google.com/[Google+_Profile]/posts"/>--}%
%{--<link rel="publisher" href=”https://plus.google.com/[Google+_Page_Profile]"/>--}%

<!-- Schema.org markup for Google+ -->
<meta itemprop="name" content="${g.message(code:titleMessageCode, args:[post.title, _domainName])}">
<meta itemprop="description" content="${post.body?.encodeAsRemovingHtmlTags()}">
%{--<meta itemprop="image" content="${post.multimedia?.url}">--}%
<meta itemprop="image" content="${postImage}" />


<!-- Twitter Card data -->
<meta name="twitter:card" content="summary" />
<meta name="twitter:image" content="${postImage}">
<meta name="twitter:site" content="@kuorumorg">
<meta name="twitter:title" content="${g.message(code:titleMessageCode, args:[post.title, _domainName])}">
%{--Page description less than 200 characters--}%
<meta name="twitter:description" content="${post.body?.encodeAsRemovingHtmlTags()?:''}">
<!-- Twitter summary card with large image must be at least 280x150px -->
%{--<meta name="twitter:image:src" content="${post.multimedia?.url}">--}%

<!-- Open Graph data -->
<meta property="og:title" content="${g.message(code:titleMessageCode, args:[post.title, _domainName])}" />
<meta property="og:type" content="article" />
<meta property="og:url" content="${g.createLink(mapping:'postShow', params:post.encodeAsLinkProperties(), absolute:true)}" />
<meta property="og:image" content="${postImage}" />
<meta name="title" content="${g.message(code:titleMessageCode, args:[post.title, _domainNames])}" />
<meta name="description" content="${post.body?.encodeAsRemovingHtmlTags()?:''}" />
<meta property="og:description" content="${post.body?.encodeAsRemovingHtmlTags()}" />
<meta property="og:site_name" content="${_domainName}" />
%{--<meta property="article:published_time" content="${formatDate(date:user.dateCreated, format:'yyyy-MM-dd')}" />--}%
%{--<meta property="article:modified_time" content="${formatDate(date:user.dateCreated, format:'yyyy-MM-dd')}" />--}%
%{--<meta property="article:section" content="User Section" />--}%
%{--<meta property="article:tag" content="${post.project.hashtag}" />--}%
%{--<meta property="fb:admins" content="Facebook numberic ID" />--}%
