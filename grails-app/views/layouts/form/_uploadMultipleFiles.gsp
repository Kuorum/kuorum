<r:require modules="customFileUploader" />
<g:set var="divId" value="multiple-file-uploader-${campaignId}"/>
%{--<label>${label}</label>--}%
<div id="${divId}">
    <noscript>
        <p>Please enable JavaScript to use file uploader.</p>
        <!-- or put a simple form for upload here -->
    </noscript>
</div>

<r:script>
    $(function(){


        var multipleFileUploader = new qq.MultipleFileUploader({
            // pass the dom node (ex. $(selector)[0] for jQuery users)
            multiple:true,
            element: document.getElementById('${divId}'),
            allowedExtensions: ['pdf', 'doc', 'docx', 'ppt', 'pptx', 'xls', 'xlsx', 'zip', 'rar', 'jpg', 'jpeg', 'JPG', 'JPEG', 'gif','GIF', 'PNG', 'png'],
            sizeLimit: 0, // max size
            minSizeLimit: 0, // min size
            abortOnFailure: true,
            messages:{
                typeError: '${g.message(code:'uploader.error.typeError')}',
                sizeError: '${g.message(code:'uploader.error.sizeError')}',
                minSizeError: '${g.message(code:'uploader.error.minSizeError')}',
                emptyError: '${g.message(code:'uploader.error.emptyError')}',
                onLeave: '"${g.message(code:'uploader.error.onLeave')}"'
            },
            showMessage:function(message){
                display.error(message);
            },
            action: '${raw(g.createLink(mapping:'ajaxUploadCampaignFile', params: campaign.encodeAsLinkProperties()))}', // path to server-side upload script
            actionDelete: '${raw(g.createLink(mapping:'ajaxDeleteCampaignFile', params: campaign.encodeAsLinkProperties()))}', // path to server-side delete file
            onComplete: function(id, fileName, responseJSON){
                var $liFile = $($("#${divId} .qq-upload-list li")[id]);
                $liFile.find(".qq-upload-file").html("<a href='"+responseJSON.fileUrl+"'>"+fileName+"</a>")
            }
        });
    })
</r:script>