/*
 * Copyright (c) 2013, 2021 Oracle and/or its affiliates. All rights reserved.
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
//     Blaise Doughan - 2.4.2 - initial implementation
package org.eclipse.persistence.testing.jaxb.json.wrapper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.testing.jaxb.json.JSONMarshalUnmarshalTestCases;

import junit.framework.TestCase;

public class WrapperNameEmptyPresentTestCases extends JSONMarshalUnmarshalTestCases {

    private static final String JSON = "org/eclipse/persistence/testing/jaxb/json/wrapper/WrapperNameEmptyPresent.json";

    public WrapperNameEmptyPresentTestCases(String name) throws Exception {
        super(name);
        setClasses(new Class<?>[] {Company.class});
        setControlJSON(JSON);
    }

    @Override
    public JAXBElement<Company> getControlObject() {
        Company company = new Company();

        Employee employee1 = new Employee();
        company.employees.add(employee1);

        Employee employee2 = new Employee();
        company.employees.add(employee2);

        return new JAXBElement<Company>(new QName(""), Company.class, company);
    }

    @Override
    public Class<?> getUnmarshalClass() {
        return Company.class;
    }

    @Override
    public Map getProperties() {
        Map<String, Object> properties = new HashMap<String, Object>(3);
        properties.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
        properties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
        properties.put(JAXBContextProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);
        return properties;
    }

}
