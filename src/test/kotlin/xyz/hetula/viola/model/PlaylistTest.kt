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

package xyz.hetula.viola.model

import org.junit.Assert.*
import org.junit.Test
import xyz.hetula.viola.BaseTest

/**
 * @author Tuomo Heino
 * @version 2.10.2017.
 */
class PlaylistTest : BaseTest() {
    @Test
    fun basicPlaylist() {
        val pl = Playlist("test", "Test")
        assertEquals("test", pl.id)
        assertEquals("Test", pl.name)

        val song1 = genSong()
        pl.addSong(song1)
        assertTrue(pl.hasSong(song1))

        pl.addSong(genSong())
        assertTrue(pl.hasSong("2"))
        assertTrue(pl.removeSong(song1))
        assertFalse(pl.hasSong(song1))
    }
}