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

    $('#${formId}')
        // Add button click handler
            .on('click', '.addButton', function() {
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
            })

        // Remove button click handler
            .on('click', '.removeButton', function() {
                var $row  = $(this).parents('.dynamic-fieldset');
                var index = $row.attr('data-dynamic-list-index');
                console.log($row)
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
            });
</script>