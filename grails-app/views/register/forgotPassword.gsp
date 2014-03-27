<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
<meta name="layout" content="kuorumLayout">
</head>


<content tag="mainContent">
    <g:form mapping="resetPassword">
        <label for="email"> EMAIL </label>
        <g:textField name="email" size="25" value="${command.email}"/>
        <g:submitButton name="reset pass"/>
    </g:form>
    <script>
        $(document).ready(function() {
            $('#username').focus();
        });
    </script>

</content>

<content tag="cColumn">
    Columna C de registro satisfactorio
</content>
