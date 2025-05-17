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
import static java.util.Objects.hash;

/**
 *
 * @author giahu
 */
@Entity
@Table(name = "thesis_reviewers")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ThesisReviewers.findAll", query = "SELECT t FROM ThesisReviewers t"),
    @NamedQuery(name = "ThesisReviewers.findById", query = "SELECT t FROM ThesisReviewers t WHERE t.id = :id"),
    @NamedQuery(name = "ThesisReviewers.findByAssignedAt", query = "SELECT t FROM ThesisReviewers t WHERE t.assignedAt = :assignedAt")})
public class ThesisReviewers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "assigned_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignedAt;
    @JoinColumn(name = "thesis_id", referencedColumnName = "id")
    @ManyToOne
    private Theses thesisId;
    @JoinColumn(name = "reviewer_id", referencedColumnName = "id")
    @ManyToOne
    private Users reviewerId;

    public ThesisReviewers() {
    }

    public ThesisReviewers(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Date assignedAt) {
        this.assignedAt = assignedAt;
    }

    public Theses getThesisId() {
        return thesisId;
    }

    public void setThesisId(Theses thesisId) {
        this.thesisId = thesisId;
    }

    public Users getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Users reviewerId) {
        this.reviewerId = reviewerId;
    }

//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (id != null ? id.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof ThesisReviewers)) {
//            return false;
//        }
//        ThesisReviewers other = (ThesisReviewers) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
    
    @Override
    public int hashCode() {
        return hash(thesisId, reviewerId);
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (this == object) {
            return true;
        }
        if (!(object instanceof ThesisReviewers)) {
            return false;
        }
        ThesisReviewers that = (ThesisReviewers) object;
        return thesisId != null && reviewerId != null
                && thesisId.equals(that.thesisId)
                && reviewerId.equals(that.reviewerId);
    }

    @Override
    public String toString() {
        return "com.ghee.pojo.ThesisReviewers[ id=" + id + " ]";
    }
    
}
