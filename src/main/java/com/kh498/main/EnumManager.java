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

package com.kh498.main;

import java.util.LinkedHashMap;

import javax.annotation.Nullable;

import com.kh498.events.EnumEvent;
import com.kh498.events.EvtEnum;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;

/**
 * @author kh498
 */
public class EnumManager
{
	private static LinkedHashMap<String, Object> skEnums;

	private static String lastExpr;

	EnumManager ()
	{
		skEnums = new LinkedHashMap<String, Object> ();
	}

	/**
	 * 
	 * @param newEnumName
	 *        The name of the new enum
	 * @param parentEnum
	 *        If null add the enum to main Map if not add to the parentEnum
	 * @return False if an enum with that name already exists
	 */
	@ SuppressWarnings ("unchecked")
	public static boolean addEnum (String newEnumName, @ Nullable String parentEnum)
	{
		if (parentEnum == null)
		{
			if (skEnums.containsKey (newEnumName))
			{
				Skript.error ("An enum with the name " + newEnumName + " already exists!");
				return true;
			}
			skEnums.put (newEnumName, new LinkedHashMap<String, Object> ());
		} else if (skEnums.containsKey (parentEnum))
		{
			/*put a new enum inside the parentEnum*/
			LinkedHashMap<String, Object> tempMap = (LinkedHashMap<String, Object>) skEnums.get (parentEnum);
			if (tempMap.containsKey (newEnumName))
			{
				Skript.error ("A sub enum with the name " + newEnumName + " already exists in the enum " + parentEnum);
				return true;
			}
			tempMap.put (newEnumName, new LinkedHashMap<String, Object> ());

		} else
		{
			return false;
		}
		return true;
	}

	//TODO Maybe make the enum hold multible values (making each values a new class)
	/**
	 * @param enumName
	 *        Object, The enum to add the value to
	 * @param valueName
	 *        Object, The name of the enum value
	 * @param obj
	 *        Object, the object the enum is refering to
	 * @return false if the mother enum does exist
	 */
	@ SuppressWarnings ("unchecked")
	public static boolean addValue (@ Nullable String parentEnum, String currEnum, String valueName, Object obj)
	{
		LinkedHashMap<String, Object> enumValues = null;

		if (parentEnum == null)
		{
			if (!skEnums.containsKey (currEnum))
			{
				Skript.error ("The enum " + currEnum + " does not exist.");
				return false;
			}
			enumValues = (LinkedHashMap<String, Object>) skEnums.get (currEnum);
		} else
		{
			if (!skEnums.containsKey (parentEnum))
			{
				Skript.error ("The enum " + parentEnum + " does not exist.");
				return false;
			} else if (!((LinkedHashMap<String, Object>) skEnums.get (parentEnum)).containsKey (currEnum))
			{
				Skript.error ("The sub enum " + currEnum + " does not exist.");
				return false;
			}

			enumValues = (LinkedHashMap<String, Object>) ((LinkedHashMap<String, Object>) skEnums.get (parentEnum)).get (currEnum);
		}

		if (enumValues == null)
		{
			enumValues = new LinkedHashMap<String, Object> ();
		} else if (enumValues.containsKey (valueName))
		{
			Skript.error ("An enum cannot hold two identical enum keys!");
			return true;
		}
		return enumValues.put (valueName, obj) != null;
	}

	/**
	 * @param errorMsg
	 *        Message to display if the event is not valid
	 * @return true if the current skript event is of the type {@link EvtEnum}
	 */
	public static boolean isValidEvent (String errMsg)
	{
		if (!ScriptLoader.isCurrentEvent (EnumEvent.class))
		{
			Skript.error (errMsg);
			return false;
		}
		return true;
	}

	public static LinkedHashMap<String, Object> getEnums ()
	{
		return skEnums;
	}

	public static void flushEnums ()
	{
		skEnums.clear ();
	}

	/**
	 * @param expr
	 *        The expression gotten from init()
	 * @return The string version of expr without ' around them
	 */
	public static String getProperEnumName (Object expr)
	{
		return expr.toString ().replaceAll ("'", "");
	}

	/**
	 * @return Either expr from the event, if not valid then the string version of expr
	 */
	public Object getProperEnumName ()
	{
		return this.toString ().replaceAll ("'", "");
	}

	/**
	 * @return the lastExpr
	 */
	public static String getLastExpr ()
	{
		return lastExpr;
	}

	/**
	 * @param lastExpr
	 *        the lastExpr to set
	 */
	public static void setLastExpr (String lastExpr)
	{
		EnumManager.lastExpr = lastExpr;
	}

	public static String getConKey (@ Nullable String key)
	{
		/* Get the parents expression (the key to the enum-map) */
		if (key == null || key.isEmpty ())
			return null;
		return key.replaceFirst ("(?i)Sub ", "").replaceFirst ("(?i)enum ", "").replaceFirst ("create new ", "");
	}
}
