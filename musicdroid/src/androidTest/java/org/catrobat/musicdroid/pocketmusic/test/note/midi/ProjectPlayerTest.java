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

import android.test.AndroidTestCase;

import org.catrobat.musicdroid.pocketmusic.note.Project;
import org.catrobat.musicdroid.pocketmusic.note.Track;
import org.catrobat.musicdroid.pocketmusic.note.midi.MidiException;
import org.catrobat.musicdroid.pocketmusic.test.instrument.InstrumentActivityMock;
import org.catrobat.musicdroid.pocketmusic.test.note.ProjectTestDataFactory;
import org.catrobat.musicdroid.pocketmusic.test.note.TrackTestDataFactory;

import java.io.File;
import java.io.IOException;

public class ProjectPlayerTest extends AndroidTestCase {

    private static final File CACHE_DIR = new File("");

    private ProjectPlayerMock player;
    private InstrumentActivityMock activity;

    @Override
    protected void setUp() {
        player = new ProjectPlayerMock();
        activity = new InstrumentActivityMock();
    }

    public void testStop1() throws IOException, MidiException {
        Project project = ProjectTestDataFactory.createProjectWithOneSimpleTrack();

        player.play(activity, CACHE_DIR, project);
        player.stop();

        assertFalse(player.isPlaying());
    }

    public void testPlayTrack1() throws IOException, MidiException {
        Project project = ProjectTestDataFactory.createProjectWithOneSimpleTrack();

        player.play(activity, CACHE_DIR, project);

        assertTrue(player.isPlaying());
    }

    public void testPlayTrackCompleteEvent() throws IOException, MidiException {
        Project project = ProjectTestDataFactory.createProjectWithOneSimpleTrack();
        FileMock tempFileToPlay = new FileMock();

        player.play(activity, CACHE_DIR, project);
        player.onPlayComplete(tempFileToPlay, activity);

        assertTrue(tempFileToPlay.isDeleted());
    }

    private class FileMock extends File {

        private boolean isDeleted;

        public FileMock() {
            super("");
            isDeleted = false;
        }

        public boolean isDeleted() {
            return isDeleted;
        }

        @Override
        public boolean delete() {
            isDeleted = true;

            return isDeleted;
        }
    }
}
