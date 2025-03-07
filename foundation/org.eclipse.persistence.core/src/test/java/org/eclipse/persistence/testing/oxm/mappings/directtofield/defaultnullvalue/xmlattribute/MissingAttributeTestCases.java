/*
 * Copyright (c) 1998, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 */

// Contributors:
//     Oracle - initial API and implementation from Oracle TopLink
package org.eclipse.persistence.testing.oxm.mappings.directtofield.defaultnullvalue.xmlattribute;

import junit.textui.TestRunner;

import org.eclipse.persistence.testing.oxm.OXTestCase.Platform;
import org.eclipse.persistence.testing.oxm.mappings.XMLMappingTestCases;
import org.eclipse.persistence.testing.oxm.mappings.directtofield.defaultnullvalue.Employee;

public class MissingAttributeTestCases extends XMLMappingTestCases {
    private final static String XML_RESOURCE = "org/eclipse/persistence/testing/oxm/mappings/directtofield/defaultnullvalue/xmlattribute/MissingAttribute.xml";

    public MissingAttributeTestCases(String name) throws Exception {
        super(name);
        setControlDocument(XML_RESOURCE);
        setProject(new DefaultNullValueAttributeProject());
    }

    @Override
    protected Object getControlObject() {
          Employee employee = new Employee();
          // We currently have different behavior when using XMLProjectReader
          if(platform==Platform.DOM && metadata==Metadata.XML_ECLIPSELINK) {
              employee.setID(DefaultNullValueAttributeProject.CONTROL_ID);
              // See bug#209398 - current behavior for deployment xml is to not preserve the nullValue of ""
              //employee.setFirstName(DefaultNullValueAttributeProject.CONTROL_FIRSTNAME);
          } else {
              employee.setID(DefaultNullValueAttributeProject.CONTROL_ID);
              // See bug#209398 - current behavior for deployment xml is to not preserve the nullValue of ""
              employee.setFirstName(DefaultNullValueAttributeProject.CONTROL_FIRSTNAME);
          }

          return employee;
        }


     public static void main(String[] args) {
        String[] arguments = { "-c", "org.eclipse.persistence.testing.oxm.mappings.directtofield.defaultnullvalue.xmlattribute.MissingAttributeTestCases" };
        TestRunner.main(arguments);
    }
}
