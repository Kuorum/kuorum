
$(function(){
    $(".popover-image").on("click", ".popover-image-upload", function (e) {
        e.preventDefault();
        var $popoverContainer = $(this).parents(".popover-image");
        new CropperPopup($popoverContainer);
    })
});
var activeCroppers ={}

function CropperPopup($popover){
    var $uploadCrop;
    var $modal;
    var popoverId;
    var reader;
    var $inputFile = $popover.find(".upload-cropper-choose-file");
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
        $inputFile.on('change', function () {
            readFile(this);
            $modal.modal("show");
        });

        $saveButton.click(function(e){
            e.preventDefault();
            var link = $(this).attr("href");
            $uploadCrop.croppie('result', {
                type: 'canvas',
                size: 'viewport'
            }).then(function(response){
                $.ajax({
                    url:link,
                    type: "POST",
                    data:{"image": response},
                    success:function(data)
                    {
                        $popover.find(".popover-image-header img").src(response);
                        $modal.modal('hide');
                    },
                    error:function(data){
                        console.log(data)
                        $popover.find(".modal-actions span.error").show();
                    }
                });
            })
        });
    }

    function resetModal(){
        $inputFile.val("")
    }

    initPopoverId();

    // CROPPER OPTS
    var opts = {
        viewport: { width: 100, height: 100 },
        boundary: { width: 300, height: 300 }, // It should fix as the css config
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

    $inputFile.click();

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