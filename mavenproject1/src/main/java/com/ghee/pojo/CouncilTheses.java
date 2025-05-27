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
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author giahu
 */
@Entity
@Table(name = "council_theses")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CouncilTheses.findAll", query = "SELECT c FROM CouncilTheses c"),
    @NamedQuery(name = "CouncilTheses.findById", query = "SELECT c FROM CouncilTheses c WHERE c.id = :id")})
public class CouncilTheses implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "council_id", referencedColumnName = "id")
    @ManyToOne
    private Councils councilId;
    @JoinColumn(name = "thesis_id", referencedColumnName = "id")
    @ManyToOne
    private Theses thesisId;

    public CouncilTheses() {
    }

    public CouncilTheses(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Councils getCouncilId() {
        return councilId;
    }

    public void setCouncilId(Councils councilId) {
        this.councilId = councilId;
    }

    public Theses getThesisId() {
        return thesisId;
    }

    public void setThesisId(Theses thesisId) {
        this.thesisId = thesisId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CouncilTheses that = (CouncilTheses) o;
        return Objects.equals(councilId, that.councilId) &&
               Objects.equals(thesisId, that.thesisId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(councilId, thesisId);
    }

    @Override
    public String toString() {
        return "com.ghee.pojo.CouncilTheses[ id=" + id + " ]";
    }
    
}
