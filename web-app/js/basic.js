// PAGE LOADING
function pageLoadingOn (trackLog){
    if (trackLog!= undefined) console.log("LOADING ON :: "+trackLog);
    $('html').addClass('loading');
}
function pageLoadingOff (trackLog){
    if (trackLog!= undefined) console.log("LOADING OFF :: " +trackLog);
    $('html').removeClass('loading');
}

function isPageLoading(){
    return $('html').hasClass('loading');
}

function isUserLogged(){
    return $("header #open-user-options").length > 0;
}


function guid() {
    function s4() {
        return Math.floor((1 + Math.random()) * 0x10000)
            .toString(16)
            .substring(1);
    }
    return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
        s4() + '-' + s4() + s4() + s4();
}