
    create table attachmentTypes (
        type_id bigint not null,
        ext varchar(255) not null,
        primary key (type_id)
    );

    create table attachments (
        attachment_id bigint not null,
        path varchar(255) not null,
        filename varchar(255) not null,
        type_id bigint,
        primary key (attachment_id)
    );

    create table sample_attachment (
        tracking_id bigint not null,
        attachment_id bigint not null,
        primary key (tracking_id, attachment_id)
    );

    create table tracker.amount_units (
        amount_units_id bigint not null auto_increment,
        name varchar(16) comment 'Unit name',
        primary key (amount_units_id)
    );

    create table tracker.audit (
        audit_id bigint not null auto_increment,
        id bigint,
        printDate date,
        primary key (audit_id)
    );

    create table tracker.compositions (
        comp_id bigint not null auto_increment,
        name varchar(255) comment 'composition name',
        primary key (comp_id)
    );

    create table tracker.custodians (
        custodians_id bigint not null auto_increment,
        name varchar(255) comment 'custodian name',
        primary key (custodians_id)
    );

    create table tracker.destinations (
        destinations_id bigint not null auto_increment,
        name varchar(255) comment 'destination name',
        description varchar(255) comment 'destination description',
        primary key (destinations_id)
    );

    create table tracker.feedstocks (
        feedstocks_id bigint not null auto_increment,
        name varchar(255) comment 'Feedstock name',
        primary key (feedstocks_id)
    );

    create table tracker.forms (
        forms_id bigint not null auto_increment,
        name varchar(255) comment 'forms name',
        primary key (forms_id)
    );

    create table tracker.fractions (
        fractions_id bigint not null auto_increment,
        name varchar(255) comment 'Fraction name',
        primary key (fractions_id)
    );

    create table tracker.id_gen (
        id bigint not null auto_increment,
        type varchar(10) not null,
        description longtext,
        value bigint,
        primary key (id)
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

    create table tracker.origins (
        origin_id bigint not null auto_increment,
        name varchar(255) comment 'origin name',
        description varchar(255) comment 'origin description',
        primary key (origin_id)
    );

    create table tracker.printers (
        printer_id bigint not null auto_increment,
        name varchar(50) comment 'Status name',
        remote_printer varchar(50) comment 'remote printer name',
        machine varchar(50) comment 'Status name',
        status bit,
        primary key (printer_id)
    );

    create table tracker.sample (
        id bigint not null auto_increment,
        sample_id varchar(255) comment 'Sample Id',
        comment varchar(255),
        amount varchar(8) comment 'Magnitude',
        amount_units_id bigint,
        external_id varchar(255) comment 'ID from outside of system.',
        owner_name varchar(255),
        custodian_name varchar(255),
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
        fire varchar(2) comment '',
        reactivity varchar(255) comment '',
        health varchar(2) comment '',
        specific_hazard varchar(5) comment '',
        origin bigint,
        description varchar(255) comment '',
        biomass_lot varchar(255) comment '',
        form bigint,
        strain bigint,
        destination bigint,
        composition bigint,
        primary key (id)
    );

    create table tracker.sources (
        sources_id bigint not null auto_increment,
        name varchar(255) comment 'source name',
        primary key (sources_id)
    );

    create table tracker.status (
        status_id bigint not null auto_increment,
        name varchar(255) comment 'Status name',
        primary key (status_id)
    );

    create table tracker.strains (
        strains_id bigint not null auto_increment,
        name varchar(255) comment 'strain name',
        primary key (strains_id)
    );

    create table tracker.treatments (
        treatments_id bigint not null auto_increment,
        name varchar(255) comment 'treatment name',
        primary key (treatments_id)
    );

    alter table attachments 
        add index FKD3F3CBB087A108DA (type_id), 
        add constraint FKD3F3CBB087A108DA 
        foreign key (type_id) 
        references attachmentTypes (type_id);

    alter table sample_attachment 
        add index FK3C3AEAD8335144EA (attachment_id), 
        add constraint FK3C3AEAD8335144EA 
        foreign key (attachment_id) 
        references attachments (attachment_id);

    alter table sample_attachment 
        add index FK3C3AEAD89B13BEA (tracking_id), 
        add constraint FK3C3AEAD89B13BEA 
        foreign key (tracking_id) 
        references tracker.sample (id);

    alter table tracker.audit 
        add index FK58D9BDB99B38562 (id), 
        add constraint FK58D9BDB99B38562 
        foreign key (id) 
        references tracker.sample (id);

    alter table tracker.sample 
        add index FKC9C775AAEB74CFF7 (feedstocks_id), 
        add constraint FKC9C775AAEB74CFF7 
        foreign key (feedstocks_id) 
        references tracker.feedstocks (feedstocks_id);

    alter table tracker.sample 
        add index FKC9C775AAF71D445D (fractions_id), 
        add constraint FKC9C775AAF71D445D 
        foreign key (fractions_id) 
        references tracker.fractions (fractions_id);

    alter table tracker.sample 
        add index FKC9C775AAE36C4EB6 (form), 
        add constraint FKC9C775AAE36C4EB6 
        foreign key (form) 
        references tracker.forms (forms_id);

    alter table tracker.sample 
        add index FKC9C775AA173128F7 (location_id), 
        add constraint FKC9C775AA173128F7 
        foreign key (location_id) 
        references tracker.location (location_id);

    alter table tracker.sample 
        add index FKC9C775AA3691996C (amount_units_id), 
        add constraint FKC9C775AA3691996C 
        foreign key (amount_units_id) 
        references tracker.amount_units (amount_units_id);

    alter table tracker.sample 
        add index FKC9C775AA883C8016 (strain), 
        add constraint FKC9C775AA883C8016 
        foreign key (strain) 
        references tracker.strains (strains_id);

    alter table tracker.sample 
        add index FKC9C775AA1F551717 (status_id), 
        add constraint FKC9C775AA1F551717 
        foreign key (status_id) 
        references tracker.status (status_id);

    alter table tracker.sample 
        add index FKC9C775AAC55D5B10 (destination), 
        add constraint FKC9C775AAC55D5B10 
        foreign key (destination) 
        references tracker.destinations (destinations_id);

    alter table tracker.sample 
        add index FKC9C775AA552C2AD7 (composition), 
        add constraint FKC9C775AA552C2AD7 
        foreign key (composition) 
        references tracker.compositions (comp_id);

    alter table tracker.sample 
        add index FKC9C775AAA9CA3636 (origin), 
        add constraint FKC9C775AAA9CA3636 
        foreign key (origin) 
        references tracker.origins (origin_id);

    alter table tracker.sample 
        add index FKC9C775AA834337F7 (treatments_id), 
        add constraint FKC9C775AA834337F7 
        foreign key (treatments_id) 
        references tracker.treatments (treatments_id);
