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
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author giahu
 */
@Entity
@Table(name = "councils")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Councils.findAll", query = "SELECT c FROM Councils c"),
    @NamedQuery(name = "Councils.findById", query = "SELECT c FROM Councils c WHERE c.id = :id"),
    @NamedQuery(name = "Councils.findByDefenseDate", query = "SELECT c FROM Councils c WHERE c.defenseDate = :defenseDate"),
    @NamedQuery(name = "Councils.findByDefenseLocation", query = "SELECT c FROM Councils c WHERE c.defenseLocation = :defenseLocation"),
    @NamedQuery(name = "Councils.findByCreatedAt", query = "SELECT c FROM Councils c WHERE c.createdAt = :createdAt")})
public class Councils implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    
    @Column(name = "defense_date")
    @Temporal(TemporalType.DATE)
    private Date defenseDate;
    
    @Size(max = 255)
    @Column(name = "defense_location")
    private String defenseLocation;
    
    @Size(max = 10)
    @Column(name = "status")
    private String status;
    
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnore
    private Users createdBy;
    
    @OneToMany(mappedBy = "councilId")
    @JsonIgnore
    private Set<CouncilMembers> councilMembersSet;
    
    @OneToMany(mappedBy = "councilId")
    @JsonIgnore
    private Set<CouncilTheses> councilThesesSet;
    
    
    public Councils() {
    }

    public Councils(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDefenseDate() {
        return defenseDate;
    }

    public void setDefenseDate(Date defenseDate) {
        this.defenseDate = defenseDate;
    }

    public String getDefenseLocation() {
        return defenseLocation;
    }

    public void setDefenseLocation(String defenseLocation) {
        this.defenseLocation = defenseLocation;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @XmlTransient
    public Set<CouncilMembers> getCouncilMembersSet() {
        return councilMembersSet;
    }

    public void setCouncilMembersSet(Set<CouncilMembers> councilMembersSet) {
        this.councilMembersSet = councilMembersSet;
    }

    @XmlTransient
    public Set<CouncilTheses> getCouncilThesesSet() {
        return councilThesesSet;
    }

    public void setCouncilThesesSet(Set<CouncilTheses> councilThesesSet) {
        this.councilThesesSet = councilThesesSet;
    }


    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the createdBy
     */
    public Users getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(Users createdBy) {
        this.createdBy = createdBy;
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
        if (!(object instanceof Councils)) {
            return false;
        }
        Councils other = (Councils) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ghee.pojo.Councils[ id=" + id + " ]";
    }
}
