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

import org.catrobat.musicdroid.pocketmusic.note.NoteName;
import org.catrobat.musicdroid.pocketmusic.note.symbol.BreakSymbol;
import org.catrobat.musicdroid.pocketmusic.note.symbol.NoteSymbol;
import org.catrobat.musicdroid.pocketmusic.note.symbol.Symbol;

public class SymbolPlayer {

    private static SymbolPlayer instance;

    private MidiDriver midiDriver;

    // TODO fw tests
    private SymbolPlayer() {
        midiDriver = new MidiDriver();
        midiDriver.start();
    }

    public static SymbolPlayer getInstance() {
        if (null == instance) {
            instance = new SymbolPlayer();
        }

        return instance;
    }

    public void play(Symbol symbol, int beatsPerMinute) {
        if (symbol instanceof BreakSymbol) {
            BreakSymbol breakSymbol = (BreakSymbol) symbol;

            sleep(breakSymbol.getNoteLength().toMilliseconds(beatsPerMinute));
        } else if (symbol instanceof NoteSymbol) {
            NoteSymbol noteSymbol = (NoteSymbol) symbol;
            long millis = 0;

            for (NoteName noteName : noteSymbol.getNoteNamesSorted()) {
                playNote(noteName, NoteEventToMidiEventConverter.DEFAULT_NOISE);

                long temp = noteSymbol.getNoteLength(noteName).toMilliseconds(beatsPerMinute);

                if (temp > millis) {
                    millis = temp;
                }
            }

            // TODO fw sollen alle Symbole genau eine NoteLength haben?
            sleep(millis);

            for (NoteName noteName : noteSymbol.getNoteNamesSorted()) {
                playNote(noteName, NoteEventToMidiEventConverter.DEFAULT_SILENT);
            }
        }
    }

    private void playNote(NoteName noteName, int velocity) {
        sendMidi(0, noteName.getMidi(), velocity);
    }

    // TODO fw was is m
    private void sendMidi(int m, int pitch, int velocity) {
        byte msg[] = new byte[3];
        msg[0] = (byte) m;
        msg[1] = (byte) pitch;
        msg[2] = (byte) velocity;

        midiDriver.queueEvent(msg);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
