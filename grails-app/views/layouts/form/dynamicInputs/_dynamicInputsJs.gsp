

<div class="modal fade" id="dynamicInputOverflow_${formId}" tabindex="-1" role="dialog" aria-labelledby="dynamicInputOverflow_${formId}" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true" class="fal fa-times-circle fa"></span><span class="sr-only">Cerrar</span></button>
                <h4 class="modal-title"><g:message code="dynamicInput.modal.constraint.maxSize.title" args="[validationDataMaxSize]"/> </h4>
            </div>
            <div class="modal-body clearfix">
                <p><g:message code="dynamicInput.modal.constraint.maxSize.description" args="[validationDataMaxSize]"/></p>
                <div class="form-group btns clearfix">
                    <a href="#" class="btn btn-default pull-right modalDeleteOldest"><g:message code="dynamicInput.modal.constraint.maxSize.addNew"/> </a>
                    <a href="#" class="btn btn-transparent pull-right" data-dismiss="modal"><g:message code="dynamicInput.modal.constraint.maxSize.cancel"/></a>
                </div>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="dynamicRemoveRow_${formId}" tabindex="-1" role="dialog" aria-labelledby="dynamicRemoveRow_${formId}" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true" class="fal fa-times-circle fa"></span><span class="sr-only">Cerrar</span></button>
                <h4 class="modal-title"><g:message code="dynamicInput.modal.remove.row.title" args="[validationDataMaxSize]"/> </h4>
            </div>
            <div class="modal-body clearfix">
                <p><g:message code="dynamicInput.modal.remove.row.description" args="[validationDataMaxSize]"/></p>
                <div class="form-group btns clearfix">
                    <a href="#" class="btn btn-default pull-right modalDeleteRow"><g:message code="dynamicInput.modal.remove.row.delete"/> </a>
                    <a href="#" class="btn btn-transparent pull-right" data-dismiss="modal"><g:message code="dynamicInput.modal.remove.row.cancel"/></a>
                </div>
            </div>
        </div>
    </div>
</div>


<r:script>
    var ${validationDataVarName} = ${raw(validationDataVarNameValue)};
    var ${validationDataVarIndex} = ${validationDataVarIndexValue};
    var removableRow_${formId};
    $(function(){
        <g:each in="${0..validationDataVarIndexValue-1}" var="i">
            <g:each in="${fields}" var="field" >
            // Update the name attributes
            if ($('#${formId}').find('[name="${parentField}[${i}].${field}"]').length!= 0){
                $('#${formId}').find('[name="${parentField}[${i}].${field}"]').attr('name', '${parentField}[${i}].${field}').end();
                // Note that we also pass the validator rules for new field as the third parameter
                var rule = ${validationDataVarName}.rules.${field};
                rule.messages = ${validationDataVarName}.messages.${field};
                $('#${formId}').find('[name="${parentField}[${i}].${field}"]').rules('add', rule)
            }
            </g:each>
        </g:each>
    });

    function addLine_${formId}(){
        var $template = $('#${templateId}');
        var $clone = $template
                        .clone()
                        .removeClass("hide")
                        .removeAttr('id')
                        .attr('data-dynamic-list-index', ${validationDataVarIndex})
                        .css("display","none"); // For the higlight
        var $insertAfter = $template;
        <g:if test="${appendLast}">
            $insertAfter = $template.parent().children(".dynamic-fieldset").last();
        </g:if>
        $clone.insertAfter($insertAfter);
        $clone.toggle( "highlight" );
        var prefix = '${parentField}[' + ${validationDataVarIndex} + ']';
        $clone.attr("data-prefix",prefix)
        if ($clone.find('input').length!= 0){
            $clone.find('input, select').each(function(idx,input){
                var name = $(input).attr("name")
                $(input).attr("name", prefix+'.'+name)
                // Note that we also pass the validator rules for new field as the third parameter
                var rule = ${validationDataVarName}.rules[name];
                if (rule != undefined){
                    rule.messages = ${validationDataVarName}.messages[name];
                    $('#${formId}').find('[name="'+prefix+'.'+name+'"]').rules('add', rule)
                }
            });
        }
        %{--<g:each in="${fields}" var="field">--}%
        %{--// Update the name attributes--}%
        %{--if ($clone.find('[name="${field}"]').length!= 0){--}%
            %{--$clone.find('[name="${field}"]').attr('name', prefix+'.${field}').end();--}%
            %{--// Note that we also pass the validator rules for new field as the third parameter--}%
            %{--var rule = ${validationDataVarName}.rules.${field};--}%
            %{--rule.messages = ${validationDataVarName}.messages.${field};--}%
            %{--$('#${formId}').find('[name="${parentField}[' + ${validationDataVarIndex} + '].${field}"]').rules('add', rule)--}%
        %{--}--}%
        %{--</g:each>--}%
        ${validationDataVarIndex}++;
        formHelper.dirtyFormControl.restart($('#${formId}'));
        formHelper.prepareForms();
    }

    function removeLine_${formId}($row){
        var index = $row.attr('data-dynamic-list-index');
        // Remove fields
        // Remove fields
        <g:each in="${fields}" var="field">
        // Update the name attributes
        if ($('#${formId}').find('[name="${parentField}[' + index + '].${field}"]').length!= 0){
            $('#${formId}').find('[name="${parentField}[' + index + '].${field}"]').rules('remove');
        }
        </g:each>
        // Remove element containing the fields
        $row.remove();
        //Set form as dirty
        formHelper.dirtyFormControl.dirty($('#${formId}'))
    }

    $('#${formId}')
        // Add button click handler
            .on('click', '.addButton', function(e) {
                e.preventDefault();
                var numElementsOnList = $('#${formId} div.dynamic-fieldset').length -1;
                if (numElementsOnList >=  ${validationDataMaxSize}){
                    $("#dynamicInputOverflow_${formId}").modal("show")
                }else{
                    addLine_${formId}()
                }
            })

        // Remove button click handler
            .on('click', '.removeButton', function() {
                var $row  = $(this).parents('.dynamic-fieldset');
                removableRow_${formId} = $row;
                $("#dynamicRemoveRow_${formId}").modal("show")
            })
            .on('click', '.modalDeleteRow', function() {
                $("#dynamicRemoveRow_${formId}").modal("hide");
                var $row  = removableRow_${formId}
                removeLine_${formId}($row)
            })
            .on('click', '.modalDeleteOldest', function(e){
                e.preventDefault();
                $(this).parents(".modal").modal("hide");
                var i = 0;
                var element;
                while (!element || element.length==0) {
                    //Finding the element with the low index -> The latest
                    element = $('#${formId} div[data-dynamic-list-index="'+i+'"]');
                    i++;
                }
                removeLine_${formId}(element);
                addLine_${formId}();

            });
</r:script>

<g:if test="${request.isXhr()}">
    <r:layoutResources disposition="defer"/>
</g:if>
