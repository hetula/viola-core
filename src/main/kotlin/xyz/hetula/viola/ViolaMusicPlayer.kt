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
import xyz.hetula.viola.model.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * @author Tuomo Heino
 * @version 27.9.2017.
 */
class ViolaMusicPlayer(private val playerImpl: Player,
                       private val executor: ExecutorService = Executors.newSingleThreadExecutor()) {

    private val syncLock = Object()

    private val playedSongs = PlayedSongs()
    private var playbackStartedList = ArrayList<Song>()
    private var playback: Playback = NoopPlayback()
    private var playbackMode: PlaybackMode = PlaybackMode.NORMAL
        get

    private var nowPlaying: Song? = null
        get

    fun initialize() = sync {
        playerImpl.create()
    }

    fun destroy() = sync {
        executor.shutdown()
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS)
        } catch (ex: InterruptedException) {
            ex.printStackTrace()
        }
        executor.shutdownNow()

        playerImpl.destroy()
    }

    fun isPaused() = sync {
        playerImpl.isPaused()
    }

    fun isPlaying() = sync {
        nowPlaying != null && playerImpl.isPlaying()
    }

    fun setPlayback(playbackMode: PlaybackMode) = runInBackground {
        this.playbackMode = playbackMode
        val song = nowPlaying ?: return@runInBackground
        playback = playbackMode(playbackStartedList, song)
    }

    fun play(song: Song, playContext: List<Song>) = runInBackground {
        playbackStartedList.clear()
        playbackStartedList.addAll(playContext)

        playback = playbackMode(playContext, song)

        playInternal(song)
    }

    fun next() = runInBackground {
        if (playedSongs.moveBack()) {
            playInternal(playedSongs(), false)
        } else if (playback.moveToNext()) {
            playInternal(playback())
        }
    }

    fun previous() = runInBackground {
        if (playedSongs.isNotEmpty() && playedSongs.moveToPrevious()) {
            playInternal(playedSongs(), false)
        }
    }

    fun pause() = runInBackground {
        playerImpl.pause()
    }

    fun resume() = runInBackground {
        playerImpl.resume()
    }

    fun seekTo(position: Long) = runInBackground {
        playerImpl.seekTo(position)
    }

    private fun playInternal(song: Song, addToPlayed: Boolean = true) = sync {
        this.nowPlaying = song
        if (addToPlayed) {
            playedSongs(song)
        }
        playerImpl.play(song.uri, song.localFile)
    }

    private inline fun runInBackground(crossinline execute: () -> Unit) {
        executor.execute {
            execute()
        }
    }

    private inline fun <R> sync(runInSync: () -> R) = synchronized(syncLock) { runInSync() }
}
