package payment.campaign

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.register.KuorumUserSession
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.bulletin.BulletinRDTO
import org.kuorum.rest.model.communication.bulletin.BulletinRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO

@Transactional
class BulletinService implements CampaignCreatorService<BulletinRSDTO, BulletinRDTO> {
    
    RestKuorumApiService restKuorumApiService
    CampaignService campaignService

    BulletinRSDTO save(KuorumUserSession user, BulletinRDTO bulletinRDTO, Long campaignId) {

        BulletinRSDTO bulletin = null
        if (campaignId) {
            bulletin = update(user, bulletinRDTO, campaignId)
        } else {
            bulletin = createBulletin(user, bulletinRDTO)
        }
//        indexSolrService.deltaIndex()
        bulletin
    }

    private BulletinRSDTO createBulletin(KuorumUserSession user, BulletinRDTO bulletinRDTO) {
        Map<String, String> params = [userId: user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.ACCOUNT_BULLETIN,
                params,
                query,
                bulletinRDTO,
                new TypeReference<BulletinRSDTO>() {}
        )

        BulletinRSDTO bulletinSaved = null
        if (response.data) {
            bulletinSaved = response.data
        }
        bulletinSaved
    }

    List<BulletinRSDTO> findAll(KuorumUserSession user, String viewerUid = null) {
        Map<String, String> params = [userId: user.getId().toString()]
        Map<String, String> query = [:]
        if (viewerUid) {
            query.put("viewerUid", viewerUid)
        }
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_BULLETINS,
                    params,
                    query,
                    new TypeReference<List<BulletinRSDTO>>() {}
            )

            return response.data ?: null
        } catch (KuorumException e) {
            log.info("Bulletins of user not found [Excpt: ${e.message}")
            return null
        }
    }

    BulletinRSDTO find(KuorumUserSession user, Long campaignId, String viewerUid = null) {
        find(user.id.toString(), campaignId, viewerUid)
    }

    BulletinRSDTO find(String userId, Long campaignId, String viewerUid = null) {
        if (!campaignId) {
            return null
        }
        Map<String, String> params = [userId: userId, campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        if (viewerUid) {
            query.put("viewerUid", viewerUid)
        }
        try {
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.ACCOUNT_BULLETIN,
                    params,
                    query,
                    new TypeReference<BulletinRSDTO>() {}
            )

            return response.data ?: null
        } catch (KuorumException e) {
            log.info("Campaign not found [Excpt: ${e.message}")
            return null
        }
    }

    private BulletinRSDTO update(KuorumUserSession user, BulletinRDTO bulletinRDTO, Long campaignId) {
        Map<String, String> params = [userId: user.id.toString(), campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.put(
                RestKuorumApiService.ApiMethod.ACCOUNT_BULLETIN,
                params,
                query,
                bulletinRDTO,
                new TypeReference<BulletinRSDTO>() {}
        )

        BulletinRSDTO campaignSaved = null
        if (response.data) {
            campaignSaved = response.data
        }

        campaignSaved
    }

    void remove(KuorumUserSession user, Long campaignId) {
        Map<String, String> params = [userId: user.id.toString(), campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.delete(
                RestKuorumApiService.ApiMethod.ACCOUNT_BULLETIN,
                params,
                query
        )

        campaignId
    }

    @Override
    BulletinRDTO map(BulletinRSDTO bulletinRSDTO) {
        return campaignService.basicMapping(bulletinRSDTO, new BulletinRDTO())
    }

    @Override
    def buildView(BulletinRSDTO campaignRSDTO, BasicDataKuorumUserRSDTO campaignOwner, String viewerUid, def params) {
        def model = [bulletin: campaignRSDTO, campaignUser: campaignOwner]
        [view: "/newsletter/show", model: model]
    }

    @Override
    BulletinRSDTO copy(KuorumUserSession user, Long campaignId) {
        if (user == null) {
            return null
        }
        return copy(user.getId().toString(), campaignId)
    }

    @Override
    BulletinRSDTO copy(String userId, Long campaignId) {
        Map<String, String> params = [userId: userId, campaignId: campaignId.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.post(
                RestKuorumApiService.ApiMethod.ACCOUNT_BULLETIN_COPY,
                params,
                query,
                null,
                new TypeReference<BulletinRSDTO>() {}
        )

        BulletinRSDTO bulletinRSDTO = null
        if (response.data) {
            bulletinRSDTO = response.data
        }
        bulletinRSDTO
    }
}
