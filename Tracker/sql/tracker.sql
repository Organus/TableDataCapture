
	create database tracker;
	use tracker;

    create table attachments (
        attachment_id bigint not null,
        path varchar(255) not null,
        filename varchar(255) not null,
        primary key (attachment_id)
    );

    create table boolean_data (
        generic_data_id bigint not null,
        value bit,
        primary key (generic_data_id)
    );

    create table date_data (
        generic_data_id bigint not null,
        value datetime,
        primary key (generic_data_id)
    );

    create table generic_data (
        generic_data_id bigint not null,
        primary key (generic_data_id)
    );

    create table group_meta_data (
        group_id bigint not null,
        meta_data_id bigint not null,
        primary key (group_id, meta_data_id)
    );

    create table kind_group_data (
        work_kind_id bigint not null,
        group_id bigint not null,
        primary key (work_kind_id, group_id)
    );

    create table kind_meta_data (
        work_kind_id bigint not null,
        meta_data_id bigint not null,
        primary key (work_kind_id, meta_data_id)
    );

    create table kind_sampleio_data (
        work_kind_id bigint not null,
        sample_io_id bigint not null,
        primary key (work_kind_id, sample_io_id)
    );

    create table long_data (
        generic_data_id bigint not null,
        value bigint,
        primary key (generic_data_id)
    );

    create table real_data (
        generic_data_id bigint not null,
        value double precision,
        primary key (generic_data_id)
    );

    create table sample_attachment (
        tracking_id bigint not null,
        attachment_id bigint not null,
        primary key (tracking_id, attachment_id)
    );

    create table sample_meta_data (
        sample_io_id bigint not null,
        meta_data_id bigint not null,
        primary key (sample_io_id, meta_data_id)
    );

    create table sample_relation (
        relation_id bigint not null,
        id bigint not null,
        primary key (relation_id, id)
    );

    create table string_data (
        generic_data_id bigint not null,
        value varchar(255),
        primary key (generic_data_id)
    );

    create table tracker.amount_units (
        amount_units_id bigint not null auto_increment,
        name varchar(16) comment 'Unit name',
        primary key (amount_units_id)
    );

    create table tracker.configs (
        configs_id bigint not null auto_increment,
        xml_path varchar(255) comment 'path of file on server',
        filename varchar(255) not null comment 'name of file on server',
        date_loaded date comment 'date file was loaded.',
        name varchar(255) comment 'name of config',
        primary key (configs_id)
    );

    create table tracker.data_types (
        type_id bigint not null auto_increment,
        type_name varchar(255) comment 'Data type',
        primary key (type_id)
    );

    create table tracker.feedstocks (
        feedstocks_id bigint not null auto_increment,
        name varchar(255) comment 'Feedstock name',
        primary key (feedstocks_id)
    );

    create table tracker.fractions (
        fractions_id bigint not null auto_increment,
        name varchar(255) comment 'Fraction name',
        primary key (fractions_id)
    );

    create table tracker.group (
        group_id bigint not null auto_increment,
        group_name varchar(255),
        group_internal_id varchar(255) comment 'A unique identifier from the XML.',
        configs_id bigint,
        work_kind_id bigint,
        meta_data_id bigint,
        primary key (group_id)
    );

    create table tracker.location (
        location_id bigint not null auto_increment,
        building varchar(255) comment '',
        room varchar(255) comment '',
        sublocation varchar(255) comment '',
        shelf varchar(255) comment '',
        holder varchar(255) comment '',
        packaging varchar(255) comment '',
        primary key (location_id)
    );

    create table tracker.meta_data (
        meta_data_id bigint not null auto_increment,
        name varchar(255) comment 'Tag name',
        display_order integer comment 'Order that metaData is display on GUI',
        configs_id bigint,
        meta_data_internal_id varchar(255) comment 'A unique identifier from the XML.',
        default_value varchar(255) comment 'Field default value',
        meta_data_type bigint comment 'Field type',
        work_kind_id bigint,
        group_id bigint,
        sample_io_id bigint,
        primary key (meta_data_id)
    );

    create table tracker.meta_data_value (
        meta_data_value_id bigint not null auto_increment,
        meta_data_id bigint,
        entry varchar(16) comment 'List entry for pick-list',
        configs_id bigint,
        primary key (meta_data_value_id)
    );

    create table tracker.origins (
        origin_id bigint not null auto_increment,
        name varchar(255) comment 'origin name',
        description varchar(255) comment 'origin description',
        primary key (origin_id)
    );

    create table tracker.relation (
        relation_id bigint not null auto_increment,
        sample_io_id bigint,
        work_done_id bigint,
        primary key (relation_id)
    );

    create table tracker.sample (
        id bigint not null auto_increment,
        sample_id varchar(255) comment 'Sample Id',
        comment varchar(255),
        amount varchar(8) comment 'Magnitude',
        amount_units_id bigint,
        external_id varchar(255) comment 'ID from outside of system.',
        owner_name varchar(25),
        custodian_name varchar(25),
        create_date date,
        mod_date date,
        treatments_id bigint,
        feedstocks_id bigint,
        fractions_id bigint,
        status_id bigint not null,
        location_id bigint,
        trb_num integer,
        trb_page integer,
        printed bit,
        storage_notes varchar(255) comment '',
        origin bigint,
        description varchar(200) comment '',
        primary key (id)
    );

    create table tracker.sample_io (
        sample_io_id bigint not null auto_increment,
        sample_io_type varchar(255),
        configs_id bigint,
        work_kind_id bigint,
        primary key (sample_io_id)
    );

    create table tracker.status (
        status_id bigint not null auto_increment,
        name varchar(255) comment 'Status name',
        primary key (status_id)
    );

    create table tracker.treatments (
        treatments_id bigint not null auto_increment,
        name varchar(255) comment 'Treatment name',
        primary key (treatments_id)
    );

    create table tracker.work_detail (
        work_detail_id bigint not null auto_increment,
        meta_data_id bigint,
        group_id bigint,
        work_done_id bigint,
        generic_data_id bigint,
        primary key (work_detail_id)
    );

    create table tracker.work_done (
        work_done_id bigint not null auto_increment,
        work_kind_id bigint,
        configs_id bigint,
        when_done date,
        worker varchar(255),
        primary key (work_done_id)
    );

    create table tracker.work_done_trb (
        workDoneTrbId bigint not null auto_increment,
        work_done_id bigint,
        trb_num integer,
        trb_page integer,
        primary key (workDoneTrbId)
    );

    create table tracker.work_kind (
        work_kind_id bigint not null auto_increment,
        name varchar(255) comment 'Display name for work kind',
        work_type_id bigint,
        work_kind_order integer comment 'Display order for work kind',
        work_kind_internal_id varchar(255) comment 'A unique identifier from the XML.',
        configs_id bigint,
        primary key (work_kind_id)
    );

    create table tracker.work_type (
        work_type_id bigint not null auto_increment,
        name varchar(255),
        work_type_order integer comment 'Display order for work type',
        work_type_internal_id varchar(255) comment 'A unique identifier from the XML.',
        configs_id bigint,
        primary key (work_type_id)
    );

    create table work_done_attachment (
        work_done_id bigint not null,
        attachment_id bigint not null,
        primary key (work_done_id, attachment_id)
    );

    alter table boolean_data 
        add index FKC21395614BDC6560 (generic_data_id), 
        add constraint FKC21395614BDC6560 
        foreign key (generic_data_id) 
        references generic_data (generic_data_id);

    alter table date_data 
        add index FKF123559B4BDC6560 (generic_data_id), 
        add constraint FKF123559B4BDC6560 
        foreign key (generic_data_id) 
        references generic_data (generic_data_id);

    alter table group_meta_data 
        add index FK3697ABE45AD5C06E (meta_data_id), 
        add constraint FK3697ABE45AD5C06E 
        foreign key (meta_data_id) 
        references tracker.meta_data (meta_data_id);

    alter table group_meta_data 
        add index FK3697ABE4EB7F4271 (group_id), 
        add constraint FK3697ABE4EB7F4271 
        foreign key (group_id) 
        references tracker.group (group_id);

    alter table kind_group_data 
        add index FK3DA6BEF5F75E7046 (work_kind_id), 
        add constraint FK3DA6BEF5F75E7046 
        foreign key (work_kind_id) 
        references tracker.work_kind (work_kind_id);

    alter table kind_group_data 
        add index FK3DA6BEF5EB7F4271 (group_id), 
        add constraint FK3DA6BEF5EB7F4271 
        foreign key (group_id) 
        references tracker.group (group_id);

    alter table kind_meta_data 
        add index FK1A995AB9F75E7046 (work_kind_id), 
        add constraint FK1A995AB9F75E7046 
        foreign key (work_kind_id) 
        references tracker.work_kind (work_kind_id);

    alter table kind_meta_data 
        add index FK1A995AB95AD5C06E (meta_data_id), 
        add constraint FK1A995AB95AD5C06E 
        foreign key (meta_data_id) 
        references tracker.meta_data (meta_data_id);

    alter table kind_sampleio_data 
        add index FK68FAD4CEF75E7046 (work_kind_id), 
        add constraint FK68FAD4CEF75E7046 
        foreign key (work_kind_id) 
        references tracker.work_kind (work_kind_id);

    alter table kind_sampleio_data 
        add index FK68FAD4CE3F6637D8 (sample_io_id), 
        add constraint FK68FAD4CE3F6637D8 
        foreign key (sample_io_id) 
        references tracker.sample_io (sample_io_id);

    alter table long_data 
        add index FK79B8F8D4BDC6560 (generic_data_id), 
        add constraint FK79B8F8D4BDC6560 
        foreign key (generic_data_id) 
        references generic_data (generic_data_id);

    alter table real_data 
        add index FKCB41A1AB4BDC6560 (generic_data_id), 
        add constraint FKCB41A1AB4BDC6560 
        foreign key (generic_data_id) 
        references generic_data (generic_data_id);

    alter table sample_attachment 
        add index FK3C3AEAD824E953E (attachment_id), 
        add constraint FK3C3AEAD824E953E 
        foreign key (attachment_id) 
        references attachments (attachment_id);

    alter table sample_attachment 
        add index FK3C3AEAD860500716 (tracking_id), 
        add constraint FK3C3AEAD860500716 
        foreign key (tracking_id) 
        references tracker.sample (id);

    alter table sample_meta_data 
        add index FK4A15714F5AD5C06E (meta_data_id), 
        add constraint FK4A15714F5AD5C06E 
        foreign key (meta_data_id) 
        references tracker.meta_data (meta_data_id);

    alter table sample_meta_data 
        add index FK4A15714F3F6637D8 (sample_io_id), 
        add constraint FK4A15714F3F6637D8 
        foreign key (sample_io_id) 
        references tracker.sample_io (sample_io_id);

    alter table sample_relation 
        add index FKF3EA74F1F052508E (id), 
        add constraint FKF3EA74F1F052508E 
        foreign key (id) 
        references tracker.sample (id);

    alter table sample_relation 
        add index FKF3EA74F1D7D3DFC3 (relation_id), 
        add constraint FKF3EA74F1D7D3DFC3 
        foreign key (relation_id) 
        references tracker.relation (relation_id);

    alter table string_data 
        add index FKA5F2AB184BDC6560 (generic_data_id), 
        add constraint FKA5F2AB184BDC6560 
        foreign key (generic_data_id) 
        references generic_data (generic_data_id);

    alter table tracker.group 
        add index FK5E0F67FF75E7046 (work_kind_id), 
        add constraint FK5E0F67FF75E7046 
        foreign key (work_kind_id) 
        references tracker.work_kind (work_kind_id);

    alter table tracker.group 
        add index FK5E0F67F5AD5C06E (meta_data_id), 
        add constraint FK5E0F67F5AD5C06E 
        foreign key (meta_data_id) 
        references tracker.meta_data (meta_data_id);

    alter table tracker.group 
        add index FK5E0F67FB25BE5B3 (group_id), 
        add constraint FK5E0F67FB25BE5B3 
        foreign key (group_id) 
        references tracker.meta_data (meta_data_id);

    alter table tracker.group 
        add index FK5E0F67F151D9B71 (configs_id), 
        add constraint FK5E0F67F151D9B71 
        foreign key (configs_id) 
        references tracker.configs (configs_id);

    alter table tracker.meta_data 
        add index FKC03C8DA4F75E7046 (work_kind_id), 
        add constraint FKC03C8DA4F75E7046 
        foreign key (work_kind_id) 
        references tracker.work_kind (work_kind_id);

    alter table tracker.meta_data 
        add index FKC03C8DA4EB7F4271 (group_id), 
        add constraint FKC03C8DA4EB7F4271 
        foreign key (group_id) 
        references tracker.group (group_id);

    alter table tracker.meta_data 
        add index FKC03C8DA4151D9B71 (configs_id), 
        add constraint FKC03C8DA4151D9B71 
        foreign key (configs_id) 
        references tracker.configs (configs_id);

    alter table tracker.meta_data 
        add index FKC03C8DA43F6637D8 (sample_io_id), 
        add constraint FKC03C8DA43F6637D8 
        foreign key (sample_io_id) 
        references tracker.sample_io (sample_io_id);

    alter table tracker.meta_data_value 
        add index FKFE25B7165AD5C06E (meta_data_id), 
        add constraint FKFE25B7165AD5C06E 
        foreign key (meta_data_id) 
        references tracker.meta_data (meta_data_id);

    alter table tracker.meta_data_value 
        add index FKFE25B716FB23C9BC (meta_data_value_id), 
        add constraint FKFE25B716FB23C9BC 
        foreign key (meta_data_value_id) 
        references tracker.meta_data (meta_data_id);

    alter table tracker.meta_data_value 
        add index FKFE25B716151D9B71 (configs_id), 
        add constraint FKFE25B716151D9B71 
        foreign key (configs_id) 
        references tracker.configs (configs_id);

    alter table tracker.relation 
        add index FKDEF3F9FC8F4D4186 (work_done_id), 
        add constraint FKDEF3F9FC8F4D4186 
        foreign key (work_done_id) 
        references tracker.work_done (work_done_id);

    alter table tracker.relation 
        add index FKDEF3F9FC3F6637D8 (sample_io_id), 
        add constraint FKDEF3F9FC3F6637D8 
        foreign key (sample_io_id) 
        references tracker.sample_io (sample_io_id);

    alter table tracker.sample 
        add index FKC9C775AAF2222523 (feedstocks_id), 
        add constraint FKC9C775AAF2222523 
        foreign key (feedstocks_id) 
        references tracker.feedstocks (feedstocks_id);

    alter table tracker.sample 
        add index FKC9C775AA101A99B1 (fractions_id), 
        add constraint FKC9C775AA101A99B1 
        foreign key (fractions_id) 
        references tracker.fractions (fractions_id);

    alter table tracker.sample 
        add index FKC9C775AA4149D923 (location_id), 
        add constraint FKC9C775AA4149D923 
        foreign key (location_id) 
        references tracker.location (location_id);

    alter table tracker.sample 
        add index FKC9C775AA58EE9C0 (amount_units_id), 
        add constraint FKC9C775AA58EE9C0 
        foreign key (amount_units_id) 
        references tracker.amount_units (amount_units_id);

    alter table tracker.sample 
        add index FKC9C775AA75F3E243 (status_id), 
        add constraint FKC9C775AA75F3E243 
        foreign key (status_id) 
        references tracker.status (status_id);

    alter table tracker.sample 
        add index FKC9C775AA2704D08A (origin), 
        add constraint FKC9C775AA2704D08A 
        foreign key (origin) 
        references tracker.origins (origin_id);

    alter table tracker.sample 
        add index FKC9C775AA89F08D23 (treatments_id), 
        add constraint FKC9C775AA89F08D23 
        foreign key (treatments_id) 
        references tracker.treatments (treatments_id);

    alter table tracker.sample_io 
        add index FK425723DBF75E7046 (work_kind_id), 
        add constraint FK425723DBF75E7046 
        foreign key (work_kind_id) 
        references tracker.work_kind (work_kind_id);

    alter table tracker.sample_io 
        add index FK425723DB151D9B71 (configs_id), 
        add constraint FK425723DB151D9B71 
        foreign key (configs_id) 
        references tracker.configs (configs_id);

    alter table tracker.work_detail 
        add index FKE598693F5AD5C06E (meta_data_id), 
        add constraint FKE598693F5AD5C06E 
        foreign key (meta_data_id) 
        references tracker.meta_data (meta_data_id);

    alter table tracker.work_detail 
        add index FKE598693F8F4D4186 (work_done_id), 
        add constraint FKE598693F8F4D4186 
        foreign key (work_done_id) 
        references tracker.work_done (work_done_id);

    alter table tracker.work_detail 
        add index FKE598693F4BDC6560 (generic_data_id), 
        add constraint FKE598693F4BDC6560 
        foreign key (generic_data_id) 
        references generic_data (generic_data_id);

    alter table tracker.work_detail 
        add index FKE598693FEB7F4271 (group_id), 
        add constraint FKE598693FEB7F4271 
        foreign key (group_id) 
        references tracker.group (group_id);

    alter table tracker.work_done 
        add index FK40F8D7F0F75E7046 (work_kind_id), 
        add constraint FK40F8D7F0F75E7046 
        foreign key (work_kind_id) 
        references tracker.work_kind (work_kind_id);

    alter table tracker.work_done 
        add index FK40F8D7F0151D9B71 (configs_id), 
        add constraint FK40F8D7F0151D9B71 
        foreign key (configs_id) 
        references tracker.configs (configs_id);

    alter table tracker.work_done_trb 
        add index FKE81850D58F4D4186 (work_done_id), 
        add constraint FKE81850D58F4D4186 
        foreign key (work_done_id) 
        references tracker.work_done (work_done_id);

    alter table tracker.work_kind 
        add index FK40FBF002151D9B71 (configs_id), 
        add constraint FK40FBF002151D9B71 
        foreign key (configs_id) 
        references tracker.configs (configs_id);

    alter table tracker.work_kind 
        add index FK40FBF002EEE50686 (work_type_id), 
        add constraint FK40FBF002EEE50686 
        foreign key (work_type_id) 
        references tracker.work_type (work_type_id);

    alter table tracker.work_type 
        add index FK410043A8151D9B71 (configs_id), 
        add constraint FK410043A8151D9B71 
        foreign key (configs_id) 
        references tracker.configs (configs_id);

    alter table work_done_attachment 
        add index FK284B80D28F4D4186 (work_done_id), 
        add constraint FK284B80D28F4D4186 
        foreign key (work_done_id) 
        references tracker.work_done (work_done_id);

    alter table work_done_attachment 
        add index FK284B80D224E953E (attachment_id), 
        add constraint FK284B80D224E953E 
        foreign key (attachment_id) 
        references attachments (attachment_id);

commit;
