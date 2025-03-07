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

package org.eclipse.persistence.testing.oxm.mappings.xmlfragmentcollection;

import java.io.InputStream;
import java.util.ArrayList;

import org.eclipse.persistence.oxm.NamespaceResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLFragmentCollectionElementDiffURITestCases extends XMLFragmentCollectionNSTestCases {

    private final static String XML_RESOURCE_DIFF_URI = "org/eclipse/persistence/testing/oxm/mappings/xmlfragmentcollection/employee_element_ns_different_uri.xml";
    private final static String XML_SUB_ELEMENT = "org/eclipse/persistence/testing/oxm/mappings/xmlfragmentcollection/sub_element_ns.xml";

    public XMLFragmentCollectionElementDiffURITestCases(String name) throws Exception {
        super(name);
        NamespaceResolver nsresolver = new NamespaceResolver();
        nsresolver.put("ns1", "http://www.example.com/some-other-uri");
        setProject(new XMLFragmentCollectionElementProject(nsresolver));
        setControlDocument(XML_RESOURCE_DIFF_URI);
    }

    @Override
    protected Object getControlObject() {
        Employee employee = new Employee();
        employee.firstName = "Jane";
        employee.lastName = "Doe";

        try {
            InputStream inputStream = ClassLoader.getSystemResourceAsStream(XML_SUB_ELEMENT);
            Document xdoc = parser.parse(inputStream);
            removeEmptyTextNodes(xdoc);
            inputStream.close();
            employee.xmlnodes = new ArrayList<Node>();
            NodeList xmlnodes = xdoc.getElementsByTagName("xml-node");
            for (int i = 0; i < xmlnodes.getLength(); i++) {
                employee.xmlnodes.add(xmlnodes.item(i));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return employee;
    }
}
