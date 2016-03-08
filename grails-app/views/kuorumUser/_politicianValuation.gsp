<!-- politician valuation -->
<form class="popover-trigger rating" data-trigger="manual" rel="popover" role="button" data-toggle="popover">
    <fieldset class="rating">
        <legend class="sr-only">Please rate:</legend>
        <input type="radio" id="star5" name="rating" value="5">
        <label for="star5">5 stars <span class="rate-message">Me gusta mucho</span></label>

        <input type="radio" id="star4" name="rating" value="4">
        <label for="star4">4 stars <span class="rate-message">Me gusta bastante</span></label>

        <input type="radio" id="star3" name="rating" value="3">
        <label for="star3">3 stars <span class="rate-message">No está mal.</span></label>

        <input type="radio" id="star2" name="rating" value="2" checked>
        <label for="star2">2 stars <span class="rate-message">No me gusta mucho</span></label>

        <input type="radio" id="star1" name="rating" value="1">
        <label for="star1">1 star <span class="rate-message">No me gusta nada</span></label>
    </fieldset>
</form>
<!-- POPOVER PARA VOTACIÓN -->
<div class="popover">
    <div class="rating-over">
        <!-- indicar la clase de la puntuación: one, two, three, four, five -->
        <div class="rate-number two"><span class="sr-only">Average: </span> 2 </div>
        <span class="sr-only">Distribution:</span>
        <ul>
            <li><span class="star">5 <span>★</span></span></li>
            <li><span class="star">4 <span>★</span></span></li>
            <li>
                <span class="star">3 <span>★</span></span>
                <div class="progress">
                    <div class="progress-bar" role="progressbar" aria-valuetransitiongoal="10" aria-valuemin="0" aria-valuemax="100"><span class="sr-only">10%</span></div>
                </div>
            </li>
            <li>
                <span class="star">2 <span>★</span></span>
                <div class="progress">
                    <div class="progress-bar" role="progressbar" aria-valuetransitiongoal="80" aria-valuemin="0" aria-valuemax="100"><span class="sr-only">80%</span></div>
                </div>
            </li>
            <li>
                <span class="star">1 <span>★</span></span>
                <div class="progress">
                    <div class="progress-bar" role="progressbar" aria-valuetransitiongoal="50" aria-valuemin="0" aria-valuemax="100"><span class="sr-only">50%</span></div>
                </div>
            </li>
        </ul>
    </div>
</div>