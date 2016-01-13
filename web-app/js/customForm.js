/**
 * Kuorum scripts for FORMS
 */

$(function(){

})

var formHelper = {
    dirtyFormControl:{
        prepare:function($form){
            $form.find('input[type="submit"]').attr('disabled', 'disabled');
            $form.areYouSure({
                message:i18n.form.warn.leavingEditedForm,
                change: function() {
                    // Enable save button only if the form is dirty. i.e. something to save.
                    if ($(this).hasClass('dirty')) {
                        $(this).find('input[type="submit"]').removeAttr('disabled');
                    } else {
                        $(this).find('input[type="submit"]').attr('disabled', 'disabled');
                    }
                }
            })
        },
        restart:function($form){
            $form.trigger('reinitialize.areYouSure'); //From plugin areYouSure
        },
        dirty:function($form){
            console.log("dirty")
            $form.addClass("dirty")
        }
    }
}