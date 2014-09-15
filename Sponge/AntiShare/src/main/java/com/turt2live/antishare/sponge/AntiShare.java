/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare.sponge;

import org.spongepowered.api.event.SpongeEventHandler;
import org.spongepowered.api.event.state.ServerAboutToStartEvent;
import org.spongepowered.api.event.state.ServerStartingEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;

/**
 * Sponge plugin main class
 *
 * @author turt2live
 */
@Plugin(id = "AntiShare", name = "AntiShare", version = VersionInfo.VERSION)
public class AntiShare {

    @SpongeEventHandler
    public void onLoad(ServerAboutToStartEvent event) {
    }

    @SpongeEventHandler
    public void onEnable(ServerStartingEvent event) {
        event.getGame().getLogger().info("[ANTISHARE]" + event.getGame().getAPIVersion());
        event.getGame().getLogger().info("[ANTISHARE]" + event.getGame().getImplementationVersion());
    }

    @SpongeEventHandler
    public void onDisable(ServerStoppingEvent event) {
    }
}
