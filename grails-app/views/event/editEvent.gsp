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
                <label for="address" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.event.EventCommand.address.label"/>:</label>
                <div class="col-sm-8 col-md-5">
                    <formUtil:input command="${command}" field="address"/>
                </div>
                <div class="col-sm-8 col-md-1">
                    <a href="#" class="btn btn-grey" id="geocode-address">
                        <span class="fa fa-map-marker" ></span>
                    </a>
                </div>
                <div id="edit-event-map" class="col-md-4"></div>
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

            var map
            var geocoder
            function googleMapsLibraryLoaded(){
                if (document.getElementById('zoom').value >0){
                    initMap();
                    geocodeAddress(geocoder, map);
                    document.getElementById('geocode-address').addEventListener('click', function(e) {
                        e.preventDefault();
                        geocodeAddress(geocoder, map);
                    });
                }else{
                    document.getElementById('geocode-address').addEventListener('click', function(e) {
                        e.preventDefault();
                        initMap();
                        geocodeAddress(geocoder, map);
                    });
                }

            }
            function initMap() {
                var zoom = 6;
                var center = {lat: 40.428054, lng: -3.7043396};
                if (document.getElementById('zoom').value >0){
                    zoom = parseInt(document.getElementById('zoom').value)
                    center = {
                        lat: parseFloat(document.getElementById('latitude').value),
                        lng: parseFloat(document.getElementById('longitude').value)
                    };

                }
                map = new google.maps.Map(document.getElementById('edit-event-map'), {
                    zoom: zoom,
                    center: center,
                    mapTypeControl:false,
                    streetViewControl:false,
                    scaleControl:false
                });
                geocoder = new google.maps.Geocoder();

                map.addListener('zoom_changed', function() {
                    document.getElementById('zoom').value=map.getZoom();
                });

                map.addListener('click', function(e) {
                    placeMarkerAndPanTo(e.latLng, map);
                });

//                document.getElementById('geocode-address').addEventListener('click', function(e) {
//                    e.preventDefault();
//                    geocodeAddress(geocoder, map);
//                });
            }

            function geocodeAddress(geocoder, resultsMap) {
                var address = document.getElementById('address').value;
                geocoder.geocode({'address': address}, function(results, status) {
//                    console.log(results)
                    if (status ===  google.maps.GeocoderStatus.OK) {
                        resultsMap.setCenter(results[0].geometry.location);
                        var marker = new google.maps.Marker({
                            map: resultsMap,
                            position: results[0].geometry.location,
//                            title:"Hello World!"
                        });

                        resultsMap.fitBounds(results[0].geometry.viewport);
                        document.getElementById('zoom').value=resultsMap.getZoom();
                        document.getElementById('latitude').value=results[0].geometry.location.lat();
                        document.getElementById('longitude').value=results[0].geometry.location.lng();

                    } else {
                        alert('Geocode was not successful for the following reason: ' + status);
                    }
                });
            }

            function placeMarkerAndPanTo(latLng, map) {
                var marker = new google.maps.Marker({
                    position: latLng,
                    map: map
                });
                map.panTo(latLng);
            }

        </script>
        <script async defer
                src="https://maps.googleapis.com/maps/api/js?key=${_googleConfig.jsKey}&callback=googleMapsLibraryLoaded">
        </script>
    </div>
</content>