package main;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

import javax.swing.JTextArea;

/* Drop Target Listener */
public class DragAndDropDropTargetListener implements DropTargetListener {
    public void dragEnter( DropTargetDragEvent e ) {}
    public void dragOver( DropTargetDragEvent e ) {}
    public void dropActionChanged( DropTargetDragEvent e ) {}
    public void dragExit( DropTargetEvent e ) {}
    public void drop( DropTargetDropEvent e ) {
        Transferable t = e.getTransferable();
        String s = "";
        try {
            if( t.isDataFlavorSupported( DataFlavor.stringFlavor ) ) {
                s = t.getTransferData( DataFlavor.stringFlavor  ).toString();
            }
        } catch( IOException ioe ) {
            ioe.printStackTrace();
        } catch( UnsupportedFlavorException ufe ) {
            ufe.printStackTrace();
        }
        System.out.println( s );
        DropTarget dt = (DropTarget)e.getSource();
        JTextArea d = ( JTextArea )dt.getComponent();
        if( s != null && s.equals( "" ) == false ) {
            d.append( s + " ");
        }
    }
}