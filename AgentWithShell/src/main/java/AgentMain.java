import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.spi.AttachProvider;
import sun.tools.attach.MyLVM;

import java.util.List;

public class AgentMain {
    public static void main(String[] args) throws Exception {
        String jarName = AgentMain.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String id = args[0];

        System.out.println("id ==> " + id);
        System.out.println("name ==> " + jarName);

//        UseMyLVM(id, jarName);
        UseNativeVM(id, jarName);
    }


    public static void UseMyLVM(String id, String jarName) throws Exception {
        List<AttachProvider> providers = AttachProvider.providers();
        for (AttachProvider provider: providers) {
            try {
                System.out.println(provider.name());
                VirtualMachine virtualMachine = MyLVM.NewMyLVM(provider, id);
                System.out.println("attach!");
                virtualMachine.loadAgent(jarName);
                virtualMachine.detach();

                System.out.println("ends");
                return ;
            } catch (AttachNotSupportedException x) {
                x.printStackTrace();
            }
        }
    }

    public static void UseNativeVM(String id, String jarName) throws Exception {
        try {
            VirtualMachine virtualMachine = VirtualMachine.attach(id);
            System.out.println("attach!");
            virtualMachine.loadAgent(jarName);
            virtualMachine.detach();

            System.out.println("ends");
        } catch (AttachNotSupportedException x) {
            x.printStackTrace();
        }
    }
}
