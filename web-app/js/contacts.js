$(function () {
    // abrir opciones nuevo filtro con select
    $('#newsletter select#recipients,#searchContacts select#recipients').on('change', filterContacts.changedFilterValueEvent);
    //$('#searchContacts select#recipients').on('change', filterContacts.searchContactsCallBacks.campaignFilterRefresh);

    $("#listContacts").on("click", "#contactsOrderOptions ul.pag-list-sort li a",function(e){
        e.preventDefault();
        var sortField = $(this).attr("data-sort");
        var sortDirection ="ASC";
        $("#contactsOrderOptions ul.pag-list-sort li a").removeClass("active");
        $(this).addClass("active");
        if ($(this).hasClass("asc")) {
            $(this).removeClass("asc");
            $(this).addClass("desc");
            sortDirection="DESC"
        }else{
            $(this).removeClass("desc");
            $(this).addClass("asc")
        }
        //console.log(sortDirection)
        filterContacts.searchContactsCallBacks.sort(sortField, sortDirection)
    });

    $("#listContacts").on("click", ".pag-list-contacts li a",function(e){
        e.preventDefault();
        if (!$(this).hasClass("disabled")){
            var page = parseInt($(this).attr("data-nextPage"));
            filterContacts.searchContactsCallBacks.page(page)
        }
    });

    // abrir modal confirmar borrado contacto
    $('#listContacts').on('click','a.contactDelete', function(e) {
        e.preventDefault();
        var link = $(this).attr("href");
        prepareAndOpenContactDeletionModal(link);
    });
    // cerrar modal confirmar envío campaña
    $('#listContacts').on('click','#contactDeleteConfirm a.deleteContactBtn', function(e) {
        e.preventDefault();
        $("#contactDeleteConfirm").modal("hide");
        var link = $(this).attr("href");
        $(this).parent("li").fadeOut();
        $.post( link)
            .done(function(data) {
                filterContacts.searchContactsCallBacks.loadTableContacts();
            })
            .fail(function(messageError) {
                display.warn("Error deleting");
                console.log(messageError)
            })
            .always(function() {
                // I don't know why modal("hide") not removes this div
                $(".modal-backdrop").remove();
            });
    });
    function prepareAndOpenContactDeletionModal(link){
        $("#contactDeleteConfirm a.deleteContactBtn").attr("href", link);
        $("#contactDeleteConfirm").modal("show");
    }

    $("#quickSearchByName").on("keypress", function(e){
        if (e.which == 13) {
            filterContacts.searchContactsCallBacks.page(0);
            return false;
        }
    });

    $("#quickSearchByName").on("blur", function(e){
        filterContacts.searchContactsCallBacks.page(0);
        return false;
    });


    //Preparar el select segun el option seleccionado
    filterContacts.changedFilterValueEvent();

    // abrir opciones nuevo filtro con botón
    $('body').on('click','#filterContacts', function(e) {
        e.preventDefault();
        if ($(this).hasClass('on')) {
            filterContacts.closeFilterCampaignsOptions();
        } else {
            filterContacts.loadFilter();
        }
    });
    // eliminar condición con botón
    $('body').on('click','.new-filter-options .minus-condition', function(e) {
        e.preventDefault();
        $(this).closest('.new-filter-options').fadeOut("fast", function(){
            $(this).remove()
            filterContacts.filterEditedEvent();
        });
    });
    $("#filterData").on("click", ".plus-condition",filterContacts.filterEditedEvent);

    // abrir modal contenido filtro seleccionado
    $('body').on('click','#numberRecipients, #infoToContacts', function(e) {
        e.preventDefault();
        filterContacts.showModalListContacts()
    });
    // cerrar filtros cuando guardo filtro
    //$('body').on('click','#refreshFilter', function() {
    //    $("#newFilterContainer").fadeOut();
    //    $('#filterContacts').removeClass('on');
    //});

    // Update filter
    $('body').on('click','#saveFilter, #refreshFilter, #saveFilterAs', function(e) {
        e.preventDefault();
        var link = $(this).attr("href");
        var callback = $(this).attr("data-callaBackFunction");
        filterContacts.postFilter(link, callback);
    });
    $('body').on('click','#deleteFilter', function(e) {
        e.preventDefault();
        var filterName = filterContacts.getFilterName();
        var filterAmount = filterContacts.getFilterSelectedAmountOfContacts();
        $("#filtersDelete span.filter-name").html(filterName);
        $("#filtersDelete span.filter-ammount").html(filterAmount);
        $("#filtersDelete").modal("show")
    });

    $('body').on('click','#deleteFilterButton', function(e) {
        e.preventDefault();
        $("#filtersDelete").modal("hide");
        filterContacts.deleteFilter();
    });

});



