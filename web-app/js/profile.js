
function SortCampaigns() {

    var that = this;
    var campaignList = $('ul.campaign-list');

    this.campaignOptions = {};
    this.campaignOptions['latest'] = {
        sort: function(a, b){
            var aTime = $(a).find('.link-wrapper').attr('data-datepublished');
            var bTime = $(b).find('.link-wrapper').attr('data-datepublished');
            return bTime.localeCompare(aTime);
        },
        filter: function (e) {
            $(this).removeClass('hide');
            return false;
        },
        name: 'latest'
    };

    this.campaignOptions['posts'] = {
        sort:function(e){
            return false;
        },
        filter:function(idx){
            var articleId = $(this).find(".link-wrapper").attr('id');
            if(articleId.indexOf("post")!=0){
                $(this).addClass('hide');
                return articleId;
            }
            else{
                $(this).removeClass('hide');
                lastOdd.addClass('last-odd');
            }
        },
        name:"posts"
    };

    this.campaignOptions['debates'] = {
        sort:function(idx){
            return false;
        },
        filter:function(idx){

            var articleId = $(this).find(".link-wrapper").attr('id');
            if(articleId.indexOf("debate")!=0) {
                $(this).addClass('hide');
                return articleId;
            }
            else{
                $(this).removeClass('hide');
                lastOdd.addClass('last-odd');
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
            if((idx == counter - 1)&&(idx % 2 != 0)){
                lastOdd = $(this);
            }
        });
    }
}

var sortCampaigns;
var lastOdd;
$(function () {
    sortCampaigns = new SortCampaigns();

    sortCampaigns.orderList();

    $('ul#campaign-sorter li a').on('click', function(e){
        var option = $(this).attr('href').substr(1);
        sortCampaigns.setCampaignOption(option);
        sortCampaigns.orderList();
    });
});