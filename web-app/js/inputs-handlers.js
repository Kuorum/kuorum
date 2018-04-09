// Input file
(function (window, document) {
    var inputElement = document.getElementById("sign-in-step-5__input-file");

    if (inputElement) {
        inputElement.addEventListener("change", handleFiles, false);
    }

    function handleFiles() {
        var fileList = this.files;
        var reader = new FileReader();

        reader.onload = function (e) {
            var screenElement = document.getElementById('macbook-pro-website-logo');
            var inputLogo = document.getElementById('logo');
            inputLogo.value = fileList[0].name
            screenElement.src = e.target.result;
        };

        reader.readAsDataURL(fileList[0]);
    }

})(window, document);

// Input file preview image
(function (window, document) {
    var inputElement1 = document.getElementById('sign-in-step-5__preview-image-1-input-file');
    var inputElement2 = document.getElementById('sign-in-step-5__preview-image-2-input-file');
    var inputElement3 = document.getElementById('sign-in-step-5__preview-image-3-input-file');

    if (inputElement1) {
        inputElement1.addEventListener("change", handleFiles, false);
    }
    if (inputElement2) {
        inputElement2.addEventListener("change", handleFiles, false);
    }
    if (inputElement3) {
        inputElement3.addEventListener("change", handleFiles, false);
    }

    function handleFiles() {
        var pos = this.getAttribute('data-pos');
        var backgroundPreview = document.getElementsByClassName('macbook-pro-screen-body')[0];
        var fileList = this.files;
        var reader = new FileReader();

        reader.onload = function (e) {
            var previewImage = document.getElementById('preview-image-' + pos);
            previewImage.src = e.target.result;
            backgroundPreview.style.backgroundImage = 'url("' + e.target.result + '")';
            backgroundPreview.style.backgroundSize = 'cover';
        };

        reader.readAsDataURL(fileList[0]);
    }
})(window, document);


// Color picker
(function (window, document) {
    var inputElement = document.getElementById("sign-in-step-5__color-picker-hex-code");

    if (inputElement) {
        var labelElement = inputElement.parentElement;
        var colorPreviewElement = document.getElementById("sign-in-step-5__color-picker-hex-code");
        inputElement.addEventListener("change", handleColor, false);
    }

    function handleColor(ev) {
        var colorHex = '#' + ev.target.value;
        var macbookPreviewButton = document.getElementById('macbook-pro-website-form-input-3');
        this.setAttribute('value', colorHex);
        colorPreviewElement.setAttribute('value', colorHex);
        macbookPreviewButton.style.backgroundColor = colorHex;
    }
})(window, document);

// Inputs (slogan, subtitle)
(function (window, document) {
    var slogan = document.getElementById("slogan");
    var subtitle = document.getElementById("subtitle");

    if (slogan && subtitle) {
        slogan.addEventListener("keydown", handleSlogan, false);
        subtitle.addEventListener("keydown", handleSubtitle, false);
    }

    function handleSlogan(ev) {
        var sloganText = document.getElementById('macbook-pro-website-slogan');
        sloganText.textContent = ev.target.value;
    }

    function handleSubtitle(ev) {
        var subtitle = document.getElementById('macbook-pro-website-subtitle');
        subtitle.textContent = ev.target.value;
    }
})(window, document);