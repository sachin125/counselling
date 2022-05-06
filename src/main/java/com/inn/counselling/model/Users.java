package com.inn.counselling.model;

import java.io.Serializable;
import java.time.Period;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonView;
import com.inn.counselling.enumWrapper.Gender;
import com.inn.counselling.utils.ConfigUtil;
import com.inn.counselling.utils.view.View.BasicView;
import com.inn.counselling.utils.view.View.NoView;
import com.inn.counselling.utils.view.View.UserBasicView;
import com.inn.counselling.utils.view.View.UserInContextView;


@NamedQueries({
	@NamedQuery(name="User.findByUsername",query="select u from Users u where lower(u.username)=:username"),
	@NamedQuery(name="User.findByUsernameOrConatctNo",query="select u from Users u where lower(u.username)=:username or u.contactno=:contactno"),

})

//@Audited
@XmlRootElement(name="Users")
@Entity
@Table(name="users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})	//It is used when we use FetchType.Lazy on manyToone, So jackson is not able to serialize that object
public class Users implements Serializable{
	
	public Users() {
		super();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@JsonView(value = {BasicView.class})
	@Column(name = "firstname", length = 30,nullable=false)
	@NotBlank(message = "Please provide first name")
	private String firstname;
	
	@JsonView(value = {BasicView.class})
	@Column(name = "lastname", length = 30,nullable=false)
	@NotBlank(message = "Please provide last name")
	private String lastname;
	
	@JsonView(value = {BasicView.class})
	@Column(name = "username", length = 30,nullable=false, unique = true)
	@NotBlank(message = "Please provide emailid")
	@Email(message="Please provide valid emailid")
	private String username;
	
	@JsonView(value = {UserBasicView.class,UserInContextView.class})
	@Column(name = "contactno", length = 13,nullable=false, unique = true)
	@NotNull(message = "Please provide mobileno")
	private Long contactno;

	@JsonView(value = {UserBasicView.class})
	@Basic
	private Boolean enabled=false;
	
	@JsonView(value = {UserBasicView.class})
	@Basic
	private Boolean deleted=false;


	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(nullable=false, length=255)
	private String checkSum;

	@JsonView(value = {UserBasicView.class})
	@Column(name="createdTime",insertable=true,updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@JsonView(value = {UserBasicView.class})
	@Column(name="modifiedTime",insertable=true,updatable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedTime;
		
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@NotAudited
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="user_role",joinColumns=@JoinColumn(name="user_id"),
						inverseJoinColumns=@JoinColumn(name="role_id"))
	private Set<Role> roles = new HashSet<Role>();
		
	@JsonIgnore
	@JsonView(value = {NoView.class})
	@NotAudited
	@ManyToOne
	@JoinColumn(name="domain_id")
	private Domain domain;
	
	@JsonView(value = {UserBasicView.class})
	@Column(name = "dob")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dob;
	
	@JsonView(value = {UserBasicView.class})
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	@Transient
	private Integer age;

	@JsonIgnore
	public void setAge(Integer age) {
		this.age = age;
	}

	@JsonProperty
	public Integer getAge() {
		if(this.dob!=null) {
			Date currentDate = new Date(); 
			Period period = ConfigUtil.calculateAge(this.dob, currentDate);
			return period.getYears();
		}else {
			return 0;
		}
	}
 
	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}	

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getContactno() {
		return contactno;
	}

	public void setContactno(Long contactno) {
		this.contactno = contactno;
	}
	
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public Boolean getDeleted() {
		return deleted;
	}
	
	public String getCheckSum() {
		return checkSum;
	}

	public void setCheckSum(String checkSum) {
		this.checkSum = checkSum;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
