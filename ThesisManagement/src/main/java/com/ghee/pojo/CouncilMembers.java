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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author giahu
 */
@Entity
@Table(name = "council_members")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CouncilMembers.findAll", query = "SELECT c FROM CouncilMembers c"),
    @NamedQuery(name = "CouncilMembers.findById", query = "SELECT c FROM CouncilMembers c WHERE c.id = :id"),
    @NamedQuery(name = "CouncilMembers.findByRole", query = "SELECT c FROM CouncilMembers c WHERE c.role = :role")})
public class CouncilMembers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "role")
    private String role;
    @JoinColumn(name = "council_id", referencedColumnName = "id")
    @ManyToOne
    private Councils councilId;
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    @ManyToOne
    private Users memberId;

    public CouncilMembers() {
    }

    public CouncilMembers(Long id) {
        this.id = id;
    }

    public CouncilMembers(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Councils getCouncilId() {
        return councilId;
    }

    public void setCouncilId(Councils councilId) {
        this.councilId = councilId;
    }

    public Users getMemberId() {
        return memberId;
    }

    public void setMemberId(Users memberId) {
        this.memberId = memberId;
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
        if (!(object instanceof CouncilMembers)) {
            return false;
        }
        CouncilMembers other = (CouncilMembers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ghee.pojo.CouncilMembers[ id=" + id + " ]";
    }
    
}
