%{--Page description. No longer than 155 characters.--}%
<meta name="description" content="${project.hashtag}" />

<!-- Google Authorship and Publisher Markup -->
%{--<link rel="author" href="https://plus.google.com/[Google+_Profile]/posts"/>--}%
%{--<link rel="publisher" href=”https://plus.google.com/[Google+_Page_Profile]"/>--}%

<!-- Schema.org markup for Google+ -->
<meta itemprop="name" content="${project.hashtag}">
<meta itemprop="description" content="${project.introduction}">
<meta itemprop="image" content="${project.image.url}">

<!-- Twitter Card data -->
<meta name="twitter:card" content="summary_large_image">
<meta name="twitter:site" content="@kuorumorg">
<meta name="twitter:title" content="Page Title">
%{--Page description less than 200 characters--}%
<meta name="twitter:description" content="${project.hashtag}">
<meta name="twitter:creator" content="@author_handle">
<!-- Twitter summary card with large image must be at least 280x150px -->
<meta name="twitter:image:src" content="${project.image.url}">

<!-- Open Graph data -->
<meta property="og:title" content="${project.hashtag}" />
<meta property="og:type" content="article" />
<meta property="og:url" content="${g.createLink(mapping:'projectShow', params:project.encodeAsLinkProperties())}" />
<meta property="og:image" content="${project.image.url}" />
<meta property="og:description" content="${project.introduction}" />
<meta property="og:site_name" content="${message(code: 'kuorum.name')}" />
<meta property="article:published_time" content="${formatDate(date:project.dateCreated, format:'yyyy-MM-dd')}" />
<meta property="article:modified_time" content="${formatDate(date:project.dateCreated, format:'yyyy-MM-dd')}" />
<meta property="article:section" content="Article Section" />
<meta property="article:tag" content="Article Tag" />
<meta property="fb:admins" content="Facebook numberic ID" />