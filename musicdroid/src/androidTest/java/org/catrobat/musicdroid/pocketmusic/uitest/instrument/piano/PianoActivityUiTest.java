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

import android.content.pm.ActivityInfo;
import android.view.View;

import com.robotium.solo.Solo;

import org.catrobat.musicdroid.pocketmusic.R;
import org.catrobat.musicdroid.pocketmusic.instrument.InstrumentActivity;
import org.catrobat.musicdroid.pocketmusic.instrument.piano.PianoActivity;
import org.catrobat.musicdroid.pocketmusic.note.NoteName;
import org.catrobat.musicdroid.pocketmusic.note.Octave;
import org.catrobat.musicdroid.pocketmusic.note.midi.ProjectToMidiConverter;
import org.catrobat.musicdroid.pocketmusic.test.note.NoteEventTestDataFactory;
import org.catrobat.musicdroid.pocketmusic.uitest.BaseActivityInstrumentationTestCase2;

import java.io.File;

public class PianoActivityUiTest extends BaseActivityInstrumentationTestCase2<PianoActivityMock> {

    private PianoActivityMock pianoActivity;
    private String pianoButtonText;

    public PianoActivityUiTest() {
        super(PianoActivityMock.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        pianoActivity = getActivity();
        pianoButtonText = solo.getString(R.string.note_name_c);
        InstrumentActivity.inCallback = false;
    }

    @Override
    protected void tearDown() throws Exception {
        if (ProjectToMidiConverter.MIDI_FOLDER.isDirectory()) {
            for (File file : ProjectToMidiConverter.MIDI_FOLDER.listFiles())
                file.delete();
        }
        pianoActivity.getSymbolContainer().clear();
        pianoActivity.getMidiPlayer().stop();
        super.tearDown();
    }

    public void testClear() throws Throwable {
        addNoteToNoteSheet(NoteName.C4);
        solo.clickOnActionBarItem(R.id.action_clear_midi);
        assertTrue(solo.waitForText(pianoActivity.getString(R.string.clear_success)));

        assertEquals(0, pianoActivity.getSymbolContainer().size());
    }

    private void addNoteToNoteSheet(final NoteName noteName) throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                pianoActivity.addNoteEvent(NoteEventTestDataFactory.createNoteEvent(noteName, true));
                pianoActivity.addNoteEvent(NoteEventTestDataFactory.createNoteEvent(noteName, false));
            }
        });

        getInstrumentation().waitForIdleSync();
    }

    public void testUndo() throws Throwable {
        addNoteToNoteSheet(NoteName.C4);
        solo.clickOnActionBarItem(R.id.action_undo_midi);

        assertEquals(0, pianoActivity.getSymbolContainer().size());
    }

    public void testRotateWithSymbolsDrawn() throws Throwable {
        solo.clickOnButton(pianoButtonText);
        rotateAndReturnActivity(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        solo.clickOnButton(pianoButtonText);

        assertEquals(2, pianoActivity.getSymbolContainer().size());
    }

    private void rotateAndReturnActivity(int orientation) {
        pianoActivity.setRequestedOrientation(orientation);
        getInstrumentation().waitForIdleSync();
        pianoActivity = getActivity();
    }

    public void testRotateAndUndo() {
        solo.clickOnButton(pianoButtonText);
        rotateAndReturnActivity(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        solo.clickOnButton(pianoButtonText);
        rotateAndReturnActivity(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        solo.clickOnActionBarItem(R.id.action_undo_midi);
        solo.clickOnActionBarItem(R.id.action_undo_midi);

        assertEquals(0, pianoActivity.getSymbolContainer().size());
    }

    private void clickSomePianoButtons(int amount) throws Throwable {
        long[] currentTimeMillis = new long[amount * 2];

        for (int i = 0; i < amount; i++) {
            long startTick = 60 * i;
            long endTick = 60 * (i + 1);

            currentTimeMillis[2 * i] = startTick;
            currentTimeMillis[(2 * i) + 1] = endTick;
        }

        pianoActivity.initializeTickProvider(currentTimeMillis);

        for (int i = 0; i < amount; i++) {
            addNoteToNoteSheet(NoteName.C4);
        }
    }

    public void testPlayMidi() throws Throwable {
        clickSomePianoButtons(5);
        solo.clickOnActionBarItem(R.id.action_play_and_stop_midi);
        Thread.sleep(100);

        assertTrue(pianoActivity.getMidiPlayer().isPlaying());
    }


    public void testPlayButtonShown() {
        assertTrue(solo.getCurrentActivity().getResources().getDrawable(R.drawable.ic_action_play).isVisible());
    }

    public void testStopButtonShown() throws Throwable {
        clickSomePianoButtons(5);
        solo.clickOnActionBarItem(R.id.action_play_and_stop_midi);

        assertTrue(solo.getCurrentActivity().getResources().getDrawable(R.drawable.ic_action_stop).isVisible());
    }

    public void testPlayButtonShownAfterStop() throws Throwable {
        clickSomePianoButtons(5);
        solo.clickOnActionBarItem(R.id.action_play_and_stop_midi);
        solo.clickOnActionBarItem(R.id.action_play_and_stop_midi);

        assertTrue(solo.getCurrentActivity().getResources().getDrawable(R.drawable.ic_action_play).isVisible());
    }

    public void testStopMidi() throws Throwable {
        clickSomePianoButtons(5);
        solo.clickOnActionBarItem(R.id.action_play_and_stop_midi);
        solo.clickOnActionBarItem(R.id.action_play_and_stop_midi);
        solo.waitForText(pianoActivity.getString(R.string.stopped));

        assertFalse(pianoActivity.getMidiPlayer().isPlaying());
    }

    public void testPlayMidiEmptyTrack() {
        solo.clickOnActionBarItem(R.id.action_play_and_stop_midi);

        assertFalse(pianoActivity.getMidiPlayer().isPlaying());
    }

    public void testPlayMidiFinishedPlaying() throws InterruptedException {
        solo.clickOnButton(pianoButtonText);
        solo.clickOnActionBarItem(R.id.action_play_and_stop_midi);
        solo.waitForDialogToOpen();
        solo.waitForDialogToClose();

        assertFalse(pianoActivity.getMidiPlayer().isPlaying());
    }

    public void testClickOnButtonMaxTrackSize() throws Throwable {
        clickSomePianoButtons(InstrumentActivity.MAX_SYMBOLS_SIZE);

        assertEquals(InstrumentActivity.MAX_SYMBOLS_SIZE, pianoActivity.getSymbolContainer().size());
    }

    public void testMaxTrackSizeTextView() throws Throwable {
        int buttonPressCount = 6;

        clickSomePianoButtons(buttonPressCount);

        String expectedTextViewText = buttonPressCount + " / " + InstrumentActivity.MAX_SYMBOLS_SIZE;
        String actualTextViewText = pianoActivity.getTrackSizeString();

        assertEquals(expectedTextViewText, actualTextViewText);
    }

    public void testEditModeDelete() throws Throwable {
        clickSomePianoButtons(1);
        enterEditModeWithOneMarkedSymbol(0);

        assertTrue(PianoActivity.inCallback);

        clickDeleteInEditMode();

        assertFalse(PianoActivity.inCallback);
        assertEquals(0, pianoActivity.getSymbolContainer().size());
        assertEquals(0, pianoActivity.getSymbolContainer().getMarkedSymbolCount());
    }

    private void clickDeleteInEditMode() {
        View v = getActivity().findViewById(R.id.edit_callback_action_delete_project);
        solo.clickOnView(v);

        solo.sleep(1000);
    }

    public void testEditModeDeleteUndo() throws Throwable {
        clickSomePianoButtons(1);
        enterEditModeWithOneMarkedSymbol(0);
        clickDeleteInEditMode();

        solo.clickOnActionBarItem(R.id.action_undo_midi);

        assertEquals(0, pianoActivity.getSymbolContainer().size());
    }

    public void testEditModeReplace() throws Throwable {
        solo.clickOnButton(pianoButtonText);
        enterEditModeWithOneMarkedSymbol(0);
        solo.clickOnButton(pianoButtonText);
        assertEquals(1, pianoActivity.getSymbolContainer().size());
        assertTrue(pianoActivity.getSymbolContainer().get(0).isMarked());
    }

    public void testClickBreak() {
        solo.setActivityOrientation(Solo.PORTRAIT);
        solo.clickOnButton(1);
        solo.clickOnImageButton(1);

        solo.sleep(1000);

        assertEquals(1, pianoActivity.getSymbolContainer().size());
        assertTrue(pianoActivity.getAdditionalSettingsFragment().isPianoViewVisible());
    }

    public void testClickBreakAndReplace() {
        solo.setActivityOrientation(Solo.PORTRAIT);
        solo.clickOnButton(getActivity().getString(R.string.breaks));
        solo.clickOnImageButton(1);

        solo.sleep(1000);

        pianoActivity.getSymbolContainer().get(0).setMarked(true);
        solo.clickLongOnView(pianoActivity.getNoteSheetView());

        solo.sleep(1000);

        solo.clickOnButton(pianoButtonText);

        assertEquals(1, pianoActivity.getSymbolContainer().size());
        assertTrue(pianoActivity.getSymbolContainer().get(0).isMarked());
    }

    public void testClickOctaveDownButton() {
        solo.clickOnButton(getActivity().getString(R.string.minus));

        solo.sleep(1000);

        assertEquals(Octave.DEFAULT_OCTAVE.previous(), pianoActivity.getOctave());
        assertEquals(pianoActivity.getNoteSheetViewFragment().getOctaveText(), getActivity().getString(R.string.octave) + " " + (-1));
    }

    public void testClickOctaveUpButton() {
        solo.clickOnButton(getActivity().getString(R.string.plus));

        solo.sleep(1000);

        assertEquals(Octave.DEFAULT_OCTAVE.next(), pianoActivity.getOctave());
        assertEquals(pianoActivity.getNoteSheetViewFragment().getOctaveText(), getActivity().getString(R.string.octave) + " " + (1));
    }

    public void testEditModeReplaceAddAccord() throws Throwable {
        NoteName[] noteNames = new NoteName[2];
        noteNames[0] = NoteName.C4;
        noteNames[1] = NoteName.D4;

        long startFirstNote = 0;
        long endFirstNote = 60;
        long startAccordFirstNote = 0;
        long startAccordSecondNote = 0;
        long endAccordFirstNote = 60;
        long endAccordSecondNote = 60;
        long[] currentTimeMillis = {startFirstNote, endFirstNote,
                startAccordFirstNote, startAccordSecondNote,
                endAccordFirstNote, endAccordSecondNote};
        pianoActivity.initializeTickProvider(currentTimeMillis);

        addNoteToNoteSheet(noteNames[0]);
        enterEditModeWithOneMarkedSymbol(0);
        addAccordToNoteSheet(noteNames);

        assertEquals(1, pianoActivity.getSymbolContainer().size());
    }

    private void enterEditModeWithOneMarkedSymbol(final int indexOfMarkedSymbol) throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                pianoActivity.getSymbolContainer().get(indexOfMarkedSymbol).setMarked(true);
                pianoActivity.startEditMode();
            }
        });

        getInstrumentation().waitForIdleSync();
    }

    private void addAccordToNoteSheet(final NoteName[] noteNames) throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (NoteName noteName : noteNames) {
                    pianoActivity.addNoteEvent(NoteEventTestDataFactory.createNoteEvent(noteName, true));
                }
                for (NoteName noteName : noteNames) {
                    pianoActivity.addNoteEvent(NoteEventTestDataFactory.createNoteEvent(noteName, false));
                }
            }
        });

        getInstrumentation().waitForIdleSync();
    }
}
