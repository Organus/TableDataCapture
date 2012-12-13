

    insert into tracker.amount_units (name) values ("grams"), ("liters"), ("kiloliters"), ("milliliters"),
		("kilograms"), ("milligrams");
    
    insert into tracker.feedstocks (name) values ("sorghum"), ("switchgrass"), ("corn stover"),
		("poplar"), ("miscanthus");
    
    insert into tracker.fractions (name) values ("liquid"), ("solid"), ("slurry");
    
    insert into tracker.treatments (name) values ("feedstock"), ("pretreated"), ("saccharified"), ("fermented"), ("extracted");
    
    insert into tracker.status (name) values ("In Process"), ("Stored"), ("Disposed");
        
insert into origins(name,description) values ('None','None');
insert into origins(name,description) values ('Kraemer','Original Kraemer material');
insert into origins(name,description) values ('Sorghum','Original Generic Sorghum material');
insert into origins(name,description) values ('Honey Sorghum','Honey flavored');
insert into origins(name,description) values ('Sugar Sorghum','Sugar flavored');
insert into origins(name,description) values ('Other','Other');