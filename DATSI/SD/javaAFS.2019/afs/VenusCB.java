// Interfaz de cliente que define los métodos remotos para gestionar
// callbacks
package afs;

import java.rmi.*;
import java.io.*;

public interface VenusCB extends Remote {
    public void invalidate(String fileName /* añada los parámetros que requiera */)
        throws RemoteException;
        
    /* añada los métodos remotos que requiera */
}

