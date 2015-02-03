<r:require modules="customFileUploader" />

<script>
    var typeErrorText = "${g.message(code:'uploader.error.typeError')}";
    var sizeErrorText = "${g.message(code:'uploader.error.sizeError')}";
    var minSizeErrorText = "${g.message(code:'uploader.error.minSizeError')}";
    var emptyErrorText = "${g.message(code:'uploader.error.emptyError')}";
    var onLeaveText = "${g.message(code:'uploader.error.onLeave')}";
    var jcropApi;
    var fileId;
</script>
<div class="uploaderImageContainer ${fileGroup}">
    <uploader:uploader
            id="uploaderPdfId_${pdfId}"
            multiple="false"
            url="${[mapping:'ajaxUploadFilePDF']}"
            sizeLimit="${fileGroup.maxSize}"
            allowedExtensions='["\'pdf\'"]'
            messages='{
                    typeError: typeErrorText,
                    sizeError: sizeErrorText,
                    minSizeError: minSizeErrorText,
                    emptyError: emptyErrorText,
                    onLeave: onLeaveText
                }'
            params='[fileGroup:"\"${fileGroup}\""]' >
        <uploader:onSubmit>
            $("#${pdfId}").attr("alt","Cargando");
            originalPdfPath = $("#${pdfId}").attr("src");
            $("#${pdfId}").attr("src","${g.resource(dir: 'images', file: 'loading@2x.gif')}");
            $("#progresBar_${pdfId}").removeClass("hidden").css("display","block");
            var progressBar = $("#progresBar_${pdfId}").children(".progress-bar");
            progressBar.attr("aria-valuenow",100);
            progressBar.attr("aria-valuemax",0);
            progressBar.css("width","0%");
            progressBar.html("0%");
        </uploader:onSubmit>
        <uploader:onProgress>
            var progressBar = $("#progresBar_${pdfId}").children(".progress-bar");
            progressBar.attr("aria-valuenow",loaded);
            progressBar.attr("aria-valuemax",total);
            var percent = Math.floor(loaded/total * 100);
            progressBar.css("width",percent+"%");
            progressBar.html(percent+"%");

            console.log(loaded+' of '+total+' done so far');
        </uploader:onProgress>
        <uploader:onComplete>
            console.log(responseJSON.absolutePathPDF);
            $("#progresBar_${pdfId}").hide();
        </uploader:onComplete>
        <uploader:showMessage>
            display.error(message);
            %{--notyError(message);--}%
        </uploader:showMessage>
        <uploader:onCancel> alert('you cancelled the upload'); </uploader:onCancel>
    </uploader:uploader>
</div>

<input type="hidden" name="${name}" id="input_${pdfId}" value="${value}"/>
<div class="progress hidden" id="progresBar_${pdfId}">
    <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;">
        0% Complete
    </div>
</div>

<g:if test="${errorMessage}">
    <span for='input_${pdfId}' class='error'>${errorMessage}</span>
</g:if>