package com.pm.server.player;

import com.pm.server.datatype.Coordinate;
import com.pm.server.datatype.PlayerName;
import com.pm.server.datatype.PlayerState;

public interface Player {

	void resetLocation();

	PlayerName getName();

	void setLocation(Coordinate location);

	Coordinate getLocation();

	void setState(PlayerState state);

	PlayerState getState();

}
