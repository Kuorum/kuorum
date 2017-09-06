<!-- Google Tag Manager -->
<noscript><iframe name="GooleTagManagerFrame" src="//www.googletagmanager.com/ns.html?id=GTM-MN2J4F"
                  height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
        new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
        j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
        '//www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
})(window,document,'script','dataLayer','GTM-MN2J4F');</script>
<!-- End Google Tag Manager -->

<script>
    <sec:ifLoggedIn>
        dataLayer.push({
            'userLoggedAlias': '${sec.loggedInUserInfo(field:'alias')}',
            'userLoggedEmail': '${sec.loggedInUserInfo(field:'username')}',
            'userLoggedName': '${sec.loggedInUserInfo(field:'name')}',
            'language':'${currentLang?.language?:"en"}'
          });
    </sec:ifLoggedIn>
    <sec:ifNotLoggedIn>
        dataLayer.push({
            'userLoggedAlias': 'NO_LOGGED',
            'userLoggedEmail': 'NO_LOGGED',
            'userLoggedName': 'NO_LOGGED',
            'language':'${currentLang?.language?:"en"}'
          });
    </sec:ifNotLoggedIn>
</script>