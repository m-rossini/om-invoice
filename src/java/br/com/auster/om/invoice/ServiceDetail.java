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
 * Created on Feb 1, 2005
 */
package br.com.auster.om.invoice;

import java.util.Date;

/**
 * @hibernate.class
 *              table="INV_SERVICE_DETAIL"
 *
 *              
 * <p><b>Title:</b> ServiceDetail</p>
 * <p><b>Description:</b> </p>
 * <p><b>Copyright:</b> Copyright (c) 2004</p>
 * <p><b>Company:</b> Auster Solutions</p>
 *
 * @author etirelli
 * @version $Id: ServiceDetail.java 382 2007-02-07 22:55:51Z mtengelm $
 */

public class ServiceDetail extends SectionDetail {
	private static final long serialVersionUID = -3035087169777546065L;
	
	/** Reference dates */
	private Date startDate;
	private String originalStartDate;
	private Date endDate;
	private String originalEndDate;
	/** Pro-Rate indicator */
	private boolean proRate;
	
	
	
	
	/**
	 * @hibernate.property
	 *            column="END_DATE"
	 *            type="date"
	 *            not-null="false"
	 */
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public boolean isEndDateNull() {
		return ( (this.endDate == null) || ("".equals(this.originalEndDate) || null == this.originalEndDate));
	}
	/**
	 * @hibernate.property
	 *            column="SRC_END_DATE"
	 *            type="string"
	 *            length="30"
	 *            not-null="false"
	 *            
	 * This attributes handles the unformatted version of the
	 * 	{@link #endDate}  
	 */
	public String getOriginalEndDate() {
		return originalEndDate;
	}
	public void setOriginalEndDate(String _originalString) {
		this.originalEndDate = _originalString;
	}	
	
	/**
	 * @hibernate.property
	 *            column="PRORATE_FLAG"
	 *            type="boolean"
	 *            not-null="false"
	 */
	public boolean isProRate() {
		return proRate;
	}
	public void setProRate(boolean proRate) {
		this.proRate = proRate;
	}
	
	/**
	 * @hibernate.property
	 *            column="START_DATE"
	 *            type="date"
	 *            not-null="false"
	 */
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}	
	public boolean isStartDateNull() {
		return ( (this.startDate == null) || ("".equals(this.originalStartDate) || null == this.originalStartDate) );
	}
	
	protected boolean checkValidNulls() {
		//IF end date is null and start date is not null (Real situation that can happen) this method will return true.
		if (this.isEndDateNull() && !this.isStartDateNull()) {
			return true;
		}
		//IF end date is not null and start date is  null (Real situation that can happen) this method will return true.
		if (!this.isEndDateNull() && this.isStartDateNull()) {
			return true;
		}		
		return false;
	}
	
	public boolean isEndDateBeforeStartDate() {
		if (checkValidNulls()) {
			return true;
		}
		
		return ( (!this.isStartDateNull()) && (!this.isEndDateNull()) && this.endDate.before(this.startDate) );
	}
	
	public boolean isStarDateBeforeEndDate() {
		//IF end date is null and start date is not null (Real situation that can happen) this method will return true.
		if (this.isEndDateNull() && !this.isStartDateNull()) {
			return true;
		}
		//IF end date is not null and start date is  null (Real situation that can happen) this method will return true.
		if (!this.isEndDateNull() && this.isStartDateNull()) {
			return true;
		}		
		return ( (!this.isStartDateNull()) && (!this.isEndDateNull()) && this.startDate.before(this.endDate) );
	}
	/**
	 * @hibernate.property
	 *            column="SRC_START_DATE"
	 *            type="string"
	 *            length="30"
	 *            not-null="false"
	 *            
	 * This attributes handles the unformatted version of the
	 * 	{@link #startDate}  
	 */
	public String getOriginalStartDate() {
		return originalStartDate;
	}
	public void setOriginalStartDate(String _originalString) {
		this.originalStartDate = _originalString;
	}	
}
