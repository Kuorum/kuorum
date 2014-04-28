


$(function (){
    $('a.social-share.twitter').click(function(e) {
        e.preventDefault()
        var width  = 575,
            height = 400,
            left   = ($(window).width()  - width)  / 2,
            top    = ($(window).height() - height) / 2,
            url    = this.href,
            opts   = 'status=1' +
                ',width='  + width  +
                ',height=' + height +
                ',top='    + top    +
                ',left='   + left +
                ',data-lang="es"'+
                ',data-related="anywhere:The Javascript API,Kuorum:The official account"';

        window.open(url, 'twitter', opts);
    });
});