<script>
    var ${validationDataVarName} = ${raw(validationDataVarNameValue)};
    var ${validationDataVarIndex} = ${validationDataVarIndexValue}

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
                                .insertBefore($template);

                <g:each in="${fields}" var="field">
                    // Update the name attributes
                    if ($clone.find('[name="${field}"]').length!= 0){
                        $clone.find('[name="${field}"]').attr('name', '${parentField}[' + ${validationDataVarIndex} + '].${field}').end()
                        // Note that we also pass the validator rules for new field as the third parameter
                        $('#${formId}').find('[name="${parentField}[' + ${validationDataVarIndex} + '].${field}"]').rules('add', ${validationDataVarName}.rules.${field})
                    }
                </g:each>
                prepareForms();
            })

        // Remove button click handler
            .on('click', '.removeButton', function() {
                var $row  = $(this).parents('.dynamic-fieldset'),
                        index = $row.attr('data-dynamic-list-index');

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