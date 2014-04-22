<r:require modules="customFileUploader" />
<g:set var="imageId" value="imageId_XXXXX"/>
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
        url="${[controller:'file', action:"uploadImage"]}"
        sizeLimit="${fileGroup.maxSize}"
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
            $("#${imageId}").attr("src","${g.resource(dir: 'img', file: 'spinner_small.gif')}");
    </uploader:onSubmit>
    <uploader:onProgress> console.log(loaded+' of '+total+' done so far') </uploader:onProgress>
    <uploader:onComplete>
        $("#${imageId}").attr("src",responseJSON.absolutePathImg);
        console.log(responseJSON.absolutePathImg)
        fileId = responseJSON.fileId
        //Reiniciamos jcrop
        if (jcropApi)
            jcropApi.destroy();
        $('#${imageId}').Jcrop({
            onSelect:    showCoords,
            %{--bgColor:     'black',--}%
            bgOpacity:   .4,
            %{--minSize:[558,0],--}%
            %{--maxSize:[558,0],--}%
            boxWidth: 558, boxHeight: 0,
            setSelect:   [ 200, 200, 100, 100 ],
            aspectRatio: ${fileGroup.aspectRatio}},
            function(){
                jcropApi = this;
            }
        );
        $("#modal_${imageId}").modal('show');
    </uploader:onComplete>
    <uploader:showMessage>
        display.error(message);
        %{--notyError(message);--}%
    </uploader:showMessage>
    <uploader:onCancel> alert('you cancelled the upload'); </uploader:onCancel>
</uploader:uploader>

<script>
    function showCoords(c){
//        consoloe.log(c)
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
<div class="modal fade uploadKuorumImage" id="modal_${imageId}">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Recorta tu foto</h4>
            </div>
            <div class="modal-body">
                <img src="" id="${imageId}"/>
            </div>
            <div class="modal-footer">
                <a href="#" class="cancel" data-dismiss="modal">Cancelar</a>
                <button type="button" class="btn" onclick="cropImage()">Recortar</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->