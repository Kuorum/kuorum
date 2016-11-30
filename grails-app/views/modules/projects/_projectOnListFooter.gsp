<footer>
    <div class="row">
        <ul class="col-xs-5 col-sm-5 col-md-6 info-kak">
            <li itemprop="datePublished" class="hidden-xs hidden-sm">
                <kuorumDate:humanDate date="${project.deadline}"/>
            </li>
        </ul>
        <div class="col-xs-7 col-sm-7 col-md-6">
            <g:render template="/project/projectVotesModuleVotingButtons" model="[project:project, userVote:userVote, iconSmall:true, header:Boolean.TRUE]"/>
        </div>
    </div>
</footer>