package CipherX;
import javax.swing.text.*;

public class JLimitedTextField extends PlainDocument {
    private int limit;
    JLimitedTextField(int limit) {
        super();
        this.limit = limit;
    }

    public void insertString(int offset, String  str, AttributeSet attr) throws BadLocationException {
        if (str == null) return;
        
        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
    }
}
