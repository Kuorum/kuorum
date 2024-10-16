%{--Page description. No longer than 155 characters.--}%
<meta name="description" content="${campaign.title}" />
<g:set var="campaignImage" value="${campaign.photoUrl?:campaign.videoUrl?g.resource(dir: "images", file: "rrss-share-video.png"):g.resource(dir: "images", file: "rrss-share-noimage.png")}"/>

<!-- Google Authorship and Publisher Markup -->
%{--<link rel="author" href="https://plus.google.com/[Google+_Profile]/campaigns"/>--}%
%{--<link rel="publisher" href=”https://plus.google.com/[Google+_Page_Profile]"/>--}%

<!-- Schema.org markup for Google+ -->
<meta itemprop="name" content="${g.message(code:titleMessageCode, args:[campaign.title, _domainName])}">
<meta itemprop="description" content="${campaign.body?.encodeAsRemovingHtmlTags()}">
%{--<meta itemprop="image" content="${campaign.multimedia?.url}">--}%
<meta itemprop="image" content="${campaignImage}" />


<!-- Twitter Card data -->
<meta name="twitter:card" content="summary" />
<meta name="twitter:image" content="${campaignImage}">
<meta name="twitter:site" content="@kuorumorg">
<meta name="twitter:title" content="${g.message(code:titleMessageCode, args:[campaign.title, _domainName])}">
%{--Page description less than 200 characters--}%
<meta name="twitter:description" content="${campaign.body?.encodeAsRemovingHtmlTags()?:''}">
<!-- Twitter summary card with large image must be at least 280x150px -->
%{--<meta name="twitter:image:src" content="${campaign.multimedia?.url}">--}%

<!-- Open Graph data -->
<meta property="og:title" content="${g.message(code:titleMessageCode, args:[campaign.title, _domainName])}" />
<meta property="og:type" content="article" />
<meta property="og:url" content="${g.createLink(mapping:'campaignShow', params:campaign.encodeAsLinkProperties(), absolute:true)}" />
<meta property="og:image" content="${campaignImage}" />
<meta name="title" content="${g.message(code:titleMessageCode, args:[campaign.title, _domainName])}" />
<meta name="description" content="${campaign.body?.encodeAsRemovingHtmlTags()?:''}" />
<meta property="og:description" content="${campaign.body?.encodeAsRemovingHtmlTags()}" />
<meta property="og:site_name" content="${_domainName}" />
%{--<meta property="article:published_time" content="${formatDate(date:user.dateCreated, format:'yyyy-MM-dd')}" />--}%
%{--<meta property="article:modified_time" content="${formatDate(date:user.dateCreated, format:'yyyy-MM-dd')}" />--}%
%{--<meta property="article:section" content="User Section" />--}%
%{--<meta property="article:tag" content="${campaign.project.hashtag}" />--}%
%{--<meta property="fb:admins" content="Facebook numberic ID" />--}%
