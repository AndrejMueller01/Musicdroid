/*
 * Musicdroid: An on-device music generator for Android
 * Copyright (C) 2010-2014 The Catrobat Team
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

package org.catrobat.musicdroid.pocketmusic.test.note.midi;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;

import org.catrobat.musicdroid.pocketmusic.note.Project;
import org.catrobat.musicdroid.pocketmusic.note.midi.MidiException;
import org.catrobat.musicdroid.pocketmusic.note.midi.ProjectPlayer;

import java.io.File;
import java.io.IOException;

public class ProjectPlayerMock extends ProjectPlayer {

    @Override
    protected void writeTempPlayFile(File tempPlayFile, Project project) throws IOException, MidiException {
    }

    @Override
    protected MediaPlayer createTrackPlayer(final Activity activity, final Uri uri) {
        return new MediaPlayerMock();
    }

    @Override
    public void onPlayComplete(File tempPlayFile, Activity activity) {
        super.onPlayComplete(tempPlayFile, activity);
    }

    private class MediaPlayerMock extends MediaPlayer {

        private boolean isPlaying;

        public MediaPlayerMock() {
            isPlaying = false;
        }

        @Override
        public void start() {
            isPlaying = true;
        }

        @Override
        public void stop() {
            isPlaying = false;
        }

        @Override
        public boolean isPlaying() {
            return isPlaying;
        }
    }
}
