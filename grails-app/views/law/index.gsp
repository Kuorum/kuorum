<%@ page import="kuorum.core.model.VoteType; kuorum.core.model.PostType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    %{--<title>${law.shortName}</title>--}%
    <meta name="layout" content="normalLayout">
</head>


<content tag="mainContent">
    <div class="intro">
        <h1>Todas las leyes</h1>
        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin dignissim augue eget eros fermentum eu pellentesque erat congue. Proin convallis justo at massa rhoncus non posuere nibh imperdiet.</p>
        <form id="all-laws" role="form" class="form-inline">
            <div class="form-group">
                <label for="region" class="sr-only">Filtrar por región</label>
                <select class="form-control" id="region">
                    <option value="Elegir region">Filtrar por región</option>
                    <option value="Region1">Región 1</option>
                    <option value="Region2">Región 2</option>
                    <option value="Region3">Región 3...</option>
                </select>
            </div>
            <div class="form-group">
                <label for="materia" class="sr-only">Filtrar por materia</label>
                <select class="form-control" id="materia">
                    <option value="Elegir materia">Filtrar por materia</option>
                    <option value="Materia1">Región 1</option>
                    <option value="Materia2">Región 2</option>
                    <option value="Materia3">Región 3...</option>
                </select>
            </div>
        </form>
    </div>

    <div class="block-results">

        <g:each in="${groupLaws}" var="region">
            <h1>${region.key}</h1>
            <g:each in="${region.value}" var="group">
                <h3>Leyes sobre ${group.commission}</h3>
                <ul>
                    <g:each in="${group.elements}" var="law">
                        <li> <g:link mapping="lawShow" params="${law.encodeAsLinkProperties()}">${law.name}</g:link> </li>
                    </g:each>
                </ul>
            </g:each>

        </g:each>
</content>