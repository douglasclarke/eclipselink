/*
 * Copyright (c) 2011, 2021 Oracle and/or its affiliates. All rights reserved.
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
// Oracle = 2.2 - Initial implementation
package org.eclipse.persistence.testing.jaxb.annotations.xmltransformation;

import java.sql.Time;
import java.sql.Timestamp;

import org.eclipse.persistence.mappings.foundation.AbstractTransformationMapping;
import org.eclipse.persistence.mappings.transformers.FieldTransformer;
import org.eclipse.persistence.sessions.Session;


public class EndTimeTransformer implements FieldTransformer {

    @Override
    public String buildFieldValue(Object instance, String fieldName, Session session) {
        return ((Employee)instance).normalHours[1];
    }

    @Override
    public void initialize(AbstractTransformationMapping mapping) {
        // TODO Auto-generated method stub

    }
}
