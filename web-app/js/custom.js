$(document).ajaxStop(function () {
    // inicia el timeago
    $("time.timeago").timeago();
    preparePopover();
});

$(document).ready(function() {

    setTimeout(prepareProgressBar, 500);
    prepareProgressBar();

    $("#brand.disabled").on('click', function (e) {
        e.preventDefault();
    });

    // botón Seguir de las cajas popover y página ley-participantes
    $(document).on({
        mouseenter: function () {
            $(this).html($(this).attr('data-message-follow_hover'));
        },
        mouseleave: function () {
            $(this).html($(this).attr('data-message-follow'));
        }
    }, ".follow.enabled"); //pass the element as an argument to .on

    $(document).on({
        mouseenter: function () {
            $(this).html($(this).attr('data-message-unfollow_hover'));
        },
        mouseleave: function () {
            $(this).html($(this).attr('data-message-unfollow'));
        }
    }, ".follow.disabled"); //pass the element as an argument to .on



    $('body').on("click", ".btn.follow", function(e) {
        e.preventDefault();
        e.stopPropagation();
        clickedButtonFollow($(this))
    });
    function clickedButtonFollow(button){
        var buttonFollow = $(button);
        if (buttonFollow.hasClass('noLogged')){
            var buttonFollowUUID = guid()
            buttonFollow.attr("id",buttonFollowUUID)
            $('#registro').find("form").attr("callback", "clickFollowAfterLogIn")
            $('#registro').find("form").attr("buttonId", buttonFollowUUID)
            $('#registro').modal('show');
        }
        else if ( followActions.isAlreadyFollowing(buttonFollow) ){
            var url = followActions.getUnFollowUrl(buttonFollow);
            followActions.ajaxFollow(url, buttonFollow, function(data, status, xhr) {
                var message = buttonFollow.attr('data-message-follow');
                var userId = followActions.getUserId(buttonFollow)
                removeUserCampaigns(userId)
                $("button.follow[data-userId="+userId+"]").html(message).removeClass('disabled').addClass('enabled');
            });
        } else {
            var url = followActions.getFollowUrl(buttonFollow);
            followActions.ajaxFollow(url, buttonFollow, function(data, status, xhr) {
                var message = buttonFollow.attr('data-message-unfollow');
                var userId = followActions.getUserId(buttonFollow)
                $("button.follow[data-userId="+userId+"]").html(message).removeClass('enabled').addClass('disabled');
                deleteUserRecommendation(userId);
                addUserCampaigns(userId);
                var $followingCounter = $(".activity li.following span.counter");
                if ($followingCounter.length > 0){
                    var numFollowing = parseInt($followingCounter.html());
                    $followingCounter.html(numFollowing +1);
                }
            });
        }
    }

    function addUserCampaigns(userId){
        if (sortCampaigns != undefined ){
            sortCampaigns.appendCampaignsOfUser(userId);
        }
    }
    function removeUserCampaigns(userId){
        if (sortCampaigns != undefined ){
            sortCampaigns.removeCampaignsOfUser(userId);
        }
    }

    function deleteUserRecommendation(userId){
        $(".user.recommendation-deletable[data-userid="+userId+"]").fadeOut('slow', function(){
            $(".user.recommendation-deletable[data-userid="+userId+"]").remove();
        });
        $("#user-list-followers-"+userId).fadeOut('slow').remove()
    }


    // si el box de Usuarios de la columna C no lleva la X de cierre quito el hueco de la derecha del botón Follow
    if ( !$('.user-list-followers .actions .close').length ) {
        $('.user-list-followers > .user > .actions').css('width', 'auto');
    }

    // desvanecer y eliminar los usuario de la lista "A quién seguir"
    $('body').on('click','ul.user-list-followers > li.user .actions .close', function(e) {

        //$(this).closest('li.user').fadeOut('slow', function(){
        //  $(this).remove();
        //});
        var userId = $(this).attr("data-userid")
        deleteUserRecommendation(userId)

    });
    $('body').on('click','ul.user-list-followers > li.user:only-child .actions .close', function(e) {

        //$(this).closest('.boxes.follow').fadeOut('slow', function(){
        //  $(this).remove();
        //});
        var userId = $(this).attr("data-userid")
        deleteUserRecommendation(userId)
    });

    function clickedButtonContact(button){
        var buttonContact = $(button);
        $('#contact-modal').modal('show');
    }

    $('body').on("click", ".btn.contact", function(e) {
        e.preventDefault();
        e.stopPropagation();
        clickedButtonContact($(this))
    });

    function clickedDeleteRecommendedUser(button, fadeElement){
        var buttonDeleteRecommendedUser= $(button);
        if (buttonDeleteRecommendedUser.hasClass('noLogged')){
            var url = buttonDeleteRecommendedUser.attr("data-noLoggedUrl");
            location.href =url;
        } else {
            var url = buttonDeleteRecommendedUser.attr("data-ajaxDeleteRecommendedUserUrl");
            ajaxDeleteRecommendedUser(url, buttonDeleteRecommendedUser, function(data, status, xhr) {
                var userId = buttonDeleteRecommendedUser.attr('data-userId');
                buttonDeleteRecommendedUser.closest(fadeElement).fadeOut('slow', function(){
                    $(this).remove();
                });
                if(data.error){
                    display.warn(data.message);
                }
            });
        }
    }

    function ajaxDeleteRecommendedUser(url, buttonFollow, doneFunction){
        $.ajax( {
            url:url,
            statusCode: {
                401: function() {
                    display.info("Estás deslogado");
                    setTimeout('location.reload()',5000);
                }
            },
            beforeSend: function(){
                buttonFollow.html('<div class="loading xs"><span class="sr-only">Cargando...</span></div>');
            },
            complete:function(){
//                console.log("end")
            }
        }).done(function(data, status, xhr) {
                doneFunction(data,status,xhr);
        });
    }

	// Buscador: cambia el placeholder según el filtro elegido
	$(function() {

		var $ui = $('#search-form');
		$ui.find('#filters li a').bind('focus click',function(){
			var filtro = $(this).text();
			$ui.find('#srch-term').attr('placeholder', filtro);

			if ( $(this).hasClass('enleyes') ) {
				$('#filterSign').html('<span class="fa fa-briefcase"></span>');
			} else if ( $(this).hasClass('enpersonas') ) {
				$('#filterSign').html('<span class="fa fa-user"></span>');
			} else {
				$('#filterSign').html('');
			}

		});

	});

    //search filters
	$('#searchFilters input:checked').css('display','block');
	$('#searchFilters input:checked').closest('label').css('color','#545454');

    $('#searchFilters input[type=checkbox]').change(function(){
        console.log("kk");
        $("#searchFilters input[type=checkbox]").prop('checked', false);

        $("#searchFilters input[value="+$(this).val()+"]").prop('checked', true);
        reloadSearchNewFilters()
    });

    function reloadSearchNewFilters(){
        var form = $("#searchFilters");
        form.submit();
//        var elementToUpdate=$("#"+form.attr('data-updateElementId'))
//        $.ajax( {
//            url:form.attr("action"),
//            data:form.serialize()
//        }).done(function(data, status, xhr) {
//            $(elementToUpdate).html(data)
//        })

    }

    $('.ajax.popover-trigger.more-users').on('shown.bs.popover', function () {
        var that = $(this);
        var content = $(this).next().children(".popover-content");
        var ajaxUrl = content.children("a").attr("href");
        var ulUserLists = content.find("ul");
        $.ajax( {
            url:ajaxUrl,
            statusCode: {
                500: function() {
                    display.error("Error en el sistema")
                }
            },
            beforeSend: function(){
                ulUserLists.html('<li class="loading xs"><span class="sr-only">Cargando...</span></li>')
            },
            complete:function(){
//                console.log("end")
            }
        }).done(function(data, status, xhr) {
            ulUserLists.html(data)
        })
    });

    $(".multimedia .groupRadio input[type=radio]").on('click', function(e){
        var multimediaType = $(this).val();
        $('[data-multimedia-switch="on"]').hide();
        $('[data-multimedia-type="'+multimediaType+'"]').show()

    });

    // desvanecer y eliminar los usuario de la lista "A quién seguir"
    $('body').on('click','ul.user-list-followers > li.user .close', function(e) {
        clickedDeleteRecommendedUser(this, 'li.user');
    });
    $('body').on('click','ul.user-list-followers > li.user:only-child .close', function(e) {
        clickedDeleteRecommendedUser(this, '.boxes.follow');
    });

    $('body').on('click','aside.condition > .close', function(e) {
        $(this).parent('aside.condition').fadeOut('slow', function(){
            $(this).remove();
        });
    });

    // load more
    $("a.loadMore").on("click", function (e) {
        loadMore(e, this)
    });
    function loadMore(e, that) {

        e.preventDefault();
        var link = $(that);
        var url = link.attr('href');
        var formId = link.attr('data-form-id');
        var paramAppender = "?";
        if (url.indexOf("?")>-1){
            paramAppender = "&"
        }
        var max = $.parseJSON($("#" + formId).find('input[name=max]').val() || 10);
        var offset = $.parseJSON(link.attr('data-offset') || max);
         //Para que sea un integer
        url += paramAppender + "offset=" + offset + "&" + $('#' + formId).serialize();
        var parentId = link.attr('data-parent-id');
        var callback = link.attr('data-callback')
        var loadingId = parentId + "-loading";
        var parent = $("#" + parentId);
        parent.append('<div class="loading" id="' + loadingId + '"><span class="sr-only">Cargando...</span></div>');
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
                var moreResults = $.parseJSON(xhr.getResponseHeader('moreResults'));
                 //Para que sea un bool
                link.attr('data-offset', offset + max);
                if (moreResults){
                    link.remove()
                }
                if (callback != undefined && callback != ""){
                    window[callback]();
                }
            })
            .fail(function(data) {
                console.log(data)
            })
            .always(function(data) {
                $("#" + loadingId).remove();
                $("time.timeago").timeago();
            });
    }
    youtubeHelper.replaceNonExistImage();
    // HANDLE WRONG YOUTUBE VIDEOS
    youtubeHelper.replaceAllWrongYoutubeImages();


    /*****************
     * Delayed modules
     *****************/
    $(".delayed").each(function(){
        reloadDynamicDiv($(this))
    });
    /*******************************
     *********** CAUSES ************
     *******************************/
    // SUPPORT CAUSES SMALL
    $("body").on("click", ".causes-tags .cause-support", function(e){
        e.preventDefault();
        e.stopPropagation();
        var $parent = $(this).parents(".cause");
        if ( $parent.hasClass("noLogged")){
            $('#registro').modal('show');
        }else{
            $a = $(this).find("a");
            clickSupportCause($a, function(){})
        }
    });
    function clickSupportCause($a, actionAfterSupport){
        hearBeat(2,  $a.find(".fa"));
        $.get(  $a.attr('href'), function( data ) {
            var citizenSupports = data.cause.citizenSupports;
            var $parent = $a.parents(".cause");
            $parent.toggleClass("active");
            $parent.find(".cause-counter").html(citizenSupports);

            relaodAllDynamicDivs();
            actionAfterSupport()
        });
    }

    function hearBeat(numHeartBeats, $element){
        if (numHeartBeats <0){
            return;
        }
        var back = numHeartBeats % 2 == 0;
        $element.animate(
            {
                'font-size': (back) ? '14px' : '20px',
                'opacity': (back) ? 1 : 0.5
            }, 100, function(){hearBeat(numHeartBeats -1, $element)});
    }

    /*******************************************/
    /******* USER RATES ************************/
    /*******************************************/
    $("body").on("click", ".user-rating-form fieldset.rating input", function (e) {
        var $form = $(this).closest("form");
        var url = $form.attr("action");
        var myRate = $(this).val();
        $.ajax({
            url: url,
            data: {rate: myRate}
        }).success(function (data) {
            // Update rating
            var newRate = Math.floor(data.userReputation + 0.5);

            // Only for profile rating
            if ($form.hasClass("rating-profile")) {
                // Update "read only" rating
                $(".user-rating label").removeClass("active");
                $(".user-rating label[for=star" + newRate + "]").addClass("active");

                // Popover - Change both ratings (visible & invisible)
                $form.closest('.popover').parent().find("input[name=rating]").removeAttr("checked");
                $form.closest('.popover').parent().find("input[name=rating][value=" + myRate + "]").attr('checked', true);
                $form.closest('.popover').parent().find("input[name=rating][value=" + myRate + "]").prop("checked", true);

                // Popover - Statistics
                $form.closest('.popover').parent().find(".counter").html(myRate);
                $form.closest('.popover').parent().find(".rating-over").find(".counter.user-reputation").html(data.userReputation.toFixed(2));
                $([1, 2, 3, 4, 5]).each(function(i) {
                    var pos = i+1;
                    $(".rate-progress-bar-" + pos).attr("aria-valuetransitiongoal", data.evaluationPercentages[pos] * 100);
                    $(".rate-progress-bar-" + pos).find("span").html(data.evaluationPercentages[pos] * 100 + "%");
                    $(".rate-progress-bar-" + pos).css("width", data.evaluationPercentages[pos]*100 + "%")
                });
                printCharts();

                // Show modal
                $("#rating-social-share-modal-" + data.userId).modal("show");
            } else {
                // Change only the invisible one
                $form.children(".rating").html(i18n.politician.valuation.rate.success)
                $form.parents('.popover').siblings('.popover:not(.in)').find("input[name=rating]").removeAttr("checked");
                $form.parents('.popover').siblings('.popover:not(.in)').find("input[name=rating][value=" + newRate + "]").attr('checked', true);
                $form.parents('.popover').siblings('.popover:not(.in)').find("input[name=rating][value=" + newRate + "]").prop("checked", true);
            }
            dataLayer.push({
                event:"Valuation",
                'politician.alias': data.userAlias,
                'politician.rating.userVote': myRate
            });
//            $(".user-rating").popover("show")
        }).error(function(jqXHR, textStatus, errorThrown){
            dataLayer.push({
                event:"Valuation",
                'politician.alias':"Error",
                'politician.rating.userVote':errorThrown
            })
        });
    });
    var orgRate;
    $(".widget form.rating.user-rate").hover(
        function(e) {
            var $form = $(this).closest("form");
            orgRate = $(this).find("input[name=rating]:checked").val();
            $form.find("input[name=rating]").removeAttr("checked");
        },
        function(e) {
            var $form = $(this).closest("form");
            console.log("out");
            var newRate = $(this).find("input[name=rating]:checked").val();
            if (newRate == undefined && orgRate != undefined) {
                $form.find("input[name=rating][value=" + orgRate + "]").attr('checked', true);
                $form.find("input[name=rating][value=" + orgRate + "]").prop("checked", true);
            }
        }
    );
    $(".widget form.rating").on("click", "fieldset.rating input",function(e) {
        var $form = $(this).closest("form");
        var url = $form.attr("action");
        var rate = $(this).val();

        $.ajax({
            url: url,
            data: {rate: rate}
        }).done(function (data) {
            $("#rating-social-share-modal-" + data.userId).modal("show");
            var $visisbleForm = $form.siblings("form");
            var newRate = Math.floor(data.userReputation + 0.5);
            $visisbleForm.find("input[name=rating]").removeAttr("checked");
            $visisbleForm.find("input[name=rating][value=" + newRate + "]").attr('checked', true);
            $visisbleForm.find("input[name=rating][value=" + newRate + "]").prop("checked", true);
            dataLayer.push({
                event:"Valuation",
                'politician.alias':data.userAlias,
                'politician.rating.userVote':rate
            })
        })
    })

    $(window).load(function() {
        noLoggedRememberPasswordCallbacks.helper.init();
    })
});

