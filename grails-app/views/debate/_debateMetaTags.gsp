%{--Page description. No longer than 155 characters.--}%
<meta name="description" content="${debate.title}" />
<g:set var="debateImage" value="${debate.photoUrl?:debate.videoUrl?image.imageYoutubeSrc(youtube: debate.videoUrl, maxResolution: true):''}"/>

<!-- Google Authorship and Publisher Markup -->
%{--<link rel="author" href="https://plus.google.com/[Google+_Profile]/posts"/>--}%
%{--<link rel="publisher" href=”https://plus.google.com/[Google+_Page_Profile]"/>--}%

<!-- Schema.org markup for Google+ -->
<meta itemprop="name" content="${g.message(code:'page.politicianProfile.title', args:[debate.title])}">
<meta itemprop="description" content="${debate.body?.encodeAsRemovingHtmlTags()}">
%{--<meta itemprop="image" content="${post.multimedia?.url}">--}%
<meta itemprop="image" content="${debateImage}" />


<!-- Twitter Card data -->
<meta name="twitter:card" content="summary" />
<meta name="twitter:image" content="${debateImage}">
<meta name="twitter:site" content="@kuorumorg">
<meta name="twitter:title" content="${g.message(code:'page.politicianProfile.title', args:[debate.title])}">
%{--Page description less than 200 characters--}%
<meta name="twitter:description" content="${debate.body?.encodeAsRemovingHtmlTags()?:''}">
<!-- Twitter summary card with large image must be at least 280x150px -->
%{--<meta name="twitter:image:src" content="${post.multimedia?.url}">--}%

<!-- Open Graph data -->
<meta property="og:title" content="${g.message(code:'page.politicianProfile.title', args:[debate.title])}" />
<meta property="og:type" content="article" />
<meta property="og:url" content="${g.createLink(mapping:'debateShow', params:debate.encodeAsLinkProperties(), absolute:true)}" />
<meta property="og:image" content="${debateImage}" />
<meta name="title" content="${g.message(code:'page.politicianProfile.title', args:[debate.title])}" />
<meta name="description" content="${debate.body?.encodeAsRemovingHtmlTags()?:''}" />
<meta property="og:description" content="${debate.body?.encodeAsRemovingHtmlTags()}" />
<meta property="og:site_name" content="${message(code: 'kuorum.name')}" />
%{--<meta property="article:published_time" content="${formatDate(date:user.dateCreated, format:'yyyy-MM-dd')}" />--}%
%{--<meta property="article:modified_time" content="${formatDate(date:user.dateCreated, format:'yyyy-MM-dd')}" />--}%
%{--<meta property="article:section" content="User Section" />--}%
%{--<meta property="article:tag" content="${post.project.hashtag}" />--}%
%{--<meta property="fb:admins" content="Facebook numberic ID" />--}%
