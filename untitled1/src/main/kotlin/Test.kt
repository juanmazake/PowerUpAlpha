import Key.*
import io.reactivex.Completable
import org.jnativehook.GlobalScreen
import org.jnativehook.mouse.NativeMouseEvent
import org.jnativehook.mouse.NativeMouseListener
import java.awt.*
import java.awt.datatransfer.StringSelection
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.VK_ENTER
import java.io.File
import java.net.URI
import java.util.concurrent.TimeUnit
import java.util.logging.Logger
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener


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
    bindHook(CTRL + SHIFT + W) {
        //        showFrame("Apretaste 1")
        sendChars("Hola Juanma")
    }

    bindHook(CTRL + SHIFT + N2) {
        showFrame("Ahora 2")
        runApp("sarasa")
    }

    bindHook(CTRL + ALT + SPACE) {
        Completable.timer(500, TimeUnit.MILLISECONDS).subscribe {
            showTextInput()
        }
    }
}



fun runApp(s: String) {
    val file = File("http://www.google.com")
//        Desktop.getDesktop().open(file)
    Desktop.getDesktop().browse(URI("http://www.google.com"));
}

//operator fun Key.plus(key: Key): KeyStroke {
//    return KeyStroke(key, listOf(key))
//}

operator fun Condition.plus(key: Condition): List<Condition> {
    return listOf(this, key)
}

operator fun Key.plus(key: Key): List<Key> {
    return listOf(this, key)
}

class Hook(
    val trigger: Condition,
    val holders: List<Condition> = emptyList(),
    val task: () -> Unit
)

val hooks = mutableListOf<Hook>()

fun bindHook(hook: List<Condition>, task: () -> Unit) {
    hooks.add(createHook(hook, task))
}

fun createHook(hook: List<Condition>, task: () -> Unit) =
    Hook(hook.last(), hook.dropLast(1), task)


fun doSomething() {

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

fun sendKeys(vararg keys: Key) {
    sendKeys(keys.toList())
}

fun sendKeys(keys: Collection<Key>) {
//    val collection = keys as Collection<Key>
    val r = Robot()
    for (key in keys) {
        r.keyPress(key.keyEvent)
    }
    for (key in keys.reversed()) {
        r.keyRelease(key.keyEvent)
    }
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



