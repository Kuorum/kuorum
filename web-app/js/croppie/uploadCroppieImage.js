
const staticNoImage = "data:image/jpeg;base64,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ZtySAuPY0Iq+vwtteFA1zi/x9KcX+PsaEVfX4W2vCjUA9AB0YxvT0jgNqoI6BGs4MwCGFMa+HoUIq+vwtteFGqgbv6iobvrTF6ncoTMxFEIDXca6EVfX4W2vCjTsgIprGfqEDKIot8DTOH8QiQG5QgAI4OqhFX1+Ftrwo0zMlV59aZnQ86DPYRkWZ0NF2WpQir6/C214UaH7wIS5cz9YFi4Td4IDRVR0u+kSLhSk97TQir6/C214UDQ/MdoewlR6BgwHZnWE9jbqgIzg6KEVfX4W2vCgRnJs+yHMPId0RIRyfQnL+I6KEVfX4W2vCiBOEyARi7aDx7yYfS7iNCKvr8LbXhRCck6vHvQg7EIY+aFCKvr8LbXhQqDQVRgn3xZihAhwaqhFX1+FtrwoRkBGokJlEEZRFCMoWUaUIQjKIIyiKEZRFCMo0IQjKIIyiKEZRBGUIIygVFKEVfX4W2vCgcFQUVfX4W2vwxCEIQhCEIQhCEIQhCEIQhCEIQhCEIS+vwroTHos9Ds9Ds8s8s8s8s9o7uzyzyzyzyzyzyz0Oz0e7PLPLPLPLPQ7PLPQ7PLPQ7PLPIIcYdf+HVdJEdYOp9JlSFUJLSGqD/Agp3vpfJJ3RovxoAOQAU1kAghwXEAEbE0TOAPMu6BjbZUOvzYQRwh3UpIQMs6TaAdkSBVTGQC0H/1EgVRCW+1AByccrU/dkJSA8whr5h0X40QAsqL8keaMyOdIfcQSdwKm8IFohwUIS9SBVmyMUCaqE1nPUqcp6SZBLApoCRPdAnZTPuus4CAlH3eaPoiOVqfuyEYDgrzlU/BSDc7Q8AP4h9hBP1eF9mv0ewX2ipEOTIIKHSg9L2ZLYy7oxhLV3RhPdFMw2y/UShpuHflRjk+faB18R6KmE6MmBgbpmTqA54VMDAqkMmzHLpIc5Pn2RB9SQRS05dJAFkjumdzUEIygJOQ76ZlEBG6fogKjgsiq2ChQNpF1KJDibCn8G6MEKYJOd14LHQf0g//aAAwDAQACAAMAAAAQ8888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888884888808884088848880w8844088848880w888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888wc8448888888888888cU888888888888888888o8o8sAsM88888888888U888888888888888888A8g844M888888888884U888888888888888888I8404c88s0888888888U888888888888888888g88M4U888Q888888888U888888888888888888o888o8888o888488888U888888888888888888g88oY804w4I88IM0884U888888888888888888g88I888888o0wc8U888U888888888888888888o8sc8888888Ic88sU88U888888888888888888g488888888888888s88U888888888888888888g4wwwwwwwwwwwwwww08U888888888888888888sEMMMMMMMMMMMMMMMMUU8888888888888888888888888888888888888888888888888888088888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888880Q48Iw0w4w0888888888888888888888888888ggw4AgEYYEE088888888888888888888888888U4cc4wwAAMAs888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888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";
const staticLoading = "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";

$(function(){
    $(".campaign-form").on("click", ".popover-image .popover-image-upload", function (e) {
        e.preventDefault();
        var $popoverContainer = $(this).parents(".popover-image");
        console.log("Click on upload file")
        console.log($popoverContainer)
        new CropperPopup($popoverContainer);
    })

    $(".campaign-form").on("click", ".popover-image .popover-image-delete", function (e) {
        e.preventDefault();
        var $popoverContainer = $(this).parents(".popover-image");
        var cropperPopup = new CropperPopup($popoverContainer, false);
        cropperPopup.removeImageAttached();
    })
    prepareCropperPopups();

});
var activeCroppers ={}

function prepareCropperPopups(){
    $('[data-toggle="popover"][data-trigger="manual-hover"]')
        //En el mousenter sacamos el popover
        .on("click",function(e){
            if ($(this).siblings(".in").length ==0){
                $(this).popover('show');
            }
        })
    // //En el click ejecutamos el link normal. El framework the popover lo está bloqueando
    // .on("click", function(e){
    //     if ( !($(this).hasClass('user-rating') || $(this).hasClass('rating'))) {
    //         var href = $(this).attr("href");
    //         var target = $(this).attr("target");
    //         if (target && target == "_blank" ){
    //             window.open(href, '_blank');
    //             e.preventDefault();
    //         }else{
    //             window.location=href;
    //         }
    //     }
    // });

    $('[data-toggle="popover"]').popoverClosable();
    $('[data-toggle="popover"][data-trigger="manual-hover"]').parent()
        .on("mouseleave", function(e){
            $(this).children('[data-toggle="popover"]').popover('hide')
        });
}

