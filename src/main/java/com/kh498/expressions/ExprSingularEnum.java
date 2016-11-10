/*
 *  This file is part of Enumsk
 *  
 *  Copyright (C) 2016, kh498
 * 
 *  Enumsk is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Enumsk is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Enumsk.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.kh498.expressions;

import java.util.LinkedHashMap;

import javax.annotation.Nullable;

import org.bukkit.event.Event;

import com.kh498.main.EnumManager;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;

public class ExprSingularEnum extends SimpleExpression<Object>
{

	private Expression<Object> expr0;
	private Expression<Object> expr1;

	@ SuppressWarnings ("unchecked")
	@ Override
	public boolean init (Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult)
	{
		expr0 = (Expression<Object>) exprs[0];
		expr1 = (Expression<Object>) exprs[1];
		return true;
	}

	@ Override
	@ Nullable
	protected Object[] get (Event e)
	{
//		final Object enumValue = e0.getSingle (event);
//		final Object enumName = e1.getSingle (event);
		final Object enumValue = EnumManager.getProperEnumName (e, expr0);
		final Object enumName = EnumManager.getProperEnumName (e, expr1);

		/* Get the object by first getting the value map (Map<Object, Object>) then getting the value */
		try
		{
			@ SuppressWarnings ("unchecked")
			Object[] obj = { ((LinkedHashMap<Object, Object>) EnumManager.getEnums ().get (enumName)).get (enumValue) };
			return obj;
		} catch (NullPointerException e1)
		{
			try
			{
				@ SuppressWarnings ("unchecked")
				Object[] obj2 = { ((LinkedHashMap<Object, Object>) EnumManager.getEnums ().get (enumValue)).get (enumName) };
				return obj2;
			} catch (NullPointerException e2)
			{
			}
		}
		return null;
	}

	@ Override
	public boolean isSingle ()
	{
		return true;
	}

	@ Override
	public Class<? extends Object> getReturnType ()
	{
		return Object.class;
	}

	@ Override
	public String toString (@ Nullable Event e, boolean debug)
	{
		return "single enum value";
	}

}
