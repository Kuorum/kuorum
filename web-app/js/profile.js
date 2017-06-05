
function SortCampaigns() {

    var that = this;
    var campaignList = $('.politician-card ul.campaign-list, .dashboard ul.campaign-list');

    this.campaignOptions = {};
    this.campaignOptions['latest'] = {
        sort: function(a, b){
            var aTime = $(a).find('.link-wrapper').attr('data-datepublished');
            var bTime = $(b).find('.link-wrapper').attr('data-datepublished');
            return aTime==undefined ? bTime.localeCompare(aTime):0;
        },
        filter: function (e) {
            $('ul.campaign-list li').removeClass('hide');
            $('ul.campaign-list li').removeClass('last-odd');
            return false;
        },
        name: 'latest'
    };

    this.campaignOptions['posts'] = {
        sort:function(a, b){
            var aTime = $(a).find('.link-wrapper').attr('data-datepublished');
            var bTime = $(b).find('.link-wrapper').attr('data-datepublished');
            return bTime.localeCompare(aTime);
        },
        filter:function(idx){
            var articleId = $(this).find(".link-wrapper").attr('id');
            if(articleId.indexOf("post")!=0){
                $(this).addClass('hide');
                return articleId;
            }
            else{
                $(this).removeClass('hide');
            }
        },
        name:"posts"
    };

    this.campaignOptions['debates'] = {
        sort:function(a, b){
            var aTime = $(a).find('.link-wrapper').attr('data-datepublished');
            var bTime = $(b).find('.link-wrapper').attr('data-datepublished');
            return bTime.localeCompare(aTime);
        },
        filter:function(idx){

            var articleId = $(this).find(".link-wrapper").attr('id');
            if(articleId.indexOf("debate")!=0) {
                $(this).addClass('hide');
                return articleId;
            }
            else{
                $(this).removeClass('hide');
            }
            // hide al li
        },
        name:"debates"
    };

    var campaignOption = that.campaignOptions.latest;

    this.setCampaignOption = function(option){
        var opt = that.campaignOptions[option];
        if (opt == undefined){
            opt = that.campaignOptions.latest
        }
        campaignOption = opt;
    };

    this.orderList = function(){
        var campaigns = campaignList.children('li').get();
        if(campaigns.length>0){
            $("#campaign-sorter").show();
        }
        var counter = campaigns.length;
        $("#campaign-sorter li").removeClass("active");
        $("a[href=#"+campaignOption.name+"]").parent().addClass("active");
        campaigns.sort(campaignOption.sort);
        $('ul.campaign-list > li').show();
        $('ul.campaign-list > li').filter(campaignOption.filter).hide();
        $.each(campaigns, function(idx, itm) {
            campaignList.append(itm);
        });
        that.showInfoEmpty();
    };

    this.appendCampaignsOfUser= function(userId){
        if (campaignList.attr("data-addCampaignsByUserUrl") != undefined){
            var url = campaignList.attr("data-addCampaignsByUserUrl");
            $.ajax( {
                url:url,
                data:{userId:userId},
                statusCode: {
                    401: function() {
                        display.info("Estás deslogado");
                        setTimeout('location.reload()',5000);
                    }
                },
                beforeSend: function(){
                    pageLoadingOn();
                },
                complete:function(){
                    pageLoadingOff();
                }
            }).done(function(data, status, xhr) {
                campaignList.append(data);
                that.orderList();
                that.showInfoEmpty();
            })
        }
    },
    this.removeCampaignsOfUser = function(userId){
        $("ul.campaign-list .card-footer .owner .user[data-userid="+userId+"]").closest("article").closest("li").remove()
        that.showInfoEmpty();
    }

    this.showInfoEmpty = function(){
        if (campaignList.find("li:not(.info-empty)").length<=0){
            campaignList.find("li.info-empty").removeClass("hidden")
        }else{
            campaignList.find("li.info-empty").addClass("hidden")
        }
    }


}


function openAuthModal() {
    $('#authorizeProfileEdition').modal("show");
    var $form = $('form#accountDetailsForm');
    var $submitButton = $('form#accountDetailsForm #authorizeProfileEditionButtonOk');
    var $cancelButton = $('form#accountDetailsForm #authorizeProfileEditionButtonClose');

    $cancelButton.on('click', function(e){
        e.preventDefault();
        $('#authorizeProfileEdition').modal("hide");
    });

    $submitButton.on('click', function(e){
        e.preventDefault();
        if ($form.valid()){
            console.log('Válido');
            $form.submit();
        }
    });
}

var sortCampaigns;
var suggestFollowersAnimated;
$(function () {

    sortCampaigns = new SortCampaigns();
    sortCampaigns.orderList();

    $('ul#campaign-sorter li a').on('click', function(e){
        var option = $(this).attr('href').substr(1);
        sortCampaigns.setCampaignOption(option);
        sortCampaigns.orderList();
    });

    suggestFollowersAnimated = new Animate("#followOthers")

    $(".info-empty").mouseenter(function(e){
        suggestFollowersAnimated.start();
    })

    $(".info-empty").mouseleave(function(e){
        suggestFollowersAnimated.stop();
    })

    // Open authorization modal in edit settings
    $('body').on('click', '#auth', function(e){
        e.preventDefault();
        openAuthModal();
    });
});
