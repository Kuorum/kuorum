$(function () {
    const url = $('.qr-text-container a').attr("href");
    const width = $("#qr-code").attr("data-qr-width");
    const height = $("#qr-code").attr("data-qr-height")
    $('#qr-code').qrcode({width: width,height: height,text: url});
})