function FilterContacts() {

    var that = this;
    var callBackBehaviour = "newsletterCallBacks";

    var allContactsFilterId= "0";
    var temporalFilterId = "-1";
    var newFilterId = "-2";

    var filterEditedText = i18n.tools.contact.filter.anonymousName
    var newFilterEditedText = i18n.tools.contact.filter.newAnonymousName

    if ($('#newsletter select#recipients').length > 0){
        // NEWS LETTER BEHAVIOUR
    }

    if ($('#searchContacts select#recipients').length > 0){
        // Contacts BEHAVIOUR
        callBackBehaviour = "searchContactsCallBacks";

    }

    this.serializedFilterData = function(){
        var $filterData = that.getFormFilterIdSelected();
        var inputs = $filterData.find("input, select").not($filterData.find("[id$='template'] input, [id$='template'] select"))
        var postData = inputs.serializeArray();
        postData.push({name:"filterId", value:that.getFilterId()});
        return postData;
    };

    this.postFilter = function(link, callback){
        that[callBackBehaviour][callback].prepare();
        var postData = that.serializedFilterData();
        pageLoadingOn();
        $.post( link, postData)
            .done(function(data) {
                var dataFilter = data.data.filter;
                that.updateAmountContacts(dataFilter.amountOfContacts);
                if (callback != undefined){
                    that[callBackBehaviour][callback].success(data);
                }
            })
            .fail(function(messageError) {
                display.warn("Error");
            })
            .always(function() {
                pageLoadingOff();
            });
    };
    this.updateAmountContacts = function(amountContacts){
        $('select#recipients option:selected').attr("data-amountContacts", amountContacts);
        $("#filterData #numberRecipients > span").html(amountContacts);
        $("#infoToContacts > span.amountRecipients").html(amountContacts);
        that[callBackBehaviour].changeSelectRecipients();
    };

    this.updateAmountContactsSilently = function(){
        $a = $("#refreshFilter");
        var link = $a.attr("href");
        var callback = $a.attr("data-callaBackFunction");
        filterContacts.postFilter(link, callback);
    };
    this.deleteFilter = function(){
        var link = $("#deleteFilterButton").attr("href");
        var filterId = that.getFilterId();
        var postData = [];
        pageLoadingOn();
        postData.push({name:'filterId', value:filterId});
        $.post( link, postData)
            .done(function(data) {
                that.changeFilterValue(allContactsFilterId);
                that.removeOptionToSelect(filterId)
            })
            .always(function() {
                pageLoadingOff();
            });
    };

    this.getFilterId= function(){
        var filterId = $("#recipients").val();
        if (filterId == undefined){
            filterId = allContactsFilterId;
        }
        return filterId;
    };
    this.getFilterName= function(){
        var filterId = $("#recipients").val();
        if (filterId == undefined){
            filterId = allContactsFilterId;
        }
        return $("#recipients option[value='"+filterId+"']").html();
    };
    this.getFormFilterIdSelected= function(){
        //var filterId = that.getFilterId();
        //return $("#formFilter_"+filterId.replace("-","_"))
        return $("#filterData")
    };
    this.loadFilter= function(){
        $('#filterContacts').attr("title",i18n.tools.contact.filter.conditions.close);
        if (that.getFilterId() == temporalFilterId){
            slideDownFilterInfo();
        }else{
            loadRealFilter();
        }
    };

    var loadRealFilter= function(){
        pageLoadingOn();
        var filterId = that.getFilterId();
        if (filterId == allContactsFilterId){
            $('select#recipients').val(newFilterId);
            var filterId = $("#recipients").val();
            that.updateAmountContacts(that.getTotalContacts());
        }
        var link = $("#filterContacts").attr("href");
        var postData = [];
        postData.push({name:'filterId', value:filterId});
        $.post(link, postData)
            .done(function(data) {
                var $filterData = $("#filterData");
                $filterData.addClass("hide");
                $filterData.html(data);
                $filterData.find("select#field").change(); /* Launch changeFieldOperation() event */
                $filterData.find("input:disabled").val(""); /* Chapu para eliminar val porque todos los inputs usan el mismo valor del command */
                slideDownFilterInfo();
                pageLoadingOff();
            })
            .fail(function(messageError) {
                display.warn("Error");
            })
            .always(function() {
                pageLoadingOff();
            });
    };

    var slideDownFilterInfo = function(){
        var $filterData = $("#filterData");
        $filterData.removeClass("hide");
        $filterData.children("div").removeClass("hide");
        $filterData.slideDown("fast", function(){$("#newFilterContainer").slideDown();$(".disabled-filters").slideDown()});
        $('#filterContacts, #infoToContacts').addClass('on');
    };
    this.isFilterEdited=function(){
        return $("input[name=filterEdited]").val() === 'true';
    };

    this.setFilterAsEdited = function(){
        $("input[name=filterEdited]").val(true);
    };

    this.changeFieldOperation = function(e){
        var $input = $(this);
        var $fieldSet = $input.parents("fieldset.new-filter-options");
        var val = $input.val();

        var activeOperator = ".text-operator";
        if (val == "STATUS"){
            activeOperator = ".status-operator";
        }

        $fieldSet.find(".filter-operator").addClass("hide");
        $fieldSet.find(activeOperator).removeClass("hide");
        $fieldSet.find(".filter-operator input, .filter-operator select").prop('disabled', false);
        $fieldSet.find(".filter-operator.hide input, .filter-operator.hide select").prop('disabled', true);
    };

    this.filterEditedEvent = function(e){
        that.setFilterAsEdited();
        that.updateAmountContactsSilently();
        var filterIdSelected = that.getFilterId();
        var filterId = temporalFilterId;
        if ($("#recipients option[value='"+filterIdSelected+"']").attr("data-anononymus") != undefined) {
            // Custom filter for a campaign. Not change
            //$("#recipients option[value='"+filterIdSelected+"']").attr("value",temporalFilterId);
        }
        if (filterIdSelected == temporalFilterId) {
            // EDITING AN ALREADY CREATED TEMPORAL FILTER

        } else if (filterIdSelected == newFilterId){
            // EDITING NEW FILTER
            var temporalFilterName =  newFilterEditedText;
            that.addOptionToSelect(temporalFilterId, temporalFilterName, "-");
            $("#recipients").val(filterId);
            that.setOriginalFilterToTemporalFilter(newFilterId)
        }else{
            // EDITING NORMAL FILTER
            var filterName = $("#recipients option[value='"+filterIdSelected+"']").html();
            var temporalFilterName =  filterEditedText.replace("{0}", filterName);
            that.addOptionToSelect(filterId, temporalFilterName, "-");
            that.setFilterName(temporalFilterName);
            $("#recipients").val(filterId);
            that.setOriginalFilterToTemporalFilter(filterIdSelected)
        }
        that[callBackBehaviour].filterEditedEvent(filterIdSelected);

    };

    this.closeFilterCampaignsOptions= function(){
        $('#filterContacts').removeClass('on');
        $('#filterContacts').attr("title",i18n.tools.contact.filter.conditions.open);
        $(".disabled-filters").slideUp("fast", function(){$(".disabled-filters").remove();});

        $('#infoToContacts, #filterContacts').removeClass('on');
        $("#filterData").html(""); // REMOVE INFO FILTER
        if (that.getFilterId()==newFilterId || that.getFilterId()==temporalFilterId ) {
            // Change 'new filter' to 'All'
            that.changeFilterValue(allContactsFilterId)
            that.removeOptionToSelect(temporalFilterId);
        }
    };

    this.setOriginalFilterToTemporalFilter=function(originalFilterId){
        $("#recipients option[value='"+temporalFilterId+"']").attr("data-orginal-filter-id", originalFilterId)
    };
    this.getOriginalFilterToTemporalFilter=function(){
        return $("#recipients option[value='"+temporalFilterId+"']").attr("data-orginal-filter-id")
    };

    this.getFilterSelectedAmountOfContacts= function(){
        return $('select#recipients option:selected').attr("data-amountContacts");
    };

    this.getTotalContacts = function(){
        return $("select#recipients option[value=0]").attr("data-amountcontacts")
    };

    this.changedFilterValueEvent = function(){
        var filterIdBeforeCloseOptions =that.getFilterId();
        that.closeFilterCampaignsOptions();
        if (filterIdBeforeCloseOptions==newFilterId) {
            //New filter
            that.loadFilter();
        }
        var amountContacts = that.getFilterSelectedAmountOfContacts();
        that.updateAmountContacts(amountContacts)
        that[callBackBehaviour].changeSelectRecipients();

    };

    this.changeFilterValue=function(filterId){
        $("#recipients").val(filterId);
        that.changedFilterValueEvent()
    };
    this.setFilterName=function(filterName){
        $("input[name=filterName]").val(filterName)
    };
    this.showModalListContacts=function(){
        var postData = that.serializedFilterData();
        postData.push({name:'page', value:0});
        postData.push({name:'size', value:100});
        postData.push({name:'asJson', value:true});
        var link = $("#infoToContacts").attr("href");
        pageLoadingOn();
        $.post(link, postData)
            .done(function(data) {
                var table=$("#filtersInfo .modal-body table tbody");
                table.html("");
                $.each(data.data, function(idx, contact){
                    table.append("<tr><td>"+contact.name+"</td><td>"+contact.email+"</td></tr>")
                });
                var txt = $("#filtersRecipients").text().replace(/\d+/, data.total);
                $("#filtersRecipients").text(txt);
                that.updateAmountContacts(data.total);

                pageLoadingOff();
                $("#filtersInfo").modal("show");
            })
            .fail(function(messageError) {
                display.warn("Error");
            })
            .always(function() {
                pageLoadingOff();
            });
    };

    this.addOptionToSelect=function(filterId, name, amountOfContacts){
        if ($("#recipients option[value='"+filterId+"']").length <= 0){
            $("#recipients option:last").before($('<option>', {
                value: filterId,
                text: name,
                'data-amountContacts':amountOfContacts
            }));
        }
    };

    this.removeOptionToSelect=function(filterId){
        if ($("#recipients option[value='"+filterId+"']").length > 0){
            $("#recipients option[value='"+filterId+"']").remove()
        }
    };

    this.addFilterTag = function (tagName) {
        // TODO
        // New input in filter
        $('#filterData .addButton').click();
        // Fill in filter
        var dynamicFieldset = $('#filterData .dynamic-fieldset');
        dynamicFieldset.eq(dynamicFieldset.length - 1);
        // Set tag name
        dynamicFieldset.find('#value').eq(0).val(tagName);
    };

    this.newsletterCallBacks = {
        init:function(){
            that.newsletterCallBacks.changeSelectRecipients();
            if ($("#recipients option[data-anononymus='true']").length>0){
                that.loadFilter();
            }
        },
        filterEditedEvent: function(filterIdSelected) {
            if (filterIdSelected == newFilterId){
                that.newsletterCallBacks.disableSendButtons();
            }
        },
        changeSelectRecipients:function(){
            var amountContacts = that.getFilterSelectedAmountOfContacts();
            if (amountContacts<=0) {
                that.newsletterCallBacks.disableSendButtons();
            }else{
                that.newsletterCallBacks.enableSendButtons();
            }
        },
        disableSendButtons:function(){
            $("#openCalendar").addClass("disabled");
            $("#send").addClass("disabled");
        },
        enableSendButtons:function(){
            $("#openCalendar").removeClass("disabled");
            $("#send").removeClass("disabled");
        },

        campaignFilterRefresh:{
            prepare:function(){
            },
            success:function(data){
                //console.log(data)
                //display.success(data.msg)
            }
        },
        campaignFilterSave:{
            prepare:function(){
                var filterId = that.getFilterId();
                if (filterId == temporalFilterId){
                    filterId = that.getOriginalFilterToTemporalFilter();
                    $("#recipients").val(filterId);
                    that.setFilterName($("#recipients option[value='"+filterId+"']").html())
                }
            },
            success:function(data){
                that.removeOptionToSelect(temporalFilterId);
                that.closeFilterCampaignsOptions();
            }
        },
        campaignFilterSaveAs:{
            prepare:function(){},
            success:function(data){
                var filter = data.data.filter;
                var htmlFilter = data.data.filterRendered;

                $("#newFilterContainer").append(htmlFilter);
                var filter = data.data.filter;
                that.addOptionToSelect(filter.id, filter.name, filter.amountOfContacts);
                $("#recipients").val(filter.id);
                that.closeFilterCampaignsOptions();
            }
        }
    };

    this.init = function () {
        $("#filterData").on("change", "input,select:not(#field)", that.filterEditedEvent);
        $("#filterData").on("change", "select#field", that.changeFieldOperation);
        this[callBackBehaviour].init()
    };

    this.searchContactsCallBacks = {
        init:function(){

        },
        filterEditedEvent: function(filterIdSelected){

        },
        changeSelectRecipients:function(data){
            if ($('select#recipients').val()!=newFilterId) {
                that.searchContactsCallBacks.resetPage();
                that.searchContactsCallBacks.loadTableContacts();
            }
        },
        campaignFilterRefresh:{
            prepare:function(){},
            success:function(data){
                that.searchContactsCallBacks.resetPage();
                that.searchContactsCallBacks.loadTableContacts();
            }
        },
        campaignFilterSave:{
            prepare:function(){
                // THE SAME LOGIC AS NEWSLETTER
                that.newsletterCallBacks.campaignFilterSave.prepare();
            },
            success:function(data){
                that.searchContactsCallBacks.resetPage();
                that.newsletterCallBacks.campaignFilterSave.success(data)
                that.searchContactsCallBacks.loadTableContacts();
            }
        },
        campaignFilterSaveAs:{
            prepare:function(){},
            success:function(data){
                that.searchContactsCallBacks.resetPage();
                that.newsletterCallBacks.campaignFilterSaveAs.success(data)
                that.searchContactsCallBacks.loadTableContacts();
            }
        },
        loadTableContacts:function(){
            $("#listContacts").html("");
            var link = $("#listContacts").attr("data-ajaxUrlContacts");
            var postData = $("#contactFilterForm").serializeArray();
            pageLoadingOn();
            $.post( link, postData)
                .done(function(data) {
                    $("#listContacts").html(data);
                    var quickSearch = $("#quickSearchByName").val()
                    if (quickSearch != undefined && quickSearch.length > 0){
                        $( "#contactsList li h3 a.contactStats" ).each(function( index ) {
                            var name = $(this).html();
                            var re = new RegExp(quickSearch, 'gi');
                            var highlighterName=name.replace(re, '<span class="highlighted">\$&</span>');
                            $(this).html(highlighterName)
                        });
                        $( "#contactsList li p.email span.raw-email" ).each(function( index ) {
                            var email = $(this).html();
                            var re = new RegExp(quickSearch, 'gi');
                            var highlighterName=email.replace(re, '<span class="highlighted">\$&</span>');
                            $(this).html(highlighterName)
                        });
                    }
                    prepareContactTags();
                })
                .fail(function(messageError) {
                    display.warn("Error");
                })
                .always(function() {
                    pageLoadingOff();
                });
        },
        sort:function(sortField, sortDirection) {
            $("input[name=sort\\.field]").val(sortField);
            $("input[name=sort\\.direction]").val(sortDirection);
            that.searchContactsCallBacks.resetPage();
            that.searchContactsCallBacks.loadTableContacts();
        },
        page:function (page) {
            $("input[name=page]").val(page);
            that.searchContactsCallBacks.loadTableContacts();
        },
        resetPage:function () {
            $("input[name=page]").val(0)
        }
    };
    this.init();
}

var filterContacts = new FilterContacts();
