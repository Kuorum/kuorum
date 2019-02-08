<r:require modules="customFileUploader" />
<r:script>
    var ${fileGroup}_typeErrorText = "${g.message(code:'uploader.error.typeError')}";
    var ${fileGroup}_sizeErrorText = "${g.message(code:'uploader.error.sizeError', args:[Math.round(fileGroup.maxSize/1000/1000)])}";
    var ${fileGroup}_minSizeErrorText = "${g.message(code:'uploader.error.minSizeError', args: [Math.round(kuorum.core.FileGroup.MIN_SIZE_IMAGE/1024)])}";
    var ${fileGroup}_emptyErrorText = "${g.message(code:'uploader.error.emptyError')}";
    var ${fileGroup}_onLeaveText = "${g.message(code:'uploader.error.onLeave')}";
    var jcropApi;
    var fileId;
</r:script>
<div class="uploaderImageContainer ${fileGroup} ${cssClass}">
    <uploader:uploader
            id="uploaderImageId_${imageId}"
            multiple="false"
            url="${[mapping:'ajaxUploadFile']}"
            sizeLimit="${fileGroup.maxSize}"
            minSizeLimit="${kuorum.core.FileGroup.MIN_SIZE_IMAGE}"
            allowedExtensions='["\'png\'", "\'jpeg\'", "\'jpg\'", "\'JPG\'", "\'JPEG\'"]'
            messages='{
                    typeError: ${fileGroup}_typeErrorText,
                    sizeError: ${fileGroup}_sizeErrorText,
                    minSizeError:${fileGroup}_minSizeErrorText,
                    emptyError: ${fileGroup}_emptyErrorText,
                    onLeave: ${fileGroup}_onLeaveText
                }'
            params='[fileGroup:"\"${fileGroup}\""]' >
        <uploader:onSubmit>
            $("#${imageId}").attr("alt","Cargando");
                originalImgPath = $("#${imageId}").attr("src");
                $("#${imageId}").attr("src","${g.resource(dir: 'images', file: 'loading%402x.gif')}");

                $("#progresBar_${imageId}").removeClass("hidden").css("display","block")
                var progressBar = $("#progresBar_${imageId}").children(".progress-bar")
                progressBar.attr("aria-valuenow",100)
                progressBar.attr("aria-valuemax",0)
                progressBar.css("width","0%")
                progressBar.html("0%")
        </uploader:onSubmit>
        <uploader:onProgress>
            var progressBar = $("#progresBar_${imageId}").children(".progress-bar")
            progressBar.attr("aria-valuenow",loaded)
            progressBar.attr("aria-valuemax",total)
            var percent = Math.floor(loaded/total * 100)
            progressBar.css("width",percent+"%")
            progressBar.html(percent+"%")

            console.log(loaded+' of '+total+' done so far')
        </uploader:onProgress>
        <uploader:onComplete>
            $("#${imageId}").attr("src",responseJSON.absolutePathImg);
            %{--console.log(responseJSON.absolutePathImg)--}%
            fileId = responseJSON.fileId
            //Reiniciamos jcrop
            if (jcropApi){
                %{--console.log("detruido jcrop")--}%
                %{--jcropApi.destroy();--}%
                jcropApi.setImage(responseJSON.absolutePathImg)
            }
            $("#modal_${imageId}").modal('show');
            $("#progresBar_${imageId}").hide()

            //Is necesary the deleay because is necesary show the modal before to calculate the
            setTimeout(
              function()
              {
                var modalContent = $("#modal_${imageId} .modal-content").children(".modal-body")
                var padding = parseInt(modalContent.css("padding-left").replace('px', ''))
                 padding += parseInt(modalContent.css("padding-right").replace('px', ''))
                var boxWidth = parseInt(modalContent.innerWidth()) - padding
                %{--var boxHeigh = parseInt(modalContent.innerWidth()) - padding--}%

                $('#${imageId}').attr("width",boxWidth)
                $('#${imageId}').Jcrop({
                    onSelect:    showCoords,
                    keySupport: false,
                %{--bgColor:     'black',--}%
                    bgOpacity:   .4,
                %{--minSize:[boxWidth,0],--}%
                    %{--maxSize:[boxWidth,0],--}%
                    boxWidth: boxWidth,
                    boxHeight: 0,
                    setSelect:   [ 200, 200, 1001, 100 ],
                    aspectRatio: ${fileGroup.aspectRatio}
                },
                function(){
                    jcropApi = this;
                }
                );
              }, 500);
        </uploader:onComplete>
        <uploader:showMessage>
            display.error(message);
            %{--notyError(message);--}%
        </uploader:showMessage>
        <uploader:onCancel> alert('you cancelled the upload'); </uploader:onCancel>
    </uploader:uploader>
    <g:if test="${errorMessage}">
        <span for='input_${imageId}' class='error'>${errorMessage}</span>
    </g:if>
