window.fbAsyncInit = function() {
    // init the FB JS SDK
    FB.init({
        appId      : '226641644202506',                        // App ID from the app dashboard
//        appId      : '361147123984843',                        // App ID from the app dashboard
        channelUrl : 'http://kuorum.org', // Channel file for x-domain comms
        status     : true,                                 // Check Facebook Login status
        xfbml      : true                                  // Look for social plugins on the page
    });

    // Additional initialization code such as adding Event Listeners goes here

};

// Load the SDK asynchronously
(function(d, s, id){
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) {return;}
    js = d.createElement(s); js.id = id;
    js.src = "//connect.facebook.net/es_ES/all.js";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));