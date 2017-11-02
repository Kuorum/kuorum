<section class="boxes follow">
    <h1>Participando en esta ley</h1>
    <div class="kakareo follow">
        <ul class="user-list-followers">
            <g:each in="${users}" var="user">
                <userUtil:showUser user="${user}" showName="false" htmlWrapper="li"/>
            </g:each>
        </ul><!-- /.user-list-followers -->
    </div>
</section>