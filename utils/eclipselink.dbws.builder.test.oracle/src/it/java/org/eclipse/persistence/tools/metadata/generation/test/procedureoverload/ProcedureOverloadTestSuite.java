/*
 * Copyright (c) 2011, 2022 Oracle and/or its affiliates. All rights reserved.
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
//     David McCann - July 2013 - Initial Implementation
package org.eclipse.persistence.tools.metadata.generation.test.procedureoverload;


import static org.eclipse.persistence.tools.metadata.generation.test.AllTests.DATABASE_DDL_CREATE_KEY;
import static org.eclipse.persistence.tools.metadata.generation.test.AllTests.DATABASE_DDL_DEBUG_KEY;
import static org.eclipse.persistence.tools.metadata.generation.test.AllTests.DATABASE_DDL_DROP_KEY;
import static org.eclipse.persistence.tools.metadata.generation.test.AllTests.DATABASE_USERNAME_KEY;
import static org.eclipse.persistence.tools.metadata.generation.test.AllTests.DEFAULT_DATABASE_DDL_CREATE;
import static org.eclipse.persistence.tools.metadata.generation.test.AllTests.DEFAULT_DATABASE_DDL_DEBUG;
import static org.eclipse.persistence.tools.metadata.generation.test.AllTests.DEFAULT_DATABASE_DDL_DROP;
import static org.eclipse.persistence.tools.metadata.generation.test.AllTests.DEFAULT_DATABASE_USERNAME;
import static org.eclipse.persistence.tools.metadata.generation.test.AllTests.DEFAULT_PACKAGE_NAME;
import static org.eclipse.persistence.tools.metadata.generation.test.AllTests.comparer;
import static org.eclipse.persistence.tools.metadata.generation.test.AllTests.conn;
import static org.eclipse.persistence.tools.metadata.generation.test.AllTests.databasePlatform;
import static org.eclipse.persistence.tools.metadata.generation.test.AllTests.documentToString;
import static org.eclipse.persistence.tools.metadata.generation.test.AllTests.removeEmptyTextNodes;
import static org.eclipse.persistence.tools.metadata.generation.test.AllTests.runDdl;
import static org.eclipse.persistence.tools.metadata.generation.test.AllTests.xmlParser;
//javase imports
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.persistence.internal.jpa.metadata.xml.XMLEntityMappings;
import org.eclipse.persistence.internal.jpa.metadata.xml.XMLEntityMappingsWriter;
import org.eclipse.persistence.tools.metadata.generation.JPAMetadataGenerator;
import org.eclipse.persistence.tools.metadata.generation.test.AllTests;
import org.eclipse.persistence.tools.oracleddl.metadata.PLSQLPackageType;
import org.eclipse.persistence.tools.oracleddl.metadata.ProcedureType;
import org.eclipse.persistence.tools.oracleddl.parser.ParseException;
import org.eclipse.persistence.tools.oracleddl.util.DatabaseTypeBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;


/**
 * Tests metadata generation from a ProcedureType.
 *
 */
public class ProcedureOverloadTestSuite {
    static final String CREATE_PACKAGEX_PACKAGE =
        "CREATE OR REPLACE PACKAGE PACKAGEX AS" +
            "\nPROCEDURE P1(T OUT VARCHAR);" +
            "\nPROCEDURE P1(T OUT VARCHAR, U IN VARCHAR);" +
            "\nPROCEDURE P1(T OUT VARCHAR, U IN VARCHAR, V IN OUT NUMERIC);" +
            "\nFUNCTION F1 RETURN VARCHAR;" +
            "\nFUNCTION F1(U IN VARCHAR) RETURN VARCHAR;" +
        "\nEND PACKAGEX;";

    static final String DROP_PACKAGE =
        "DROP PACKAGE PACKAGEX";

    static boolean ddlCreate = false;
    static boolean ddlDrop = false;
    static boolean ddlDebug = false;

