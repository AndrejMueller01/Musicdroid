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

package org.catrobat.musicdroid.pocketmusic.uitest.instrument.piano;

import android.content.res.Configuration;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;

import org.catrobat.musicdroid.pocketmusic.instrument.piano.PianoViewFragment;
import org.catrobat.musicdroid.pocketmusic.note.Octave;
import org.catrobat.musicdroid.pocketmusic.tools.DisplayMeasurements;

public class PianoViewFragmentTest extends ActivityInstrumentationTestCase2<PianoActivityMock> {

    private PianoActivityMock pianoActivityMock;
    private PianoViewFragment pianoViewFragment;

    public PianoViewFragmentTest() {
        super(PianoActivityMock.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        pianoActivityMock = getActivity();
        pianoViewFragment = pianoActivityMock.getPianoViewFragment();
    }

    @UiThreadTest
    public void testCalculatePianoKeyPositions() {
        pianoViewFragment.calculatePianoKeyPositions(PianoViewFragment.DEFAULT_PIANO_KEY_WIDTH_SCALE_FACTOR, PianoViewFragment.DEFAULT_BLACK_KEY_HEIGHT_SCALE_FACTOR);

        assertButtonWidth(PianoViewFragment.DEFAULT_PIANO_KEY_WIDTH_SCALE_FACTOR);
        assertButtonPosition(PianoViewFragment.DEFAULT_PIANO_KEY_WIDTH_SCALE_FACTOR);
    }

    private void assertButtonWidth(int keyWidthScaleFactor) {
        DisplayMeasurements displayMeasurements = new DisplayMeasurements(pianoActivityMock);
        int buttonWidth = displayMeasurements.getDisplayWidth() / (Octave.NUMBER_OF_UNSIGNED_HALF_TONE_STEPS_PER_OCTAVE + keyWidthScaleFactor);

        for (int i = 0; i < pianoViewFragment.getBlackButtonCount(); i++) {
            assertEquals(pianoViewFragment.getBlackButtonAtIndex(i).getLayoutParams().width, buttonWidth);
        }
        for (int i = 0; i < pianoViewFragment.getWhiteButtonCount(); i++) {
            assertEquals(pianoViewFragment.getWhiteButtonAtIndex(i).getLayoutParams().width, buttonWidth);
        }
    }

    private void assertButtonPosition(int keyWidthScaleFactor) {
        DisplayMeasurements displayMeasurements = new DisplayMeasurements(pianoActivityMock);
        int buttonWidth = displayMeasurements.getDisplayWidth() / (Octave.NUMBER_OF_UNSIGNED_HALF_TONE_STEPS_PER_OCTAVE + keyWidthScaleFactor);

        assertEquals((buttonWidth / 2), pianoViewFragment.getBlackButtonAtIndex(0).getLeft());
        assertEquals((buttonWidth / 2 * 3), pianoViewFragment.getBlackButtonAtIndex(1).getLeft());

        assertEquals(0, pianoViewFragment.getWhiteButtonAtIndex(0).getLeft());
        assertEquals(buttonWidth, pianoViewFragment.getWhiteButtonAtIndex(1).getLeft());
    }

    @UiThreadTest
    public void testButtonVisibilityPianoLayout() {
        int expectedVisibility;
        int actualVisibility;

        for (int i = 0; i < pianoViewFragment.getBlackButtonCount(); i++) {
            if (i == PianoViewFragment.DEFAULT_INACTIVE_BLACK_KEY) {
                expectedVisibility = Button.INVISIBLE;
            } else {
                expectedVisibility = Button.VISIBLE;
            }

            actualVisibility = pianoViewFragment.getBlackButtonAtIndex(i).getVisibility();

            assertEquals(expectedVisibility, actualVisibility);
        }
    }

    @UiThreadTest
    public void testDisableBlackKey1() {
        int index = 1;

        pianoViewFragment.setBlackKeyInvisible(index);
        Button button = pianoViewFragment.getBlackButtonAtIndex(index);

        assertEquals(button.getVisibility(), Button.INVISIBLE);
    }

    @UiThreadTest
    public void testPianoButtonLayoutByOrientation() {
        pianoViewFragment.prepareViewDependingOnOrientation(Configuration.ORIENTATION_LANDSCAPE);

        assertButtonWidth(PianoViewFragment.DEFAULT_LANDSCAPE_KEY_WIDTH_SCALE_FACTOR);
    }
}
