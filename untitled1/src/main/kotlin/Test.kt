import Key.*
import io.reactivex.Completable
import org.jnativehook.GlobalScreen
import org.jnativehook.mouse.NativeMouseEvent
import org.jnativehook.mouse.NativeMouseListener
import java.awt.Font
import java.awt.Robot
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.VK_ENTER
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

import javax.swing.JLabel
import javax.swing.JWindow


fun main() {
    println("Powering Up!")
//    sendChars("Hola Juanma")

    disableLog()
    KeyboardHooks.registerKeyboardHook(hooks)

    println("Done")
}

fun disableLog() {
    val logger: Logger = Logger.getLogger(GlobalScreen::class.java.getPackage().name)
//    logger.setLevel(Level.WARNING)

// Don't forget to disable the parent handlers.
    // Don't forget to disable the parent handlers.
    logger.setUseParentHandlers(false)

    bindHook(CTRL + SHIFT + N1) {
        showFrame("Apretaste 1")
    }
    bindHook(CTRL + SHIFT + N2){
        showFrame("Ahora 2")
    }
}

private operator fun Key.plus(key: Key): KeyStroke {
    return KeyStroke(key, listOf(key))
}

private operator fun KeyStroke.plus(key: Key): KeyStroke {
    return KeyStroke(key, listOf(listOf(trigger), this.holders).flatten())
}

class KeyStroke(
    val trigger: Key,
    val holders: List<Key> = emptyList()
)

class Hook (
    val keyStroke: KeyStroke,
    val task: () -> Unit
)

val hooks = mutableListOf<Hook>()

fun bindHook(hook: KeyStroke, task: () -> Unit) {
    hooks.add(Hook(hook, task))
}

fun doSomething(){

}

class MouseListener : NativeMouseListener {
    override fun nativeMousePressed(e: NativeMouseEvent) {
        println("Key Pressed: ${e.source} ${e.button}")
    }

    override fun nativeMouseClicked(e: NativeMouseEvent) {
        println("Key Clicked: ${e.source} ${e.button}  ${e.clickCount}")
    }

    override fun nativeMouseReleased(e: NativeMouseEvent) {
        println("Key Released: ${e.source} ${e.button}")

    }

}

fun sendKey(key: Key) {
    val r = Robot()
    r.keyPress(key.keyEvent)
    r.keyRelease(key.keyEvent)
}

fun sendChars(s: String, pressReturn: Boolean = true) {
    val r = Robot()
    for (c in s) {
        val ci = KeyEvent.getExtendedKeyCodeForChar(c.toInt())
        r.keyPress(ci)
        r.keyRelease(ci)
    }
    if (pressReturn) {
        r.keyPress(VK_ENTER)
        r.keyRelease(VK_ENTER)
    }
}

fun openPM() {
    val r = Robot()
    r.keyPress(KeyEvent.VK_CONTROL)
    r.keyPress(KeyEvent.VK_SHIFT)
    r.keyPress(KeyEvent.VK_P)
    r.keyRelease(KeyEvent.VK_P)
    r.keyRelease(KeyEvent.VK_SHIFT)
    r.keyRelease(KeyEvent.VK_CONTROL)
}

fun pasteClipboard() {
    val r = Robot()
    r.keyPress(KeyEvent.VK_CONTROL)
    r.keyPress(KeyEvent.VK_V)
    r.keyRelease(KeyEvent.VK_V)
    r.keyRelease(KeyEvent.VK_CONTROL)
}

fun setClipboard(text: String) {
    val systemClipboard = Toolkit.getDefaultToolkit().systemClipboard
    val contents = StringSelection(text)
    systemClipboard.setContents(contents, null)
}



fun showFrame(text: String) {
    Completable.timer(300, TimeUnit.MILLISECONDS).subscribe {  }

    val frame = JWindow()
    val jLabel = JLabel(text)
    jLabel.font = Font("Consolas", Font.BOLD, 26)
    frame.contentPane.add(jLabel)

    //    frame.focusableWindowState = false
    //    Operation = JFrame.EXIT_ON_CLOSE
//    frame.contentPane.background = Color(1f,1f,1f, .5f)
    frame.pack()
    frame.setSize(jLabel.width, jLabel.height)
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    frame.setLocation(100, screenSize.height - jLabel.height - 10)
    frame.focusableWindowState = true
//    frame.isLocationByPlatform = true
    frame.isAlwaysOnTop = true
    frame.opacity = .8f
    frame.isVisible = true
//    frame.requestFocus()
//    frame.transferFocus()
//    frame.requestFocusInWindow()

    Completable.timer(300, TimeUnit.MILLISECONDS).subscribe {
        frame.isVisible = false
    }


//    val window = JWindow()
//    window.focus
}

