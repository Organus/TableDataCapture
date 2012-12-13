package gov.nrel.nbc.labelprinting.dao;

import gov.nrel.nbc.labelprinting.client.AppConstants;
import gov.nrel.nbc.labelprinting.client.SampleCriteria;
import gov.nrel.nbc.labelprinting.model.AttachmentType;
import gov.nrel.nbc.labelprinting.model.Attachments;
import gov.nrel.nbc.labelprinting.model.Location;
import gov.nrel.nbc.labelprinting.model.Sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.mortbay.log.Log;

/**
 * Implements the data access operations using Hibernate APIs.
 * 
 * @author jalbersh
 *
 */
public class SampleDAOHibernate extends GenericHibernateDAO<Sample, Long> implements
		SampleDAO, AppConstants {
	
	private static final String PERCENT = "%";
	private static final String ASTERISK = "*";

	/**
	 * Public method to find a Sample by ID.
	 * 
	 * @param id <Long> ID to search by.
	 * @param lock boolean Locking
	 * 
	 * @return <Sample>
	 */
	public Sample findById(Long id, boolean lock) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Sample.class)
			.add(Restrictions.eq(ID, id));
		
		return (Sample)crit.uniqueResult();
	}
	
	/**
	 * Public method to find a List of <Sample> by sample data
	 * 
	 * @param criteria <SampleCriteria> Sample data
	 * @return <List<Sample>> 
	 */
	@SuppressWarnings("unchecked")
	public List<Sample> findByCriteria(SampleCriteria criteria) {

		Session session = getSession(); 
		
		Criteria crit = session.createCriteria(Sample.class);
		
		if (criteria.getTrackingId() != 0) {
			crit.add(Restrictions.eq(ID, criteria.getTrackingId()));
		}
		
		if ((criteria.getSampleId() != null) && (!criteria.getSampleId().isEmpty())) {
			String value = criteria.getSampleId();
			value = value.replace(ASTERISK, PERCENT);
			crit.add(Restrictions.like(SAMPLE_ID, value));
		}
		
		if ((criteria.getComments() != null) && (!criteria.getComments().isEmpty())) {
			String value = criteria.getComments();
			value = value.replace(ASTERISK, PERCENT);
			crit.add(Restrictions.like(COMMENT, value));
		}
		
		if ((criteria.getStorageNotes() != null) && (!criteria.getStorageNotes().isEmpty())) {
			String value = criteria.getStorageNotes();
			value = value.replace(ASTERISK, PERCENT);
			crit.add(Restrictions.like(STORAGE_NOTES, value));
		}
		
		if ((criteria.getAmount() != null) && (!criteria.getAmount().isEmpty())) {
			crit.add(Restrictions.like(AMOUNT, criteria.getAmount()));
		}
		
		if ((criteria.getUnits() != null) && (!criteria.getUnits().isEmpty())) {
			crit.setFetchMode(UNITS, FetchMode.JOIN);
			crit.createCriteria(UNITS)
				.add(Restrictions.eq(NAME, criteria.getUnits()));
		}
		
		if (criteria.getStartCreateDate() != null && criteria.getEndCreateDate() != null) {
			crit.add(Restrictions.between(CREATE_DATE, criteria.getStartCreateDate(), criteria.getEndCreateDate()));
		} else if (criteria.getStartCreateDate() != null) {
			crit.add(Restrictions.ge(CREATE_DATE, criteria.getStartCreateDate()));
		} else if (criteria.getEndCreateDate() != null) {
			crit.add(Restrictions.le(CREATE_DATE, criteria.getEndCreateDate()));
		}
		
		if ((criteria.getExternalId() != null) && (!criteria.getExternalId().isEmpty())) {
			String value = criteria.getExternalId();
			value = value.replace(ASTERISK, PERCENT);
			crit.add(Restrictions.like(EXTERNAL_ID, value));
		}
		
		if ((criteria.getCustodianName() != null) && (!criteria.getCustodianName().isEmpty())) {
			String value = criteria.getCustodianName();
			value = value.replace(ASTERISK, PERCENT);
			crit.add(Restrictions.like(CUSTODIAN_NAME, value));
		}
		
		if ((criteria.getOwnerName() != null) && (!criteria.getOwnerName().isEmpty())) {
			String value = criteria.getOwnerName();
			value = value.replace(ASTERISK, PERCENT);
			crit.add(Restrictions.like(OWNER_NAME, value));
		}
		
		if ((criteria.getFeedstock() != null) && (!criteria.getFeedstock().isEmpty())) {
			String value = criteria.getFeedstock();
			value = value.replace(ASTERISK, PERCENT);
			crit.createCriteria(FEEDSTOCK)
				.add(Restrictions.like(NAME, value));
		}
		
		if ((criteria.getTreatment() != null) && (!criteria.getTreatment().isEmpty())) {
			String value = criteria.getTreatment();
			value = value.replace(ASTERISK, PERCENT);
			crit.createCriteria(TREATMENT)
				.add(Restrictions.like(NAME, value));
		}
		
		if ((criteria.getDestination() != null) && (!criteria.getDestination().isEmpty())) {
			String value = criteria.getDestination();
			value = value.replace(ASTERISK, PERCENT);
			crit.createCriteria(DESTINATION_NAME)
				.add(Restrictions.like(NAME, value));
		}
		
		if ((criteria.getOrigin() != null) && (!criteria.getOrigin().isEmpty())) {
			String value = criteria.getOrigin();
			value = value.replace(ASTERISK, PERCENT);
			crit.createCriteria(ORIGIN)
				.add(Restrictions.like(NAME, value));
		}
		
		if ((criteria.getFraction() != null) && (!criteria.getFraction().isEmpty())) {
			String value = criteria.getFraction();
			value = value.replace(ASTERISK, PERCENT);
			crit.createCriteria(FRACTION)
				.add(Restrictions.like(NAME, value));
		}
		
		if ((criteria.getForm() != null) && (!criteria.getForm().isEmpty())) {
			String value = criteria.getForm();
			value = value.replace(ASTERISK, PERCENT);
			crit.createCriteria(FORM_NAME)
				.add(Restrictions.like(NAME, value));
		}
		
		if ((criteria.getBiomass_lots() != null) && (!criteria.getBiomass_lots().isEmpty())) {
			String value = criteria.getBiomass_lots();
			value = value.replace(ASTERISK, PERCENT);
			crit.add(Restrictions.like(BIOMASS_LOT, value));
		}
		
		if ((criteria.getStrain() != null) && (!criteria.getStrain().isEmpty())) {
			String value = criteria.getStrain();
			value = value.replace(ASTERISK, PERCENT);
			crit.createCriteria(STRAIN_NAME)
				.add(Restrictions.like(NAME, value));
		}
		
		if ((criteria.getComposition() != null) && (!criteria.getComposition().isEmpty())) {
			String value = criteria.getComposition();
			value = value.replace(ASTERISK, PERCENT);
			crit.createCriteria(COMPOSITION_NAME)
				.add(Restrictions.like(NAME, value));
		}
		
		if ((criteria.getStatus() != null) && (!criteria.getStatus().isEmpty())) {
			String value = criteria.getStatus();
			value = value.replace(ASTERISK, PERCENT);
			crit.createCriteria(STATUS)
				.add(Restrictions.like(NAME, value));
		}
		
	    if (criteria.getAttachment_ext() != null && !criteria.getAttachment_ext().isEmpty()) {
			AttachmentTypeDAOHibernate adh = new AttachmentTypeDAOHibernate();
			adh.setSession(session);
			Iterator<String> ait = criteria.getAttachment_ext().iterator();
			while (ait.hasNext()) {
				String ext = ait.next();
				AttachmentType type = adh.findByExtension(ext);
				if (type != null) {
					Set<Long> wids = new HashSet<Long>();
					List<Sample> samples = findByAttachmentExtension(type);
					Iterator<Sample> sit = samples.iterator();
					while (sit.hasNext()) {
						Sample s = sit.next();
						wids.add(s.getId());
					}
					crit.add(Restrictions.in(ID, wids));
				}
			}
	    }
	    
	    if (criteria.getTrackingIds() != null && !criteria.getTrackingIds().isEmpty()) {
			crit.add(Restrictions.in(ID, criteria.getTrackingIds()));
	    }
	    
		if ((criteria.getBuilding() != null) && (!criteria.getBuilding().isEmpty()) ||
				((criteria.getRoom() != null) && (!criteria.getRoom().isEmpty())) ||
				((criteria.getHolder() != null) && (!criteria.getHolder().isEmpty())) ||
				((criteria.getShelf() != null) && (!criteria.getShelf().isEmpty())) ||
				((criteria.getPackaging() != null) && (!criteria.getPackaging().isEmpty())) ||
				((criteria.getSubLocation() != null) && (!criteria.getSubLocation().isEmpty()))) {
			
			Criteria crit2 = crit.createCriteria(LOCATION);
			if ((criteria.getBuilding() != null) && (!criteria.getBuilding().isEmpty())) {
				String value = criteria.getBuilding();
				value = value.replace(ASTERISK, PERCENT);
				crit2.add(Restrictions.like(BUILDING, value));
			}
			
			if ((criteria.getRoom() != null) && (!criteria.getRoom().isEmpty())) {
				String value = criteria.getRoom();
				value = value.replace(ASTERISK, PERCENT);
				crit2.add(Restrictions.like(ROOM, value));
			}
			
			if ((criteria.getHolder() != null) && (!criteria.getHolder().isEmpty())) {
				String value = criteria.getHolder();
				value = value.replace(ASTERISK, PERCENT);
				crit2.add(Restrictions.like(HOLDER, value));
			}
			
			if ((criteria.getShelf() != null) && (!criteria.getShelf().isEmpty())) {
				String value = criteria.getShelf();
				value = value.replace(ASTERISK, PERCENT);
				crit2.add(Restrictions.like(SHELF, value));
			}
			
			if ((criteria.getPackaging() != null) && (!criteria.getPackaging().isEmpty())) {
				String value = criteria.getPackaging();
				value = value.replace(ASTERISK, PERCENT);
				crit2.add(Restrictions.like(PACKAGING, value));
			}
			
			if ((criteria.getSubLocation() != null) && (!criteria.getSubLocation().isEmpty())) {
				String value = criteria.getSubLocation();
				value = value.replace(ASTERISK, PERCENT);
				crit2.add(Restrictions.like(SUBLOCATION, value));
			}
		}
		
		if (criteria.getTrbNum() != 0) {
			crit.add(Restrictions.eq(TRB_NUM, criteria.getTrbNum()));
		}
		
		if (criteria.getTrbPage() != 0) {
			crit.add(Restrictions.eq(TRB_PAGE, criteria.getTrbPage()));
		}
				
		crit.addOrder(Order.desc(ID));
		return (List<Sample>)crit.list();
	}

	/**
	 * Public method to find a List of <Sample> by sample data
	 * 
	 * @param criteria <SampleCriteria> Sample data
	 * @return <List<Sample>> 
	 */
	@SuppressWarnings("unchecked")
	public List<Sample> findByCriteria(List<SampleCriteria> criterias, int start, int pageSize) {

		Session session = getSession(); 
		
		Criteria crit = session.createCriteria(Sample.class);
		crit.setFirstResult((start*pageSize));
		if (pageSize>0)
			crit.setMaxResults(pageSize);
		
		Iterator<SampleCriteria> cit = criterias.iterator();
		while (cit.hasNext()) {
			SampleCriteria criteria = cit.next();
			String operator=criteria.getOperator();
			int oper=0;
			// = 1, > 2, < 3, != 4
			if (operator == null) oper=1;
			else if (operator.isEmpty()) oper=1;
			else if (operator.equals("=")) oper=1;
			else if (operator.equals(">")) oper=2;
			else if (operator.equals("<")) oper=3;
			else if (operator.equals("!=")) oper=4;
		
			if (criteria.getTrackingId() != 0) {
				switch (oper) {
				case 1: crit.add(Restrictions.eq(ID, criteria.getTrackingId())); break;
				case 2: crit.add(Restrictions.gt(ID, criteria.getTrackingId())); break;
				case 3: crit.add(Restrictions.lt(ID, criteria.getTrackingId())); break;
				case 4: crit.add(Restrictions.ne(ID, criteria.getTrackingId())); break;				
				}
			}
			
			if ((criteria.getSampleId() != null) && (!criteria.getSampleId().isEmpty())) {
				String value = criteria.getSampleId();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.add(Restrictions.like(SAMPLE_ID, value));break;
				case 2: crit.add(Restrictions.gt(SAMPLE_ID, value));break;
				case 3: crit.add(Restrictions.lt(SAMPLE_ID, value));break;
				case 4: crit.add(Restrictions.ne(SAMPLE_ID, value));break;
				}
			}
			
			if ((criteria.getComments() != null) && (!criteria.getComments().isEmpty())) {
				String value = criteria.getComments();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.add(Restrictions.like(COMMENT, value));break;
				case 2:crit.add(Restrictions.gt(COMMENT, value));break;
				case 3:crit.add(Restrictions.lt(COMMENT, value));break;
				case 4:crit.add(Restrictions.ne(COMMENT, value));break;
				}
			}
			
			if ((criteria.getStorageNotes() != null) && (!criteria.getStorageNotes().isEmpty())) {
				String value = criteria.getStorageNotes();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.add(Restrictions.like(STORAGE_NOTES, value));break;
				case 2:crit.add(Restrictions.gt(STORAGE_NOTES, value));break;
				case 3:crit.add(Restrictions.lt(STORAGE_NOTES, value));break;
				case 4:crit.add(Restrictions.ne(STORAGE_NOTES, value));break;
				}
			}
			
			if ((criteria.getAmount() != null) && (!criteria.getAmount().isEmpty())) {
				switch (oper) {
				case 1:crit.add(Restrictions.like(AMOUNT, criteria.getAmount()));break;
				case 2:crit.add(Restrictions.gt(AMOUNT, criteria.getAmount()));break;
				case 3:crit.add(Restrictions.lt(AMOUNT, criteria.getAmount()));break;
				case 4:crit.add(Restrictions.ne(AMOUNT, criteria.getAmount()));break;
				}
			}
			
			if ((criteria.getUnits() != null) && (!criteria.getUnits().isEmpty())) {
				crit.setFetchMode(UNITS, FetchMode.JOIN);
				switch (oper) {
				case 1:crit.createCriteria(UNITS).add(Restrictions.eq(NAME, criteria.getUnits()));break;
				case 2:crit.createCriteria(UNITS).add(Restrictions.gt(NAME, criteria.getUnits()));break;
				case 3:crit.createCriteria(UNITS).add(Restrictions.lt(NAME, criteria.getUnits()));break;
				case 4:crit.createCriteria(UNITS).add(Restrictions.ne(NAME, criteria.getUnits()));break;
				}
			}
			
			if (criteria.getStartCreateDate() != null && criteria.getEndCreateDate() != null) {
				crit.add(Restrictions.between(CREATE_DATE, criteria.getStartCreateDate(), criteria.getEndCreateDate()));break;
			} else if (criteria.getStartCreateDate() != null) {
				switch (oper) {
				case 1: crit.add(Restrictions.eq(CREATE_DATE, criteria.getStartCreateDate()));break;
				case 2: crit.add(Restrictions.ge(CREATE_DATE, criteria.getStartCreateDate()));break;
				case 3: crit.add(Restrictions.le(CREATE_DATE, criteria.getStartCreateDate()));break;
				case 4: crit.add(Restrictions.ne(CREATE_DATE, criteria.getStartCreateDate()));break;
				}
			} else if (criteria.getEndCreateDate() != null) {
				switch (oper) {
				case 1: crit.add(Restrictions.eq(CREATE_DATE, criteria.getEndCreateDate()));break;
				case 2: crit.add(Restrictions.ge(CREATE_DATE, criteria.getEndCreateDate()));break;
				case 3: crit.add(Restrictions.le(CREATE_DATE, criteria.getEndCreateDate()));break;
				case 4: crit.add(Restrictions.ne(CREATE_DATE, criteria.getEndCreateDate()));break;
				}
			}
			
			if ((criteria.getExternalId() != null) && (!criteria.getExternalId().isEmpty())) {
				String value = criteria.getExternalId();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.add(Restrictions.like(EXTERNAL_ID, value));break;
				case 2:crit.add(Restrictions.gt(EXTERNAL_ID, value));break;
				case 3:crit.add(Restrictions.lt(EXTERNAL_ID, value));break;
				case 4:crit.add(Restrictions.ne(EXTERNAL_ID, value));break;
				}
			}
			
			if ((criteria.getCustodianName() != null) && (!criteria.getCustodianName().isEmpty())) {
				String value = criteria.getCustodianName();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.add(Restrictions.like(CUSTODIAN_NAME, value));break;
				case 2: crit.add(Restrictions.gt(CUSTODIAN_NAME, value));break;
				case 3: crit.add(Restrictions.lt(CUSTODIAN_NAME, value));break;
				case 4: crit.add(Restrictions.ne(CUSTODIAN_NAME, value));break;
				}
			}
			
			if ((criteria.getOwnerName() != null) && (!criteria.getOwnerName().isEmpty())) {
				String value = criteria.getOwnerName();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.add(Restrictions.like(OWNER_NAME, value));break;
				case 2: crit.add(Restrictions.gt(OWNER_NAME, value));break;
				case 3: crit.add(Restrictions.lt(OWNER_NAME, value));break;
				case 4: crit.add(Restrictions.ne(OWNER_NAME, value));break;
				}
			}
			
			if ((criteria.getFeedstock() != null) && (!criteria.getFeedstock().isEmpty())) {
				String value = criteria.getFeedstock();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.createCriteria(FEEDSTOCK).add(Restrictions.like(NAME, value));break;
				case 2: crit.createCriteria(FEEDSTOCK).add(Restrictions.gt(NAME, value));break;
				case 3: crit.createCriteria(FEEDSTOCK).add(Restrictions.lt(NAME, value));break;
				case 4: crit.createCriteria(FEEDSTOCK).add(Restrictions.ne(NAME, value));break;
				}
			}
			
			if ((criteria.getTreatment() != null) && (!criteria.getTreatment().isEmpty())) {
				String value = criteria.getTreatment();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.createCriteria(TREATMENT).add(Restrictions.like(NAME, value));break;
				case 2: crit.createCriteria(TREATMENT).add(Restrictions.gt(NAME, value));break;
				case 3: crit.createCriteria(TREATMENT).add(Restrictions.lt(NAME, value));break;
				case 4: crit.createCriteria(TREATMENT).add(Restrictions.ne(NAME, value));break;
				}
			}
			
			if ((criteria.getDestination() != null) && (!criteria.getDestination().isEmpty())) {
				String value = criteria.getDestination();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.createCriteria(DESTINATION_NAME).add(Restrictions.like(NAME, value));break;
				case 2: crit.createCriteria(DESTINATION_NAME).add(Restrictions.gt(NAME, value));break;
				case 3: crit.createCriteria(DESTINATION_NAME).add(Restrictions.lt(NAME, value));break;
				case 4: crit.createCriteria(DESTINATION_NAME).add(Restrictions.ne(NAME, value));break;
				}
			}
			
			if ((criteria.getOrigin() != null) && (!criteria.getOrigin().isEmpty())) {
				String value = criteria.getOrigin();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.createCriteria(ORIGIN).add(Restrictions.like(NAME, value));break;
				case 2: crit.createCriteria(ORIGIN).add(Restrictions.gt(NAME, value));break;
				case 3: crit.createCriteria(ORIGIN).add(Restrictions.lt(NAME, value));break;
				case 4: crit.createCriteria(ORIGIN).add(Restrictions.ne(NAME, value));break;
				}
			}
			
			if ((criteria.getFraction() != null) && (!criteria.getFraction().isEmpty())) {
				String value = criteria.getFraction();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.createCriteria(FRACTION).add(Restrictions.like(NAME, value));break;
				case 2:crit.createCriteria(FRACTION).add(Restrictions.gt(NAME, value));break;
				case 3:crit.createCriteria(FRACTION).add(Restrictions.lt(NAME, value));break;
				case 4:crit.createCriteria(FRACTION).add(Restrictions.ne(NAME, value));break;
				}
			}
			
			if ((criteria.getForm() != null) && (!criteria.getForm().isEmpty())) {
				String value = criteria.getForm();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.createCriteria(FORM_NAME).add(Restrictions.like(NAME, value));break;
				case 2:crit.createCriteria(FORM_NAME).add(Restrictions.gt(NAME, value));break;
				case 3:crit.createCriteria(FORM_NAME).add(Restrictions.lt(NAME, value));break;
				case 4:crit.createCriteria(FORM_NAME).add(Restrictions.ne(NAME, value));break;
				}
			}
			
			if ((criteria.getBiomass_lots() != null) && (!criteria.getBiomass_lots().isEmpty())) {
				String value = criteria.getBiomass_lots();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.add(Restrictions.like(BIOMASS_LOT, value));break;
				case 2:crit.add(Restrictions.gt(BIOMASS_LOT, value));break;
				case 3:crit.add(Restrictions.lt(BIOMASS_LOT, value));break;
				case 4:crit.add(Restrictions.ne(BIOMASS_LOT, value));break;
				}
			}
			
			if ((criteria.getStrain() != null) && (!criteria.getStrain().isEmpty())) {
				String value = criteria.getStrain();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.createCriteria(STRAIN_NAME).add(Restrictions.like(NAME, value));break;
				case 2:crit.createCriteria(STRAIN_NAME).add(Restrictions.gt(NAME, value));break;
				case 3:crit.createCriteria(STRAIN_NAME).add(Restrictions.lt(NAME, value));break;
				case 4:crit.createCriteria(STRAIN_NAME).add(Restrictions.ne(NAME, value));break;
				}
			}
			
			if ((criteria.getComposition() != null) && (!criteria.getComposition().isEmpty())) {
				String value = criteria.getComposition();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.createCriteria(COMPOSITION_NAME).add(Restrictions.like(NAME, value));break;
				case 2:crit.createCriteria(COMPOSITION_NAME).add(Restrictions.gt(NAME, value));break;
				case 3:crit.createCriteria(COMPOSITION_NAME).add(Restrictions.lt(NAME, value));break;
				case 4:crit.createCriteria(COMPOSITION_NAME).add(Restrictions.ne(NAME, value));break;
				}
			}
			
			if ((criteria.getStatus() != null) && (!criteria.getStatus().isEmpty())) {
				String value = criteria.getStatus();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.createCriteria(STATUS).add(Restrictions.like(NAME, value));break;
				case 2:crit.createCriteria(STATUS).add(Restrictions.gt(NAME, value));break;
				case 3:crit.createCriteria(STATUS).add(Restrictions.lt(NAME, value));break;
				case 4:crit.createCriteria(STATUS).add(Restrictions.ne(NAME, value));break;
				}
			}
			
		    if (criteria.getAttachment_ext() != null && !criteria.getAttachment_ext().isEmpty()) {
				AttachmentTypeDAOHibernate adh = new AttachmentTypeDAOHibernate();
				adh.setSession(session);
				Iterator<String> ait = criteria.getAttachment_ext().iterator();
				while (ait.hasNext()) {
					String ext = ait.next();
					AttachmentType type = adh.findByExtension(ext);
					if (type != null) {
						Set<Long> wids = new HashSet<Long>();
						List<Sample> samples = findByAttachmentExtension(type);
						Iterator<Sample> sit = samples.iterator();
						while (sit.hasNext()) {
							Sample s = sit.next();
							wids.add(s.getId());
						}
						crit.add(Restrictions.in(ID, wids));break;
					}
				}
		    }
		    
		    if (criteria.getTrackingIds() != null && !criteria.getTrackingIds().isEmpty()) {
				crit.add(Restrictions.in(ID, criteria.getTrackingIds()));
		    }
		    
			if ((criteria.getBuilding() != null) && (!criteria.getBuilding().isEmpty()) ||
					((criteria.getRoom() != null) && (!criteria.getRoom().isEmpty())) ||
					((criteria.getHolder() != null) && (!criteria.getHolder().isEmpty())) ||
					((criteria.getShelf() != null) && (!criteria.getShelf().isEmpty())) ||
					((criteria.getPackaging() != null) && (!criteria.getPackaging().isEmpty())) ||
					((criteria.getSubLocation() != null) && (!criteria.getSubLocation().isEmpty()))) {
				
				Criteria critLocation = crit.createCriteria(LOCATION);
				if ((criteria.getBuilding() != null) && (!criteria.getBuilding().isEmpty())) {
					String value = criteria.getBuilding();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:critLocation.add(Restrictions.like(BUILDING, value));break;
					case 2:critLocation.add(Restrictions.gt(BUILDING, value));break;
					case 3:critLocation.add(Restrictions.lt(BUILDING, value));break;
					case 4:critLocation.add(Restrictions.ne(BUILDING, value));break;
					}
				}
				
				if ((criteria.getRoom() != null) && (!criteria.getRoom().isEmpty())) {
					String value = criteria.getRoom();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:critLocation.add(Restrictions.like(ROOM, value));break;
					case 2:critLocation.add(Restrictions.gt(ROOM, value));break;
					case 3:critLocation.add(Restrictions.lt(ROOM, value));break;
					case 4:critLocation.add(Restrictions.ne(ROOM, value));break;
					}
				}
				
				if ((criteria.getHolder() != null) && (!criteria.getHolder().isEmpty())) {
					String value = criteria.getHolder();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:critLocation.add(Restrictions.like(HOLDER, value));break;
					case 2:critLocation.add(Restrictions.gt(HOLDER, value));break;
					case 3:critLocation.add(Restrictions.lt(HOLDER, value));break;
					case 4:critLocation.add(Restrictions.ne(HOLDER, value));break;
					}
				}
				
				if ((criteria.getShelf() != null) && (!criteria.getShelf().isEmpty())) {
					String value = criteria.getShelf();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:critLocation.add(Restrictions.like(SHELF, value));break;
					case 2:critLocation.add(Restrictions.gt(SHELF, value));break;
					case 3:critLocation.add(Restrictions.lt(SHELF, value));break;
					case 4:critLocation.add(Restrictions.ne(SHELF, value));break;
					}
				}
				
				if ((criteria.getPackaging() != null) && (!criteria.getPackaging().isEmpty())) {
					String value = criteria.getPackaging();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:critLocation.add(Restrictions.like(PACKAGING, value));break;
					case 2:critLocation.add(Restrictions.gt(PACKAGING, value));break;
					case 3:critLocation.add(Restrictions.lt(PACKAGING, value));break;
					case 4:critLocation.add(Restrictions.ne(PACKAGING, value));break;
					}
				}
				
				if ((criteria.getSubLocation() != null) && (!criteria.getSubLocation().isEmpty())) {
					String value = criteria.getSubLocation();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:critLocation.add(Restrictions.like(SUBLOCATION, value));break;
					case 2:critLocation.add(Restrictions.gt(SUBLOCATION, value));break;
					case 3:critLocation.add(Restrictions.lt(SUBLOCATION, value));break;
					case 4:critLocation.add(Restrictions.ne(SUBLOCATION, value));break;
					}
				}
			}
			
			if (criteria.getTrbNum() != 0) {
				switch (oper) {
				case 1:crit.add(Restrictions.eq(TRB_NUM, criteria.getTrbNum()));break;
				case 2:crit.add(Restrictions.gt(TRB_NUM, criteria.getTrbNum()));break;
				case 3:crit.add(Restrictions.lt(TRB_NUM, criteria.getTrbNum()));break;
				case 4:crit.add(Restrictions.ne(TRB_NUM, criteria.getTrbNum()));break;
				}
			}
			
			if (criteria.getTrbPage() != 0) {
				switch (oper) {
				case 1:crit.add(Restrictions.eq(TRB_PAGE, criteria.getTrbPage()));break;
				case 2:crit.add(Restrictions.gt(TRB_PAGE, criteria.getTrbPage()));break;
				case 3:crit.add(Restrictions.lt(TRB_PAGE, criteria.getTrbPage()));break;
				case 4:crit.add(Restrictions.ne(TRB_PAGE, criteria.getTrbPage()));break;
				}
			}
		}		
		crit.addOrder(Order.desc(ID));
		return (List<Sample>)crit.list();
	}

	/**
	 * Public method to find a List of <Sample> by sample data
	 * 
	 * @param criteria <SampleCriteria> Sample data
	 * @return <List<Sample>> 
	 */
	@SuppressWarnings("unchecked")
	public int findByCriteria(List<SampleCriteria> criterias) {

		Session session = getSession(); 
		
		Criteria crit = session.createCriteria(Sample.class);
		
		Iterator<SampleCriteria> cit = criterias.iterator();
		while (cit.hasNext()) {
			SampleCriteria criteria = cit.next();
			String operator=criteria.getOperator();
			int oper=0;
			// = 1, > 2, < 3, != 4
			if (operator == null) oper=1;
			else if (operator.isEmpty()) oper=1;
			else if (operator.equals("=")) oper=1;
			else if (operator.equals(">")) oper=2;
			else if (operator.equals("<")) oper=3;
			else if (operator.equals("!=")) oper=4;
		
			if (criteria.getTrackingId() != 0) {
				switch (oper) {
				case 1: crit.add(Restrictions.eq(ID, criteria.getTrackingId())); break;
				case 2: crit.add(Restrictions.gt(ID, criteria.getTrackingId())); break;
				case 3: crit.add(Restrictions.lt(ID, criteria.getTrackingId())); break;
				case 4: crit.add(Restrictions.ne(ID, criteria.getTrackingId())); break;				
				}
			}
			
			if ((criteria.getSampleId() != null) && (!criteria.getSampleId().isEmpty())) {
				String value = criteria.getSampleId();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.add(Restrictions.like(SAMPLE_ID, value));break;
				case 2: crit.add(Restrictions.gt(SAMPLE_ID, value));break;
				case 3: crit.add(Restrictions.lt(SAMPLE_ID, value));break;
				case 4: crit.add(Restrictions.ne(SAMPLE_ID, value));break;
				}
			}
			
			if ((criteria.getComments() != null) && (!criteria.getComments().isEmpty())) {
				String value = criteria.getComments();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.add(Restrictions.like(COMMENT, value));break;
				case 2:crit.add(Restrictions.gt(COMMENT, value));break;
				case 3:crit.add(Restrictions.lt(COMMENT, value));break;
				case 4:crit.add(Restrictions.ne(COMMENT, value));break;
				}
			}
			
			if ((criteria.getStorageNotes() != null) && (!criteria.getStorageNotes().isEmpty())) {
				String value = criteria.getStorageNotes();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.add(Restrictions.like(STORAGE_NOTES, value));break;
				case 2:crit.add(Restrictions.gt(STORAGE_NOTES, value));break;
				case 3:crit.add(Restrictions.lt(STORAGE_NOTES, value));break;
				case 4:crit.add(Restrictions.ne(STORAGE_NOTES, value));break;
				}
			}
			
			if ((criteria.getAmount() != null) && (!criteria.getAmount().isEmpty())) {
				switch (oper) {
				case 1:crit.add(Restrictions.like(AMOUNT, criteria.getAmount()));break;
				case 2:crit.add(Restrictions.gt(AMOUNT, criteria.getAmount()));break;
				case 3:crit.add(Restrictions.lt(AMOUNT, criteria.getAmount()));break;
				case 4:crit.add(Restrictions.ne(AMOUNT, criteria.getAmount()));break;
				}
			}
			
			if ((criteria.getUnits() != null) && (!criteria.getUnits().isEmpty())) {
				crit.setFetchMode(UNITS, FetchMode.JOIN);
				switch (oper) {
				case 1:crit.createCriteria(UNITS).add(Restrictions.eq(NAME, criteria.getUnits()));break;
				case 2:crit.createCriteria(UNITS).add(Restrictions.gt(NAME, criteria.getUnits()));break;
				case 3:crit.createCriteria(UNITS).add(Restrictions.lt(NAME, criteria.getUnits()));break;
				case 4:crit.createCriteria(UNITS).add(Restrictions.ne(NAME, criteria.getUnits()));break;
				}
			}
			
			if (criteria.getStartCreateDate() != null && criteria.getEndCreateDate() != null) {
				crit.add(Restrictions.between(CREATE_DATE, criteria.getStartCreateDate(), criteria.getEndCreateDate()));break;
			} else if (criteria.getStartCreateDate() != null) {
				switch (oper) {
				case 1: crit.add(Restrictions.eq(CREATE_DATE, criteria.getStartCreateDate()));break;
				case 2: crit.add(Restrictions.ge(CREATE_DATE, criteria.getStartCreateDate()));break;
				case 3: crit.add(Restrictions.le(CREATE_DATE, criteria.getStartCreateDate()));break;
				case 4: crit.add(Restrictions.ne(CREATE_DATE, criteria.getStartCreateDate()));break;
				}
			} else if (criteria.getEndCreateDate() != null) {
				switch (oper) {
				case 1: crit.add(Restrictions.eq(CREATE_DATE, criteria.getEndCreateDate()));break;
				case 2: crit.add(Restrictions.ge(CREATE_DATE, criteria.getEndCreateDate()));break;
				case 3: crit.add(Restrictions.le(CREATE_DATE, criteria.getEndCreateDate()));break;
				case 4: crit.add(Restrictions.ne(CREATE_DATE, criteria.getEndCreateDate()));break;
				}
			}
			
			if ((criteria.getExternalId() != null) && (!criteria.getExternalId().isEmpty())) {
				String value = criteria.getExternalId();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.add(Restrictions.like(EXTERNAL_ID, value));break;
				case 2:crit.add(Restrictions.gt(EXTERNAL_ID, value));break;
				case 3:crit.add(Restrictions.lt(EXTERNAL_ID, value));break;
				case 4:crit.add(Restrictions.ne(EXTERNAL_ID, value));break;
				}
			}
			
			if ((criteria.getCustodianName() != null) && (!criteria.getCustodianName().isEmpty())) {
				String value = criteria.getCustodianName();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.add(Restrictions.like(CUSTODIAN_NAME, value));break;
				case 2: crit.add(Restrictions.gt(CUSTODIAN_NAME, value));break;
				case 3: crit.add(Restrictions.lt(CUSTODIAN_NAME, value));break;
				case 4: crit.add(Restrictions.ne(CUSTODIAN_NAME, value));break;
				}
			}
			
			if ((criteria.getOwnerName() != null) && (!criteria.getOwnerName().isEmpty())) {
				String value = criteria.getOwnerName();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.add(Restrictions.like(OWNER_NAME, value));break;
				case 2: crit.add(Restrictions.gt(OWNER_NAME, value));break;
				case 3: crit.add(Restrictions.lt(OWNER_NAME, value));break;
				case 4: crit.add(Restrictions.ne(OWNER_NAME, value));break;
				}
			}
			
			if ((criteria.getFeedstock() != null) && (!criteria.getFeedstock().isEmpty())) {
				String value = criteria.getFeedstock();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.createCriteria(FEEDSTOCK).add(Restrictions.like(NAME, value));break;
				case 2: crit.createCriteria(FEEDSTOCK).add(Restrictions.gt(NAME, value));break;
				case 3: crit.createCriteria(FEEDSTOCK).add(Restrictions.lt(NAME, value));break;
				case 4: crit.createCriteria(FEEDSTOCK).add(Restrictions.ne(NAME, value));break;
				}
			}
			
			if ((criteria.getTreatment() != null) && (!criteria.getTreatment().isEmpty())) {
				String value = criteria.getTreatment();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.createCriteria(TREATMENT).add(Restrictions.like(NAME, value));break;
				case 2: crit.createCriteria(TREATMENT).add(Restrictions.gt(NAME, value));break;
				case 3: crit.createCriteria(TREATMENT).add(Restrictions.lt(NAME, value));break;
				case 4: crit.createCriteria(TREATMENT).add(Restrictions.ne(NAME, value));break;
				}
			}
			
			if ((criteria.getDestination() != null) && (!criteria.getDestination().isEmpty())) {
				String value = criteria.getDestination();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.createCriteria(DESTINATION_NAME).add(Restrictions.like(NAME, value));break;
				case 2: crit.createCriteria(DESTINATION_NAME).add(Restrictions.gt(NAME, value));break;
				case 3: crit.createCriteria(DESTINATION_NAME).add(Restrictions.lt(NAME, value));break;
				case 4: crit.createCriteria(DESTINATION_NAME).add(Restrictions.ne(NAME, value));break;
				}
			}
			
			if ((criteria.getOrigin() != null) && (!criteria.getOrigin().isEmpty())) {
				String value = criteria.getOrigin();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.createCriteria(ORIGIN).add(Restrictions.like(NAME, value));break;
				case 2: crit.createCriteria(ORIGIN).add(Restrictions.gt(NAME, value));break;
				case 3: crit.createCriteria(ORIGIN).add(Restrictions.lt(NAME, value));break;
				case 4: crit.createCriteria(ORIGIN).add(Restrictions.ne(NAME, value));break;
				}
			}
			
			if ((criteria.getFraction() != null) && (!criteria.getFraction().isEmpty())) {
				String value = criteria.getFraction();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.createCriteria(FRACTION).add(Restrictions.like(NAME, value));break;
				case 2:crit.createCriteria(FRACTION).add(Restrictions.gt(NAME, value));break;
				case 3:crit.createCriteria(FRACTION).add(Restrictions.lt(NAME, value));break;
				case 4:crit.createCriteria(FRACTION).add(Restrictions.ne(NAME, value));break;
				}
			}
			
			if ((criteria.getForm() != null) && (!criteria.getForm().isEmpty())) {
				String value = criteria.getForm();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.createCriteria(FORM_NAME).add(Restrictions.like(NAME, value));break;
				case 2:crit.createCriteria(FORM_NAME).add(Restrictions.gt(NAME, value));break;
				case 3:crit.createCriteria(FORM_NAME).add(Restrictions.lt(NAME, value));break;
				case 4:crit.createCriteria(FORM_NAME).add(Restrictions.ne(NAME, value));break;
				}
			}
			
			if ((criteria.getBiomass_lots() != null) && (!criteria.getBiomass_lots().isEmpty())) {
				String value = criteria.getBiomass_lots();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.add(Restrictions.like(BIOMASS_LOT, value));break;
				case 2:crit.add(Restrictions.gt(BIOMASS_LOT, value));break;
				case 3:crit.add(Restrictions.lt(BIOMASS_LOT, value));break;
				case 4:crit.add(Restrictions.ne(BIOMASS_LOT, value));break;
				}
			}
			
			if ((criteria.getStrain() != null) && (!criteria.getStrain().isEmpty())) {
				String value = criteria.getStrain();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.createCriteria(STRAIN_NAME).add(Restrictions.like(NAME, value));break;
				case 2:crit.createCriteria(STRAIN_NAME).add(Restrictions.gt(NAME, value));break;
				case 3:crit.createCriteria(STRAIN_NAME).add(Restrictions.lt(NAME, value));break;
				case 4:crit.createCriteria(STRAIN_NAME).add(Restrictions.ne(NAME, value));break;
				}
			}
			
			if ((criteria.getComposition() != null) && (!criteria.getComposition().isEmpty())) {
				String value = criteria.getComposition();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.createCriteria(COMPOSITION_NAME).add(Restrictions.like(NAME, value));break;
				case 2:crit.createCriteria(COMPOSITION_NAME).add(Restrictions.gt(NAME, value));break;
				case 3:crit.createCriteria(COMPOSITION_NAME).add(Restrictions.lt(NAME, value));break;
				case 4:crit.createCriteria(COMPOSITION_NAME).add(Restrictions.ne(NAME, value));break;
				}
			}
			
			if ((criteria.getStatus() != null) && (!criteria.getStatus().isEmpty())) {
				String value = criteria.getStatus();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.createCriteria(STATUS).add(Restrictions.like(NAME, value));break;
				case 2:crit.createCriteria(STATUS).add(Restrictions.gt(NAME, value));break;
				case 3:crit.createCriteria(STATUS).add(Restrictions.lt(NAME, value));break;
				case 4:crit.createCriteria(STATUS).add(Restrictions.ne(NAME, value));break;
				}
			}
			
		    if (criteria.getAttachment_ext() != null && !criteria.getAttachment_ext().isEmpty()) {
				AttachmentTypeDAOHibernate adh = new AttachmentTypeDAOHibernate();
				adh.setSession(session);
				Iterator<String> ait = criteria.getAttachment_ext().iterator();
				while (ait.hasNext()) {
					String ext = ait.next();
					AttachmentType type = adh.findByExtension(ext);
					if (type != null) {
						Set<Long> wids = new HashSet<Long>();
						List<Sample> samples = findByAttachmentExtension(type);
						Iterator<Sample> sit = samples.iterator();
						while (sit.hasNext()) {
							Sample s = sit.next();
							wids.add(s.getId());
						}
						crit.add(Restrictions.in(ID, wids));break;
					}
				}
		    }
		    
		    if (criteria.getTrackingIds() != null && !criteria.getTrackingIds().isEmpty()) {
				crit.add(Restrictions.in(ID, criteria.getTrackingIds()));
		    }
		    
			if ((criteria.getBuilding() != null) && (!criteria.getBuilding().isEmpty()) ||
					((criteria.getRoom() != null) && (!criteria.getRoom().isEmpty())) ||
					((criteria.getHolder() != null) && (!criteria.getHolder().isEmpty())) ||
					((criteria.getShelf() != null) && (!criteria.getShelf().isEmpty())) ||
					((criteria.getPackaging() != null) && (!criteria.getPackaging().isEmpty())) ||
					((criteria.getSubLocation() != null) && (!criteria.getSubLocation().isEmpty()))) {
				
				Criteria crit2 = crit.createCriteria(LOCATION);
				if ((criteria.getBuilding() != null) && (!criteria.getBuilding().isEmpty())) {
					String value = criteria.getBuilding();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:crit2.add(Restrictions.like(BUILDING, value));break;
					case 2:crit2.add(Restrictions.gt(BUILDING, value));break;
					case 3:crit2.add(Restrictions.lt(BUILDING, value));break;
					case 4:crit2.add(Restrictions.ne(BUILDING, value));break;
					}
				}
				
				if ((criteria.getRoom() != null) && (!criteria.getRoom().isEmpty())) {
					String value = criteria.getRoom();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:crit2.add(Restrictions.like(ROOM, value));break;
					case 2:crit2.add(Restrictions.gt(ROOM, value));break;
					case 3:crit2.add(Restrictions.lt(ROOM, value));break;
					case 4:crit2.add(Restrictions.ne(ROOM, value));break;
					}
				}
				
				if ((criteria.getHolder() != null) && (!criteria.getHolder().isEmpty())) {
					String value = criteria.getHolder();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:crit2.add(Restrictions.like(HOLDER, value));break;
					case 2:crit2.add(Restrictions.gt(HOLDER, value));break;
					case 3:crit2.add(Restrictions.lt(HOLDER, value));break;
					case 4:crit2.add(Restrictions.ne(HOLDER, value));break;
					}
				}
				
				if ((criteria.getShelf() != null) && (!criteria.getShelf().isEmpty())) {
					String value = criteria.getShelf();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:crit2.add(Restrictions.like(SHELF, value));break;
					case 2:crit2.add(Restrictions.gt(SHELF, value));break;
					case 3:crit2.add(Restrictions.lt(SHELF, value));break;
					case 4:crit2.add(Restrictions.ne(SHELF, value));break;
					}
				}
				
				if ((criteria.getPackaging() != null) && (!criteria.getPackaging().isEmpty())) {
					String value = criteria.getPackaging();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:crit2.add(Restrictions.like(PACKAGING, value));break;
					case 2:crit2.add(Restrictions.gt(PACKAGING, value));break;
					case 3:crit2.add(Restrictions.lt(PACKAGING, value));break;
					case 4:crit2.add(Restrictions.ne(PACKAGING, value));break;
					}
				}
				
				if ((criteria.getSubLocation() != null) && (!criteria.getSubLocation().isEmpty())) {
					String value = criteria.getSubLocation();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:crit2.add(Restrictions.like(SUBLOCATION, value));break;
					case 2:crit2.add(Restrictions.gt(SUBLOCATION, value));break;
					case 3:crit2.add(Restrictions.lt(SUBLOCATION, value));break;
					case 4:crit2.add(Restrictions.ne(SUBLOCATION, value));break;
					}
				}
			}
			
			if (criteria.getTrbNum() != 0) {
				switch (oper) {
				case 1:crit.add(Restrictions.eq(TRB_NUM, criteria.getTrbNum()));break;
				case 2:crit.add(Restrictions.gt(TRB_NUM, criteria.getTrbNum()));break;
				case 3:crit.add(Restrictions.lt(TRB_NUM, criteria.getTrbNum()));break;
				case 4:crit.add(Restrictions.ne(TRB_NUM, criteria.getTrbNum()));break;
				}
			}
			
			if (criteria.getTrbPage() != 0) {
				switch (oper) {
				case 1:crit.add(Restrictions.eq(TRB_PAGE, criteria.getTrbPage()));break;
				case 2:crit.add(Restrictions.gt(TRB_PAGE, criteria.getTrbPage()));break;
				case 3:crit.add(Restrictions.lt(TRB_PAGE, criteria.getTrbPage()));break;
				case 4:crit.add(Restrictions.ne(TRB_PAGE, criteria.getTrbPage()));break;
				}
			}
		}		
		//crit.addOrder(Order.desc(ID));
		//return (Long) crit.setProjection(Projections.rowCount()).uniqueResult();
		//return (List<Sample>)crit.list();
		List<Sample> list = (List<Sample>)crit.list();
		System.out.println("there are "+list.size()+" samples found");
		return list.size();
	}

	public Collection<SampleCriteria> convertSamples2Criteria(
			List<Sample> samples) {
		if ((samples == null) || (samples.isEmpty()))
			return null;
		
		Collection<SampleCriteria> criteria = new ArrayList<SampleCriteria>();
		
		Iterator<Sample> it = samples.iterator();
		while (it.hasNext()) {
			Sample sample = it.next();
			criteria.add(convertSample2Criterium(sample));
		}
		return criteria;
	}

	public SampleCriteria convertSample2Criterium(Sample sample) {
		if (sample == null)
			return null;
		
		SampleCriteria crit = new SampleCriteria();
		
		if (sample.getId() != 0)
			crit.setTrackingId(sample.getId());
		
		if ((sample.getSampleId() != null) && (!sample.getSampleId().isEmpty()))
			crit.setSampleId(sample.getSampleId());
		
		if ((sample.getStorageNotes() != null) && (!sample.getStorageNotes().isEmpty()))
			crit.setStorageNotes(sample.getStorageNotes());
		
		if ((sample.getDescription() != null) && (!sample.getDescription().isEmpty()))
			crit.setDescription(sample.getDescription());
		
		if ((sample.getComment() != null) && (!sample.getComment().isEmpty()))
			crit.setComments(sample.getComment());
		
		if ((sample.getAmount() != null) && (!sample.getAmount().isEmpty()))
			crit.setAmount(sample.getAmount());
		
		if (sample.getUnits() != null)
			crit.setUnits(sample.getUnits().getName());
		
		if (sample.getCreateDate() != null)
			crit.setStartCreateDate(sample.getCreateDate());
		
		if ((sample.getExternalId() != null) && (!sample.getExternalId().isEmpty()))
			crit.setExternalId(sample.getExternalId());
		
		if ((sample.getCustodianName() != null) && (!sample.getCustodianName().isEmpty()))
			crit.setCustodianName(sample.getCustodianName());
		
		if ((sample.getOwnerName() != null) && (!sample.getOwnerName().isEmpty()))
			crit.setOwnerName(sample.getOwnerName());
		
		if (sample.getTreatment() != null)
			crit.setTreatment(sample.getTreatment().getName());
		
		if (sample.getOrigin() != null)
			crit.setOrigin(sample.getOrigin().getName());
		
		if (sample.getFeedstock() != null)
			crit.setFeedstock(sample.getFeedstock().getName());
		
		if (sample.getFraction() != null)
			crit.setFraction(sample.getFraction().getName());
		
		if (sample.getDestination() != null)
			crit.setDestination(sample.getDestination().getName());
		
		if (sample.getForm() != null)
			crit.setForm(sample.getForm().getName());
		
		if (sample.getBiomassLot() != null)
			crit.setBiomass_lots(sample.getBiomassLot());
		
		if (sample.getStrain() != null)
			crit.setStrain(sample.getStrain().getName());
		
		if (sample.getComposition() != null)
			crit.setComposition(sample.getComposition().getName());
		
		crit.setFire(String.valueOf(sample.getFire()));
		
		crit.setReactivity(String.valueOf(sample.getReactivity()));
		
		if (sample.getSpecificHazard() != null)
			crit.setSpecific(sample.getSpecificHazard());
		
		crit.setHealth(String.valueOf(sample.getHealth()));
		
		if (sample.getStatus() != null)
			crit.setStatus(sample.getStatus().getName());
		
		if ((sample.getLocation() != null)
				&& (sample.getLocation().getBuilding() != null)
				&& (!sample.getLocation().getBuilding().isEmpty()))
			crit.setBuilding(sample.getLocation().getBuilding());
		
		if ((sample.getLocation() != null)
				&& (sample.getLocation().getRoom() != null)
				&& (!sample.getLocation().getRoom().isEmpty()))
			crit.setRoom(sample.getLocation().getRoom());
		
		if ((sample.getLocation() != null)
				&& (sample.getLocation().getHolder() != null)
				&& (!sample.getLocation().getHolder().isEmpty()))
			crit.setHolder(sample.getLocation().getHolder());
		
		if ((sample.getLocation() != null)
				&& (sample.getLocation().getShelf() != null)
				&& (!sample.getLocation().getShelf().isEmpty()))
			crit.setShelf(sample.getLocation().getShelf());
		
		if ((sample.getLocation() != null)
				&& (sample.getLocation().getPackaging() != null)
				&& (!sample.getLocation().getPackaging().isEmpty()))
			crit.setPackaging(sample.getLocation().getPackaging());
		
		if ((sample.getLocation() != null)
				&& (sample.getLocation().getSubLocation() != null)
				&& (!sample.getLocation().getSubLocation().isEmpty()))
			crit.setSubLocation(sample.getLocation().getSubLocation());
		
		if (sample.getTrbNum() != -1) {
			crit.setTrbNum(sample.getTrbNum());
		}
		
		if (sample.getTrbPage() != -1) 
			crit.setTrbPage(sample.getTrbPage());
		
		if (sample.getAttachments() != null && !sample.getAttachments().isEmpty()) {
			Iterator<Attachments> ait = sample.getAttachments().iterator();
			while (ait.hasNext()) {
				Attachments attach = ait.next();
				crit.getAttachmendIds().add(attach.getAttachment_id());
				AttachmentType type = attach.getType_id();
				crit.getAttachment_ext().add(type.getExt());
			}
		}
		
		return crit;
	}

	/**
	 * Public method to retrieve all Sample names.
	 * 
	 * @return <Collection<String>> names
	 */
	@SuppressWarnings("unchecked")
	public Collection<String> findAllSampleIds(List<Long> sids) {

		Session session = getSession();
		List<String> names = new ArrayList<String>();
		Criteria crit = session.createCriteria(Sample.class);
		List<Sample> os = (List<Sample>)crit.list();
		Iterator<Sample> oit = os.iterator();
		while (oit.hasNext()) {
			Sample o = oit.next();
			String name = o.getSampleId();
			if (sids.contains(new Long(o.getId())))
				names.add(name);
		}
		return names;
	}

	/**
	 * Public method to find all custodian names.
	 * 
	 * @return <Collection<String>>
	 */
	@SuppressWarnings("unchecked")
	public Collection<String> findAllCustodians() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Sample.class)
			.setProjection(Projections.distinct(Projections.property(CUSTODIAN_NAME)));
				
		return (List<String>)crit.list();
	}
	
	/**
	 * Public method to get a list of samples by attachment type.
	 * 
	 * @param type - the type of attachment
	 * @return <List<Sample>> - list of samples.
	 */
	@SuppressWarnings("unchecked")
	public List<Sample> findByAttachmentExtension(AttachmentType type) {

		Session session = getSession();
		//List<WorkbookData> workbooks = new ArrayList<WorkbookData>();

		Criteria crit = session.createCriteria(Sample.class)
					.setFetchMode("attachments", FetchMode.JOIN);
		crit.createCriteria("attachments")
			.add(Restrictions.eq("type_id", type));
//				.setProjection(Projections.distinct(Projections.property("attachments")))
	//			.add(Restrictions.eq("type_id", type));

		return (List<Sample>) crit.list();
	}

	/**
	 * Public method to find all owner names.
	 * 
	 * @return <Collection<String>>
	 */
	@SuppressWarnings("unchecked")
	public Collection<String> findAllOwners() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Sample.class)
			.setProjection(Projections.distinct(Projections.property(OWNER_NAME)));
				
		return (List<String>)crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<Sample> findByLocation(Location loc) {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Sample.class)
			.add(Restrictions.eq(LOCATION, loc));
		
		return (List<Sample>)crit.list();
	}

	@SuppressWarnings("unchecked")
	public Collection<String> findAllBiomassLots() {
		Session session = getSession();
		
		Criteria crit = session.createCriteria(Sample.class)
		.setProjection(Projections.distinct(Projections.property(BIOMASS_LOT)));
			
		return (List<String>)crit.list();

	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<String> findAllSampleIds() {
		Session session = getSession();
		List<String> names = new ArrayList<String>();
		Criteria crit = session.createCriteria(Sample.class);
		List<Sample> os = (List<Sample>)crit.list();
		Iterator<Sample> oit = os.iterator();
		while (oit.hasNext()) {
			Sample o = oit.next();
			String name = o.getSampleId();
			names.add(name);
		}
		return names;
	}
}