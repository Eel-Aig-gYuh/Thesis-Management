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

/**
 *
 * @author giahu
 */
@Entity
@Table(name = "thesis_advisors")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ThesisAdvisors.findAll", query = "SELECT t FROM ThesisAdvisors t"),
    @NamedQuery(name = "ThesisAdvisors.findById", query = "SELECT t FROM ThesisAdvisors t WHERE t.id = :id"),
    @NamedQuery(name = "ThesisAdvisors.findByRegisteredAt", query = "SELECT t FROM ThesisAdvisors t WHERE t.registeredAt = :registeredAt")})
public class ThesisAdvisors implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "registered_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registeredAt;
    @JoinColumn(name = "thesis_id", referencedColumnName = "id")
    @ManyToOne
    private Theses thesisId;
    @JoinColumn(name = "advisor_id", referencedColumnName = "id")
    @ManyToOne
    private Users advisorId;

    public ThesisAdvisors() {
    }

    public ThesisAdvisors(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Date registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Theses getThesisId() {
        return thesisId;
    }

    public void setThesisId(Theses thesisId) {
        this.thesisId = thesisId;
    }

    public Users getAdvisorId() {
        return advisorId;
    }

    public void setAdvisorId(Users advisorId) {
        this.advisorId = advisorId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ThesisAdvisors)) {
            return false;
        }
        ThesisAdvisors other = (ThesisAdvisors) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ghee.pojo.ThesisAdvisors[ id=" + id + " ]";
    }
    
}
