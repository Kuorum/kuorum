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

<!-- MODAL REMOVE FILE -->
<div class="modal fade in modal-remove-file" id="multi-uploader-remove-file-modal-${divId}" tabindex="-1" role="dialog" aria-labelledby="multi-uploader-remove-file-modal-title" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fal fa-times-circle fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                </button>
                <h4>
                    <g:message code="uploader.multiFile.removeFile.modal.title"/>
                </h4>
            </div>
            <div class="modal-body">
                <p>Remove file - replaced</p>
            </div>
            <div class="modal-actions">
                <a href="" role="button" class="btn btn-blue inverted btn-lg confirm-button"><g:message code="uploader.multiFile.removeFile.modal.confirm"/></a>
                <a href="" role="button" class="btn btn-grey-light btn-lg close-modal"><g:message code="uploader.multiFile.removeFile.modal.cancel"/></a>
            </div>
        </div>
    </div>
</div>

<r:script>
    var removeCallbackAction = function(){console.log("Remove action is not defined");};
    if (multipleFileUploaders == undefined){
        var multipleFileUploaders = {};
    }

    $(function(){
        multipleFileUploaders['${divId}'] = new qq.MultipleFileUploader({
            multiple:true,
            disabled:${disabled?'true':'false'},
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
                fileUrlCopiedSuccess: "${g.message(code:'uploader.error.copiedUrlSuccess')}",
                confirmRemoveMessage: "${g.message(code:'uploader.multiFile.removeFile.modal.body')}"
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
            removeConfirmModal: function(fileName, msgConfirm, deleteActionCallback){
                $("#multi-uploader-remove-file-modal-${divId}").find(".modal-body p").html(msgConfirm);
                removeCallbackAction = deleteActionCallback;
                $("#multi-uploader-remove-file-modal-${divId}").modal("show");
                console.log("REMOVE CLICKED")
            }
        });

         $("#multi-uploader-remove-file-modal-${divId}").find(".confirm-button").on("click", function(e) {
            e.preventDefault();
            removeCallbackAction();
            $("#multi-uploader-remove-file-modal-${divId}").modal("hide");
         })
    })
</r:script>