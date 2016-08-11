package com.pm.server.registry;

import java.util.List;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.Pacdot;

public interface PacdotRegistry {

	/**
	 * Retrieves the pacdot at the given location.
	 * 
	 * Returns null if no pacdot with the given location is found
	 * 
	 * @param location Location of the requested pacdot
	 * @throws NullPointerException if the location is null
	 * @return the requested pacdot
	 */
	Pacdot getPacdotByLocation(Coordinate location)
			throws NullPointerException;

	/**
	 * Retrieves all pacdots in the registry.
	 * 
	 * @return all pacdots
	 */
	List<Pacdot> getAllPacdots();

	/**
	 * Sets the eaten status of a pacdot to true/false.
	 * 
	 * Idempotent (e.g. eaten to eaten is valid)
	 * 
	 * @param location Location of the requested pacdot
	 * @param eaten Whether or not the pacdot has been eaten
	 * @throws NullPointerException if the location given is null
	 */
	void setEatenStatusByLocation(Coordinate location, boolean eaten)
			throws NullPointerException;

}