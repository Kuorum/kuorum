<input type="hidden" name="politician.id" value="${command.politician.id}"/>
<fieldset class="row">
    <div class="form-group col-md-12">
        <formUtil:dynamicListInput command="${command}" field="causes" showLabel="true" autocompleteUrl="${g.createLink(mapping: 'suggestTags', absolute: true)}"/>
    </div>
</fieldset>
<fieldset>
    <div class="form-group">
        <div class="col-xs-5 col-xs-offset-1">
            <button type="submit" class="btn btn-default"><g:message code="admin.createUser.submit"/></button>
        </div>
    </div>
</fieldset>