// ***** End jQuey Init *********** //
function prepareProgressBar(){
    $('.progress-bar').progressbar();
    // animo la progress-bar de boxes.likes
//    $('.likes .progress-bar').progressbar({
//        done: function() {
//            var posTooltip = $('.progress-bar').width();
//            $('#m-callback-done').css('left', posTooltip).css('opacity', '1');
//            $('#m-callback-done > .likesCounter').html($('.likes .progress-bar').attr("aria-valuenow"))
//        }
//    });
}

// funciones que llaman a las diferentes notificacones (salen en la parte superior de la pantalla)
var display = {
    error:function(text){this._notyGeneric(text, "error", "top")},
    success:function(text){this._notyGeneric(text, "success", "top")},
    info:function(text){this._notyGeneric(text, "information", "top")},
    warn:function(text){this._notyGeneric(text, "warning", "top")},
    cookie:function(text){this._notyGeneric(text, "warning", "bottomRight")},
    blockAdvise:function(text, eventClick){this._slideDown(text, eventClick)},
    hideAdvise:function(){this._slideUp()},

    _notyGeneric:function(text, type, notyLayout) {
        var htmlText = $.parseHTML(text);
        var nW = noty({
            layout: notyLayout,
            dismissQueue: true,
            animation: {
                open: {height: 'toggle'},
                close: {height: 'toggle'},
                easing: 'swing',
                speed: 500 // opening & closing animation speed
            },
            timeout: 10000,
            template: '<div class="noty_message" role="alert"><span class="noty_text"></span><div class="noty_close"></div></div>',
            type: type,
            text: htmlText
        });
    },
    _slideDown:function(text, clickEvent){
        var clickEventFunction = typeof clickEvent !== 'undefined'?clickEvent : function (e){e.preventDefault(); console.log(e)};
        $("#header nav.navbar a.header-msg").remove();
        var template = "<a class='header-msg' style='display:none' href=''><h6 class='text-center'>"+text+"</h6></a>";
        $("#header nav.navbar").prepend(template);
        $("#header nav.navbar a.header-msg").slideDown();
        $("#header nav.navbar a.header-msg").on("click", clickEventFunction);
        $("div.row.main").animate({ marginTop: '36px'}, 500);
    },

    _slideUp:function(){
        $("#header nav.navbar a.header-msg").slideUp();
        $("div.row.main").animate({ marginTop: '0px'}, 500);
    }
};


