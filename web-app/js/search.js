
$(".pag-list-search").on("change", "select[name=sizePage]",function(e){
    var sizePage = $(this).val()
    var link = $(this).find('option:selected').attr('data-link');
    window.location=link
    console.log(link)
});