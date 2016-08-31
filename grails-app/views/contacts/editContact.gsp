<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.contacts"/></title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianContacts"> <g:message code="tools.contact.title"/> </g:link></li>
        <li class="active">Leonard Hoffstader</li>
    </ol>
    <div class="box-ppal edit-contact-header clearfix" itemscope itemtype="http://schema.org/Person">
        <div>
            <span class="user-img"><img src="images/user.jpg" alt="Diecisiete caract" itemprop="image"></span>
            <h3 class="title"><a href="#">Pepito Pérez y Pérez</a></h3>
            <p class="email"><span class="fa fa-envelope-o"></span> <a href="#">leonard.hoffstader@microsoft.com</a></p>
            <p class="followers">
                <span class="fa fa-user"></span> 12 followers
            <!-- opción de editar si procede -->
                <a href="#"><span class="fa fa-pencil-square-o"></span><span class="sr-only">Editar</span></a>
            </p>
            <ul class="social-links">
                <li><a href="#"><span class="fa fa-facebook-square"></span><span class="sr-only">Facebook</span></a></li>
                <li><a href="#"><span class="fa fa-twitter-square"></span><span class="sr-only">Twitter</span></a></li>
                <li><a href="#"><span class="fa fa-google-plus-square"></span><span class="sr-only">Google+</span></a></li>
                <li><a href="#"><span class="fa fa-linkedin-square"></span><span class="sr-only">Linkedin</span></a></li>
            </ul>
            <ul class="btns">
                <!-- ejemplo deshabilitado -->
                <li><a href="#" class="btn btn-blue inverted disabled"><span class="fa fa-plus"></span> Follow</a></li>
                <li><a href="#" class="btn btn-blue inverted"><span class="fa fa-envelope-0"></span> Contact</a></li>
            </ul>
        </div>
        <div class="container-lists">
            <form class="addTag off">
                <a href="#" role="button" class="tag label label-info addTagBtn"><span class="fa fa-tag"></span> edit tag</a>
                <ul>
                    <li><a href="#" class="tag label label-info">tory</a></li>
                    <li><a href="#" class="tag label label-info">journalist</a></li>
                    <li><a href="#" class="tag label label-info">anti-gun</a></li>
                </ul>
                <label for="tagsField" class="sr-only">Save tags</label>
                <input id="tagsField" class="tagsField" type="text" value="tory,journalist,anti-gun">
                <input type="submit" value="Save tags" class="btn btn-blue inverted" id="inputAddTags">
            </form>
            <div class="engagement-container clearfix">
                <h4>Engagement:</h4>
                <ul class="engagement">
                    <li><a href="#">inactive</a></li>
                    <li class="active"><a href="#">reader</a></li>
                    <li><a href="#">supported</a></li>
                    <li><a href="#">broadcaster</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="box-ppal edit-contact clearfix">
        <ul class="nav nav-tabs simple" data-tabs="tabs">
            <li role="presentation" class="active"><a href="#details" data-toggle="tab">Details</a></li>
            <li role="presentation"><a href="#activity" data-toggle="tab">Activity</a></li>
            <li role="presentation"><a href="#socialNetwork" data-toggle="tab">Social Networks</a></li>
            <li role="presentation"><a href="#notes" data-toggle="tab">Notes</a></li>
        </ul>
        <div id="tabs-new-campaign" class="tab-content">
            <div class="tab-pane active" id="details">
                <h4 class="sr-only">Details</h4>
                <form>
                    <div class="row">
                        <div class="form-group col-md-4">
                            <label for="name">Name</label>
                            <input type="text" name="name" class="form-control input-lg" id="name" required placeholder="Pepito Pérez" aria-required="true">
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-4">
                            <label for="email">Email</label>
                            <input type="text" name="email" class="form-control input-lg" id="email" required placeholder="pepito.perez@gmail.com" aria-required="true">
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-4">
                            <label for="city">City</label>
                            <input type="text" name="city" class="form-control input-lg" id="city" required placeholder="Manchester" aria-required="true">
                        </div>
                        <div class="form-group col-md-4">
                            <label for="language">Language</label>
                            <!-- ejemplo deshabilitado -->
                            <input type="text" name="language" class="form-control input-lg" id="language" required placeholder="English" aria-required="true" disabled>
                        </div>
                        <div class="col-md-4">
                            <input type="submit" value="Save" class="btn btn-blue inverted">
                        </div>
                    </div>
                </form>
            </div>
            <div class="tab-pane" id="activity">
                <h4 class="sr-only">Activity</h4>
                <ul class="activity">
                    <li class="posts">
                        <span>52</span> Campaigns sent
                    </li>
                    <li class="posts">
                        <span>30%</span> Open rate
                    </li>
                    <li class="posts">
                        <span>1%</span> Click rate
                    </li>
                </ul>
                <ul class="activity des-engagement clearfix">
                    <li class="posts">
                        <span>inactive</span>
                        <ul><li>Open rate &#60; 10%</li></ul>
                    </li>
                    <li class="posts">
                        <span>reader</span> <ul><li>Open rate &#62; 10%</li><li> Click rate &#60; 3%</li></ul>
                    </li>
                    <li class="posts">
                        <span>supporter</span> <ul><li>Open rate &#62; 10%</li><li>Click rate &#60; 3%</li></ul>
                    </li>
                    <li class="posts">
                        <span>broadcaster</span> <ul><li>Open rate &#62; 70%</li></ul>
                    </li>
                </ul>
            </div>
            <div class="tab-pane" id="socialNetwork">
                <h4 class="sr-only">Social Networks</h4>
                <form>
                    <div class="row">
                        <div class="form-group col-md-4">
                            <label for="socialFb">Facebook</label>
                            <div class="input-group">
                                <div class="input-group-addon"><span class="fa fa-facebook"></span></div>
                                <input type="text" class="form-control" id="socialFb">
                            </div>
                        </div>
                        <div class="form-group col-md-4">
                            <label for="socialTw">Twitter</label>
                            <div class="input-group">
                                <div class="input-group-addon"><span class="fa fa-twitter"></span></div>
                                <input type="text" class="form-control" id="socialTw" disabled>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-4">
                            <label for="socialGp">Google+</label>
                            <div class="input-group">
                                <div class="input-group-addon"><span class="fa fa-google-plus"></span></div>
                                <input type="text" class="form-control" id="socialGp">
                            </div>
                        </div>
                        <div class="form-group col-md-4">
                            <label for="socialLk">Linkedin</label>
                            <div class="input-group">
                                <div class="input-group-addon"><span class="fa fa-linkedin"></span></div>
                                <input type="text" class="form-control" id="socialLk">
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4 col-md-offset-8">
                            <input type="submit" value="Save" class="btn btn-blue inverted" disabled>
                        </div>
                    </div>
                </form>
            </div>
            <div class="tab-pane" id="notes">
                <h4 class="sr-only">Notes</h4>
                <form>
                    <div class="row">
                        <div class="form-group col-md-8">
                            <label for="notesContact">Notes</label>
                            <textarea id="notesContact" class="form-control"></textarea>
                        </div>
                        <div class="form-group col-md-2 col-md-offset-2">
                            <input type="submit" value="Save" class="btn btn-blue inverted">
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</content>