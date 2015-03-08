<section class="boxes taking-part">
    <h1><g:message code="post.show.boxes.like.userList.title"/> </h1>

    <userUtil:showListUsers
            users="${users}"
            visibleUsers="13"
            total="${20}"
            cssClass="user-list-followers"
            messagesPrefix="post.show.boxes.like.userList"/>
</section>