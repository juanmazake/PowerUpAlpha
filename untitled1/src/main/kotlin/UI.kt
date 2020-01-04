import io.reactivex.Completable
import java.awt.*
import java.util.concurrent.TimeUnit
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.BadLocationException
import javax.swing.text.DefaultCaret


fun showTextInput() {
    /*
    val frame = JWindow()
    val textField = TextField()
    textField.font = Font("Consolas", Font.BOLD, 26)
    frame.contentPane.add(textField)

//    frame.contentPane.background = Color(1f,1f,1f, .5f)
//    frame.pack()
//    frame.setSize(textField.width, textField.height)
//    val screenSize = Toolkit.getDefaultToolkit().screenSize
//    frame.setLocation(100, screenSize.height - textField.height - 10)
    frame.focusableWindowState = true
    frame.isLocationByPlatform = true
//    frame.isAlwaysOnTop = true
//    frame.opacity = .5f
    frame.isVisible = true
    frame.requestFocus()
*/

    val frame = JFrame()

//    frame.layout = FlowLayout(FlowLayout.LEFT)
//    frame.setSize(300, 300)
    frame.isUndecorated = true

//    frame.layout = BoxLayout(frame, BoxLayout.Y_AXIS)
    val textField = JTextField("asdasdasd")
    textField.border = BorderFactory.createEmptyBorder()
    textField.font = Font("Consolas", Font.BOLD, 26)
    textField.background = Color.MAGENTA
    textField.foreground = Color.CYAN
    textField.selectedTextColor = Color.MAGENTA
    textField.selectionColor = Color.CYAN
    textField.caret.blinkRate = 0
    textField.caretColor = Color.WHITE
    textField.document.addDocumentListener(Asd(frame))
    textField.margin = Insets(100,100,100,100)
    textField.caret = MyCaret()

    frame.add(textField)
    frame.pack()

    frame.isAlwaysOnTop = true
    frame.focusableWindowState = true
    frame.isVisible = true
    frame.setExtendedState(JFrame.ICONIFIED);
    frame.setExtendedState(JFrame.NORMAL);
    frame.toFront();
    frame.requestFocus();
    textField.requestFocus()
}

class Asd(val field: Window) : DocumentListener {
    override fun changedUpdate(p0: DocumentEvent?) {
        field.pack()
//        field.doLayout()
    }

    override fun insertUpdate(p0: DocumentEvent?) {
        field.pack()
    }

    override fun removeUpdate(p0: DocumentEvent?) {
        field.pack()
    }

}


fun showFrame(text: String) {
    val frame = JWindow()
    val jLabel = JLabel(text)
    jLabel.font = Font("Consolas", Font.BOLD, 26)
    frame.add(jLabel)

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
    frame.opacity = .5f
    frame.isVisible = true
//    frame.requestFocus()
//    frame.transferFocus()
//    frame.requestFocusInWindow()

    Completable.timer(1000, TimeUnit.MILLISECONDS).subscribe {
        frame.isVisible = false
        frame.dispose()
    }


//    val window = JWindow()
//    window.focus
}

class MyCaret : DefaultCaret() {
    private val mark = "<"
    @Synchronized
    override fun damage(r: Rectangle?) {
        if (r == null) {
            return
        }
        val comp = component
        val fm = comp.getFontMetrics(comp.font)
        val textWidth = fm.stringWidth(">")
        val textHeight = fm.height
        x = r.x
        y = r.y
        width = textWidth
        height = textHeight
        repaint() // calls getComponent().repaint(x, y, width, height)
    }

    override fun paint(g: Graphics) {
        val comp = component ?: return
        val dot = dot
        var r: Rectangle? = null
        r = try {
            comp.modelToView(dot)
        } catch (e: BadLocationException) {
            return
        }
        if (r == null) {
            return
        }
        if (x != r.x || y != r.y) {
            repaint() // erase previous location of caret
            damage(r)
        }
        if (isVisible) {
            val fm = comp.getFontMetrics(comp.font)
            val textWidth = fm.stringWidth(">")
            val textHeight = fm.height
            g.color = comp.caretColor
            g.drawString(mark, x, y + fm.ascent)
        }
    }

    init {
        blinkRate = 500
    }
}