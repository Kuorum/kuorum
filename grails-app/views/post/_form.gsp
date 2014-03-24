<%@ page import="kuorum.post.Post" %>


<div class="fieldcontain ${hasErrors(bean: command, field: 'photo', 'error')} ">
    <label for="photo">
        <g:message code="post.photo.label" default="Photo"/>

    </label>
    <g:textField name="photo" value="${command?.photo}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: command, field: 'postType', 'error')} ">
    <label for="postType">
        <g:message code="post.postType.label" default="Post Type"/>

    </label>
    <g:select name="postType" from="${kuorum.core.model.PostType?.values()}"
              keys="${kuorum.core.model.PostType.values()*.name()}" required=""
              value="${command?.postType?.name()}"/>
</div>


<div class="fieldcontain ${hasErrors(bean: command, field: 'text', 'error')} ">
    <label for="text">
        <g:message code="post.text.label" default="Text"/>

    </label>
    <g:textField name="text" value="${command?.text}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: command, field: 'title', 'error')} ">
    <label for="title">
        <g:message code="post.title.label" default="Title"/>

    </label>
    <g:textField name="title" value="${command?.title}"/>
</div>
