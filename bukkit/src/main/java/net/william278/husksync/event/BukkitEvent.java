/*
 * This file is part of HuskSync, licensed under the Apache License 2.0.
 *
 *  Copyright (c) William278 <will27528@gmail.com>
 *  Copyright (c) contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.william278.husksync.event;

import net.william278.husksync.BukkitHuskSync;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public abstract class BukkitEvent extends Event implements net.william278.husksync.event.Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    protected BukkitEvent() {
    }

    @Override
    public CompletableFuture<net.william278.husksync.event.Event> fire() {
        final CompletableFuture<net.william278.husksync.event.Event> eventFireFuture = new CompletableFuture<>();
        // Don't fire events while the server is shutting down
        if (!BukkitHuskSync.getInstance().isEnabled()) {
            eventFireFuture.complete(this);
        } else {
            BukkitHuskSync.getInstance().getScheduler().runNextTick(() -> {
                Bukkit.getServer().getPluginManager().callEvent(this);
                eventFireFuture.complete(this);
            });
        }
        return eventFireFuture;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
