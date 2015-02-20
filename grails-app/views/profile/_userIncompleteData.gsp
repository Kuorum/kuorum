<g:if test="${(!errors) && (orderedNotice != null)}">
    <aside class="box-ppal condition clearfix">
    <g:if test="${orderedNotice != message(code:'dashboard.warningsUserProfile.FOLLOWPOLITICIANS')}">
        <button class="close" type="button"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">eliminar de la lista</span></button>
    </g:if>

        <g:if test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPROVINCE')}">
            <h1>${message(code:'dashboard.userProfile.incompleteDate.fillTheProvince.mainTitle')}</h1>
        </g:if>
        <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPOLITICIANPHONE')}">
            <h1>${message(code:'dashboard.userProfile.incompleteDate.fillThePhone.mainTitle')}</h1>
        </g:elseif>
        <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPOLITICIANINYOURCOUNTRY')}">
            <h1>${message(code:'dashboard.userProfile.incompleteDate.fillAPoliticianInYourCountry.mainTitle')}</h1>
        </g:elseif>
        <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOAGEANDGENDER')}">
            <h1>${message(code:'dashboard.userProfile.incompleteDate.fillYourAgeAndGender.mainTitle')}</h1>
        </g:elseif>
        <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.FOLLOWPOLITICIANS')}">
            <h1>${message(code:'dashboard.userProfile.incompleteDate.notFollowingPoliticians.mainTitle')}</h1>
        </g:elseif>

        <g:if test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPOLITICIANINYOURCOUNTRY') || orderedNotice == message(code:'dashboard.warningsUserProfile.FOLLOWPOLITICIANS') }">
            <g:if test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPOLITICIANINYOURCOUNTRY')}">
                <h2>${message(code:'dashboard.userProfile.incompleteDate.fillPAPoliticianInYourCountry.leftTitle')}</h2>
            </g:if>
            <g:if test="${orderedNotice == message(code:'dashboard.warningsUserProfile.FOLLOWPOLITICIANS')}">
                <h2>${message(code:'dashboard.userProfile.incompleteDate.notFollowingPoliticians.leftTitle')}</h2>
                %{--//TODO por hacer esta parte de los usuarios--}%
                <ul class="user-list-followers">
                    <li class="user" itemscope itemtype="http://schema.org/Person">
                    </li>
                </ul>
            </g:if>
        </g:if>
        <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPROVINCE') || orderedNotice == message(code:'dashboard.warningsUserProfile.NOPOLITICIANPHONE') || orderedNotice == message(code:'dashboard.warningsUserProfile.NOAGEANDGENDER')}">
            <div class="row">
                <div class="col-md-6 col-lg-7">
                    <g:if test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPROVINCE')}">
                        <h2>${message(code:'dashboard.userProfile.incompleteDate.fillTheProvince.leftTitle')}</h2>
                    </g:if>
                    <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPOLITICIANPHONE')}">
                        <h1>${message(code:'dashboard.userProfile.incompleteDate.fillThePhone.leftTitle')}</h1>
                    </g:elseif>
                    <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOAGEANDGENDER')}">
                        <h1>${message(code:'dashboard.userProfile.incompleteDate.fillYourAgeAndGender.leftTitle')}</h1>
                    </g:elseif>
                </div>
                <div class="col-md-6 col-lg-5">
                    <g:if test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPROVINCE')}">
                        <p>${message(code:'dashboard.userProfile.incompleteDate.fillTheProvince.rightTitle')}</p>
                        <form action="#" method="post" name="sign" id="sign" role="form" class="no-postalcode">
                            <div class="form-group pull-left">
                                <label for="pais" class="sr-only">País</label>
                                <select name="pais" class="form-control input-lg" id="pais">
                                    <option value="Elige tu país">País</option>
                                    <option value="Alemania">Alemania</option>
                                    <option value="Austria">Austria</option>
                                    <option>...</option>
                                </select>
                            </div>
                                <div class="form-group pull-left">
                                    <label for="codigo-postal" class="sr-only">Código postal</label>
                                    <input type="number" name="codpostal" class="form-control input-lg" id="codpostal" required placeholder="Código postal" aria-required="true">
                                </div>
                                <div class="form-group">
                                    <input type="submit" value="Guardar" class="btn btn-grey btn-lg">
                                </div>
                        </form>
                    </g:if>
                    <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPOLITICIANPHONE')}">
                        <p>${message(code:'dashboard.userProfile.incompleteDate.fillThePhone.rightTitle')}</p>
                        <form action="#" method="post" name="sign" id="sign" role="form" class="no-phone">
                            <div class="form-group pull-left">
                                <label for="phone-prefix" class="sr-only">Prefijo teléfono</label>
                                <select name="phone-prefix" class="form-control input-lg" id="phone-prefix">
                                    <option value="+34">+34</option>
                                    <option value="+32">+32</option>
                                    <option value="+33">+33</option>
                                    <option>...</option>
                                </select>
                            </div>
                            <div class="form-group pull-left">
                                <label for="phone" class="sr-only">Número de teléfono</label>
                                <input type="number" name="phone" class="form-control input-lg" id="phone" required placeholder="Número de teléfono" aria-required="true">
                            </div>
                            <div class="form-group">
                                <input type="submit" value="Guardar" class="btn btn-grey btn-lg">
                            </div>
                        </form>
                    </g:elseif>
                    <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOAGEANDGENDER')}">
                        <form action="#" method="post" name="sign" id="sign" role="form" class="no-age">
                            <div class="form-group groupRadio">
                                <label class="radio-inline"><input type="radio" name="gender" value="MALE">Hombre</label>
                                <label class="radio-inline"><input type="radio" name="gender" value="FEMALE">Mujer</label>
                                <label class="radio-inline"><input type="radio" name="gender" value="ORGANIZATION">Organización</label>
                            </div>
                            <div class="form-group pull-left">
                                <label for="nacimiento" class="sr-only">Año de nacimiento</label>
                                <select name="nacimiento" class="form-control input-lg" id="nacimiento">
                                    <option value="Elige año de nacimiento">Año de nacimiento</option>
                                    <option value="1910">1910</option>
                                    <option value="1911">1911</option>
                                    <option>...</option>
                                </select>
                            </div>
                            <div class="form-group pull-left">
                                <input type="submit" value="Guardar" class="btn btn-grey btn-lg">
                            </div>
                        </form>
                    </g:elseif>
                </div>
            </div>
        </g:elseif>

        <div class="photo">
            <g:if test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPROVINCE')}"> <img alt="" src="images/img-nopostalcode.jpg"> </g:if>
            <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPOLITICIANPHONE')}"> <img alt="" src="images/img-nophone.jpg"></g:elseif>
            <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOPOLITICIANINYOURCOUNTRY')}"> <img alt="" src="images/img-nopolitician.jpg"></g:elseif>
            <g:elseif test="${orderedNotice == message(code:'dashboard.warningsUserProfile.NOAGEANDGENDER')}"> <img alt="" src="images/img-noage.jpg"></g:elseif>
        </div>
    </aside>
</g:if>

<g:elseif test="${errors}">
    <script>
        $(function(){
            display.error('${orderedNotice}')
        })
    </script>
</g:elseif>

