<!-- LISTADO DE CONTACTOS -->
<div class="box-ppal list-contacts">
    <div id="contactsOrderOptions" class="box-order-options clearfix">
        <span>Order by:</span>
        <ul>
            <!-- añadir la clase "active asc" o "active desc" al <a> -->
            <li><a href="#" role="button" class="sort active asc" data-sort="timestamp">Name</a></li>
            <li><a href="#" role="button" class="sort" data-sort="title">Email</a></li>
            <li><a href="#" role="button" class="sort" data-sort="recip-number">Followers</a></li>
            <li><a href="#" role="button" class="sort" data-sort="open-number">Engagement</a></li>
        </ul>
        <div class="pag-list-contacts">
            <ul class="paginationTop">
                <li class="active">
                    <a class="page" href="#">1</a>
                </li>
                <li>
                    <a class="page" href="#">2</a>
                </li>
                <li class="disabled">
                    <a class="page" href="#">...</a>
                </li>
                <li>
                    <a class="page" href="#">4</a>
                </li>
            </ul>
            <span class="counterList">Total of <span class="totalList">33</span></span>
        </div>
    </div>
    <ul id="contactsList" class="list">
        <g:each in="${contacts.data}" var="contact">
            <g:render template="/contacts/liContact" model="[contact:contact]"/>
        </g:each>
    </ul>

    <div class="pag-list-contacts clearfix">
        <ul class="paginationBottom">
            <li class="active">
                <a class="page" href="#">1</a>
            </li>
            <li>
                <a class="page" href="#">2</a>
            </li>
            <li class="disabled">
                <a class="page" href="#">...</a>
            </li>
            <li>
                <a class="page" href="#">4</a>
            </li>
        </ul>
        <span class="counterList">Total of <span class="totalList">33</span></span>
    </div>
</div>