<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">
        <g:message code="head.logged.account.tools.massMailing.edit" args="[campaign.name]"/>
    </g:set>

    <title>${breadCrumbName}</title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
    <r:require modules="datepicker, postForm, debateForm" />
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianCampaigns"><g:message code="head.logged.account.tools.massMailing"/></g:link></li>
        <li><g:link mapping="politicianCampaignsNew"><g:message code="tools.campaign.new.title"/></g:link></li>
        <li class="active">${breadCrumbName}</li>
    </ol>

    <div class="box-steps container-fluid choose-campaign">
        <g:set var="mappings" value="${
            [step:'event',
             next:'postEditContent',
             settings:'postEdit',
             event:'postEditEvent',
             content:'postEditContent',
             showResult: 'postShow']}"/>
        <g:if test="${campaign instanceof org.kuorum.rest.model.communication.debate.DebateRSDTO}">
            <g:set var="mappings" value="${
                [step:'event',
                 next:'debateEditContent',
                 settings:'debateEdit',
                 event:'debateEditEvent',
                 content:'debateEditContent',
                 showResult: 'debateShow']}"/>
        </g:if>
        <g:render template="/campaigns/steps/twoSteps" model="[mappings: mappings, attachEvent:true]"/>
    </div>

    <div class="box-ppal campaign-new">
        <h1 class="sr-only"><g:message code="admin.createDebate.title"/></h1>

        <formUtil:validateForm bean="${command}" form="politicianMassMailingForm" dirtyControl="true"/>
        <form action="#" class="form-horizontal" id="politicianMassMailingForm" method="POST" data-generalErrorMessage="${g.message(code:'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
            <input type="hidden" name="sendType" value="DRAFT" id="sendMassMailingType"/>
            <input type="hidden" name="redirectLink" id="redirectLink"/>
            %{--<input type="hidden" name="latitude" id="latitude" value="${g.formatNumber(number:command.latitude, type:'number', format:'$##.##########')}"/>--}%
            <input type="hidden" name="latitude" id="latitude" value="${command.latitude}"/>
            <input type="hidden" name="longitude" id="longitude" value="${command.longitude}"/>
            <input type="hidden" name="zoom" id="zoom" value="${command.zoom}"/>

            <fieldset class="form-group">
                <label for="eventDate" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.event.EventCommand.eventDate.label"/>:</label>
                <div class="col-sm-8 col-md-5">
                    <formUtil:date command="${command}" field="eventDate" time="true"/>
                </div>
            </fieldset>

            <fieldset class="form-group">
                <label for="localName" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.event.EventCommand.localName.label"/>:</label>
                <div class="col-sm-8 col-md-5">
                    <formUtil:input command="${command}" field="localName"/>
                </div>
            </fieldset>
            <fieldset class="form-group">
                <label for="capacity" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.event.EventCommand.capacity.label"/>:</label>
                <div class="col-sm-8 col-md-5">
                    <formUtil:input command="${command}" field="capacity" type="number"/>
                </div>
            </fieldset>
            <fieldset class="form-group map-location">
                <label for="address" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.event.EventCommand.address.label"/>:</label>
                <div class="col-sm-8 col-md-5">
                    <formUtil:input command="${command}" field="address"/>
                    <a href="#" class="btn btn-grey" id="geocode-address">
                        <span class="fa fa-map-marker" ></span>
                    </a>
                </div>
                <div id="edit-event-map" class="col-sm-offset-2 col-sm-8 col-md-offset-0 col-md-4"></div>
            </fieldset>
            <fieldset class="buttons">
                <div class="text-right">
                    <ul class="form-final-options">
                        <li>
                            <a href="#" id="save-draft-debate" data-redirectLink="politicianCampaigns">
                                <g:message code="tools.massMailing.saveDraft"/>
                            </a>
                        </li>
                        <li><a href="#" class="btn btn-blue inverted" id="next" data-redirectLink="${mappings.next}"><g:message code="tools.massMailing.next"/></a></li>
                    </ul>
                </div>
            </fieldset>
        </form>
        <script>

            function KuorumGoogleMapEditEvent(){
                var map
                var geocoder
                var marker
                var mapDivContainerId="edit-event-map"
                var geocoderButtonId='geocode-address'
                var zoom = 1;
                var center = {lat: 40.428054, lng: -3.7043396};
                var that = this;

                var placeSearch, autocomplete;
                var inputAddressId="address"
                var componentForm = {
                    street_number: 'short_name',
                    route: 'long_name',
                    locality: 'long_name',
                    administrative_area_level_1: 'short_name',
                    country: 'long_name',
                    postal_code: 'short_name'
                };


                document.getElementById(inputAddressId).addEventListener("focusout", function(e){
                    if (document.getElementById('zoom').value==""){
                        that.geocodeAddress(geocoder, map);
                    }
                });
                document.getElementById(inputAddressId).addEventListener("input", function(e){
                    document.getElementById('zoom').value=""

                });

                this.initMap = function() {
                    if (map != undefined){
                        console.log("map alredy defined");
                        return;
                    }
                    map = new google.maps.Map(document.getElementById(mapDivContainerId), {
                        zoom: zoom,
                        center: center,
                        mapTypeControl:false,
                        streetViewControl:false,
                        scaleControl:false
                    });
                    geocoder = new google.maps.Geocoder();

                    autocomplete = new google.maps.places.Autocomplete(
                        (document.getElementById(inputAddressId)),
                        {types: ['geocode']});

                    // When the user selects an address from the dropdown, populate the address
                    // fields in the form.
                    autocomplete.addListener('place_changed', that.fillInAddress);

                    map.addListener('zoom_changed', function() {
                        document.getElementById('zoom').value=map.getZoom();
                    });

//                    map.addListener('click', function(e) {
//                        that.placeMarkerAndPanTo(e.latLng);
//                    });
                }
                document.getElementById(geocoderButtonId).addEventListener('click', function(e) {
                    e.preventDefault();
                    that.geocodeAddress(geocoder, map);
                });
                this.fillInAddress = function() {
                    // Get the place details from the autocomplete object.
                    var place = autocomplete.getPlace();

                    that.removeMarker();
                    map.setCenter(place.geometry.location);
                    marker = new google.maps.Marker({
                        map: map,
                        position: place.geometry.location,
//                            title:"Hello World!"
                    });

                    map.fitBounds(place.geometry.viewport);

                    document.getElementById('zoom').value=map.getZoom();
                    document.getElementById('latitude').value=place.geometry.location.lat();
                    document.getElementById('longitude').value=place.geometry.location.lng();

                }
                this.geocodeAddress= function () {
                    var address = document.getElementById('address').value;
                    geocoder.geocode({'address': address}, function(results, status) {
//                    console.log(results)
                        if (status ===  google.maps.GeocoderStatus.OK) {
                            that.removeMarker();
                            map.setCenter(results[0].geometry.location);
                            marker = new google.maps.Marker({
                                map: map,
                                position: results[0].geometry.location,
//                            title:"Hello World!"
                            });

                            map.fitBounds(results[0].geometry.viewport);
                            document.getElementById('zoom').value=map.getZoom();
                            document.getElementById('latitude').value=results[0].geometry.location.lat();
                            document.getElementById('longitude').value=results[0].geometry.location.lng();
                        } else {
                            display.warn("${g.message(code:'tools.massMailing.event.location.error')}")
                            that.removeMarker();
                            document.getElementById('zoom').value=''
                            document.getElementById('latitude').value=''
                            document.getElementById('longitude').value=''
                        }
                    });
                }

                this.removeMarker = function(){
                    if (marker != undefined){
                        marker.setMap(null)
                    }
                }

                this.placeMarkerAndPanTo = function(latLng) {
                    var marker = new google.maps.Marker({
                        position: latLng,
                        map: that.map
                    });
                    that.map.panTo(latLng);
                }
                this.show = function(){
                    document.getElementById(mapDivContainerId).removeAttribute("style");
                }
                // INIT MAP
                this.initMap()
                if (document.getElementById('zoom').value >0){
                    zoom = parseInt(document.getElementById('zoom').value)
                    center = {
                        lat: parseFloat(document.getElementById('latitude').value),
                        lng: parseFloat(document.getElementById('longitude').value)
                    };
//                    this.geocodeAddress()
                    map.setCenter(center);
                    map.setZoom(zoom);
                    var marker = new google.maps.Marker({
                        position: center,
                        map: map
                    });
                    that.show();
                }
            }

            var kuorumGoogleMapEditEvent
            function googleMapsLibraryLoaded(){
                kuorumGoogleMapEditEvent = new KuorumGoogleMapEditEvent()
            }

        </script>
        <g:set var="currentLang" value="${org.springframework.web.servlet.support.RequestContextUtils.getLocale(request)}" />
        <script async defer
                src="https://maps.googleapis.com/maps/api/js?key=${_googleConfig.jsKey}&callback=googleMapsLibraryLoaded&libraries=places&language=${currentLang.language}">
        </script>
    </div>
</content>