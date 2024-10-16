
function SortCampaigns() {
    var that = this;
    var campaignList = $('.politician-card ul.search-list, .dashboard ul#campaign-list-id.search-list');

    this.campaignOptions = {};
    this.campaignOptions['latest'] = {
        sort: function(a, b){
            var aTime = $(a).find('.link-wrapper').attr('data-datepublished');
            var bTime = $(b).find('.link-wrapper').attr('data-datepublished');
            var aStarred = $(a).find('.link-wrapper').parents("article.box-ppal").hasClass("highlighted");
            var bStarred = $(b).find('.link-wrapper').parents("article.box-ppal").hasClass("highlighted");
            if (aStarred) return -1;
            if (bStarred) return 1;
            if(aTime != undefined && bTime != undefined){
                return bTime.localeCompare(aTime);
            }
            if(aTime == undefined && bTime != undefined){
                return -1;
            }
            if(bTime == undefined && aTime != undefined){
                return 1;
            }
            return 0;
        },
        filter: function (e) {
            $('ul.search-list li').removeClass('hide');
            $('ul.search-list li').removeClass('last-odd');
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
        console.log("orderList")
        var campaigns = campaignList.children('li').get();
        if(campaigns.length>0){
            $("#campaign-sorter").show();
        }
        var counter = campaigns.length;
        $("#campaign-sorter li").removeClass("active");
        $("a[href=#"+campaignOption.name+"]").parent().addClass("active");
        campaigns.sort(campaignOption.sort)
        var campaignIds = campaigns.map(function (i) {return $(i).find("article > .link-wrapper").attr("id").split("-")[1]})
        var duplicatedIds = findDuplicates(campaignIds)
        var repeated = function(pos, item) {
            var currId = $(item).find(".link-wrapper").attr("id").split("-")[1];
            var repeated = duplicatedIds.indexOf(currId)>=0;
            var highlighted = $(item).find("article").hasClass("highlighted")
            return repeated  && !highlighted;
        };
        campaigns = campaigns.filter(function(item, pos, ary) {
            if (pos){
                var currId = $(item).find(".link-wrapper").attr("id");
                var prevId = $(ary[pos - 1]).find(".link-wrapper").attr("id");
                var repeated = duplicatedIds.indexOf(currId)>=0;
                return prevId != currId && repeated;
            }else{
                return false;
            }
        });
        $('ul.search-list > li').show();
        $('ul.search-list > li').filter(campaignOption.filter).hide();
        $('ul.search-list > li').filter(repeated).remove();
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
                console.log("Ajax Success")
                campaignList.append(data);
                that.orderList();
                that.showInfoEmpty();
            })
        }
    },
    this.removeCampaignsOfUser = function(userId){
        $(".dashboard ul.search-list .card-footer .owner .user[data-userid="+userId+"]").closest("article").closest("li").remove()
        that.showInfoEmpty();
    }

    this.showInfoEmpty = function(){
        if (campaignList.find("li").length<=0){
            campaignList.siblings(".load-more").addClass("hidden");
            $("div.info-campaigns-empty").removeClass("hidden")
        }else{
            campaignList.siblings(".load-more").removeClass("hidden");
            $("div.info-campaigns-empty").addClass("hidden")
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
    // sortCampaigns.orderList();

    $('ul#campaign-sorter li a').on('click', function(e){
        var option = $(this).attr('href').substr(1);
        sortCampaigns.setCampaignOption(option);
        sortCampaigns.orderList();
    });

    suggestFollowersAnimated = new Animate("#followOthers")

    $(".info-campaigns-empty").mouseenter(function(e){
        suggestFollowersAnimated.start();
    })

    $(".info-campaigns-empty").mouseleave(function(e){
        suggestFollowersAnimated.stop();
    })

    // Open authorization modal in edit settings
    $('body').on('click', '#auth', function(e){
        e.preventDefault();
        openAuthModal();
    });
});
