%{--Page description. No longer than 155 characters.--}%
<meta name="description" content="${law.hashtag}" />

<!-- Google Authorship and Publisher Markup -->
%{--<link rel="author" href="https://plus.google.com/[Google+_Profile]/posts"/>--}%
%{--<link rel="publisher" href=”https://plus.google.com/[Google+_Page_Profile]"/>--}%

<!-- Schema.org markup for Google+ -->
<meta itemprop="name" content="${law.hashtag}">
<meta itemprop="description" content="${law.introduction}">
<meta itemprop="image" content="${law.image.url}">

<!-- Twitter Card data -->
<meta name="twitter:card" content="summary_large_image">
<meta name="twitter:site" content="@kuorumorg">
<meta name="twitter:title" content="Page Title">
%{--Page description less than 200 characters--}%
<meta name="twitter:description" content="${law.hashtag}">
<meta name="twitter:creator" content="@author_handle">
<!-- Twitter summary card with large image must be at least 280x150px -->
<meta name="twitter:image:src" content="${law.image.url}">

<!-- Open Graph data -->
<meta property="og:title" content="${law.hashtag}" />
<meta property="og:type" content="article" />
<meta property="og:url" content="${g.createLink(mapping:'lawShow', params:law.encodeAsLinkProperties())}" />
<meta property="og:image" content="${law.image.url}" />
<meta property="og:description" content="${law.introduction}" />
<meta property="og:site_name" content="${message(code: 'kuorum.name')}" />
<meta property="article:published_time" content="${formatDate(date:law.dateCreated, format:'yyyy-MM-dd')}" />
<meta property="article:modified_time" content="${formatDate(date:law.dateCreated, format:'yyyy-MM-dd')}" />
<meta property="article:section" content="Article Section" />
<meta property="article:tag" content="Article Tag" />
<meta property="fb:admins" content="Facebook numberic ID" />