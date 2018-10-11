<g:if test="${event}">
    <div class="comment-box call-to-action event-info hidden-sm">
        <div class="comment-header">
            <span class="call-title"><g:message code="event.module.info.title"/></span>
            <span class="call-subTitle"><g:message code="event.module.info.subtitle"/></span>
        </div>
        <div class="comment-proposal clearfix">
            <div class="event-date-time">
                <g:if test="${event.eventDate}">
                    <p class="event-date"><span class="fal fa-clock"></span>
                        <g:formatDate formatName="default.date.format.long"  date="${event.eventDate}" timeZone="${eventUser.timeZone}"/>
                    </p>
                    <p class="event-time">
                        <g:formatDate type="time" style="SHORT"  date="${event.eventDate}" timeZone="${eventUser.timeZone}"/>
                    </p>
                </g:if>
                <g:else>
                    <p> --- </p>
                </g:else>
            </div>
            <div class="event-location">
                <p class="event-local-name"><span class="fal fa-map-marker-alt"></span> ${event.localName}</p>
                <p class="event-address">${event.address}</p>
            </div>
            <g:if test="${event.capacity}">
                <div class="event-capacity">
                    <p class="event-capacity-name"><span class="fal fa-ticket-alt"></span> <span class="event-capacity-number">${event.amountAssistants}</span>/${event.capacity}</p>
                </div>
            </g:if>
            <div class="map" id="map"></div>
            <script>
                function initMap() {
                    var eventLocation = {lat: ${event.latitude}, lng: ${event.longitude}};
                    var map = new google.maps.Map(document.getElementById('map'), {
                        zoom: ${event.zoom},
                        center: eventLocation
                    });
                    var marker = new google.maps.Marker({
                        position: eventLocation,
                        map: map
                    });
                }
            </script>
            <script async defer
                    src="https://maps.googleapis.com/maps/api/js?key=${_googleConfig.jsKey}&callback=initMap">
            </script>
        </div>
    </div>
</g:if>