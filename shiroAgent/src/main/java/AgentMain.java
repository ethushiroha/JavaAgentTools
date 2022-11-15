import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.spi.AttachProvider;
import sun.tools.attach.MyLVM;

import java.util.List;

public class AgentMain {
    public static void main(String[] args) throws Exception {
        String id = args[0];
        String jarName = args[1];


        System.out.println("id ==> " + id);
        System.out.println("jarName ==> " + jarName);

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
}
