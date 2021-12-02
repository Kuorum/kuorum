%{--REQUIRE MODULE croppieFileUploader --}%

<div class="popover-image" id="${popoverId}" data-image-size="${maxSizeMega}" data-image-ratio="${aspectRatio}">
    <span class="popover-trigger ${value?'fas':'fal'} fa-image" data-trigger="manual-hover" rel="popover" data-placement="top"  data-toggle="popover" target="_self" data-original-title="" title="">
    </span>

    <div class="popover" data-placement="top">
        <div class="popover-image-container">
            <div class="popover-image-header clearfix">
                <img src="${value?:g.resource(dir: "images", file: "no-image.jpg")}" alt="Image of the option" />
            </div>
            <div class="popover-image-body center">
                <button class="btn btn-transparent popover-image-delete ${value?'':'hide'}">
                    <span class="fal fa-trash"></span>
                </button>
                <button class="btn btn-transparent popover-image-upload">
                    <span class="fal fa-cloud-upload"></span>
                </button>
            </div>
        </div>
    </div>
    <div class="modal" tabindex="-1" role="dialog" data-backdrop="static" data-keyboard="false" aria-labelledby="modal-image-title" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"
                            aria-label="Cerrar"><span aria-hidden="true"
                                                      class="fal fa-times-circle fa"></span><span
                            class="sr-only">Cerrar</span></button>
                    <h4><g:message code="default.upload.image"/></h4>
                </div>
                <div class="modal-body">
                    <div class="upload-cropper-wrap">
                        <div class="upload-cropper-wrap-container"></div>
                    </div>
                    <div class="center upload-cropper-choose-file-container">
                        <input type="file" class="upload-cropper-choose-file" value="Choose a file" accept="image/*">
                        <input type="hidden" class="upload-cropper-choose-file-url" name="${fieldName}" value="${value}">
                    </div>
                </div>

                <div class="modal-actions">
                    <g:link mapping="ajaxUploadFile" class="btn upload-cropper-save">
                        <span><g:message code="default.save"/></span>
                    </g:link>
                    <span class="error">Error saving image</span>
                </div>
            </div>
        </div>
    </div>
</div>