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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author giahu
 */
@Entity
@Table(name = "thesis_files")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ThesisFiles.findAll", query = "SELECT t FROM ThesisFiles t"),
    @NamedQuery(name = "ThesisFiles.findById", query = "SELECT t FROM ThesisFiles t WHERE t.id = :id"),
    @NamedQuery(name = "ThesisFiles.findByFileUrl", query = "SELECT t FROM ThesisFiles t WHERE t.fileUrl = :fileUrl"),
    @NamedQuery(name = "ThesisFiles.findByFileName", query = "SELECT t FROM ThesisFiles t WHERE t.fileName = :fileName"),
    @NamedQuery(name = "ThesisFiles.findBySubmittedAt", query = "SELECT t FROM ThesisFiles t WHERE t.submittedAt = :submittedAt")})
public class ThesisFiles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "file_url")
    private String fileUrl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "submitted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submittedAt;
    @JoinColumn(name = "thesis_id", referencedColumnName = "id")
    @ManyToOne
    private Theses thesisId;
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne
    private Users studentId;

    public ThesisFiles() {
    }

    public ThesisFiles(Long id) {
        this.id = id;
    }

    public ThesisFiles(Long id, String fileUrl, String fileName) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
    }

    public ThesisFiles(String fileUrl, String fileName, Date submittedAt, Theses thesisId, Users studentId) {
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.submittedAt = submittedAt;
        this.thesisId = thesisId;
        this.studentId = studentId;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
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
        if (!(object instanceof ThesisFiles)) {
            return false;
        }
        ThesisFiles other = (ThesisFiles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ghee.pojo.ThesisFiles[ id=" + id + " ]";
    }
    
}
