$(function () {
    const url = $("#qr-code").attr("data-qr-url");
    const width = $("#qr-code").attr("data-qr-width");
    const height = $("#qr-code").attr("data-qr-height")
    $('#qr-code').qrcode({width: width,height: height,text: url});
})