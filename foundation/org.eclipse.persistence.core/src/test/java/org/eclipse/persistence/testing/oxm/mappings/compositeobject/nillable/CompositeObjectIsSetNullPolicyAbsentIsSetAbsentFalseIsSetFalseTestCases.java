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
package org.eclipse.persistence.testing.oxm.mappings.compositeobject.nillable;

import org.eclipse.persistence.oxm.NamespaceResolver;
import org.eclipse.persistence.oxm.XMLConstants;
import org.eclipse.persistence.oxm.XMLDescriptor;
import org.eclipse.persistence.oxm.mappings.nullpolicy.AbstractNullPolicy;
import org.eclipse.persistence.oxm.mappings.nullpolicy.IsSetNullPolicy;
import org.eclipse.persistence.oxm.mappings.nullpolicy.XMLNullRepresentationType;

import org.eclipse.persistence.oxm.mappings.XMLCompositeObjectMapping;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.testing.oxm.mappings.XMLWithJSONMappingTestCases;

/**
 * UC 8-9 and 11.3
 */
public class CompositeObjectIsSetNullPolicyAbsentIsSetAbsentFalseIsSetFalseTestCases extends XMLWithJSONMappingTestCases {
    private final static String XML_RESOURCE = //
        "org/eclipse/persistence/testing/oxm/mappings/compositeobject/nillable/CompositeObjectIsSetNullPolicyAbsentIsSetAbsentFalseIsSetFalse.xml";
    private final static String JSON_RESOURCE = //
        "org/eclipse/persistence/testing/oxm/mappings/compositeobject/nillable/CompositeObjectIsSetNullPolicyAbsentIsSetAbsentFalseIsSetFalse.json";

    public CompositeObjectIsSetNullPolicyAbsentIsSetAbsentFalseIsSetFalseTestCases(String name) throws Exception {
        super(name);
        setControlDocument(XML_RESOURCE);
        setControlJSON(JSON_RESOURCE);

        IsSetNullPolicy aNullPolicy = new IsSetNullPolicy();
        // Alter unmarshal policy state
        aNullPolicy.setNullRepresentedByEmptyNode(false); // No effect
        aNullPolicy.setNullRepresentedByXsiNil(false); // No effect
        // Alter marshal policy state
        aNullPolicy.setMarshalNullRepresentation(XMLNullRepresentationType.ABSENT_NODE);

        aNullPolicy.setIsSetMethodName("isSetManager");
        Project aProject = new CompositeObjectNodeNullPolicyProject(true);
        XMLDescriptor teamDescriptor = (XMLDescriptor) aProject.getDescriptor(Team.class);
        //NamespaceResolver namespaceResolver = new NamespaceResolver();
        //namespaceResolver.put(XMLConstants.SCHEMA_INSTANCE_PREFIX, javax.xml.XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
        //teamDescriptor.setNamespaceResolver(namespaceResolver);
        XMLCompositeObjectMapping aMapping = (XMLCompositeObjectMapping) teamDescriptor.getMappingForAttributeName("manager");
        aMapping.setNullPolicy(aNullPolicy);
        setProject(aProject);
    }

    @Override
    public Object getWriteControlObject() {
        Team aTeam = new Team();
        aTeam.setId(123);
        aTeam.setName("Eng");
        //aTeam.setManager(null);
        return aTeam;
    }

    @Override
    protected Object getControlObject() {
        Team aTeam = new Team();
        aTeam.setId(123);
        aTeam.setName("Eng");
        //aTeam.setManager(null);
        return aTeam;
    }
}
