<r:require modules="customFileUploader" />
<g:set var="divId" value="multiple-file-uploader-${elementId}"/>
<g:if test="${label}">
    <label>${label}</label>
</g:if>
<div id="${divId}">
    <noscript>
        <p>Please enable JavaScript to use file uploader.</p>
        <!-- or put a simple form for upload here -->
    </noscript>
</div>

<r:script>
    $(function(){


        var multipleFileUploader = new qq.MultipleFileUploader({
            multiple:true,
            element: document.getElementById('${divId}'),
            allowedExtensions: ['pdf', 'doc', 'docx', 'ppt', 'pptx', 'xls', 'xlsx', 'zip', 'rar', 'jpg', 'jpeg', 'JPG', 'JPEG', 'gif','GIF', 'PNG', 'png'],
            sizeLimit: 20971520, // 20 M max size
            minSizeLimit: 0, // min size
            abortOnFailure: true,
            messages:{
                typeError: '${g.message(code:'uploader.error.typeError')}',
                sizeError: '${g.message(code:'uploader.error.sizeError', args : ['20'])}',
                minSizeError: '${g.message(code:'uploader.error.minSizeError')}',
                emptyError: '${g.message(code:'uploader.error.emptyError')}',
                onLeave: '"${g.message(code:'uploader.error.onLeave')}"',
                fileUrlCopiedSuccess: "${g.message(code:'uploader.error.copiedUrlSuccess')}"
            },
            showMessage:function(message){
                display.success(message);
            },
            action: '${raw(actionUpload)}', // path to server-side upload script
            actionDelete: '${raw(actionDelete)}', // path to server-side delete file
            initialFiles: ${alreadyUploadedFiles?raw("['${alreadyUploadedFiles.join("','")}']"):'[]'},
            fileTemplate: '<li>' +
                '<div class="qq-upload-file-type"></div>' +
                '<div class="qq-upload-file"></div>' +
                '<div class="qq-upload-spinner"></div>' +
                '<div class="qq-upload-size"></div>' +
                '<div class="qq-upload-cancel"><span class="fal fa-ban"></span></div>' +
                '<div class="qq-upload-failed-text"><abbr title="${g.message(code:'uploader.error.serviceError')}"><span class="fal fa-exclamation-circle"></span></abbr></div>' +
                '<div class="qq-upload-success"><span class="fal fa-copy"></span></div>' +
                '<div class="qq-upload-delete"> <a href="#" class="qq-upload-delete-action fal fa-trash"></a></div>' +
                '</li>',
        });
    })
</r:script>