function CropperPopup($popover, init = true){
    var $uploadCrop;
    var $modal;
    var popoverId;
    var reader;
    var $inputChooseFile = $popover.find(".upload-cropper-choose-file");
    var $inputUrlCropped = $popover.find("input.upload-cropper-choose-file-url");
    var $popOverImage = $popover.find(".popover-image-header img");
    var $saveButton = $popover.find(".upload-cropper-save");
    var $popoverTrigger = $popover.find(".popover-trigger");
    var $popOverButtonRemove = $popover.find(".popover-image-delete");
    var maxSize = parseInt($popover.attr("data-image-size"));
    function readFile(input) {
        if (input.files && input.files[0]) {
            var size = input.files[0].size
            if (!isNaN(maxSize) && size > (maxSize * 1024 * 1000) ) {
                display.warn(i18n.uploader.error.sizeError.replace("{0}", maxSize));
                return false;
            }else if (size <= 0){
                display.warn(i18n.uploader.error.emptyError);
                return false;
            }else{
                reader.readAsDataURL(input.files[0]);
                return true;
            }
        }
        else {
            swal("Sorry - you're browser doesn't support the FileReader API");
            return false;
        }
    }

    function initReader(){
        reader = new FileReader();

        reader.onload = function (e) {
            // console.log($uploadCrop)
            $uploadCrop.croppie('bind', {
                url: e.target.result
            }).then(function(){
                console.log('jQuery bind complete');
            });

        }
    }

    function initPopoverId(){
        if ($popover.attr("id")!= undefined){
            popoverId = $popover.attr("id");
        }else{
            popoverId = guid();
            $popover.attr("id", popoverId);
        }
    }

    function dataURItoBlob(dataURI) {
        // convert base64/URLEncoded data component to raw binary data held in a string
        var byteString;
        if (dataURI.split(',')[0].indexOf('base64') >= 0)
            byteString = atob(dataURI.split(',')[1]);
        else
            byteString = unescape(dataURI.split(',')[1]);

        // separate out the mime component
        var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

        // write the bytes of the string to a typed array
        var ia = new Uint8Array(byteString.length);
        for (var i = 0; i < byteString.length; i++) {
            ia[i] = byteString.charCodeAt(i);
        }

        return new Blob([ia], {type:mimeString});
    }

    function attachEvents(){
        $inputChooseFile.on('change', function () {
            var fileReadProperly = readFile(this);
            if (fileReadProperly){
                $modal.modal("show");
            }
        });

        $saveButton.click(function(e){
            e.preventDefault();
            $uploadCrop.croppie('result', {
                type: "canvas",
                size: "viewport", // IF "original" -> will be better quality but the size of the image is huge
                format: "png",
                // quality: 0.5, // default -> 1
                // viewport: { width: 300, height: 300 },
                // boundary: { width: 300, height: 300 }
            }).then(function(response){
                // $popover.find(".popover-image-header img").attr("src",response);
                link = $saveButton.attr("href")
                var blob = dataURItoBlob(response);
                var fd = new FormData();
                fd.append("qqfile", blob, guid()+'.png');
                fd.append("fileGroup", "USER_AVATAR");
                var loadingSpan = $("<span class='fas fa-spinner fa-spin'></span>")
                $saveButton.prepend(loadingSpan)
                // fd.append("originalFilename", guid()+'.png');
                $.ajax({
                    url:link,
                    type: "POST",
                    data:fd,
                    cache:false,
                    processData: false,
                    contentType: false,
                    // data:{
                    //     "fileGroup": "USER_AVATAR",
                    //     "image": response,
                    //     "fileName":  guid()+'.png'
                    // },
                    success:function(data)
                    {
                        loadingSpan.remove()
                        switchIconActive("on")
                        $popOverImage.attr("src",response);
                        $inputUrlCropped.val(data.absolutePathImg);
                        $modal.modal('hide');
                        $popOverButtonRemove.removeClass("hide");
                    },
                    error:function(data){
                        display.warn("Error uploading image")
                        $popover.find(".modal-actions span.error").show();
                    }
                });
            })
        });
    }

    function resetModal(){
        $inputChooseFile.val("")
    }

    function switchIconActive(action){
        if (action.toLowerCase() == "on"){
            $popoverTrigger.removeClass('fal').addClass('fas');
        }else{
            $popoverTrigger.removeClass('fas').addClass('fal');
        }
    }

    initPopoverId();

    // CROPPER OPTS
    var opts = {
        viewport: { width: 200, height: 200 },
        boundary: { width: 300, height: 300 }, // It should fix as the css config
        showZoomer: true,
        enableOrientation: true // IMPORTANT: It doesn't crop properly without it
    };

    if (init) {
        // INIT MODAL
        $modal = $popover.find(".modal");
        $modal.on('shown.bs.modal', function () {
            $popover.find(".modal-actions span.error").hide();
        });


        $modal.on('hidden.bs.modal', function (e) {
            resetModal();
        })

        $inputChooseFile.click();

        // INIT CROPPER
        if (activeCroppers[popoverId] != undefined) {
            // console.log("Already initialized")
            $uploadCrop = activeCroppers[popoverId]
        } else {
            $uploadCrop = $popover.find(".upload-cropper-wrap .upload-cropper-wrap-container").croppie(opts);
            activeCroppers[popoverId] = $popover
            // INIT READER
            initReader();
            attachEvents();
        }
    }

    // PUBLIC
    this.removeImageAttached = function(){
        $inputUrlCropped.val("");
        $inputChooseFile.val("");
        $popOverImage.attr("src",staticNoImage);
        $popOverButtonRemove.addClass("hide");
        switchIconActive("off")
    }
}