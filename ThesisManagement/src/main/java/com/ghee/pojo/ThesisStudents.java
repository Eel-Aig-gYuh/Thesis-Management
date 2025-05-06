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
@Table(name = "thesis_students")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ThesisStudents.findAll", query = "SELECT t FROM ThesisStudents t"),
    @NamedQuery(name = "ThesisStudents.findById", query = "SELECT t FROM ThesisStudents t WHERE t.id = :id"),
    @NamedQuery(name = "ThesisStudents.findByRegisteredAt", query = "SELECT t FROM ThesisStudents t WHERE t.registeredAt = :registeredAt")})
public class ThesisStudents implements Serializable {

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
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne
    private Users studentId;

    public ThesisStudents() {
    }

    public ThesisStudents(Long id) {
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

    public Users getStudentId() {
        return studentId;
    }

    public void setStudentId(Users studentId) {
        this.studentId = studentId;
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
        if (!(object instanceof ThesisStudents)) {
            return false;
        }
        ThesisStudents other = (ThesisStudents) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ghee.pojo.ThesisStudents[ id=" + id + " ]";
    }
    
}
