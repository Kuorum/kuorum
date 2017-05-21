%{--<r:require modules="customFileUploader" />--}%
<r:require modules="findUploader" />



<div id="fine-uploader-gallery" class="kuorum-upload-multi-image"></div>

<!-- MODAL IMAGES CHANGED -->
<div class="modal fade in" id="multi-uploader-replaced-images-modal" tabindex="-1" role="dialog" aria-labelledby="multi-uploader-replaced-images-modal-title" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                </button>
                <h4>
                    <g:message code="uploader.multiImage.replaceImages.modal.title"/>
                </h4>
            </div>
            <div class="modal-body">
                <p><g:message code="uploader.multiImage.replaceImages.modal.body"/> </p>
                <div class="scroll-container">
                    <table class="table-bordered replaced-images-list">
                        <thead>
                            <th><g:message code="uploader.multiImage.replaceImages.modal.table.image"/></th>
                            <th><g:message code="uploader.multiImage.replaceImages.modal.table.times"/></th>
                        </thead>
                        <tbody>
                            <tr><td>image/img1.jpg</td><td>2</td></tr></li>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/template" id="qq-template-gallery">
<div class="qq-uploader-selector qq-uploader qq-gallery" qq-drop-area-text="Drop files here">
    <div class="qq-total-progress-bar-container-selector qq-total-progress-bar-container">
        <div role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" class="qq-total-progress-bar-selector qq-progress-bar qq-total-progress-bar"></div>
    </div>
    <div class="qq-upload-drop-area-selector qq-upload-drop-area" qq-hide-dropzone>
        <span class="qq-upload-drop-area-text-selector"></span>
    </div>
    <div class="qq-upload-button-selector btn btn-blue">
        <div><g:message code="uploader.multiImage.replaceImages.upload"/> </div>
    </div>
    <div class="qq-images-uploaded-arrow"> <span class="fa fa-3x fa-angle-right"></span> </div>
    <a class="images-uploaded-action btn btn-blue"><g:message code="uploader.multiImage.replaceImages.actionButton"/> </a>
    <span class="qq-drop-processing-selector qq-drop-processing">
        <span>Processing dropped files...</span>
        <span class="qq-drop-processing-spinner-selector qq-drop-processing-spinner"></span>
    </span>
    <ul class="qq-upload-list-selector qq-upload-list" role="region" aria-live="polite" aria-relevant="additions removals">
        <li>
            <span role="status" class="qq-upload-status-text-selector qq-upload-status-text"></span>
            <div class="qq-progress-bar-container-selector qq-progress-bar-container">
                <div role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" class="qq-progress-bar-selector qq-progress-bar"></div>
            </div>
            <span class="qq-upload-spinner-selector qq-upload-spinner"></span>
            <div class="qq-thumbnail-wrapper">
                <img class="qq-thumbnail-selector" qq-max-size="120" qq-server-scale>
            </div>
            <button type="button" class="qq-upload-cancel-selector qq-upload-cancel">X</button>
            <button type="button" class="qq-upload-retry-selector qq-upload-retry">
                <span class="qq-btn qq-retry-icon" aria-label="Retry"></span>
                Retry
            </button>

            <div class="qq-file-info">
                <div class="qq-file-name">
                    <span class="qq-upload-file-selector qq-upload-file"></span>
                    <span class="qq-edit-filename-icon-selector qq-btn qq-edit-filename-icon" aria-label="Edit filename"></span>
                </div>
                <input class="qq-edit-filename-selector qq-edit-filename" tabindex="0" type="text">
                <span class="qq-upload-size-selector qq-upload-size"></span>
                <button type="button" class="qq-btn qq-upload-delete-selector qq-upload-delete">
                    <span class="qq-btn qq-delete-icon" aria-label="Delete"></span>
                </button>
                <button type="button" class="qq-btn qq-upload-pause-selector qq-upload-pause">
                    <span class="qq-btn qq-pause-icon" aria-label="Pause"></span>
                </button>
                <button type="button" class="qq-btn qq-upload-continue-selector qq-upload-continue">
                    <span class="qq-btn qq-continue-icon" aria-label="Continue"></span>
                </button>
            </div>
        </li>
    </ul>

    <dialog class="qq-alert-dialog-selector">
        <div class="qq-dialog-message-selector"></div>
        <div class="qq-dialog-buttons">
            <button type="button" class="qq-cancel-button-selector">Close</button>
        </div>
    </dialog>

    <dialog class="qq-confirm-dialog-selector">
        <div class="qq-dialog-message-selector"></div>
        <div class="qq-dialog-buttons">
            <button type="button" class="qq-cancel-button-selector">No</button>
            <button type="button" class="qq-ok-button-selector">Yes</button>
        </div>
    </dialog>

    <dialog class="qq-prompt-dialog-selector">
        <div class="qq-dialog-message-selector"></div>
        <input type="text">
        <div class="qq-dialog-buttons">
            <button type="button" class="qq-cancel-button-selector">Cancel</button>
            <button type="button" class="qq-ok-button-selector">Ok</button>
        </div>
    </dialog>
</div>
</script>


<script>
    var imagesCampaign${campaign.id} = {}
    $(function(){
        var galleryUploader = new qq.FineUploader({
            element: document.getElementById("fine-uploader-gallery"),
            template: 'qq-template-gallery',
            cors:{
                allowXdr:true,
                expected:true,
            },
            request: {
                endpoint: '${raw(requestEndPoint)}'
            },
            session:{
                endpoint:'${raw(sessionEndPoint)}'
            },
            thumbnails: {
                placeholders: {
                    waitingPath: '${raw(g.resource(dir: 'js/fineUploader/placeholders', file: 'waiting-generic.png'))}',
                    notAvailablePath: '${raw(g.resource(dir: 'js/fineUploader/placeholders', file: 'not_available-generic.png'))}'
                }
            },
            namespace:"multiImage",
            validation: {
                allowedExtensions: ['jpeg', 'jpg', 'gif', 'png', 'JPG', 'JPEG','PNG']
            },
            callbacks: {
                onComplete: function (id, name, responseJSON, xhr) {
                    imagesCampaign${campaign.id}[name]=responseJSON.absolutePath

                },
                onSessionRequestComplete:function(response, success, xhr){
                    response.forEach(function(data){
                        imagesCampaign${campaign.id}[data.name]=data.thumbnailUrl
                    })
                },
            }
        });
//        galleryUploader.log = function(message, level) {window.console.log(message);}

        $(".images-uploaded-action").on("click", function(e){
            e.preventDefault();
            var $textarea =  $("textarea[name=text]")
            var text = $textarea.val();
            var $tbody = $("#multi-uploader-replaced-images-modal table.replaced-images-list tbody");
            $tbody.html("");

            Object.keys(imagesCampaign${campaign.id}).map(function(key, index) {
                var regex = new RegExp('src=[\'"][^\'"]*'+key+'[\'"]', 'gi')
                text =text.replace(regex, "src='"+imagesCampaign${campaign.id}[key]+"'")
                var matches = 0;
                if (regex.test(text)){
                    matches = text.match(regex).length;
                }
                $tbody.append("<tr><td>"+key+"</td><td>"+matches+"</td></tr></li>");
            });
            $textarea.val(text)
            $("#multi-uploader-replaced-images-modal").modal("show")
        })
    });
</script>

