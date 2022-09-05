package com.example.demo.Repository;

import com.example.demo.domain.Department;
import com.example.demo.domain.GradCondition;
import com.example.demo.domain.Student;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class GradConditionRepository {

    @PersistenceContext
    private EntityManager em;



    public Long save(GradCondition gradCondition) {
        em.persist(gradCondition);
        return gradCondition.getId();
    }

    public GradCondition findbyId(Long id) {
        return em.find(GradCondition.class, id);
    }

    public GradCondition findByDeptAndAdmissionYear(Department dept, int admissionYear) {
        GradCondition result = em.createQuery("select g from GradCondition g where g.admissionYear =:admissionYear "
                        + "and g.department =:dept", GradCondition.class)
                .setParameter("admissionYear", admissionYear)
                .setParameter("dept", dept)
                .getSingleResult();
        return result;
    }

}
