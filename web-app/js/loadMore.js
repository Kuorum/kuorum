$("a.loadMore").on("click", function(e){
    loadMore(e, this);
});

function loadMore(e, that) {
    e.preventDefault();
    var link = $(that);
    var url = link.attr('href');
    var formId = link.attr('data-form-id');
    var paramAppender = "?";
    if (url.indexOf("?")>-1){
        paramAppender = "&";
    }

    var offset = $.parseJSON(link.attr('data-offset') || 10 ); //Para que sea un integer
    url += paramAppender+"offset="+offset+"&"+$('#'+formId).serialize();
    var parentId = link.attr('data-parent-id');
    var loadingId = parentId+"-loading";
    var parent = $("#"+parentId);
    parent.append('<div class="loading" id="'+loadingId+'"><span class="sr-only">Cargando...</span></div>');
    $.ajax( {
        url:url,
        statusCode: {
            401: function() {
                location.reload();
            }
        }
    })
    .done(function(data, status, xhr) {
        parent.append(data);
        var moreResults = $.parseJSON(xhr.getResponseHeader('moreResults')); //Para que sea un bool
        link.attr('data-offset', offset +10);
        if (moreResults){
            link.remove();
        }
    })
    .fail(function(data) {
        console.log(data);
    })
    .always(function(data) {
        $("#"+loadingId).remove();
        $("time.timeago").timeago();
    });
}