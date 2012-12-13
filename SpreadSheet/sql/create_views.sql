drop view r;
CREATE VIEW r as
select rd.row_data_id,ch.name,
dd.lvalue,dd.rvalue,dd.dvalue,dd.svalue,dd.bvalue from 
row_data rd, cell_data cd, cell_data_hdr ch, value_data dd
 where cd.cell_hdr_id = ch.cell_hdr_id and cd.value_id=dd.data_id and 
 cd.row_data_id = rd.row_data_id;

drop view s;
CREATE VIEW s AS
select distinct(sd.sheet_data_id),sc.sheet_name,wc.config_name,ch.name,
dd.lvalue,dd.rvalue,dd.dvalue,dd.svalue,dd.bvalue from  value_data dd, cell_data 
cd,cell_data_hdr ch,row_data rd,sheet_data sd, sheet_config sc, workbook_data 
wd, workbook_config wc, workbook_file_data wf where cd.value_id=dd.data_id and 
cd.row_data_id = rd.row_data_id and sd.sheet_data_id=rd.sheet_data_id and 
sd.workbook_id=wd.workbook_id and wd.workbook_config_id = wc.workbook_config_id 
and ch.cell_hdr_id = cd.cell_hdr_id and wf.workbook_file_id = 
wd.workbook_file_id and sd.sheet_config_id = sc.sheet_config_id;

drop view w;
CREATE VIEW w AS
select distinct(wd.workbook_id),wc.config_name from 
meta_data md, meta_data_hdr mh, value_data dd, workbook_data wd, workbook_config 
wc, workbook_file_data wf where md.value=dd.data_id and md.metadata_hdr_id = 
mh.hdr_id and md.workbook_id = wd.workbook_id and wd.workbook_config_id = 
wc.workbook_config_id and wf.workbook_file_id = wd.workbook_file_id;

drop view v;
CREATE VIEW v AS
select ch.name, ch.hdr_index, wd.workbook_id, rd.sheet_data_id, rd.row_data_id, 
wc.config_name, dd.lvalue, dd.rvalue, dd.dvalue, dd.svalue, dd.bvalue from 
value_data dd, cell_data cd,cell_data_hdr ch,row_data rd,sheet_data sd, 
sheet_config sc, workbook_data wd, workbook_config wc where 
cd.value_id=dd.data_id and cd.row_data_id = rd.row_data_id and 
sd.sheet_data_id=rd.sheet_data_id and sd.workbook_id=wd.workbook_id and 
wd.workbook_config_id = wc.workbook_config_id and ch.cell_hdr_id = 
cd.cell_hdr_id and sd.sheet_config_id = sc.sheet_config_id;

drop view m;
CREATE VIEW m AS 
select mh.name, mh.hdr_order, wd.workbook_id, '0', '0', wc.config_name, 
dd.lvalue, dd.rvalue, dd.dvalue, dd.svalue, dd.bvalue from value_data dd, 
meta_data md, meta_data_hdr mh, workbook_data wd, workbook_config wc, 
workbook_file_data wf where md.value=dd.data_id and md.metadata_hdr_id = 
mh.hdr_id and md.workbook_id = wd.workbook_id and wd.workbook_config_id = 
wc.workbook_config_id and wf.workbook_file_id = wd.workbook_file_id;

drop view a;
CREATE VIEW a AS
(select 'attachments','0',wa.workbook_id,'0','0', wc.config_name,
count(wa.attachment_id) as 'count','0','0','0','0' from attachments a, 
attachmentTypes att, workbook_attachments wa, workbook_data wd, 
workbook_config wc where a.type_id = att.type_id and wa.attachment_id = 
a.attachment_id and wd.workbook_config_id = wc.workbook_config_id and 
wa.workbook_id = wd.workbook_id 
group by wa.workbook_id order by wa.workbook_id desc);