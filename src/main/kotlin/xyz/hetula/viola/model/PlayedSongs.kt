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

/**
 * @author Tuomo Heino
 * @version 27.9.2017.
 */
internal class PlayedSongs {
    private val songs = ArrayList<Song>()
    private var currentIndex = 0

    operator fun invoke() = songs[currentIndex]

    operator fun invoke(song: Song) {
        if (currentIndex == songs.size) {
            songs.add(song)
            currentIndex = songs.size
        }
    }

    fun isNotEmpty() = songs.isNotEmpty()

    fun moveToPrevious(): Boolean {
        currentIndex--
        if (currentIndex < -1) {
            currentIndex = -1
        }
        return currentIndex >= 0
    }

    fun moveBack(): Boolean {
        currentIndex++
        if (currentIndex > songs.size) {
            currentIndex = songs.size
        }
        return currentIndex < songs.size
    }

}