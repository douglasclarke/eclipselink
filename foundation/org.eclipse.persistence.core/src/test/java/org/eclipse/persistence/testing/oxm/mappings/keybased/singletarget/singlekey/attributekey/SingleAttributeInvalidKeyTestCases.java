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
package org.eclipse.persistence.testing.oxm.mappings.keybased.singletarget.singlekey.attributekey;

import java.io.InputStream;
import java.util.ArrayList;
import org.eclipse.persistence.testing.oxm.mappings.keybased.*;
import org.eclipse.persistence.testing.oxm.mappings.keybased.singletarget.Employee;
import org.w3c.dom.Document;

public class SingleAttributeInvalidKeyTestCases extends KeyBasedMappingTestCases {
    private static final String XML_RESOURCE = "org/eclipse/persistence/testing/oxm/mappings/keybased/singletarget/singlekey/elementkey/instance-invalidvalue.xml";
    private static final String XML_WRITE_RESOURCE = "org/eclipse/persistence/testing/oxm/mappings/keybased/singletarget/singlekey/elementkey/writecontrolinstance-invalidvalue.xml";

    public SingleAttributeInvalidKeyTestCases(String name) throws Exception {
        super(name);
        setControlDocument(XML_RESOURCE);
        setProject(new SingleAttributeKeyProject());
    }

    @Override
    protected Object getControlObject() {
        Employee employee = new Employee();
        employee.id = "222";
        employee.name = "Joe Smith";
        Root root = new Root();
        root.employee = employee;
        return root;
    }

    @Override
    protected Document getWriteControlDocument() throws Exception {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(XML_WRITE_RESOURCE);
        Document doc = parser.parse(inputStream);
        removeEmptyTextNodes(doc);
        inputStream.close();
        return doc;
    }

    @Override
    public Object getWriteControlObject() {
        ArrayList rootAddresses = new ArrayList();
        Address address = new Address();
        address.id = "199";
        address.street = "Some Other St.";
        address.city = "Anyothertown";
        address.country = "Canada";
        address.zip = "X0X0X0";
        rootAddresses.add(address);
        address = new Address();
        address.id = "99";
        address.street = "Some St.";
        address.city = "Anytown";
        address.country = "Canada";
        address.zip = "X0X0X0";
        rootAddresses.add(address);
        address = new Address();
        address.id = "11199";
        address.street = "Another St.";
        address.city = "Anytown";
        address.country = "Canada";
        address.zip = "Y0Y0Y0";
        rootAddresses.add(address);
        address = new Address();
        address.id = "1199";
        address.street = "Some St.";
        address.city = "Sometown";
        address.country = "Canada";
        address.zip = "X0X0X0";
        rootAddresses.add(address);
        Employee employee = new Employee();
        employee.id = "222";
        employee.name = "Joe Smith";
        Root root = new Root();
        root.employee = employee;
        root.addresses = rootAddresses;
        return root;
    }
}
