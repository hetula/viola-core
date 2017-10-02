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

import org.junit.Assert.*
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito
import org.mockito.Mockito.*
import xyz.hetula.viola.backend.Player
import xyz.hetula.viola.model.Song
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Tuomo Heino
 * @version 2.10.2017.
 */
class ViolaMusicPlayerTest : BaseTest() {
    private val playerImplementation: Player = mock(Player::class.java, "TestPlayerImplementation")
    private val player: ViolaMusicPlayer = ViolaMusicPlayer(playerImplementation, mockExec())

    @Test
    fun initDestroy() {
        player.initialize()
        Mockito.verify(playerImplementation, times(1)).create()
        player.destroy()
        Mockito.verify(playerImplementation, times(1)).destroy()
    }

    @Test
    fun playSong() {
        val song = genSong("uri")
        player.play(song)
        Mockito.verify(playerImplementation, times(1)).play("uri", true)
        assertEquals(song, player.nowPlaying)
        assertEquals(song.addedTimestamp, player.nowPlaying?.addedTimestamp)
    }

    @Test
    fun playMultipleSongs() {
        val song = genSong()
        val playContext = ArrayList<Song>()
        for (i in 0..5) {
            playContext += genSong()
        }
        player.play(song, playContext)
        Mockito.verify(playerImplementation, times(1)).play("uri[1]", true)
        player.next()
        Mockito.verify(playerImplementation, times(1)).play("uri[2]", true)
        player.previous()
        Mockito.verify(playerImplementation, times(1)).play("uri[1]", true)
        player.next()
        player.next()
        Mockito.verify(playerImplementation, times(1)).play("uri[3]", true)
        player.previous()
        player.previous()
        player.previous()
        player.previous()
        Mockito.verify(playerImplementation, times(2)).play("uri[1]", true)
    }

    @Test
    fun playPauseResume() {
        val song = genSong()
        player.play(song)
        Mockito.verify(playerImplementation, times(1)).play("uri[1]", true)
        player.pause()
        Mockito.verify(playerImplementation, times(1)).pause()
        `when`(playerImplementation.isPlaying()).thenReturn(true)
        `when`(playerImplementation.isPaused()).thenReturn(true)
        assertTrue(player.isPaused())
        player.resume()
        Mockito.verify(playerImplementation, times(1)).resume()
        `when`(playerImplementation.isPaused()).thenReturn(false)
        assertFalse(player.isPaused())
    }

    @Test
    fun devices() {
        `when`(playerImplementation.getDevice()).thenReturn(Pair("Device_1", 0))
        assertEquals(0, player.getDevice().second)
        val deviceList = Arrays.asList("Device_1", "Device_2", "Device_43")
        `when`(playerImplementation.getDevices()).thenReturn(deviceList)
        assertEquals(deviceList, player.getDevices())
        val selIndex = AtomicInteger()
        `when`(playerImplementation.setDevice(ArgumentMatchers.anyInt())).thenAnswer {
            val index = it.getArgument<Int>(0)
            if (index >= 0 && index < deviceList.size) {
                selIndex.set(index)
            }
        }
        `when`(playerImplementation.getDevice()).thenAnswer {
            Pair(deviceList[selIndex.get()], selIndex.get())
        }
        player.setDevice(2)
        assertEquals("Device_43", player.getDevice().first)
        player.setDevice(42)
        assertEquals("Device_43", player.getDevice().first)
        player.setDevice(1)
        assertEquals("Device_2", player.getDevice().first)
        player.setDevice(-1324)
        assertEquals("Device_2", player.getDevice().first)
    }

    @Test
    fun miscReturns() {
        player.seekTo(4)
        Mockito.verify(playerImplementation, times(1)).seekTo(4)
        player.setMuted(true)
        Mockito.verify(playerImplementation, times(1)).setMuted(true)
        player.setVolume(234)
        Mockito.verify(playerImplementation, times(1)).setVolume(234)

        `when`(playerImplementation.queryDuration()).thenReturn(3)
        assertEquals(3, player.queryDuration())
        `when`(playerImplementation.queryPosition()).thenReturn(22)
        assertEquals(22, player.queryPosition())
        `when`(playerImplementation.getVolume()).thenReturn(66)
        assertEquals(66, player.getVolume())
    }

    private fun mockExec(): ExecutorService {
        val executor = mock(ExecutorService::class.java, "TestExecutor")
        Mockito.doNothing().`when`(executor).shutdown()
        `when`(executor.shutdownNow()).thenReturn(ArrayList<Runnable>())
        `when`(executor.awaitTermination(anyLong(), Mockito.any(TimeUnit::class.java))).thenReturn(true)
        `when`(executor.isShutdown).thenReturn(true)
        `when`(executor.execute(any(Runnable::class.java))).thenAnswer { it.getArgument<Runnable>(0).run() }
        return executor
    }

}