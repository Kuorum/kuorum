<!-- Google Tag Manager -->
<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
        new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
    j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
    'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
})(window,document,'script','dataLayer','GTM-586WVQN');</script>
<!-- End Google Tag Manager -->
<script>
    <sec:ifLoggedIn>
        dataLayer.push({
            'userLogged':true,
            %{--'userLogged': '${sec.loggedInUserInfo(field:'alias')}--}%
            'language':'${currentLang?.language?:"en"}',
            'domain':'${_domain}'
          });
    </sec:ifLoggedIn>
    <sec:ifNotLoggedIn>
        dataLayer.push({
            'userLogged':false,
            'language':'${currentLang?.language?:"en"}',
            'domain':'${_domain}'
          });
    </sec:ifNotLoggedIn>
</script>

<!-- Google Tag Manager (noscript) -->
<noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-586WVQN"
                  height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
<!-- End Google Tag Manager (noscript) -->