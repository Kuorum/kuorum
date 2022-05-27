<r:require modules="forms"/>
<div class="box-ppal-section">
    <fieldset aria-live="polite" class="row">
        <div class="form-group col-md-12">
            <formUtil:dynamicListInput command="${command}" field="causes" showLabel="true" autocompleteUrl="${g.createLink(mapping: 'suggestTags', absolute: true)}"/>
        </div>
    </fieldset>
</div>

<div class="box-ppal-section">
    <fieldset aria-live="polite">
        <div class="form-group text-center">
            <button type="submit" class="btn btn-default btn-lg"><g:message code="default.save"/></button>
        </div>
    </fieldset>
</div>