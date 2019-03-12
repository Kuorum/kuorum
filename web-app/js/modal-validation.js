

var userValidatedByDomain={

    executable : undefined,
    binded:false,
    checkUserValid:function(userId, executableFunctionCallback){
        var url = kuorumUrls.profileValidByDomainChecker;
        var data = {};
        executable = executableFunctionCallback;
        $.ajax({
            type: "POST",
            url: url,
            data: data,
            success: function (dataLogin) {
                // Success is 200 code No
                executable.exec()
            },
            error:function(){
                // User is no logged or is not validated
                // Showing modal validation process
                pageLoadingOff();
                $("#domain-validation").modal("show");
                if(!userValidatedByDomain.binded){
                    $("#validateDomain-modal-form-button-id").on("click",userValidatedByDomain.handleSubmitValidationForm );
                    userValidatedByDomain.binded = true
                }
                if (($("#registro").data('bs.modal') || {}).isShown){
                    $("#registro").modal("hide");
                    $('#domain-validation').on('hidden.bs.modal', function () {
                        noLoggedCallbacks.reloadPage()
                    })
                }else if (!isUserLogged()){
                    // User is logged but the page is not reloaded
                    $('#domain-validation').on('hidden.bs.modal', function () {
                        noLoggedCallbacks.reloadPage()
                    })
                }
            },
            complete: function () {
                // pageLoadingOff();
            }
        });
    },

    ExcutableFunctionCallback: function (excutable, params){
        this.exec = function(){
            excutable(params)
        }
    },

    handleSubmitValidationForm:function (e) {
        e.preventDefault();
        var $button = $(this);
        var $form = $button.closest("form");
        var url = $form.attr("action");
        var data = $form.serialize();
        var $loading = $button.siblings(".loading");
        if ($form.valid()){
            $loading.show();
            $button.hide();

            $.ajax({
                type: "POST",
                url: url,
                data: data,
                success: function (data) {
                    // Success is 200 code No
                    console.log(data);
                    if (data.success){
                        $( "#validateDomain-modal-form-button-id").siblings(".text-success").show();
                        setTimeout(function () {
                            executable.exec();
                            $("#domain-validation").modal("hide")
                        }, 1000);
                    }else{
                        $button.show();
                        display.error(data.msg)
                    }
                },
                error:function(){
                    // Wrong user validation
                    display.error("Error validating user")
                },
                complete: function () {
                    $loading.hide()
                    // pageLoadingOff();
                }
            });
        }
    }
};


// function test(args){
//     console.log(args)
// }
// var executable = new userValidatedByDomain.ExcutableFunctionCallback(test, {var1:"hola"})
//
// executable.exec()