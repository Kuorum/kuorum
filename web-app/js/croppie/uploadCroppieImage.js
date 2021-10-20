
$(function(){
    prepareCropperPopups();
});
var activeCroppers ={}

function prepareCropperPopups(){
    $(".popover-image").on("click", ".popover-image-upload", function (e) {
        e.preventDefault();
        var $popoverContainer = $(this).parents(".popover-image");
        new CropperPopup($popoverContainer);
    })

    $('[data-toggle="popover"][data-trigger="manual-hover"]')
        //En el mousenter sacamos el popover
        .on("mouseenter",function(e){
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

function CropperPopup($popover){
    var $uploadCrop;
    var $modal;
    var popoverId;
    var reader;
    var $inputChooseFile = $popover.find(".upload-cropper-choose-file");
    var $saveButton = $popover.find(".upload-cropper-save");
    function readFile(input) {
        if (input.files && input.files[0]) {
            reader.readAsDataURL(input.files[0]);
        }
        else {
            swal("Sorry - you're browser doesn't support the FileReader API");
        }
    }

    function initReader(){
        reader = new FileReader();

        reader.onload = function (e) {
            console.log($uploadCrop)
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

    function attachEvents(){
        $inputChooseFile.on('change', function () {
            readFile(this);
            $modal.modal("show");
        });

        $saveButton.click(function(e){
            e.preventDefault();
            $uploadCrop.croppie('result', {
                type: "canvas",
                size: "original",
                format: "png",
                quality: 1
            }).then(function(response){
                $popover.find(".popover-image-header img").attr("src",response);
                $popover.find("input.upload-cropper-choose-file-base64").val(response);
                $modal.modal('hide');

                // $.ajax({
                //     url:link,
                //     type: "POST",
                //     data:{"image": response},
                //     success:function(data)
                //     {
                //         $popover.find(".popover-image-header img").src(response);
                //         $modal.modal('hide');
                //     },
                //     error:function(data){
                //         console.log(data)
                //         $popover.find(".modal-actions span.error").show();
                //     }
                // });
            })
        });
    }

    function resetModal(){
        $inputChooseFile.val("")
    }

    initPopoverId();

    // CROPPER OPTS
    var opts = {
        viewport: { width: 100, height: 100 },
        // boundary: { width: 300, height: 300 }, // It should fix as the css config
        showZoomer: true,
        enableOrientation: true // IMPORTANT: It doesn't crop properly without it
    };

    // INIT MODAL
    $modal = $popover.find(".modal");
    $modal.on('shown.bs.modal', function(){
        $popover.find(".modal-actions span.error").hide();
    });


    $modal.on('hidden.bs.modal', function (e) {
        resetModal();
    })

    $inputChooseFile.click();

    // INIT CROPPER
    if (activeCroppers[popoverId] != undefined){
        // console.log("Already initialized")
        $uploadCrop = activeCroppers[popoverId]
    }else{
        $uploadCrop = $popover.find(".upload-cropper-wrap .upload-cropper-wrap-container").croppie(opts);
        activeCroppers[popoverId] = $popover
        // INIT READER
        initReader();
        attachEvents();
    }

}