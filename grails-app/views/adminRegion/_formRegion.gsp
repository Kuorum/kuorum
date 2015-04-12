<div class="form-group">
    <formUtil:input command="${command}" field="name"/>
</div>
<div class="form-group">
    <formUtil:input command="${command}" field="iso3166_2"/>
</div>
<div class="form-group">
    <formUtil:selectEnum command="${command}" field="regionType" cssLabel="hidden"/>
</div>
<div class="form-group">
    <formUtil:selectDomainObject command="${command}" field="superRegion" values="${regions}" />
</div>

<div class="form-group">
    <formUtil:dynamicListInput command="${command}" field="postalCodes" cssClass="input-sm" type="number" maxlength="5"/>
</div>
