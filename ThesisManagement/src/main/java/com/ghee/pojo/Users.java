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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author giahu
 */
@Entity
@Table(name = "users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findById", query = "SELECT u FROM Users u WHERE u.id = :id"),
    @NamedQuery(name = "Users.findByUsername", query = "SELECT u FROM Users u WHERE u.username = :username"),
    @NamedQuery(name = "Users.findByPassword", query = "SELECT u FROM Users u WHERE u.password = :password"),
    @NamedQuery(name = "Users.findByLastname", query = "SELECT u FROM Users u WHERE u.lastname = :lastname"),
    @NamedQuery(name = "Users.findByFirstname", query = "SELECT u FROM Users u WHERE u.firstname = :firstname"),
    @NamedQuery(name = "Users.findByAvatar", query = "SELECT u FROM Users u WHERE u.avatar = :avatar"),
    @NamedQuery(name = "Users.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email"),
    @NamedQuery(name = "Users.findByRole", query = "SELECT u FROM Users u WHERE u.role = :role"),
    @NamedQuery(name = "Users.findByMajor", query = "SELECT u FROM Users u WHERE u.major = :major"),
    @NamedQuery(name = "Users.findByIsActive", query = "SELECT u FROM Users u WHERE u.isActive = :isActive")})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "username", unique = true)
    private String username;
    @Basic(optional = false)
    @NotNull
    @JsonIgnore
    @Size(min = 1, max = 255)
    @Column(name = "password")
    private String password;
    @Size(max = 255)
    @Column(name = "lastname")
    private String lastname;
    @Size(max = 50)
    @Column(name = "firstname")
    private String firstname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "avatar")
    private String avatar;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "email", unique = true)
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "role", nullable = false)
    private String role;
    @Size(max = 100)
    @Column(name = "major")
    private String major;
    @Column(name = "is_active")
    private Boolean isActive;
    
    @OneToMany(mappedBy = "memberId")
    @JsonIgnore
    private Set<CouncilMembers> councilMembersSet;
    
    @OneToMany(mappedBy = "councilMemberId")
    @JsonIgnore
    private Set<Scores> scoresSet;
    
    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    private Set<Criteria> criteriaSet;
    
    @OneToMany(mappedBy = "studentId")
    @JsonIgnore
    private Set<ThesisFiles> thesisFilesSet;
    
    @OneToMany(mappedBy = "chairmanId")
    @JsonIgnore
    private Set<Councils> councilsSet;
    
    @OneToMany(mappedBy = "secretaryId")
    @JsonIgnore
    private Set<Councils> councilsSet1;
    
    @OneToMany(mappedBy = "advisorId")
    @JsonIgnore
    private Set<ThesisAdvisors> thesisAdvisorsSet;
    
    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    private Set<Theses> thesesSet;
    
    @OneToMany(mappedBy = "studentId")
    @JsonIgnore
    private Set<ThesisStudents> thesisStudentsSet;
    
    @OneToMany(mappedBy = "userId")
    @JsonIgnore
    private Set<Notifications> notificationsSet;
    
    @OneToMany(mappedBy = "reviewerId")
    @JsonIgnore
    private Set<ThesisReviewers> thesisReviewersSet;
   
    
    public Users() {
    }

    public Users(Long id) {
        this.id = id;
    }

    public Users(Long id, String username, String password, String avatar, String email, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @XmlTransient
    public Set<CouncilMembers> getCouncilMembersSet() {
        return councilMembersSet;
    }

    public void setCouncilMembersSet(Set<CouncilMembers> councilMembersSet) {
        this.councilMembersSet = councilMembersSet;
    }

    @XmlTransient
    public Set<Scores> getScoresSet() {
        return scoresSet;
    }

    public void setScoresSet(Set<Scores> scoresSet) {
        this.scoresSet = scoresSet;
    }

    @XmlTransient
    public Set<Criteria> getCriteriaSet() {
        return criteriaSet;
    }

    public void setCriteriaSet(Set<Criteria> criteriaSet) {
        this.criteriaSet = criteriaSet;
    }

    @XmlTransient
    public Set<ThesisFiles> getThesisFilesSet() {
        return thesisFilesSet;
    }

    public void setThesisFilesSet(Set<ThesisFiles> thesisFilesSet) {
        this.thesisFilesSet = thesisFilesSet;
    }

    @XmlTransient
    public Set<Councils> getCouncilsSet() {
        return councilsSet;
    }

    public void setCouncilsSet(Set<Councils> councilsSet) {
        this.councilsSet = councilsSet;
    }

    @XmlTransient
    public Set<Councils> getCouncilsSet1() {
        return councilsSet1;
    }

    public void setCouncilsSet1(Set<Councils> councilsSet1) {
        this.councilsSet1 = councilsSet1;
    }

    @XmlTransient
    public Set<ThesisAdvisors> getThesisAdvisorsSet() {
        return thesisAdvisorsSet;
    }

    public void setThesisAdvisorsSet(Set<ThesisAdvisors> thesisAdvisorsSet) {
        this.thesisAdvisorsSet = thesisAdvisorsSet;
    }

    @XmlTransient
    public Set<Theses> getThesesSet() {
        return thesesSet;
    }

    public void setThesesSet(Set<Theses> thesesSet) {
        this.thesesSet = thesesSet;
    }

    @XmlTransient
    public Set<ThesisStudents> getThesisStudentsSet() {
        return thesisStudentsSet;
    }

    public void setThesisStudentsSet(Set<ThesisStudents> thesisStudentsSet) {
        this.thesisStudentsSet = thesisStudentsSet;
    }

    @XmlTransient
    public Set<Notifications> getNotificationsSet() {
        return notificationsSet;
    }

    public void setNotificationsSet(Set<Notifications> notificationsSet) {
        this.notificationsSet = notificationsSet;
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
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ghee.pojo.Users[ id=" + id + " ]";
    } 
}