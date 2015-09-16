/*
 * Copyright (c) 2004 Auster Solutions. All Rights Reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Created on Jan 20, 2005
 */
package br.com.auster.om.invoice;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @hibernate.class
 *              table="INV_ACCOUNT"
 *              
 *              
 * <p><b>Title:</b> Account</p>
 * <p><b>Description:</b> A class to represent billing accounts</p>
 * <p><b>Copyright:</b> Copyright (c) 2004</p>
 * <p><b>Company:</b> Auster Solutions</p>
 *
 * @author etirelli
 * @version $Id: Account.java 385 2007-02-08 20:17:59Z framos $
 */
public class Account extends InvoiceModelObject {
	private static final long serialVersionUID = -8786326200990343679L;
	
	private String carrierCode = null;
	private String carrierState = null;
	private String carrierName = null;
	private String accountNumber = null;
	private String accountName = null;
    private String accountType = null;
	private String accountState = null;
	
	// Map<String identType, Identity identity>
	private Map identities;
	// Map<String addressType, Address address>
	private Map addresses;
	// Map<Date referenceDate, Invoice invoice>
	private Map invoices;
	
	//Due to need to override account state....
	private String changedAccountState = null;
	private boolean accountStateModified = false;
	
	public String customerServiceArea = null;
	
	public Account() {
		identities = new HashMap();
		addresses = new HashMap();
		invoices = new HashMap();
	}
	
