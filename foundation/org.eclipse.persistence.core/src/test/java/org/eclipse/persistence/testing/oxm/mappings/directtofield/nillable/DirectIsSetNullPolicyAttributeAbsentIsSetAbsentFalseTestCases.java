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
package org.eclipse.persistence.testing.oxm.mappings.directtofield.nillable;

import org.eclipse.persistence.oxm.mappings.nullpolicy.AbstractNullPolicy;
import org.eclipse.persistence.oxm.mappings.nullpolicy.IsSetNullPolicy;
import org.eclipse.persistence.oxm.mappings.nullpolicy.XMLNullRepresentationType;

import org.eclipse.persistence.oxm.mappings.XMLDirectMapping;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.testing.oxm.mappings.XMLWithJSONMappingTestCases;

public class DirectIsSetNullPolicyAttributeAbsentIsSetAbsentFalseTestCases extends XMLWithJSONMappingTestCases{
    // TC 0-3 UC 0-5 to 0-8, 2-1
    private final static String XML_RESOURCE = //
    "org/eclipse/persistence/testing/oxm/mappings/directtofield/nillable/DirectIsSetNullPolicyAttributeAbsentIsSetAbsentFalse.xml";
    private final static String JSON_RESOURCE = "org/eclipse/persistence/testing/oxm/mappings/directtofield/nillable/DirectIsSetNullPolicyAttributeAbsentIsSetAbsentFalse.json";

    public DirectIsSetNullPolicyAttributeAbsentIsSetAbsentFalseTestCases(String name) throws Exception {
        super(name);
        setControlDocument(XML_RESOURCE);
        setControlJSON(JSON_RESOURCE);
        IsSetNullPolicy aNullPolicy = new IsSetNullPolicy();
        // Alter unmarshal policy state
        aNullPolicy.setNullRepresentedByEmptyNode(true); // No effect
        aNullPolicy.setNullRepresentedByXsiNil(false);  // No effect
        // Alter marshal policy state
        aNullPolicy.setMarshalNullRepresentation(XMLNullRepresentationType.ABSENT_NODE);
        aNullPolicy.setIsSetMethodName("isSetFirstName");
        Project aProject = new DirectNodeNullPolicyProject(false);
        XMLDirectMapping aMapping = (XMLDirectMapping)aProject.getDescriptor(Employee.class)//
        .getMappingForAttributeName("firstName");
        aMapping.setNullPolicy(aNullPolicy);
        setProject(aProject);
    }

    @Override
    protected Object getControlObject() {
        Employee anEmployee = new Employee();
        anEmployee.setId(123);
        // isSet = false if we do not perform a set
        //anEmployee.setFirstName(null);
        anEmployee.setLastName("Doe");
        return anEmployee;
    }
}
