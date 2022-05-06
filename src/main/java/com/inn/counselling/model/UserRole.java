//package com.inn.counselling.model;
//
//import java.io.Serializable;
//import java.util.HashSet;
//import java.util.Set;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.JoinTable;
//import javax.persistence.ManyToMany;
//import javax.persistence.NamedQueries;
//import javax.persistence.NamedQuery;
//import javax.persistence.Table;
//import javax.xml.bind.annotation.XmlRootElement;
//
//import org.apache.commons.lang.builder.ToStringBuilder;
//import org.hibernate.envers.Audited;
//import org.hibernate.envers.NotAudited;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonInclude;
//
//@NamedQueries({
//		@NamedQuery(name = "Role.findRolesByRoleName", query = "select r from Role r where r.name=:rolename"),
//		@NamedQuery(name = "Role.findPermissionsByRoleId", query = "select p from Role r join r.permissions p where r.id=:roleId"),
//})
//
//
//@XmlRootElement(name="UserRole")
//@Entity
//@Table(name="user_role")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//public class UserRole implements Serializable{
//	
//	public UserRole() {
//		super();
//	}
//
//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	private Long id;
//	
//	@ManyToMany(fetch=FetchType.EAGER)
//	@JoinTable(name="role_permission",joinColumns=@JoinColumn(name="role_id"),
//									  inverseJoinColumns=@JoinColumn(name="permission_id"))
//	private Set<Permission> permissions = new HashSet<Permission>();
//	
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public Set<Permission> getPermissions() {
//		return permissions;
//	}
//
//	public void setPermissions(Set<Permission> permissions) {
//		this.permissions = permissions;
//	}
//
//	@Override
//	public String toString() {
//		return ToStringBuilder.reflectionToString(this);
//	}
//}
