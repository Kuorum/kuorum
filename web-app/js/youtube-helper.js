
function YoutubeHelper(){
    var googleApiKey = "AIzaSyBlkPXlyoUtZZfco4OF3o27OmL7NjCOXm0"; // TODO: This key to a properties file
    var that = this;
    function validYoutube(videoID, onSuccess, onError){
        var url = "https://www.googleapis.com/youtube/v3/videos?part=snippet&id="+videoID+"&key="+googleApiKey;
        $.ajax({
            url: url,
            //dataType: "jsonp",
            success: function(data) {
                console.log(data)
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
        console.log(response)
        console.log(img)
        $.each(response.items, function (index, item) {
            if (item.snippet.thumbnails.maxres != undefined && item.snippet.thumbnails.maxres.url!= undefined){
                var maxResUrl = item.snippet.thumbnails.maxres.url;
                img.setAttribute('src', maxResUrl);
            }else if (item.snippet.thumbnails.high != undefined && item.snippet.thumbnails.high.url!= undefined){
                var maxResUrl = item.snippet.thumbnails.high.url;
                img.setAttribute('src', maxResUrl);
            }

        });
    }

    this.checkValidYoutube= function(img){
        var youtubeId = img.getAttribute("data-youtubeId");
        var imageYoutubeNotFound = img.getAttribute("data-urlYoutubeNotFound");
        validYoutube(youtubeId, function(data){
            maxResImage(data, img);
        }, function(){
            img.setAttribute("src", imageYoutubeNotFound);
            var a = img.parentNode
            var videoContainer = a.parentNode
            videoContainer.innerHTML="";
            videoContainer.appendChild(img)
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
    }
    this.replaceYoutubeImageType=function(completeUrl, newImageName){
        var resultUrl = completeUrl.substring(0, completeUrl.lastIndexOf('/'));
        resultUrl = resultUrl +"/" +newImageName;
        return resultUrl;
    }

    this.replaceAllWrongYoutubeImages=function(){
        $.each( $("div.video img"), function( key, img ) {
            youtubeHelper.checkValidYoutube(img)
        });
    }
}

var youtubeHelper = new YoutubeHelper();