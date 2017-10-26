package mutilThreadDemo;

import org.junit.Test;
import sun.reflect.generics.tree.BaseType;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * PipeWriter、PipeReader 修改 PipeOutputStream、PipeInputStream 进行实验二
 * @author Damon
 * @create 2017-10-26 15:05
 **/

public class App3 {

    @Test
    public void TestPipe() {
        try {
            PipedOutputStream out = new PipedOutputStream();
            PipedInputStream in = new PipedInputStream();
            out.connect(in);
            OutThread tOut = new OutThread(out);
            InThread tIn = new InThread(in);
            tIn.start();
            tOut.start();
            Thread.sleep(5000);
        }catch (Exception ex){ex.printStackTrace();}

    }

}
class OutThread extends Thread{
    private PipedOutputStream out;
    public OutThread(PipedOutputStream out){
        this.out = out;
    }
    public void run(){
        try {
            for (int i = 0; i < 30; i++) {
                out.write((""+i).getBytes());
                System.out.printf("write data:%s\r\n",i);
            }
            out.close();
        }catch (Exception ex){ex.printStackTrace();}
    }
}

class InThread extends Thread{
    private PipedInputStream in;
    public InThread(PipedInputStream in){
        this.in = in;
    }
    public void run(){
        try {
            byte[] bytes = new byte[20];
            int readLength = 0;
            for(readLength = in.read(bytes);readLength!=-1;readLength = in.read(bytes)){
                    String readStr = new String(bytes, 0, readLength);
                    System.out.printf("read data:%s\r\n", readStr);
            }
            in.close();
        }catch (Exception ex){ex.printStackTrace();}
    }
}