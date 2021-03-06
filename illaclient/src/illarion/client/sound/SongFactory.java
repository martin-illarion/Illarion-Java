/*
 * This file is part of the Illarion Client.
 *
 * Copyright © 2011 - Illarion e.V.
 *
 * The Illarion Client is free software: you can redistribute i and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * The Illarion Client is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * the Illarion Client. If not, see <http://www.gnu.org/licenses/>.
 */
package illarion.client.sound;

import gnu.trove.map.hash.TIntObjectHashMap;
import illarion.client.graphics.ResourceFactory;
import illarion.common.util.FastMath;
import illarion.common.util.TableLoader;
import illarion.common.util.TableLoaderSink;

import java.util.List;

import javolution.util.FastTable;

/**
 * The song factory, so the main storage for background music. While this sounds
 * like a another child of the RecycleFactory, it is not one. This Factory works
 * independent from the RecycleFactory, because there is only one song at time
 * played anyway.
 */
public final class SongFactory implements TableLoaderSink, ResourceFactory {
    /**
     * The singleton instance of the SongFactory.
     */
    private static final SongFactory INSTANCE = new SongFactory();

    /**
     * Folder containing the sound effects.
     */
    @SuppressWarnings("nls")
    public static final String SOUND_PATH = "data/music/";

    /**
     * The index position of the song id in a table record.
     */
    private static final int TB_ID = 0;

    /**
     * The index position of the song name in a table record.
     */
    private static final int TB_NAME = 1;

    /**
     * Get the singleton instance of the sound factory.
     * 
     * @return the singleton instance
     */
    public static SongFactory getInstance() {
        return INSTANCE;
    }

    /**
     * The storage for the songs and the variations of the songs.
     */
    private TIntObjectHashMap<List<String>> songs;

    /**
     * Constructor of the factory. Starts to loading of table file containing
     * the songs.
     */
    private SongFactory() {
        // nothing to do
    }

    /**
     * Get a song from a id. This function also selects what variation of a song
     * shall be used.
     * 
     * @param id id of the song that is needed
     * @return null if the song was not found, if there is just one song with
     *         this id, the song is returned, in case there are multiple
     *         variations of this song, one is selected randomly and returned
     */
    public String getSong(final int id) {
        if ((songs != null) && (songs.contains(id))) {
            // select a variant at random
            final List<String> clipList = songs.get(id);
            final int variant = FastMath.nextRandomInt(0, clipList.size() - 1);
            return songs.get(id).get(variant);
        }
        return null;
    }

    /**
     * The initialization function prepares all prototyped that are needed to
     * work with this function.
     */
    @Override
    @SuppressWarnings("nls")
    public void init() {
        songs = new TIntObjectHashMap<List<String>>();
        new TableLoader("Songs", this);

        songs.compact();
    }

    /**
     * Process a record of the table file containing the songs. Use the data to
     * set up the instances of the songs.
     * 
     * @param line the line that is current processed
     * @param loader the table loader that processes the file and provides the
     *            data
     * @return true at all times
     */
    @Override
    public boolean processRecord(final int line, final TableLoader loader) {
        final int clipID = loader.getInt(TB_ID);
        final String clipName = SOUND_PATH + loader.getString(TB_NAME);

        List<String> clipList;
        if (!songs.contains(clipID)) {
            clipList = new FastTable<String>();
            songs.put(clipID, clipList);
        } else {
            clipList = songs.get(clipID);
        }
        clipList.add(clipName);

        return true;
    }
}
