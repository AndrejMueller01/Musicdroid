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

package org.catrobat.musicdroid.pocketmusic.uitest;

import android.content.res.Resources;
import android.view.View;

import com.robotium.solo.Solo;

import junit.framework.Assert;

public class ContextualActionBarHelper {
    public final String OVERFLOW_BUTTON_CLASS_NAME = "com.android.internal.view.menu.ActionMenuPresenter$OverflowMenuButton";
    public final String OVERFLOW_BUTTON_CLASS_NAME_NEW = "android.widget.ActionMenuPresenter$OverflowMenuButton";
    private Solo solo;


    public ContextualActionBarHelper(Solo solo) {
        this.solo = solo;
    }

    public void clickOnContextualActionBarItem(int menuItemId, String itemText) {
        View menuItemView = null;
        boolean iconFound = false;

        for (View view : solo.getViews()) {
            if (view.getId() == menuItemId) {
                menuItemView = view;
                iconFound = true;
            }
        }

        if (!iconFound)
            for (View view : solo.getViews()) {
                if (view.getClass().getName().compareTo(OVERFLOW_BUTTON_CLASS_NAME) == 0 ||
                        view.getClass().getName().compareTo(OVERFLOW_BUTTON_CLASS_NAME_NEW) == 0) {
                    solo.clickOnView(view);
                    if (solo.waitForText(itemText)) {
                        menuItemView = solo.getText(itemText);
                        break;
                    }
                }
            }

        Assert.assertNotNull("Contextual Action Bar Item should be found", menuItemView);
        solo.clickOnView(menuItemView);
    }

    public void clickOnDoneButton() {
        int doneButtonId = Resources.getSystem().getIdentifier("action_mode_close_button", "id", "android");
        solo.clickOnView(solo.getView(doneButtonId));
    }
}
