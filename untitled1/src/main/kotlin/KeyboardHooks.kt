import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.NativeInputEvent
import org.jnativehook.keyboard.NativeKeyAdapter
import org.jnativehook.keyboard.NativeKeyEvent
import java.lang.reflect.Field

object KeyboardHooks {

    private lateinit var hooks: MutableList<Hook>

    val set = mutableSetOf<Key>()
    val map = mutableMapOf<Key, Any?>()


    fun checkForKey(key: Key): Boolean {
        return set.contains(key)
    }

    fun registerKeyboardHook(hooks: MutableList<Hook>) {
        this.hooks = hooks

        try {
            GlobalScreen.setEventDispatcher(VoidDispatchService())
            GlobalScreen.registerNativeHook()
        } catch (ex: NativeHookException) {
            println("There was a problem registering the native hook.")
            println(ex.message)
            System.exit(1)
        }

        GlobalScreen.addNativeKeyListener(GlobalKeyListenerExample())
        GlobalScreen.addNativeMouseListener(MouseListener())
    }

    class GlobalKeyListenerExample : NativeKeyAdapter() {


        override fun nativeKeyPressed(e: NativeKeyEvent) {
            println("Key Pressed: ${NativeKeyEvent.getKeyText(e.keyCode)} ${NativeKeyEvent.getModifiersText(e.modifiers)}")
            println("Map size ${map.size}")
            println("Set size ${set.size}")

            val key = e.toKey()

            hooks.firstOrNull { hook ->
                hook.keyStroke.trigger == key &&
                        set.containsAll(hook.keyStroke.holders)
            }?.run {
                task()
            }

            map[key] = null
            set.add(key)

            if (e.keyCode == NativeKeyEvent.VC_B) {
                print("Attempting to consume B event...\t")
                try {
                    val f: Field = NativeInputEvent::class.java.getDeclaredField("reserved")
                    f.isAccessible = true
                    f.setShort(e, 0x01.toShort())
                    print("[ OK ]\n")
                } catch (ex: Exception) {
                    print("[ !! ]\n")
                    ex.printStackTrace()
                }
            }
        }

        override fun nativeKeyReleased(e: NativeKeyEvent) {
            println("Key Released: ${e.keyCode} ${e.modifiers}")

            set.remove(e.toKey())

            if (e.keyCode == NativeKeyEvent.VC_B) {
                print("Attempting to consume B event...\t")
                try {
                    val f = NativeInputEvent::class.java.getDeclaredField("reserved")
                    f.isAccessible = true
                    f.setShort(e, 0x01.toShort())
                    print("[ OK ]\n")
                } catch (ex: java.lang.Exception) {
                    print("[ !! ]\n")
                    ex.printStackTrace()
                }
                sendKey(Key.DOWN)
            }

            when (e.keyCode) {
                Key.N1.nativeKeyEvent -> {
//                openPM()
//                sendChars("Bingo!")
//                setClipboard("Bingo!")
//                pasteClipboard()
                }
                Key.N0.nativeKeyEvent -> {
                    showFrame("Hola Consolas!!!")
                }
                else -> {
                }
            }
        }

    }
}