<div class="${isQuestionWithImages?'col-xs-12 col-sm-3 question-option-image': 'col-xs-1 question-option-icon'} ${isQuestionWithImages && !option.urlImage?'imagen-shadowed-main-color-domain':''}">
    <span class="${faClassEmpty} check-icon"></span>
    <span class="${faClassChecked} check-icon"></span>
    <g:if test="${isQuestionWithImages}">
        <img src="${option.urlImage?:g.resource(dir: "images", file: "emptySquared.png")}"/>
    </g:if>
</div>