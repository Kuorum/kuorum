
class KuorumSearchUrlMappings {

    static mappings = {
        name searcherSearch:        "/search"        (controller: "search", action:"search"){mappingName="searcherSearch"}
        name en_searcherSearch:     "/search/$word?" (controller: "search", action:"search"){mappingName="searcherSearch"}
        name es_searcherSearch:     "/buscar/$word?" (controller: "search", action:"search"){mappingName="searcherSearch"}
        name de_searcherSearch:     "/suche/$word?" (controller: "search", action:"search"){mappingName="searcherSearch"}
        name ca_searcherSearch:     "/cerca/$word?" (controller: "search", action:"search"){mappingName="searcherSearch"}

        name searcherSearchByCAUSE:   "/search/cause"             (controller: "search", action:"search"){searchType="CAUSE"; mappingName="searcherSearchByCAUSE"}
        name en_searcherSearchByCAUSE:"/search/cause/$word?"      (controller: "search", action:"search"){searchType="CAUSE"; mappingName="searcherSearchByCAUSE"}
        name es_searcherSearchByCAUSE:"/buscar/causa/$word?"      (controller: "search", action:"search"){searchType="CAUSE"; mappingName="searcherSearchByCAUSE"}
        name de_searcherSearchByCAUSE:"/suche/themen/$word?"      (controller: "search", action:"search"){searchType="CAUSE"; mappingName="searcherSearchByCAUSE"}
        name ca_searcherSearchByCAUSE:"/cerca/causa/$word?"      (controller: "search", action:"search"){searchType="CAUSE"; mappingName="searcherSearchByCAUSE"}

        name searcherSearchByREGION:   "/search/users/from/$word?"   (controller: "search", action:"search"){searchType="REGION"; mappingName="searcherSearchByREGION"}
        name en_searcherSearchByREGION:"/search/from/$word?"         (controller: "search", action:"search"){searchType="REGION"; mappingName="searcherSearchByREGION"}
        name es_searcherSearchByREGION:"/buscar/en/$word?"           (controller: "search", action:"search"){searchType="REGION"; mappingName="searcherSearchByREGION"}
        name de_searcherSearchByREGION:"/suche/von/$word?"           (controller: "search", action:"search"){searchType="REGION"; mappingName="searcherSearchByREGION"}
        name ca_searcherSearchByREGION:"/cerca/a/$word?"           (controller: "search", action:"search"){searchType="REGION"; mappingName="searcherSearchByREGION"}

        name searcherSearchKUORUM_USER:     "/search/users"             (controller: "search", action:"search"){solrType="KUORUM_USER"; mappingName="searcherSearchKUORUM_USER"}
        name en_searcherSearchKUORUM_USER:  "/search/users/$word?"      (controller: "search", action:"search"){solrType="KUORUM_USER"; mappingName="searcherSearchKUORUM_USER"}
        name es_searcherSearchKUORUM_USER:  "/buscar/usuarios/$word?"   (controller: "search", action:"search"){solrType="KUORUM_USER"; mappingName="searcherSearchKUORUM_USER"}
        name de_searcherSearchKUORUM_USER:  "/suche/benutzer/$word?"   (controller: "search", action:"search"){solrType="KUORUM_USER"; mappingName="searcherSearchKUORUM_USER"}
        name ca_searcherSearchKUORUM_USER:  "/cerca/usuaris/$word?"   (controller: "search", action:"search"){solrType="KUORUM_USER"; mappingName="searcherSearchKUORUM_USER"}

        name searcherSearchKUORUM_USERByCAUSE:   "/search/users/cause/$word?"          (controller: "search", action:"search"){searchType="CAUSE";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByCAUSE"}
        name en_searcherSearchKUORUM_USERByCAUSE:"/search/users/cause/$word?"          (controller: "search", action:"search"){searchType="CAUSE";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByCAUSE"}
        name es_searcherSearchKUORUM_USERByCAUSE:"/buscar/usuarios/causa/$word?"       (controller: "search", action:"search"){searchType="CAUSE";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByCAUSE"}
        name de_searcherSearchKUORUM_USERByCAUSE:"/suche/benutzer/themen/$word?"       (controller: "search", action:"search"){searchType="CAUSE";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByCAUSE"}
        name ca_searcherSearchKUORUM_USERByCAUSE:"/cerca/usuaris/causa/$word?"       (controller: "search", action:"search"){searchType="CAUSE";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByCAUSE"}

        name searcherSearchKUORUM_USERByREGION:   "/search/users/from/$word?"          (controller: "search", action:"search"){searchType="REGION";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByREGION"}
        name en_searcherSearchKUORUM_USERByREGION:"/search/users/from/$word?"          (controller: "search", action:"search"){searchType="REGION";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByREGION"}
        name es_searcherSearchKUORUM_USERByREGION:"/buscar/usuarios/en/$word?"         (controller: "search", action:"search"){searchType="REGION";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByREGION"}
        name de_searcherSearchKUORUM_USERByREGION:"/suche/benutzer/von/$word?"         (controller: "search", action:"search"){searchType="REGION";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByREGION"}
        name ca_searcherSearchKUORUM_USERByREGION:"/cerca/usuaris/a/$word?"         (controller: "search", action:"search"){searchType="REGION";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByREGION"}

        name searcherSearchPOST:   "/search/post"                   (controller: "search", action:"search"){solrType="POST"; mappingName="searcherSearchPOST"}
        name en_searcherSearchPOST:"/search/post/$word?"            (controller: "search", action:"search"){solrType="POST"; mappingName="searcherSearchPOST"}
        name es_searcherSearchPOST:"/buscar/publicacion/$word?"     (controller: "search", action:"search"){solrType="POST"; mappingName="searcherSearchPOST"}
        name de_searcherSearchPOST:"/suche/beitraege/$word?"     (controller: "search", action:"search"){solrType="POST"; mappingName="searcherSearchPOST"}
        name ca_searcherSearchPOST:"/cerca/proposta/$word?"     (controller: "search", action:"search"){solrType="POST"; mappingName="searcherSearchPOST"}

        name searcherSearchPOSTByCAUSE:   "/search/post/cause/$word?"           (controller: "search", action:"search"){searchType="CAUSE";type="POST"; mappingName="searcherSearchPOSTByCAUSE"}
        name en_searcherSearchPOSTByCAUSE:"/search/post/cause/$word?"           (controller: "search", action:"search"){searchType="CAUSE";type="POST"; mappingName="searcherSearchPOSTByCAUSE"}
        name es_searcherSearchPOSTByCAUSE:"/buscar/publicacion/causa/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="POST"; mappingName="searcherSearchPOSTByCAUSE"}
        name de_searcherSearchPOSTByCAUSE:"/suche/beitraege/themen/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="POST"; mappingName="searcherSearchPOSTByCAUSE"}
        name ca_searcherSearchPOSTByCAUSE:"/cerca/proposta/causa/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="POST"; mappingName="searcherSearchPOSTByCAUSE"}

        name searcherSearchPOSTByREGION:   "/search/post/from/$word?"           (controller: "search", action:"search"){searchType="REGION";type="POST"; mappingName="searcherSearchPOSTByREGION"}
        name en_searcherSearchPOSTByREGION:"/search/post/from/$word?"           (controller: "search", action:"search"){searchType="REGION";type="POST"; mappingName="searcherSearchPOSTByREGION"}
        name es_searcherSearchPOSTByREGION:"/buscar/publicacion/en/$word?"      (controller: "search", action:"search"){searchType="REGION";type="POST"; mappingName="searcherSearchPOSTByREGION"}
        name de_searcherSearchPOSTByREGION:"/suche/beitraege/von/$word?"      (controller: "search", action:"search"){searchType="REGION";type="POST"; mappingName="searcherSearchPOSTByREGION"}
        name ca_searcherSearchPOSTByREGION:"/cerca/proposta/a/$word?"      (controller: "search", action:"search"){searchType="REGION";type="POST"; mappingName="searcherSearchPOSTByREGION"}

        name searcherSearchDEBATE:   "/search/debate"            (controller: "search", action:"search"){solrType="DEBATE"; mappingName="searcherSearchDEBATE"}
        name en_searcherSearchDEBATE:"/search/debate/$word?"     (controller: "search", action:"search"){solrType="DEBATE"; mappingName="searcherSearchDEBATE"}
        name es_searcherSearchDEBATE:"/buscar/debate/$word?"     (controller: "search", action:"search"){solrType="DEBATE"; mappingName="searcherSearchDEBATE"}
        name de_searcherSearchDEBATE:"/suche/debatte/$word?"     (controller: "search", action:"search"){solrType="DEBATE"; mappingName="searcherSearchDEBATE"}
        name ca_searcherSearchDEBATE:"/cerca/debat/$word?"     (controller: "search", action:"search"){solrType="DEBATE"; mappingName="searcherSearchDEBATE"}

        name searcherSearchDEBATEByCAUSE:   "/search/debate/cause/$word?"          (controller: "search", action:"search"){searchType="CAUSE";type="DEBATE"; mappingName="searcherSearchDEBATEByCAUSE"}
        name en_searcherSearchDEBATEByCAUSE:"/search/debate/cause/$word?"          (controller: "search", action:"search"){searchType="CAUSE";type="DEBATE"; mappingName="searcherSearchDEBATEByCAUSE"}
        name es_searcherSearchDEBATEByCAUSE:"/buscar/debate/causa/$word?"          (controller: "search", action:"search"){searchType="CAUSE";type="DEBATE"; mappingName="searcherSearchDEBATEByCAUSE"}
        name de_searcherSearchDEBATEByCAUSE:"/suche/debatte/themen/$word?"          (controller: "search", action:"search"){searchType="CAUSE";type="DEBATE"; mappingName="searcherSearchDEBATEByCAUSE"}
        name ca_searcherSearchDEBATEByCAUSE:"/cerca/debat/causa/$word?"          (controller: "search", action:"search"){searchType="CAUSE";type="DEBATE"; mappingName="searcherSearchDEBATEByCAUSE"}

        name searcherSearchDEBATEByREGION:   "/search/debate/from/$word?"           (controller: "search", action:"search"){searchType="REGION";type="DEBATE"; mappingName="searcherSearchDEBATEByREGION"}
        name en_searcherSearchDEBATEByREGION:"/search/debate/from/$word?"           (controller: "search", action:"search"){searchType="REGION";type="DEBATE"; mappingName="searcherSearchDEBATEByREGION"}
        name es_searcherSearchDEBATEByREGION:"/buscar/debate/en/$word?"             (controller: "search", action:"search"){searchType="REGION";type="DEBATE"; mappingName="searcherSearchDEBATEByREGION"}
        name de_searcherSearchDEBATEByREGION:"/suche/debatte/von/$word?"             (controller: "search", action:"search"){searchType="REGION";type="DEBATE"; mappingName="searcherSearchDEBATEByREGION"}
        name ca_searcherSearchDEBATEByREGION:"/cerca/debat/a/$word?"             (controller: "search", action:"search"){searchType="REGION";type="DEBATE"; mappingName="searcherSearchDEBATEByREGION"}


        name searcherSearchEVENT:   "/search/event"             (controller: "search", action:"search"){solrType="EVENT"; mappingName="searcherSearchEVENT"}
        name en_searcherSearchEVENT:"/search/event/$word?"      (controller: "search", action:"search"){solrType="EVENT"; mappingName="searcherSearchEVENT"}
        name es_searcherSearchEVENT:"/buscar/evento/$word?"     (controller: "search", action:"search"){solrType="EVENT"; mappingName="searcherSearchEVENT"}
        name de_searcherSearchEVENT:"/suche/ereignis/$word?"     (controller: "search", action:"search"){solrType="EVENT"; mappingName="searcherSearchEVENT"}
        name ca_searcherSearchEVENT:"/cerca/esdeveniment/$word?"     (controller: "search", action:"search"){solrType="EVENT"; mappingName="searcherSearchEVENT"}

        name searcherSearchEVENTByCAUSE:   "/search/event/cause/$word?"          (controller: "search", action:"search"){searchType="CAUSE";type="EVENT"; mappingName="searcherSearchEVENTByCAUSE"}
        name en_searcherSearchEVENTByCAUSE:"/search/event/cause/$word?"          (controller: "search", action:"search"){searchType="CAUSE";type="EVENT"; mappingName="searcherSearchEVENTByCAUSE"}
        name es_searcherSearchEVENTByCAUSE:"/buscar/evento/causa/$word?"         (controller: "search", action:"search"){searchType="CAUSE";type="EVENT"; mappingName="searcherSearchEVENTByCAUSE"}
        name de_searcherSearchEVENTByCAUSE:"/suche/ereignis/themen/$word?"         (controller: "search", action:"search"){searchType="CAUSE";type="EVENT"; mappingName="searcherSearchEVENTByCAUSE"}
        name ca_searcherSearchEVENTByCAUSE:"/cerca/esdeveniment/causa/$word?"         (controller: "search", action:"search"){searchType="CAUSE";type="EVENT"; mappingName="searcherSearchEVENTByCAUSE"}

        name searcherSearchEVENTByREGION:   "/search/event/from/$word?"          (controller: "search", action:"search"){searchType="REGION";type="EVENT"; mappingName="searcherSearchEVENTByREGION"}
        name en_searcherSearchEVENTByREGION:"/search/event/from/$word?"          (controller: "search", action:"search"){searchType="REGION";type="EVENT"; mappingName="searcherSearchEVENTByREGION"}
        name es_searcherSearchEVENTByREGION:"/buscar/evento/en/$word?"           (controller: "search", action:"search"){searchType="REGION";type="EVENT"; mappingName="searcherSearchEVENTByREGION"}
        name de_searcherSearchEVENTByREGION:"/suche/ereignis/von/$word?"           (controller: "search", action:"search"){searchType="REGION";type="EVENT"; mappingName="searcherSearchEVENTByREGION"}
        name ca_searcherSearchEVENTByREGION:"/cerca/esdeveniment/a/$word?"           (controller: "search", action:"search"){searchType="REGION";type="EVENT"; mappingName="searcherSearchEVENTByREGION"}

        name searcherSearchSURVEY:   "/search/survey"               (controller: "search", action:"search"){solrType="SURVEY"; mappingName="searcherSearchSURVEY"}
        name en_searcherSearchSURVEY:"/search/survey/$word?"        (controller: "search", action:"search"){solrType="SURVEY"; mappingName="searcherSearchSURVEY"}
        name es_searcherSearchSURVEY:"/buscar/encuesta/$word?"      (controller: "search", action:"search"){solrType="SURVEY"; mappingName="searcherSearchSURVEY"}
        name de_searcherSearchSURVEY:"/suche/umfrage/$word?"      (controller: "search", action:"search"){solrType="SURVEY"; mappingName="searcherSearchSURVEY"}
        name ca_searcherSearchSURVEY:"/cerca/enquesta/$word?"      (controller: "search", action:"search"){solrType="SURVEY"; mappingName="searcherSearchSURVEY"}

        name searcherSearchSURVEYByCAUSE:   "/search/survey/cause/$word?"      (controller: "search", action:"search"){searchType="CAUSE";type="SURVEY"; mappingName="searcherSearchSURVEYByCAUSE"}
        name en_searcherSearchSURVEYByCAUSE:"/search/survey/cause/$word?"      (controller: "search", action:"search"){searchType="CAUSE";type="SURVEY"; mappingName="searcherSearchSURVEYByCAUSE"}
        name es_searcherSearchSURVEYByCAUSE:"/buscar/encuesta/causa/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="SURVEY"; mappingName="searcherSearchSURVEYByCAUSE"}
        name de_searcherSearchSURVEYByCAUSE:"/suche/umfrage/themen/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="SURVEY"; mappingName="searcherSearchSURVEYByCAUSE"}
        name ca_searcherSearchSURVEYByCAUSE:"/cerca/enquesta/causa/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="SURVEY"; mappingName="searcherSearchSURVEYByCAUSE"}

        name searcherSearchSURVEYByREGION:   "/search/survey/from/$word?"      (controller: "search", action:"search"){searchType="REGION";type="SURVEY"; mappingName="searcherSearchSURVEYByREGION"}
        name en_searcherSearchSURVEYByREGION:"/search/survey/from/$word?"      (controller: "search", action:"search"){searchType="REGION";type="SURVEY"; mappingName="searcherSearchSURVEYByREGION"}
        name es_searcherSearchSURVEYByREGION:"/buscar/encuesta/en/$word?"      (controller: "search", action:"search"){searchType="REGION";type="SURVEY"; mappingName="searcherSearchSURVEYByREGION"}
        name de_searcherSearchSURVEYByREGION:"/suche/umfrage/von/$word?"      (controller: "search", action:"search"){searchType="REGION";type="SURVEY"; mappingName="searcherSearchSURVEYByREGION"}
        name ca_searcherSearchSURVEYByREGION:"/cerca/enquesta/a/$word?"      (controller: "search", action:"search"){searchType="REGION";type="SURVEY"; mappingName="searcherSearchSURVEYByREGION"}

        name searcherSearchPARTICIPATORY_BUDGET:   "/search/participatory-budget"                  (controller: "search", action:"search"){solrType="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGET"}
        name en_searcherSearchPARTICIPATORY_BUDGET:"/search/participatory-budget/$word?"           (controller: "search", action:"search"){solrType="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGET"}
        name es_searcherSearchPARTICIPATORY_BUDGET:"/buscar/presupuesto-participativo/$word?"      (controller: "search", action:"search"){solrType="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGET"}
        name de_searcherSearchPARTICIPATORY_BUDGET:"/suche/burgerhaushalt/$word?"                  (controller: "search", action:"search"){solrType="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGET"}
        name ca_searcherSearchPARTICIPATORY_BUDGET:"/cerca/pressupost-participatiu/$word?" (controller: "search", action:"search"){solrType="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGET"}

        name searcherSearchPARTICIPATORY_BUDGETByCAUSE:   "/search/participatory-budget/cause/$word?"      (controller: "search", action:"search"){searchType="CAUSE";type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGETByCAUSE"}
        name en_searcherSearchPARTICIPATORY_BUDGETByCAUSE:"/search/participatory-budget/cause/$word?"      (controller: "search", action:"search"){searchType="CAUSE";type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGETByCAUSE"}
        name es_searcherSearchPARTICIPATORY_BUDGETByCAUSE:"/buscar/presupuesto-participativo/causa/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGETByCAUSE"}
        name de_searcherSearchPARTICIPATORY_BUDGETByCAUSE:"/suche/burgerhaushalt/themen/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGETByCAUSE"}
        name ca_searcherSearchPARTICIPATORY_BUDGETByCAUSE:"/cerca/pressupost-participatiu/causa/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGETByCAUSE"}

        name searcherSearchPARTICIPATORY_BUDGETByREGION:   "/search/participatory-budget/from/$word?"      (controller: "search", action:"search"){searchType="REGION";type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGETByREGION"}
        name en_searcherSearchPARTICIPATORY_BUDGETByREGION:"/search/participatory-budget/from/$word?"      (controller: "search", action:"search"){searchType="REGION";type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGETByREGION"}
        name es_searcherSearchPARTICIPATORY_BUDGETByREGION:"/buscar/presupuesto-participativo/en/$word?"      (controller: "search", action:"search"){searchType="REGION";type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGETByREGION"}
        name de_searcherSearchPARTICIPATORY_BUDGETByREGION:"/suche/burgerhaushalt/von/$word?"      (controller: "search", action:"search"){searchType="REGION";type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGETByREGION"}
        name ca_searcherSearchPARTICIPATORY_BUDGETByREGION:"/cerca/pressupost-participatiu/a/$word?"      (controller: "search", action:"search"){searchType="REGION";type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGETByREGION"}


        name searcherSearchDISTRICT_PROPOSAL:   "/search/proposal"                  (controller: "search", action:"search"){solrType="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSAL"}
        name en_searcherSearchDISTRICT_PROPOSAL:"/search/proposal/$word?"           (controller: "search", action:"search"){solrType="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSAL"}
        name es_searcherSearchDISTRICT_PROPOSAL:"/buscar/propuesta/$word?"      (controller: "search", action:"search"){solrType="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSAL"}
        name de_searcherSearchDISTRICT_PROPOSAL:"/suche/vorschlag/$word?"                  (controller: "search", action:"search"){solrType="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSAL"}
        name ca_searcherSearchDISTRICT_PROPOSAL:"/cerca/publicacio/$word?" (controller: "search", action:"search"){solrType="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSAL"}

        name searcherSearchDISTRICT_PROPOSALByCAUSE:   "/search/proposal/cause/$word?"      (controller: "search", action:"search"){searchType="CAUSE";type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSALByCAUSE"}
        name en_searcherSearchDISTRICT_PROPOSALByCAUSE:"/search/proposal/cause/$word?"      (controller: "search", action:"search"){searchType="CAUSE";type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSALByCAUSE"}
        name es_searcherSearchDISTRICT_PROPOSALByCAUSE:"/buscar/propuesta/causa/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSALByCAUSE"}
        name de_searcherSearchDISTRICT_PROPOSALByCAUSE:"/suche/vorschlag/themen/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSALByCAUSE"}
        name ca_searcherSearchDISTRICT_PROPOSALByCAUSE:"/cerca/publicacio/causa/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSALByCAUSE"}

        name searcherSearchDISTRICT_PROPOSALByREGION:   "/search/proposal/from/$word?"      (controller: "search", action:"search"){searchType="REGION";type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSALByREGION"}
        name en_searcherSearchDISTRICT_PROPOSALByREGION:"/search/proposal/from/$word?"      (controller: "search", action:"search"){searchType="REGION";type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSALByREGION"}
        name es_searcherSearchDISTRICT_PROPOSALByREGION:"/buscar/propuesta/en/$word?"      (controller: "search", action:"search"){searchType="REGION";type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSALByREGION"}
        name de_searcherSearchDISTRICT_PROPOSALByREGION:"/suche/vorschlag/von/$word?"      (controller: "search", action:"search"){searchType="REGION";type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSALByREGION"}
        name ca_searcherSearchDISTRICT_PROPOSALByREGION:"/cerca/publicacio/a/$word?"      (controller: "search", action:"search"){searchType="REGION";type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSALByREGION"}

        name searcherSearchPETITION:   "/search/petition"                  (controller: "search", action:"search"){solrType="PETITION"; mappingName="searcherSearchPETITION"}
        name en_searcherSearchPETITION:"/search/petition/$word?"           (controller: "search", action:"search"){solrType="PETITION"; mappingName="searcherSearchPETITION"}
        name es_searcherSearchPETITION:"/buscar/peticion/$word?"      (controller: "search", action:"search"){solrType="PETITION"; mappingName="searcherSearchPETITION"}
        name de_searcherSearchPETITION:"/suche/petition/$word?"                  (controller: "search", action:"search"){solrType="PETITION"; mappingName="searcherSearchPETITION"}
        name ca_searcherSearchPETITION:"/cerca/peticio/$word?" (controller: "search", action:"search"){solrType="PETITION"; mappingName="searcherSearchPETITION"}

        name searcherSearchPETITIONByCAUSE:   "/search/petition/cause/$word?"      (controller: "search", action:"search"){searchType="CAUSE";type="PETITION"; mappingName="searcherSearchPETITIONByCAUSE"}
        name en_searcherSearchPETITIONByCAUSE:"/search/petition/cause/$word?"      (controller: "search", action:"search"){searchType="CAUSE";type="PETITION"; mappingName="searcherSearchPETITIONByCAUSE"}
        name es_searcherSearchPETITIONByCAUSE:"/buscar/peticion/causa/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="PETITION"; mappingName="searcherSearchPETITIONByCAUSE"}
        name de_searcherSearchPETITIONByCAUSE:"/suche/petition/themen/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="PETITION"; mappingName="searcherSearchPETITIONByCAUSE"}
        name ca_searcherSearchPETITIONByCAUSE:"/cerca/peticio/causa/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="PETITION"; mappingName="searcherSearchPETITIONByCAUSE"}

        name searcherSearchPETITIONByREGION:   "/search/petition/from/$word?"      (controller: "search", action:"search"){searchType="REGION";type="PETITION"; mappingName="searcherSearchPETITIONByREGION"}
        name en_searcherSearchPETITIONByREGION:"/search/petition/from/$word?"      (controller: "search", action:"search"){searchType="REGION";type="PETITION"; mappingName="searcherSearchPETITIONByREGION"}
        name es_searcherSearchPETITIONByREGION:"/buscar/peticion/en/$word?"      (controller: "search", action:"search"){searchType="REGION";type="PETITION"; mappingName="searcherSearchPETITIONByREGION"}
        name de_searcherSearchPETITIONByREGION:"/suche/petition/von/$word?"      (controller: "search", action:"search"){searchType="REGION";type="PETITION"; mappingName="searcherSearchPETITIONByREGION"}
        name ca_searcherSearchPETITIONByREGION:"/cerca/peticio/a/$word?"      (controller: "search", action:"search"){searchType="REGION";type="PETITION"; mappingName="searcherSearchPETITIONByREGION"}

        name searcherSearchCONTEST:    "/search/contest"                        (controller: "search", action:"search"){solrType="CONTEST"; mappingName="searcherSearchCONTEST"}
        name en_searcherSearchCONTEST:"/search/contest/$word?"                  (controller: "search", action:"search"){solrType="CONTEST"; mappingName="searcherSearchCONTEST"}
        name es_searcherSearchCONTEST:"/buscar/concurso/$word?"                 (controller: "search", action:"search"){solrType="CONTEST"; mappingName="searcherSearchCONTEST"}
        name de_searcherSearchCONTEST:"/suche/wettbewerb/$word?"                (controller: "search", action:"search"){solrType="CONTEST"; mappingName="searcherSearchCONTEST"}
        name ca_searcherSearchCONTEST:"/cerca/concurs/$word?"                   (controller: "search", action:"search"){solrType="CONTEST"; mappingName="searcherSearchCONTEST"}

        name searcherSearchCONTESTByCAUSE:   "/search/contest/cause/$word?"     (controller: "search", action:"search"){searchType="CAUSE";type="CONTEST"; mappingName="searcherSearchCONTESTByCAUSE"}
        name en_searcherSearchCONTESTByCAUSE:"/search/contest/cause/$word?"     (controller: "search", action:"search"){searchType="CAUSE";type="CONTEST"; mappingName="searcherSearchCONTESTByCAUSE"}
        name es_searcherSearchCONTESTByCAUSE:"/buscar/concurso/causa/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="CONTEST"; mappingName="searcherSearchCONTESTByCAUSE"}
        name de_searcherSearchCONTESTByCAUSE:"/suche/wettbewerb/themen/$word?"  (controller: "search", action:"search"){searchType="CAUSE";type="CONTEST"; mappingName="searcherSearchCONTESTByCAUSE"}
        name ca_searcherSearchCONTESTByCAUSE:"/cerca/concurs/causa/$word?"      (controller: "search", action:"search"){searchType="CAUSE";type="CONTEST"; mappingName="searcherSearchCONTESTByCAUSE"}

        name searcherSearchCONTESTByREGION:   "/search/contest/from/$word?"     (controller: "search", action:"search"){searchType="REGION";type="CONTEST"; mappingName="searcherSearchCONTESTByREGION"}
        name en_searcherSearchCONTESTByREGION:"/search/contest/from/$word?"     (controller: "search", action:"search"){searchType="REGION";type="CONTEST"; mappingName="searcherSearchCONTESTByREGION"}
        name es_searcherSearchCONTESTByREGION:"/buscar/concurso/en/$word?"      (controller: "search", action:"search"){searchType="REGION";type="CONTEST"; mappingName="searcherSearchCONTESTByREGION"}
        name de_searcherSearchCONTESTByREGION:"/suche/wettbewerb/von/$word?"    (controller: "search", action:"search"){searchType="REGION";type="CONTEST"; mappingName="searcherSearchCONTESTByREGION"}
        name ca_searcherSearchCONTESTByREGION:"/cerca/concurs/a/$word?"         (controller: "search", action:"search"){searchType="REGION";type="CONTEST"; mappingName="searcherSearchCONTESTByREGION"}

        name searcherSearchCONTEST_APPLICATION:    "/search/application"                    (controller: "search", action:"search"){solrType="CONTEST_APPLICATION"; mappingName="searcherSearchCONTEST_APPLICATION"}
        name en_searcherSearchCONTEST_APPLICATION:"/search/application/$word?"              (controller: "search", action:"search"){solrType="CONTEST_APPLICATION"; mappingName="searcherSearchCONTEST_APPLICATION"}
        name es_searcherSearchCONTEST_APPLICATION:"/buscar/proyecto/$word?"                 (controller: "search", action:"search"){solrType="CONTEST_APPLICATION"; mappingName="searcherSearchCONTEST_APPLICATION"}
        name de_searcherSearchCONTEST_APPLICATION:"/suche/anwendungen/$word?"               (controller: "search", action:"search"){solrType="CONTEST_APPLICATION"; mappingName="searcherSearchCONTEST_APPLICATION"}
        name ca_searcherSearchCONTEST_APPLICATION:"/cerca/projecte/$word?"                  (controller: "search", action:"search"){solrType="CONTEST_APPLICATION"; mappingName="searcherSearchCONTEST_APPLICATION"}

        name searcherSearchCONTEST_APPLICATIONByCAUSE:   "/search/application/cause/$word?" (controller: "search", action:"search"){searchType="CAUSE";type="CONTEST_APPLICATION"; mappingName="searcherSearchCONTEST_APPLICATIONByCAUSE"}
        name en_searcherSearchCONTEST_APPLICATIONByCAUSE:"/search/application/cause/$word?" (controller: "search", action:"search"){searchType="CAUSE";type="CONTEST_APPLICATION"; mappingName="searcherSearchCONTEST_APPLICATIONByCAUSE"}
        name es_searcherSearchCONTEST_APPLICATIONByCAUSE:"/buscar/proyecto/causa/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="CONTEST_APPLICATION"; mappingName="searcherSearchCONTEST_APPLICATIONByCAUSE"}
        name de_searcherSearchCONTEST_APPLICATIONByCAUSE:"/suche/anwendungen/themen/$word?" (controller: "search", action:"search"){searchType="CAUSE";type="CONTEST_APPLICATION"; mappingName="searcherSearchCONTEST_APPLICATIONByCAUSE"}
        name ca_searcherSearchCONTEST_APPLICATIONByCAUSE:"/cerca/projecte/causa/$word?"     (controller: "search", action:"search"){searchType="CAUSE";type="CONTEST_APPLICATION"; mappingName="searcherSearchCONTEST_APPLICATIONByCAUSE"}

        name searcherSearchCONTEST_APPLICATIONByREGION:   "/search/application/from/$word?" (controller: "search", action:"search"){searchType="REGION";type="CONTEST_APPLICATION"; mappingName="searcherSearchCONTEST_APPLICATIONByREGION"}
        name en_searcherSearchCONTEST_APPLICATIONByREGION:"/search/application/from/$word?" (controller: "search", action:"search"){searchType="REGION";type="CONTEST_APPLICATION"; mappingName="searcherSearchCONTEST_APPLICATIONByREGION"}
        name es_searcherSearchCONTEST_APPLICATIONByREGION:"/buscar/proyecto/en/$word?"      (controller: "search", action:"search"){searchType="REGION";type="CONTEST_APPLICATION"; mappingName="searcherSearchCONTEST_APPLICATIONByREGION"}
        name de_searcherSearchCONTEST_APPLICATIONByREGION:"/suche/anwendungen/von/$word?"   (controller: "search", action:"search"){searchType="REGION";type="CONTEST_APPLICATION"; mappingName="searcherSearchCONTEST_APPLICATIONByREGION"}
        name ca_searcherSearchCONTEST_APPLICATIONByREGION:"/cerca/projecte/a/$word?"        (controller: "search", action:"search"){searchType="REGION";type="CONTEST_APPLICATION"; mappingName="searcherSearchCONTEST_APPLICATIONByREGION"}

    }

}