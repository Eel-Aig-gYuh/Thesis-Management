/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author giahu
 */
@Entity
@Table(name = "thesis_criteria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ThesisCriteria.findAll", query = "SELECT t FROM ThesisCriteria t"),
    @NamedQuery(name = "ThesisCriteria.findById", query = "SELECT t FROM ThesisCriteria t WHERE t.id = :id"),
    @NamedQuery(name = "ThesisCriteria.findByCreatedAt", query = "SELECT t FROM ThesisCriteria t WHERE t.createdAt = :createdAt")})
public class ThesisCriteria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @JoinColumn(name = "criteria_id", referencedColumnName = "id")
    @ManyToOne
    private Criteria criteriaId;
    
    @JoinColumn(name = "thesis_id", referencedColumnName = "id")
    @ManyToOne
    private Theses thesisId;
    

    public ThesisCriteria() {
    }

    public ThesisCriteria(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Criteria getCriteriaId() {
        return criteriaId;
    }

    public void setCriteriaId(Criteria criteriaId) {
        this.criteriaId = criteriaId;
    }

    public Theses getThesisId() {
        return thesisId;
    }

    public void setThesisId(Theses thesisId) {
        this.thesisId = thesisId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(thesisId != null ? thesisId.getId() : null, criteriaId != null ? criteriaId.getId() : null);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ThesisCriteria)) {
            return false;
        }
        ThesisCriteria other = (ThesisCriteria) obj;
        return Objects.equals(thesisId != null ? thesisId.getId() : null, other.thesisId != null ? other.thesisId.getId() : null) &&
               Objects.equals(criteriaId != null ? criteriaId.getId() : null, other.criteriaId != null ? other.criteriaId.getId() : null);
    }

    @Override
    public String toString() {
        return "com.ghee.pojo.ThesisCriteria[ id=" + id + " ]";
    }
    
}
