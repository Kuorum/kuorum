<%@ page import="kuorum.post.Post" %>



<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'defender', 'error')} ">
    <label for="defender">
        <g:message code="post.defender.label" default="Defender"/>

    </label>
    <g:select id="defender" name="defender.id" from="${kuorum.users.KuorumUser.list()}" optionKey="id" required=""
              value="${postInstance?.defender?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'firstCluck', 'error')} ">
    <label for="firstCluck">
        <g:message code="post.firstCluck.label" default="First Cluck"/>

    </label>
    <g:select id="firstCluck" name="firstCluck.id" from="${kuorum.post.Cluck.list()}" optionKey="id" required=""
              value="${postInstance?.firstCluck?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'law', 'error')} ">
    <label for="law">
        <g:message code="post.law.label" default="Law"/>

    </label>
    <g:select id="law" name="law.id" from="${kuorum.law.Law.list()}" optionKey="id" required=""
              value="${postInstance?.law?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'numClucks', 'error')} ">
    <label for="numClucks">
        <g:message code="post.numClucks.label" default="Num Clucks"/>

    </label>
    <g:field type="number" name="numClucks" value="${postInstance.numClucks}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'numVotes', 'error')} ">
    <label for="numVotes">
        <g:message code="post.numVotes.label" default="Num Votes"/>

    </label>
    <g:field type="number" name="numVotes" value="${postInstance.numVotes}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'owner', 'error')} ">
    <label for="owner">
        <g:message code="post.owner.label" default="Owner"/>

    </label>
    <g:select id="owner" name="owner.id" from="${kuorum.users.KuorumUser.list()}" optionKey="id" required=""
              value="${postInstance?.owner?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'photo', 'error')} ">
    <label for="photo">
        <g:message code="post.photo.label" default="Photo"/>

    </label>
    <g:textField name="photo" value="${postInstance?.photo}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'postType', 'error')} ">
    <label for="postType">
        <g:message code="post.postType.label" default="Post Type"/>

    </label>
    <g:select name="postType" from="${kuorum.core.model.PostType?.values()}"
              keys="${kuorum.core.model.PostType.values()*.name()}" required=""
              value="${postInstance?.postType?.name()}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'published', 'error')} ">
    <label for="published">
        <g:message code="post.published.label" default="Published"/>

    </label>
    <g:checkBox name="published" value="${postInstance?.published}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'text', 'error')} ">
    <label for="text">
        <g:message code="post.text.label" default="Text"/>

    </label>
    <g:textField name="text" value="${postInstance?.text}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'title', 'error')} ">
    <label for="title">
        <g:message code="post.title.label" default="Title"/>

    </label>
    <g:textField name="title" value="${postInstance?.title}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'victory', 'error')} ">
    <label for="victory">
        <g:message code="post.victory.label" default="Victory"/>

    </label>
    <g:checkBox name="victory" value="${postInstance?.victory}"/>
</div>