    @SuppressWarnings("rawtypes")
    static List dbProcedures;
    static DatabaseTypeBuilder dbTypeBuilder;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @BeforeClass
    public static void setUp() throws ClassNotFoundException, SQLException {
        AllTests.setUp();

        String ddlCreateProp = System.getProperty(DATABASE_DDL_CREATE_KEY, DEFAULT_DATABASE_DDL_CREATE);
        if ("true".equalsIgnoreCase(ddlCreateProp)) {
            ddlCreate = true;
        }
        String ddlDropProp = System.getProperty(DATABASE_DDL_DROP_KEY, DEFAULT_DATABASE_DDL_DROP);
        if ("true".equalsIgnoreCase(ddlDropProp)) {
            ddlDrop = true;
        }
        String ddlDebugProp = System.getProperty(DATABASE_DDL_DEBUG_KEY, DEFAULT_DATABASE_DDL_DEBUG);
        if ("true".equalsIgnoreCase(ddlDebugProp)) {
            ddlDebug = true;
        }
        if (ddlCreate) {
            runDdl(conn, CREATE_PACKAGEX_PACKAGE, ddlDebug);
        }
        String schema = System.getProperty(DATABASE_USERNAME_KEY, DEFAULT_DATABASE_USERNAME);

        List<String> procedurePatterns = new ArrayList<String>();
        procedurePatterns.add("P1");
        procedurePatterns.add("P1_1");
        procedurePatterns.add("P1_2");
        procedurePatterns.add("F1");
        procedurePatterns.add("F1_1");

        // use DatabaseTypeBuilder to generate a list of PackageTypes
        dbTypeBuilder = new DatabaseTypeBuilder();
        dbProcedures = new ArrayList();
        try {
            // process the package
            List<PLSQLPackageType> packages = dbTypeBuilder.buildPackages(conn, schema, "PACKAGEX");
            for (PLSQLPackageType pkgType : packages) {
                // now get the desired procedures/functions from the processed package
                for (ProcedureType procType : pkgType.getProcedures()) {
                    if (procedurePatterns.contains(procType.getProcedureName())) {
                        dbProcedures.add(procType);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() {
        if (ddlDrop) {
            runDdl(conn, DROP_PACKAGE, ddlDebug);
        }
    }

    @Test
    @SuppressWarnings({ "unchecked" })
    public void testProcedureOverload() {
        if (dbProcedures == null || dbProcedures.isEmpty()) {
            fail("No types were generated.");
        }
        XMLEntityMappings mappings = null;
        try {
            JPAMetadataGenerator gen = new JPAMetadataGenerator(DEFAULT_PACKAGE_NAME, databasePlatform);
            mappings = gen.generateXmlEntityMappings(dbProcedures);
        } catch (Exception x) {
            fail("An unexpected exception occurred: " + x.getMessage());
        }
        if (mappings == null) {
            fail("No Jakarta Persistence metadata was generated");
        }
        ByteArrayOutputStream metadata = new ByteArrayOutputStream();
        XMLEntityMappingsWriter.write(mappings, metadata);
        Document testDoc = xmlParser.parse(new StringReader(metadata.toString()));
        removeEmptyTextNodes(testDoc);
        Document controlDoc = xmlParser.parse(new StringReader(procedureMetadata));
        removeEmptyTextNodes(controlDoc);
        assertTrue("Metadata comparison failed.  Expected:\n" + documentToString(controlDoc) + "\nActual\n" + documentToString(testDoc), comparer.isNodeEqual(controlDoc, testDoc));
    }

    static final String procedureMetadata =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<orm:entity-mappings xsi:schemaLocation=\"http://www.eclipse.org/eclipselink/xsds/persistence/orm org/eclipse/persistence/jpa/eclipselink_orm_2_5.xsd\"" +
            "     xmlns:orm=\"http://www.eclipse.org/eclipselink/xsds/persistence/orm\" " +
            "     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
            "   <orm:named-plsql-stored-procedure-query name=\"P1\" procedure-name=\"PACKAGEX.P1\">\n" +
            "     <orm:parameter direction=\"OUT\" name=\"T\" database-type=\"VARCHAR_TYPE\"/>\n" +
            "  </orm:named-plsql-stored-procedure-query>\n" +
            "  <orm:named-plsql-stored-procedure-query name=\"P1_1\" procedure-name=\"PACKAGEX.P1\">\n" +
            "     <orm:parameter direction=\"OUT\" name=\"T\" database-type=\"VARCHAR_TYPE\"/>\n" +
            "     <orm:parameter direction=\"IN\" name=\"U\" database-type=\"VARCHAR_TYPE\"/>\n" +
            "  </orm:named-plsql-stored-procedure-query>\n" +
            "  <orm:named-plsql-stored-procedure-query name=\"P1_2\" procedure-name=\"PACKAGEX.P1\">\n" +
            "     <orm:parameter direction=\"OUT\" name=\"T\" database-type=\"VARCHAR_TYPE\"/>\n" +
            "     <orm:parameter direction=\"IN\" name=\"U\" database-type=\"VARCHAR_TYPE\"/>\n" +
            "     <orm:parameter direction=\"INOUT\" name=\"V\" database-type=\"NUMERIC_TYPE\"/>\n" +
            "  </orm:named-plsql-stored-procedure-query>\n" +
            "  <orm:named-plsql-stored-function-query name=\"F1\" function-name=\"PACKAGEX.F1\">\n" +
            "     <orm:return-parameter name=\"RESULT\" database-type=\"VARCHAR_TYPE\"/>\n" +
            "  </orm:named-plsql-stored-function-query>\n" +
            "  <orm:named-plsql-stored-function-query name=\"F1_1\" function-name=\"PACKAGEX.F1\">\n" +
            "     <orm:parameter direction=\"IN\" name=\"U\" database-type=\"VARCHAR_TYPE\"/>\n" +
            "     <orm:return-parameter name=\"RESULT\" database-type=\"VARCHAR_TYPE\"/>\n" +
            "  </orm:named-plsql-stored-function-query>\n" +
            "</orm:entity-mappings>";

}
