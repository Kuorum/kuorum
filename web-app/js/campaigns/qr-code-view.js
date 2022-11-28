$(function () {
    const url = $('.qr-text-container a').attr("href");
    $('#qr-code').qrcode(url);
})