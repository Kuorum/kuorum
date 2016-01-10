

<div class="modal fade" id="dynamicInputOverflow" tabindex="-1" role="dialog" aria-labelledby="dynamicInputOverflow" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                <h4 class="modal-title" id="apadrinarPropuesta">Apadrinar esta propuesta</h4>
            </div>
            <div class="modal-body clearfix">
                <p>Al apadrinar esta propuesta te estas comprometiendo a defenderla y, si es posible, llevarla a cabo. Si el autor de la propuesta considera que has cumplido tu promesa obtendrás una victoria.</p>
                <div class="form-group btns clearfix">
                    <a href="#" class="btn btn-default pull-right modalDeleteOldest">Delete oldest</a>
                    <a href="#" class="btn btn-transparent pull-right" data-dismiss="modal">Cancel</a>
                </div>
            </div>
        </div>
    </div>
</div>


<script>
    var ${validationDataVarName} = ${raw(validationDataVarNameValue)};
    var ${validationDataVarIndex} = ${validationDataVarIndexValue}
    $(function(){
        <g:each in="${0..validationDataVarIndexValue-1}" var="i">
            <g:each in="${fields}" var="field" >
            // Update the name attributes
            if ($('#${formId}').find('[name="${parentField}[${i}].${field}"]').length!= 0){
                $('#${formId}').find('[name="${parentField}[${i}].${field}"]').attr('name', '${parentField}[${i}].${field}').end()
                // Note that we also pass the validator rules for new field as the third parameter
                var rule = ${validationDataVarName}.rules.${field};
                rule.messages = ${validationDataVarName}.messages.${field};
                $('#${formId}').find('[name="${parentField}[${i}].${field}"]').rules('add', rule)
            }
            </g:each>
        </g:each>
    })

    function addLine_${formId}(){
        ${validationDataVarIndex}++;
        var $template = $('#${templateId}'),
                $clone    = $template
                        .clone()
                        .removeClass('hide')
                        .removeAttr('id')
                        .attr('data-dynamic-list-index', ${validationDataVarIndex})
                        .insertAfter($template);

        <g:each in="${fields}" var="field">
        // Update the name attributes
        if ($clone.find('[name="${field}"]').length!= 0){
            $clone.find('[name="${field}"]').attr('name', '${parentField}[' + ${validationDataVarIndex} + '].${field}').end()
            // Note that we also pass the validator rules for new field as the third parameter
            var rule = ${validationDataVarName}.rules.${field};
            rule.messages = ${validationDataVarName}.messages.${field};
            $('#${formId}').find('[name="${parentField}[' + ${validationDataVarIndex} + '].${field}"]').rules('add', rule)
        }
        </g:each>
        prepareForms();
    }

    $('#${formId}')
        // Add button click handler
            .on('click', '.addButton', function() {
                var numElementsOnList = $('#relevantEventsForm div.dynamic-fieldset').length -1
                if (numElementsOnList >=  ${validationDataMaxSize}){
                    $("#dynamicInputOverflow").modal("show")
                }else{
                    addLine_${formId}()
                }
            })

        // Remove button click handler
            .on('click', '.removeButton', function() {
                var $row  = $(this).parents('.dynamic-fieldset');
                var index = $row.attr('data-dynamic-list-index');
                // Remove fields
                // Remove fields
                <g:each in="${fields}" var="field">
                // Update the name attributes
                console.log('[name="${parentField}[' + index + '].${field}"]')
                if ($('#${formId}').find('[name="${parentField}[' + index + '].${field}"]').length!= 0){
                    $('#${formId}').find('[name="${parentField}[' + index + '].${field}"]').rules('remove');
                }
                </g:each>
                // Remove element containing the fields
                $row.remove();
            })
            .on('click', '.modalDeleteOldest', function(e){
                e.preventDefault()
                $(this).parents(".modal").modal("hide")
                var i = 0
                var element
                while (!element || element.length==0) {
                    //Finding the element with the low index -> The latest
                    element = $('#${formId} div[data-dynamic-list-index="'+i+'"]');
                    i++;
                }
                element.find('.removeButton').click()
                addLine_${formId}();

            })
</script>
