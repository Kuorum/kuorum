$(function(){


    // Pagination of tracking mails of a campaign
    $("#tabs-stats-campaign").on("click", ".pag-list-contacts li a",function(e){
        e.preventDefault();
        if (!$(this).hasClass("disabled")){
            var page = parseInt($(this).attr("data-nextPage"));
            var link = $(this).parents("ul").attr("data-link")
            loadTrackingCampaignEvents(link, page)
        }
    });

    $("#tabs-stats-campaign").on("click", "#status-filter-options a", function(e){
        e.preventDefault();
        var link = $("#tabs-stats-campaign .pagination ul.paginationTop").attr("data-link");
        var status = $(this).attr("href").substring(1);
        $("#filter-status").val(status);
        loadTrackingCampaignEvents(link, 0)

    });

    function loadTrackingCampaignEvents(link, page){
        pageLoadingOn();

        var status = $("#filter-status").val();
        var postData = {page:page, status:status}
        $.post( link, postData)
            .done(function(data) {
                $("#recipients").html(data)
            })
            .fail(function(messageError) {
                display.warn("Error");
            })
            .always(function() {
                pageLoadingOff();
            });
    }

    // Modals exports
    $(".export-modal-button").on("click", function(e){
        pageLoadingOn();
        e.preventDefault();
        var $a = $(this)
        var link = $a.attr("href")
        var modalId = $a.attr("data-modalId")
        $.post(link)
            .done(function(data) {
                $("#"+modalId).modal("show")
            })
            .fail(function(messageError) {
                display.warn("Error exporting");
            })
            .always(function() {
                pageLoadingOff();
            });
    });


    // Request campaign collection export
    $("#exportCampaigns").on("click", function(e){
        pageLoadingOn();
        e.preventDefault();
        var $a = $(this);
        var link = $a.attr("href");
        $.post(link)
            .done(function(data) {
                $("#export-campaigns-modal").modal("show");
            })
            .fail(function(messageError) {
                display.warn("Error");
            })
            .always(function() {
                pageLoadingOff();
            });
    });


    // EDITAR BORRAR CAMPAÑAS
    // abrir modal editar campaña planificada
    $('body').on('click', 'a.modalEditScheduled', function(e) {
        e.preventDefault();
        var linkScheduled = $(this).attr('href');
        $("#modalEditScheduled").modal("show");
        $("a#modalEditScheduledButtonOk").attr("href", linkScheduled)
    });

    // cerrar modal confirmar borrar campaña
    $('body').on('click','a.deleteCampaignBtn', function(e) {
        e.preventDefault();
        $("#campaignDeleteConfirm").modal("hide");
        pageLoadingOn();
        var link = $(this).attr("href")
        var campaignId = $(this).attr("data-campaign-id")
        var postData= {};
        $.post( link, postData)
            .done(function(data) {
                campaignList.remove('id', campaignId);
            })
            .fail(function(messageError) {
                display.warn("Error");
            })
            .always(function() {
                pageLoadingOff();
            });
    });

    // FILTRADO Y BUSCADOR LISTADO CAMPAÑAS
    if ($('#listCampaigns').length) {

        $('#search-form-campaign').submit(function(){
            //The search campaign form has not a submit action. All the search is done with javascript
            return false;
        });
        //contador para el select (antes del plugin)
        var counterList = $('#campaignsList > li').length;
        $('.totalList').text(counterList);
        var sent = $('li.SENT').length;
        var scheduled = $('li.SCHEDULED').length;
        var draft = $('li.DRAFT').length;
        var newsletter = $('li.newsletterItem').length;
        var debate = $('li.debateItem').length;
        var post = $('li.postItem').length;

        //select filtro campañas según tipo
        $('#filterCampaigns').on('change', function () {
            if ($('#filterCampaigns option:selected').is('#all')) {
                $('.totalList').text(counterList);
                $('#infoFilterCampaigns').removeClass().find('.filtered').text('');
            }
            if ($('#filterCampaigns option:selected').is('#newsletter')) {
                $('.totalList').text(newsletter);
                $('#infoFilterCampaigns').removeClass().find('.filtered').text('');
            }
            if ($('#filterCampaigns option:selected').is('#debate')) {
                $('.totalList').text(debate);
                $('#infoFilterCampaigns').removeClass().find('.filtered').text('');
            }
            if ($('#filterCampaigns option:selected').is('#post')) {
                $('.totalList').text(post);
                $('#infoFilterCampaigns').removeClass().find('.filtered').text('');
            }
        });

        // clase active botones ordenar listado
        $('body').on('click','.sort', function(e) {
            if (!$(this).hasClass('active')) {
                $('.sort').removeClass('active');
                $(this).addClass('active');
            }
        });

        //plugin options
        var paginationTopOptions = {
            name: "paginationTop",
            paginationClass: "paginationTop",
            innerWindow: 1,
            outerWindow: 1
        };
        var paginationBottomOptions = {
            name: "paginationBottom",
            paginationClass: "paginationBottom",
            innerWindow: 1,
            outerWindow: 1
        };
        var options = {
            valueNames: [ 'id', 'name', 'title', 'recip-number', 'open-number', 'click-number', 'state', 'type',{ name: 'timestamp', attr: 'val' } ],
            page: 10,
            searchClass: "searchCampaigns",
            plugins: [
                ListPagination(paginationTopOptions),
                ListPagination(paginationBottomOptions)
            ]
        };
        var campaignList = new List('listCampaigns', options);


        //select filtro campañas según estado
        $('#filterCampaigns').on('change', function () {
            var selection = this.value;
            if ($('select#filterCampaigns option:selected').is('#all')) {
                campaignList.filter();
            } else {
                // filter items in the list
                campaignList.filter(function (item) {
                    if (item.values().type == selection) {
                        return (item.values().type == selection);
                    }
                });
            }
        });
        $("#campaignsOrderOptions").on("click", "a", function(e){
            e.preventDefault();
        })
    }
    // Sorting campaign by default when load campaigns
    if ($("#campaignsOrderOptions ul li:first a").length >0){
        $("#campaignsOrderOptions ul li:first a")[0].click();
    }

    campaignListHelper.prepareDeleteCampaignButton();
})

var campaignListHelper={
    // eliminar campaña
    prepareDeleteCampaignButton: function() {
        // Needed to add new buttons to jQuery-extended object
        $('.campaignDelete').on("click",function(e) {
            e.preventDefault();
            var link = $(this).attr("href");
            var itemId =  $(this).parents("ul#campaignsList > li").find('.id').text();
            campaignListHelper.prepareAndOpenCampaignConfirmDeletionModal(link, itemId)
        });
    },
    prepareAndOpenCampaignConfirmDeletionModal: function (urlDeleteCampaign, campaignId){
        $("#campaignDeleteConfirm a.deleteCampaignBtn").attr("href",urlDeleteCampaign)
        $("#campaignDeleteConfirm a.deleteCampaignBtn").attr("data-campaign-id",campaignId)
        $("#campaignDeleteConfirm").modal("show");
    }
}