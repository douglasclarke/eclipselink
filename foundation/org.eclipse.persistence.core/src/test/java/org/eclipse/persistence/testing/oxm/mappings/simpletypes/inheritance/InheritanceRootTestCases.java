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
package org.eclipse.persistence.testing.oxm.mappings.simpletypes.inheritance;

// JDK imports
import java.io.InputStream;

// TopLink imports
import org.eclipse.persistence.oxm.XMLMarshaller;
import org.eclipse.persistence.oxm.XMLRoot;
import org.eclipse.persistence.exceptions.XMLMarshalException;
import org.eclipse.persistence.testing.oxm.mappings.XMLMappingTestCases;

public class InheritanceRootTestCases extends XMLMappingTestCases {
    private final static String XML_RESOURCE = "org/eclipse/persistence/testing/oxm/mappings/simpletypes/inheritance/InheritanceRootElementTest.xml";
    private final static String CONTROL_EMPLOYEE_FIRST_NAME = "Jane";
    private final static String CONTROL_EMPLOYEE_LAST_NAME = "Doe";
    private final static String CONTROL_EMPLOYEE_TITLE = "Developer";

    private XMLMarshaller xmlMarshaller;

    public InheritanceRootTestCases(String name) throws Exception {
        super(name);
        setControlDocument(XML_RESOURCE);
        setProject(new EmployeeProject());
    }

    @Override
    protected Object getControlObject() {
        XMLRoot theRoot = new XMLRoot();
        theRoot.setLocalName("person");
        Employee employee = new Employee();
        employee.setFirstName(CONTROL_EMPLOYEE_FIRST_NAME);
        employee.setLastName(CONTROL_EMPLOYEE_LAST_NAME);
        employee.setJobTitle(CONTROL_EMPLOYEE_TITLE);

        theRoot.setObject(employee);

        return theRoot;
    }
    @Override
    public Object getReadControlObject() {
        Employee employee = new Employee();
        employee.setFirstName(CONTROL_EMPLOYEE_FIRST_NAME);
        employee.setLastName(CONTROL_EMPLOYEE_LAST_NAME);
        employee.setJobTitle(CONTROL_EMPLOYEE_TITLE);
        return employee;
    }

}
