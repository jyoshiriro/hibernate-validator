/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.validator.internal.engine.messageinterpolation.el;

import jakarta.el.ArrayELResolver;
import jakarta.el.BeanELResolver;
import jakarta.el.CompositeELResolver;
import jakarta.el.ELResolver;
import jakarta.el.ExpressionFactory;
import jakarta.el.ListELResolver;
import jakarta.el.MapELResolver;
import jakarta.el.ResourceBundleELResolver;
import jakarta.el.StandardELContext;

/**
 * @author Hardy Ferentschik
 * @author Guillaume Smet
 */
public class SimpleELContext extends StandardELContext {
	private static final ELResolver DEFAULT_RESOLVER = new CompositeELResolver() {
		{
			add( new RootResolver() );
			add( new ArrayELResolver( true ) );
			add( new ListELResolver( true ) );
			add( new MapELResolver( true ) );
			add( new ResourceBundleELResolver() );
			add( new BeanELResolver( true ) );
		}
	};

	public SimpleELContext(ExpressionFactory expressionFactory) {
		super( expressionFactory );

		// In jakarta.el.ELContext, the ExpressionFactory is extracted from the context map. If it is not found, it
		// defaults to ELUtil.getExpressionFactory() which, if we provided the ExpressionFactory to the
		// ResourceBundleMessageInterpolator, might not be the same. Thus, we inject the ExpressionFactory in the
		// context.
		putContext( ExpressionFactory.class, expressionFactory );
	}

	@Override
	public void addELResolver(ELResolver cELResolver) {
		throw new UnsupportedOperationException( getClass().getSimpleName() + " does not support addELResolver." );
	}

	@Override
	public ELResolver getELResolver() {
		return DEFAULT_RESOLVER;
	}

}
