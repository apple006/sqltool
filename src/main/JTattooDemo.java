package main;

//ÊµÀý´úÂë
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class JTattooDemo extends JFrame {

    public JTattooDemo() {
        
        // setup menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.setMnemonic('F');
        JMenuItem menuItem = new JMenuItem("Exit");
        menuItem.setMnemonic('x');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK));
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0); } }); menu.add(menuItem); menuBar.add(menu); setJMenuBar(menuBar); // setup widgets
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 4));
        JScrollPane westPanel = new JScrollPane(new JTree());
        JEditorPane editor = new JEditorPane("text/plain", "Hello World");
        JScrollPane eastPanel = new JScrollPane(editor);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, westPanel,eastPanel);
        splitPane.setDividerLocation(148);
        contentPanel.add(splitPane, BorderLayout.CENTER);
        setContentPane(contentPanel);
        
        // add listeners
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        // show application
        setLocation(32, 32);
        setSize(400, 300);
        show();
    } // end CTor MinFrame
    
    public static void main(String[] args) {
        try {
            // select Look and Feel
            UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
            // start application
            new JTattooDemo();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    } // end main
    
} // end class