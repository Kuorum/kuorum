


$(function (){
    $('body').on('click','a.social-share.google',function(e) {
        e.preventDefault();
        var width  = 600,
            height = 600,
            left   = ($(window).width()  - width)  / 2,
            top    = ($(window).height() - height) / 2,
            url    = this.href,
            opts   = 'status=1' +
                'menubar=no,toolbar=no,resizable=yes,scrollbars=yes'+
                ',width='  + width  +
                ',height=' + height +
                ',top='    + top    +
                ',left='   + left +
                ',data-lang="es"'+
                ',data-related="anywhere:The Javascript API,Kuorum:The official account"';

        window.open(url, 'twitter', opts);
    });
});