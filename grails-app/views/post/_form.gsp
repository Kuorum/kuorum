<%@ page import="kuorum.post.Post" %>


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
