<fieldset class="form-group new-filter-options first">
    <label for="matches" class="col-sm-2 col-md-1 control-label">Matches</label>
    <div class="col-sm-4 col-md-3">
        <select name="matches" class="form-control input-lg" id="matches">
            <option value="">Any</option>
            <option value="">All</option>
        </select>
    </div>
    <div class="col-sm-5">
        of the following conditions:
    </div>
</fieldset>
<fieldset class="form-group new-filter-options">
    <div class="col-sm-2 col-sm-offset-2 col-md-2 col-md-offset-1">
        <a href="#" role="button" class="minus-condition"><span class="fa fa-minus-circle fa-lg"></span> <span class="text">Delete condition</span></a>
        <label for="condition1What" class="sr-only">Choose...</label>
        <select name="condition1What" class="form-control input-lg" id="condition1What">
            <option value="">Tag</option>
            <option value="">Opción 2</option>
        </select>
    </div>
    <div class="col-sm-3">
        <label for="condition1How" class="sr-only">Choose...</label>
        <select name="condition1How" class="form-control input-lg" id="condition1How">
            <option value="">Contains</option>
            <option value="">Opción 2</option>
        </select>
    </div>
    <div class="col-sm-4">
        <label for="condition1Text" class="sr-only">Type your condition</label>
        <input class="form-control" type="text" id="condition1Text" name="condition1Text">
    </div>
</fieldset>
<fieldset class="form-group new-filter-options">
    <div class="col-sm-2 col-sm-offset-2 col-md-2 col-md-offset-1">
        <a href="#" role="button" class="minus-condition"><span class="fa fa-minus-circle fa-lg"></span> <span class="text">Delete condition</span></a>
        <label for="condition2What" class="sr-only">Choose...</label>
        <select name="condition2What" class="form-control input-lg" id="condition2What">
            <option value="">Tag</option>
            <option value="">Opción 2</option>
        </select>
    </div>
    <div class="col-sm-3">
        <label for="condition2How" class="sr-only">Choose...</label>
        <select name="condition2How" class="form-control input-lg" id="condition2How">
            <option value="">Contains</option>
            <option value="">Opción 2</option>
        </select>
    </div>
    <div class="col-sm-4">
        <label for="condition2Text" class="sr-only">Type your condition</label>
        <input class="form-control" type="text" id="condition2Text" name="condition2Text">
    </div>
</fieldset>
<fieldset class="form-group new-filter-options last">
    <div class="col-sm-2 col-sm-offset-2 col-md-2 col-md-offset-1">
        <a href="#" role="button" class="plus-condition"><span class="fa fa-plus-circle fa-lg"></span> <span class="text">Add</span></a>
    </div>
</fieldset>
<fieldset class="new-filter-actions">
    <a href="#" id="numberRecipients">10 recipients</a>
    <a href="#" role="button" class="btn btn-blue inverted" id="refreshFilter">Refresh filter</a>
    <a href="#" role="button" class="btn btn-blue inverted" id="saveFilter">Save filter</a>
    <a href="#" role="button" class="btn btn-blue inverted" id="saveFilterAs">Save filter as</a>
</fieldset>