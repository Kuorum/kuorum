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

    $("#tabs-stats-campaign").on("change", ".pag-list-contacts select[name=sizePage]",function(e){
        e.preventDefault();
        var sizePage = $(this).val()
        var link = $(this).parent().siblings("ul").attr("data-link")
        $("select[name=sizePage]").val(sizePage)
        loadTrackingCampaignEvents(link, 0)
    });

    $("#tabs-stats-campaign").on("click", "#status-filter-options a", function (e) {
        e.preventDefault();
        var link = $("#tabs-stats-campaign .pagination ul.paginationTop").attr("data-link");
        var status = $(this).attr("href").substring(1);
        $("#filter-status").val(status);
        loadTrackingCampaignEvents(link, 0)

    });

    function setDefaultStatsTabActive() {
        var $tabs = $("#tabs-stats-campaign").parents(".campaign-stats").children(".nav-tabs");
        var numberActive = $tabs.children("li.active").length
        if (numberActive == 0) {
            $defaultActiveLi = $tabs.find("li:first-child");
            $defaultActiveLi.addClass("active");
            var defaultTabId = $defaultActiveLi.find("a").attr("href");
            $(defaultTabId).addClass("active")
        }
    }

    setDefaultStatsTabActive();

    $("#report-files").on("click", ".files-list-btn-refresh, .file-action", function (e) {
        pageLoadingOn();
        e.preventDefault();
        var link = $(this).attr("href");
        var postData = {}
        $.post(link, postData)
            .done(function (data) {
                $("#report-files-list").html(data)
            })
            .fail(function(messageError) {
                display.warn("Error");
            })
            .always(function() {
                pageLoadingOff();
            });
    });

    function loadTrackingCampaignEvents(link, page){
        pageLoadingOn();

        var status = $("#filter-status").val();
        var size = $("select[name=sizePage]").val()
        var postData = {page:page, status:status, size:size}
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
            .done(function (data) {
                $("#" + modalId).modal("show")
            })
            .fail(function (messageError) {
                display.warn("Error exporting");
            })
            .always(function () {
                pageLoadingOff();
            });
    });

    $("#close-survey-modal-button").on("click", function (e) {
        e.preventDefault();
        var $a = $(this)
        var link = $a.attr("href")
        var modalId = $a.attr("data-modalId")
        var $modal = $("#" + modalId);
        $modal.modal("show");
        $modal.find(".submit-close-survey-button").attr("href", link);
    });

    $("#close-survey-modal").on("click", ".submit-close-survey-button", function (e) {
        pageLoadingOn();
        e.preventDefault();
        var $a = $(this)
        var link = $a.attr("href")
        $.post(link)
            .done(function (data) {
                location.reload();
            })
            .fail(function (messageError) {
                display.warn("Error closing survey");
            })
            .always(function () {
                pageLoadingOff();
            });
    });


    // Request campaign collection export
    $("#exportCampaigns").on("click", function (e) {
        pageLoadingOn();
        e.preventDefault();
        var $a = $(this);
        var link = $a.attr("href");
        $.post(link)
            .done(function (data) {
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
            .fail(function (messageError) {
                display.warn("Error");
            })
            .always(function () {
                pageLoadingOff();
            });
    });

    // MODAL FOR CREATING A SUMMONING CALL
    $('body').on('click', 'a.summoing-call', function (e) {
        e.preventDefault();
        var linkScheduled = $(this).attr('href');
        $("#modalCreateSummoning").modal("show");
        $("a#modalCreateSummoningButtonOk").attr("href", linkScheduled)
    });

    // FILTRADO Y BUSCADOR LISTADO CAMPAÑAS
    if ($('#listCampaigns').length) {

        $('#search-form-campaign').submit(function () {
            //The search campaign form has not a submit action. All the search is done with javascript
            return false;
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
            valueNames: ['id', 'name', 'title', 'recip-number', 'open-number', 'click-number', 'state', 'type', {
                name: 'timestamp',
                attr: 'val'
            }],
            page: 10,
            searchClass: "searchCampaigns",
            plugins: [
                ListPagination(paginationTopOptions),
                ListPagination(paginationBottomOptions)
            ],
            pagination: true
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
            $('.totalList').text(campaignList.matchingItems.length);
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
    campaignListHelper.preparePauseCampaignButton();
})

var campaignListHelper={
    // eliminar campaña
    prepareDeleteCampaignButton: function() {
        // Needed to add new buttons to jQuery-extended object
        $('.list-campaigns').on("click",'.campaignDelete',function(e) {
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
    },
    // Pausar campaña
    preparePauseCampaignButton: function() {
        // Needed to add new buttons to jQuery-extended object
        $('.list-campaigns').on("click",'.campaignPause',function(e) {
            e.preventDefault();
            pageLoadingOn();
            var $button = $(this)
            var link = $button.attr("href");
            var campaignId =  $(this).parents("ul#campaignsList > li").find('.id').text();
            var $campaignLi = $("#campaignPos_"+campaignId)
            var textSent = $button.attr("data-text-sent");
            var textPaused = $button.attr("data-text-paused");
            var pauseData= {
                activeOn:!$campaignLi.hasClass("PAUSE")
            };
            $.post( link, pauseData)
                .done(function(data) {
                    var statusText = textSent
                    if (data.paused){
                        statusText = textPaused
                    }
                    console.log($campaignLi.find(".name > .state"))
                    $campaignLi.find(".state").html(statusText)
                    $campaignLi.find(".state").attr("data-original-title",statusText)
                    $campaignLi.find(".name > .state").html(statusText)
                    $button.find("span").toggleClass("fa-pause-circle fa-play-circle");
                    $campaignLi.toggleClass("PAUSE SENT");
                })
                .fail(function(messageError) {
                    display.warn("Error pausing campaign");
                })
                .always(function() {
                    pageLoadingOff();
                });
        });
    },
}