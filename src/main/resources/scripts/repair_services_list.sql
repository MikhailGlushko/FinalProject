SELECT rpad(concat_ws('',lpad(s.id,3,0),lpad(m.id,3,0)),6,0) as 'kod', s.name as 'group', m.name, s.name_ru as 'group_ru', m.name_ru, m.id
FROM repair_agency.repair_services m
left join repair_agency.repair_services s on m.parent = s.id
order by kod;
