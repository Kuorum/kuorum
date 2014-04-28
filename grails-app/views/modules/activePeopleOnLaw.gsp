<section class="boxes follow">
    <h1>Participando en esta ley</h1>
    <div class="kakareo follow">
        <ul class="user-list-followers">
            <g:each in="${users}" var="user">
                <li itemscope itemtype="http://schema.org/Person">
                    <userUtil:showUser user="${user}" showRole="false" showName="false"/>
                    %{--<a href="#" class="popover-trigger" rel="popover" role="button" data-toggle="popover">--}%
                        %{--<img src="images/user.jpg" alt="nombre" class="user-img" itemprop="image">--}%
                    %{--</a>--}%
                </li>
            </g:each>

        </ul><!-- /.user-list-followers -->
    </div>
</section>