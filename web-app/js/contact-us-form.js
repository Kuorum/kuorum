
$(function(){
   $("#request-demo-form button[type=submit]").on("click", function(e){
      e.preventDefault();
       var $form = $(this).parents("form");
       if ($form.valid()){
           var url = $form.attr("action")
           $.ajax({
               url:url,
               data:$form.serializeArray(),
               success:function(data){
                   display.success(data);
                   $form.trigger('reset')
               }
           })
       }
   });
});