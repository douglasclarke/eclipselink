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
package org.eclipse.persistence.testing.oxm.mappings.onetomany.keyonsource.eis.map;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Vector;

import org.eclipse.persistence.eis.interactions.XQueryInteraction;
import org.eclipse.persistence.internal.eis.adapters.xmlfile.XMLFileInteractionSpec;
import org.eclipse.persistence.testing.oxm.mappings.EISMappingTestCases;

public class  MapTestCases extends EISMappingTestCases {

  private final static String XML_RESOURCE = "org/eclipse/persistence/testing/oxm/mappings/onetomany/keyonsource/eis/map/writing/employee_control.xml";
  private final static String XML_TEST_RESOURCE="org/eclipse/persistence/testing/oxm/mappings/onetomany/keyonsource/eis/map/writing/employee.xml";
  private final static String CONTROL_EMPLOYEE1_NAME = "Jane";

  private final static long CONTROL_PROJECT1_ID = 1;
  private final static String CONTROL_PROJECT1_NAME = "Project1";

  private final static long CONTROL_PROJECT2_ID = 2;
  private final static String CONTROL_PROJECT2_NAME = "Project2";

  private final static long CONTROL_PROJECT3_ID = 3;
  private final static String CONTROL_PROJECT3_NAME = "Project3";

  public MapTestCases(String name) throws Exception {
    super(name);
    setControlDocument(XML_RESOURCE);
        setProject(new MapProject());
  }

  @Override
  protected Object getControlObject() {

    Project project1 = new Project();
    project1.setId(CONTROL_PROJECT1_ID);
    project1.setName(CONTROL_PROJECT1_NAME);
        project1.setType(Project.PRIORITY_1);

    Project project2 = new Project();
    project2.setId(CONTROL_PROJECT2_ID);
    project2.setName(CONTROL_PROJECT2_NAME);
    project2.setType(Project.PRIORITY_2);

    Project project3 = new Project();
    project3.setId(CONTROL_PROJECT3_ID);
    project3.setName(CONTROL_PROJECT3_NAME);
    project3.setType(Project.PRIORITY_3);

    Employee employee1 = new Employee();
    employee1.setFirstName(CONTROL_EMPLOYEE1_NAME);
    employee1.addProject(project1);
        employee1.addProject(project3);

        ArrayList objects = new ArrayList();
    objects.add(employee1);
      objects.add(project1);
    objects.add(project2);
    objects.add(project3);

    return objects;
  }

  @Override
  protected ArrayList getRootClasses()
  {
    ArrayList classes = new ArrayList();
    classes.add(Employee.class);
    classes.add(Project.class);
    return classes;
  }

    @Override
    protected Class<?> getSourceClass(){
        return Employee.class;
    }

  @Override
  protected String getTestDocument()
  {
    return XML_TEST_RESOURCE;
  }

  @Override
  protected void createTables()
  {
    // Drop tables
        XQueryInteraction interaction = new XQueryInteraction();
         XMLFileInteractionSpec spec = new XMLFileInteractionSpec();

        interaction = new XQueryInteraction();
        interaction.setFunctionName("drop-PROJECT");
        spec = new XMLFileInteractionSpec();
        spec.setFileName("project.xml");
        spec.setInteractionType(XMLFileInteractionSpec.DELETE);
        interaction.setInteractionSpec(spec);
        session.executeNonSelectingCall(interaction);

        interaction = new XQueryInteraction();
        interaction.setFunctionName("drop-EMPLOYEE");
        spec = new XMLFileInteractionSpec();
        spec.setFileName("employee.xml");
        spec.setInteractionType(XMLFileInteractionSpec.DELETE);
        interaction.setInteractionSpec(spec);
        session.executeNonSelectingCall(interaction);
  }
}
