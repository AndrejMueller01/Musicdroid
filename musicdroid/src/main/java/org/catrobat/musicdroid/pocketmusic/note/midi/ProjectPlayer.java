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

package org.catrobat.musicdroid.pocketmusic.note.midi;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;

import org.catrobat.musicdroid.pocketmusic.ToastDisplayer;
import org.catrobat.musicdroid.pocketmusic.note.Project;
import org.catrobat.musicdroid.pocketmusic.note.Track;
import org.catrobat.musicdroid.pocketmusic.projectselection.ProjectSelectionActivity;

import java.io.File;
import java.io.IOException;

public class ProjectPlayer {

    private static final String TEMP_PLAY_FILE_NAME = "tmp_play.midi";

    private static ProjectPlayer instance;

    protected MediaPlayer player;

    protected ProjectPlayer() {}

    public static ProjectPlayer getInstance() {
        if (null == instance) {
            instance = new ProjectPlayer();
        }

        return instance;
    }

    public boolean isPlaying() {
        if (player != null) {
            return player.isPlaying();
        }

        return false;
    }

    public void stop() {
        if ((null != player) && player.isPlaying()) {
            player.stop();
        }
    }

    public void play(Activity activity, File cacheDirectory, Project project) throws IOException, MidiException {
        File tempPlayFile = new File(cacheDirectory, TEMP_PLAY_FILE_NAME);
        writeTempPlayFile(tempPlayFile, project);

        player = createPlayerWithOnCompletionListener(activity, tempPlayFile);
        player.start();
    }

    protected void writeTempPlayFile(File tempPlayFile, Project project) throws IOException, MidiException {
        ProjectToMidiConverter converter = new ProjectToMidiConverter();
        converter.writeProjectAsMidi(project, tempPlayFile);
    }

    private MediaPlayer createPlayerWithOnCompletionListener(final Activity activity, final File tempPlayFile) {
        MediaPlayer player = createTrackPlayer(activity, Uri.parse(tempPlayFile.getAbsolutePath()));

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onPlayComplete(tempPlayFile, activity);
            }
        });

        return player;
    }

    protected void onPlayComplete(File tempPlayFile, Activity activity) {
        tempPlayFile.delete();

        try {
            activity.invalidateOptionsMenu();
            ToastDisplayer.showDoneToast(activity.getApplicationContext());
            ProjectSelectionActivity projectSelectionActivity = (ProjectSelectionActivity) activity;
            projectSelectionActivity.notifyTrackPlayed();
        } catch (Exception e){
        }
    }

    protected MediaPlayer createTrackPlayer(final Activity activity, final Uri uri) {
        return MediaPlayer.create(activity, uri);
    }
}
