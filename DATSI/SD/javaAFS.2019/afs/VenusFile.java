// Clase de cliente que define la interfaz a las aplicaciones.
// Proporciona la misma API que RandomAccessFile.
package afs;

import java.rmi.*; 
import java.io.*; 

public class VenusFile {
    public static final String cacheDir = "Cache/";
    RandomAccessFile rnd;
    ViceWriter escritor;
    ViceReader lector;
    String modo;
    Venus ven;
    String file;
    public VenusFile(Venus venus, String fileName, String mode) throws RemoteException, IOException, FileNotFoundException {
        try{
            this.ven=venus;
            this.modo = mode;
            this.file=fileName;
            rnd = new RandomAccessFile(cacheDir+fileName,mode);
            if(mode.equals("rw")){
                lector = venus.rec.download(fileName,mode,ven.callback);
                byte [] leidos;
                while((leidos = lector.read(Integer.parseInt(venus.tam))) != null){
                    rnd.write(leidos);
                }
                rnd.seek(0);
                //escritor = venus.rec.upload(fileName,mode,ven.callback);
                lector.close();
            }
        }
        catch(FileNotFoundException e){
            lector = venus.rec.download(fileName,mode,ven.callback);
            rnd = new RandomAccessFile(cacheDir+fileName,"rw");
            byte [] leidos;
            while((leidos = lector.read(Integer.parseInt(venus.tam))) != null){
                rnd.write(leidos);
            }
            rnd.close();
            rnd = new RandomAccessFile(cacheDir+fileName, "r");
            lector.close();
        }
    }
    public int read(byte[] b) throws RemoteException, IOException {
        return rnd.read(b);
    }
    public void write(byte[] b) throws RemoteException, IOException {
        rnd.write(b);
        return;
    }
    public void seek(long p) throws RemoteException, IOException {
        rnd.seek(p);
        return;
    }
    public void setLength(long l) throws RemoteException, IOException {
        return;
    }
    public void close() throws RemoteException, IOException {
        if(this.modo.equals("rw")){
            rnd.seek(0);
            byte [] leidos = new byte[Integer.parseInt(ven.tam)];
            int leido = 0;
            escritor = ven.rec.upload(file,modo,ven.callback);
            while((leido = rnd.read(leidos)) != -1){
                if(leido < Integer.parseInt(ven.tam)){
                    byte [] leidos2 = new byte[leido];
                    for(int i=0;i<leido;i++) leidos2[i]=leidos[i];
                    escritor.write(leidos2);
                }
                else{
                    escritor.write(leidos);
                }
            }
            escritor.close();
        }
        rnd.close();
    }
}

