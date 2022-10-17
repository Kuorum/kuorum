<g:render template="/contest/showModules/mainContent/contestData"
          model="[contest: contest, campaignUser: campaignUser]"/>

<g:render template="/contest/showModules/cCallToAction"
          model="[contest: contest, campaignUser: campaignUser, hideXs: false, hideXl: true]"/>

<g:render template="/contest/showModules/cContestRanking"
          model="[contest: contest, campaignUser: campaignUser, hideXs: false, hideXl: true]"/>

<g:render template="/contest/showModules/mainContent/contestApplications"
          model="[contest: contest, campaignUser: campaignUser]"/>

<g:render template="/campaigns/showModules/campingModalEditScheduled"/>