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

package xyz.hetula.viola

import xyz.hetula.viola.backend.Player
import xyz.hetula.viola.model.NoopPlayback
import xyz.hetula.viola.model.Playback
import xyz.hetula.viola.model.PlaybackMode
import xyz.hetula.viola.model.Song

/**
 * @author Tuomo Heino
 * @version 27.9.2017.
 */
class ViolaMusicPlayer(private val playerImpl: Player) {
    private var playback: Playback = NoopPlayback()
    private var playbackStartedList = ArrayList<Song>()
    private var playbackMode: PlaybackMode = PlaybackMode.NORMAL
        get

    private var nowPlaying: Song? = null
        get

    fun setPlayback(playbackMode: PlaybackMode) {
        this.playbackMode = playbackMode
        val song = nowPlaying ?: return
        playback = playbackMode(playbackStartedList, song)
    }

    fun play(song: Song, playContext: List<Song>) {
        playbackStartedList.clear()
        playbackStartedList.addAll(playContext)

        playback = playbackMode(playContext, song)

        playerImpl.play(song.uri, song.localFile)
    }

    fun next() {
        if (playback.moveToNext()) {
            val nowPlaying = playback()
            this.nowPlaying = nowPlaying
            playerImpl.play(nowPlaying.uri, nowPlaying.localFile)
        }
    }

    fun previous() {
        // TODO: Implement Previous system!
    }

    fun pause() = playerImpl.pause()

    fun resume() = playerImpl.resume()

    fun isPaused() = playerImpl.isPaused()

    fun isPlaying(): Boolean {
        return nowPlaying != null && playerImpl.isPlaying()
    }

    fun seekTo(position: Long) = playerImpl.seekTo(position)


}
