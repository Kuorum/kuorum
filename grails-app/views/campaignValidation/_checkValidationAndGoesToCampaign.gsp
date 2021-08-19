<r:script>
        function checkValidation(){
            var url =
            $.ajax({
                type: "POST",
                url: kuorumUrls.profileDomainValidationChecker,
                data: {
                    campaignId:${campaign.id}
    },
    statusCode: {
        500: function() {
            console.log("ERROR")
        }
    },
    beforeSend:function(){
        console.log("Checking external validation")
    },
    complete:function(){
    }
}).done(function( data ) {
    if (data.validated){
        pageLoadingOn()
        window.location="${g.createLink(mapping: "campaignShow", params: campaign.encodeAsLinkProperties(), fragment: "survey-questions")}"
                }
            });
        }
        var checkValidationInterval = window.setInterval(checkValidation, 10000);
</r:script>