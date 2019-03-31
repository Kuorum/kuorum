
set @domainId = 14;
set @adminId = (select id from CRM_USER where alias='admin' and domain_id=@domainId);
set @adminEmail = (select email from CRM_USER where id =@adminId);


update CONTACT s set deleted=1 where mongoid in (
  '5c8e008cd9a37d11e3905a5e',
  '5c88da42a9aa248a02b85e79',
  '5c94fb34d9a37d0641029415',
  '5c9baf6fd9a37d0641f748e7',
  '5c88da42a9aa248a02b85e79',
  '5c8d3a2cd9a37d11e3904734'
);

update CRM_USER s set deleted=1 where mongoId in (
  '5c8e008cd9a37d11e3905a5e',
  '5c88da42a9aa248a02b85e79',
  '5c94fb34d9a37d0641029415',
  '5c9baf6fd9a37d0641f748e7',
  '5c88da42a9aa248a02b85e79',
  '5c8d3a2cd9a37d11e3904734'
);


insert into CONTACT
select
  null as id,
  CURDATE() as dateCreated,
  cu.deleted as deleted,
  cu.email as email,
  cu.mongoId as mongoId,
  cu.name as name,
  null as surname,
  null as notes,
  0 as clicks,
  0 as numFollowers,
  0 as numMails,
  0 as opens,
  0 as uploadedContact,
  1 as isFollowen,
  @adminId as owner_id,
  0 as status,
  0 as numSoftBounces,
  0 as numHardBounces,
  0 as blackList,
  1 as subscribed,
  null as language
from CRM_USER cu
where
  cu.email not in (
    select c.email
    from CONTACT c
    where c.owner_id=@adminId
  )
  and cu.domain_id = @domainId
  and cu.email <> @adminEmail
  and cu.deleted = 0;


update CONTACT c set mongoId=(select mongoId from CRM_USER where email=c.email and domain_id = @domainId) where owner_id=@adminId;
#update CONTACT set deleted = 1 where uploadedContact =0 and mongoId is null;


update CONTACT c set deleted=0, isFollower=1 where owner_id=@adminId and mongoId in(
    '5c8a9432a9aac8a3bc6094c2',
    '5c8ccc79a9aa7f196daf336c',
    '5c8d1b1fa9aa7f196daf3380',
    '5c8d2e09a9aa7f196daf3398',
    '5c8d4ba7a9aa7f196daf33a8',
    '5c8d5de1a9aa7f196daf33b2',
    '5c8d8d8fa9aa7f196daf33d4',
    '5c8e0165a9aa7f196daf33e3',
    '5c8e0736a9aa7f196daf33eb',
    '5c8e0be3a9aa7f196daf33f1',
    '5c8f611da9aa7f196daf344a',
    '5c92261da9aa05d4fbbe5f78',
    '5c923308a9aa05d4fbbe5f8b',
    '5c92b25fd9a37d06413e9a16',
    '5c9325eba9aa05d4fbbe5ffb',
    '5c932979d9a37d06413e9e6d',
    '5c932d05d9a37d06413e9e98',
    '5c9346bfd9a37d06413ea10b',
    '5c93cb9ed9a37d06413eab62',
    '5c93e5f0a9aa05d4fbbe6048',
    '5c97d00ed9a37d064102a0a5',
    '5c97f524d9a37d064102a57a',
    '5c982cc9a9aa3136ff777644',
    '5c98a3d9a9aa3136ff777661',
    '5c98a584a9aa3136ff777664',
    '5c8ca0aba9aa7f196daf335e'
);