	/**
	 * @hibernate.property
	 *            column="ACCOUNT_NAME"
	 *            type="string"
	 *            length="256"
	 *            not-null="false"
	 */
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	/**
	 * @hibernate.property
	 *            column="ACCOUNT_NUMBER"
	 *            type="string"
	 *            length="64"
	 *            not-null="true"
	 */
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
  /**
   * @hibernate.property
   *            column="ACCOUNT_TYPE"
   *            type="string"
   *            length="2"
   *            not-null="false"
   */
  public String getAccountType() {
    return accountType;
  }
  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }
  
	/**
   * @hibernate.map
   *            lazy="true"
   *            cascade="delete"
   *            
   * @hibernate.collection-key
   *            column="account_id"
   *            
   * @hibernate.collection-index
   *            column="ADDRESS_TYPE"
   *            type="string"
   *            length="32"
   *            
   * @hibernate.collection-one-to-many
   *            class="br.com.auster.om.invoice.Address"
   * 
	 * @return Returns a Synchronized Map<String addressType, Address address> with all addresses.
	 */
  public Map getAddresses() {
    return addresses;
  }
  public void setAddresses(Map addresses) {
    this.addresses = addresses;
  }
  
  /**
   * @param addressType
   * @return
   */
	public Address getAddress(String addressType) {
		return (Address) addresses.get(addressType);
	}
	public void addAddress(Address address) {
		addresses.put(address.getAddressType(), address);
	}
	
	/**
   * @hibernate.map
   *            lazy="true"
   *            cascade="delete"
   *            
   * @hibernate.collection-key
   *            column="account_id"
   *            
   * @hibernate.collection-index
   *            column="IDENTITY_TYPE"
   *            type="string"
   *            length="32"
   * 
   * @hibernate.collection-one-to-many
   *            class="br.com.auster.om.invoice.Identity"
   * 
	 * @return Returns a Synchronized Map<String identType, Identity identity> with all identities.
	 */
  public Map getIdentities() {
    return identities;
  }
  public void setIdentities(Map identities) {
    this.identities = identities;
  }

  /**
   * @param addressType
   * @return
   */
	public Identity getIdentity(String identityType) {
		return (Identity) identities.get(identityType);
	}
	public void addIdentity(Identity identity) {
		identities.put(identity.getIdentityType(), identity);
	}
	
	/**
   * @hibernate.map
   *            lazy="true"
   *            cascade="delete"
   *            inverse="true"
   *            
   * @hibernate.collection-key
   *            column="account_id"
   *            
   * @hibernate.collection-index
   *            column="DUE_DATE"
   *            type="date"
   * 
   * @hibernate.collection-one-to-many
   *            class="br.com.auster.om.invoice.Invoice"
   * 
	 * @return Returns the invoices.
	 */
	public Map getInvoices() {
		return invoices;
	}
	
	public Collection getInvoiceList() {
		return invoices.values();
	}
	
	public Collection getInvoiceReferenceDateList() {
		return invoices.keySet();
	}
	
  public void setInvoices(Map invoices) {
    this.invoices = invoices;
  }
  public Invoice getInvoice(Date referenceDate) {
		return (Invoice) invoices.get(referenceDate);
	}
	public void addInvoice(Invoice invoice) {
		invoices.put(invoice.getDueDate(), invoice);
		invoice.setAccount(this);
	}
	
	/**
	 * @hibernate.property
	 *            column="CARRIER_CODE"
	 *            type="string"
	 *            length="64"
	 *            not-null="false"
	 */
	public String getCarrierCode() {
		return carrierCode;
	}
	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}
	
	/**
	 * @hibernate.property
	 *            column="CARRIER_STATE"
	 *            type="string"
	 *            length="10"
	 *            not-null="false"
	 */
	public String getCarrierState() {
		return carrierState;
	}
	public void setCarrierState(String carrierState) {
		this.carrierState = carrierState;
	}
	
	public String getCarrierName() {
		return carrierName;
	}
	public void setCarrierName(String _carrierName) {
		this.carrierName = _carrierName;
	}
	
	/**
	 * @hibernate.property
	 *            column="ACCOUNT_STATE"
	 *            type="string"
	 *            length="10"
	 *            not-null="false"
	 */
	public String getAccountState() {
		String ret = (this.isAccountStateModified()) ? this.getChangedAccountState() : this.accountState;
		ret = ( ret == null || ret.equals("")) ? this.accountState : ret;
		return ret;
	}
	/**
	 * @param accountState The accountState to set.
	 */
	public void setAccountState(String accountState) {
		this.accountState = accountState;
	}
	
	
	public String toString() {
		return this.getClass().getName() + "=" + this.accountNumber;
	}
	
	/**
	 * Compares 2 user objects
	 * 
	 * @param o object to compare
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		int ret = 0;
		if(o instanceof Account) {
			Account account = (Account) o;
			if((this.getAccountNumber() != null) &&
					(this.getCarrierCode() != null) &&
					(this.getCarrierState() != null) &&
					(account.getAccountNumber() != null) &&
					(account.getCarrierCode() != null) &&
					(account.getCarrierState() != null)) {
				ret = this.getCarrierCode().compareTo(account.getCarrierCode());
				if(ret == 0) {
					ret = this.getCarrierState().compareTo(account.getCarrierState());
				}
				if(ret == 0) {
					ret = this.getAccountNumber().compareTo(account.getAccountNumber());
				}
			}
		} else {
			throw new ClassCastException("Can't compare an "+this.getClass().getName()+" to an "+o.getClass().getName());
		}
		return ret;
	}

	/***
	 * Returns the original Account State
	 * @return
	 */
	public String getOriginalAccountState() {
		return this.accountState;
	}
	/**
	 * See setAccountModified method
   * @return the accountStateModified. 
   * 
   */
  public boolean isAccountStateModified() {
  	return this.accountStateModified;
  }

	/**
	 * This method is intended to be used by then part of rules.
	 * If we need to set account state differentlu from the original file,
	 * one must call this method with true.
   * @param accountStateModified the accountStateModified to set
   */
  public void setAccountStateModified(boolean accountStateModified) {
  	this.accountStateModified = accountStateModified;
  }

	/**
	 * This is the changed account state by rule
   * @return the changedAccountState
   */
  public String getChangedAccountState() {
  	return this.changedAccountState;
  }

	/**
   * @param changedAccountState the changedAccountState to set
   */
  public void setChangedAccountState(String changedAccountState) {
  	this.changedAccountState = changedAccountState;
  }

	/**
   * @return the customerServiceArea
   */
  public String getCustomerServiceArea() {
  	return this.customerServiceArea;
  }

	/**
   * @param customerServiceArea the customerServiceArea to set
   */
  public void setCustomerServiceArea(String customerServiceArea) {
  	this.customerServiceArea = customerServiceArea;
  }
	
}
