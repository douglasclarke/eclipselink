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
package org.eclipse.persistence.testing.oxm.inheritance.typetests.any;

import org.eclipse.persistence.testing.oxm.inheritance.typetests.ContactMethod;

import org.eclipse.persistence.testing.oxm.mappings.XMLWithJSONMappingTestCases;

public class ContactAsAnyNestedTestCases extends XMLWithJSONMappingTestCases {
    private static final String READ_DOC = "org/eclipse/persistence/testing/oxm/inheritance/typetests/customer_with_contact_noxsi.xml";
    private static final String JSON_DOC = "org/eclipse/persistence/testing/oxm/inheritance/typetests/customer_with_contact_noxsi.json";

    public ContactAsAnyNestedTestCases(String name) throws Exception {
        super(name);
        setProject(new AnyTypeProject());
        setControlDocument(READ_DOC);
        setControlJSON(JSON_DOC);
    }

    @Override
    public Object getControlObject() {
        Customer cust = new Customer();
        ContactMethod cm = new ContactMethod();
        cm.setId("123");
        cust.setContact(cm);
        return cust;
    }

    public static void main(String[] args) {
        String[] arguments = { "-c", "org.eclipse.persistence.testing.oxm.inheritance.typetests.any.ContactAsAnyNestedTestCases" };
        junit.textui.TestRunner.main(arguments);
    }
}
