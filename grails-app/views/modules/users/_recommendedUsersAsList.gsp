<ul class="user-list-followers">
    <g:each in="${users}" var="user">
        <li itemtype="http://schema.org/Person" itemscope class="user">
            <userUtil:showUser user="${user}" showName="true" showRole="true" showActions="true"/>
        </li>
    </g:each>
</ul>