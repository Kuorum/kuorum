$(document).ready(function() {

    prepareArrowClucks();
    setTimeout(prepareProgressBar, 500)
    prepareProgressBar();

    // para los checkbox del formulario de registro
    var texts= {
        0: i18n.customRegister.step4.form.submit.description0,
        1: i18n.customRegister.step4.form.submit.description1,
        2: i18n.customRegister.step4.form.submit.description2,
        ok:i18n.customRegister.step4.form.submit.descriptionOk
    }

    function changeDescriptionNumSelect(){
        var numChecked = $("#sign4 input[type=checkbox]:checked").length
        if (numChecked < 3){
            $("#descNumSelect").html(texts[numChecked])
            $("#sign4 input[type=submit]").addClass('disabled')
        }else{
            $("#descNumSelect").html(texts['ok'])
            $("#sign4 input[type=submit]").removeClass('disabled')
        }
    }

    $("#brand.disabled").on('click', function(e){e.preventDefault();})
    // links kakareo, impulsar
    $('body').on('click', '.action.cluck', function(e) {
        e.preventDefault();
        e.stopPropagation();
        if (!$(this).hasClass('disabled')){
            var url = $(this).attr("href");
            var postId = $(this).parents("article").first().attr("data-cluck-postId");
            var html = $("article[data-cluck-postId='"+postId+"'] li.kakareo-number").html()
            $.ajax({
                url:url,
                beforeSend:function(xhr){
                    $("article[data-cluck-postId='"+postId+"'] li.kakareo-number").html('<div class="loading xs"><span class="sr-only">Cargando...</span></div>')
                }
            }).done(function(data){
                $("article[data-cluck-postId='"+postId+"'] li.kakareo-number").html(html)
                $("article[data-cluck-postId='"+postId+"'] li.kakareo-number .action").addClass('disabled');
                $("article[data-cluck-postId='"+postId+"'] li.kakareo-number .counter").each(function(idx, element){
                    var numKakareos = parseInt($(element).text()) +1;
                    $(element).text(numKakareos);
                });
            });
        }
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
        e.preventDefault()
        e.stopPropagation()
        clickedButtonFollow($(this))
    });
    function clickedButtonFollow(button){
        var buttonFollow= $(button)
        if (buttonFollow.hasClass('noLogged')){
            $('#registro').modal('show');
        }
        else if ( buttonFollow.hasClass('disabled') ){
            var url = buttonFollow.attr("data-ajaxunfollowurl")
            ajaxFollow(url, buttonFollow, function(data, status, xhr) {
                var message = buttonFollow.attr('data-message-follow');
                var userId = buttonFollow.attr('data-userId');
                $("button.follow[data-userId="+userId+"]").html(message).removeClass('disabled').addClass('enabled');
            });
        } else {
            var url = buttonFollow.attr("data-ajaxfollowurl")
            ajaxFollow(url, buttonFollow, function(data, status, xhr) {
                var message = buttonFollow.attr('data-message-unfollow');
                var userId = buttonFollow.attr('data-userId');
                $("button.follow[data-userId="+userId+"]").html(message).removeClass('enabled').addClass('disabled');
                $("#user-list-followers-"+userId).fadeOut('slow').remove()
            });
        }
    }

    function clickedButtonContact(button){
        var buttonContact= $(button)
        $('#contact-modal').modal('show');
    }

    $('body').on("click", ".btn.contact", function(e) {
        e.preventDefault()
        e.stopPropagation()
        clickedButtonContact($(this))
    });

    function ajaxFollow(url, buttonFollow, doneFunction){
        $.ajax( {
            url:url,
            statusCode: {
                401: function() {
                    display.info("Estás deslogado")
                    setTimeout('location.reload()',5000);
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
    }

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


	// Deshabilitar enlaces números (descubre)
	$('.introDiscover .steps .active').find('a').addClass('disabled');
	$('body').on("click", ".introDiscover .steps .active a", function(e) {
		e.preventDefault();
	});

    // Deshabilitar botón defender (Post)
    $('body').on("click", "#driveDefend .btn", function(e) {
        e.preventDefault();
        var anonymous = $("#drive :input").is(":checked")
        var url = $(this).attr("href")
        var postId = $(this).attr("data-postId")
        //votePost(url, postId, anonymous)
    });

    // Deshabilitar botón defender (Post)
    $('body').on("click", "#drive-noLogged .btn", function(e) {
        e.preventDefault();
        $("#drive-noLogged").submit();
    });

    // Deshabilitar botón Impulsar (Post)
    $('body').on("click", "#drive .btn", function(e) {
        e.preventDefault();
        var anonymous = $("#drive :input").is(":checked")
        var url = $(this).attr("href")
        var postId = $(this).attr("data-postId")
        votePost(url, postId, anonymous)
    });


    $('body').on("click", ".voting ul.noLoggedVoteDiv li a", function(e) {
        e.preventDefault();
        e.stopPropagation();
        $('#registro').modal('show');
    })

    $('body').on("click", ".voting li a.ajaxVote", function(e) {
        e.preventDefault();
        e.stopPropagation();
        var projectId = $(this).attr("data-projectId")
        var votingDiv = $(this).parents(".voting");
        var iconsHtml = votingDiv.html()
        votingDiv.html('<div class="loading"><span class="sr-only">Cargando...</span></div>')
        $.ajax( {
            url:$(this).attr("href"),
            statusCode: {
                401: function() {
                    display.info("Estás deslogado")
                    setTimeout('location.reload()',5000);
                }
            }
        }).done(function(data, status, xhr) {
            votingDiv.html(iconsHtml);
            $('.voting[data-projectid='+projectId+'] ul li a').removeClass("active")
            $('.voting[data-projectid='+projectId+'] ul li.'+data.voteType+' a').addClass("active")
            if (data.newVote){
                karma.open(data.gamification)
            }
        })
});

	$('body').on("click", ".voting li .yes", function(e) {
        var lawId = $(this).parents("section").attr("data-lawId")
		$('section[data-lawId='+lawId+'] .activity .favor').addClass('active');
	});
	$('body').on("click", ".voting li .no", function(e) {
        var lawId = $(this).parents("section").attr("data-lawId")
        $('section[data-lawId='+lawId+'] .activity .contra').addClass('active');
	});
	$('body').on("click", ".voting li .neutral", function(e) {
        var lawId = $(this).parents("section").attr("data-lawId")
        $('section[data-lawId='+lawId+'] .activity .abstencion').addClass('active');
	});

    $(".noMailConfirmedVoteDiv").on("click", function(e){
        e.preventDefault()
        //messageError has been defined on layout recovering data from flash.error
        display.warn(i18n.showMailConfirm)
    });


	// Si hago click en cambio de opinión vuelven los botones
	$('body').on("click", ".changeOpinion", function(e) {
		e.preventDefault();
        var lawId = $(this).parents("section").attr("data-lawId")
		$('section[data-lawId='+lawId+'] .activity li').removeClass('active');
		$(this).css('display', 'none');
		$('section[data-lawId='+lawId+']  .voting ul').css('display', 'block');
	});

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
        console.log("kk")
        $("#searchFilters input[type=checkbox]").prop('checked', false);

        $("#searchFilters input[value="+$(this).val()+"]").prop('checked', true);
        reloadSearchNewFilters()
    });

    function reloadSearchNewFilters(){
        var form = $("#searchFilters")
        form.submit();
//        var elementToUpdate=$("#"+form.attr('data-updateElementId'))
//        $.ajax( {
//            url:form.attr("action"),
//            data:form.serialize()
//        }).done(function(data, status, xhr) {
//            $(elementToUpdate).html(data)
//        })

    }

    $("#filter-menu ul.dropdown-menu li a").on('click', function(e){
        e.preventDefault();
        var element = $(this)
        var field = element.attr("data-forminput")
        var value = element.attr("data-value")
        $("#discover-project-form input[name="+field+"]").val(value)
        $(this).parent("button").html(element.html())
        $("#discover-project-form").submit()
    })

	// el enlace callMobile (visible sólo en pantallas de hasta 767px, desaparece si le haces click o si llegas a la votación
	if ( $('#vote').length > 0 ) {

		$(function() {
			var callMobile = $('.callMobile');
			var eTop = $('#vote').offset().top;
			var realPos = eTop - 80;
			$(window).scroll(function() {
				var scroll = $(window).scrollTop();
				if (scroll >= realPos) {
					callMobile.fadeOut('slow');
				} else {
					callMobile.fadeIn('slow');
				}
			});
		});

	}

	// si boxes lleva foto pongo padding superior
	if ( $('.boxes.noted.likes.important').children('img.actor').length > 0 ) {
		$('.boxes.noted.likes.important').css('padding-top', '275px');

	}


	// oculta los comentarios
//	$('.listComments > li:gt(2)').hide();
	$('#ver-mas a').click(function(e) {
		e.preventDefault();
//		$('.listComments > li:gt(2)').fadeIn('slow');
        openComments()
	});
    $("#comment").focus(function(e){
        openComments();
        $("html, body").animate({ scrollTop: $(this).parents(".listComments").offset().top -100}, 1000);
    });
    $("#addComment").on('submit', function(e){
        e.preventDefault()
        var form = $(this)
        if (form.valid()){
            var parentId = form.attr('data-parent-id')
            var parent = $("#"+parentId)
            var url = form.attr('action')
            $.ajax( {
                url:url,
                data:$(this).serialize(),
                statusCode: {
                    401: function() {
                        display.warn("Tienes que registrarte para poder añadir comentarios")
                    }
                }
            })
                .done(function(data, status, xhr) {
                    parent.append(data)
                    $('#ver-mas a').click()
                    form[0].reset()
                    if (parent.children().last().prev().length >0){
                        //NO es el primer elemento
                        $("html, body").animate({ scrollTop: parent.children().last().prev().offset().top }, 1000);
                    }
                })
                .fail(function(data) {
                    console.log(data)
                })
                .always(function(data) {

                });
        }else{
            //console.log("pete")
        }
    })

    $("#addDebate").on('submit', function(e){
        e.preventDefault()
        var form = $(this)
        if (form.valid()){
            var url = form.attr('action')
            var ulList = $("ul.chat")
            $.ajax( {
                url:url,
                data:$(this).serialize(),
                statusCode: {
                    401: function() {
                        display.warn("Tienes que registrarte para poder añadir comentarios")
                    }
                }
            })
                .done(function(data, status, xhr) {
                    ulList.find(">li:last-child").before(data)
                    form.each (function(){  this.reset();});
//                    $("html, body").animate({ scrollTop: parent.children().last().prev().offset().top }, 1000);
                })
                .fail(function(data) {
                    display.warn("Hubo algun error. Intent otra vez o contacte con info@kuorum.org")
                    console.log(data)
                })
                .always(function(data) {

                });
        }else{
            //console.log("pete")
        }
    })


	// change text when select option in the edit post form
	$('#updateText').text($('#typePubli li.active').text());
	$('#selectType').change(function(){
        	$('#updateText').text($('#typePubli li').eq(this.selectedIndex).text());
    });


	// countdown textarea edición propuesta
	$(function() {
		var totalChars      = parseInt($('#charInit span').text());
		var countTextBox    = $('.counted'); // Textarea input box
		var charsCountEl    = $('#charNum span'); // Remaining chars count will be displayed here

		if (countTextBox.length> 0){
			charsCountEl.text(totalChars - countTextBox.val().length); //initial value of countchars element
        }
		countTextBox.keyup(function() { //user releases a key on the keyboard

			var thisChars = this.value.replace(/{.*}/g, '').length; //get chars count in textarea

			if (thisChars > totalChars) //if we have more chars than it should be
			{
				var CharsToDel = (thisChars-totalChars); // total extra chars to delete
				this.value = this.value.substring(0,this.value.length-CharsToDel); //remove excess chars from textarea
			} else {
				charsCountEl.text( totalChars - thisChars ); //count remaining chars
			}
		});
	});

    $(function() {
        var totalChars      = parseInt($('#charInitTit span').text());
        var countTextBox    = $('#title-project.counted');
        var charsCountEl    = $('#charNumTit span');

        if (countTextBox.length> 0){
            charsCountEl.text(totalChars - countTextBox.val().length);
        }
        countTextBox.keyup(function() {

            var thisChars = this.value.replace(/{.*}/g, '').length;

            if (thisChars > totalChars)
            {
                var CharsToDel = (thisChars-totalChars);
                this.value = this.value.substring(0,this.value.length-CharsToDel);
            } else {
                charsCountEl.text( totalChars - thisChars );
            }
        });
    });

    $('.input-group.date').datepicker({

        language: "es",
        autoclose: true,
        todayHighlight: true

    });

	// hacer visible la contraseña
	$('#show-pass').attr('checked', false);

	$('#show-pass').click(function(){

		if ($(this).hasClass('checked')) {
			$(this).removeClass('checked');
		} else {
			$(this).addClass('checked');
		}

	    name = $('#password').attr('name');
	    value = $('#password').val();

	    if($(this).hasClass('checked')) {
	    	html = '<input type="text" name="'+ name + '" value="' + value + '" id="password" class="form-control input-lg">';
	        $('#password').after(html).remove();
	    } else {
	    	html = '<input type="password" name="'+ name + '" value="' + value + '" id="password" class="form-control input-lg">';
	    	$('#password').after(html).remove();
	    }
	});

    function prepareFormUsingGender(userType){
        if (userType == "ORGANIZATION"){
            $(".userData").hide()
            $(".politicianData").hide()
            $(".organizationData").show()
        }else if (userType == "POLITICIAN"){
            $(".userData").show()
            $(".politicianData").show()
            $(".organizationData").hide()
        }else{
            $(".userData").show()
            $(".politicianData").hide()
            $(".organizationData").hide()
        }

    }
    $("input[name=userType], input[name=gender]").on("change", function(e){
        prepareFormUsingGender($(this).val())
    })

    if ($("input[name=userType]:checked").val() != undefined){
        prepareFormUsingGender($("input[name=userType]:checked").val())
    }else{
        prepareFormUsingGender("PERSON")
    }

    if ($("input[name=gender]:checked").val() != undefined){
        prepareFormUsingGender($("input[name=gender]:checked").val())
    }else{
        prepareFormUsingGender("FEMALE")
    }

	// para los checkbox del formulario de registro
	var texts= {
        0: i18n.customRegister.step4.form.submit.description0,
        1: i18n.customRegister.step4.form.submit.description1,
        2: i18n.customRegister.step4.form.submit.description2,
        ok:i18n.customRegister.step4.form.submit.descriptionOk
    }

    function changeDescriptionNumSelect(){
        var numChecked = $("#sign4 input[type=checkbox]:checked").length
        if (numChecked < 3){
            $("#descNumSelect").html(texts[numChecked])
            $("#sign4 input[type=submit]").addClass('disabled')
        }else{
            $("#descNumSelect").html(texts['ok'])
            $("#sign4 input[type=submit]").removeClass('disabled')
        }
    }


	// seleccionar todos los checkbox
	$(function () {

        changeDescriptionNumSelect()
	    var checkAll = $('#selectAll');
	    var checkboxes = $('input.check');

        $('input.check').each(function(){
            var self = $(this),
            label = self.next(),
            label_text = label.html();
            label.remove();
            self.iCheck({
              checkboxClass: 'icheckbox_line-orange',
              radioClass: 'iradio_line-orange',
              inheritID: true,
              aria: true,
              insert:  label_text
            });
        });

	    $('#selectAll').change(function() {
		    if($(this).is(':checked')) {
		        checkboxes.iCheck('check');
		        $('#others').prop('checked', true);
		    } else {
		        checkboxes.iCheck('uncheck');
		        $('#others').prop('checked', false);
		    }
		});

	    checkAll.on('ifChecked ifUnchecked', function(event) {
	        if (event.type == 'ifChecked') {
	            checkboxes.iCheck('check');
	        } else {
	            checkboxes.iCheck('uncheck');
	        }
	    });

	    checkboxes.on('ifChanged', function(event){
	        if(checkboxes.filter(':checked').length == checkboxes.length) {
	            checkAll.prop('checked', 'checked');
	        } else {
	            checkAll.removeProp('checked');
	        }
	        checkAll.iCheck('update');
            changeDescriptionNumSelect();
	    });
	});


	// seleccionar todos los checkbox en configuración
	$(function () {
        $('.allActivityMails').change(function() {
            var formGroup = $(this).parents(".form-group")
            if($(this).is(':checked')) {
                formGroup.find('.checkbox input').prop('checked', true);
            } else {
                formGroup.find('.checkbox input').prop('checked', false);
            }
        });
	});


	// le da la clase error al falso textarea
	$(function () {
		if ( $('#textPost').hasClass('error') ) {
		    $('#textPost').closest('.jqte').addClass('error');
		}

        if ( $('#description').hasClass('error') ) {
            $('#description').closest('.jqte').addClass('error');
        }
	});


    $('.ajax.popover-trigger.more-users').on('shown.bs.popover', function () {
        var that = $(this)
        var content = $(this).next().children(".popover-content")
        var ajaxUrl = content.children("a").attr("href")
        var ulUserLists = content.find("ul")
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
    })
    $('.roleButton').on("click", function(e){
        e.preventDefault()
        var link = $(this)
        var url = link.attr('href')
        ajaxFollow(url,link, function(data){
            $(".roleButton.active").removeClass("active").html("Activar")
            link.addClass("btn-green active")
            link.html(i18n.profile.kuorumStore.roleButton.active)
            $("#numEggs").html(data.numEggs)
            $("#numCorns").html(data.numCorns)
            $("#numPlumes").html(data.numPlumes)
        })
    })
    $('.skillButton').on("click", function(e){
        e.preventDefault()
        var link = $(this)
        var url = link.attr('href')
        ajaxFollow(url,link, function(data){
            link.addClass("active")
            link.html(i18n.profile.kuorumStore.skillButton.active)
            $("#numEggs").html(data.numEggs)
            $("#numCorns").html(data.numCorns)
            $("#numPlumes").html(data.numPlumes)
        })
    })

    $("#deleteAccountForm a").on("click", function(e){
        e.preventDefault()
        $("#deleteAccountForm input[name=forever]").val("true")
        $("#deleteAccountForm").submit()
    })
    $("#deleteAccountForm button").on("click", function(e){
        e.preventDefault()
        $("#deleteAccountForm input[name=forever]").val("false")
        $("#deleteAccountForm").submit()
    })

    $(".multimedia .groupRadio input[type=radio]").on('click', function(e){
        var multimediaType = $(this).val()
        $('[data-multimedia-switch="on"]').hide()
        $('[data-multimedia-type="'+multimediaType+'"]').show()

    })

    $('body').on("click", ".openModalVictory",function(e){
        var notificationId = $(this).attr("data-notification-id")
        modalVictory.openModal(notificationId)
    });

    $('body').on("click", ".openModalDefender",function(e){
        e.preventDefault();
        e.stopPropagation();
        var postId = $(this).attr("data-postId")
        $("#apadrinar").find("input[name=postId]").val(postId);
        $('#apadrina-propuesta').modal('show');
    });

    $('.modalVictoryClose').on("click", function (e) {
        e.preventDefault()
        $('#modalVictory').modal('hide');
    });

    $('.modalVictoryAction').on("click", function (e) {
        e.preventDefault()
        var notificationId = $(this).attr("data-notificationId")
        var victoryOk = $(this).attr("data-victoryOk")
        var link = $(this).attr("href")
        $.ajax({
            url:link,
            data:{victoryOk:victoryOk}
        }).done(function(data){
            $('#modalVictory').modal('hide');
            modalVictory.hideNotificationActions(notificationId);
            display.success(data)
        })
    });

    $('body').on("click",".votePostCommentLink", function(e){
        e.preventDefault()
        var element = $(this)
        if (!element.hasClass("disabled")){
            var url = $(this).attr('href')
            $.ajax(url).done(function(data){
                element.parent().siblings().children().addClass("disabled");
                element.addClass("disabled");
                var q = parseInt(element.siblings('span').html(),10)
                element.siblings('span').html(q+1)
            });
        }
    });

    $("#projectChangePostTypeButtonDiv a").click(function(e){
        e.preventDefault()
        e.stopPropagation()
        $("#projectChangePostTypeButtonDiv a").removeClass("active")
        $(this).addClass("active")
        var showDivId = $(this).attr("data-showDivId")
        $("div[data-name=listClucks]").addClass("hidden")
        $("#"+showDivId).removeClass("hidden")
    });

    cookiesHelper.displayCookiesPolitics();

    $(".dynamicList").dynamiclist();

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

    function preapreReadMore(){
        $('.limit-height').each(function (idx){
            var collapsedHeight = parseInt($(this).attr("data-collapsedHeight"));
            var buttonCss = "btn btn-xs btn-blue"
            if ($(this)[0].hasAttribute("data-collapsedButtonCss")){
                buttonCss = $(this).attr("data-collapsedButtonCss")
            }
            if (typeof buttonCss === typeof undefined && buttonCss === false){
                buttonCss = "btn btn-xs btn-blue"
            }
            if ($(this).height() > collapsedHeight){
                $(this).readmore({
                    speed: 500,
                    collapsedHeight: collapsedHeight,
                    heightMargin: 16,
                    moreLink: '<div class="center-button"><a href="#" class="'+buttonCss+'">'+i18n.read.more+' <span class="fa fa-angle-down"></a></div>',
                    lessLink: '<div class="center-button"><a href="#" class="'+buttonCss+'">'+i18n.read.less+' <span class="fa fa-angle-up"></a></div>',
                    embedCSS: true,
                    blockCSS: 'display: block; width: auto;',
                    startOpen: false,
    // callbacks
                    beforeToggle: function(){},
                    afterToggle: function(){}
                });
            }
        })
    }
    preapreReadMore();

    // load more
    $("a.loadMore").on("click", function(e){loadMore(e, this)})

    function loadMore(e, that) {

        e.preventDefault()
        var link = $(that)
        var url = link.attr('href')
        var formId = link.attr('data-form-id')
        var paramAppender = "?";
        if (url.indexOf("?")>-1){
            paramAppender = "&"
        }
        var max = $.parseJSON($("#"+formId).find('input[name=max]').val() || 10)
        var offset = $.parseJSON(link.attr('data-offset') || max ) //Para que sea un integer
        url += paramAppender+"offset="+offset+"&"+$('#'+formId).serialize()
        var parentId = link.attr('data-parent-id')
        var loadingId = parentId+"-loading"
        var parent = $("#"+parentId)
        parent.append('<div class="loading" id="'+loadingId+'"><span class="sr-only">Cargando...</span></div>')
        $.ajax( {
            url:url,
            statusCode: {
                401: function() {
                    location.reload();
                }
            }
        })
            .done(function(data, status, xhr) {
                parent.append(data)
                var moreResults = $.parseJSON(xhr.getResponseHeader('moreResults')) //Para que sea un bool
                link.attr('data-offset', offset +max)
                if (moreResults){
                    link.remove()
                }
            })
            .fail(function(data) {
                console.log(data)
            })
            .always(function(data) {
                $("#"+loadingId).remove()
                $("time.timeago").timeago();
            });
    }
    youtubeHelper.replaceNonExistImage();


    $(".input-region").autocomplete({
        paramName:"word",
        params:{country:""},
        serviceUrl:urls.suggestRegion,
        minChars:3,
        width:330,
        noCache: false, //default is false, set to true to disable caching
        onSearchStart: function (query) {
//            var realInputId = $(this).attr('data-real-input-id');
//            realInputId = realInputId.replace(".", "\\.")
//            $("#"+realInputId).val("");
        },
        onSearchComplete: function (query, suggestions) {
            $('.loadingSearch').hide()
        },
        formatResult:function (suggestion, currentValue) {
            var format = ""
            if (suggestion.type=="NATION"){
                //AD ICONS DEPENDING ONT REGION TYPE || EXAMPLE COMMENTED
//                format = "<img class='user-img' alt='"+suggestion.data.name+"' src='"+suggestion.data.urlAvatar+"'>"
//                format +="<span class='name'>"+suggestion.data.name+"</span>"
//                format +="<span class='user-type'>"+suggestion.data.role.i18n+"</span>"
            }else{
                format =  suggestion.value
            }
            return format
        },
        searchUserText:function(userText){
            $(this).val(userText)
        },
        onSelect: function(suggestion){
            if(suggestion.type=="NATION"){
                console.log("NATION")
            }
            $(this).val(suggestion.data.name)
            var realInputId = $(this).attr('data-real-input-id');
            realInputId = realInputId.replace(".", "\\.")
            $("#"+realInputId).val(suggestion.data.iso3166_2);
            $("#"+realInputId).valid()
        },
        triggerSelectOnValidInput:false,
        deferRequestBy: 100 //miliseconds
    }).focusout(function(e){
        var realInputId = $(this).attr('data-real-input-id');
        realInputId = realInputId.replace(".", "\\.")
        if ($(this).val().length ===0){
            $("#"+realInputId).val("")
        }
        $("#"+realInputId).valid()
    }).keypress(function(e){
        var realInputId = $(this).attr('data-real-input-id');
        realInputId = realInputId.replace(".", "\\.")
        $("#"+realInputId).val("")
    });


    /*****************
     * Delayed modules
     *****************/
    $(".delayed").each(function(){
        reloadDynamicDiv($(this))
    })

    /*******************************
     *********** CAUSES ************
     *******************************/
    // SUPPORT DASHBOARD CARDS CAUSES
    $("body").on("click", ".causes-list .cause-support", function(e){
        e.preventDefault();
        if ($(this).hasClass("disabled")){
            //The cause card is disappearing and will be removed
        }else{
            $(this).addClass("disabled")
            var $liCause = $(this).closest("li")
            var appendUl = $(this).parents("ul.causes-list");
            clickSupportCause($(this), function(){
                addNewCauseToList(appendUl, $liCause);
            })
        }
    })

    // SUPPORT CAUSES SMALL
    $("body").on("click", ".causes-tags .cause-support", function(e){
        e.preventDefault();
        e.stopPropagation();
        var $parent = $(this).parents(".cause")
        if ( $parent.hasClass("noLogged")){
            $('#registro').modal('show');
        }else{
            $a = $(this).find("a")
            clickSupportCause($a, function(){})
        }
    })

    function clickSupportCause($a, actionAfterSupport){
        hearBeat(2,  $a.find(".fa"));
        $.get(  $a.attr('href'), function( data ) {
            var citizenSupports = data.cause.citizenSupports
            var $parent = $a.parents(".cause");
            $parent.toggleClass("active");
            $parent.find(".cause-counter").html(citizenSupports);

            if($("#user-logged-leaning-index-panel-id").length){
                var barWidth= data.leaningIndex.liberalIndex+'%';
                $("#user-logged-leaning-index-panel-id").find(".progress-bar").css('width',barWidth);
                $("#user-logged-leaning-index-panel-id").find(".tooltip").css('left',barWidth);
                $("#user-logged-leaning-index-panel-id").find(".tooltip-inner").html(barWidth);
            }
            relaodAllDynamicDivs()
            actionAfterSupport()
        });
    }

    function hearBeat(numHeartBeats, $element){
        if (numHeartBeats <0){
            return;
        }
        var back = numHeartBeats % 2 == 0
        $element.animate(
            {
                'font-size': (back) ? '14px' : '20px',
                'opacity': (back) ? 1 : 0.5
            }, 100, function(){hearBeat(numHeartBeats -1, $element)});
    }

    // botón de cierre de las causas del dashboard
    // DISCARD CAUSE DASHBOARD
    if ($('.causes-list').length) {
        $(".causes-list").on("click","li article a.close", function(e){
            e.preventDefault();
            e.stopPropagation();
            var link = $(this).attr("href");
            var $liCause = $(this).closest('li')
            $.get( link)
                .done(function(data) {
                    var appendUl = $liCause.parents("ul.causes-list");
                    addNewCauseToList(appendUl, $liCause);
                });
        });
    }

    function addNewCauseToList($ul, $li){
        var offset = $ul.next().children("a.loadMore").attr("data-offset");
        var linkNewCause = $ul.next().children("a.loadMore").attr("href");
        $.get(linkNewCause, { offset: offset, max:1})
            .done(function(data){
//                $ul.append(data)
                var newCause = $(data).hide();
                console.log(newCause)
                $li.children('article').fadeOut('fast', function(){
                    newCause.unwrap();
                    $li.replaceWith(newCause)
                    newCause.fadeIn('fast')
                });
            })
    }

    /*******************************************/
    /******* USER RATES ************************/
    /*******************************************/
    $("#user-rating-form input[type=radio]").on("click", function(e){

        $("#rating-social-share-modal").modal("show")
    })

//    $("form.submitOrangeButton input.form-control").on('keyup paste',function(){
//        console.log("change")
//        var submitButtons = $(this).parents("form").find("input[type=submit]");
//        submitButtons.removeClass("btn-grey");
//        submitButtons.removeClass("disabled");
//        submitButtons.addClass("btn-orange");
//    })
//    $("form.submitOrangeButton input[type=submit]").addClass("disabled")

});

// ***** End jQuey Init *********** //

//
//var modalDefend = {
//    data:{},
//    openModal:function(postId){
//        var modalData = this.data['post_'+postId]
//        $("#modalDefenderPolitician img").attr('src',modalData.defender.imageUrl)
//        $("#modalDefenderPolitician img").attr('alt',modalData.defender.name)
//        $("#modalDefenderPolitician #sponsorLabel").html(modalData.post.sponsorLabel)
//        $("#modalDefenderPolitician h1").html(modalData.defender.name)
//        $("#modalDefenderOwner img").attr('src',modalData.owner.imageUrl)
//        $("#modalDefenderOwner img").attr('alt',modalData.owner.name)
//        $("#modalDefenderOwner .name").html(modalData.owner.name)
//        $("#modalDefenderOwner .what").html(modalData.post.what)
//        $("#modalDefenderOwner .action span").html(modalData.post.numVotes)
//        $("#modalSponsor .modal-body").children("p").html(modalData.post.description)
//        $("#modalSponsor .modal-body").children("div").each(function(i,buttonElement){
//            var dataButton = modalData.post.options[i]
//            $(buttonElement).children("a").html(dataButton.textButton)
//            $(buttonElement).children("a").attr('href',dataButton.defendLink)
//            $(buttonElement).children("p").html(dataButton.textDescription)
//        })
//    }
//}

var modalVictory = {
    data:{},
    openModal:function(notificationId){
        var modalData = this.data['notification_'+notificationId]
        $("#modalVictoryUser img").attr('src',modalData.user.imageUrl)
        $("#modalVictoryUser img").attr('alt',modalData.user.name)
        $("#modalVictoryDefender img").attr('src',modalData.defender.imageUrl)
        $("#modalVictoryDefender img").attr('alt',modalData.defender.name)
        $("#modalVictoryDefender .name").html(modalData.defender.name)
        $("#modalVictoryDefender .action").html(modalData.post.action)
        $("#modalVictory .modal-body p").first().html(modalData.post.description)
        $("#modalVictory .modal-body p").last().html(modalData.post.lawLink)
        $("#modalVictory .modal-footer a").attr('href',modalData.post.victoryLink)
        $("#modalVictory .modal-footer a").attr('data-notificationId',notificationId)
    },
    hideNotificationActions:function(notificationId){
        $("[data-notification-id="+notificationId+"]").hide();
    }
}

var karma = {
    title:"",
    text:"",

    numEggs:0,
    numPlumes:0,
    numCorns:0,
    //Open es la funcion que abriría el Karma. pero se ha comentado dejando la funcionalidad en realOpen
    open:function(options){},
    //Esta funcion no se llama. Se deja así para un futuro carma
    realOpen:function(options){
        console.log("Karma disabled")
        if (options != undefined){
            this.numEggs = options.eggs || 0
            this.numPlumes = options.plumes || 0
            this.numCorns = options.corns || 0
            this.text = options.text || ""
            this.title = options.title || ""
        }
        this._open()
    },

    close:function(){
        $('#karma').css('display','none').removeClass('in');
    },

    _idLiEggs:"karmaEggs",
    _idLiPlumes:"karmaPlumes",
    _idLiCorn:"karmaCorn",
    _open:function(){
        this._prepareKarma()
        $('#karma').fadeIn().addClass('in');
    },

    _prepareKarma:function(){
        $("#karma h2").html(this.title)
        var motivation = $("#karma p span")
        var motivationText = "<span class='"+motivation.attr('class')+"'>"+motivation.html()+"</span>"
        $("#karma p").html(this.text +"<br>"+motivationText)

        $("ul.karma li").removeClass("active");
        this._prepareIcon(this._idLiEggs, this.numEggs)
        this._prepareIcon(this._idLiPlumes, this.numPlumes)
        this._prepareIcon(this._idLiCorn, this.numCorns)
    },

    _prepareIcon:function(liId, quantity){
        $("#"+liId+" .counter").html("+"+quantity)
        if (quantity > 0) $("#"+liId).addClass("active")
    }
}


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

function prepareArrowClucks(){
// el hover sobre el kakareo que afecte al triángulo superior
    $('.kakareo > .link-wrapper').on({
        mouseenter: function () {
            $(this).prev('.from').find('.inside').css('border-bottom', '8px solid #efefef');
        },
        mouseleave: function () {
            $(this).prev('.from').find('.inside').css('border-bottom', '8px solid #fafafa');
        }
    });

    $('.important .kakareo > .link-wrapper').on({
        mouseenter: function () {
            $(this).prev('.from').find('.inside').css('border-bottom', '8px solid #feedce');
        },
        mouseleave: function () {
            $(this).prev('.from').find('.inside').css('border-bottom', '8px solid #fff8ed');
        }
    });
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
        var htmlText = $.parseHTML(text)
        var nW = noty({
            layout: notyLayout,
            dismissQueue: true,
            animation: {
                open: {height: 'toggle'},
                close: {height: 'toggle'},
                easing: 'swing',
                speed: 500 // opening & closing animation speed
            },
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
        $("#header nav.navbar a.header-msg").on("click",clickEventFunction)
        $("div.row.main").animate({ marginTop: '36px'}, 500);
    },

    _slideUp:function(){
        $("#header nav.navbar a.header-msg").slideUp();
        $("div.row.main").animate({ marginTop: '0px'}, 500);
    }
}

var cookiesHelper = {
    cookieNamePoliticsAccepted: "kuorumCookiesAccepted",
     setCookie:function(cname, cvalue, exdays) {
         var d = new Date();
         d.setTime(d.getTime() + (exdays*24*60*60*1000));
         var expires = "expires="+d.toGMTString();
         var domain = document.domain;
         document.cookie = cname + "=" + cvalue + "; " + expires+ ";domain="+domain+";path=/";
     },
    getCookie:function (cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for(var i=0; i<ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0)==' ') c = c.substring(1);
            if (c.indexOf(name) != -1) return c.substring(name.length,c.length);
        }
        return "";
    },
    checkCookie: function(cname, onCookieFound, onNotCookieFound) {
        var cvalue=this.getCookie(cname);
        if (cvalue!="") {
            onCookieFound(cvalue);
        }else{
            onNotCookieFound(cname);
        }
    },
    displayCookiesPolitics: function (){
        this.checkCookie(
            this.cookieNamePoliticsAccepted,
            function(){},
            function(cName){
                var button = "<button id='acceptCookies' class='btn btn-xs' onclick='cookiesHelper.acceptedCookiesPolitics()'>"+i18n.cookies.accept+"</button>";
                var message = i18n.cookies.message + button;
                display.cookie(message);
            }
        )
    },
    acceptedCookiesPolitics: function(){
        this.setCookie(this.cookieNamePoliticsAccepted, "true", 99999);
    }
}

function votePost(url, postId, anonymous){
    var html = $("article[data-cluck-postId='"+postId+"'] li.like-number").html()
    var htmlDriveButton = $('#drive > a').html()
    var loadingHtml = '<div class="loading xs"><span class="sr-only">Cargando...</span></div>'
    $.ajax({
        url:url,
        data:{postId:postId, anonymous:anonymous},
        beforeSend:function(xhr){
            $("article[data-cluck-postId='"+postId+"'] li.like-number").html(loadingHtml)
            $('#drive > a').html(loadingHtml)
        }
    }).done(function(data){
        var numLikes = data.numLikes
        var limitTo = data.limitTo
        $("article[data-cluck-postId='"+postId+"'] li.like-number").html(html)
        $("article[data-cluck-postId='"+postId+"'] li.like-number .action").addClass('disabled');
        $("article[data-cluck-postId='"+postId+"'] li.like-number .counter").each(function(idx, element){
            numLikes = parseInt($(element).text()) +1;
            $(element).text(numLikes);
        });

        //If Page == Post
        var progressBarNumLikes = $("section.boxes.noted.likes > .likesContainer > div > .likesCounter")
        $('#m-callback-done').css('opacity', '0');
        $('.likes .progress-bar').attr("aria-valuetransitiongoal",numLikes)
        $('.likes .progress-bar').attr("aria-valuenow",numLikes)
        $('.likes .progress-bar').attr("aria-valuemax",limitTo)
        $('#drive > a').html(i18n.post.show.boxes.like.vote.buttonVoted).addClass('disabled');
        $("#drive :input").attr("disabled", true);
        $("#drive div.form-group a").addClass("disabled")
        var alreadyVotedText = $("#drive div.form-group a span").attr("data-textalreadyvoted");
        var span= $("#drive div.form-group a span");
        $("#drive div.form-group a").html(span.prop('outerHTML') + alreadyVotedText)
        prepareProgressBar()
        setTimeout(prepareProgressBar, 500)
//                setTimeout(prepareProgressBar, 1000)

//            console.log(data.gamification)
        karma.open(data.gamification)

    });
}

function openComments(){
    $('.listComments > li').fadeIn('slow');
    $('#ver-mas').hide();
}

function readLater(readLaterElement){
    var url = $(readLaterElement).attr("href");
    var postId = $(readLaterElement).parents("article").first().attr("data-cluck-postId");
    var html = $("article[data-cluck-postId='"+postId+"'] li.read-later").html()
    var loadingHtml = '<div class="loading xs"><span class="sr-only">Cargando...</span></div>'
    $.ajax({
        url:url,
        beforeSend:function(xhr){
            $("article[data-cluck-postId='"+postId+"'] li.read-later").html(loadingHtml)
        }
    }).done(function(data, status, xhr){
        var isFavorite = xhr.getResponseHeader('isFavorite');
        var numFavorites = xhr.getResponseHeader('numList');
        $("article[data-cluck-postId='"+postId+"'] li.read-later").html(html);
        $(".pending h1 .badge").text(numFavorites);
        $("section.boxes.guay.pending").addClass(numFavorites==0 ? "hide" : "");
        $("section.boxes.guay.pending").removeClass(numFavorites!=0 ? "hide" : "");
        $("section.boxes.guay.pending ul.kakareo-list li.text-center").addClass(numFavorites < 6 ? 'hide' : '');
        $("section.boxes.guay.pending ul.kakareo-list li.text-center").removeClass(numFavorites >= 6 ? 'hide' : '');

        if (isFavorite == 'true'){
            $("article[data-cluck-postId='"+postId+"'] li.read-later a").addClass("disabled");
            $("article[data-cluck-postId='"+postId+"'] li.read-later a").removeClass("enabled");
            $("section.boxes.guay.pending ul.kakareo-list").prepend(data);
            $("section.boxes.guay.pending ul.kakareo-list li:first").addClass(numFavorites<=5 ? '' : 'hide');
        }
        else{
            $("article[data-cluck-postId='"+postId+"'] li.read-later a").removeClass("disabled");
            $("article[data-cluck-postId='"+postId+"'] li.read-later a").addClass("enabled");
            $("section.boxes.guay.pending article[data-cluck-postId='"+postId+"']").parent().remove();
            $("section.boxes.guay.pending ul.kakareo-list li.hide:first").removeClass('hide');
        }
    });
}

var youtubeHelper={

    metaTags :["property='og:image'","itemprop=image", "name='twitter:image:src'"],
    replaceNonExistImage:function(){
        for (i = 0; i < this.metaTags.length; i++) {
            var metaTagName = this.metaTags[i];
            var $metaTag = $("meta["+metaTagName+"]");
            var imageUrl = $metaTag.attr("content");
            if (imageUrl!=undefined && imageUrl.indexOf("img.youtube.com/vi/")>=0){
                $("<img data-meta-tag-name=\""+metaTagName+"\"/>")
                    .on('load', function() {
                        //ÑAPA Porque youtube no devuelve error si no una imagen gris
                        if (this.width == 120 && this.height == 90){
                            var $metaTag = $("meta["+$(this).attr("data-meta-tag-name")+"]");
                            var imageUrl = $metaTag.attr("content");
                            var newImageUrl = youtubeHelper.replaceYoutubeImageType(imageUrl, "0.jpg");
                            $metaTag.attr("content",newImageUrl);
                        }
                    }
                )
                    .on('error', function(response) { console.log("error loading image"); })
                    .attr("src", imageUrl)
                ;
            }
        }
    },
    replaceYoutubeImageType:function(completeUrl, newImageName){
        var resultUrl = completeUrl.substring(0, completeUrl.lastIndexOf('/'));
        resultUrl = resultUrl +"/" +newImageName;
        return resultUrl;
    }



}

$(document).ajaxStop(function () {

	// inicia el timeago
	$("time.timeago").timeago();

	prepareArrowClucks();
    preparePopover();

});


function Campaign(id, name, headText,headVotedText,  modalDelay){
    var _id = id;
    var _name = name;
    var _headText = headText;
    var _headVotedText = headVotedText;
    var _cookieElectionName="Election-"+id;
    var _modalDelay=modalDelay;
    var _modalId="#causes-modal";

    var _showModal = function(){
        $(_modalId).modal("show");
    }

    this.showModal = _showModal
    this.hideModal = function(){
        $(_modalId).modal("hide");
    }
    this.preparePageForCampaign = function() {
        cookiesHelper.checkCookie(
            _cookieElectionName,
            function(cookieValue){
                display.blockAdvise(_headVotedText, function(e){
                    e.preventDefault();
                    _showModal()
                });
            },
            function(cookieName){
                display.blockAdvise(_headText, function(e){
                    e.preventDefault();
                    _showModal()
                });
                // Launch the election modal (if exists) after delay seconds
                window.setTimeout(_showModal, _modalDelay);
            }
        );
    };

    this.hideCampaign = function(){
        this.hideModal();
        display.hideAdvise();
    }

    this.notShowCampaignAgain = function(){
        cookiesHelper.setCookie(_cookieElectionName,true, 99999);
    }

    return this;
}

function relaodAllDynamicDivs(){
    if ($(".reload").length){
        $(".reload").each(function(){
            reloadDynamicDiv($(this))
        })
    }
}

function reloadDynamicDiv($div){
    var link = $div.attr("data-link")
    var loadingHtml = '<div class="loading xs"><span class="sr-only">Cargando...</span></div>'
    $div.html(loadingHtml)
    $.get( link)
        .done(function(data) {
            $div.html(data)
        });
}