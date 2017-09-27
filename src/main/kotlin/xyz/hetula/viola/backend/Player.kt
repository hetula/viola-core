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

package xyz.hetula.viola.backend

/**
 * Interface for Actual MediaPlayer implementation.
 *
 * @author Tuomo Heino
 * @version 27.9.2017.
 */
interface Player {

    /**
     * Called to Initialize player.<br>
     * Initializing already initialized player should do nothing.<br>
     * This method should block if possible.
     */
    fun create()

    /**
     * Called to destroy player.<br>
     * Uninitialized player should do nothing.<br>
     * This method should stop playback and do cleanup.<br>
     * This method should block if possible
     */
    fun destroy()

    /**
     * Starts playing given uri.<br>
     * Local is given as extra information, but uri should always
     * contain needed information.<br>
     * Play should not block
     */
    fun play(uri: String, local: Boolean)

    /**
     * Pauses playback if there is any
     */
    fun pause()

    /**
     * Resumes playback if there is any
     */
    fun resume()

    /**
     * Stops playback is there is any
     */
    fun stop()

    /**
     * If Song is being played.
     */
    fun isPlaying(): Boolean

    /**
     * If Song is playing but it is paused
     */
    fun isPaused(): Boolean

    /**
     * Returns current songs duration.
     * If nothing is playing returns 0
     */
    fun queryDuration(): Long

    /**
     * Returns current songs playback position.
     * If nothing is playing returns 0
     */
    fun queryPosition(): Long

    /**
     * Seeks to given position.
     * Method should automatically clip to proper limits
     */
    fun seekTo(position: Long)

    /**
     * Sets Volume to given volume level.
     * Method should check for bound limits
     */
    fun setVolume(volume: Int)

    /**
     * Returns current volume.
     * Should be between 0-100
     */
    fun getVolume(): Int

    /**
     * Returns muted state
     */
    fun isMuted(): Boolean

    /**
     * Sets muted on/off
     */
    fun setMuted(muted: Boolean)

    /**
     * If this Player implementation supports different audio devices for playback
     * i.e. Speakers, Headset etc
     */
    fun isDeviceChangePossible(): Boolean

    /**
     * Returns current Device.
     * Name and index in getDevices
     */
    fun getDevice(): Pair<String, Int>

    /**
     * Returns List of supported devices.
     * Order of given devices should not change between calls.
     * Only exception for this is if actual devices change
     */
    fun getDevices(): String

    /**
     * Sets device based on getDevices indexing.
     * Player implementation can ignore this call if changing is not possible
     */
    fun setDevice(index: Int)

}