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
 * Created on Feb 4, 2005
 */
package br.com.auster.om.invoice.rules;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import br.com.auster.common.rules.RulesEngineProcessor;
import br.com.auster.om.invoice.Account;
import br.com.auster.om.invoice.BarCode;
import br.com.auster.om.invoice.Invoice;
import br.com.auster.om.invoice.InvoiceModelObject;
import br.com.auster.om.invoice.Section;
import br.com.auster.om.invoice.SectionDetail;
import br.com.auster.om.invoice.UsageDetail;

/**
 * <p>
 * <b>Title:</b> InvoiceAssertionWorker
 * </p>
 * <p>
 * <b>Description:</b>
 * </p>
 * <p>
 * <b>Copyright:</b> Copyright (c) 2004
 * </p>
 * <p>
 * <b>Company:</b> Auster Solutions
 * </p>
 * 
 * @author etirelli
 * @version $Id: InvoiceAssertionWorker.java 367 2007-01-29 15:27:33Z framos $
 */

public class InvoiceAssertionWorker {
	private static Logger	log	= Logger.getLogger(InvoiceAssertionWorker.class);

	public static void assertObjectsByReflection(RulesEngineProcessor engine,
	    InvoiceModelObject object) {
		try {
			log.debug("Asserting =>[" + object + "] of the Type " + object.getClass().getName());
			engine.assertFact(object);
			Method[] methods = object.getClass().getMethods();
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().startsWith("get")
				    && ((Collection.class.isAssignableFrom(methods[i].getReturnType())) || (Map.class
				        .isAssignableFrom(methods[i].getReturnType())))) {
					Collection col = null;
					if (methods[i].getReturnType().isAssignableFrom(Map.class)) {
						Map map = (Map) methods[i].invoke(object, (Object[]) null);
						if (map != null) {
							col = map.values();
						}
					} else {
						col = (Collection) methods[i].invoke(object, (Object[]) null);
					}
					if (col != null) {
						for (Iterator it = col.iterator(); it.hasNext();) {
							Object entry = it.next();
							if (entry instanceof InvoiceModelObject) {
								assertObjectsByReflection(engine, (InvoiceModelObject) entry);
							}
						}
					}
				} else if (methods[i].getName().startsWith("get")
				    && BarCode.class.isAssignableFrom(methods[i].getReturnType())) {
					engine.assertFact((InvoiceModelObject) methods[i].invoke(object, (Object[]) null));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void assertUsageObjects(RulesEngineProcessor engine, InvoiceModelObject object) {
		try {
			int counter = 0;
			if (object instanceof Account) {
				Account account = (Account) object;
				engine.assertFact(account);
				Collection invoices = account.getInvoices().values();
				for (Iterator invIt = invoices.iterator(); invIt.hasNext();) {
					Invoice inv = (Invoice) invIt.next();
					engine.assertFact(inv);
					for (Iterator secIt1 = inv.getSections().iterator(); secIt1.hasNext();) {
						Section topSection = (Section) secIt1.next();
						if ("300D".equals(topSection.getTag())) {
							for (Iterator secIt2 = topSection.getSubSections().iterator(); secIt2.hasNext();) {
								Section sbsSection = (Section) secIt2.next();
								if ("399D".equals(sbsSection.getTag())) {
									for (Iterator secIt3 = sbsSection.getSubSections().iterator(); secIt3.hasNext();) {
										Section usgSection = (Section) secIt3.next();
										for (Iterator usageIt = usgSection.getDetails().iterator(); usageIt.hasNext();) {
											SectionDetail detail = (SectionDetail) usageIt.next();
											if (detail instanceof UsageDetail) {
												engine.assertFact(detail);
												counter++;
											}
										}
									}
								}
							}
						}
					}
				}
				log.info("Total Asserted Usages:" + counter);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static long assertSimpleUsage(RulesEngineProcessor engine, InvoiceModelObject object) {
		long qtde = 0;

		try {
			if (object instanceof Account) {
				Account account = (Account) object;
				engine.assertFact(account);
				Collection invoices = account.getInvoices().values();
				for (Iterator invIt = invoices.iterator(); invIt.hasNext();) {
					Invoice inv = (Invoice) invIt.next();
					engine.assertFact(inv);
					for (Iterator itL1 = inv.getSections().iterator();itL1.hasNext();) {
						//L1 Sections are Section just below Invoice.
						Section l1Section = (Section) itL1.next();
						for (Iterator itL2=l1Section.getSubSections().iterator();itL2.hasNext();) {
							//L2 Section are Contract Info Sections
							Section l2Section = (Section) itL2.next();
							for (Iterator itL3=l2Section.getSubSections().iterator();itL3.hasNext();) {
								//L3 Are SubSections
								Section l3Section = (Section) itL3.next();
								for (Iterator itUsg=l3Section.getDetails().iterator();itUsg.hasNext();) {
									engine.assertFact( (UsageDetail)itUsg.next());
									qtde++;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("Total Asserted Usages:" + qtde);		
		return qtde;
	}
}
