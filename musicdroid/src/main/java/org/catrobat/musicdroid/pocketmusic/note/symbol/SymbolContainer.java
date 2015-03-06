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

package org.catrobat.musicdroid.pocketmusic.note.symbol;

import org.catrobat.musicdroid.pocketmusic.note.MusicalInstrument;
import org.catrobat.musicdroid.pocketmusic.note.MusicalKey;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class SymbolContainer implements Serializable {

    private static final long serialVersionUID = 7483012569872527915L;

    private MusicalKey key;
    private MusicalInstrument instrument;
    private int beatsPerMinute;
    private LinkedList<Symbol> symbols;

    public SymbolContainer(MusicalKey key, MusicalInstrument instrument, int beatsPerMinute) {
        this.key = key;
        this.instrument = instrument;
        this.beatsPerMinute = beatsPerMinute;

        symbols = new LinkedList<Symbol>();
    }

    public MusicalKey getKey() {
        return key;
    }

    public MusicalInstrument getInstrument() {
        return instrument;
    }

    public int getBeatsPerMinute() {
        return beatsPerMinute;
    }

    public int size() {
        return symbols.size();
    }

    public void add(Symbol symbol) {
        symbols.add(symbol);
    }

    public void addAll(List<Symbol> symbols) {
        this.symbols.addAll(symbols);
    }

    public void clear() {
        symbols.clear();
    }

    public List<Symbol> getSymbols() {
        return symbols;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof SymbolContainer)) {
            return false;
        }

        SymbolContainer symbolContainer = (SymbolContainer) obj;

        if (symbolContainer.getKey() != getKey()) {
            return false;
        }

        if (symbolContainer.getInstrument() != getInstrument()) {
            return false;
        }

        if (symbolContainer.getBeatsPerMinute() != getBeatsPerMinute()) {
            return false;
        }

        if (size() == symbolContainer.size()) {
            for (int i = 0; i < size(); i++) {
                if (false == getSymbols().get(i).equals(symbolContainer.getSymbols().get(i))) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }
}
