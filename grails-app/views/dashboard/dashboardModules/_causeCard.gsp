<%@ page import="kuorum.core.model.UserType" %>

<article itemtype="http://schema.org/Article" itemscope role="article" class="box-ppal clearfix" id="cause-${cause.name.encodeAsKuorumUrl()}">
    <g:link mapping="causeDiscard" params="${cause.encodeAsLinkProperties()}" type="button" class="close">
        <span class="fa fa-times-circle-o fa"></span>
        <span class="sr-only"><g:message code="cause.card.discard.text" args="[cause.name]"/></span>
    </g:link>
    <div class="causes-tags">
        <div class="cause">
            <div class="link-wrapper">
                <g:link mapping="searcherSearch" params="[type:UserType.POLITICIAN, word:cause.name]" class="cause-name"><span class="fa fa-tag"></span>${cause.name}</g:link>
            </div>
        </div>
    </div>
    <g:if  test="${mainPolitician}">
        <div class="user author" itemprop="author" itemscope="" itemtype="http://schema.org/Person">
            <userUtil:showUser user="${mainPolitician}"/>
        </div>
    </g:if>
    <div class="cause-footer clearfix">
        <div class="politicians-counter">
            <a class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                <span class="fa fa-user"></span> <span><g:message code="cause.card.support.politicians.text" args="[total]"/></span>
            </a>
            <!-- POPOVER PARA SACAR LISTAS DE POLÍTICOS -->
            %{--<userUtil:showListUsers users="${politicianCauses.get(cause.name)}" visibleUsers="0"/>--}%

            <div class="popover">
                <a href="#" class="hidden" rel="nofollow"><g:message code="cause.card.support.politicians.list.show"/> </a>
                <button type="button" class="close" aria-hidden="true" data-dismiss="popover"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">cerrar</span></button>
                <div class="popover-user-list">
                    <p><g:message code="cause.card.support.politicians.list.title"/> </p>
                    <div class="scroll">
                        <ul>
                            <g:render template="/kuorumUser/embebedUsersList" model="[users:politcians]"/>
                        </ul>
                    </div><!-- /.contenedor scroll -->
                </div><!-- /popover-user-list -->
            </div>
        </div>
        <div class="cause">
            <g:link mapping="causeSupport" params="${cause.encodeAsLinkProperties()}" class="cause-support" role="button" aria-pressed="false">
                <span class="fa fa-heart"></span>
                <span class="fa fa-heart-o"></span>
            </g:link>
            <a href="#" class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                <span class="sr-only"><g:message code="cause.card.support.citizens.text"/></span>
                <span class="cause-counter">${cause.citizenSupports}</span>
            </a>

        <!-- POPOVER PARA SACAR LISTAS DE USUARIOS QUE APOYAN LA CAUSA -->
            <div class="popover">
                <a href="#" class="hidden" rel="nofollow">Mostrar lista de usuarios que apoyan esta causa</a>
                <button type="button" class="close" aria-hidden="true" data-dismiss="popover"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">cerrar</span></button>
                <div class="popover-user-list">
                    <p><g:message code="cause.card.support.citizens.text"/> </p>
                    <div class="scroll">
                        <ul>
                            <g:render template="/kuorumUser/embebedUsersList" model="[users:citizens]"/>
                        </ul>
                    </div><!-- /.contenedor scroll -->
                </div><!-- /popover-user-list -->
            </div>
        </div>
    </div>
</article>