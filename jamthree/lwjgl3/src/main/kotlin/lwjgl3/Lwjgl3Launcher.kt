package lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import jamthree.Jam

/** Launches the desktop (LWJGL3) application.  */
fun main() {
    Lwjgl3Application(Jam("Water", "Earth"), Lwjgl3ApplicationConfiguration().apply{
        setTitle("jamThree")
        setWindowedMode(16 * 48, 9 * 48)
        setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
    })
}
