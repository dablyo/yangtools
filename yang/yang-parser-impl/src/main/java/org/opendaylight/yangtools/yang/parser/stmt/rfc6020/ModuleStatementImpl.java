/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.parser.stmt.rfc6020;

import org.opendaylight.yangtools.yang.model.api.stmt.ModuleStatement;
import org.opendaylight.yangtools.yang.model.api.stmt.NamespaceStatement;
import org.opendaylight.yangtools.yang.model.api.stmt.PrefixStatement;
import org.opendaylight.yangtools.yang.model.api.stmt.YangVersionStatement;
import org.opendaylight.yangtools.yang.parser.spi.meta.StmtContext;

public class ModuleStatementImpl extends AbstractRootStatement<ModuleStatement> implements ModuleStatement {

    protected ModuleStatementImpl(StmtContext<String, ModuleStatement,?> context) {
        super(context);
    }

    @Override
    public String getName() {
        return rawArgument();
    }

    @Override
    public YangVersionStatement getYangVersion() {
        return firstDeclared(YangVersionStatement.class);
    }

    @Override
    public NamespaceStatement getNamespace() {
        return firstDeclared(NamespaceStatement.class);
    }

    @Override
    public PrefixStatement getPrefix() {
        return firstDeclared(PrefixStatement.class);
    }

}
