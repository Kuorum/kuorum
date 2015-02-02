<%@ page import="kuorum.core.model.Gender" %>
<ul class="col-xs-12 col-sm-8 col-md-8 pull-right infoVotes">
    <li class="vote-yes">
        <span>84%</span>
        <span class="sr-only">Votos a favor</span>
        <span class="icon-smiley fa-lg"></span>
    </li>
    <li class="vote-no">
        <span>15%</span>
        <span class="sr-only">Votos en contra</span>
        <span class="icon-sad fa-lg"></span>
    </li>
    <li class="vote-neutral">
        <span>1%</span>
        <span class="sr-only">Abstenciones</span>
        <span class="icon-neutral fa-lg"></span>
    </li>
    <li>
        <span>48</span>
        <span class="sr-only">Propuestas</span>
        <span class="fa fa-lightbulb-o fa-lg"></span>
    </li>
    <li class="arrow">
        <span class="sr-only">Ampliar detalles de la votación</span>
        <a aria-controls="detailsInfoVotes" aria-expanded="false" href="#detailsInfoVotes" data-toggle="collapse" class="open-infoVotes">
            <span class="fa fa-caret-down fa-lg"></span>
        </a>
    </li>
</ul>
<div class="col-xs-12 collapse" id="detailsInfoVotes">
    <div class="row">
        <div class="col-sm-8 col-sm-push-4 col-md-9 col-md-push-3 col-lg-10 col-lg-push-2">
            <table class="table table-condensed">
                <thead class="sr-only">
                <tr>
                    <th></th>
                    <th>Votos a favor</th>
                    <th>Votos en contra</th>
                    <th>Abstenciones</th>
                    <th>Propuestas</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <th scope="row"><span class="fa fa-female hidden-md hidden-lg"></span><span class="hidden-xs hidden-sm">mujeres</span></th>
                    <td>${regionStats.genderVotes[Gender.FEMALE].yes}</td>
                    <td>${regionStats.genderVotes[Gender.FEMALE].no}</td>
                    <td>${regionStats.genderVotes[Gender.FEMALE].abs}</td>
                    <td>${regionStats.genderVotes[Gender.FEMALE].numPosts}</td>
                </tr>
                <tr>
                    <th scope="row"><span class="fa fa-male hidden-md hidden-lg"></span><span class="hidden-xs hidden-sm">hombres</span></th>
                    <td>${regionStats.genderVotes[Gender.MALE].yes}</td>
                    <td>${regionStats.genderVotes[Gender.MALE].no}</td>
                    <td>${regionStats.genderVotes[Gender.MALE].abs}</td>
                    <td>${regionStats.genderVotes[Gender.MALE].numPost}</td>
                </tr>
                <tr>
                    <th scope="row"><span class="fa fa-users hidden-md hidden-lg"></span><span class="hidden-xs hidden-sm">organizaciones</span></th>
                    <td>${regionStats.genderVotes[Gender.ORGANIZATION].yes}</td>
                    <td>${regionStats.genderVotes[Gender.ORGANIZATION].no}</td>
                    <td>${regionStats.genderVotes[Gender.ORGANIZATION].abs}</td>
                    <td>${regionStats.genderVotes[Gender.ORGANIZATION].numPost}</td>
                </tr>
                <tr>
                    <th scope="row"><span class="hidden-md hidden-lg">*</span><span class="hidden-xs hidden-sm">otras regiones *</span></th>
                    <td>125</td>
                    <td>29</td>
                    <td>1</td>
                    <td>5</td>
                </tr>
                </tbody>
            </table>
        </div>
        <g:render template="projectInfoIcons" model="[project:project]"/>
    </div>
</div>