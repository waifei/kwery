package com.kwery.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.kwery.models.JobModel;
import com.kwery.services.job.JobSearchFilter;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;

public class JobDao {
    protected final Provider<EntityManager> entityManagerProvider;
    protected final JobExecutionDao jobExecutionDao;

    @Inject
    public JobDao(Provider<EntityManager> entityManagerProvider, JobExecutionDao jobExecutionDao) {
        this.entityManagerProvider = entityManagerProvider;
        this.jobExecutionDao = jobExecutionDao;
    }

    @Transactional
    public JobModel save(JobModel m) {
        EntityManager e = entityManagerProvider.get();

        long now = System.currentTimeMillis();
        m.setUpdated(System.currentTimeMillis());

        if (m.getId() != null && m.getId() > 0) {
            m.setCreated(getJobById(m.getId()).getCreated());
            return e.merge(m);
        } else {
            m.setCreated(now);
            e.persist(m);
            return m;
        }
    }

    @Transactional
    public void delete(int jobId) {
        EntityManager e = entityManagerProvider.get();
        jobExecutionDao.deleteByJobId(jobId);
        e.remove(getJobById(jobId));
    }

    @Transactional
    public JobModel getJobById(int jobId) {
        EntityManager e = entityManagerProvider.get();
        return e.find(JobModel.class, jobId);
    }

    @Transactional
    public JobModel getJobByName(String name) {
        EntityManager m = entityManagerProvider.get();
        CriteriaBuilder cb = m.getCriteriaBuilder();
        CriteriaQuery<JobModel> cq = cb.createQuery(JobModel.class);
        Root<JobModel> root = cq.from(JobModel.class);
        cq.where(cb.equal(root.get("name"), name));
        TypedQuery<JobModel> tq = m.createQuery(cq);
        List<JobModel> jobModels = tq.getResultList();
        if (jobModels.isEmpty()) {
            return null;
        } else {
            if (jobModels.size() > 1) {
                throw new AssertionError(String.format("More than one job with name %s present", name));
            }
            return jobModels.get(0);
        }
    }

    @Transactional
    public List<JobModel> getAllJobs() {
        EntityManager e = entityManagerProvider.get();
        CriteriaBuilder cb = e.getCriteriaBuilder();
        CriteriaQuery<JobModel> cq = cb.createQuery(JobModel.class);
        Root<JobModel> rootEntry = cq.from(JobModel.class);
        CriteriaQuery<JobModel> all = cq.select(rootEntry);
        TypedQuery<JobModel> allQuery = e.createQuery(all);
        return allQuery.getResultList();
    }

    @Transactional
    public List<JobModel> filterJobs(JobSearchFilter filter) {
        EntityManager m = entityManagerProvider.get();

        CriteriaBuilder cb = m.getCriteriaBuilder();
        CriteriaQuery<JobModel> cq = cb.createQuery(JobModel.class);
        Root<JobModel> jobModel = cq.from(JobModel.class);

        if (!filter.getJobLabelIds().isEmpty()) {
            Expression<Collection<Integer>> ids = jobModel.join("labels").get("id");
            cq.where(ids.in(filter.getJobLabelIds()));
        }

        if (!filter.getSqlQueryIds().isEmpty()) {
            Expression<Collection<Integer>> ids = jobModel.join("sqlQueries").get("id");
            cq.where(ids.in(filter.getSqlQueryIds()));
        }

        //We need this because updated can be null, which is a remnant from the days where we did not have these columns
        Expression<Object> updatedExpression = cb.selectCase().when(cb.isNull(jobModel.get("updated")), 0).otherwise(jobModel.get("updated"));
        cq.orderBy(cb.desc(updatedExpression), cb.desc(jobModel.get("id")));

        if (filter.getPageNo() != null && filter.getResultCount() != null) {
            return m.createQuery(cq)
                    .setMaxResults(filter.getResultCount())
                    .setFirstResult(filter.getPageNo() * filter.getResultCount()).getResultList();
        } else {
            return m.createQuery(cq).getResultList();
        }
    }

    @Transactional
    public List<JobModel> getJobsByJobLabelIds(Collection<Integer> jobLabelIds) {
        EntityManager m = entityManagerProvider.get();

        CriteriaBuilder cb = m.getCriteriaBuilder();
        CriteriaQuery<JobModel> cq = cb.createQuery(JobModel.class);
        Root<JobModel> jobModel = cq.from(JobModel.class);

        Expression<Collection<Integer>> ids = jobModel.join("labels").get("id");
        cq.where(ids.in(jobLabelIds));

        TypedQuery<JobModel> tq = m.createQuery(cq);
        return tq.getResultList();
    }
}