</div>
<r:script>
    function showCoords(coords){
        console.log(coords);
//        var $preview = $('#preview');
//        if (parseInt(coords.w) > 0)
//        {
//            var rx = 100 / coords.w;
//            var ry = 100 / coords.h;
//            $preview.attr('src', $(".upload-modal-image").attr('src'))
//
//            $preview.css({
//                width: Math.round(rx * 500) + 'px',
//                height: Math.round(ry * 370) + 'px',
//                marginLeft: '-' + Math.round(rx * coords.x) + 'px',
//                marginTop: '-' + Math.round(ry * coords.y) + 'px'
//            }).show();
//        }
    }
    function cropImage(imageId){
        var selected = jcropApi.tellSelect();
        var cutButton = $(".uploadKuorumImage .modal-footer button");
        $.ajax({
            type: "POST",
            url: "${g.createLink(controller: 'file', action: 'cropImage')}",
            data: {
                x:selected.x,
                y:selected.y,
                height:selected.h,
                width: selected.w,
                fileId: fileId
            },
            statusCode: {
                500: function() {
                    display.warn("${g.message(code:'uploader.error.cropping')}")
                }
            },
            beforeSend:function(){
                pageLoadingOn()
            },
            complete:function(){
            }
        }).done(function( data ) {
            changeImageBackground(data.absolutePathImg, imageId);
            $("#modal_"+imageId).modal('hide');
            $("#input_" + imageId).val(data.fileId);
            pageLoadingOff();
            formHelper.dirtyFormControl.dirty($("#input_"+imageId).parents("form"))
        });
    }

    function changeImageBackground(urlImage, imageId){
        var timestampedUrlImage = urlImage +'?timestamp='+new Date().getTime();
//        var timestampedUrlImage = urlImage;
        console.log($("#au-uploaderImageId_"+imageId+" .qq-upload-drop-area"));
        $("#au-uploaderImageId_"+imageId+" .qq-upload-drop-area").css("background-image",'url('+timestampedUrlImage+')');
        console.log($("#au-uploaderImageId_"+imageId+" .qq-upload-drop-area").css("background-image"));
        $("#au-uploaderImageId_" + imageId + " .qq-upload-drop-area").css("background-size", "100% auto");
        $("#au-uploaderImageId_" + imageId + " .qq-upload-drop-area").css("background-position", "0 0");
        $("#au-uploaderImageId_" + imageId +" .button-container").css("background-color","rgba(0, 0, 0, 0.7)");
        $image = $("#au-uploaderImageId_" + imageId +" .qq-upload-drop-area");
        console.log($image);
        $image.css("height", $image.width()/(${fileGroup.aspectRatio}))
    }

    <g:if test="${imageUrl}">

    // DEFERED SCRIPT
            $(function(){
                console.log( $("#au-uploaderImageId_${imageId} .qq-upload-drop-area"))
                console.log( "changeImageBackground('${raw(imageUrl)}', '${imageId}')")
                changeImageBackground('${raw(imageUrl)}', '${imageId}')
            });

    </g:if>
</r:script>
<input type="hidden" name="${name}" id="input_${imageId}" value="${value}"/>
%{--<img src="" id="preview"/>--}%
<div class="progress hidden" id="progresBar_${imageId}">
    <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;">
        0% Complete
    </div>
</div>
<div class="modal fade uploadKuorumImage" id="modal_${imageId}" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><g:message code="profile.modal.cropImage.title"/></h4>
            </div>
            <div class="modal-body">
                <img class="upload-modal-image" src="" id="${imageId}"/>
            </div>
            <div class="modal-footer">
                <a href="#" class="cancel" data-dismiss="modal"><g:message code="profile.modal.cropImage.cancel"/> </a>
                <button type="button" class="btn" onclick="cropImage('${imageId}')"><g:message code="profile.modal.cropImage.cropButton"/></button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->