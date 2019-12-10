<div class="row">
    <div class="form-group col-md-4">
        <formUtil:input command="${command}" field="name" showLabel="true"/>
    </div>
    <div class="form-group col-md-4">
        <formUtil:input command="${command}" field="surname" showLabel="true"/>
    </div>
</div>
<div class="row">
    <div class="form-group col-md-4">
        <formUtil:input command="${command}" field="email" showLabel="true" disabled="${contact.mongoId}"/>
    </div>
    <div class="form-group col-md-4">
        <formUtil:input command="${command}" field="phone" showLabel="true"/>
    </div>
</div>
<div class="row">
    <div class="form-group col-md-4">
        <label for="city">City</label>
        <input type="text" name="city" class="form-control input-lg" id="city" required placeholder="--" aria-required="true" disabled>
    </div>
    <div class="form-group col-md-4">
        <formUtil:selectEnum
                field="language"
                label="${g.message(code:'tools.contact.import.table.columnOption.language')}"
                command="${command}"
                enumClass="${org.kuorum.rest.model.contact.ContactLanguageRDTO.class}"/>
    </div>
    <div class="col-md-4">
        <input type="submit" value="${g.message(code:'default.save')}" class="btn btn-blue inverted">
    </div>
</div>