<g:render template="/contest/showModules/mainContent/contestData"
          model="[contest: contest, campaignUser: campaignUser]"/>
<%-- Se oculta en escritorio y moviles pero se mantiene el render para no romper la logica Javascipt del boton flotante --%>
<g:render template="/contest/showModules/cCallToAction"
          model="[contest: contest, campaignUser: campaignUser, hideXs: true, hideXl: true]"/>

<g:render template="/contest/showModules/cContestRanking"
          model="[contest: contest, campaignUser: campaignUser, hideXs: false, hideXl: true]"/>

<g:render template="/contest/showModules/mainContent/contestApplications"
          model="[contest: contest, campaignUser: campaignUser]"/>

<g:render template="/campaigns/showModules/campingModalEditScheduled"/>