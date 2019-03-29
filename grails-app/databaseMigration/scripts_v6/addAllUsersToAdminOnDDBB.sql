
set @domainId = 14;
set @adminId = (select id from CRM_USER where alias='admin' and domain_id=@domainId);
set @adminEmail = (select email from CRM_USER where id =@adminId);

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

update CONTACT s set deleted=1 where mongoid in (
  '5c8e008cd9a37d11e3905a5e',
  '5c94fb34d9a37d0641029415',
  '5c9baf6fd9a37d0641f748e7',
  '5c8d3a2cd9a37d11e3904734'
);
