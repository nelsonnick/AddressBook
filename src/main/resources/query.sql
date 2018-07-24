SELECT
  county.id AS countyId,
	county.name AS countyName,
	bureau.id AS bureauId,
	bureau.name AS bureauName,
	bureau.duty AS bureauDuty,
	bureau.remark AS dbureauRemark,
  department.id AS departmentId,
	department.name AS departmentName,
	department.address AS departmentAddress,
	department.img AS departmentImg,
	department.latitude AS departmentLatitude,
	department.longitude AS departmentLongitude,
	department.duty AS departmentDuty,
	department.remark AS departmentRemark,
  contact.id AS contactId,
	contact.name AS contactName,
	contact.phone AS contactPhone,
	contact.office AS contactOffice
FROM
	county
LEFT JOIN bureau ON county.id = bureau.cid
LEFT JOIN department ON bureau.id = department.bid
LEFT JOIN contact ON department.id = contact.did
WHERE contact.id = 1
ORDER BY
	bureau.id,
	department.id