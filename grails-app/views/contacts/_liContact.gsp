<li>
    <span class="user-img"><img src="${image.contactImgSrc(contact:contact)}" alt="${contact.name}" itemprop="image"></span>
    <h3 class="title"><a href="#">${contact.name}</a></h3>
    <p class="email"><span class="fa fa-envelope-o"></span> <a href="#">${contact.email}</a></p>
    <p class="followers"> <span class="fa fa-user"></span> ${contact.numFollowers} followers <a href="#"><span class="fa fa-pencil-square-o"></span><span class="sr-only">Editar</span></a></p>
    <div class="container-lists">
        <form class="addTag off">
            <a href="#" role="button" class="tag label label-info addTagBtn"><span class="fa fa-tag"></span>+ add tag</a>
            <label for="tagsField" class="sr-only">Add tags</label>
            <input class="tagsField" type="text" value="${contact.tags.join(",")}" data-urlTags="${g.createLink(mapping:'politicianContactTagsAjax')}">
            <input type="submit" value="Add tags" class="btn btn-blue inverted" id="inputAddTags">
            <div class="engagement-container">
                <h4>Engagement:</h4>
                <ul class="engagement">
                    <li class="${contact.status == org.kuorum.rest.model.contact.ContactStatusRSDTO.INACTIVE?'active':''}"><a href="#">inactive</a></li>
                    <li class="${contact.status == org.kuorum.rest.model.contact.ContactStatusRSDTO.READER?'active':''}"><a href="#">reader</a></li>
                    <li class="${contact.status == org.kuorum.rest.model.contact.ContactStatusRSDTO.SUPPORTER?'active':''}"><a href="#">supported</a></li>
                    <li class="${contact.status == org.kuorum.rest.model.contact.ContactStatusRSDTO.BROADCASTER?'active':''}"><a href="#">broadcaster</a></li>
                </ul>
            </div>
        </form>
    </div>
    <a href="#" class="contactStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats<span></a>
    <a href="#" role="button" class="contactDelete"><span class="fa fa-trash"></span> <span class="sr-only">Delete</span></a>
</li>
