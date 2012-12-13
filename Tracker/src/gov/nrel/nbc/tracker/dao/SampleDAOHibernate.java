package gov.nrel.nbc.tracker.dao;

import gov.nrel.nbc.tracker.client.DevTestProdConstants;
import gov.nrel.nbc.tracker.client.AppConstants;
import gov.nrel.nbc.tracker.client.SampleCriteria;
import gov.nrel.nbc.tracker.model.AttachmentType;
import gov.nrel.nbc.tracker.model.Attachments;
import gov.nrel.nbc.tracker.model.Location;
import gov.nrel.nbc.tracker.model.Sample;
import gov.nrel.nbc.tracker.utils.XLogger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
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
     *  XLogger.<OFF|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST|ALL>
     */
    private static final XLogger log = new XLogger(SampleDAOHibernate.class);
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
			log.warning("comments="+value);
			value = value.replace(ASTERISK, PERCENT);
			crit.add(Restrictions.like(COMMENT, value));
		}
		
		if ((criteria.getStorageNotes() != null) && (!criteria.getStorageNotes().isEmpty())) {
			String value = criteria.getStorageNotes();
			log.warning("storage="+value);
			value = value.replace(ASTERISK, PERCENT);
			crit.add(Restrictions.like(STORAGE_NOTES, value));
		}
		
		if ((criteria.getAmount() != null) && (!criteria.getAmount().isEmpty())) {
			log.warning("amount="+criteria.getAmount());
			crit.add(Restrictions.like(AMOUNT, criteria.getAmount()));
		}
		
		if ((criteria.getUnits() != null) && (!criteria.getUnits().isEmpty())) {
			log.warning("units="+criteria.getUnits());
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
			log.warning("extid="+criteria.getExternalId());
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
		
		if ((criteria.getLabelDescription() != null) && (!criteria.getLabelDescription().isEmpty())) {
			String value = criteria.getLabelDescription();
			value = value.replace(ASTERISK, PERCENT);
			crit.createCriteria(LABEL_DESCRIPTION).add(Restrictions.like(NAME, value));
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
				log.warning("holder="+value);
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
			log.warning("trb #="+criteria.getTrbNum());
			crit.add(Restrictions.eq(TRB_NUM, criteria.getTrbNum()));
		}
		
		if (criteria.getTrbPage() != 0) {
			log.warning("trb page="+criteria.getTrbPage());
			crit.add(Restrictions.eq(TRB_PAGE, criteria.getTrbPage()));
		}
				
		crit.addOrder(Order.desc(ID));
		return (List<Sample>)crit.list();
	}

	Date getDate(Date dt1,int plusMinus) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt1);
		cal.add(Calendar.DAY_OF_MONTH, plusMinus);
		Date dt2 = cal.getTime();
		return dt2;
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
				case 4: crit.add(Restrictions.or(Restrictions.not(Restrictions.like(SAMPLE_ID, value)),Restrictions.isNull(SAMPLE_ID)));break;
				}
			}
			
			if ((criteria.getComments() != null) && (!criteria.getComments().isEmpty())) {
				String value = criteria.getComments();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.add(Restrictions.like(COMMENT, value));break;
				case 2:crit.add(Restrictions.gt(COMMENT, value));break;
				case 3:crit.add(Restrictions.lt(COMMENT, value));break;
				case 4:crit.add(Restrictions.or(Restrictions.not(Restrictions.like(COMMENT, value)),Restrictions.isNull(COMMENT)));break;
				}
			}
			
			if ((criteria.getStorageNotes() != null) && (!criteria.getStorageNotes().isEmpty())) {
				String value = criteria.getStorageNotes();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.add(Restrictions.like(STORAGE_NOTES, value));break;
				case 2:crit.add(Restrictions.gt(STORAGE_NOTES, value));break;
				case 3:crit.add(Restrictions.lt(STORAGE_NOTES, value));break;
				case 4:crit.add(Restrictions.or(Restrictions.not(Restrictions.like(STORAGE_NOTES,value)) ,Restrictions.isNull(STORAGE_NOTES)));break;
				}
			}
			
			if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.BIOMASS)) {
				if ((criteria.getDestination() != null) && (!criteria.getDestination().isEmpty())) {
					String value = criteria.getDestination();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:crit.add(Restrictions.like(PROJECT, value));break;
					case 2:crit.add(Restrictions.gt(PROJECT, value));break;
					case 3:crit.add(Restrictions.lt(PROJECT, value));break;
					case 4:crit.add(Restrictions.or(Restrictions.not(Restrictions.like(PROJECT, value)),Restrictions.isNull(PROJECT)));break;
					}
				}
			} else {
				if ((criteria.getDestination() != null) && (!criteria.getDestination().isEmpty())) {
					String value = criteria.getDestination();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1: crit.createCriteria(DESTINATION_NAME).add(Restrictions.like(NAME, value));break;
					case 2: crit.createCriteria(DESTINATION_NAME).add(Restrictions.gt(NAME, value));break;
					case 3: crit.createCriteria(DESTINATION_NAME).add(Restrictions.lt(NAME, value));break;
					case 4: crit.createCriteria(DESTINATION_NAME).add(Restrictions.or(Restrictions.not(Restrictions.like(NAME, value)),Restrictions.isNull(NAME)));break;
					}
				}
			}
			
			if ((criteria.getAmount() != null) && (!criteria.getAmount().isEmpty())) {
				switch (oper) {
				case 1:crit.add(Restrictions.like(AMOUNT, criteria.getAmount()));break;
				case 2:crit.add(Restrictions.gt(AMOUNT, criteria.getAmount()));break;
				case 3:crit.add(Restrictions.lt(AMOUNT, criteria.getAmount()));break;
				case 4:crit.add(Restrictions.or(Restrictions.not(Restrictions.like(AMOUNT, criteria.getAmount())),Restrictions.isNull(AMOUNT)));break;
				}
			}
			
			if ((criteria.getUnits() != null) && (!criteria.getUnits().isEmpty())) {
				crit.setFetchMode(UNITS, FetchMode.JOIN);
				switch (oper) {
				case 1:crit.createCriteria(UNITS).add(Restrictions.eq(NAME, criteria.getUnits()));break;
				case 2:crit.createCriteria(UNITS).add(Restrictions.gt(NAME, criteria.getUnits()));break;
				case 3:crit.createCriteria(UNITS).add(Restrictions.lt(NAME, criteria.getUnits()));break;
				case 4:crit.createCriteria(UNITS).add(Restrictions.or(Restrictions.not(Restrictions.like(NAME, criteria.getUnits())),Restrictions.isNull(NAME)));break;
				}
			}
			
			if (criteria.getStartCreateDate() != null && criteria.getEndCreateDate() != null) {
				crit.add(Restrictions.between(CREATE_DATE, criteria.getStartCreateDate(), criteria.getEndCreateDate()));break;
			} else if (criteria.getStartCreateDate() != null) {
				switch (oper) {
				case 1: crit.add(Restrictions.eq(CREATE_DATE, criteria.getStartCreateDate()));break;
				case 2: crit.add(Restrictions.ge(CREATE_DATE, getDate(criteria.getStartCreateDate(),1)));break;
				case 3: crit.add(Restrictions.le(CREATE_DATE, getDate(criteria.getStartCreateDate(),-1)));break;
				case 4: crit.add(Restrictions.or(Restrictions.not(Restrictions.eq(CREATE_DATE, criteria.getStartCreateDate())),Restrictions.isNull(CREATE_DATE)));break;
				}
			} else if (criteria.getEndCreateDate() != null) {
				switch (oper) {
				case 1: crit.add(Restrictions.eq(CREATE_DATE, criteria.getEndCreateDate()));break;
				case 2: crit.add(Restrictions.ge(CREATE_DATE, getDate(criteria.getEndCreateDate(),1)));break;
				case 3: crit.add(Restrictions.le(CREATE_DATE, getDate(criteria.getEndCreateDate(),-1)));break;
				case 4: crit.add(Restrictions.or(Restrictions.not(Restrictions.eq(CREATE_DATE, criteria.getEndCreateDate())),Restrictions.isNull(CREATE_DATE)));break;
				}
			}
			
			if ((criteria.getExternalId() != null) && (!criteria.getExternalId().isEmpty())) {
				String value = criteria.getExternalId();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.add(Restrictions.like(EXTERNAL_ID, value));break;
				case 2:crit.add(Restrictions.gt(EXTERNAL_ID, value));break;
				case 3:crit.add(Restrictions.lt(EXTERNAL_ID, value));break;
				case 4:crit.add(Restrictions.or(Restrictions.not(Restrictions.like(EXTERNAL_ID, value)),Restrictions.isNull(EXTERNAL_ID)));break;
				}
			}
			
			if ((criteria.getCustodianName() != null) && (!criteria.getCustodianName().isEmpty())) {
				String value = criteria.getCustodianName();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.add(Restrictions.like(CUSTODIAN_NAME, value));break;
				case 2: crit.add(Restrictions.gt(CUSTODIAN_NAME, value));break;
				case 3: crit.add(Restrictions.lt(CUSTODIAN_NAME, value));break;
				case 4: crit.add(Restrictions.or(Restrictions.not(Restrictions.like(CUSTODIAN_NAME, value)),Restrictions.isNull(CUSTODIAN_NAME)));break;
				}
			}
			
			if ((criteria.getOwnerName() != null) && (!criteria.getOwnerName().isEmpty())) {
				String value = criteria.getOwnerName();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.add(Restrictions.like(OWNER_NAME, value));break;
				case 2: crit.add(Restrictions.gt(OWNER_NAME, value));break;
				case 3: crit.add(Restrictions.lt(OWNER_NAME, value));break;
				case 4: crit.add(Restrictions.or(Restrictions.not(Restrictions.like(OWNER_NAME, value)),Restrictions.isNull(OWNER_NAME)));break;
				}
			}
			
			if ((criteria.getStorageNotes() != null) && (!criteria.getStorageNotes().isEmpty())) {
				String value = criteria.getStorageNotes();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.add(Restrictions.like(STORAGE_NOTES, value));break;
				case 2: crit.add(Restrictions.gt(STORAGE_NOTES, value));break;
				case 3: crit.add(Restrictions.lt(STORAGE_NOTES, value));break;
				case 4: crit.add(Restrictions.or(Restrictions.not(Restrictions.like(STORAGE_NOTES, value)),Restrictions.isNull(STORAGE_NOTES)));break;
				}
			}
			
			if ((criteria.getFeedstock() != null) && (!criteria.getFeedstock().isEmpty())) {
				String value = criteria.getFeedstock();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.createCriteria(FEEDSTOCK).add(Restrictions.like(NAME, value));break;
				case 2: crit.createCriteria(FEEDSTOCK).add(Restrictions.gt(NAME, value));break;
				case 3: crit.createCriteria(FEEDSTOCK).add(Restrictions.lt(NAME, value));break;
				case 4: crit.createCriteria(FEEDSTOCK).add(Restrictions.or(Restrictions.not(Restrictions.like(NAME, value)),Restrictions.isNull(NAME)));break;
				}
			}
			
			if ((criteria.getTreatment() != null) && (!criteria.getTreatment().isEmpty())) {
				String value = criteria.getTreatment();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.createCriteria(TREATMENT).add(Restrictions.like(NAME, value));break;
				case 2: crit.createCriteria(TREATMENT).add(Restrictions.gt(NAME, value));break;
				case 3: crit.createCriteria(TREATMENT).add(Restrictions.lt(NAME, value));break;
				case 4: crit.createCriteria(TREATMENT).add(Restrictions.or(Restrictions.not(Restrictions.like(NAME, value)),Restrictions.isNull(NAME)));break;
				}
			}
			
			if ((criteria.getOrigin() != null) && (!criteria.getOrigin().isEmpty())) {
				String value = criteria.getOrigin();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.createCriteria(ORIGIN).add(Restrictions.like(NAME, value));break;
				case 2: crit.createCriteria(ORIGIN).add(Restrictions.gt(NAME, value));break;
				case 3: crit.createCriteria(ORIGIN).add(Restrictions.lt(NAME, value));break;
				case 4: crit.createCriteria(ORIGIN).add(Restrictions.or(Restrictions.not(Restrictions.like(NAME, value)),Restrictions.isNull(NAME)));break;
				}
			}
			
			if ((criteria.getFraction() != null) && (!criteria.getFraction().isEmpty())) {
				String value = criteria.getFraction();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.createCriteria(FRACTION).add(Restrictions.like(NAME, value));break;
				case 2:crit.createCriteria(FRACTION).add(Restrictions.gt(NAME, value));break;
				case 3:crit.createCriteria(FRACTION).add(Restrictions.lt(NAME, value));break;
				case 4:crit.createCriteria(FRACTION).add(Restrictions.or(Restrictions.not(Restrictions.like(NAME, value)),Restrictions.isNull(NAME)));break;
				}
			}
			
			if ((criteria.getForm() != null) && (!criteria.getForm().isEmpty())) {
				String value = criteria.getForm();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.createCriteria(FORM_NAME).add(Restrictions.like(NAME, value));break;
				case 2:crit.createCriteria(FORM_NAME).add(Restrictions.gt(NAME, value));break;
				case 3:crit.createCriteria(FORM_NAME).add(Restrictions.lt(NAME, value));break;
				case 4:crit.createCriteria(FORM_NAME).add(Restrictions.or(Restrictions.not(Restrictions.like(NAME, value)),Restrictions.isNull(NAME)));break;
				}
			}
			
			if ((criteria.getBiomass_lots() != null) && (!criteria.getBiomass_lots().isEmpty())) {
				String value = criteria.getBiomass_lots();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.add(Restrictions.like(BIOMASS_LOT, value));break;
				case 2:crit.add(Restrictions.gt(BIOMASS_LOT, value));break;
				case 3:crit.add(Restrictions.lt(BIOMASS_LOT, value));break;
				case 4:crit.add(Restrictions.or(Restrictions.not(Restrictions.like(BIOMASS_LOT, value)),Restrictions.isNull(BIOMASS_LOT)));break;
				}
			}
			
			if ((criteria.getStrain() != null) && (!criteria.getStrain().isEmpty())) {
				String value = criteria.getStrain();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.createCriteria(STRAIN_NAME).add(Restrictions.like(NAME, value));break;
				case 2:crit.createCriteria(STRAIN_NAME).add(Restrictions.gt(NAME, value));break;
				case 3:crit.createCriteria(STRAIN_NAME).add(Restrictions.lt(NAME, value));break;
				case 4:crit.createCriteria(STRAIN_NAME).add(Restrictions.or(Restrictions.not(Restrictions.like(NAME, value)),Restrictions.isNull(NAME)));break;
				}
			}
			///*
			if ((criteria.getLabelDescription() != null) && (!criteria.getLabelDescription().isEmpty())) {
				String value = criteria.getLabelDescription();
				value = value.replace(ASTERISK, PERCENT);
				log.warning("descr="+value);
				switch (oper) {
				case 1:crit.add(Restrictions.like(LABEL_DESCRIPTION, value));break;
				case 2:crit.add(Restrictions.gt(LABEL_DESCRIPTION, value));break;
				case 3:crit.add(Restrictions.lt(LABEL_DESCRIPTION, value));break;
				case 4:crit.add(Restrictions.or(Restrictions.not(Restrictions.like(LABEL_DESCRIPTION, value)),Restrictions.isNull(LABEL_DESCRIPTION)));break;
				}
			}
			//*/
			/*
			if ((criteria.getDescription() != null) && (!criteria.getDescription().isEmpty())) {
				String value = criteria.getDescription();
		        String sql = "select id from tracker.sample where description ";//like '"+value+"'";
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:sql += " like '"+value+"'";break;
				case 2:sql += " > '" + value + "'";break;
				case 3:sql += " < '" + value + "'";break;
				case 4:sql += " != '" + value + "'";break;
				}
		        DBUtils db = new DBUtils("jdbc:mysql://nbcdbdev.nrel.gov:3306/tracker","trackeruser","trackeruserpw");
		        db.performQuery(sql,true);
		        List<Integer> sids = new ArrayList<Integer>();
		        while (db.getNextRow()) {
		        	Integer i = db.getIntColumn(1);
		        	sids.add(i);
		        }
		        db.close();
		        if (sids.size()==0)
		        	return null;
		        crit.add(Restrictions.in(ID, criteria.getTrackingIds()));
			}
			*/
			if ((criteria.getComposition() != null) && (!criteria.getComposition().isEmpty())) {
				String value = criteria.getComposition();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.createCriteria(COMPOSITION_NAME).add(Restrictions.like(NAME, value));break;
				case 2:crit.createCriteria(COMPOSITION_NAME).add(Restrictions.gt(NAME, value));break;
				case 3:crit.createCriteria(COMPOSITION_NAME).add(Restrictions.lt(NAME, value));break;
				case 4:crit.createCriteria(COMPOSITION_NAME).add(Restrictions.or(Restrictions.not(Restrictions.like(NAME, value)),Restrictions.isNull(NAME)));break;
				}
			}
			
			if ((criteria.getStatus() != null) && (!criteria.getStatus().isEmpty())) {
				String value = criteria.getStatus();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.createCriteria(STATUS).add(Restrictions.like(NAME, value));break;
				case 2:crit.createCriteria(STATUS).add(Restrictions.gt(NAME, value));break;
				case 3:crit.createCriteria(STATUS).add(Restrictions.lt(NAME, value));break;
				case 4:crit.createCriteria(STATUS).add(Restrictions.or(Restrictions.not(Restrictions.like(NAME, value)),Restrictions.isNull(NAME)));break;
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
					case 4:critLocation.add(Restrictions.or(Restrictions.not(Restrictions.like(BUILDING, value)),Restrictions.isNull(BUILDING)));break;
					}
				}
				
				if ((criteria.getRoom() != null) && (!criteria.getRoom().isEmpty())) {
					String value = criteria.getRoom();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:critLocation.add(Restrictions.like(ROOM, value));break;
					case 2:critLocation.add(Restrictions.gt(ROOM, value));break;
					case 3:critLocation.add(Restrictions.lt(ROOM, value));break;
					case 4:critLocation.add(Restrictions.or(Restrictions.not(Restrictions.like(ROOM, value)),Restrictions.isNull(ROOM)));break;
					}
				}
				
				if ((criteria.getHolder() != null) && (!criteria.getHolder().isEmpty())) {
					String value = criteria.getHolder();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:critLocation.add(Restrictions.like(HOLDER, value));break;
					case 2:critLocation.add(Restrictions.gt(HOLDER, value));break;
					case 3:critLocation.add(Restrictions.lt(HOLDER, value));break;
					case 4:critLocation.add(Restrictions.or(Restrictions.not(Restrictions.like(HOLDER, value)),Restrictions.isNull(HOLDER)));break;
					}
				}
				
				if ((criteria.getShelf() != null) && (!criteria.getShelf().isEmpty())) {
					String value = criteria.getShelf();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:critLocation.add(Restrictions.like(SHELF, value));break;
					case 2:critLocation.add(Restrictions.gt(SHELF, value));break;
					case 3:critLocation.add(Restrictions.lt(SHELF, value));break;
					case 4:critLocation.add(Restrictions.or(Restrictions.not(Restrictions.like(SHELF, value)),Restrictions.isNull(SHELF)));break;
					}
				}
				
				if ((criteria.getPackaging() != null) && (!criteria.getPackaging().isEmpty())) {
					String value = criteria.getPackaging();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:critLocation.add(Restrictions.like(PACKAGING, value));break;
					case 2:critLocation.add(Restrictions.gt(PACKAGING, value));break;
					case 3:critLocation.add(Restrictions.lt(PACKAGING, value));break;
					case 4:critLocation.add(Restrictions.or(Restrictions.not(Restrictions.like(PACKAGING, value)),Restrictions.isNull(PACKAGING)));break;
					}
				}
				
				if ((criteria.getSubLocation() != null) && (!criteria.getSubLocation().isEmpty())) {
					String value = criteria.getSubLocation();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:critLocation.add(Restrictions.like(SUBLOCATION, value));break;
					case 2:critLocation.add(Restrictions.gt(SUBLOCATION, value));break;
					case 3:critLocation.add(Restrictions.lt(SUBLOCATION, value));break;
					case 4:critLocation.add(Restrictions.or(Restrictions.not(Restrictions.like(SUBLOCATION, value)),Restrictions.isNull(SUBLOCATION)));break;
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
				case 4: crit.add(Restrictions.or(Restrictions.not(Restrictions.like(SAMPLE_ID, value)),Restrictions.isNull(SAMPLE_ID)));break;
				}
			}
			
			if ((criteria.getComments() != null) && (!criteria.getComments().isEmpty())) {
				String value = criteria.getComments();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.add(Restrictions.like(COMMENT, value));break;
				case 2:crit.add(Restrictions.gt(COMMENT, value));break;
				case 3:crit.add(Restrictions.lt(COMMENT, value));break;
				case 4:crit.add(Restrictions.or(Restrictions.not(Restrictions.like(COMMENT, value)),Restrictions.isNull(COMMENT)));break;
				}
			}
			
			if ((criteria.getStorageNotes() != null) && (!criteria.getStorageNotes().isEmpty())) {
				String value = criteria.getStorageNotes();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.add(Restrictions.like(STORAGE_NOTES, value));break;
				case 2:crit.add(Restrictions.gt(STORAGE_NOTES, value));break;
				case 3:crit.add(Restrictions.lt(STORAGE_NOTES, value));break;
				case 4:crit.add(Restrictions.or(Restrictions.not(Restrictions.like(STORAGE_NOTES, value)),Restrictions.isNull(STORAGE_NOTES)));break;
				}
			}
			
			if (DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.BIOMASS)) {
				if ((criteria.getDestination() != null) && (!criteria.getDestination().isEmpty())) {
					String value = criteria.getDestination();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:crit.add(Restrictions.like(PROJECT, value));break;
					case 2:crit.add(Restrictions.gt(PROJECT, value));break;
					case 3:crit.add(Restrictions.lt(PROJECT, value));break;
					case 4:crit.add(Restrictions.or(Restrictions.not(Restrictions.like(PROJECT, value)),Restrictions.isNull(PROJECT)));break;
					}
				}
			} else {
				if ((criteria.getDestination() != null) && (!criteria.getDestination().isEmpty())) {
					String value = criteria.getDestination();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1: crit.createCriteria(DESTINATION_NAME).add(Restrictions.like(NAME, value));break;
					case 2: crit.createCriteria(DESTINATION_NAME).add(Restrictions.gt(NAME, value));break;
					case 3: crit.createCriteria(DESTINATION_NAME).add(Restrictions.lt(NAME, value));break;
					case 4: crit.createCriteria(DESTINATION_NAME).add(Restrictions.or(Restrictions.not(Restrictions.like(NAME, value)),Restrictions.isNull(NAME)));break;
					}
				}
			}
			
			if ((criteria.getAmount() != null) && (!criteria.getAmount().isEmpty())) {
				switch (oper) {
				case 1:crit.add(Restrictions.like(AMOUNT, criteria.getAmount()));break;
				case 2:crit.add(Restrictions.gt(AMOUNT, criteria.getAmount()));break;
				case 3:crit.add(Restrictions.lt(AMOUNT, criteria.getAmount()));break;
				case 4:crit.add(Restrictions.or(Restrictions.not(Restrictions.like(AMOUNT, criteria.getAmount())),Restrictions.isNull(AMOUNT)));break;
				}
			}
			
			if ((criteria.getUnits() != null) && (!criteria.getUnits().isEmpty())) {
				crit.setFetchMode(UNITS, FetchMode.JOIN);
				switch (oper) {
				case 1:crit.createCriteria(UNITS).add(Restrictions.eq(NAME, criteria.getUnits()));break;
				case 2:crit.createCriteria(UNITS).add(Restrictions.gt(NAME, criteria.getUnits()));break;
				case 3:crit.createCriteria(UNITS).add(Restrictions.lt(NAME, criteria.getUnits()));break;
				case 4:crit.createCriteria(UNITS).add(Restrictions.or(Restrictions.not(Restrictions.like(NAME, criteria.getUnits())),Restrictions.isNull(NAME)));break;
				}
			}
			
			if (criteria.getStartCreateDate() != null && criteria.getEndCreateDate() != null) {
				crit.add(Restrictions.between(CREATE_DATE, criteria.getStartCreateDate(), criteria.getEndCreateDate()));break;
			} else if (criteria.getStartCreateDate() != null) {
				switch (oper) {
				case 1: crit.add(Restrictions.eq(CREATE_DATE, criteria.getStartCreateDate()));break;
				case 2: crit.add(Restrictions.ge(CREATE_DATE, getDate(criteria.getStartCreateDate(),1)));break;
				case 3: crit.add(Restrictions.le(CREATE_DATE, getDate(criteria.getStartCreateDate(),-1)));break;
				case 4: crit.add(Restrictions.or(Restrictions.not(Restrictions.eq(CREATE_DATE, criteria.getStartCreateDate())),Restrictions.isNull(CREATE_DATE)));break;
				}
			} else if (criteria.getEndCreateDate() != null) {
				switch (oper) {
				case 1: crit.add(Restrictions.eq(CREATE_DATE, criteria.getEndCreateDate()));break;
				case 2: crit.add(Restrictions.ge(CREATE_DATE, getDate(criteria.getEndCreateDate(),1)));break;
				case 3: crit.add(Restrictions.le(CREATE_DATE, getDate(criteria.getEndCreateDate(),-1)));break;
				case 4: crit.add(Restrictions.or(Restrictions.not(Restrictions.eq(CREATE_DATE, criteria.getEndCreateDate())),Restrictions.isNull(CREATE_DATE)));break;
				}
			}
			
			if ((criteria.getExternalId() != null) && (!criteria.getExternalId().isEmpty())) {
				String value = criteria.getExternalId();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.add(Restrictions.like(EXTERNAL_ID, value));break;
				case 2:crit.add(Restrictions.gt(EXTERNAL_ID, value));break;
				case 3:crit.add(Restrictions.lt(EXTERNAL_ID, value));break;
				case 4:crit.add(Restrictions.or(Restrictions.not(Restrictions.like(EXTERNAL_ID, value)),Restrictions.isNull(EXTERNAL_ID)));break;
				}
			}
			
			if ((criteria.getCustodianName() != null) && (!criteria.getCustodianName().isEmpty())) {
				String value = criteria.getCustodianName();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.add(Restrictions.like(CUSTODIAN_NAME, value));break;
				case 2: crit.add(Restrictions.gt(CUSTODIAN_NAME, value));break;
				case 3: crit.add(Restrictions.lt(CUSTODIAN_NAME, value));break;
				case 4: crit.add(Restrictions.or(Restrictions.not(Restrictions.like(CUSTODIAN_NAME, value)),Restrictions.isNull(CUSTODIAN_NAME)));break;
				}
			}
			
			if ((criteria.getOwnerName() != null) && (!criteria.getOwnerName().isEmpty())) {
				String value = criteria.getOwnerName();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.add(Restrictions.like(OWNER_NAME, value));break;
				case 2: crit.add(Restrictions.gt(OWNER_NAME, value));break;
				case 3: crit.add(Restrictions.lt(OWNER_NAME, value));break;
				case 4: crit.add(Restrictions.or(Restrictions.not(Restrictions.like(OWNER_NAME, value)),Restrictions.isNull(OWNER_NAME)));break;
				}
			}
			
			if ((criteria.getStorageNotes() != null) && (!criteria.getStorageNotes().isEmpty())) {
				String value = criteria.getStorageNotes();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.add(Restrictions.like(STORAGE_NOTES, value));break;
				case 2: crit.add(Restrictions.gt(STORAGE_NOTES, value));break;
				case 3: crit.add(Restrictions.lt(STORAGE_NOTES, value));break;
				case 4: crit.add(Restrictions.or(Restrictions.not(Restrictions.like(STORAGE_NOTES, value)),Restrictions.isNull(STORAGE_NOTES)));break;
				}
			}
			
			if ((criteria.getFeedstock() != null) && (!criteria.getFeedstock().isEmpty())) {
				String value = criteria.getFeedstock();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.createCriteria(FEEDSTOCK).add(Restrictions.like(NAME, value));break;
				case 2: crit.createCriteria(FEEDSTOCK).add(Restrictions.gt(NAME, value));break;
				case 3: crit.createCriteria(FEEDSTOCK).add(Restrictions.lt(NAME, value));break;
				case 4: crit.createCriteria(FEEDSTOCK).add(Restrictions.or(Restrictions.not(Restrictions.like(NAME, value)),Restrictions.isNull(NAME)));break;
				}
			}
			
			if ((criteria.getTreatment() != null) && (!criteria.getTreatment().isEmpty())) {
				String value = criteria.getTreatment();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.createCriteria(TREATMENT).add(Restrictions.like(NAME, value));break;
				case 2: crit.createCriteria(TREATMENT).add(Restrictions.gt(NAME, value));break;
				case 3: crit.createCriteria(TREATMENT).add(Restrictions.lt(NAME, value));break;
				case 4: crit.createCriteria(TREATMENT).add(Restrictions.or(Restrictions.not(Restrictions.like(NAME, value)),Restrictions.isNull(NAME)));break;
				}
			}
			
			if ((criteria.getOrigin() != null) && (!criteria.getOrigin().isEmpty())) {
				String value = criteria.getOrigin();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1: crit.createCriteria(ORIGIN).add(Restrictions.like(NAME, value));break;
				case 2: crit.createCriteria(ORIGIN).add(Restrictions.gt(NAME, value));break;
				case 3: crit.createCriteria(ORIGIN).add(Restrictions.lt(NAME, value));break;
				case 4: crit.createCriteria(ORIGIN).add(Restrictions.or(Restrictions.not(Restrictions.like(NAME, value)),Restrictions.isNull(NAME)));break;
				}
			}
			
			if ((criteria.getFraction() != null) && (!criteria.getFraction().isEmpty())) {
				String value = criteria.getFraction();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.createCriteria(FRACTION).add(Restrictions.like(NAME, value));break;
				case 2:crit.createCriteria(FRACTION).add(Restrictions.gt(NAME, value));break;
				case 3:crit.createCriteria(FRACTION).add(Restrictions.lt(NAME, value));break;
				case 4:crit.createCriteria(FRACTION).add(Restrictions.or(Restrictions.not(Restrictions.like(NAME, value)),Restrictions.isNull(NAME)));break;
				}
			}
			
			if ((criteria.getForm() != null) && (!criteria.getForm().isEmpty())) {
				String value = criteria.getForm();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.createCriteria(FORM_NAME).add(Restrictions.like(NAME, value));break;
				case 2:crit.createCriteria(FORM_NAME).add(Restrictions.gt(NAME, value));break;
				case 3:crit.createCriteria(FORM_NAME).add(Restrictions.lt(NAME, value));break;
				case 4:crit.createCriteria(FORM_NAME).add(Restrictions.or(Restrictions.not(Restrictions.like(NAME, value)),Restrictions.isNull(NAME)));break;
				}
			}
			
			if ((criteria.getBiomass_lots() != null) && (!criteria.getBiomass_lots().isEmpty())) {
				String value = criteria.getBiomass_lots();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.add(Restrictions.like(BIOMASS_LOT, value));break;
				case 2:crit.add(Restrictions.gt(BIOMASS_LOT, value));break;
				case 3:crit.add(Restrictions.lt(BIOMASS_LOT, value));break;
				case 4:crit.add(Restrictions.or(Restrictions.not(Restrictions.like(BIOMASS_LOT, value)),Restrictions.isNull(BIOMASS_LOT)));break;
				}
			}
			
			if ((criteria.getStrain() != null) && (!criteria.getStrain().isEmpty())) {
				String value = criteria.getStrain();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.createCriteria(STRAIN_NAME).add(Restrictions.like(NAME, value));break;
				case 2:crit.createCriteria(STRAIN_NAME).add(Restrictions.gt(NAME, value));break;
				case 3:crit.createCriteria(STRAIN_NAME).add(Restrictions.lt(NAME, value));break;
				case 4:crit.createCriteria(STRAIN_NAME).add(Restrictions.or(Restrictions.not(Restrictions.like(NAME, value)),Restrictions.isNull(NAME)));break;
				}
			}
			///*
			if ((criteria.getLabelDescription() != null) && (!criteria.getLabelDescription().isEmpty())) {
				String value = criteria.getLabelDescription();
				value = value.replace(ASTERISK, PERCENT);
				log.warning("descr="+value);
				switch (oper) {
				case 1:crit.add(Restrictions.like(LABEL_DESCRIPTION, value));break;
				case 2:crit.add(Restrictions.gt(LABEL_DESCRIPTION, value));break;
				case 3:crit.add(Restrictions.lt(LABEL_DESCRIPTION, value));break;
				case 4:crit.add(Restrictions.or(Restrictions.not(Restrictions.like(LABEL_DESCRIPTION, value)),Restrictions.isNull(LABEL_DESCRIPTION)));break;
				}
			}
			//*/
			/*
			if ((criteria.getDescription() != null) && (!criteria.getDescription().isEmpty())) {
				String value = criteria.getDescription();
		        String sql = "select id from tracker.sample where description ";//like '"+value+"'";
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:sql += " like '"+value+"'";break;
				case 2:sql += " > '" + value + "'";break;
				case 3:sql += " < '" + value + "'";break;
				case 4:sql += " != '" + value + "'";break;
				}
		        DBUtils db = new DBUtils("jdbc:mysql://nbcdbdev.nrel.gov:3306/tracker","trackeruser","trackeruserpw");
		        db.performQuery(sql,true);
		        List<Integer> sids = new ArrayList<Integer>();
		        while (db.getNextRow()) {
		        	Integer i = db.getIntColumn(1);
		        	sids.add(i);
		        }
		        db.close();
		        if (sids.size()==0)
		        	return null;
		        crit.add(Restrictions.in(ID, criteria.getTrackingIds()));
			}
			*/
			if ((criteria.getComposition() != null) && (!criteria.getComposition().isEmpty())) {
				String value = criteria.getComposition();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.createCriteria(COMPOSITION_NAME).add(Restrictions.like(NAME, value));break;
				case 2:crit.createCriteria(COMPOSITION_NAME).add(Restrictions.gt(NAME, value));break;
				case 3:crit.createCriteria(COMPOSITION_NAME).add(Restrictions.lt(NAME, value));break;
				case 4:crit.createCriteria(COMPOSITION_NAME).add(Restrictions.or(Restrictions.not(Restrictions.like(NAME, value)),Restrictions.isNull(NAME)));break;
				}
			}
			
			if ((criteria.getStatus() != null) && (!criteria.getStatus().isEmpty())) {
				String value = criteria.getStatus();
				value = value.replace(ASTERISK, PERCENT);
				switch (oper) {
				case 1:crit.createCriteria(STATUS).add(Restrictions.like(NAME, value));break;
				case 2:crit.createCriteria(STATUS).add(Restrictions.gt(NAME, value));break;
				case 3:crit.createCriteria(STATUS).add(Restrictions.lt(NAME, value));break;
				case 4:crit.createCriteria(STATUS).add(Restrictions.or(Restrictions.not(Restrictions.like(NAME, value)),Restrictions.isNull(NAME)));break;
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
					case 4:critLocation.add(Restrictions.or(Restrictions.not(Restrictions.like(BUILDING, value)),Restrictions.isNull(BUILDING)));break;
					}
				}
				
				if ((criteria.getRoom() != null) && (!criteria.getRoom().isEmpty())) {
					String value = criteria.getRoom();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:critLocation.add(Restrictions.like(ROOM, value));break;
					case 2:critLocation.add(Restrictions.gt(ROOM, value));break;
					case 3:critLocation.add(Restrictions.lt(ROOM, value));break;
					case 4:critLocation.add(Restrictions.or(Restrictions.not(Restrictions.like(ROOM, value)),Restrictions.isNull(ROOM)));break;
					}
				}
				
				if ((criteria.getHolder() != null) && (!criteria.getHolder().isEmpty())) {
					String value = criteria.getHolder();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:critLocation.add(Restrictions.like(HOLDER, value));break;
					case 2:critLocation.add(Restrictions.gt(HOLDER, value));break;
					case 3:critLocation.add(Restrictions.lt(HOLDER, value));break;
					case 4:critLocation.add(Restrictions.or(Restrictions.not(Restrictions.like(HOLDER, value)),Restrictions.isNull(HOLDER)));break;
					}
				}
				
				if ((criteria.getShelf() != null) && (!criteria.getShelf().isEmpty())) {
					String value = criteria.getShelf();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:critLocation.add(Restrictions.like(SHELF, value));break;
					case 2:critLocation.add(Restrictions.gt(SHELF, value));break;
					case 3:critLocation.add(Restrictions.lt(SHELF, value));break;
					case 4:critLocation.add(Restrictions.or(Restrictions.not(Restrictions.like(SHELF, value)),Restrictions.isNull(SHELF)));break;
					}
				}
				
				if ((criteria.getPackaging() != null) && (!criteria.getPackaging().isEmpty())) {
					String value = criteria.getPackaging();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:critLocation.add(Restrictions.like(PACKAGING, value));break;
					case 2:critLocation.add(Restrictions.gt(PACKAGING, value));break;
					case 3:critLocation.add(Restrictions.lt(PACKAGING, value));break;
					case 4:critLocation.add(Restrictions.or(Restrictions.not(Restrictions.like(PACKAGING, value)),Restrictions.isNull(PACKAGING)));break;
					}
				}
				
				if ((criteria.getSubLocation() != null) && (!criteria.getSubLocation().isEmpty())) {
					String value = criteria.getSubLocation();
					value = value.replace(ASTERISK, PERCENT);
					switch (oper) {
					case 1:critLocation.add(Restrictions.like(SUBLOCATION, value));break;
					case 2:critLocation.add(Restrictions.gt(SUBLOCATION, value));break;
					case 3:critLocation.add(Restrictions.lt(SUBLOCATION, value));break;
					case 4:critLocation.add(Restrictions.or(Restrictions.not(Restrictions.like(SUBLOCATION, value)),Restrictions.isNull(SUBLOCATION)));break;
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
		
		if ((sample.getLabel_description() != null) && (!sample.getLabel_description().isEmpty()))
			crit.setLabelDescription(sample.getLabel_description());
		
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
		
		//if (!DevTestProdConstants.DISPLAY_MODE.equals(DevTestProdConstants.ALGAE)) {
			if (sample.getProject() != null) 
				crit.setProject(sample.getProject());
		//}
		//else 
		if (sample.getDestination() != null) {
				crit.setDestination(sample.getDestination().getName());
		}
		
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
	 * Public method to find all custodian names.
	 * 
	 * @return <Collection<String>>
	 */
	@SuppressWarnings("unchecked")
	public Collection<String> findAllProjects() {

		Session session = getSession();
		
		Criteria crit = session.createCriteria(Sample.class)
			.setProjection(Projections.distinct(Projections.property(PROJECT)));
				
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