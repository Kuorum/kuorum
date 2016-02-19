<section class="panel panel-default causes">
    <div class="panel-heading">
        <h3 class="panel-title">Your top causes </h3>
    </div>
    <div class="panel-body">
        <ul class="causes-tags hide4">
            <g:each in="${supportedCauses}" var="cause">
                <cause:show cause="${cause}"/>
            </g:each>
        </ul>
    </div>
</section>