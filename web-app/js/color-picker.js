(function (window, document) {
    var inputElement = document.getElementById("sign-in-step-5__color-picker");
    var labelElement = inputElement.parentElement;
    var colorPreviewElement = document.getElementById("sign-in-step-5__color-picker-hex-code");
    inputElement.addEventListener("change", handleColor, false);

    function handleColor(ev) {
        var colorHex = ev.target.value;
        var macbookPreviewButton = document.getElementById('macbook-pro-website-form-input-3');
        this.setAttribute('value', colorHex);
        colorPreviewElement.setAttribute('value', colorHex);
        labelElement.style.backgroundColor = colorHex;
        macbookPreviewButton.style.backgroundColor = colorHex;
    }
})(window, document);