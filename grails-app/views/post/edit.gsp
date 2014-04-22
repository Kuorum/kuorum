<%@ page import="kuorum.core.FileGroup" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="editPostLayout">
</head>

<content tag="intro">
    <h1>Participa expresando tu voz</h1>
    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod.</p>
</content>

<content tag="mainContent">
    <form action="#" method="POST" role="form">
        <fieldset class="type">
            <div class="form-group">
                <label for="selectType">Selecciona tu tipo de publicación</label>
                <div class="row">

                    <!-- la siguiente lista está oculta; debe venir marcado con class="active" el <li> que corresponda pues ese aparecerá visible -->
                    <ul id="typePubli" class="hidden">
                        <li class="active">Cuéntanos tu historia. En qué te afecta esta ley. Nos comprometemos a hacerla llegar a los políticos para que la tengan en cuenta.</li>
                        <li>Haz una pregunta. Incididunt ut labore et dolore magna aliqua consectetur adipisicing elit lorem ipsum dolor sit amet.</li>
                        <li>Haz una propuesta. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod</li>
                    </ul>
                    <p class="col-md-7" id="updateText"></p> <!-- aquí hago visible por js el texto que corresponde a la opción elegida por el usuario -->
                    <div class="col-md-5">
                        <select class="form-control" id="selectType">
                            <option value="Historia">&#xf02d; Historia</option> <!-- debe venir con la primera opción que sea la que el usuario ha seleccionado en el paso anterior -->
                            <option value="Pregunta">&#xf128; Pregunta</option>
                            <option value="Propuesta">&#xf0eb; Propuesta</option>
                        </select>
                    </div>
                </div>
            </div><!-- /.form-group -->
        </fieldset>
        <fieldset class="title">
            <div class="form-group">
                <label for="titlePost">Da un título a tu publicación</label>
                <div class="textareaContainer">
                    <textarea class="form-control counted" rows="3" placeholder="Escribe un título que describa tu publicación..." id="titlePost" tabindex="13" required></textarea>
                    <span class="hashtag">#leyloquesea</span>
                </div>
                <div id="charInit" class="hidden">Tienes un límite de caracteres de <span>122</span></div>
                <div id="charNum">Te quedan <span></span> caracteres</div>
            </div>
        </fieldset>
        <fieldset class="text">
            <div class="form-group">
                <label for="textPost">Texto de tu publicación</label>
                <textarea class="form-control texteditor validate[required]" rows="10" placeholder="Escribe un texto que describa tu publicación..." id="textPost" tabindex="14" required></textarea>
            </div>
        </fieldset>
        <fieldset class="multimedia">
            <div class="form-group image">
                <label for="fileupload">Sube una imagen</label>

                <formUtil:editImage command="${command}" field="imageId" fileGroup="${FileGroup.POST_IMAGE}"/>
            </div>
            <div class="form-group video">
                <label for="videoPost">También puedes escribir una dirección de un vídeo</label>
                <input type="url" class="form-control" id="videoPost" placeholder="http://" tabindex="16">
            </div>
        </fieldset>
        <fieldset class="page">
            <div class="form-group">
                <label for="numberPage">Escribe la página de la ley que influye para esta propuesta:</label>
                <div class="form-group">
                    <input type="number" id="numberPage" placeholder="0" tabindex="17">
                    <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua</p>
                </div>
            </div>
        </fieldset>
        <fieldset class="btns">
            <div class="form-group">
                <button type="button" class="btn btn-grey btn-lg" tabindex="18">Guardar y continuar</button>
                <a href="#" class="cancel" tabindex="19">Cancelar</a>
            </div>
        </fieldset>
    </form>
</content>

<content tag="cColumn">
    <section class="boxes noted">
        <a href="#">#loquesea</a>
        <h1>Proyecto de ley del aborto</h1>
        <p>Ley Orgánica de protección de la vida del concebido y derechos de la mujer embarazada</p>
    </section>
    <section class="boxes">
        <h1>Consejos de publicación</h1>
        <h2><span class="fa fa-caret-right"></span> El título</h2>
        <p>Elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
        <h2><span class="fa fa-caret-right"></span> Tu propuesta</h2>
        <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
        <p>Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.</p>
    </section>
</content>
