$(function () {
    // abrir opciones nuevo filtro con select
    $('select#recipients').on('change', filterContacts.changedFilterValueEvent);
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
    $("#listContacts").on("change", ".pag-list-contacts select[name=sizePage]",function(e){
        e.preventDefault();
        var sizePage = $(this).val()
        $("select[name=sizePage]").val(sizePage)
        filterContacts.searchContactsCallBacks.pageSize(sizePage)
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
                var oldValue = $('span.amountRecipients').text(); // ÑAPA para filtros  dinámicos
                var contacts = parseInt(oldValue) - 1;
                filterContacts.updateAmountContacts(contacts);
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
            filterContacts.lastQuickSearch = $(this).val();
            return false;
        }
    });

    $("#quickSearchByName").on("blur", function(e){
        if (filterContacts.lastQuickSearch != $(this).val()){
            filterContacts.searchContactsCallBacks.page(0);
            filterContacts.lastQuickSearch = $(this).val();
        }
        return false;
    });

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
        filterContacts.getFilterSelectedAmountOfContacts($('select#recipients option:selected'), function(amountOfContacts){
            $("#filtersDelete span.filter-name").html(filterName);
            $("#filtersDelete span.filter-ammount").html(amountOfContacts);
            $("#filtersDelete").modal("show")
        });
    });

    $('body').on('click','#deleteFilterButton', function(e) {
        e.preventDefault();
        $("#filtersDelete").modal("hide");
        filterContacts.deleteFilter();
    });

    $("#exportContacts").on("click", function(e){
        pageLoadingOn();
        e.preventDefault();
        var $a = $(this)
        var link = $a.attr("href")
        var filterData = filterContacts.serializedFilterData()
        $.post(link, filterData)
            .done(function(data) {
                $("#export-contacts-modal").modal("show")
            })
            .fail(function(messageError) {
                display.warn("Error");
            })
            .always(function() {
                pageLoadingOff();
            });
    })



    $("#tabs-edit-contact #notes #updateContactNotes").on("submit",function(e){
        e.preventDefault();
        var postData = $(this).serialize();
        var link = $(this).attr("action")
        $.post( link, postData)
            .done(function(data) {
                if (data.err != undefined){
                    display.warn(data.err)
                }else{
                    display.success(data.msg)
                }
            })
            .fail(function (messageError) {
                display.warn("Error");
            })
            .always(function () {
                pageLoadingOff();
            });
        return false;
    });

    $("#tabs-edit-contact #contactIssues #addContactIssueForm").on("submit", function (e) {
        e.preventDefault();
        var $form = $(this)
        var postData = $form.serialize();
        var link = $form.attr("action");
        var $ul = $form.siblings("ul.contact-issues")
        if ($form.valid()) {
            pageLoadingOn("Adding Issue Contact");
            $.post(link, postData)
                .done(function (data) {
                    $ul.find("li:first").after(data);
                    $form.find("input[name=note]").val("");
                    $form.find("select[name=issueType] option").prop("selected", false);
                })
                .fail(function (messageError) {
                    display.warn("Error");
                })
                .always(function () {
                    pageLoadingOff();
                });
        }
        return false;
    });

    $("#tabs-edit-contact #contactIssues").on("click", ".contact-issue-delete a", function (e) {
        e.preventDefault();
        console.log("DELETE ISSUE")
        $a = $(this)
        var link = $a.attr("href");
        var $li = $a.parents("li.contact-issue");
        pageLoadingOn("Deleting Issue Contact");
        $.post(link)
            .done(function (data) {
                $li.remove();
            })
            .fail(function (messageError) {
                display.warn("Error");
            })
            .always(function () {
                pageLoadingOff();
            });
    });

    $("#tabs-edit-contact #extraInfo #extraInfoContact").on("submit", function (e) {
        e.preventDefault();
        var postData = $(this).serialize();
        var link = $(this).attr("action")
        $.post(link, postData)
            .done(function (data) {
                if (data.err != undefined) {
                    display.warn(data.err)
                } else {
                    display.success(data.msg)
                }
            })
            .fail(function (messageError) {
                display.warn("Error");
            })
            .always(function () {
                pageLoadingOff();
            });
        return false;
    });


    //TODO: This function should go to a more generic js
    function prepareSelectCampaigns() {
        $select = $("select[name=campaignId][data-select-options-ajax-url]")
        var url = $select.attr("data-select-options-ajax-url")
        $.get(url)
            .done(function (data, status, xhr) {
                for (const campaign of data) {
                    $select.append("<option value='" + campaign.id + "'>" + campaign.name + "</option>")
                }
                if (data.length == 1) {
                    $select.find('option').get(0).remove();
                }

            })
            .fail(function (data) {
                console.log(data)
            })
            .always(function (data) {

            })
        ;
    }

    prepareSelectCampaigns();

    // Bulk actions -- Open modal
    $("#listContacts").on("change", "#contactsOrderOptions .bulk-actions", function (e) {
        e.preventDefault();
        var type = parseInt($(this).val());

        // Reset type
        $(this).val(-1);

        var numSelectedContactsElement = $('.num-selected-contacts');
        var contactsSelected = $('#contactsList .checkbox-inline input[type=checkbox]:checked');
        var isAllContactsSelected = $('#contactsOrderOptions .checkbox-inline input[type=checkbox]').is(':checked');

        if (isAllContactsSelected) {
            // Update text
            filterContacts.getFilterSelectedAmountOfContacts($('select#recipients option:selected'), function(amountOfContacts){
                numSelectedContactsElement.text(amountOfContacts);
            });
        } else {
            // Update text
            numSelectedContactsElement.text(contactsSelected.length);
        }

        // Select modal
        switch (type) {
            case 1:
                // "Delete all" popup
                $('#bulk-action-delete-all-modal').modal('show');
                break;
            case 2:
                // "Add tags" popup
                $('#bulk-action-add-tags-modal').modal('show');
                break;
            case 3:
                // "Remove tags" popup
                $('#bulk-action-remove-tags-modal').modal('show');
                break;
            case 4:
                // Generate personal code popup
                $('#bulk-action-generate-personal-code').modal('show');
                break;
            case 5:
                // Remove personal code popup
                $('#bulk-action-remove-personal-code').modal('show');
                break;
        }
    });

    // Bulk actions -- Submit modal form
    $('#bulk-action-delete-all-modal form, ' +
        '#bulk-action-add-tags-modal form, ' +
        '#bulk-action-generate-personal-code form, ' +
        '#bulk-action-remove-personal-code form, ' +
        '#bulk-action-remove-tags-modal form').submit(function (e) {
        e.preventDefault();

        var contactsSelected = $('#contactsList .checkbox-inline input[type=checkbox]:checked');
        var isAllContactsSelected = $('#contactsOrderOptions .checkbox-inline input[type=checkbox]').is(':checked');

        var actionPath = $(this).attr("action");
        var postData = "";

        if (isAllContactsSelected) {
            // Send filter
            var $filterData = $('#contactFilterForm');
            var inputs = filterContacts.getFilterInputs($filterData)
            postData = inputs.serialize();

            postData += "&checkedAll=1";
        } else {
            // Send list of ids
            var listIds = "listIds=";
            contactsSelected.each(function () {
                listIds += $(this).val() + ",";
            });
            listIds = listIds.slice(0, -1);
            postData = listIds;
            postData += "&checkedAll=0";
        }

        // Additional params
        var type = parseInt($(this).data('type'));
        switch (type) {
            case 2:
                postData += "&tags=" + $('#addTagsField').val();
                break;
            case 3:
                postData += "&tags=" + $('#removeTagsField').val();
                break;
        }

        $.post(actionPath, postData)
            .done(function (data) {
                if (data.status == 'ok') {
                    // All good
                    display.success(data.msg);
                    setTimeout(function() {
                        // Update contact's list
                        filterContacts.searchContactsCallBacks.loadTableContacts();

                        // Close modals
                        $('#bulk-action-delete-all-modal').modal('hide');
                        $('#bulk-action-add-tags-modal').modal('hide');
                        $('#bulk-action-remove-tags-modal').modal('hide');
                        $('#bulk-action-generate-personal-code').modal('hide');
                        $('#bulk-action-remove-personal-code').modal('hide');
                    }, 1000);
                } else {
                    display.warn(data.msg);
                }
            })
            .fail(function () {
                display.warn("Error");
            });
    });

    // Bulk actions -- "Check-all" checkbox
    $('#listContacts').on('change', '#contactsOrderOptions .checkbox-inline input[type=checkbox]', function (e) {
        var contactsCheckbox = $('#contactsList .checkbox-inline input[type=checkbox]');

        if ($(this).is(':checked')) {
            // Check all "contact item" checkbox
            contactsCheckbox.prop('checked', true);

            // Show bulk actions
            $('.bulk-actions').show();
        } else {
            // Uncheck all "contact item" checkbox
            contactsCheckbox.prop('checked', false);

            // Hide bulk actions
            $('.bulk-actions').hide();
        }
    });

    // Buk actions -- "Contact item" checkbox
    $('#listContacts').on('change', '#contactsList .checkbox-inline input[type=checkbox]', function (e) {
        var contactsSelected = $('#contactsList .checkbox-inline input[type=checkbox]:checked');

        if ($(this).is(':checked')) {
            if (contactsSelected.length >= 2) {
                // Show bulk actions
                $('.bulk-actions').show();
            }
        } else {
            // Uncheck "check-all" checkbox
            $('#contactsOrderOptions .checkbox-inline input[type=checkbox]').prop('checked', false);

            // Check if there are no users selected
            if (contactsSelected.length < 2) {
                $('.bulk-actions').hide();
            }
        }
    });

    // TAGS


    //importar contacts add tag
    $('body').on('click','.addTagBtn', function(e) {
        e.preventDefault();
        if ($(this).hasClass('on')) {
            $(this).next('ul').show();
            $(this).removeClass('on');
            $(this).closest('.addTag').addClass('off');
        } else {
            $(this).next('ul').hide();
            $(this).addClass('on');
            $(this).closest('.addTag').delay(1000).removeClass('off');
            $(this).parent("form").find("input.tt-input").focus();
        }
    });

    $(".contact-activity-history").on("click","a.resend-email", function(e) {
        e.preventDefault();
        var $link = $(this)
        var linkId = guid();
        $link.attr("id", linkId);
        $("#activity-resend-confirm").modal("show");
        $("#activity-resend-confirm").find(".modal-footer button").attr("data-linkId", linkId);
    });

    $(".contact-activity-history").on("click","#activity-resend-confirm .modal-footer button", function(e){
        e.preventDefault();
        console.log("CLICK ON BUTTON")
        var $button = $(this);
        var linkId = $button.attr("data-linkId");
        var $link = $("#"+linkId);
        resend($link, $button);
    });

    $('body').on('click','.resend-bulletin', function(e) {
        e.preventDefault();
        var $a = $(this)
        var link = $a.attr("href")
        $("#activity-resend-bulletin-confirm").modal("show");
        $("#activity-resend-bulletin-confirm").find(".modal-footer button").attr("data-link", link);
    });

    $("#bulletins").on("click","#activity-resend-bulletin-confirm .modal-footer button", function(e){
        e.preventDefault();
        console.log("CLICK ON BUTTON")
        const $button = $(this);
        const url = $button.attr("data-link");
        $.post(url)
            .done(function(data) {
                display.success(data.message)
                $("#activity-resend-bulletin-confirm").modal("hide");
            })
            .fail(function(messageError) {
                display.error("Error");
            })
            .always(function() {
                pageLoadingOff();
            });
    });

    function resend($link, $button){
        var $icon = $button.find("span.fa-angle-double-right")
        var url = $link.attr("href");
        $icon.removeClass("fa-angle-double-right fa-check-circle fa-exclamation-circle error")
        $icon.addClass("fa-spinner fa-spin")
        $.post(url)
            .done(function(data) {
                $icon.removeClass("fa-spinner fa-spin")
                $icon.addClass("fa-check-circle")
                setTimeout(function(){$("#activity-resend-confirm").modal("hide")}, 500);
            })
            .fail(function(messageError) {
                $icon.removeClass("fa-spinner fa-spin")
                $icon.addClass("fa-exclamation-circle error")
            })
            .always(function() {
                pageLoadingOff();
            });
        //Not submit
        return false;
    };

    $("body").on('submit', 'form.addTag', function(e){
        e.preventDefault();
        pageLoadingOn();
        var $form = $(this);
        var closeInputs =$form.children(".addTagBtn");
        var url = $form.attr("action");
        var postData = $form.serialize();

        $.post(url, postData)
            .done(function(data) {
                var $ul = $form.find("ul");
                $ul.html("");
                var urlSearchByTag = $ul.attr("data-genericTagLink");
                for (i = 0; i < data.tags.length; i++) {
                    var tagName = data.tags[i];
                    var tagLink = urlSearchByTag.replace("REPLACED_TAG",tagName)
                    $ul.append('<li><a href="'+tagLink+'" class="tag label label-info">'+tagName+'</a></li>');
                    tagsnames.add({name:data.tags[i]})
                }
                closeInputs.click();
            })
            .fail(function(messageError) {
                display.warn("Error");
            })
            .always(function() {
                pageLoadingOff();
            });
        //Not submit
        return false;
    })

    // Add tags to filter when clicked
    $('body').on('click', '.addTagBtn', function() {
        // Get value
        var value = $(this).text();
        // TODO: Add to filter

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

    this.lastQuickSearch = "";

    if ($('#newsletter select#recipients').length > 0){
        // NEWS LETTER BEHAVIOUR
    }

    if ($('#searchContacts select#recipients').length > 0){
        // Contacts BEHAVIOUR
        callBackBehaviour = "searchContactsCallBacks";

    }

    this.getFilterInputs=function($filterData){
        return $filterData.find("input, select").not($filterData.find("[id$='template'] input, [id$='template'] select, .hide select, .hide input"));
    };
    this.serializedFilterData = function(){
        var $filterData = that.getFormFilterIdSelected();
        var inputs = that.getFilterInputs($filterData)
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
                $filterData.find("input:disabled").val(""); /* Chapu para eliminar val porque todos los inputs usan el mismo valor del command */
                slideDownFilterInfo();
                $filterData.find("select#field").change(); /* Launch changeFieldOperation() event */
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
        }else if (val =="BLACK_LIST" || val == 'SUBSCRIBED') {
            activeOperator = ".boolean-operator"
        }else if (val =="PERSONAL_CODE"){
            activeOperator = ".exists-operator"
        }else if (val =="CONTACT_TYPE"){
            activeOperator =".contactType-operator"
        }else if (val =="LANGUAGE"){
            activeOperator =".language-operator"
        }else if (val =="EVENT"){
            activeOperator =".assistant-event-operator"
        }else if (val == "PARTICIPATORY_BUDGET") {
            activeOperator = ".participatoryBudget-operator"
        } else if (val == "CONTEST") {
            activeOperator = ".contest-operator"
        } else if (val == "ISSUES") {
            activeOperator = ".issues-operator"
        } else if (val == "SURVEY") {
            activeOperator = ".survey-operator"
        }

        $fieldSet.find(".filter-operator").addClass("hide");
        $fieldSet.find(activeOperator).removeClass("hide");
        $fieldSet.find(".filter-operator input, .filter-operator select").prop('disabled', false);
        $fieldSet.find(".filter-operator.hide input, .filter-operator.hide select").prop('disabled', true);

        that.setFilterAsEdited();
        that.updateAmountContactsSilently();
    };

    this.filterEditedEvent = function(e){
        if (e != undefined){
            e.preventDefault();
        }
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

    this.getFilterSelectedAmountOfContacts= function($option, callback){
        var amountOfContacts = $option.attr("data-amountContacts");
        console.log($option)
        console.log(amountOfContacts)
        var filterId = $option.attr("value");
        if (typeof amountOfContacts !== typeof undefined && amountOfContacts !== false) {
            callback(amountOfContacts)
        }else if (filterId != undefined) {
            $.get(kuorumUrls.politicianContactFilterData, { filterId: filterId, render:'JSON'}, function(filterData){
                var ajaxAmountOfContacts = -1;
                ajaxAmountOfContacts = filterData.amountOfContacts;
                $option.attr("data-amountContacts", ajaxAmountOfContacts);
                callback(ajaxAmountOfContacts)
            }, 'json')
        }else{
            // console.log("Loading filter without ID")
        }
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
        that.getFilterSelectedAmountOfContacts($('select#recipients option:selected'), function(amountOfContacts){
            that.updateAmountContacts(amountOfContacts)
            that[callBackBehaviour].changeSelectRecipients();
        });
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
                    var surname = contact.surname;
                    if (surname == undefined || surname == "null"){
                        surname = ""
                    }
                    table.append("<tr><td>"+contact.name+" "+surname+"</td><td>"+contact.email+"</td></tr>")
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
            //Preparar el select segun el option seleccionado
            that.changedFilterValueEvent();

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
            var amountContacts = that.getFilterSelectedAmountOfContacts($('select#recipients option:selected'), function(amountOfContacts) {
                if (amountContacts <= 0) {
                    that.newsletterCallBacks.disableSendButtons();
                } else {
                    that.newsletterCallBacks.enableSendButtons();
                }
            })
        },
        disableSendButtons:function(){
            $("#openCalendar").addClass("disabled");
            $("#send-draft").addClass("disabled"); // Post && Debate send button
            $("#send").addClass("disabled"); // News letter send button
        },
        enableSendButtons:function(){
            $("#openCalendar").removeClass("disabled");
            $("#send-draft").removeClass("disabled"); // Post && Debate send button
            $("#send").removeClass("disabled"); // News letter send button
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
                that.removeOptionToSelect(temporalFilterId);
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
            if(window.location.search) {
                slideDownFilterInfo()
                var $filterData = $("#filterData");
                $filterData.find("select#field").change();
                $filterData.find("input:disabled").val("")
                that.searchContactsCallBacks.loadTableContacts();
            }else{
                //Preparar el select segun el option seleccionado
                that.changedFilterValueEvent();
            }
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
            $("#contactFilterForm .hide select").prop("disabled",true);
            $("#contactFilterForm .hide input").prop("disabled",true);
            var postData = $("#contactFilterForm").serializeArray();
            //var postData = that.serializedFilterData() ?????
            $("#contactFilterForm .hide input").prop("disabled",false);
            $("#contactFilterForm .hide select").prop("disabled",false);
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
                    prepareAutocompleteTags();
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
        pageSize:function (pageSize) {
            $("input[name=size]").val(pageSize);
            that.searchContactsCallBacks.loadTableContacts();
        },
        resetPage:function () {
            $("input[name=page]").val(0)
        }
    };
}

var filterContacts = new FilterContacts();
filterContacts.init();
