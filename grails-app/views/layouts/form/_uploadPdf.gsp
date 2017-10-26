<r:require modules="customFileUploader" />
<g:set var="divId" value="file-uploader-${pdfId}"/>
<g:set var="inputId" value="input_file_-${pdfId}"/>
<g:set var="text">
    <g:if test="${fileName}">
        ${fileName}
    </g:if>
    <g:else>
        ${placeHolder}
    </g:else>
</g:set>
<label>${label}</label>
<div id="${divId}">
    <noscript>
        <p>Please enable JavaScript to use file uploader.</p>
        <!-- or put a simple form for upload here -->
    </noscript>
</div>
<input type="hidden" name="${name}" id="${inputId}" value="${value}"/>

<r:script>
    $(function(){


        var pdfUploader = new qq.GenericFileUploader({
            // pass the dom node (ex. $(selector)[0] for jQuery users)
            elementID: '${divId}',
            allowedExtensions: ['pdf'],
            sizeLimit: 0, // max size
            minSizeLimit: 0, // min size
            abortOnFailure: true,
            text:'${raw(text.trim())}',
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
            action: '${raw(g.createLink(mapping:'ajaxUploadFilePDF'))}', // path to server-side upload script
            onSuccess: function(id, fileName, responseJSON){
                qq.GenericFileUploader.prototype._onComplete(id, fileName, responseJSON);
                $("#${inputId}").val(responseJSON.fileId)
            }
        });
    })
</r:script>