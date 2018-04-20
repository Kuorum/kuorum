%{--Page description. No longer than 155 characters.--}%
<g:set var="kuorumName" value="${message(code: 'kuorum.name')}"/>

<meta name="description" content="${kuorumDescription}" />

<!-- Schema.org markup for Google+ -->
<meta name="title" content="${kuorumTitle}" />
<meta name="description" content="${kuorumDescription}" />
<meta name="image" content="${kuorumImage}" />
<meta name="keywords" content="${g.message(code:"layout.head.meta.keywords", args:[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name])}" />
<meta name="application-name" content="${kuorumName}" />

<meta itemprop="name" content="${kuorumName}">
<meta itemprop="description" content="${kuorumDescription}">
<meta itemprop="image" content="${kuorumImage}" />


<!-- Twitter Card data -->
<meta name="twitter:card" content="summary" />
<meta name="twitter:site" content="@kuorumorg">
<meta name="twitter:title" content="${kuorumTitle}">
%{--Page description less than 200 characters--}%
<meta name="twitter:description" content="${kuorumDescription}">
<meta name="twitter:image" content="${kuorumImage}">
<meta property="twitter:account_id" content="4503599627910348" />
<!-- Open Graph data -->
<meta property="og:title" content="${kuorumTitle}" />
<meta property="og:type" content="website" />
<meta property="og:image" content="${kuorumImage}" />
<meta property="og:description" content="${kuorumDescription}" />
<meta property="og:site_name" content="${kuorumName}" />

<meta property="fb:app_id" content="361147123984843" />