function relaodAllDynamicDivs(){
    if ($(".reload").length){
        $(".reload").each(function(){
            reloadDynamicDiv($(this))
        })
    }
}

function reloadDynamicDiv($div){
    var link = $div.attr("data-link");
    var loadingHtml = '<div class="loading xs"><span class="sr-only">Cargando...</span></div>';
    $div.html(loadingHtml);
    $.get( link)
        .done(function(data) {
            $div.html(data)
        });
}

var noLoggedCallbacks = {
    reloadPage : function(){
        pageLoadingOn("Reloading page");
        document.location.reload();
    }
};

var noLoggedRememberPasswordCallbacks = {
    helper:{
        callbackFunctionKey:'noLoggedRememberPasswordCallbackFunctionName',
        cookieNameUrlAfterRememberPassword:'urlAfterRememberPassword',
        init:function(){
            var hash = window.location.hash
            if (hash == "#recoverStatus"){
                var functionName = localStorage.getItem(noLoggedRememberPasswordCallbacks.helper.callbackFunctionKey);
                functionName = functionName || "doNothing";
                noLoggedRememberPasswordCallbacks[functionName].recoverState();
                noLoggedRememberPasswordCallbacks.helper.restoreDefaultStatus();
            }
        },
        restoreDefaultStatus:function(){
            // REMOVE ALL LOCALSTORAGE
            for ( var i = 0, len = localStorage.length; i < len; ++i ) {
                localStorage.removeItem( localStorage.key( i ) ) ;
            }
            localStorage.setItem(noLoggedRememberPasswordCallbacks.helper.callbackFunctionKey, "doNothing");
            cookiesHelper.removeCookie(noLoggedRememberPasswordCallbacks.helper.cookieNameUrlAfterRememberPassword);
        },
        saveItem:function(key, value){
            localStorage.setItem(key,value);
        },
        restoreItem:function(key){
            var value = localStorage.getItem(key);
            localStorage.removeItem(key);
            return value;
        },
        saveCurrentUrlForRememberPasswordOnCookie:function(){
            var url =
                window.location.protocol+
                window.location.port+
                '//' +
                window.location.hostname +
                window.location.pathname;
            url = encodeURIComponent(url);
            cookiesHelper.setCookie(noLoggedRememberPasswordCallbacks.helper.cookieNameUrlAfterRememberPassword, url, 3);
        }
    },
    doNothing : {
        saveState: function(){},
        recoverState: function(){}
    }
}

