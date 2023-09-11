
function onYouTubeIframeAPIReady(){
    console.log("API YOUTUBE LOADED");
}

function YoutubeHelper(){
    var googleApiKey = kuorumKeys._googleJsAPIKey;
    var that = this;

    // LOAD IFRAME YOUTUBE API
    // https://developers.google.com/youtube/iframe_api_reference
    var tag = document.createElement('script');
    tag.src = "https://www.youtube.com/iframe_api";
    var firstScriptTag = document.getElementsByTagName('script')[0];
    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);


    function validYoutube(videoID, onSuccess, onError){
        var url = "https://www.googleapis.com/youtube/v3/videos?part=snippet&id="+videoID+"&key="+googleApiKey;
        $.ajax({
            url: url,
            //dataType: "jsonp",
            success: function(data) {
                // console.log(data)
                if (data.pageInfo.totalResults <= 0){
                    onError();
                }else{
                    onSuccess(data);
                }
            },
            error: function(jqXHR, textStatus, errorThrown)
            {
                onError()
            }
        });
    }

    function maxResImage(response, img){
        // console.log(response)
        // console.log(img)
        $.each(response.items, function (index, item) {
            var priorityQuality = ['maxres', 'standard', 'high', 'medium', 'default'];
            for (var idx in priorityQuality){
                var quality = priorityQuality[idx];
                if (_validThumbnail(item.snippet.thumbnails, quality)){
                    var maxResUrl = item.snippet.thumbnails[quality].url;
                    img.setAttribute('src', maxResUrl);
                    // console.log("Replaced youtube image: "+item.id+" -> "+quality +" :: "+maxResUrl)
                    break;
                }
            }

        });
    }
    function _validThumbnail(snippetThumbnail, quality){
        var thumbnail = snippetThumbnail[quality];

        var valid =
            thumbnail!= undefined &&
            thumbnail.url!= undefined &&
            thumbnail.width / thumbnail.height == 16/9;
        return valid;
    }


    this.checkValidYoutube= function(img){
        var youtubeId = img.getAttribute("data-youtubeId");
        var imageYoutubeNotFound = img.getAttribute("data-urlYoutubeNotFound");

        function setErrorYoutubeImage() {
            img.setAttribute("src", imageYoutubeNotFound);
            var a = img.parentNode;
            var videoContainer = a.parentNode;
            videoContainer.innerHTML = "";
            videoContainer.appendChild(img)
        }

        validYoutube(youtubeId, function(data){
            // Campaign has custom image
            var isCampaignThumbnailUrl = !$('meta[itemprop="thumbnailUrl"]').attr("content").includes("youtube");
            console.log("Custom image: "+isCampaignThumbnailUrl)
            isCampaignThumbnailUrl? '': maxResImage(data, img);
        }, function(){
           // setErrorYoutubeImage();
            console.log("Error loading youtube data");
        });
    };

    this.metaTags = ["property='og:image'","itemprop=image", "name='twitter:image:src'"];
    this.replaceNonExistImage=function(){
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
    };
    this.replaceYoutubeImageType=function(completeUrl, newImageName){
        var resultUrl = completeUrl.substring(0, completeUrl.lastIndexOf('/'));
        resultUrl = resultUrl +"/" +newImageName;
        return resultUrl;
    };

    this.replaceAllWrongYoutubeImages=function(){
        $.each( $("div.video img"), function( key, img ) {
            youtubeHelper.checkValidYoutube(img)
        });
    }

    this.playVideo=function(youtubeDiv){
        if (youtubeDiv.id == undefined || youtubeDiv.id == ''){
            youtubeDiv.id = guid();
        }
        var youtubeVideoId = youtubeDiv.getAttribute("data-youtubeId")
        var height = $(youtubeDiv).parent().find("a.front").height();
        player = new YT.Player(youtubeDiv.id, {
            videoId: youtubeVideoId,
            height: height,
            playerVars: { 'autoplay': 1, 'controls': 1, 'modestbranding':1, 'rel':0},
            events: {
                'onReady': function onPlayerReady(event) {
                    event.target.setVolume(100);
                    event.target.playVideo();
                },
                'onStateChange': function(){}
            }
        });
        // console.log(player)
        // player.playVideo();
    }
}

var youtubeHelper = new YoutubeHelper();