
function YoutubeHelper(){
    var googleApiKey = "AIzaSyAVooZZq5A5kpuTrWvwU2X_P6oXpAszOAc"; // TODO: This key to a properties file
    var that = this;
    function validYoutube(videoID, onSuccess, onError){
        $.ajax({
            url: "https://www.googleapis.com/youtube/v3/videos?part=status&id="+videoID+"&key="+googleApiKey,
            //dataType: "jsonp",
            success: function(data) {
                console.log(data)
                if (data.pageInfo.totalResults <= 0){
                    onError();
                }else{
                    onSuccess();
                }
            },
            error: function(jqXHR, textStatus, errorThrown)
            {
                onError()
            }
        });
    }
    this.checkValidYoutube= function(img){
        var youtubeId = img.getAttribute("data-youtubeId");
        var imageYoutubeNotFound = img.getAttribute("data-urlYoutubeNotFound");
        validYoutube(youtubeId, function(){}, function(){
            img.setAttribute("src", imageYoutubeNotFound);
            var a = img.parentNode
            var videoContainer = a.parentNode
            videoContainer.innerHTML="";
            videoContainer.appendChild(img)
        });

    }

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
}

var youtubeHelper = new YoutubeHelper();