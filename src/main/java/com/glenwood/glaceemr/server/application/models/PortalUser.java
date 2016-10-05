package com.glenwood.glaceemr.server.application.models;

import java.sql.Timestamp;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glenwood.glaceemr.server.utils.JsonTimestampSerializer;

@Entity
@Table(name = "h809")
public class PortalUser implements UserDetails {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="h809_h809001_seq")
	@SequenceGenerator(name ="h809_h809001_seq", sequenceName="h809_h809001_seq", allocationSize=1)
	@Column(name="h809001")
	private Integer h809001;

	
	@Column(name="h809002")
	private Long id;

	@Column(name="h809003")
	private Date h809003;

	@Column(name="h809004")
	private String username;

	@Column(name="h809005")
	private String password;

	@Column(name="h809006")
	private Integer isActive;

	@Column(name="h809007")
	private String h809007;

	@Column(name="h809008")
	private String h809008;

	@Column(name="h809009")
	private Integer isOldUser;

	@Column(name="h809010")
	private String originalPassword;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="h809011")
	private Timestamp h809011;

	@Column(name="wrong_entry_count")
	private Integer wrongEntryCount;

	@JsonSerialize(using = JsonTimestampSerializer.class)
	@Column(name="access_time")
	private Timestamp accessTime;

	@Column(name="security_question1")
	private String securityQuestion1;

	@Column(name="security_question2")
	private String securityQuestion2;

	@Column(name="security_question3")
	private String securityQuestion3;

	@Column(name="security_answer1")
	private String securityAnswer1;

	@Column(name="security_answer2")
	private String securityAnswer2;

	@Column(name="security_answer3")
	private String securityAnswer3;
	
	@Column(name="password_reset")
	private Integer passwordReset;
	
	@Column(name="from_portal")
	private boolean fromPortal;
	
	@Column(name="from_portal_isactive")
	private boolean fromPortalIsactive;
	
	@Column(name="h809_token")
	private String h809Token;

	public Integer getH809001() {
		return h809001;
	}

	public void setH809001(Integer h809001) {
		this.h809001 = h809001;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getH809003() {
		return h809003;
	}

	public void setH809003(Date h809003) {
		this.h809003 = h809003;
	}
	@JsonIgnore
	public String getUsername() {
		return username;
	}
	@JsonProperty
	public void setUsername(String username) {
		this.username = username;
	}
	@JsonIgnore
	public String getPassword() {
		return password;
	}
	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public String getOriginalPassword() {
		return originalPassword;
	}

	public void setOriginalPassword(String originalPassword) {
		this.originalPassword = originalPassword;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	
	public String getH809007() {
		return h809007;
	}

	public void setH809007(String h809007) {
		this.h809007 = h809007;
	}

	public String getH809008() {
		return h809008;
	}

	public void setH809008(String h809008) {
		this.h809008 = h809008;
	}

	public Integer getIsOldUser() {
		return isOldUser;
	}

	public void setIsOldUser(Integer isOldUser) {
		this.isOldUser = isOldUser;
	}

	public Timestamp getH809011() {
		return h809011;
	}

	public void setH809011(Timestamp h809011) {
		this.h809011 = h809011;
	}

	public Integer getWrongEntryCount() {
		return wrongEntryCount;
	}

	public void setWrongEntryCount(Integer wrongEntryCount) {
		this.wrongEntryCount = wrongEntryCount;
	}

	public Timestamp getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Timestamp accessTime) {
		this.accessTime = accessTime;
	}

	public String getSecurityQuestion1() {
		return securityQuestion1;
	}

	public void setSecurityQuestion1(String securityQuestion1) {
		this.securityQuestion1 = securityQuestion1;
	}

	public String getSecurityQuestion2() {
		return securityQuestion2;
	}

	public void setSecurityQuestion2(String securityQuestion2) {
		this.securityQuestion2 = securityQuestion2;
	}

	public String getSecurityQuestion3() {
		return securityQuestion3;
	}

	public void setSecurityQuestion3(String securityQuestion3) {
		this.securityQuestion3 = securityQuestion3;
	}

	public String getSecurityAnswer1() {
		return securityAnswer1;
	}

	public void setSecurityAnswer1(String securityAnswer1) {
		this.securityAnswer1 = securityAnswer1;
	}

	public String getSecurityAnswer2() {
		return securityAnswer2;
	}

	public void setSecurityAnswer2(String securityAnswer2) {
		this.securityAnswer2 = securityAnswer2;
	}

	public String getSecurityAnswer3() {
		return securityAnswer3;
	}

	public void setSecurityAnswer3(String securityAnswer3) {
		this.securityAnswer3 = securityAnswer3;
	}
	
	public Integer getPasswordReset() {
		return passwordReset;
	}

	public void setPasswordReset(Integer passwordReset) {
		this.passwordReset = passwordReset;
	}

	public boolean isFromPortal() {
		return fromPortal;
	}

	public void setFromPortal(boolean fromPortal) {
		this.fromPortal = fromPortal;
	}

	public boolean isFromPortalIsactive() {
		return fromPortalIsactive;
	}

	public void setFromPortalIsactive(boolean fromPortalIsactive) {
		this.fromPortalIsactive = fromPortalIsactive;
	}

	public String getH809Token() {
		return h809Token;
	}

	public void setH809Token(String h809Token) {
		this.h809Token = h809Token;
	}

	public PortalUser()
	{

	}

	public PortalUser(String username) {
		this.username = username;
	}
	public PortalUser(String username,Date expires,boolean accountEnabled,boolean accountExpired,
			boolean credentialsExpired,boolean accountLocked) {
		this.username = username;
		this.expires = expires.getTime();
		this.accountEnabled=accountEnabled;
		this.accountExpired=accountExpired;
		this.credentialsExpired=credentialsExpired;
		this.accountLocked=accountLocked;
	}
	
	@Transient
	private long expires;
	
	@Transient
	private boolean accountExpired=false;
	
	@Transient
	private boolean accountLocked=false;
	
	@Transient
	private boolean credentialsExpired=false;
	
	@Transient
	private boolean accountEnabled=false;
	
	@Transient
	private String newPassword;	
	
	@JsonIgnore
	public String getNewPassword() {
		return newPassword;
	}
	@JsonProperty
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	
	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return !accountExpired;
	}
	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return !accountLocked;
	}
	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return !credentialsExpired;
	}
	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return !accountEnabled;
	}
	
	
	public long getExpires() {
		return expires;
	}
	public void setExpires(long expires) {
		this.expires = expires;
	}
	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + getUsername();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return getAuthorities('A');
		
	}
	
	
	/**
	 * Retrieves a collection of {@link GrantedAuthority} based on a numerical role
	 * @param role the numerical role
	 * @return a collection of {@link GrantedAuthority
	 */
	public Collection<? extends GrantedAuthority> getAuthorities(Character role) {
		List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(role));
		return authList;
	}
	
	/**
	 * Converts a numerical role to an equivalent list of roles
	 * @param role the numerical role
	 * @return list of roles as as a list of {@link String}
	 */
	public List<String> getRoles(Character role) {
		List<String> roles = new ArrayList<String>();
		
		if (role == 'A') {
			roles.add("GLACE_ADMIN");

			
		} else if (role == 'G') {
			roles.add("GLACE_USER");
		}
		
		return roles;
	}
	
	/**
	 * Wraps {@link String} roles to {@link SimpleGrantedAuthority} objects
	 * @param roles {@link String} of roles
	 * @return list of granted authorities
	 */
	public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}

	

}