//$Id: GenericHibernateDAO.java 656 2008-08-14 02:34:43Z glenn $
package gov.nrel.nbc.tracker.dao;

import gov.nrel.nbc.tracker.hibernate.HibernateSessionFactory;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;

/**
 * Implements the generic CRUD data access operations using Hibernate APIs.
 *
 *<p> To write a DAO, subclass and parameterize this class with your
 * persistent class.  Of course, assuming that you have a traditional
 * 1:1 approach for Entity:DAO design.
 *
 *<p> You have to inject a current Hibernate <tt>Session</tt> to use a
 * DAO. Otherwise, this generic implementation will use
 * <tt>HibernateUtil.getSessionFactory()</tt> to obtain the current
 * <tt>Session</tt>.
 *
 * @author Christian Bauer
 * @see <a href="http://www.hibernate.org/328.html">Generic Data Access Objects at http://www.hibernate.org/328.html</a>
 * @see <a href="http://www.hibernate.org/42.html">Sessions and Transactions at http://www.hibernate.org/42.html</a>
 */
public abstract class GenericHibernateDAO<T, ID extends Serializable>
    implements GenericDAO<T, ID> {

    // Static initialization--------------------------------------------

    // Instance variables-----------------------------------------------

    private Class<T> persistentClass;
    private Session session;


    // Constructors-----------------------------------------------------

    @SuppressWarnings("unchecked")
	public GenericHibernateDAO() {

        this.persistentClass
            = (Class<T>) ((ParameterizedType) getClass()
                          .getGenericSuperclass()).getActualTypeArguments()[0];
    }


    // Methods----------------------------------------------------------

    public void setSession(Session s) {
        this.session = s;
    }

    protected Session getSession() {
        // For using several db's, remove this if block and have
		// subclasses of this class call setSession() with the
		// appropriate HibernateUtil.
        if (session == null) {
			session = HibernateSessionFactory.getSession();
		}
        return session;
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    @SuppressWarnings("unchecked")
    public T findById(ID id, boolean lock) {
        T entity;
        if (lock) {
			entity = (T) getSession().load(getPersistentClass(),
                                           id,
                                           LockMode.UPGRADE);
		} else {
			entity = (T) getSession().load(getPersistentClass(), id);
		}

        return entity;
    }

    //@SuppressWarnings("unchecked")
    public List<T> findAll() {
        return findByCriteria();
    }

    @SuppressWarnings("unchecked")
    public List<T> findByExample(T exampleInstance, String... excludeProperty) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        Example example =  Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        return crit.list();
    }

    //@SuppressWarnings("unchecked")
    public T makePersistent(T entity) {
        getSession().saveOrUpdate(entity);
        return entity;
    }

    public void makeTransient(T entity) {
        getSession().delete(entity);
    }

    public void flush() {
        getSession().flush();
    }

    public void clear() {
        getSession().clear();
    }

    /**
     * Use this inside subclasses as a convenience method.
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(Criterion... criterion) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        for (Criterion c : criterion) {
            crit.add(c);
        }
        return crit.list();
    }

}

