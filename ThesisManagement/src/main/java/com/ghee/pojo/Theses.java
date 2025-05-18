/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author giahu
 */
@Entity
@Table(name = "theses")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Theses.findAll", query = "SELECT t FROM Theses t"),
    @NamedQuery(name = "Theses.findById", query = "SELECT t FROM Theses t WHERE t.id = :id"),
    @NamedQuery(name = "Theses.findByTitle", query = "SELECT t FROM Theses t WHERE t.title = :title"),
    @NamedQuery(name = "Theses.findByStatus", query = "SELECT t FROM Theses t WHERE t.status = :status"),
    @NamedQuery(name = "Theses.findByCreatedAt", query = "SELECT t FROM Theses t WHERE t.createdAt = :createdAt")})
public class Theses implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    @Size(max = 50)
    @Column(name = "semester")
    private String semester;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "title")
    private String title;
    
    @Basic(optional = false)
    @Column(name = "average_score")
    private BigDecimal averageScore;
    
    @Size(max = 10)
    @Column(name = "status")
    private String status;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @OneToMany(mappedBy = "thesisId")
    @JsonIgnore
    private Set<CouncilTheses> councilThesesSet;
    
    @OneToMany(mappedBy = "thesisId")
    @JsonIgnore
    private Set<Scores> scoresSet;
    
    @OneToMany(mappedBy = "thesisId")
    @JsonIgnore
    private Set<ThesisFiles> thesisFilesSet;
    
    @OneToMany(mappedBy = "thesisId", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<ThesisAdvisors> thesisAdvisorsSet;
    
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private Users createdBy;
    
    @OneToMany(mappedBy = "thesisId", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<ThesisStudents> thesisStudentsSet;
    
    @OneToMany(mappedBy = "thesisId", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<ThesisReviewers> thesisReviewersSet;

    public Theses() {
    }

    public Theses(Long id) {
        this.id = id;
    }

    public Theses(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @XmlTransient
    public Set<CouncilTheses> getCouncilThesesSet() {
        return councilThesesSet;
    }

    public void setCouncilThesesSet(Set<CouncilTheses> councilThesesSet) {
        this.councilThesesSet = councilThesesSet;
    }

    @XmlTransient
    public Set<Scores> getScoresSet() {
        return scoresSet;
    }

    public void setScoresSet(Set<Scores> scoresSet) {
        this.scoresSet = scoresSet;
    }

    @XmlTransient
    public Set<ThesisFiles> getThesisFilesSet() {
        return thesisFilesSet;
    }

    public void setThesisFilesSet(Set<ThesisFiles> thesisFilesSet) {
        this.thesisFilesSet = thesisFilesSet;
    }

    @XmlTransient
    public Set<ThesisAdvisors> getThesisAdvisorsSet() {
        return thesisAdvisorsSet;
    }

    public void setThesisAdvisorsSet(Set<ThesisAdvisors> thesisAdvisorsSet) {
        this.thesisAdvisorsSet = thesisAdvisorsSet;
    }

    public Users getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Users createdBy) {
        this.createdBy = createdBy;
    }

    @XmlTransient
    public Set<ThesisStudents> getThesisStudentsSet() {
        return thesisStudentsSet;
    }

    public void setThesisStudentsSet(Set<ThesisStudents> thesisStudentsSet) {
        this.thesisStudentsSet = thesisStudentsSet;
    }

    @XmlTransient
    public Set<ThesisReviewers> getThesisReviewersSet() {
        return thesisReviewersSet;
    }

    public void setThesisReviewersSet(Set<ThesisReviewers> thesisReviewersSet) {
        this.thesisReviewersSet = thesisReviewersSet;
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
        if (!(object instanceof Theses)) {
            return false;
        }
        Theses other = (Theses) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ghee.pojo.Theses[ id=" + id + " ]";
    }

    /**
     * @return the semester
     */
    public String getSemester() {
        return semester;
    }

    /**
     * @param semester the semester to set
     */
    public void setSemester(String semester) {
        this.semester = semester;
    }

    /**
     * @return the averageScore
     */
    public BigDecimal getAverageScore() {
        return averageScore;
    }

    /**
     * @param averageScore the averageScore to set
     */
    public void setAverageScore(BigDecimal averageScore) {
        this.averageScore = averageScore;
    }
    
}
