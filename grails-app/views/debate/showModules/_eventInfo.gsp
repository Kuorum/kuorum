
<div class="comment-box call-to-action event-info hidden-sm">
    <div class="comment-header">
        <span class="call-title">Event information</span>
        <span class="call-subTitle">Don't forget the time and palce of this event.</span>
    </div>
    <div class="comment-proposal clearfix">
        <div class="event-date-time">
            <p class="event-date"><span class="fa fa-clock-o"></span>
                <g:formatDate type="date" style="FULL"  date="${eventData.dateTime}"/>
            </p>
            <p class="event-time">
                <g:formatDate type="time" style="SHORT"  date="${eventData.dateTime}"/>
            </p>
        </div>
        <div class="event-location">
            <p class="event-local-name"><span class="fa fa-map-marker"></span> ${eventData.localName}</p>
            <p class="event-address">${eventData.address}</p>
        </div>
        <div class="map" id="map"></div>
        <script>
            function initMap() {
                var eventLocation = {lat: ${eventData.latitude}, lng: ${eventData.longitude}};
                var map = new google.maps.Map(document.getElementById('map'), {
                    zoom: ${eventData.zoom},
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