/*
 * Musicdroid: An on-device music generator for Android
 * Copyright (C) 2010-2015 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.musicdroid.pocketmusic.note.midi;

import android.app.Activity;

import org.catrobat.musicdroid.pocketmusic.note.Project;
import org.catrobat.musicdroid.pocketmusic.note.Track;

import java.io.File;
import java.io.IOException;

public class TrackPlayer {

    private static TrackPlayer instance;

    protected ProjectPlayer projectPlayer;

    protected TrackPlayer(ProjectPlayer projectPlayer) {
        this.projectPlayer = projectPlayer;
    }

    // TODO fw tests
    public static TrackPlayer getInstance() {
        if (null == instance) {
            instance = new TrackPlayer(ProjectPlayer.getInstance());
        }

        return instance;
    }

    public void play(Activity activity, File cacheDirectory, Track track, int beatsPerMinute) throws IOException, MidiException {
        Project project = new Project("temp", beatsPerMinute);
        project.addTrack(track);

        projectPlayer.play(activity, cacheDirectory, project);
    }

    public void stop() {
        projectPlayer.stop();
    }

    public boolean isPlaying() {
        return projectPlayer.isPlaying();
    }
}
