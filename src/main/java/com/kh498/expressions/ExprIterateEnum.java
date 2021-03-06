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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import javax.annotation.Nullable;

import org.bukkit.event.Event;

import com.kh498.main.EnumManager;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;

public class ExprIterateEnum extends SimpleExpression<Object>
{

	private Expression<Object> expr0;
	private Object enumName;

	@ SuppressWarnings ("unchecked")
	@ Override
	public boolean init (Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult)
	{
		expr0 = (Expression<Object>) exprs[0];
		enumName = EnumManager.getProperEnumName (expr0);
		return (this.getEnums () != null) ? true : false;
	}

	@ Override
	@ Nullable
	protected Object[] get (Event e)
	{
		return getEnums ();
	}

	@ SuppressWarnings ("unchecked")
	private Object[] getEnums ()
	{
		try
		{
			final Collection<Object> objectMap = ((LinkedHashMap<String, Object>) EnumManager.getEnums ().get (enumName)).values ();

			ArrayList<Object> noNullList = new ArrayList<Object> ();

			for (Object LHM : objectMap) //LHM = Linked Hash Map
			{
				if (!(LHM instanceof LinkedHashMap<?, ?>))
				{
					noNullList.add (LHM);
				}
			}
			return noNullList.toArray (new Object[noNullList.size ()]);
		} catch (NullPointerException ex)
		{
		}
		Skript.error ("Can not find the enum");
		return null;
	}

	@ Override
	public boolean isSingle ()
	{
		return false;
	}

	@ Override
	public Class<? extends Object> getReturnType ()
	{
		return Object.class;
	}

	@ Override
	public String toString (@ Nullable Event e, boolean debug)
	{
		return "all values of an enum";
	}

}
