import Key.*
import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.NativeInputEvent
import org.jnativehook.keyboard.NativeKeyAdapter
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.mouse.NativeMouseWheelEvent
import org.jnativehook.mouse.NativeMouseWheelListener
import java.lang.reflect.Field

object KeyboardHooks {

    private lateinit var hooks: MutableList<Hook>

    val currentHolders = mutableSetOf<Key>()
    val map = mutableMapOf<Key, Any?>()


    fun checkForKey(key: Key): Boolean {
        return currentHolders.contains(key)
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
        GlobalScreen.addNativeMouseWheelListener(MouseWheelListener())
    }

    class GlobalKeyListenerExample : NativeKeyAdapter() {


        override fun nativeKeyPressed(e: NativeKeyEvent) {
            println("Key Pressed: ${NativeKeyEvent.getKeyText(e.keyCode)} ${NativeKeyEvent.getModifiersText(e.modifiers)}")
            println("Map size ${map.size}")
            println("Set size ${currentHolders.size}")

            val key = e.toKey()

            hooks.firstOrNull { hook ->
                hook.trigger == key &&
                        currentHolders.containsAll(hook.holders)
            }?.run {
                // Si hice algo no mando esa tecla al sistema
                eatKey(e)
                task()
            }

            map[key] = null
            currentHolders.add(key)
        }

        override fun nativeKeyReleased(e: NativeKeyEvent) {
            println("Key Released: ${e.keyCode} ${e.modifiers}")

            currentHolders.remove(e.toKey())
        }

        private fun eatKey(e: NativeKeyEvent) {
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

    class MouseWheelListener : NativeMouseWheelListener {
        override fun nativeMouseWheelMoved(e: NativeMouseWheelEvent) {
            println(e.paramString())
            val WHEEL_UP = -1
            val WHEEL_DOWN = 1
            val WHEEL_LEFT = -2
            val WHEEL_RIGHT = 2
            if (
                e.wheelRotation == WHEEL_DOWN
//                &&
//                currentHolders.containsAll(listOf(Key.SHIFT, Key.CTRL))
            ) {
//                sendKeys(CTRL + TAB)
            }
        }

    }

}