noLoggedCallbacks["clickFollowAfterLogIn"]=function(){
    console.log("follow after login");

    var buttonFollowUUID = $('#registro').find("form").attr("buttonId")
    var $button = $("#"+buttonFollowUUID);
    var url = followActions.getFollowUrl($button)
    followActions.ajaxFollow(url, $button, noLoggedCallbacks.reloadPage,noLoggedCallbacks.reloadPage);
}

var followActions = {
    ajaxFollow: function(url, buttonFollow, doneFunction, handle403Status){
        $.ajax( {
            url:url,
            statusCode: {
                401: function() {
                    display.info("Estás deslogado");
                    setTimeout('location.reload()',5000);
                },
                403: function(data){
                    var message = buttonFollow.attr('data-message-follow');
                    var userId = followActions.getUserId(buttonFollow)
                    $("button.follow[data-userId="+userId+"]").html(message).removeClass('disabled').addClass('enabled');
                    if (window.notMailConfirmedWarn != undefined){
                        // This function is only created when the page is loaded and the user has not the mailconfirmed
                        notMailConfirmedWarn();
                    }
                    if (handle403Status != undefined){
                        handle403Status();
                    }
                }
            },
            beforeSend: function(){
                buttonFollow.html('<div class="loading xs"><span class="sr-only">Cargando...</span></div>')
            },
            complete:function(){
                // console.log("end")
            }
        }).done(function(data, status, xhr) {
            doneFunction(data,status,xhr)
        })
    },
    getFollowUrl:function($button){
        return $button.attr("data-ajaxfollowurl");
    },
    getUnFollowUrl:function($button){
        return $button.attr("data-ajaxunfollowurl");
    },
    getUserId:function($button){
        return $button.attr('data-userId');
    },
    isAlreadyFollowing: function($button){
        return $button.hasClass('disabled')
    }
}