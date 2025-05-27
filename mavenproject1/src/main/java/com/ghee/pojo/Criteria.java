/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author giahu
 */
@Entity
@Table(name = "criteria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Criteria.findAll", query = "SELECT c FROM Criteria c"),
    @NamedQuery(name = "Criteria.findById", query = "SELECT c FROM Criteria c WHERE c.id = :id"),
    @NamedQuery(name = "Criteria.findByName", query = "SELECT c FROM Criteria c WHERE c.name = :name"),
    @NamedQuery(name = "Criteria.findByMaxScore", query = "SELECT c FROM Criteria c WHERE c.maxScore = :maxScore"),
    @NamedQuery(name = "Criteria.findByCreatedAt", query = "SELECT c FROM Criteria c WHERE c.createdAt = :createdAt")})
public class Criteria implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name")
    private String name;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "max_score")
    private BigDecimal maxScore;
    
    @OneToMany(mappedBy = "criteriaId")
    private Set<ThesisCriteria> thesisCriteriaSet;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @OneToMany(mappedBy = "criteriaId")
    @JsonIgnore
    private Set<Scores> scoresSet;
    
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private Users createdBy;

    public Criteria() {
    }

    public Criteria(Long id) {
        this.id = id;
    }

    public Criteria(Long id, String name, BigDecimal maxScore) {
        this.id = id;
        this.name = name;
        this.maxScore = maxScore;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public BigDecimal getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(BigDecimal maxScore) {
        this.maxScore = maxScore;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @XmlTransient
    public Set<Scores> getScoresSet() {
        return scoresSet;
    }

    public void setScoresSet(Set<Scores> scoresSet) {
        this.scoresSet = scoresSet;
    }

    public Users getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Users createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return Objects.hash(id);
        }
        return Objects.hash(name, maxScore);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Criteria)) {
            return false;
        }
        Criteria other = (Criteria) obj;
        if (id != null && other.id != null) {
            return id.equals(other.id);
        }
        return Objects.equals(name, other.name) && Objects.equals(maxScore, other.maxScore);
    }

    @Override
    public String toString() {
        return "com.ghee.pojo.Criteria[ id=" + id + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Set<ThesisCriteria> getThesisCriteriaSet() {
        return thesisCriteriaSet;
    }

    public void setThesisCriteriaSet(Set<ThesisCriteria> thesisCriteriaSet) {
        this.thesisCriteriaSet = thesisCriteriaSet;
    }
    
}
