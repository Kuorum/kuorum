%{--<r:require modules="customFileUploader" />--}%
<r:require modules="htmlMultiImageUploader" />



<div id="fine-uploader-gallery" class="kuorum-upload-multi-image"></div>

<!-- MODAL IMAGES CHANGED -->
<div class="modal fade in" id="multi-uploader-replaced-images-modal" tabindex="-1" role="dialog" aria-labelledby="multi-uploader-replaced-images-modal-title" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fal fa-times-circle fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
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

<script type="text/template" id="miu-template-gallery">
<div class="miu-uploader-selector miu-uploader miu-gallery" miu-drop-area-text="Drop files here">
    <div class="miu-total-progress-bar-container-selector miu-total-progress-bar-container">
        <div role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" class="miu-total-progress-bar-selector miu-progress-bar miu-total-progress-bar"></div>
    </div>
    <div class="miu-upload-drop-area-selector miu-upload-drop-area" miu-hide-dropzone>
        <span class="miu-upload-drop-area-text-selector"></span>
    </div>
    <div class="miu-upload-button-selector btn btn-blue">
        <div><g:message code="uploader.multiImage.replaceImages.upload"/> </div>
    </div>
    <div class="miu-images-uploaded-arrow"> <span class="fal fa-angle-right fa-3x"></span> </div>
    <a class="images-uploaded-action btn btn-blue"><g:message code="uploader.multiImage.replaceImages.actionButton"/> </a>
    <span class="miu-drop-processing-selector miu-drop-processing">
        <span>Processing dropped files...</span>
        <span class="miu-drop-processing-spinner-selector miu-drop-processing-spinner"></span>
    </span>
    <ul class="miu-upload-list-selector miu-upload-list" role="region" aria-live="polite" aria-relevant="additions removals">
        <li>
            <span role="status" class="miu-upload-status-text-selector miu-upload-status-text"></span>
            <div class="miu-progress-bar-container-selector miu-progress-bar-container">
                <div role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" class="miu-progress-bar-selector miu-progress-bar"></div>
            </div>
            <span class="miu-upload-spinner-selector miu-upload-spinner"></span>
            <div class="miu-thumbnail-wrapper">
                <img class="miu-thumbnail-selector" miu-max-size="120" miu-server-scale>
            </div>
            <button type="button" class="miu-upload-cancel-selector miu-upload-cancel">X</button>
            <button type="button" class="miu-upload-retry-selector miu-upload-retry">
                <span class="miu-btn miu-retry-icon" aria-label="Retry"></span>
                Retry
            </button>

            <div class="miu-file-info">
                <div class="miu-file-name">
                    <span class="miu-upload-file-selector miu-upload-file"></span>
                    <span class="miu-edit-filename-icon-selector miu-btn miu-edit-filename-icon" aria-label="Edit filename"></span>
                </div>
                <input class="miu-edit-filename-selector miu-edit-filename" tabindex="0" type="text">
                <span class="miu-upload-size-selector miu-upload-size"></span>
                <button type="button" class="miu-btn miu-upload-delete-selector miu-upload-delete">
                    <span class="miu-btn miu-delete-icon" aria-label="Delete"></span>
                </button>
                <button type="button" class="miu-btn miu-upload-pause-selector miu-upload-pause">
                    <span class="miu-btn miu-pause-icon" aria-label="Pause"></span>
                </button>
                <button type="button" class="miu-btn miu-upload-continue-selector miu-upload-continue">
                    <span class="miu-btn miu-continue-icon" aria-label="Continue"></span>
                </button>
            </div>
        </li>
    </ul>

    <dialog class="miu-alert-dialog-selector">
        <div class="miu-dialog-message-selector"></div>
        <div class="miu-dialog-buttons">
            <button type="button" class="miu-cancel-button-selector">Close</button>
        </div>
    </dialog>

    <dialog class="miu-confirm-dialog-selector">
        <div class="miu-dialog-message-selector"></div>
        <div class="miu-dialog-buttons">
            <button type="button" class="miu-cancel-button-selector">No</button>
            <button type="button" class="miu-ok-button-selector">Yes</button>
        </div>
    </dialog>

    <dialog class="miu-prompt-dialog-selector">
        <div class="miu-dialog-message-selector"></div>
        <input type="text">
        <div class="miu-dialog-buttons">
            <button type="button" class="miu-cancel-button-selector">Cancel</button>
            <button type="button" class="miu-ok-button-selector">Ok</button>
        </div>
    </dialog>
</div>
</script>


<r:script>
    var imagesCampaign${campaign.id} = {}
    $(function(){
        var galleryUploader = new miu.FineUploader({
            element: document.getElementById("fine-uploader-gallery"),
            template: 'miu-template-gallery',
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
</r:script>

