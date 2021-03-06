/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.model.repo.api;

import static com.google.common.base.Preconditions.checkArgument;
import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.io.ByteSource;
import java.io.IOException;
import java.io.InputStream;
import org.opendaylight.yangtools.concepts.Delegator;

/**
 * YIN text schema source representation. Exposes an RFC6020 XML representation as an {@link InputStream}.
 */
@Beta
public abstract class YinTextSchemaSource extends ByteSource implements YinSchemaSourceRepresentation {
    private final SourceIdentifier identifier;

    protected YinTextSchemaSource(final SourceIdentifier identifier) {
        this.identifier = Preconditions.checkNotNull(identifier);
    }

    public static SourceIdentifier identifierFromFilename(final String name) {
        checkArgument(name.endsWith(".yin"), "Filename %s does not have a .yin extension", name);
        // FIXME: add revision-awareness
        return SourceIdentifier.create(name.substring(0, name.length() - 4), Optional.absent());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final SourceIdentifier getIdentifier() {
        return identifier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends YinTextSchemaSource> getType() {
        return YinTextSchemaSource.class;
    }

    @Override
    public final String toString() {
        return addToStringAttributes(MoreObjects.toStringHelper(this).add("identifier", identifier)).toString();
    }

    /**
     * Add subclass-specific attributes to the output {@link #toString()} output. Since
     * subclasses are prevented from overriding {@link #toString()} for consistency
     * reasons, they can add their specific attributes to the resulting string by attaching
     * attributes to the supplied {@link ToStringHelper}.
     *
     * @param toStringHelper ToStringHelper onto the attributes can be added
     * @return ToStringHelper supplied as input argument.
     */
    protected abstract ToStringHelper addToStringAttributes(final ToStringHelper toStringHelper);

    /**
     * Create a new YinTextSchemaSource with a specific source identifier and backed
     * by ByteSource, which provides the actual InputStreams.
     *
     * @param identifier SourceIdentifier of the resulting schema source
     * @param delegate Backing ByteSource instance
     * @return A new YinTextSchemaSource
     */
    public static YinTextSchemaSource delegateForByteSource(final SourceIdentifier identifier, final ByteSource delegate) {
        return new DelegatedYinTextSchemaSource(identifier, delegate);
    }

    private static final class DelegatedYinTextSchemaSource extends YinTextSchemaSource implements Delegator<ByteSource> {
        private final ByteSource delegate;

        private DelegatedYinTextSchemaSource(final SourceIdentifier identifier, final ByteSource delegate) {
            super(identifier);
            this.delegate = Preconditions.checkNotNull(delegate);
        }

        @Override
        public ByteSource getDelegate() {
            return delegate;
        }

        @Override
        public InputStream openStream() throws IOException {
            return delegate.openStream();
        }

        @Override
        protected ToStringHelper addToStringAttributes(final ToStringHelper toStringHelper) {
            return toStringHelper.add("delegate", delegate);
        }
    }
}
