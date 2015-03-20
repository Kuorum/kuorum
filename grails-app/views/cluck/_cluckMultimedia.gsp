<postUtil:ifHasMultimedia post="${post}">
    <g:if test="${post.multimedia.fileType == kuorum.core.FileType.IMAGE}">
        <div class="photo">
            <img src="${post.multimedia.url}" alt="${post.multimedia.originalName}" itemprop="image">
        </div>
    </g:if>
</postUtil:ifHasMultimedia>
