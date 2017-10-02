/*
 * MIT License
 *
 * Copyright (c) 2017 Tuomo Heino
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package xyz.hetula.viola.model.playback

import org.junit.Assert.*
import org.junit.Test
import xyz.hetula.viola.BaseTest
import xyz.hetula.viola.model.NoopPlayback
import xyz.hetula.viola.model.Song
import java.lang.IllegalStateException
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Tuomo Heino
 * @version 2.10.2017.
 */
class PlaybackTest : BaseTest() {

    @Test(expected = IllegalStateException::class)
    fun noopPlayback() {
        val noop = NoopPlayback()
        assertFalse(noop.moveToNext())
        noop()
    }

    @Test
    fun normalPlayback() {
        val pl = ArrayList<Song>()
        val song = genSong()

        pl += song
        for (i in 0..1)
            pl += genSong()
        val normal = NormalPlayback(pl, song)

        assertEquals(song, normal())
        assertTrue(normal.moveToNext())
        assertEquals(pl[1], normal())
        assertTrue(normal.moveToNext())
        assertEquals(pl[2], normal())
        assertFalse(normal.moveToNext())

        val empty = NormalPlayback(ArrayList(), song)
        assertFalse(empty.moveToNext())
    }

    @Test
    fun randomPlayback() {
        val pl = Arrays.asList(genSong(), genSong())
        val random = RandomPlayback(pl)
        assertTrue(random.moveToNext())
        assertNotNull(random())
        assertTrue(random.moveToNext())
        assertTrue(random.moveToNext())
        assertTrue(random.moveToNext())
        assertTrue(random.moveToNext())
        assertTrue(random.moveToNext())
        assertTrue(random.moveToNext())
        assertTrue(random.moveToNext())
        assertTrue(random.moveToNext())
        assertTrue(random.moveToNext())
        assertTrue(random.moveToNext())
    }

    @Test
    fun repeatSinglePlayback() {
        val song = genSong()
        val repeatSingle = RepeatSinglePlayback(song)
        assertEquals(song, repeatSingle())
        assertTrue(repeatSingle.moveToNext())
        assertEquals(song, repeatSingle())
        assertTrue(repeatSingle.moveToNext())
        assertEquals(song, repeatSingle())
        assertTrue(repeatSingle.moveToNext())
        assertEquals(song, repeatSingle())
        assertTrue(repeatSingle.moveToNext())
    }

    @Test
    fun shufflePlayback() {
        val pl = ArrayList<Song>()
        val song = genSong()

        pl += song
        for (i in 0..1)
            pl += genSong()
        val normal = ShufflePlayback(pl, song)

        assertEquals(song, normal())
        assertTrue(normal.moveToNext())
        assertTrue(normal.moveToNext())
        assertTrue(normal.moveToNext())
        assertTrue(normal.moveToNext())
        assertTrue(normal.moveToNext())
    }

    @Test
    fun singlePlayback() {
        val song = genSong()
        val single = SinglePlayback(song)
        assertEquals(song, single())
        assertFalse(single.moveToNext())
    }

}