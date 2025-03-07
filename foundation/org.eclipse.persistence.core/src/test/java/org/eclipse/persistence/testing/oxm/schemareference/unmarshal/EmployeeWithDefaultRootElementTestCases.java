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
package org.eclipse.persistence.testing.oxm.schemareference.unmarshal;

import junit.framework.TestCase;
import junit.textui.TestRunner;
import org.eclipse.persistence.oxm.XMLRoot;
import org.eclipse.persistence.testing.oxm.mappings.XMLMappingTestCases;

public class EmployeeWithDefaultRootElementTestCases extends XMLMappingTestCases {
    private final static String XML_RESOURCE = "org/eclipse/persistence/testing/oxm/schemareference/unmarshal/EmployeeWithoutDefaultRootElement.xml";
    private final static String CONTROL_EMPLOYEE_NAME = "Jane Doe";
    //private final static String CONTROL_ELEMENT_NAME = "ns:NOT-DEFAULT-ROOT";
    private final static String CONTROL_ELEMENT_NAME = "NOT-DEFAULT-ROOT";
    private final static String CONTROL_NAMESPACE_URI = "urn:test";

    public EmployeeWithDefaultRootElementTestCases(String name) throws Exception {
        super(name);
        setControlDocument(XML_RESOURCE);
        setProject(new EmployeeWithDefaultRootElementProject());
    }

    public static void main(String[] args) {
        String[] arguments = { "-c", "org.eclipse.persistence.testing.oxm.schemareference.unmarshal.EmployeeWithDefaultRootElementTestCases" };
        TestRunner.main(arguments);
    }

    @Override
    protected Object getControlObject() {
        Employee employee = new Employee();
        employee.setName(CONTROL_EMPLOYEE_NAME);

        XMLRoot xmlRoot = new XMLRoot();
        xmlRoot.setLocalName(CONTROL_ELEMENT_NAME);
        xmlRoot.setNamespaceURI(CONTROL_NAMESPACE_URI);
        xmlRoot.setObject(employee);
        return xmlRoot;
    }

    // THIS TEST DOES NOT APPLY
    @Override
    public void testObjectToXMLDocument() throws Exception {
    }

    // THIS TEST DOES NOT APPLY
    @Override
    public void testObjectToXMLStringWriter() throws Exception {
    }

    // THIS TEST DOES NOT APPLY
    @Override
    public void testObjectToContentHandler() throws Exception {
    }

    @Override
    public void xmlToObjectTest(Object testObject) throws Exception {
        log("\n**testXMLDocumentToObject**");
        log("Expected:");
        log(getReadControlObject().toString());
        log("Actual:");
        log(testObject.toString());

        XMLRoot controlObj = (XMLRoot) getReadControlObject();
        XMLRoot testObj = (XMLRoot) testObject;

        assertEquals(controlObj.getLocalName(), testObj.getLocalName());
        assertEquals(controlObj.getNamespaceURI(), testObj.getNamespaceURI());
        assertEquals(controlObj.getObject(), testObj.getObject());
    }
}
