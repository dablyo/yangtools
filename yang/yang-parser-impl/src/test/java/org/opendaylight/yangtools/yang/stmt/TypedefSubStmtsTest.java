/*
 * Copyright (c) 2016 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.yangtools.yang.stmt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;
import org.junit.Test;
import org.opendaylight.yangtools.yang.model.api.TypeDefinition;
import org.opendaylight.yangtools.yang.parser.spi.meta.ReactorException;
import org.opendaylight.yangtools.yang.parser.stmt.reactor.CrossSourceStatementReactor;
import org.opendaylight.yangtools.yang.parser.stmt.rfc6020.YangInferencePipeline;
import org.opendaylight.yangtools.yang.parser.stmt.rfc6020.YangStatementSourceImpl;
import org.opendaylight.yangtools.yang.parser.stmt.rfc6020.effective.EffectiveSchemaContext;

public class TypedefSubStmtsTest {

    private static final YangStatementSourceImpl FOOBAR = new YangStatementSourceImpl
            ("/typedef-substmts-test/typedef-substmts-test.yang", false);

    @Test
    public void typedefSubStmtsTest() throws ReactorException {
        CrossSourceStatementReactor.BuildAction reactor = YangInferencePipeline.RFC6020_REACTOR.newBuild();
        StmtTestUtils.addSources(reactor, FOOBAR);

        EffectiveSchemaContext result = reactor.buildEffective();
        assertNotNull(result);

        Set<TypeDefinition<?>> typedefs = result.getTypeDefinitions();
        assertEquals(1, typedefs.size());

        TypeDefinition<?> typedef = typedefs.iterator().next();
        assertEquals("time-of-the-day", typedef.getQName().getLocalName());
        assertEquals("string", typedef.getBaseType().getQName().getLocalName());
        assertEquals("24-hour-clock", typedef.getUnits());
        assertEquals("1am", typedef.getDefaultValue().toString());
    }
}
