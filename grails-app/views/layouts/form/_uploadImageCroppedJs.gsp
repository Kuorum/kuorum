%{--REQUIRE MODULE croppieFileUploader --}%

<div class="popover-image" id="${popoverId}">
    <span class="popover-trigger fas fa-image" data-trigger="manual-hover" rel="popover" data-placement="top"  data-toggle="popover" target="_self" data-original-title="" title="">
    </span>

    <div class="popover" data-placement="top" >
        <div class="popover-image-container">
            <div class="popover-image-header clearfix">
                <img src="${popoverImageUrl}" alt="Image Q1" />
            </div>
            <div class="popover-image-body center">
                <button class="btn btn-transparent">
                    <span class="fa fa-trash"></span>
                </button>
                <button class="btn btn-transparent popover-image-upload">
                    <span class="fa fa-cloud-upload"></span>
                </button>
            </div>
        </div>
    </div>
    <div class="modal" id="modal-image-${popoverId}" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false" aria-labelledby="modal-image-title" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"
                            aria-label="Cerrar"><span aria-hidden="true"
                                                      class="fal fa-times-circle fa"></span><span
                            class="sr-only">Cerrar</span></button>
                    <h4 id="modal-image-title-${popoverId}"><g:message code="default.upload.image"/></h4>
                </div>
                <div class="modal-body">
                    <div class="upload-cropper-wrap">
                        <div class="upload-cropper-wrap-container"></div>
                    </div>
                    <div class="center upload-cropper-choose-file-container">
                        <input type="file" class="upload-cropper-choose-file" value="Choose a file" accept="image/*">
                        <input type="hidden" class="upload-cropper-choose-file-base64" name="${fieldName}" value="${value}">
                    </div>
                </div>

                <div class="modal-actions">
                    <a href="https://api.dev.kuorum.org" class="btn upload-cropper-save">
                        <span><g:message code="default.save"/></span>
                    </a>
                    <span class="error">Error saving image</span>
                </div>
            </div>
        </div>
    </div>
</div>