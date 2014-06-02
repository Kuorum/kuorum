<r:require modules="customFileUploader" />

<label for="input_${imageId}" class="${labelCssClass}">${label} </label>
<script>
    var typeErrorText = "${g.message(code:'uploader.error.typeError')}"
    var sizeErrorText = "${g.message(code:'uploader.error.sizeError')}"
    var minSizeErrorText = "${g.message(code:'uploader.error.minSizeError')}"
    var emptyErrorText = "${g.message(code:'uploader.error.emptyError')}"
    var onLeaveText = "${g.message(code:'uploader.error.onLeave')}"
    var jcropApi
    var fileId
</script>
<uploader:uploader
        id="uploaderImageId"
        multiple="false"
        url="${[mapping:'ajaxUploadFile']}"
        sizeLimit="${fileGroup.maxSize}"
        minSizeLimit="${kuorum.core.FileGroup.MIN_SIZE_IMAGE}"
        allowedExtensions='["\'png\'", "\'gif\'", "\'jpeg\'", "\'jpg\'"]'
        messages='{
                typeError: typeErrorText,
                sizeError: sizeErrorText,
                minSizeError: minSizeErrorText,
                emptyError: emptyErrorText,
                onLeave: onLeaveText
            }'
        params='[fileGroup:"\"${fileGroup}\""]' >
    <uploader:onSubmit>
        $("#${imageId}").attr("alt","Cargando");
            originalImgPath = $("#${imageId}").attr("src");
            $("#${imageId}").attr("src","${g.resource(dir: 'images', file: 'spinner.gif')}");
    </uploader:onSubmit>
    <uploader:onProgress>
        $("#progresBar_${imageId}").removeClass("hidden").css("display","block")
        var progressBar = $("#progresBar_${imageId}").children(".progress-bar")
        progressBar.attr("aria-valuenow",loaded)
        progressBar.attr("aria-valuemax",total)
        var percent = total/loaded * 100
        progressBar.css("width",percent+"%;")

        console.log(loaded+' of '+total+' done so far')
    </uploader:onProgress>
    <uploader:onComplete>
        $("#${imageId}").attr("src",responseJSON.absolutePathImg);
        console.log(responseJSON.absolutePathImg)
        fileId = responseJSON.fileId
        //Reiniciamos jcrop
        if (jcropApi){
            console.log("detruido jcrop")
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
            console.log("${imageId}")
            var padding = parseInt(modalContent.css("padding-left").replace('px', ''))
             padding += parseInt(modalContent.css("padding-right").replace('px', ''))
             console.log("padding:" + padding )
            var boxWidth = parseInt(modalContent.innerWidth()) - padding
            %{--var boxHeigh = parseInt(modalContent.innerWidth()) - padding--}%
            console.log(boxWidth)

            $('#${imageId}').attr("width",boxWidth)
            $('#${imageId}').Jcrop({
                onSelect:    showCoords,
                keySupport: false,
            %{--bgColor:     'black',--}%
                bgOpacity:   .4,
            %{--minSize:[boxWidth,0],--}%
                %{--maxSize:[boxWidth,0],--}%
                boxWidth: boxWidth, boxHeight: 0,
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

<script>
    function showCoords(coords){
       console.log(coords)
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
    function cropImage(){
        var selected = jcropApi.tellSelect()


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
                    display.warn("Ha habido algún problbema recortando la foto. Vuelva a intentarlo")
                }
            }
        }).done(function( data ) {
            changeImageBackground(data.absolutePathImg)
            $("#modal_${imageId}").modal('hide');
            $("#input_${imageId}").val(data.fileId)
        });
    }

    function changeImageBackground(urlImage){
        $(".qq-upload-drop-area").css("background-image",'url('+urlImage+')');
        $(".qq-upload-drop-area").css("background-size","100% auto")
        $(".qq-upload-drop-area").css("background-position","0 0")
        $(".button-container").css("background-color","rgba(0, 0, 0, 0.7)")
    }

    <g:if test="${imageUrl}">
    $(function(){
        changeImageBackground('${imageUrl}')
    })
    </g:if>
</script>
<input type="hidden" name="${name}" id="input_${imageId}" value="${value}"/>
%{--<img src="" id="preview"/>--}%
<div class="progress hidden" id="progresBar_${imageId}">
    <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;">
        0% Complete
    </div>
</div>
<div class="modal fade uploadKuorumImage" id="modal_${imageId}">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Recorta tu foto</h4>
            </div>
            <div class="modal-body">
                <img class="upload-modal-image" src="" id="${imageId}"/>
            </div>
            <div class="modal-footer">
                <a href="#" class="cancel" data-dismiss="modal">Cancelar</a>
                <button type="button" class="btn" onclick="cropImage()">Recortar</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<g:if test="${errorMessage}">
    <span for='input_${imageId}' class='error'>${errorMessage}</span>
</g:if>