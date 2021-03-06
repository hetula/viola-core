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

import xyz.hetula.viola.ViolaMusicLibrary

/**
 * @author Tuomo Heino
 * @version 27.9.2017.
 */
class Playlist(val id: String, val name: String) {
    private val songIds = HashMap<String, Int>()

    fun addSong(song: Song) {
        songIds.computeIfAbsent(song.id) { songIds.size }
    }

    fun removeSong(song: Song) = songIds.remove(song.id) != null

    fun hasSong(song: Song) = hasSong(song.id)

    fun hasSong(id: String) = songIds.containsKey(id)

    fun songs(library: ViolaMusicLibrary): Array<Song> {
        if (songIds.isEmpty()) {
            return emptyArray()
        }
        val songs = songIds.mapNotNull { (songId, _) -> library[songId] }.toList()
        if (songs.isEmpty()) {
            return emptyArray()
        }
        val songArr = arrayOfNulls<Song>(songs.size)

        songs.forEach { songArr[songIds[it.id]!!] = it }

        return songArr.requireNoNulls()
    }
}