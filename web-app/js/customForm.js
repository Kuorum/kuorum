/**
 * Kuorum scripts for FORMS
 */

$(function(){

})

var formHelper = {
    dirtyFormControl:{
        prepare:function($form){
            $form.find('[type="submit"]').attr('disabled', 'disabled');
            $form.areYouSure({
                message:i18n.form.warn.leavingEditedForm,
                addRemoveFieldsMarksDirty:true,
                change: function() {
                    // Enable save button only if the form is dirty. i.e. something to save.
                    if ($(this).hasClass('dirty')) {
                        $(this).find('[type="submit"]').removeAttr('disabled');
                    } else {
                        $(this).find('[type="submit"]').attr('disabled', 'disabled');
                    }
                }
            })
        },
        restart:function($form){
            $form.trigger('rescan.areYouSure'); //From plugin areYouSure
        },
        dirty:function($form){
            $form.addClass("dirty")
            $form.trigger('checkform.areYouSure');
            $form.find('[type="submit"]').removeAttr('disabled');
        }
    }
}